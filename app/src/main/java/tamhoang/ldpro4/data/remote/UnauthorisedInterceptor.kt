package tamhoang.ldpro4.data.remote

import android.content.Context
import android.os.Handler
import android.os.Looper
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import org.greenrobot.eventbus.EventBus
import tamhoang.ldpro4.Application
import timber.log.Timber
import tamhoang.ldpro4.BuildConfig
import tamhoang.ldpro4.data.BusEvent
import tamhoang.ldpro4.data.local.PreferencesHelper
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 30/03/2018.
 */
class UnauthorisedInterceptor(context: Context,
                              private val preferencesHelper: PreferencesHelper) : Interceptor {

    @Inject lateinit var eventBus: EventBus

    init {
//        Application.get(context).applicationComponent.inject(this)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request()
                .url()
                .newBuilder()
                .host(preferencesHelper.getHostName())
                .scheme(if (preferencesHelper.getLastHostName() == PreferencesHelper.VALUE_LAST_HOST_POS365) "https" else "http")
                .build()
        Timber.e("Url: $newUrl")
        val request =
                if (newUrl.toString().contains(Pos365Service.CREDENTIAL) ||
                        newUrl.toString().contains(Pos365Service.LOGOUT)) {
                    val rb = chain.request().newBuilder()
                            .url(newUrl) // Update host name
                            .header("Cache-Control", "max-age=60") //cho phép truy cập bộ nhớ cache khi offline.
                            .addHeader("User-Agent", BuildConfig.APPLICATION_ID)
                    if (newUrl.toString().contains(Pos365Service.CREDENTIAL)) {
                        preferencesHelper.getCurrentBranchOffId().forEach {
                            if (it.first != PreferencesHelper.PREF_BRANCH_OFF_KEY_DEFAULT) {
                                rb.addHeader("Cookie", "${it.first}=${it.second}")
                            }
                        }
                    }
                    rb.build()
                } else if (newUrl.toString().contains(Pos365Service.ORDERS)) {
                    chain.withReadTimeout(60, TimeUnit.SECONDS)
                            .withConnectTimeout(60, TimeUnit.SECONDS)
                            .withWriteTimeout(60, TimeUnit.SECONDS)
                            .request().newBuilder()
                            .url(newUrl) // Update host name
                            .header("Cache-Control", "max-age=60") //cho phép truy cập bộ nhớ cache khi offline.
                            .addHeader("User-Agent", BuildConfig.APPLICATION_ID)
                            .addHeader("Cookie", "ss-id=${preferencesHelper.getSessionId()}")
                            .build()
                } else {
                    chain.request().newBuilder()
                            .url(newUrl) // Update host name
                            .header("Cache-Control", "max-age=60") //cho phép truy cập bộ nhớ cache khi offline.
                            .addHeader("User-Agent", BuildConfig.APPLICATION_ID)
                            .addHeader("Cookie", "ss-id=${preferencesHelper.getSessionId()}")
                            .build()
                }

        //Use when need show body of post
//        if (request.method() == "POST") {
//            val bodyString = bodyToString(request)
//            Timber.e("Url: Body POST $bodyString")
//        }

        val response = chain.proceed(request)
        if (BuildConfig.DEBUG) showInformationService(chain, request, response)

        if (response.code() == 401) {
            if (!request.url().toString().contains(Pos365Service.CREDENTIAL)) {
                Handler(Looper.getMainLooper()).post { eventBus.post(BusEvent.AuthenticationError()) }
            }
        }
        return response
    }

    private fun bodyToString(request: Request): String? {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "Exception"
        }
    }

    private fun showInformationService(chain: Interceptor.Chain,
                                       request: Request,
                                       response: Response) {
        val timeStart = System.nanoTime()
//        Timber.i(String.format("Sending request %s on %s%n%s",
//                request.url(), chain.connection(), request.headers()))

        val timeEnd = System.nanoTime()
//        Timber.i(String.format("Received response for %s in %.1fms%n%s",
//                response.request().url(), (timeEnd - timeStart) / 1e6, response.headers()))
    }
}