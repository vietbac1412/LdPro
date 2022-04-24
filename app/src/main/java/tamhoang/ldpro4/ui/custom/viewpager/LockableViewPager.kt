package tamhoang.ldpro4.ui.custom.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 21/03/2018.
 */
// Class cho phep vo hieu hoa swipe tren view pager
// https://gist.github.com/nesquena/898db22a38747bd9bc19
class LockableViewPager : ViewPager {

    private var mSwipeAble: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.mSwipeAble = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return try {
            if (this.mSwipeAble) super.onTouchEvent(event) else false
        } catch (e: Exception) {
            false
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return try {
            if (this.mSwipeAble) super.onInterceptTouchEvent(event) else false
        } catch (e: Exception) {
            super.onInterceptTouchEvent(event)
        }
    }

    fun setSwipeAble(swipeAble: Boolean) {
        this.mSwipeAble = swipeAble
    }
}