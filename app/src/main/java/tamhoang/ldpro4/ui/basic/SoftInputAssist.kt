package tamhoang.ldpro4.ui.basic

import android.app.Activity
import android.graphics.Rect
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 09/08/2018.
 */

/**
 * Man hinh full screen khong cho phep scroll, thay doi kich thuoc (adjustResize khong hoat dong)
 * Su dung:
 * https://stackoverflow.com/questions/7417123/android-how-to-adjust-layout-in-full-screen-mode-when-softkeyboard-is-visible/19494006#19494006
 * https://gist.github.com/grennis/2e3cd5f7a9238c59861015ce0a7c5584
 * de cho phep co dan thay doi kich thuoc khi full man hinh
 * */
class SoftInputAssist constructor(activity: Activity) {
    private val contentContainer = activity.findViewById(android.R.id.content) as ViewGroup
    private val rootView = contentContainer.getChildAt(0)
    private val rootViewLayout = rootView.layoutParams as FrameLayout.LayoutParams
//    private val viewTreeObserver: ViewTreeObserver by lazy { rootView.viewTreeObserver }
    private val listener = { possiblyResizeChildOfContent() }

    private val contentAreaOfWindowBounds = Rect()
    private var usableHeightPrevious = 0

    // I call this in "onResume()" of my fragment
    fun addListener() {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }


    // I call this in "onPause()" of my fragment
    fun removeListener() {
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    private fun possiblyResizeChildOfContent() {
        contentContainer.getWindowVisibleDisplayFrame(contentAreaOfWindowBounds)
        val usableHeightNow = contentAreaOfWindowBounds.height()
        if (usableHeightNow != usableHeightPrevious) {
            rootViewLayout.height = usableHeightNow
            // Change the bounds of the root view to prevent gap between keyboard and content, and top of content positioned above top screen edge.
            rootView.layout(contentAreaOfWindowBounds.left, contentAreaOfWindowBounds.top, contentAreaOfWindowBounds.right, contentAreaOfWindowBounds.bottom)
            rootView.requestLayout()

            usableHeightPrevious = usableHeightNow
        }
    }
}