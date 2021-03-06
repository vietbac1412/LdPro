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
// https://developer.android.com/training/animation/screen-slide.html#zoom-out
class ZoomOutPageTransformer : ViewPager.PageTransformer {

    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height

        when {
            position < -1 -> // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.alpha = 0F
            position <= 1 -> { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                val vertMargin = pageHeight * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2
                if (position < 0) {
                    view.translationX = horzMargin - vertMargin / 2
                } else {
                    view.translationX = -horzMargin + vertMargin / 2
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor

                // Fade the page relative to its size.
                view.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)

            }
            else -> // (1,+Infinity]
                // This page is way off-screen to the right.
                view.alpha = 0F
        }
    }
}
