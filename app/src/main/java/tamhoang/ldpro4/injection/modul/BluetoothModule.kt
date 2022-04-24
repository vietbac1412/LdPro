package tamhoang.ldpro4.injection.modul

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 24/10/2018.
 */
@Module
class BluetoothModule {
    @Provides
    @Singleton
    fun provideBluetoothAdapter(application: Application) : BluetoothAdapter? {
        val packageManager = application.packageManager
        val hasBluetooth = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
        return if (hasBluetooth) {
            BluetoothAdapter.getDefaultAdapter()
        } else {
            null
        }
    }

    @Provides
    @Singleton
    fun provideUUID(): UUID {
        return UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}