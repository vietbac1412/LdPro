package tamhoang.ldpro4.ui.custom.text

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.util.AttributeSet
import android.widget.EditText
import java.util.*

/**
 * @author : Pos365
 * @Skype : chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 1/19/19.
 */
@SuppressLint("AppCompatCustomView")
class EmojiExcludeEditText : EditText {

    private var emojiExcludeFilter: EmojiExcludeFilter? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        if (emojiExcludeFilter == null) {
            emojiExcludeFilter = EmojiExcludeFilter()
        }
        setFilters(arrayOf<InputFilter>(emojiExcludeFilter!!))
    }

    override fun setFilters(filters: Array<InputFilter>) {
        var filters = filters
        if (filters.isNotEmpty()) { //if length == 0 it will here return when init() is called
            var add = true
            for (inputFilter in filters) {
                if (inputFilter === emojiExcludeFilter) {
                    add = false
                    break
                }
            }
            if (add) {
                filters = Arrays.copyOf(filters, filters.size + 1)
                filters[filters.size - 1] = this.emojiExcludeFilter!!
            }
        }
        super.setFilters(filters)
    }

    private inner class EmojiExcludeFilter : InputFilter {

        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            for (i in start until end) {
                val type = Character.getType(source[i])
                if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                    return ""
                }
            }
            return null
        }
    }
}
