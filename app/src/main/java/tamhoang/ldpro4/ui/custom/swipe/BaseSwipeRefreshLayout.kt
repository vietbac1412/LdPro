package tamhoang.ldpro4.ui.custom.swipe

import android.content.Context
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.util.AttributeSet
import tamhoang.ldpro4.R

/**
 * @author  : Hanet Electronics
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : muoick@hanet.com
 * @Website : http://hanet.com/
 * @Project : Music
 * Created by chukimmuoi on 10/02/2018.
 */
class BaseSwipeRefreshLayout : SwipeRefreshLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
//        setColorSchemeResources(
//                R.color.colorSwipeBlue,
//                R.color.colorSwipeGreen,
//                R.color.colorSwipeOrange,
//                R.color.colorSwipeRed)
    }
}