package tamhoang.ldpro4.data

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.Keep
import com.github.kotvertolet.youtubejextractor.YoutubeJExtractor
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import es.dmoral.toasty.Toasty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import me.drakeet.support.toast.ToastCompat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tamhoang.ldpro4.Application
import timber.log.Timber
import tamhoang.ldpro4.BuildConfig
import tamhoang.ldpro4.R
import tamhoang.ldpro4.data.constants.Constants
import tamhoang.ldpro4.data.local.PreferencesHelper
import tamhoang.ldpro4.data.model.*
import tamhoang.ldpro4pos365.util.extension.isNetworkConnected
import tamhoang.ldpro4pos365.util.extension.toggleAndroidComponent
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Keep
class CashiersService : Service() {

    companion object {
        private const val ROOM_ID_ERROR = -101
        private const val METHOD_SYNCHRONIZATION_ORDER = "SynchronizationOrder"
        private const val METHOD_NOTIFY = "notify"
        fun getStartIntent(context: Context): Intent {
            val intent = Intent()
            intent.setClass(context, CashiersService::class.java)
            return intent
        }
    }

    @Inject lateinit var dataManager: DataManager

    @Inject lateinit var preferencesHelper: PreferencesHelper

    private val youtubeJExtractor = YoutubeJExtractor()


    private var mStartMode: Int = START_STICKY
    private val mBinder by lazy { LocalBinder() } // Binder given to clients.
    private val mAllowRebind: Boolean = true

//    private var mHubConnection: HubConnection? = null
//    private var mHubProxy: HubProxy? = null

    private val mDisposables by lazy { CompositeDisposable() }
    private val mPresentDisposables by lazy { CompositeDisposable() }

    @Inject lateinit var bus: EventBus

    private var mCacheMessage = ""

    private val mPublishSubject by lazy {
        PublishSubject.create<Pair<String, Any>>()
    }

    private val mSubjectToast by lazy {
        PublishSubject.create<String>()
    }

//    private var screenManager = ScreenManager.getInstance()

    private var justUpdate = false
    var isRestaurantCashier : Boolean? = null

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService() = this@CashiersService
    }

    /**
     * H??? th???ng s??? g???i ph????ng ph??p n??y khi d???ch v??? ???????c t???o l???p l???n ?????u,
     * ????? th???c hi???n quy tr??nh thi???t l???p m???t l???n
     * (tr?????c khi n?? c?? th??? g???i ho???c onStartCommand() ho???c onBind()).
     * N???u d???ch v??? ???? ??ang ch???y, ph????ng ph??p n??y s??? kh??ng ???????c g???i.
     * */
    override fun onCreate() {
        super.onCreate()
        (applicationContext as Application)
                .applicationComponent
                .inject(this)

        if (!bus.isRegistered(this)) bus.register(this)


//        rxSendMessage()

//        isRestaurantCashier = dataManager.ableToSendSignal()

//        rxHandlerSeverEventSendError()
//        rxHandlerCloudInvoice()
//        reConnectSignalR()

//        updateDataPresent()
    }

    /**
     * H??? th???ng s??? g???i ph????ng ph??p n??y khi m???t th??nh ph???n kh??c, ch???ng h???n nh?? m???t ho???t ?????ng,
     * y??u c???u d???ch v??? ph???i ???????c b???t ?????u, b???ng c??ch g???i startService().
     * Sau khi ph????ng ph??p n??y th???c thi, d???ch v??? s??? ???????c b???t ?????u v?? c?? th??? ch???y v?? th???i h???n trong n???n.
     * N???u b???n tri???n khai ??i???u n??y, b???n c?? tr??ch nhi???m d???ng d???ch v??? khi c??ng vi???c c???a n?? ???????c ho??n th??nh,
     * b???ng c??ch g???i stopSelf() ho???c stopService().
     * (N???u ch??? mu???n cung c???p kh??? n??ng g???n k???t, b???n kh??ng c???n tri???n khai ph????ng ph??p n??y.)
     *
     * N???u m???t th??nh ph???n b???t ?????u d???ch v??? b???ng c??ch g???i startService()
     * (k???t qu??? l?? m???t l???nh g???i t???i onStartCommand()),
     * khi ???? d???ch v??? s??? v???n ch???y t???i khi t??? n?? d???ng b???ng stopSelf()
     * ho???c m???t th??nh ph???n kh??c d???ng n?? b???ng c??ch g???i stopService().
     * */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("Start sync ...")

        if (!isNetworkConnected()) { // N???u kh??ng c?? k???t n???i m???ng. B???t broadcast l???ng nghe thay ?????i k???t n???i m???ng.
            Timber.i("Sync canceled, connection not available")
            (applicationContext as tamhoang.ldpro4.Application).isOnline = false

            toggleAndroidComponent(SyncOnConnectionAvailable::class.java, true)
            stopSelf(startId)

            mStartMode =  START_NOT_STICKY
        }

        mStartMode = START_STICKY

        return mStartMode
    }

    /**
     * H??? th???ng s??? g???i ph????ng ph??p n??y khi m???t th??nh ph???n kh??c mu???n g???n k???t v???i d???ch v???
     * (ch???ng h???n nh?? ????? th???c hi???n RPC), b???ng c??ch g???i bindService().
     * Trong tri???n khai ph????ng ph??p n??y c???a m??nh,
     * b???n ph???i cung c???p m???t giao di???n m?? c??c m??y kh??ch s??? d???ng ????? giao ti???p v???i d???ch v???,
     * b???ng c??ch tr??? v??? IBinder.
     * B???n ph???i lu??n tri???n khai ph????ng ph??p n??y,
     * nh??ng n???u b???n kh??ng mu???n cho ph??p g???n k???t th?? b???n n??n tr??? v??? r???ng.
     *
     * N???u m???t th??nh ph???n g???i bindService() ????? t???o d???ch v??? (v?? onStartCommand() kh??ng ???????c g???i),
     * khi ???? d???ch v??? s??? ch??? ch???y khi n??o m?? th??nh ph???n ???? c??n g???n k???t v???i n??.
     * Sau khi d???ch v??? ???????c b??? g???n k???t kh???i t???t c??? m??y kh??ch, h??? th???ng s??? h???y n??.
     * */
    override fun onBind(intent: Intent): IBinder {
        Timber.e("CashiersService: On Bind")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.e("CashiersService: On Unbind")
        return mAllowRebind
    }

    override fun onRebind(intent: Intent?) {
        Timber.e("CashiersService: on Rebind")
        super.onRebind(intent)
    }

    /**
     * H??? th???ng s??? g???i ph????ng ph??p n??y khi d???ch v??? kh??ng c??n ???????c s??? d???ng v?? ??ang b??? h???y.
     * D???ch v??? c???a b???n s??? tri???n khai ph????ng ph??p n??y ????? d???n d???p m???i t??i nguy??n nh?? lu???ng,
     * ?????i t?????ng theo d??i ???????c ????ng k??, h??m nh???n, v.v...
     * ????y l?? l???nh g???i cu???i c??ng m?? d???ch v??? nh???n ???????c.
     * */
    override fun onDestroy() {
//        stopSignalRRun()

        mPresentDisposables.clear()

        super.onDestroy()
    }

    class SyncOnConnectionAvailable : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION &&
                    context.isNetworkConnected()) { // L???ng nghe, khi c?? k???t n???i m???ng start service t???t broadcastReceiver.
                Timber.i("Connection is now available, triggering sync...")
//                (context.applicationContext as Application).isOnline = true

                context.toggleAndroidComponent(SyncOnConnectionAvailable::class.java, false)
                context.startService(getStartIntent(context))
            }
        }
    }


//    private fun syncPartner() =
//            Observable.concat<Any>(
//                    dataManager.syncPriceBookTreeView(),
//                    dataManager.syncPartner()
//            )

//    private fun rxSendMessage() {
//        mPublishSubject
//                .debounce(Constants.VALUE_TIME_DEBOUNCE, TimeUnit.MILLISECONDS)
//                .filter { it.second is ServerEvents }
//                .map { Pair(it.first, it.second as ServerEvents) }
//                .map {
//                    val jsonContent = it.second.jsonContent
//
//                    // Delete image
//                    jsonContent.orderDetails.map {
//                        it.productImages = emptyList()
//                        it
//                    }
//
//                    // Check UUID
//                    var offlineId = jsonContent.offlineId
//                    if (offlineId.isNullOrEmpty()) {
//                        offlineId = UUID.randomUUID().toString()
//                        jsonContent.offlineId = offlineId
//                    }
//
//                    // Update data
//                    it.second.jsonContentString = jsonContent.toString()
//
//                    it
//                }
//                .map { sendMessageOnlineOffline(mHubProxy, it.first, it.second) } //Online & Offline.
//                .subscribeOn(Schedulers.single())
//                .subscribeBy(
//                        onNext = {
//                            Timber.e("sendMessage: onNext")
//                        },
//                        onComplete = {
//                            Timber.e("sendMessage: onComplete")
//                        },
//                        onError = {
//                            Timber.e("sendMessage: onError: ${ it.printStackTrace() }")
//                        }
//                )
//
//        mSubjectToast
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribeBy(
//                        onNext = {
//                            val mess = (if (it.isNullOrEmpty()) "" else "[$it] ") + resources.getString(R.string.dong_bo_du_lieu_tu_thu_ngan_khac)
//                            Toasty.info(applicationContext, mess , Toast.LENGTH_SHORT).show()
//                        },
//                        onComplete = {},
//                        onError = {
//                            Timber.e("showtoast: onError: ${ it.printStackTrace() }")
//                        }
//                )
//    }

//    private fun rxHandlerSeverEventSendError(){
//        Observable.interval(15, TimeUnit.SECONDS)
//                .filter { isRestaurantCashier ?: true }
//                .map {
//                    justUpdate = false
//                    dataManager.selectServerEventSendErrorNonRx()?:0
//                }
//                .filter{ it is ServerEvents }
//                .map { it as ServerEvents }
//                .map {
//                    val jsonContent = it.jsonContent
//
//                    // Delete image
//                    jsonContent.orderDetails.map {
//                        it.productImages = emptyList()
//                        it
//                    }
//
//                    // Check UUID
//                    var offlineId = jsonContent.offlineId
//                    if (offlineId.isNullOrEmpty()) {
//                        offlineId = UUID.randomUUID().toString()
//                        jsonContent.offlineId = offlineId
//                    }
//
//                    // Update data
//                    it.jsonContentString = jsonContent.toString()
//
//                    it
//
//                }
//                .map { reSendMessengeOnline(mHubProxy, it) }
//                .subscribeOn(Schedulers.single())
//                .subscribeBy(
//                        onNext = {
//                            Timber.e("resendMessage: onNext")
//                        },
//                        onComplete = {
//                            Timber.e("resendMessage: onComplete")
//                        },
//                        onError = {
//                            Timber.e("resendMessage: onError: ${ it.printStackTrace() }")
//                        }
//                )
//    }

//    private fun rxHandlerCloudInvoice(){
//        var totalCount = 0
//        var countTrue = 0
//        var countFail = 0
//        Observable.interval(5, TimeUnit.MINUTES)
//                .filter {
//                    val application = applicationContext as CashierApplication
//                    application.isOnline && application.isLogin
//                }
//                .flatMap {
//                    countTrue = 0
//                    countFail = 0
//                    Observable.fromArray(dataManager.getOfflinePayDataAll().take(1)) }
//                .flatMap { it }
//                .flatMapIterable {
//                    val filterList = it.filter { it.syncCount <= PaymentData.MAX_SYNC_COUNT }
//                    totalCount = filterList.size
//                    filterList
//                }
//                .flatMap { paymentData ->
//                    // Reset data qr code.
//                    paymentData.syncCount ++
//                    paymentData.qrCodeEnable = false
//                    paymentData.order?.accountId = null
//
//                    dataManager.paymentAction(paymentData).onErrorReturn {
//                        val localizedMessage = it.localizedMessage ?: ""
//                        if (localizedMessage.indexOf(Constants.NET_RESULT_400) >= 0) paymentData.syncCount = 10
//                        updatePaymentData(paymentData)
//                        if (localizedMessage.indexOf(Constants.NET_RESULT_409) >= 0)
//                            PayResult(message = "Duplicate", code = paymentData.id)
//                        else
//                            PayResult(paymentData.order?.code ?: "OFFLINE00000000")
//                    }
//                }
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribeBy(
//                        onNext = {
//
//                            val code = it.code
//                            if (code.isNullOrEmpty()) {
//                                countFail += 1
//                            } else {
//                                countTrue += 1
//                                dataManager.deletePayData(code)
//                            }
//                            Timber.e("uploadPaymentData onNext ")
//                            val totalProcess = countFail + countTrue
//                            if (totalCount != 0 && totalCount <= totalProcess && countTrue > 0) {
//                                val content = String.format(getString(R.string.da_dong_bo_don_hang_thanh_cong_hoac_loi), countTrue, countFail)
//                                val mToast = ToastCompat.makeText(applicationContext, content, Toast.LENGTH_LONG)
//                                mToast?.setGravity(Gravity.CENTER, 0, 0)
//                                mToast?.show()
//                            }
//                        },
//                        onComplete = {
//                            Timber.e("uploadPaymentData onComplete")
//                        },
//                        onError = {
//                            Timber.e("uploadPaymentData onError ${ it.message }")
//                        }
//                )
//    }

//    private fun updatePaymentData(paymentData: PaymentData) {
//        mDisposables.add(
//                dataManager.updatePaymentData(paymentData)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .subscribeBy(
//                                onNext = {
//                                    Timber.e("updatePaymentData onNext")
//                                },
//                                onComplete = {
//                                    Timber.e("updatePaymentData onComplete")
//                                },
//                                onError = {
//                                    Timber.e("updatePaymentData onError $it")
//
//                                }
//                        )
//        )
//    }

    /**
     * method for clients.
     */
//    private fun sendMessageNow(methodName: String = METHOD_SYNCHRONIZATION_ORDER, message: Any) {
//        mDisposables.add(
//            Observable.just(message)
//                .filter { it is ServerEvents }
//                .map { it as ServerEvents }
//                .map {
//                    val jsonContent = it.jsonContent
//
//                    // Delete image
//                    jsonContent.orderDetails.map {
//                        it.productImages = emptyList()
//                        it
//                    }
//
//                    // Check UUID
//                    var offlineId = jsonContent.offlineId
//                    if (offlineId.isNullOrEmpty()) {
//                        offlineId = UUID.randomUUID().toString()
//                        jsonContent.offlineId = offlineId
//                    }
//
//                    // Update data
//                    it.jsonContentString = jsonContent.toString()
//
//                    it
//                }
//                .map { sendMessageOnlineOffline(mHubProxy, methodName, it) } //Online & Offline.
//                .subscribeOn(Schedulers.single())
//                .subscribeBy(
//                    onNext = {
//                        Timber.e("sendMessage now: onNext")
//                    },
//                    onComplete = {
//                        Timber.e("sendMessage now: onComplete")
//                    },
//                    onError = {
//                        Timber.e("sendMessage now: onError: ${ it.printStackTrace() }")
//                    }
//                )
//        )
//    }

//    private fun sendMessageOnlineOffline(hubProxy: HubProxy?, message: String, serverEvents: ServerEvents) {
//        try {
//            serverEvents.jsonContentString = serverEvents.jsonContentString.encodeBase64()
//            serverEvents.compress = true
//        } catch (e: Exception) {
//            serverEvents.compress = false
//        }
//
//        hubProxy?.let {
//            it.invoke(message, serverEvents)
//                .done {
//                    Timber.e("Send DONE!")
//                    mCacheMessage = serverEvents.toString()
//                }
//                .onError {
//                    Timber.e("Send ERROR! ${ it.printStackTrace() }")
//                    mCacheMessage = ""
//                }
//        }
//
//        // Offline.
//        dataManager.insertOrUpdateServerEvents(serverEvents)
//    }

//    fun reSendMessengeOnline(hubProxy: HubProxy?, serverEvents: ServerEvents, message: String = METHOD_SYNCHRONIZATION_ORDER){
//        try {
//            serverEvents.jsonContentString = serverEvents.jsonContentString.encodeBase64()
//            serverEvents.compress = true
//        } catch (e: Exception) {
//            serverEvents.compress = false
//        }
//
//        hubProxy?.let {
//            it.invoke(message, serverEvents)
//                    .done {
//                        Timber.e("Resend DONE!")
//                        dataManager.updateServerEventSent(serverEvents, true)
//                    }
//                    .onError {
//                        Timber.e("Resend ERROR! ${ it.printStackTrace() }")
//                    }
//        }
//    }

    /**
     * method for clients.
     */
//    fun sendMessage(methodName: String = METHOD_SYNCHRONIZATION_ORDER, message: Any, isNow: Boolean = false) {
//        if (!(isRestaurantCashier ?: dataManager.ableToSendSignal())) return
//
//        if (mCacheMessage.isNotEmpty() && mCacheMessage == message.toString()) return
//
//        if (isNow) {
//            sendMessageNow(methodName, message)
//        } else {
//            mPublishSubject.onNext(Pair(methodName, message))
//        }
//    }

    /**
     * method for clients.
     */
//    fun sendNotifyMessage(hubProxy: HubProxy? = mHubProxy, methodName: String = METHOD_NOTIFY, content: String, isNow: Boolean = false) {
//        mDisposables.add(
//                Observable.just(content)
//                        .map {
//                            var isDone = false
//                            hubProxy?.let {
//                                it.invoke(methodName, content)
//                                        .done {
//                                            isDone = true
//                                            Timber.e("Send DONE!")
//                                        }
//                                        .onError {
//                                            Timber.e("Send ERROR! ${ it.printStackTrace() }")
//                                        }
//                            }
//                            isDone
//                        }
//                        .subscribeOn(Schedulers.single())
//                        .subscribeBy(
//                                onNext = {
//                                    Timber.e("sendMessage now: onNext")
//                                },
//                                onComplete = {
//                                    Timber.e("sendMessage now: onComplete")
//                                },
//                                onError = {
//                                    Timber.e("sendMessage now: onError: ${ it.printStackTrace() }")
//                                }
//                        )
//        )
//    }

    /**
     * method for clients.
     */
//    fun sendMessageTo(methodName: String = METHOD_SYNCHRONIZATION_ORDER, receiverName: String, message: Any) {
//        // Do something
//    }
//
//    fun startSignalR(isRunLoop: Boolean = false): Observable<Any> {
//        if (dataManager.checkUINotUseSignal()) return Observable.just(false)
//
//        return Observable.create<Any> { emitter ->
//            Timber.e("Start SignalR")
//            var isHubConnect = true
//            try {
//                stopSignalRRun()
//
//                Platform.loadPlatformComponent(AndroidPlatformComponent())
//
//                // Create a new console logger.
//                val logger = Logger { message, level ->
//                    Timber.i("message = $message")
//                    Timber.i("level   = $level")
//                }
//
//                val credentials = Credentials {
//                    it.addHeader("User-Agent", BuildConfig.APPLICATION_ID)
//                    it.addHeader("Cookie", "ss-id=${preferencesHelper.getSessionId()}")
//                }
//
//                // Connect to the server.
//                mHubConnection = HubConnection(
//                        "https://signalr.pos365.vn/signalr",
//                        preferencesHelper.getSessionSetting().signalRQueryString(),
//                        true, logger)
//                mHubConnection?.credentials = credentials
//                mHubConnection?.error { Timber.e("ERROR - ${it.message} - ${it.cause}") } // Subscribe to the error event.
//                mHubConnection?.connected {
//                    Timber.i("CONNECTED")
//                    emitter.onNext(true)
//                } // Subscribe to the connected event.
//                mHubConnection?.closed { Timber.i("DISCONNECTED") } // Subscribe to the closed event.
//                mHubConnection?.received {
//                    Timber.d("RECEIVED: $it")
//                    try {
//                        val gson = GsonBuilder().create()
//                        val signalRResponse = gson.fromJson(it, object : TypeToken<SignalRResponse>() {}.type) as SignalRResponse
//                        when (signalRResponse.m){
//                            SignalRResponse.TYPE_NOTIFY -> {
//                                if (!dataManager.isRestaurantCashier()) return@received
//                                val notify = gson.fromJson(it, object : TypeToken<Notify>() {}.type) as Notify
//
//                                var content = ""
//                                notify.message.forEach { content += "$it\n" }
//                                if (notify.message.isNotEmpty() && preferencesHelper.getReceiveMessageFromServer()) {
//                                    showNotify(content)
//                                }
//                            }
//                            else -> {
//                                val realTime = gson.fromJson(it, object : TypeToken<RealTime>() {}.type) as RealTime
//
//                                if (realTime.serverEventList.isNotEmpty() && realTime.checkUpdateRealTime()
//                                        && (applicationContext as CashierApplication).receiveSignalR) {
//                                    updateRealTime(realTime)
//                                }
//                            }
//                        }
//                    } catch (e: UndeliverableException) {
//                        e.printStackTrace()
//                    }
//                } // Subscribe to the received event.
//
//                // Create the hub proxy.
//                mHubProxy = mHubConnection?.createHubProxy("SaleHub")
//                val clientTransport = ServerSentEventsTransport(mHubConnection?.logger)
//                val signalRFuture = mHubConnection
//                        ?.start(clientTransport)
//                        ?.done {
//                            Timber.i("DONE CONNECTING ...")
//                            emitter.onComplete()
//                        } // Start the connection.
//
//                signalRFuture?.get()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                isHubConnect = false
//                if (!isRunLoop) emitter.onError(e) // If show error not loop.
//            } finally {
//                emitter.onNext(isHubConnect)
//            }
//        }
//    }
//
//    private fun stopSignalR(): Observable<Any> = Observable.create<Any> { emitter ->
//        Timber.e("Stop SignalR")
//        try {
//            mDisposables.clear()
//
//            mHubConnection?.let {
//                it.disconnect()
//                it.stop()
//
//                emitter.onNext(true)
//            }
//            mHubConnection = null
//
//            emitter.onComplete()
//        } catch (e: Exception) {
//            emitter.onError(e)
//        }
//    }
//
//    fun stopSignalRRun() {
//        if (dataManager.checkUINotUseSignal()) return
//
//        Timber.e("Stop SignalR Run")
//        stopSignalR()
//            .subscribeOn(Schedulers.io())
//            .subscribeBy(
//                onNext = {
//                    Timber.e("stopSignalRRun onNext")
//                },
//                onComplete = {
//                    Timber.e("stopSignalRRun onComplete")
//                },
//                onError = {
//                    Timber.e("stopSignalRRun onError ${ it.message }")
//                }
//            )
//    }
//
//    private fun updateRealTime(realTime: RealTime) {
//        if (mCacheMessage.isNotEmpty() && mCacheMessage == realTime.serverEventList[0].toString()) return
//
//        if (realTime.serverEventList.isNotEmpty()) {
//            val serverEventServer = realTime.serverEventList[0]
//            val severVersion = serverEventServer.version
//            val localRoomId = serverEventServer.roomId
//            val localType = serverEventServer.position
//
//            fun getObservableInsertOrUpdateServerEvent(realTime: RealTime, listServerEvents: MutableList<ServerEvents>,
//                                                       serverEventServer: ServerEvents, severVersion: Int) =
//                if (listServerEvents.isEmpty()) {
//                    dataManager.insertOrUpdateServerEvent(realTime.serverEventList) { sendMessage(message = it, isNow = true) }
//                } else {
//                    val serverEventLocal = listServerEvents.first()
//                    val localVersion = serverEventLocal.version
//                    // >= or > lien quan den param isUpVersion cua ham updateServerEvents trong class RestaurantV2Presenter
//                    if (severVersion >= localVersion && serverEventServer.jsonContentString != serverEventLocal.jsonContentString) {
//                        mSubjectToast.onNext(serverEventLocal.jsonContent.roomName
//                                + if (realTime.m == "UpdateSelf") "_" else "")
//                        dataManager.insertOrUpdateServerEvent(realTime.serverEventList) { sendMessage(message = it, isNow = true) }
//                    } else {
//                        null
//                    }
//                }
//            Observable.fromArray(dataManager.getServerEvent(localRoomId, localType).take(1))
//                .flatMap { it }
//                .flatMapIterable {
//                    if (it.isEmpty()) mutableListOf(ServerEvents(roomId = ROOM_ID_ERROR))
//                    else it
//                }
//                .flatMap {
//                    if (it.roomId == ROOM_ID_ERROR)
//                        getObservableInsertOrUpdateServerEvent(realTime, mutableListOf(), serverEventServer, severVersion)
//                    else
//                        getObservableInsertOrUpdateServerEvent(realTime, mutableListOf(it), serverEventServer, severVersion)
//                                ?: Observable.just(ServerEvents(roomId = ROOM_ID_ERROR))
//                }
//                .take(1)
//                ?.subscribeOn(Schedulers.io())
//                ?.subscribeBy(
//                    onNext = {
//                        dataManager.updateStateTable(it) {
//                            if (it.roomId != ROOM_ID_ERROR) {
//                                bus.post(BusEvent.UpdateStateRoom())
//                            }
//                        }
//                    },
//                    onComplete = {
//                        Timber.e("Check Version: onComplete")
//                        mCacheMessage = serverEventServer.toString()
//                    },
//                    onError = {
//                        Timber.e("Check Version: onError: ${ it.printStackTrace() }")
//                        mCacheMessage = ""
//                    }
//                )
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onSyncGateway(event: BusEvent.SyncGatewayUpdate) {
//        val realTime = event.realTime
//        realTime.let { updateRealTime(it) }
//    }
//
////    fun reConnectSignalR() {
////        if (dataManager.checkUINotUseSignal()) return
////
////        Observable.interval(60, TimeUnit.SECONDS)
////                .filter {
////                    val application = applicationContext as CashierApplication
////                    application.isOnline  && application.isLogin
////                }
////                .filter {
////                    mHubConnection == null || mHubConnection?.state == ConnectionState.Disconnected
////                }
////                .flatMap {
////                    startSignalR()
////                }
////                .subscribeOn(Schedulers.single())
////                .subscribeBy(
////                        onNext = {
////                            Timber.e("reConnectSignalR: onNext")
////                        },
////                        onComplete = {
////                            Timber.e("reConnectSignalR: onComplete")
////                        },
////                        onError = {
////                            Timber.e("reConnectSignalR: onError: ${ it.message }")
////                        }
////                )
////
////    }
//
//    fun checkConnectSignalR(): Observable<Any> {
//        if (dataManager.checkUINotUseSignal()) return Observable.just(false)
//
//        val isConnectNetWork = (applicationContext as CashierApplication).isOnline
//        Timber.e("isConnectNetWork = $isConnectNetWork")
//        if (mHubConnection != null) {
//            val state = mHubConnection?.state
//            when(state) {
//                ConnectionState.Connecting -> {
//                    Timber.e("---> 1: Connecting")
//                    if (!isConnectNetWork) {
//                        return stopSignalR()
//                    }
//                }
//                ConnectionState.Connected -> {
//                    Timber.e("---> 2: Connected")
//                    if (!isConnectNetWork) {
//                        return stopSignalR()
//                    }
//                }
//                ConnectionState.Reconnecting -> {
//                    Timber.e("---> 3: Reconnecting")
//                    if (!isConnectNetWork) {
//                        return stopSignalR()
//                    }
//                }
//                ConnectionState.Disconnected -> {
//                    Timber.e("---> 4: Disconnected")
//                    if (isConnectNetWork) {
//                        reconnectSyncData()
//                        return startSignalR(true)
//                    }
//                }
//            }
//        } else {
//            Timber.e("---> 5: Some thing else")
//            if (isConnectNetWork) {
//                reconnectSyncData()
//                return startSignalR(true)
//            }
//        }
//
//        return Observable.just(true)
//    }
//
//    private fun reconnectSyncData() {
//        if (justUpdate && (applicationContext as CashierApplication).receiveSignalR) return
//        if (isRestaurantCashier == null)
//            isRestaurantCashier = dataManager.ableToSendSignal()
//        dataManager.reconnectSyncServerEvent()
//                .flatMap {
//                    justUpdate = true
//                    dataManager.insertOrUpdateServerEventWhenChangTable(it) {
//                        if (isRestaurantCashier != false)
//                            dataManager.updateServerEventSent(it, false)
//                    }
//                }
//                .subscribeOn(Schedulers.io())
//                .subscribeBy(
//                        onNext = {
//                            Timber.e("reconnectSyncData: onNext")
//                        },
//                        onComplete = {
//                            Timber.e("reconnectSyncData: onComplete")
////                            dataManager.getMapServerEvent(false)
//                        },
//                        onError = {
//                            Timber.e("reconnectSyncData: onError ${it.printStackTrace()}")
//                        }
//                )
//    }
//
//    /**
//     * Update data present.
//     * */
//    private fun updateDataPresent() {
//        screenManager.init(this)
//        var displays = screenManager.displays
//        if (displays.size <= 1) return
//
//        fun getDataPresent(session: Session): Observable<Session> {
//            var link = session.settings.secondMonitor
//            if (link.isNullOrEmpty()) link = Constants.YOUTUBE_RTSP
//
//            return if (link.isYoutubeUrl()) {
//                session.settings.isPresentVideo = true
//                Observable.just(link.getVideoIdFromYoutubeUrl())
//                    .map{ youtubeJExtractor.extract(it)}
//                    .map {
//                        val adaptiveVideoStreams = it.streamingData?.adaptiveVideoStreams ?: emptyList()
//                        Log.e("LINK","url live quality ${adaptiveVideoStreams.map { it.qualityLabel }}")
//
//                        session.settings.adaptiveVideoStreams = adaptiveVideoStreams
//
//                        val isCurrentLink = adaptiveVideoStreams.firstOrNull{ it.url == session.settings.linkPresent } != null
//                        if (isCurrentLink)
//                            session.settings.linkPresent
//                        else
//                            adaptiveVideoStreams.maxBy { it.fps }?.url
//                    }
//                    .map {
//                        Log.e("LINK","url live $it")
//                        session.settings.linkPresent = it
//                        session
//                    }
//            } else {
//                session.settings.isPresentVideo = false
//                session.settings.linkPresent = link
//                Observable.just(session)
//            }
//        }
//
//        mPresentDisposables.add(
//            Observable.interval(10,60 * 60, TimeUnit.SECONDS)
//                .flatMap {
//                    val session = preferencesHelper.getSessionSetting()
//                    getDataPresent(session)
//                }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribeBy(
//                    onNext = {
//                        Timber.e("updateDataPresent: onNext ")
//                        preferencesHelper.putSessionSetting(it)
//                    },
//                    onComplete = {
//                        Timber.e("updateDataPresent: onComplete")
//                    },
//                    onError = {
//                        Timber.e("updateDataPresent: onError = ${ it.message }")
//                    }
//                )
//        )
//    }
//
//    //pop up
//    private fun showNotify(message: String) {
//        Observable.just(message)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribeBy(
//                        onNext = {
//                            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
//                                    && android.provider.Settings.canDrawOverlays(applicationContext)){
//                                notifyFloatingManager.addNotify(it)
//                            }else{
//                                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
//                            }
//                        },
//                        onComplete = {
//                            Timber.e("showNotify: onComplete")
//                        },
//                        onError = {
//                            Timber.e("showNotify: onError = ${it.message}")
//                        }
//                )
//
//    }

}
