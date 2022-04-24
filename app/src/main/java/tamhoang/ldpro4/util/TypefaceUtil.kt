package tamhoang.ldpro4.util

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.DisplayMetrics

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 23/02/2018.
 */
class TypefaceUtil {
    companion object {
        /**
         * see https://gist.github.com/artem-zinnatullin/7749076
         * <p>
         * Using reflection to override default typeface
         * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
         *
         * @param context                    to work with assets
         * @param defaultFontNameToOverride  for example "monospace", "serif"
         * @param customFontFileNameInAssets file name of the font from assets
         */
        fun overrideFont(context: Context,
                         defaultFontNameToOverride: String,
                         customFontFileNameInAssets: String) {
            try {
                val customFontTypeface = Typeface.createFromAsset(context.assets, customFontFileNameInAssets)

                val defaultFontTypefaceField = Typeface::class.java.getDeclaredField(defaultFontNameToOverride)
                defaultFontTypefaceField.isAccessible = true
                defaultFontTypefaceField.set(null, customFontTypeface)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * see http://stackoverflow.com/questions/6786439/how-to-set-font-scale-in-my-own-android-application
         * <p>
         * Using reflection to override default text size
         *
         * @param context  MainActivity, place it before calling super.setContentView(R.layout.yourLayout)
         * @param textSize for example 0.85f small, 1.0f normal, 1.15f large
         */
        fun overrideSize(context: Activity, textSize: Float) {
            try {
                val configuration = context.resources.configuration
                configuration.fontScale = textSize

                val metrics = DisplayMetrics()
                context.windowManager.defaultDisplay.getMetrics(metrics)
                metrics.scaledDensity = configuration.fontScale * metrics.density
                context.baseContext.resources.updateConfiguration(configuration, metrics)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}