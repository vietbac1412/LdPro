package tamhoang.ldpro4.ui.custom.text

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.MultiAutoCompleteTextView

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 11/22/18.
 */
class QuickAutoComplete : androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView {

    constructor(context: Context) : super(context)

    constructor(arg0: Context, arg1: AttributeSet) : super(arg0, arg1)

    constructor(arg0: Context, arg1: AttributeSet, arg2: Int) : super(arg0, arg1, arg2)

    override fun enoughToFilter(): Boolean {
        return true
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && filter != null) {
            performFiltering(text, 0)
        }
    }
}