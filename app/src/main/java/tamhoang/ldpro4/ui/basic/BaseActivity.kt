package tamhoang.ldpro4pos365.ui.basic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.webkit.PermissionRequest
import android.webkit.WebView
import android.widget.*
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.zxing.integration.android.IntentIntegrator
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import tamhoang.ldpro4.R
import es.dmoral.toasty.Toasty
import io.reactivex.Observable.fromIterable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import me.drakeet.support.toast.ToastCompat
import tamhoang.ldpro4.Application
import tamhoang.ldpro4.anim.BounceInterpolator
import tamhoang.ldpro4.data.*
import tamhoang.ldpro4.data.constants.Constants
import tamhoang.ldpro4.data.local.PreferencesHelper
import tamhoang.ldpro4.data.model.*
import tamhoang.ldpro4.ui.basic.ActivityView
import tamhoang.ldpro4.util.TypefaceUtil
import tamhoang.ldpro4.util.extension.convertToCalendar
import tamhoang.ldpro4.util.extension.drawTextToBitmap
import tamhoang.ldpro4.util.extension.numberFormat
import tamhoang.ldpro4pos365.injection.component.ActivityComponent
import tamhoang.ldpro4pos365.injection.component.ConfigPersistentComponent
import tamhoang.ldpro4pos365.injection.component.DaggerConfigPersistentComponent
import tamhoang.ldpro4pos365.injection.modul.ActivityModule
import tamhoang.ldpro4pos365.ui.basic.fragment.BaseFragment
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

/**
 * @author : Hanet Electronics
 * @Skype  : chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email  : muoick@hanet.com
 * @Website: http://hanet.com/
 * @Project: Music
 * Created by CHUKIMMUOI on 1/31/2018.
 */
@Keep
abstract class BaseActivity : AppCompatActivity(), ActivityView,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    companion object {
        private const val KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID"
        private const val DATE_PICKER_DIALOG_TAG = "DatePickerDialog"
        private const val TIME_PICKER_DIALOG_TAG = "TimePickerDialog"

        private val NEXT_ID        = AtomicLong(0) // Default = 0
        private val sComponentsMap = HashMap<Long, ConfigPersistentComponent>()

        //Type of toast
        const val TOAST_NORMAL = 0
        const val TOAST_ERROR = 1
        const val TOAST_SUCCESS = 2
        const val TOAST_INFOR = 3
        const val TOAST_WARNING = 4

        //Type Status
        const val ONLINE = 0
        const val OFFLINE = 1
        const val ERROR = 2
    }

    private var mActivityId: Long = 0L

    lateinit var activityComponent: ActivityComponent

    lateinit var typefaceBold: Typeface

    lateinit var typefaceItalic: Typeface

    lateinit var typefaceLight: Typeface

    lateinit var typefaceMedium: Typeface

    lateinit var typefaceRegular: Typeface

    lateinit var typefaceThin: Typeface

    var materialDialog : MaterialDialog? = null

    private var mToast: Toast? = null
    private var mSnackBar: Snackbar? = null

    lateinit var cashiersService: CashiersService
    private var mBound = false

    private val mListFragment = mutableListOf<BaseFragment>()
    var countCacheFragment = 1 // Ap dung khi dung view pager, se co nhieu framgment.

//    val animation: Animation by lazy {
//        AnimationUtils.loadAnimation(this, R.anim.bounce)
//    }

    private val interpolator: Interpolator by lazy {
        BounceInterpolator(0.2, 20.0)
    }

    private val mDisposables by lazy { CompositeDisposable() }

    @Inject lateinit var dataManager: DataManager
    @Inject lateinit var preferencesHelper: PreferencesHelper
    @Inject lateinit var webPrint: WebView

    private val mNetworkReceiver: BroadcastReceiver by lazy { NetworkChangeReceiver() }
    private lateinit var reviewManager : ReviewManager

    var readPermission: Boolean = true
    var createPermission: Boolean = true
    var updatePermission: Boolean = true
    var deletePermission: Boolean = true
    var importPermission: Boolean = true
    var exportPermission: Boolean = true
    var viewCostPermission: Boolean = true
    var updateCostPermission: Boolean = true

    /**
     * Defines callbacks for service binding, passed to bindService()
     * */
    private val mServiceConnection: ServiceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                Timber.e("on Service Connected")
                val binder = service as CashiersService.LocalBinder
                cashiersService = binder.getService()
                mBound = true
                actionStartCashierServiceDone()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Timber.e("on Service Disconnected")
                mBound = false
            }
        }
    }
    var isStartService = true

    private val mDatePickerDialog : DatePickerDialog by lazy {
        val now = Calendar.getInstance()
        DatePickerDialog.newInstance(
                this@BaseActivity,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        )
    }

    private val mTimePickerDialog : TimePickerDialog by lazy {
        val now = Calendar.getInstance()
        TimePickerDialog.newInstance(
                this@BaseActivity,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        )
    }

    private var mBarcode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onWindowFocusChanged(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw()
        }
//        showDangerousPermissionsWithPermissionCheck()
        createTypeface()

        // Tạo ActivityComponent và sử dụng lại cache ConfigPersistentComponent
        // nếu điều này đang được gọi sau khi một sự thay đổi cấu hình.
        mActivityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NEXT_ID.getAndIncrement()

        if (sComponentsMap[mActivityId] != null) {
            Timber.i("Reusing ConfigPersistentComponent id = %d", mActivityId)
        }

        val configPersistentComponent = sComponentsMap.getOrPut(mActivityId) {// Default value.
            Timber.i("Creating new ConfigPersistentComponent id = %d", mActivityId)

            val component = (applicationContext as Application).applicationComponent

            DaggerConfigPersistentComponent.builder().applicationComponent(component).build()
        }

        activityComponent = configPersistentComponent.activityComponent(ActivityModule(this))

//        animation.interpolator = interpolator

        reviewManager = ReviewManagerFactory.create(this@BaseActivity)

        createLayout()
        createVariableStart(savedInstanceState)
        savedInstanceState?.let {
            createVariableReload(it)
        }
        createVariableView()
        createVariableNormal()

        registerNetworkBroadcastForNougat()
        if (isStartService) startService() // Bind to LocalService

        initPresent()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus && !(application as Application).showNavigationBar) hideSystemUI()
    }

    override fun onStart() {
        super.onStart()
        Timber.e("on ROOT START")
    }

    override fun onResume() {
        super.onResume()
        setPrintServ()
        Timber.e("on ROOT RESUME")

        val datePickerDialog = supportFragmentManager.findFragmentByTag(DATE_PICKER_DIALOG_TAG)
        datePickerDialog?.let { (it as DatePickerDialog).onDateSetListener = this }

        val timePickerDialog = supportFragmentManager.findFragmentByTag(TIME_PICKER_DIALOG_TAG)
        timePickerDialog?.let { (it as TimePickerDialog).onTimeSetListener = this }

        showVideoDisplay()
    }

    override fun onPause() {
        super.onPause()
        Timber.e("on ROOT PAUSE")
    }

    override fun onStop() {
        Timber.e("on ROOT STOP")

        dismissDialog()
        dismissToast()
        dismissSnackBar()

        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong(KEY_ACTIVITY_ID, mActivityId)

        // Luu lai danh sach fragment.
        for (fragment in mListFragment) {
            if (fragment.isAdded) {
                fragment.tag?.let {
                    supportFragmentManager.putFragment(outState, it, fragment)
                }
            }
        }
    }

    override fun onDestroy() {
        Timber.e("on ROOT DESTROY")
        mListFragment.clear()

        if (!isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId)

            sComponentsMap.remove(mActivityId)
        }

//        animation.cancel()

        unregisterNetworkChanges()

        stopService() // Unbind from the service

//        videoDisplay?.dismiss()
//        videoDisplay = null
//        videoMenuDisplay?.dismiss()
//        videoMenuDisplay = null
//        if (aidlUltiT2mini != null) {
//            aidlUltiT2mini!!.sendLCDDoubleString(preferencesHelper.getCurrentBranchName(),getString(R.string.xin_chao))
//            aidlUltiT2mini = null
//        }

        mDisposables.clear()

        super.onDestroy()
    }

    /**
     * Thiet lap layout XML cho activity.
     * */
    abstract fun createLayout()

    /**
     * Noi khai bao cac bien duoc truyen tu activity hoac fragment khac
     *
     * @param savedInstanceState bien nay luu cac gia tri duoc truyen co the la string, int, boolean, ...
     * */
    abstract fun createVariableStart(savedInstanceState: Bundle?)

    /**
     * Noi nhan va khoi phuc cac bien duoc truyen tu onSaveInstanceState()
     * </p>
     * @param savedInstanceState
     * */
    override fun createVariableReload(savedInstanceState: Bundle) {
        // Khoi phuc lai danh sach framgent.
        //mListFragment.clear()
        //for (fragment in supportFragmentManager.fragments) {
        //    if (fragment is BaseFragment) {
        //        Timber.e("ADD VariableReload -- 1: $fragment")
        //        mListFragment.add(fragment)
        //    }
        //}
    }

    /**
     * Noi khai bao, xu ly cac bien lien quan den UI nhu button, edit text, text view, ...
     * */
    abstract fun createVariableView()

    /**
     * Noi khai bao cac bien cac bien thong thuong de xu ly tinh toan ma khong phai UI
     *
     * Luu y: Cho phep su dung menu hay khong se duoc khai bao o day
     * */
    abstract fun createVariableNormal()

    override fun createTypeface() {
        TypefaceUtil.overrideFont(applicationContext,
                "SERIF",
                "fonts/Roboto-Regular.ttf")

        typefaceBold    = Typeface.createFromAsset(assets, "fonts/Roboto-Bold.ttf")
        typefaceItalic  = Typeface.createFromAsset(assets, "fonts/Roboto-Italic.ttf")
        typefaceLight   = Typeface.createFromAsset(assets, "fonts/Roboto-Light.ttf")
        typefaceMedium  = Typeface.createFromAsset(assets, "fonts/Roboto-Medium.ttf")
        typefaceRegular = Typeface.createFromAsset(assets, "fonts/Roboto-Regular.ttf")
        typefaceThin    = Typeface.createFromAsset(assets, "fonts/Roboto-Thin.ttf")
    }

    override fun showDialogBasic(title: String, content: String, isCancel: Boolean,
                                 positive: String, positiveCallback: ((Any) -> Unit)?,
                                 negative: String, negativeCallback: ((Any) -> Unit)?,
                                 neutral: String, neutralCallback: ((Any) -> Unit)?) {

//        val dialog = MaterialDialog(this)
//                .customView(R.layout.dialog_base_basic, noVerticalPadding = true)
//                .cornerRadius(20F)
//        dialog.btnClose.setOnClickListener {
//            dismissDialog()
//        }
//
//        dialog.tvTitle.text = title
//        dialog.tvContent.text = content
//
//        if (negative.isNotEmpty()) {
//            dialog.btnNegative.visibility = View.VISIBLE
//            dialog.btnNegative.text = negative
//            dialog.btnNegative.setOnClickListener {
//                negativeCallback?.let { it(dialog) }
//                dismissDialog()
//            }
//        } else dialog.btnNegative.visibility = View.GONE
//
//         if (neutral.isNotEmpty()) {
//             dialog.btnNeutral.visibility = View.VISIBLE
//             dialog.btnNeutral.text = neutral
//             dialog.btnNeutral.setOnClickListener {
//                 neutralCallback?.let { it(dialog) }
//                 dismissDialog()
//             }
//         } else dialog.btnNeutral.visibility = View.GONE
//
//        if (positive.isNotEmpty()) dialog.btnPositive.text = positive
//        dialog.btnPositive.setOnClickListener {
//            positiveCallback?.let { it(dialog) }
//            dismissDialog()
//        }
//        materialDialog = dialog
//        materialDialog?.show()
    }

    override fun showDialogInput(inputType: Int, title: String, content: String, hint: String, prefill: String,
                                 positive: String, positiveCallback: ((Any) -> Unit)?) {

//        val dialog = MaterialDialog(this)
//                .customView(R.layout.dialog_base_input, noVerticalPadding = true)
//                .cornerRadius(16f)
//        dialog.imgRemove.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.tvTitle.text = title
//        dialog.tvTitleInput.text = content
//        dialog.edtInput.setText(prefill)
//        dialog.btnApply.isEnabled = !prefill.isBlank()
//        dialog.edtInput.hint = hint
//        dialog.edtInput.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {}
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                dialog.btnApply.isEnabled = !p0.isNullOrBlank()
//            }
//        })
//        dialog.btnApply.setOnClickListener {
//            if (positiveCallback != null) {
//                positiveCallback(dialog.edtInput.text)
//            }
//            dialog.dismiss()
//        }
//        materialDialog = dialog
//        dialog.show()
    }

    override fun showDialogSingleChoice(title: String, collection: Collection<String>, defaultIndex: Int, isCancel: Boolean,
                                        positive: String, positiveCallback: ((Any, String) -> Unit)?,
                                        negative: String, negativeCallback: ((Any) -> Unit)?,
                                        neutral: String, neutralCallback: ((Any) -> Unit)?,
                                        cancelCallback: (() -> Unit)?) {
        if (collection.isEmpty()) return
        materialDialog = MaterialDialog(this).show {
            var mIndex =  if (collection.size - 1 >= defaultIndex) defaultIndex else 0
            var mText = collection.toList()[mIndex]

            title(text = title)

            listItemsSingleChoice(items = collection.toList(), initialSelection = mIndex, waitForPositiveButton = false) { _, index, text ->
                mIndex = index
                mText = text.toString()
            }

            if (positive.isNotEmpty()) {
                positiveButton(text = positive) { dialog ->
                  positiveCallback?.let { it(mIndex, mText) }
                }
            }
            if (negative.isNotEmpty()) {
                negativeButton(text = negative) { dialog ->
                    negativeCallback?.let { it(dialog) }
                }
            }
            if (neutral.isNotEmpty()) {
                negativeButton(text = neutral) { dialog ->
                    neutralCallback?.let { it(dialog) }
                }
            }
        }
    }

    override fun showDialogMultiChoice(title: String, collection: Collection<String>, defaultIndex: List<Int>, isCancel: Boolean,
                                       positive: String, positiveCallback: ((Any, String) -> Unit)?,
                                       negative: String, negativeCallback: ((Any) -> Unit)?,
                                       neutral: String, neutralCallback: ((Any) -> Unit)?,
                                       cancelCallback: (() -> Unit)?) {

//        val mIndex = mutableListOf<Int>()
//        var mText = ""
//        var choosen = false
//
//        val dialog = MaterialDialog(this)
//                .customView(R.layout.dialog_base_multichoice, noVerticalPadding = true)
//                .cornerRadius(16f)
//        val rcView = dialog.view.findViewById<BaseRecycleView>(R.id.recyclerView)
//
//        val listItem = collection.mapIndexed { index, string -> ItemCheckBox(string) }
//        defaultIndex.forEach {
//            listItem[it].isCheck = true
//        }
//        val adapter = MultiChoiceAdapter(this)
//
//        rcView.adapter = adapter.setAdapter{ pos, check ->
//            listItem[pos].isCheck = check
//            choosen = true
//        }
//        rcView.initLinearLayoutManager(0, false)
//        adapter.list = listItem.toMutableList()
//
//        dialog.btnClose.setOnClickListener {
//            dismissDialog()
//        }
//
//        dialog.tvTitle.text = title
//
//        if (negative.isNotEmpty()) {
//            dialog.btnNegative.visibility = View.VISIBLE
//            dialog.btnNegative.text = negative
//            dialog.btnNegative.setOnClickListener {
//                negativeCallback?.let { it(dialog) }
//                dismissDialog()
//            }
//
//        } else dialog.btnNegative.visibility = View.GONE
//
//        if (neutral.isNotEmpty()) {
//            dialog.btnNeutral.visibility = View.VISIBLE
//            dialog.btnNeutral.text = neutral
//            dialog.btnNeutral.setOnClickListener {
//                neutralCallback?.let { it(dialog) }
//                dismissDialog()
//            }
//        } else dialog.btnNeutral.visibility = View.GONE
//
//        if (positive.isNotEmpty()) dialog.btnPositive.text = positive
//        dialog.btnPositive.setOnClickListener {
//            val str = StringBuilder()
//            val chooseItemList = mutableListOf<ItemCheckBox>()
//            adapter.list.filterIsInstance<ItemCheckBox>().forEachIndexed { index, item ->
//                if (item.isCheck) {
//                    chooseItemList.add(item)
//                    mIndex.add(index)
//                }
//            }
//            chooseItemList.forEachIndexed { index, item ->
//                if (index != 0) {
//                    str.append(',')
//                    str.append('\n')
//                }
//                str.append(item.title)
//            }
//            mText = str.toString()
//            if (choosen) positiveCallback?.let { it(mIndex.toIntArray(), mText) }
//            dismissDialog()
//        }
//        materialDialog = dialog
//        materialDialog?.show()

    }

    override fun showDialogSingleChoice(title: String, array: Int, defaultIndex: Int, isCancel: Boolean,
                                        positive: String, positiveCallback: ((Any, String) -> Unit)?,
                                        negative: String, negativeCallback: ((Any) -> Unit)?,
                                        neutral: String, neutralCallback: ((Any) -> Unit)?,
                                        cancelCallback: (() -> Unit)?) {
        showDialogSingleChoice(title, resources.getStringArray(array).toMutableList(), defaultIndex, isCancel,
                positive, positiveCallback,
                negative, negativeCallback,
                neutral, neutralCallback,
                cancelCallback)
    }

    override fun showDialogMultiChoice(title: String, array: Int, defaultIndex: List<Int>, isCancel: Boolean, positive: String, positiveCallback: ((Any, String) -> Unit)?, negative: String, negativeCallback: ((Any) -> Unit)?, neutral: String, neutralCallback: ((Any) -> Unit)?, cancelCallback: (() -> Unit)?) {
        showDialogMultiChoice(title, resources.getStringArray(array).toMutableList(), defaultIndex, isCancel,
                positive, positiveCallback,
                negative, negativeCallback,
                neutral, neutralCallback,
                cancelCallback)
    }

    //============================================DIALOG==========================================//
    override fun showDialogBasic(title: Int, content: Int, isCancel: Boolean,
                                 positive: Int, positiveCallback: ((Any) -> Unit)?,
                                 negative: Int, negativeCallback: ((Any) -> Unit)?,
                                 neutral: Int, neutralCallback: ((Any) -> Unit)?) {
        showDialogBasic(getString(title), getString(content), isCancel,
                if (positive == 0) "" else getString(positive), positiveCallback,
                if (negative == 0) "" else getString(negative), negativeCallback,
                if (neutral == 0) "" else getString(neutral), neutralCallback)
    }

    override fun showDialogInput(inputType: Int, title: Int, content: Int, hint: Int, prefill: String,
                                 positive: Int, positiveCallback: ((Any) -> Unit)?) {
        showDialogInput(inputType, getString(title), getString(content),
                getString(hint), prefill,
                getString(positive), positiveCallback)
    }

    override fun showDialogSingleChoice(title: Int, array: Int, defaultIndex: Int, isCancel: Boolean,
                                        positive: Int, positiveCallback: ((Any, String) -> Unit)?,
                                        negative: Int, negativeCallback: ((Any) -> Unit)?,
                                        neutral: Int, neutralCallback: ((Any) -> Unit)?,
                                        cancelCallback: (() -> Unit)?) {
        showDialogSingleChoice(getString(title), array, defaultIndex, isCancel,
                if (positive == 0) "" else getString(positive), positiveCallback,
                if (negative == 0) "" else getString(negative), negativeCallback,
                if (neutral == 0) "" else getString(neutral), neutralCallback,
                cancelCallback)
    }

    override fun showDialogMultiChoice(title: Int, array: Int, defaultIndex: List<Int>, isCancel: Boolean, positive: Int, positiveCallback: ((Any, String) -> Unit)?, negative: Int, negativeCallback: ((Any) -> Unit)?, neutral: Int, neutralCallback: ((Any) -> Unit)?, cancelCallback: (() -> Unit)?) {
        showDialogMultiChoice(getString(title), array, defaultIndex, isCancel,
                if (positive == 0) "" else getString(positive), positiveCallback,
                if (negative == 0) "" else getString(negative), negativeCallback,
                if (neutral == 0) "" else getString(neutral), neutralCallback,
                cancelCallback)
    }

    override fun showDialogSingleChoice(title: Int, collection: Collection<String>, defaultIndex: Int, isCancel: Boolean,
                                        positive: Int, positiveCallback: ((Any, String) -> Unit)?,
                                        negative: Int, negativeCallback: ((Any) -> Unit)?,
                                        neutral: Int, neutralCallback: ((Any) -> Unit)?,
                                        cancelCallback: (() -> Unit)?) {
        showDialogSingleChoice(getString(title), collection, defaultIndex, isCancel,
                if (positive == 0) "" else getString(positive), positiveCallback,
                if (negative == 0) "" else getString(negative), negativeCallback,
                if (neutral == 0) "" else getString(neutral), neutralCallback,
                cancelCallback)
    }

    override fun showDialogMultiChoice(title: Int, collection: Collection<String>, defaultIndex: List<Int>, isCancel: Boolean, positive: Int, positiveCallback: ((Any, String) -> Unit)?, negative: Int, negativeCallback: ((Any) -> Unit)?, neutral: Int, neutralCallback: ((Any) -> Unit)?, cancelCallback: (() -> Unit)?) {
        showDialogMultiChoice(getString(title), collection, defaultIndex, isCancel,
                if (positive == 0) "" else getString(positive), positiveCallback,
                if (negative == 0) "" else getString(negative), negativeCallback,
                if (neutral == 0) "" else getString(neutral), neutralCallback,
                cancelCallback)
    }

    override fun showDialogProgress(title: String, content: String, isHorizontal: Boolean, isCancelTouchOutside : Boolean) {
//        dismissDialog()
//        if (this.hasWindowFocus()) {
//            val builder = MaterialDialog(this)
//                    .customView(R.layout.partial_progress_sync, noVerticalPadding = true)
//                    .cornerRadius(16f)
//                    .cancelOnTouchOutside(isCancelTouchOutside)
//            val titleSetupData = builder.view.findViewById<TextView>(R.id.title_setup_data)
//            titleSetupData.text = title
//            val titlePleaseWait = builder.view.findViewById<TextView>(R.id.title_please_wait)
//            titlePleaseWait.text = content
//            materialDialog = builder
//            materialDialog?.show()
//
//        }
    }

    override fun showDialogProgressCircle(title: String, content: String, isCancelTouchOutside: Boolean) {
        showDialogProgress(title, content, false, isCancelTouchOutside)
    }

    override fun showDialogProgressHorizontal(title: String, content: String, isCancelTouchOutside: Boolean) {
        showDialogProgress(title, content, true, isCancelTouchOutside)
    }

    override fun showDialogProgressCircle(title: Int, content: Int, isCancelTouchOutside : Boolean ) {
        showDialogProgressCircle(getString(title), getString(content), isCancelTouchOutside)
    }

    override fun showDialogProgressHorizontal(title: Int, content: Int, isCancelTouchOutside : Boolean) {
        showDialogProgressHorizontal(getString(title), getString(content), isCancelTouchOutside)
    }

    override fun shoDialogBasicLongContent(title: Int, content: Int, positive: Int, negative: Int) {
        shoDialogBasicLongContent(
                getString(title), getString(content),
                getString(positive), if (negative == 0) "" else getString(negative)
        )
    }

    override fun shoDialogBasicLongContent(title: String, content: String, positive: String, negative: String) {
        dismissDialog()

        materialDialog = MaterialDialog(this).show {
            title(text = title)

            message(text = content)

            if (positive.isNotEmpty()) {
                positiveButton(text = positive)
            }
            if (negative.isNotEmpty()) {
                negativeButton(text = negative)
            }
        }
    }

    override fun showDialogDelete(
        title: Int,
        content: Int,
        icon: Int?,
        cancelOnTouchSide: Boolean,
        positive: Int,
        positiveCallback: ((Any) -> Unit)?,
        negative: Int,
        negativeCallback: ((Any) -> Unit)?
    ) {
//        showDialogDelete(
//            title = getString(if (title != 0) title else R.string.xoa),
//            content = getString(if (content != 0) content else R.string.thong_bao_xoa_vat_the),
//            icon = ResourcesCompat.getDrawable(resources, icon ?: R.drawable.ic_delete_white_40dp, null),
//            cancelOnTouchSide = cancelOnTouchSide,
//            positive = getString(if (positive != 0) positive else R.string.xac_nhan),
//            positiveCallback = positiveCallback,
//            negative = getString(if (negative != 0) negative else R.string.huy),
//            negativeCallback = negativeCallback
//        )
    }

    override fun showDialogDelete(
        title: String,
        content: String,
        icon: Drawable?,
        cancelOnTouchSide: Boolean,
        positive: String,
        positiveCallback: ((Any) -> Unit)?,
        negative: String,
        negativeCallback: ((Any) -> Unit)?
    ) {
//        val dialog = MaterialDialog(this)
//            .customView(R.layout.popup_delete_default_v2, noVerticalPadding = true)
//            .cancelOnTouchOutside(cancelOnTouchSide)
//            .cornerRadius(16f)
//
//        val titleView = dialog.view.findViewById<TextView>(R.id.tvTitle)
//        val contentView = dialog.view.findViewById<TextView>(R.id.tvContent)
//        val iconView = dialog.view.findViewById<ImageView>(R.id.imgIcon)
//        val negativeButton = dialog.view.findViewById<Button>(R.id.btnCancel)
//        val positiveButton = dialog.view.findViewById<Button>(R.id.btnConfirm)
//        val imgClose = dialog.view.findViewById<ImageView>(R.id.imgClose)
//
//        titleView.text = title
//        contentView.text = content
//        iconView.background = icon
//        imgClose.setOnClickListener {
//            dialog.dismiss()
//        }
//        negativeButton.text = negative
//        negativeButton.setOnClickListener {
//            negativeCallback?.let { it(dialog) }
//            dialog.dismiss()
//        }
//        positiveButton.text = positive
//        positiveButton.setOnClickListener {
//            positiveCallback?.let { it(dialog) }
//            dialog.dismiss()
//        }
//
//        materialDialog = dialog
//        materialDialog?.show()
    }

    fun getBottomOrDialog(peekHeight: Int = R.dimen.bottom_sheet_height_300dp) = if(isWh960Screen()) MaterialDialog(this)
    else MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT))

    override fun dismissDialog() {
        materialDialog?.dismiss()
        materialDialog = null
    }

    override fun hideDialog() {
        materialDialog?.hide()
    }
    //============================================DIALOG==========================================//

    //=============================================TOAST==========================================//
    override fun showToast(content: String, isLongTime: Boolean, type: Int, checkVersion: Boolean, gravity: Int) {
        dismissToast()

        val time = if (isLongTime) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        mToast = when(type) {
            TOAST_NORMAL  -> Toasty.normal(this, content, time)
            TOAST_ERROR   -> Toasty.error(this, content, time)
            TOAST_SUCCESS -> Toasty.success(this, content, time)
            TOAST_INFOR   -> Toasty.info(this, content, time)
            TOAST_WARNING -> Toasty.warning(this, content, time)
            else -> ToastCompat.makeText(this, content, time)
        }
        mToast?.setGravity(gravity, 0, 0)

        if (checkVersion && Build.VERSION.SDK_INT == 25)
            ToastCompat.makeText(this, content, time).show()
        else
            mToast?.show()
    }

    override fun showToast(content: Int, isLongTime: Boolean, type: Int, checkVersion: Boolean, gravity: Int) {
        showToast(getString(content), isLongTime, type, checkVersion, gravity)
    }

    override fun dismissToast() {
        mToast?.cancel()
        mToast = null
    }
    //=============================================TOAST==========================================//

    //===========================================SNACK BAR========================================//
    override fun showSnackBar(view: View, message: String, typeTime: Int,
                              actionName: String?, onClickListener: View.OnClickListener?) {
        dismissSnackBar()

        mSnackBar = Snackbar.make(view, message, typeTime).setAction(actionName, onClickListener)
        mSnackBar?.setActionTextColor(resources.getColor(R.color.primaryColor))
        mSnackBar?.show()
    }

    override fun showSnackBar(view: View, message: Int, typeTime: Int,
                              actionName: Int?, onClickListener: View.OnClickListener?) {
        showSnackBar(view, getString(message), typeTime,
                actionName?.let { getString(actionName) }, onClickListener)
    }

    override fun dismissSnackBar() {
        mSnackBar?.dismiss()
        mSnackBar = null
    }
    //===========================================SNACK BAR========================================//

    //============================================KEYBOARD========================================//
    override fun showSoftKeyboard(view: View) {
        view.requestFocus()
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun hideSoftKeyboard(view: View?) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        view?.clearFocus()
    }

    override fun hideSoftKeyboard() {
        hideSoftKeyboard(currentFocus)
    }

    //============================================KEYBOARD========================================//

    //============================================FRAGMENT========================================//
    override fun reDisplayFragment(layoutContainer: Int, tag: String, bundle: Bundle, isSaveCache: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = Fragment.instantiate(this@BaseActivity, tag, bundle) as BaseFragment

        addFragment(fragment = fragment, isRefresh = true)
        fragmentTransaction.replace(layoutContainer, fragment, tag)

        if (isSaveCache) fragmentTransaction.addToBackStack(tag)

        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun displayFragment(layoutContainer: Int, fragmentManager: FragmentManager,
                                 tag: String, bundle: Bundle, isSaveCache: Boolean) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = Fragment.instantiate(this@BaseActivity, tag, bundle) as BaseFragment

        addFragment(fragment = fragment, isRefresh = true)
        fragmentTransaction.replace(layoutContainer, fragment, tag)

        if (isSaveCache) fragmentTransaction.addToBackStack(tag)

        fragmentTransaction.commit()
    }

    override fun displayFragment(layoutContainer: Int, tag: String, bundle: Bundle, isSaveCache: Boolean) {
        displayFragment(layoutContainer, supportFragmentManager, tag, bundle, isSaveCache)
    }

    override fun findingFragment(layoutId: Int, fragmentManager: FragmentManager): Fragment? {
        return fragmentManager.findFragmentById(layoutId)
    }

    override fun findingFragment(layoutId: Int): Fragment? {
        return findingFragment(layoutId, supportFragmentManager)
    }

    override fun findingFragment(tag: String, fragmentManager: FragmentManager): Fragment? {
        return fragmentManager.findFragmentByTag(tag)
    }

    override fun findingFragment(tag: String): Fragment? {
        return findingFragment(tag, supportFragmentManager)
    }

    override fun addFragment(fragment: BaseFragment, isRefresh: Boolean): Boolean {
        synchronized(mListFragment) {
            if (isRefresh) mListFragment.clear()

            val fistSize = mListFragment.size

            mListFragment.add(fragment)

            val lastSize = mListFragment.size

            return lastSize > fistSize
        }
    }

    override fun addFragment(fragments: List<BaseFragment>, isRefresh: Boolean): Int {
        var output = 0

        for (fragment in fragments) {
            if (addFragment(fragment, isRefresh)) {
                output += 1
            }
        }

        return output
    }

    // https://stackoverflow.com/questions/18305945/how-to-resume-fragment-from-backstack-if-exists
    // https://stackoverflow.com/questions/33237235/remove-all-fragments-from-container
    override fun displayMultiFragment(layoutContainer: Int, fragmentManager: FragmentManager,
                                      tag: String, tagParent: String,
                                      bundle: Bundle) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        overridePendingSlideFromRightToLeftFragment(fragmentTransaction)

        if (tagParent.isNotEmpty()) {
            findingFragment(tagParent, fragmentManager)?.let {
                bundle.putString(BaseFragment.BUNDLE_FRAGMENT_PARENT_TAG, tagParent)
                fragmentTransaction.hide(it)
            }
        }

        if (tag.isNotEmpty()) {
            val fragment = findingFragment(tag, fragmentManager)
            if (fragment != null) {
                if (fragment.isAdded) {
                    fragmentTransaction.show(fragment)
                }
            } else {
                val fragment = Fragment.instantiate(this@BaseActivity, tag, bundle) as BaseFragment
                addFragment(fragment)
                fragmentTransaction.add(layoutContainer, fragment, tag)
            }
        }
        fragmentTransaction.commit()

    }

    override fun displayMultiFragment(layoutContainer: Int,
                                      tag: String, tagParent: String,
                                      bundle: Bundle) {
        displayMultiFragment(layoutContainer, supportFragmentManager, tag, tagParent, bundle)
    }

    override fun backStackFragment() : Boolean {
        val size = mListFragment.size
        if (size > 0) {
            synchronized(mListFragment) {
                mListFragment.removeAt(size - 1).let {
                    it.backToParentFragment()
                    return true
                }
            }
        }

        return false
    }

    override fun backStackToTopFragment() : Boolean {
        val size = mListFragment.size
        if (size > 1) {
            synchronized(mListFragment) {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                for (index in (size - 1) downTo 1) {
                    fragmentTransaction.remove(mListFragment[index])
                    mListFragment.removeAt(index)
                }
                fragmentTransaction.show(mListFragment[0])

                fragmentTransaction.commit()
                return true
            }
        }

        return false
    }

    override fun onBackPressed() {
        if (mListFragment.size <= countCacheFragment) {
            super.onBackPressed()
            overridePendingSlideFromLeftToRight()
        } else {
            backStackFragment()
        }
        hideSoftKeyboard()
    }

    override fun onBackPressedV2() {
        super.onBackPressed()
        overridePendingSlideFromLeftToRight()
        hideSoftKeyboard()
    }

    override fun onBackPressedV3() {
        if (mListFragment.size <= countCacheFragment) {
            super.onBackPressed()
            overridePendingSlideFromTopToBottom()
        } else {
            backStackFragment()
        }
        hideSoftKeyboard()
    }

    //===========================================FRAGMENT=========================================//

    //==========================================PERMISSION========================================//
    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

//        onRequestPermissionsResult(requestCode, grantResults)
    }

//    @NeedsPermission(
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA)
    override fun showDangerousPermissions() {

    }

//    @OnShowRationale(
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA)
    override fun showRationalForDangerousPermission() {

    }

//    @OnPermissionDenied(
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA)
    override fun onDangerousPermissionDenied() {

    }
//
//    @OnNeverAskAgain(
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA)
    override fun onDangerousPermissionNeverAskAgain() {

    }
//
//    @NeedsPermission(Manifest.permission.CAMERA)
//    override fun showCamera() {
//
//    }
    //==========================================PERMISSION========================================//

    //============================================SERVICE=========================================//
    override fun startService() {
        bindService(
                CashiersService.getStartIntent(this),
                mServiceConnection,
                Context.BIND_AUTO_CREATE)
    }

    override fun stopService() {
        if (mBound) {
            unbindService(mServiceConnection)
            mBound = false
        }
    }
    //============================================SERVICE=========================================//

    //=================================ANIMATION WHEN CHANGE ACTIVITY=============================//
    override fun finish() {
        super.finish()
        overridePendingSlideFromRigthToLeft()
    }

    override fun goToActivity(activityOld: Activity, intent: Intent, isFinish: Boolean, typeAnimation: Int ) {
        startActivity(intent).apply {
            when (typeAnimation) {
                ActivityView.TYPE_ANIMATION_SLIDE_BOTTOM_TO_TOP -> overridePendingSlideFromBottomToTop()
                else ->  overridePendingSlideFromRigthToLeft()
            }
            if (isFinish) activityOld.finish()
        }
    }

    override fun overridePendingSlideFromRigthToLeft() {
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun overridePendingSlideFromBottomToTop() {
//        overridePendingTransition(R.anim.slide_from_top, R.anim.slide_to_bottom)
    }

    override fun overridePendingSlideFromRightToLeftFragment(fragmentTransaction: FragmentTransaction) {
//        fragmentTransaction.setCustomAnimations(
//                R.anim.slide_from_right,
//                R.anim.slide_to_left,
//                R.anim.slide_from_left,
//                R.anim.slide_to_right
//        )
    }

    override fun overridePendingSlideFromLeftToRight() {
//        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    override fun overridePendingSlideFromTopToBottom() {
//        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top)
    }

    override fun overridePendingSlideFromLeftToRightFragment(fragmentTransaction: FragmentTransaction) {
//        fragmentTransaction.setCustomAnimations(
//                R.anim.slide_from_left,
//                R.anim.slide_to_right,
//                R.anim.slide_from_right,
//                R.anim.slide_to_left
//        )
    }

    //=================================ANIMATION WHEN CHANGE ACTIVITY=============================//

    //==========================================ACTION BAR========================================//
    private var isToolbarSearch = false

    override fun setTitleActionBar(title: CharSequence) {
        if (isToolbarSearch)
            toolbar_title?.text = title
        else
            supportActionBar?.title = title
    }

    override fun setSubtitleActionBar(subtitle: CharSequence) {
        supportActionBar?.subtitle = subtitle
    }

    override fun setIconActionBar(icon: Int) {
        supportActionBar?.setIcon(icon)
    }

    override fun displayIconHomeActionbar(isShow: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isShow)
        supportActionBar?.setDisplayShowHomeEnabled(isShow)
        supportActionBar?.setHomeButtonEnabled(isShow)
    }

    override fun showToolbarSearch(isShow: Boolean){
        isToolbarSearch = isShow
        toolbar_layoutSearch?.visibility = if (isShow) View.VISIBLE else View.GONE
        if (isShow) {
            toolbar_etSearch?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
//                    if (s?.length ?: 0 == 1) toolbar_imgIconSearch?.setImageResource(R.drawable.ic_close_black_24dp)
//                    else if (s?.length ?: 0 == 0)  toolbar_imgIconSearch.setImageResource(R.drawable.ic_search_white_24dp_v2)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            toolbar_imgIconSearch?.setOnClickListener {
                toolbar_etSearch.setText("")
            }
        }
    }
    //==========================================ACTION BAR========================================//

    override fun <T> handlerMultiUIwh960(wh960: () -> T, default: () -> T) : T {
        return handlerMultiUI(wh960, wh960, default)
    }

    override fun <T> handlerMultiUIh960Default(w960: () -> T, h960default: () -> T): T {
        return handlerMultiUI(w960, h960default, h960default)
    }

    override fun <T> handlerMultiUI(w960: () -> T, h960: () -> T, default: () -> T): T {
        return when(resources.getInteger(R.integer.type_ui_screen)) {
            0 -> default()
            1 -> h960()
            2 -> w960()
            else -> default()
        }
    }

    override fun isWh960Screen() : Boolean = (resources.getInteger(R.integer.type_ui_screen) != 0)
    override fun isW960Screen() : Boolean = (resources.getInteger(R.integer.type_ui_screen) == 2)

//    override fun getHtmlPrint(jsonContent: JsonContent, typeHeader: String,
//                              code: String, number: String): String {
//        return PrintService.getHtmlPrintCustom(this, preferencesHelper, dataManager,
//                jsonContent, typeHeader, code, number, preferencesHelper.getHtmlPrint())
//    }

    fun checkServiceInit() = ::cashiersService.isInitialized

    override fun registerNetworkBroadcastForNougat() {
        registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun networkAction(state: Boolean) {
        if (checkServiceInit() && state) {
            // do some thing.
        }
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        setDateSetResultListener(view, year, monthOfYear, dayOfMonth)
    }

    override fun onTimeSet(view: TimePickerDialog, hourOfDay: Int, minute: Int, second: Int) {
        setTimeSetResultListener(view, hourOfDay, minute, second)
    }

    override fun setDateSetResultListener(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        Timber.e("Child do something date.")
    }

    override fun setTimeSetResultListener(view: TimePickerDialog, hourOfDay: Int, minute: Int, second: Int) {
        Timber.e("Child do something time.")
    }

    override fun showDialogDate(date: Date, enableFuture : Boolean?) {
        val calendar = Calendar.getInstance()
        calendar.time = date

        showDialogDate(calendar, enableFuture)
    }

    override fun showDialogDate(date: Calendar, enableFuture: Boolean?) {
        if (mDatePickerDialog.isAdded || mTimePickerDialog.isAdded) return
        with(mDatePickerDialog) {
            initialize(
                    this@BaseActivity,
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH)
            )
            if (enableFuture == false) maxDate = Calendar.getInstance()

            accentColor = ContextCompat.getColor(this@BaseActivity, R.color.primaryColor)

            show(supportFragmentManager, DATE_PICKER_DIALOG_TAG)
        }
    }

    override fun showDialogDateNow(enableFuture: Boolean?) {
        showDialogDate(Calendar.getInstance(), enableFuture)
    }

    override fun showDialogTime(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date

        showDialogTime(calendar)
    }

    override fun showDialogTime(date: Calendar) {
        if (mTimePickerDialog.isAdded || mDatePickerDialog.isAdded) return
        with(mTimePickerDialog) {
            initialize(
                    this@BaseActivity,
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE),
                    date.get(Calendar.SECOND),
                    true
            )

            accentColor = ContextCompat.getColor(this@BaseActivity, R.color.primaryColor)

            show(supportFragmentManager, TIME_PICKER_DIALOG_TAG)
        }
    }

    override fun showDialogTimeNow() {
        showDialogTime(Calendar.getInstance())
    }

    override fun showDialogDateV2(enableFutureDate : Boolean?, listener : (Date) -> Unit){
//        val dialog = MaterialDialog(this)
//                .customView(R.layout.dialog_choose_date, noVerticalPadding = true, scrollable = true)
//                .cornerRadius(20f)
//
//        val calendarView = dialog.view.findViewById<DateRangeCalendarView>(R.id.calendar_View)
//        if (enableFutureDate == null) { // truong hop khong chon ngay qua khu
//            val maxTime = Calendar.getInstance()
//            maxTime.set(Calendar.YEAR, maxTime.getActualMaximum(Calendar.YEAR))
//            calendarView.setSelectableDateRange(Calendar.getInstance(), maxTime)
//        } else if (!enableFutureDate) {
//            val minTime = Calendar.getInstance()
//            minTime.time = Date(0)
//            calendarView.setSelectableDateRange(minTime, Calendar.getInstance())
//        } else {
//            val minTime = Calendar.getInstance()
//            minTime.time = Date(0)
//            val maxTime = Calendar.getInstance()
//            maxTime.set(Calendar.YEAR, maxTime.getActualMaximum(Calendar.YEAR))
//            calendarView.setSelectableDateRange(minTime, maxTime)
//        }
//        calendarView.setCalendarListener(object : CalendarListener {
//            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {}
//
//            override fun onFirstDateSelected(startDate: Calendar) {
//                listener(startDate.time)
//                dialog.dismiss()
//            }
//
//        })
//        dialog.show()
    }

    override fun showDialogDateV2(startDate: Date?, endDate : Date?, enableFutureDate: Boolean, listener: (Date, Date) -> Unit) {
//        val dialog = MaterialDialog(this)
//                .customView(R.layout.dialog_choose_date, noVerticalPadding = true, scrollable = true)
//                .cornerRadius(20f)
//
//        val calendarView = dialog.view.findViewById<DateRangeCalendarView>(R.id.calendar_View)
//
//        val minTime = Calendar.getInstance()
//        minTime.time = Date(0)
//        val maxTime = Calendar.getInstance()
//        maxTime.set(Calendar.YEAR, maxTime.getMaximum(Calendar.YEAR))
//        if (!enableFutureDate) {
//            calendarView.setSelectableDateRange(minTime, Calendar.getInstance())
//        }
//        if (startDate != null) {
//            calendarView.setSelectableDateRange(startDate.convertToCalendar(),
//                    if (!enableFutureDate) Calendar.getInstance() else maxTime)
//            calendarView.setSelectedDateRange(startDate.convertToCalendar(), endDate?.convertToCalendar())
//        }
//        else {
//            if (endDate != null) {
//                calendarView.setSelectableDateRange(minTime, endDate.convertToCalendar())
//            }
//        }
//
//        calendarView.setCalendarListener(object : CalendarListener {
//            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
//                listener(startDate.time,endDate.time)
//                dialog.dismiss()
//            }
//
//            override fun onFirstDateSelected(startDateCallBack : Calendar) {
//                if ((startDate != null && endDate != null)
//                        || (startDate == null && endDate == null)
//                        || (startDate == null && endDate != null)) {
//                    listener(startDateCallBack.time, startDateCallBack.time)
//                    dialog.dismiss()
//                }
//            }
//
//        })
//        materialDialog = dialog
//        dialog.show()
    }

    override fun slideUp(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, view.height.toFloat(), 0f)
        animate.duration = 300
        animate.fillAfter = false
        view.startAnimation(animate)
    }

    override fun slideDown(view: View) {
        val animate = TranslateAnimation(0f, 0f, 0f, view.height.toFloat())
        animate.duration = 300
        animate.fillAfter = false
        view.startAnimation(animate)
        view.visibility = View.GONE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateNavigationCount() {
//        try {
//            val count = event.count
//            if (count <= 0) {
//                drawerCountView?.text = "0"
//                drawerCountView?.visibility = View.GONE
//            } else {
//                val countStr = if (count >= 100) {
//                    "99+"
//                } else {
//                    "$count"
//                }
//                drawerCountView?.text = countStr
//                drawerCountView?.visibility = View.VISIBLE
//
//                animation.cancel()
//                drawerCountView?.startAnimation(animation)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    override fun showMessageCheckInternet() {
//        showToast(R.string.kiem_tra_ket_noi_internet, false, TOAST_ERROR)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.action == KeyEvent.ACTION_DOWN) {
            if (event.keyCode != KeyEvent.KEYCODE_ENTER) {
                if (event.unicodeChar != 0) mBarcode += event.unicodeChar.toChar()
                super.dispatchKeyEvent(event)
            } else if (event.scanCode != 0) {
                actionSearchBarcode(mBarcode)
                mBarcode = ""
                false
            } else false
        } else {
            super.dispatchKeyEvent(event)
        }
    }

    override fun actionSearchBarcode(barcode: String) {

    }

    override fun checkCamera() =
            packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) &&
                    android.hardware.Camera.getNumberOfCameras() > 0

    override fun goScanActivity() {
//        if (checkCamera()) {
//            val integrator = IntentIntegrator(this)
//            integrator.captureActivity = ToolbarCaptureActivity::class.java
//            integrator.setPrompt(getString(R.string.quet_ma_barcode_hoac_qrcode))
//            integrator.setOrientationLocked(false)
//            integrator.setBeepEnabled(true)
//            integrator.initiateScan()
//        } else {
//            showToast(R.string.thong_bao_khong_ho_tro_camera,false, TOAST_WARNING)
//        }
    }

    override fun checkCameraPermission(): Boolean {
        val permission = android.Manifest.permission.CAMERA
        val res = checkCallingOrSelfPermission(permission)
        return (res == PackageManager.PERMISSION_GRANTED)
    }

    override fun actionStartCashierServiceDone() {

    }

    override fun checkDemoVNPay(): Boolean {
//        return preferencesHelper.getSessionSetting().currentRetailer.id == Constants.VNPAY_DEMO_CODE
        return false
    }

    override fun getWidthItemProduct(): Int {
        return if (checkDemoVNPay()) R.dimen.main_search_product_self_vn_pay_image_size else R.dimen.main_search_product_self_image_size
    }

    override fun getCountColumnProduct(): Int {
        return if (checkDemoVNPay()) 2 else 3
    }

//    override fun getWidthItemProductCustom(): Int {
//        return when (preferencesHelper.getMenuItemSize()) {
//            Constants.SMALL_SIZE_ITEM_MENU -> {
//                R.dimen.main_search_product_image_size_small
//            }
//            Constants.DEFAULT_SIZE_ITEM_MENU -> {
//                R.dimen.main_search_product_image_size
//            }
//            Constants.LARGE_SIZE_ITEM_MENU -> {
//                R.dimen.main_search_product_image_size_large
//            }
//            else -> R.dimen.main_search_product_image_size
//        }
//    }

    private fun initPresent() {
        if (!preferencesHelper.getUseTwoScreen()) return

//        screenManager.init(this)
//        var displays = screenManager.displays
//        if (displays.size > 1) {
//            val isOnline = (application as CashierApplication).isOnline
//            val setting = preferencesHelper.getSessionSetting().settings
//
//            if (isOnline && setting.linkPresent.isNotEmpty()) {
//                setting.linkPresent.let {
//                    videoDisplay     = VideoDisplay(this, displays[1], it, setting.isPresentVideo)
//                    videoMenuDisplay = VideoMenuDisplay(this, displays[1], it, setting.isPresentVideo)
//                }
//            } else {
//                val path = preferencesHelper.getBannerItem()
//                videoDisplay     = VideoDisplay(this, displays[1], path, false)
//                videoMenuDisplay = VideoMenuDisplay(this, displays[1], path, false)
//            }
//        }
//
//        if((application as CashierApplication).keepScreenOn) window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//        aidlUltiT2mini = if (Build.MODEL == Constants.DEVICES_NAME_T2_MINI) {
//            AidlUtil.instance
//        } else null
//        if (aidlUltiT2mini != null) {
//            aidlUltiT2mini!!.sendLCDDoubleString(preferencesHelper.getCurrentBranchName(), getString(R.string.xin_chao))
//        } // init LCD
    }

//    override fun display2ScreenPresent(serverEvents: ServerEvents, typeHeader: Int) {
//        if (!preferencesHelper.getUseTwoScreen()) return
//
//        mDisposables.add(
//                Observable.just(serverEvents.jsonContent)
//                        .map {
//                            if (it.orderDetails.isNotEmpty()) {
//                                val typeHeader = resources.getString(typeHeader)
//                                getHtmlPrint(it, typeHeader)
//                                        .replace(Constants.DEFAULT_FOOTER_POS365, "")
//                            }
//                            else ""
//                        }
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .subscribeBy(
//                                onNext = {
//                                    if (it.isEmpty())
//                                        showVideoDisplay()
//                                    else
//                                        showVideoMenuDisplay(it)
//
//
//                                },
//                                onError = {},
//                                onComplete = {}
//                        )
//        )
////        serverEvents.jsonContent.let {
////            if (it.orderDetails.isNotEmpty()) {
////                val typeHeader = resources.getString(typeHeader)
////                val html = getHtmlPrint(it, typeHeader)
////                        .replace(Pos365Constants.DEFAULT_FOOTER_POS365, "")
////
////                showVideoMenuDisplay(html)
////            } else {
////                showVideoDisplay()
////            }
////        }
//    }

    override fun showVideoDisplay() {
//        if (!preferencesHelper.getUseTwoScreen()) return
//
//        if (videoDisplay != null && videoDisplay?.isShow == false) {
//            videoDisplay?.show()
//        }
    }

    override fun showVideoMenuDisplay(html: String) {
        if (!preferencesHelper.getUseTwoScreen()) return

//        if (videoMenuDisplay != null && videoMenuDisplay?.isShow == false) {
//            videoMenuDisplay?.show()
//            videoMenuDisplay?.update(html)
//        } else {
//            if (null != videoMenuDisplay) {
//                videoMenuDisplay?.update(html)
//            } else {
//
//            }
//        }
    }

    override fun displayLCDDoubleString(isShow: Boolean, title: String, total: Double) {
//        if (aidlUltiT2mini == null) return
//        val totalString = total.numberFormat().plus(preferencesHelper.getCurrencyUnit())
//        if (isShow) {
//            if (title.length > 16 || totalString.length > 16) {
//                val stringDisplay = title.plus("\n").plus(totalString)
//                val bitmap = stringDisplay.drawTextToBitmap()
//                if (bitmap != null) {
//                    val bitmapScale = Bitmap.createScaledBitmap(bitmap, 128, 40, false)
//                    aidlUltiT2mini!!.sendLCDBitmap(bitmapScale)
//                }
//            } else aidlUltiT2mini!!.sendLCDDoubleString(title, totalString)
//        } else {
//            aidlUltiT2mini!!.sendLCDDoubleString(preferencesHelper.getCurrentBranchName(), getString(R.string.xin_chao))
//        }
    }

    override fun showQRCodeSecondScreen(qrCode: Bitmap?, isShow: Boolean, accountId : Int?) {
//        if (!preferencesHelper.getUseTwoScreen()) return
//        if (videoMenuDisplay != null) {
//            videoMenuDisplay?.show()
//            if (isShow) {
//                videoMenuDisplay?.showQRCode(qrCode,accountId)
//            } else {
//                videoMenuDisplay?.hideQRCode()
//            }
//        }
//        if (isShow) {
//            if (aidlUltiT2mini != null && qrCode != null) {
//                val bitmapScale = Bitmap.createScaledBitmap(qrCode, 40, 40, false)
//                aidlUltiT2mini!!.sendLCDBitmap(bitmapScale)
//            }
//        } else {
//            if (aidlUltiT2mini != null) {
//                aidlUltiT2mini!!.sendLCDCommand(4)
//            }
//        }
    }

    @SuppressLint("InlinedApi")
    fun hideSystemUI() {
        val visibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
        if (window.decorView.systemUiVisibility != visibility) window.decorView.systemUiVisibility = visibility
    }

    fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun checkScreenOn() : Boolean {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            powerManager.isInteractive
        } else {
            powerManager.isScreenOn
        }
    }

    @SuppressLint("InlinedApi")
    fun checkDialogSystemUI() {
        if ((application as Application).showNavigationBar) return
        val visibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN

        if (materialDialog != null && materialDialog?.window?.decorView?.systemUiVisibility != visibility)
            materialDialog?.window?.decorView?.systemUiVisibility = visibility
    }

    // for Restaurant App

//    fun showDialogAccount(accounts: List<Account>, indexDefault: Int, actionChoose : (Account) -> Unit) {
//        val dialog = MaterialDialog(this)
//                .customView(R.layout.popup_account_change, noVerticalPadding = true)
//                .cornerRadius(16f)
//        dialog.btnCloseDialog.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.radioGroup.removeAllViews()
//        accounts.forEach {
//            val radioButton = getAccountRadioButton(it, indexDefault)
//            dialog.radioGroup.addView(radioButton)
//            val layoutParams = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//            layoutParams.bottomMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5F, resources.displayMetrics).toInt()
//            radioButton.layoutParams = layoutParams
//        }
//
//        dialog.radioGroup.check(indexDefault)
//
//        dialog.btnApply.setOnClickListener {
//            val idChoose = dialog.radioGroup.checkedRadioButtonId
//            actionChoose(accounts.firstOrNull { it.id == idChoose } ?: Account(name = getString(R.string.tien_mat)))
//            dialog.dismiss()
//        }
//        materialDialog = dialog
//        dialog.show()
//    }
//
//    private fun getAccountRadioButton(account: Account, indexDefault: Int): RadioButton {
//        val radioButton = LayoutInflater.from(this).inflate(R.layout.radio_button_channel, null) as RadioButton
//        radioButton.id = account.id ?: 0
//
//        if (account.id ?: 0 == indexDefault) radioButton.isChecked = true
//        when (account.id) {
//            0 -> {
//                val drawable = resources?.getDrawable(R.drawable.ic_money_dark_sea_blue_40dp)
//                radioButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
//                radioButton.text = account.name
//            }
//            Constants.ACCOUNT_ID_PAY_CARD -> {
//                val drawable = resources?.getDrawable(R.drawable.ic_vnpaypos_origin_40dp)
//                radioButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
//
//                val charSequenceName = "VNPAYpos"
//                val spannableName = SpannableString(charSequenceName)
//                spannableName.setSpan(ForegroundColorSpan(Color.parseColor("#004a9c")),2,5,0)
//                radioButton.text = spannableName
//                radioButton.setTextColor(Color.parseColor("#e50019"))
//            }
//
//            Constants.ACCOUNT_ID_BARCODE -> {
//                val drawable = resources?.getDrawable(R.drawable.ic_vnpayqr_origin_40dp)
//                radioButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
//
//                val charSequenceName = "VNPAYqr"
//                val spannableName = SpannableString(charSequenceName)
//                spannableName.setSpan(ForegroundColorSpan(Color.parseColor("#004a9c")),2,5,0)
//                radioButton.text = spannableName
//                radioButton.setTextColor(Color.parseColor("#e50019"))
//            }
//
//            else -> {
//                val drawable = resources?.getDrawable(R.drawable.ic_grid_dark_sea_blue_40dp)
//                radioButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
//                radioButton.text = account.name
//            }
//        }
//
//        return radioButton
//    }
//
//    fun showPopupPrintWaitingList() {
//        val dialog = MaterialDialog(this)
//                .customView(R.layout.popup_print_waiting,noVerticalPadding = true)
//                .cornerRadius(16f)
//        val title = dialog.view.tvTitle
//        val dismiss = dialog.view.btnClose
//        val recycler = dialog.view.recycleList
//        val btnPrintAll = dialog.view.btnPrintAll
//        val btnDeleteAll = dialog.view.btnDeleteAll
//        val adapter = printWaitingAdapter.setListener(
//                deleteItem = { index, printData ->
//                    dataManager.deleteWaitingPrintData(printData.id)
//                },
//                clickItem = { index, printData -> },
//                print = { index, printData ->
//                    if (dataManager.deleteWaitingPrintData(printData.id)) {
//                        printService.startActionEnqueuePrint(this, mutableListOf(printData))//in xong xoa
//                    }
//                }
//        )
//        title.text = getString(R.string.danh_sach_lenh_in_loi)
//        dismiss.setOnClickListener {
//            dialog.dismiss()
//        }
//        btnPrintAll.setOnClickListener {
//            val waitingPrints = dataManager.getWaitingPrintData().value
//            if (waitingPrints != null) {
//                dataManager.deleteAllWaitingPrintData()
//                printService.startActionEnqueuePrint(this, waitingPrints!!)
//            }
//        }
//        btnDeleteAll.setOnClickListener {
//            dataManager.deleteAllWaitingPrintData()
//        }
//        recycler.adapter = adapter
//        recycler.initLinearLayoutManager(0,false)
//
//        val disposable = dataManager.getWaitingPrintData()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribeBy(
//                        onNext = {
//                            adapter.list = it.toMutableList()
//                            title.text = "${getString(R.string.danh_sach_lenh_in_loi)} (${it.size})"
//                        },
//                        onComplete = {},
//                        onError = {}
//                )
//
//        dialog.setOnDismissListener {
//            disposable.dispose()
//        }
//
//        materialDialog = dialog
//        checkDialogSystemUI()
//        materialDialog?.show()
//    }

    override fun showRateApp() {
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener {
            if (it.isSuccessful) {
                //We got the ReviewInfo Object
                val reviewInfo = it.result
                val flow = reviewManager.launchReviewFlow(this@BaseActivity,reviewInfo)
                flow.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Timber.e("Review Successs")
                    }
                    if (it.isComplete) {
                        Timber.e("Review Complete")
                    }
                }
            }
            else {
                Timber.e("ReviewTaskException ${it.exception?.message}")
            }
        }
    }

    override fun checkAndGoAppStore() {
        if ((application as Application).isNewestVersion) return
        (application as Application).isNewestVersion = true //just call once
        showDialogBasic(
                "phien_ban_moi_kha_dung",
                "thong_bao_cap_nhat_phien_ban_moi",
                true,
                "dong_y",
                positiveCallback = {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                    } catch (e: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                    }
                },
                negative = "huy_bo")
    }

    override fun checkPermission(permission: String) {
//        if (preferencesHelper.getCurrentUser().isAdmin) return

        createPermission = false
        updatePermission = false
        deletePermission = false
        importPermission = false
        exportPermission = false
        viewCostPermission = false
        updateCostPermission = false

//        mDisposables.add(
//                Observable.fromIterable(dataManager.getPermissionFollowGroup(permission))
//                        .map {
//                            when(it){
//                                PermissionMap.CAN_CREATE -> createPermission = true
//                                PermissionMap.CAN_UPDATE -> updatePermission = true
//                                PermissionMap.CAN_DELETE -> deletePermission = true
//                                PermissionMap.CAN_IMPORT -> importPermission = true
//                                PermissionMap.CAN_EXPORT -> exportPermission = true
//                                PermissionMap.CAN_VIEW_COST -> viewCostPermission = true
//                                PermissionMap.CAN_UPDATE_COST -> updateCostPermission = true
//                            }
//                            it
//                        }
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .subscribeBy(
//                                onNext = {
//                                },
//                                onComplete = {
//                                },
//                                onError = {
//                                }
//                        )
//        )
    }

    override fun checkReadPermissionFlowGroup(permission: String) {
//        if (preferencesHelper.getCurrentUser().isAdmin) return
//        else {
//            readPermission = false
//            if (dataManager.isReadPermissionFollowGroup(permission)) readPermission = true
//        }
    }

    override fun showNetworkStatus(status: Int) {
    }

    override fun setPrintServ() {
//        try {
//            val webView = findViewById<WebView>(R.id.webPrint)
//            if (webView == null || preferencesHelper.getPrintHiddenMode())
//                printService.setWebview(webPrint, null)
//            else
//                printService.setWebview(webView, layout_webprint)
//        } catch (e: Exception) { }
    }

//    override fun getPrintServ() = printService
}