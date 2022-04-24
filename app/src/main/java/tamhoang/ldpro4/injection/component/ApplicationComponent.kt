package tamhoang.ldpro4pos365.injection.component

import android.app.Application
import android.content.Context
import android.webkit.WebView
import dagger.Component
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import tamhoang.ldpro4.data.CashiersService
import tamhoang.ldpro4.data.DataManager
import tamhoang.ldpro4.data.local.BluetoothHelper
import tamhoang.ldpro4.data.local.DatabaseHelper
import tamhoang.ldpro4.data.local.PreferencesHelper
import tamhoang.ldpro4.data.remote.GoogleDriverService
import tamhoang.ldpro4.data.remote.Pos365Service
import tamhoang.ldpro4.data.remote.UnauthorisedInterceptor
import tamhoang.ldpro4pos365.injection.ApplicationContext
import tamhoang.ldpro4pos365.injection.modul.ApplicationModule
import tamhoang.ldpro4pos365.injection.modul.DataModule
import javax.inject.Singleton

/**
 * @author : Hanet Electronics
 * @Skype  : chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email  : muoick@hanet.com
 * @Website: http://hanet.com/
 * @Project: Music
 * Created by CHUKIMMUOI on 1/31/2018.
 */
@Singleton
@Component(modules = [(ApplicationModule::class), (DataModule::class)])
interface ApplicationComponent {

    fun inject(cashierApplication: Application)
    fun inject(unauthorisedInterceptor: UnauthorisedInterceptor)
    fun inject(cashiersService: CashiersService)

    @ApplicationContext fun context(): Context
    fun application(): Application
    fun ribotsService(): Pos365Service
    fun retrofitBuilder(): Retrofit.Builder
    fun googleDriverService(): GoogleDriverService
    fun databaseHelper(): DatabaseHelper
    fun dataManager(): DataManager
    fun preferencesHelper(): PreferencesHelper
    fun eventBus(): EventBus
    fun bluetoothHelper(): BluetoothHelper

    fun webPrint(): WebView
}