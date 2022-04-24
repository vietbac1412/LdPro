package tamhoang.ldpro4.ui.custom.viewpager

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 21/03/2018.
 */
// Vo hieu hoa swipe theo huong bat ky
// https://stackoverflow.com/questions/19602369/how-to-disable-viewpager-from-swiping-in-one-direction/34076649#34076649
class DirectionViewPager : ViewPager {

    private var initialXValue: Float = 0F
    private var direction: SwipeDirection = SwipeDirection.all

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.isSwipeAllowed(event)) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.isSwipeAllowed(event)) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    private fun isSwipeAllowed(event: MotionEvent): Boolean {
        if (this.direction === SwipeDirection.all) return true

        if (direction == SwipeDirection.none) // disable any swipe
            return false

        if (event.action == MotionEvent.ACTION_DOWN) {
            initialXValue = event.x
            return true
        }

        if (event.action == MotionEvent.ACTION_MOVE) {
            try {
                val diffX = event.x - initialXValue
                if (diffX > 0 && direction == SwipeDirection.right) {
                    // swipe from left to right detected
                    return false
                } else if (diffX < 0 && direction == SwipeDirection.left) {
                    // swipe from right to left detected
                    return false
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
        return true
    }

    fun setAllowedSwipeDirection(direction: SwipeDirection) {
        this.direction = direction
    }

}

enum class SwipeDirection {
    all, left, right, none
}

