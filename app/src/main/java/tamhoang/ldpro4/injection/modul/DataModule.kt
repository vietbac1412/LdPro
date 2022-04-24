package tamhoang.ldpro4pos365.injection.modul

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import tamhoang.ldpro4.data.local.PreferencesHelper
import tamhoang.ldpro4.injection.modul.BluetoothModule
import tamhoang.ldpro4.injection.modul.USBModule
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
@Module(includes = [(ApiModule::class), (DbModule::class), (BluetoothModule::class), (USBModule::class)])
class DataModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(PreferencesHelper.PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferencesHelper(sharedPreferences: SharedPreferences): PreferencesHelper {
        return PreferencesHelper(sharedPreferences)
    }
}