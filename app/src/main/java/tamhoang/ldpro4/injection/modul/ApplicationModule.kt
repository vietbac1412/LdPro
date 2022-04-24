package tamhoang.ldpro4pos365.injection.modul

import android.app.Application
import android.content.Context
import android.webkit.WebView
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import tamhoang.ldpro4pos365.injection.ApplicationContext
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
/**
 * Cung cấp mức phụ thuộc cấp ứng dụng.
 * */
@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    internal fun provideApplication() : Application = application

    @Provides
    @Singleton
    @ApplicationContext
    internal fun providesContext() : Context = application

    @Provides
    @Singleton
    internal fun providesEventBus() : EventBus = EventBus.getDefault()

    @Provides
    @Singleton
    internal fun providesWebPrint() : WebView {
        val scale = (application.resources.displayMetrics.widthPixels / application.resources.displayMetrics.density).toInt()
        val webPrint = WebView(application)
        webPrint.layout(0, 0, 100, 100  )
        webPrint.setInitialScale(scale)

        return webPrint
    }
}