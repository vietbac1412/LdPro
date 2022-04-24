package tamhoang.ldpro4pos365.injection.modul

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import tamhoang.ldpro4.BuildConfig
import tamhoang.ldpro4.data.local.PreferencesHelper
import tamhoang.ldpro4.data.remote.GoogleDriverService
import tamhoang.ldpro4.data.remote.Pos365Service
import tamhoang.ldpro4.data.remote.UnauthorisedInterceptor
import tamhoang.ldpro4pos365.injection.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author  : Hanet Electronics
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : muoick@hanet.com
 * @Website : http://hanet.com/
 * @Project : Music
 * Created by chukimmuoi on 03/02/2018.
 */
@Module
class ApiModule {

    companion object {
        private const val VALUES_CONNECT_TIMEOUT = 30L
        private const val VALUES_READ_TIMEOUT    = 30L
        private const val VALUES_WRITE_TIMEOUT   = 30L

        private const val VALUES_ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    }

    /**
     * Setup gson object.
     * */
    @Provides @Singleton fun provideGson() : Gson {
        return GsonBuilder()
                .setDateFormat(VALUES_ISO_FORMAT) // millis seconds.
                //.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setLenient()
                .create()
    }

    /**
     * Retrofit and Authentication.
     * */
    @Provides @Singleton fun provideInterceptor(@ApplicationContext context: Context,
                                                preferencesHelper: PreferencesHelper): Interceptor {
        return UnauthorisedInterceptor(context, preferencesHelper)
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        val cacheSize: Long = 10 * 1024 * 1024 // 10 MB.
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor, cache: Cache) : OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor) // or addNetworkInterceptor(interceptor)
                .connectTimeout(VALUES_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(VALUES_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(VALUES_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                .build()
    }

    @Provides
    @Singleton
    fun provideRibotsService(okHttpClient: OkHttpClient,
                             gson: Gson) : Pos365Service {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://${PreferencesHelper.VALUE_HOST_NAME_DEFAULT}/")
                .addConverterFactory(GsonConverterFactory.create(gson))    // Gson.
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // RxJava.
                .build()
                .create(Pos365Service::class.java)
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

    @Provides
    @Singleton
    fun provideDriverService(cache: Cache, gson: Gson) : GoogleDriverService {
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                    .header("Cache-Control", "max-age=60") //cho phép truy cập bộ nhớ cache khi offline.
                    .addHeader("User-Agent", BuildConfig.APPLICATION_ID)
                    .build()

            val response = chain.proceed(request)
            if (BuildConfig.DEBUG) showInformationService(chain, request, response)

            response
        }
        val okHttp = OkHttpClient.Builder()
                .addInterceptor(interceptor) // or addNetworkInterceptor(interceptor)
                .connectTimeout(VALUES_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(VALUES_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(VALUES_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                .build()

        return Retrofit.Builder()
                .client(okHttp)
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))    // Gson.
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // RxJava.
                .build()
                .create(GoogleDriverService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(cache: Cache, gson: Gson) : Retrofit.Builder {
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                    .header("Cache-Control", "max-age=60") //cho phép truy cập bộ nhớ cache khi offline.
                    .addHeader("User-Agent", BuildConfig.APPLICATION_ID)
                    .build()

            val response = chain.proceed(request)
            if (BuildConfig.DEBUG) showInformationService(chain, request, response)

            response
        }
        val okHttp = OkHttpClient.Builder()
                .addInterceptor(interceptor) // or addNetworkInterceptor(interceptor)
                .connectTimeout(VALUES_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(VALUES_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(VALUES_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                .build()

        return Retrofit.Builder()
                .client(okHttp)
                .addConverterFactory(GsonConverterFactory.create(gson))    // Gson.
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // RxJava.
    }
}