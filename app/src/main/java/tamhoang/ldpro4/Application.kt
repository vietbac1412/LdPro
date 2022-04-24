package tamhoang.ldpro4

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import tamhoang.ldpro4.data.BusEvent
import tamhoang.ldpro4.data.DataManager
import tamhoang.ldpro4.data.local.PreferencesHelper
import tamhoang.ldpro4.data.model.*
import tamhoang.ldpro4pos365.injection.component.ApplicationComponent
import tamhoang.ldpro4pos365.injection.component.DaggerApplicationComponent
import tamhoang.ldpro4pos365.injection.modul.ApiModule
import tamhoang.ldpro4pos365.injection.modul.ApplicationModule
import tamhoang.ldpro4pos365.injection.modul.DataModule
import tamhoang.ldpro4pos365.injection.modul.DbModule
import timber.log.BuildConfig
import javax.inject.Inject

@Keep
class Application : Application() {

    @Inject lateinit var eventBus: EventBus
    @Inject lateinit var dataManager: DataManager
    @Inject lateinit var preferencesHelper: PreferencesHelper

    var isOnline = true

    var isPrinting = false
    var showNavigationBar = true
    var keepScreenOn = true
    var isLogin = false
    var isNewestVersion = true
    var receiveSignalR = true

    var applicationComponent: ApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .dataModule(DataModule())
            .apiModule(ApiModule())
            .dbModule(DbModule())
            .build()

    private val soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)

    companion object {
        fun get(context: Context) : Application = context.applicationContext as Application
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        // Setup log.
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree(){
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return "${super.createStackElementTag(element)}.${element.methodName}(${element.lineNumber}) "
                }
            })
        }

        applicationComponent.inject(this)
        eventBus.register(this)

    }

    @Subscribe
    fun onAuthenticationError(event: BusEvent.AuthenticationError) {} //eventBus.register cần anation @Subscribe


    fun playSoundNotification() {
        if (!preferencesHelper.getNotificationsPay()) return
        soundPool.play(2, 1F, 1F, 10, 0, 1F)
    }

}