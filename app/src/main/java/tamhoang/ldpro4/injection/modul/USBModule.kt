package tamhoang.ldpro4.injection.modul

import android.app.Application
import android.content.Context
import android.hardware.usb.UsbManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 5/7/19.
 */
@Module
class USBModule {
    @Provides
    @Singleton
    fun provideUSBManager(application: Application) : UsbManager {
        return application.getSystemService(Context.USB_SERVICE) as UsbManager
    }
}