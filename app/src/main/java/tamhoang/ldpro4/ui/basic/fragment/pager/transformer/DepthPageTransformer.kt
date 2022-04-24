package tamhoang.ldpro4.ui.basic.fragment.pager.transformer

import androidx.viewpager.widget.ViewPager
import android.view.View


/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 11/03/2018.
 */
// https://developer.android.com/training/animation/screen-slide.html#depth-page
class DepthPageTransformer : ViewPager.PageTransformer {

    companion object {
        private const val MIN_SCALE = 0.75f
    }

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.getWidth()

        when {
            position < -1 -> // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.alpha = 0F
            position <= 0 -> { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.alpha = 1F
                view.translationX = 0F
                view.scaleX = 1F
                view.scaleY = 1F

            }
            position <= 1 -> { // (0,1]
                // Fade the page out.
                view.alpha = 1 - position

                // Counteract the default slide transition
                view.translationX = pageWidth * -position

                // Scale the page down (between MIN_SCALE and 1)
                val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position))
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
            }
            else -> // (1,+Infinity]
                // This page is way off-screen to the right.
                view.alpha = 0F
        }
    }
}