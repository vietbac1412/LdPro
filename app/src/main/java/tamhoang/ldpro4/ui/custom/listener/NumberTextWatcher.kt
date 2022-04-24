package tamhoang.ldpro4.ui.custom.listener

import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.widget.EditText
import timber.log.Timber
import tamhoang.ldpro4.util.extension.checkValueMoney
import tamhoang.ldpro4.util.extension.formatPriceDisplay
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 08/04/2018.
 */
class NumberTextWatcher(private val editText: EditText,
                        private val action: (String) -> Unit) : TextWatcher {

    private var mIsFractionalPart = false

    private val nf = NumberFormat.getNumberInstance(Locale.US)
    private val df = nf as DecimalFormat

    private val nfnd = NumberFormat.getNumberInstance(Locale.US)
    private val dfnd = nfnd as DecimalFormat
    init {

        with(df) {
            applyPattern("###,###,###.###")
            isDecimalSeparatorAlwaysShown = true
        }

        with(dfnd) {
            applyPattern("###,###,###")
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        mIsFractionalPart = s.toString().contains(df.decimalFormatSymbols.decimalSeparator.toString())
    }

    override fun afterTextChanged(s: Editable) {
        editText.removeTextChangedListener(this)

        try {
            val startLength = editText.text.length

            val v = s.toString().replace(df.decimalFormatSymbols.groupingSeparator.toString(), "")
            val n = df.parse(v)
            val cp = editText.selectionStart
            if (mIsFractionalPart) {
                editText.setText(df.format(n))
            } else {
                editText.setText(dfnd.format(n))
            }

            val endLength = editText.text.length

            val sel = (cp + (endLength - startLength))
            if (sel > 0 && sel <= editText.text.length) {
                editText.setSelection(sel)
            } else {
                editText.setSelection(editText.text.length)
            }
        } catch (nfe: NumberFormatException) {
            Timber.e(nfe.message)
        } catch (e: ParseException) {
            Timber.e(e.message)
        }

        editText.addTextChangedListener(this)
        action(editText.text.toString())
    }
}


class TextWatcherDecimal( private val editText: EditText,private val length: Int, private val action: (Double) -> Unit):TextWatcher{

    private var mIsFractionalPart = false

    private val nf = NumberFormat.getNumberInstance(Locale.US)
    private val df = nf as DecimalFormat

    private val nfnd = NumberFormat.getNumberInstance(Locale.US)
    private val dfnd = nfnd as DecimalFormat
    init {

        with(df) {
            applyPattern("###,###,###.##")
            isDecimalSeparatorAlwaysShown = true
        }

        with(dfnd) {
            applyPattern("###,###,###")
        }

        editText.filters = getMaxLength(length)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        mIsFractionalPart = s.toString().contains(df.decimalFormatSymbols.decimalSeparator.toString())
    }

    override fun afterTextChanged(s: Editable) {
        editText.removeTextChangedListener(this)

        try {
            val startLength = editText.text.length

            val v = s.toString().replace(df.decimalFormatSymbols.groupingSeparator.toString(), "")
            val n = df.parse(v)
            val cp = editText.selectionStart
            if (mIsFractionalPart) {
                editText.setText(df.format(n))
            } else {
                editText.setText(dfnd.format(n))
            }

            val endLength = editText.text.length

            val sel = (cp + (endLength - startLength))
            if (sel > 0 && sel <= editText.text.length) {
                editText.setSelection(sel)
            } else {
                editText.setSelection(editText.text.length)
            }
        } catch (nfe: NumberFormatException) {
            Timber.e(nfe.message)
        } catch (e: ParseException) {
            Timber.e(e.message)
        }

        editText.addTextChangedListener(this)
        val value = editText.text.toString().checkValueMoney<String>()
        action(value)
    }

    private fun getMaxLength(length: Int): Array<InputFilter?>{
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = InputFilter.LengthFilter(length)
        return filterArray
    }

}

class TextWatcherQuantity( private val editText: EditText,private val length: Int, private val action: (Double) -> Unit): TextWatcher{

    init {
        editText.filters = getMaxLength(length)
    }

    override fun afterTextChanged(p0: Editable?) {
        val value1 = p0.toString().checkValueMoney<String>()
        action(value1)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    private fun getMaxLength(length: Int): Array<InputFilter?>{
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = InputFilter.LengthFilter(length)
        return filterArray
    }

}

///////////////// Private

class TextWatcherTempSize( private val editText: EditText, private val action: (Double) -> Unit):TextWatcher{

    private var mIsFractionalPart = false

    private val nf = NumberFormat.getNumberInstance(Locale.US)
    private val df = nf as DecimalFormat

    init {
        editText.filters = getMaxLength(5)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        mIsFractionalPart = s.toString().contains(df.decimalFormatSymbols.decimalSeparator.toString())
    }

    override fun afterTextChanged(s: Editable) {
        editText.removeTextChangedListener(this)

        try {
            val startLength = editText.text.length
            var string = s.toString().replace("x","")

            val stringAfter = if (string.length >= 2 && !s.toString().endsWith("x")) string.substring(0, 2) + "x" + string.substring(2, string.length)
            else string
            val cp = editText.selectionStart
            var indexX = -1
            stringAfter.forEachIndexed { index, c -> if ( c.equals('x')) indexX = index }
            if (indexX == -1) editText.setText(stringAfter)
            else {
                val spannableString = SpannableString(stringAfter)
                spannableString.setSpan(ForegroundColorSpan(Color.GRAY), indexX, indexX + 1, 0)
                editText.setText(spannableString)
            }

            val endLength = editText.text.length

            val sel = (cp + (endLength - startLength))
            if (sel > 0 && sel <= editText.text.length) {
                editText.setSelection(sel)
            } else {
                editText.setSelection(editText.text.length)
            }
        } catch (nfe: NumberFormatException) {
            Timber.e(nfe.message)
        } catch (e: ParseException) {
            Timber.e(e.message)
        }

        editText.addTextChangedListener(this)
        val value = editText.text.toString().checkValueMoney<String>()
        action(value)
    }

    private fun getMaxLength(length: Int): Array<InputFilter?>{
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = InputFilter.LengthFilter(length)
        return filterArray
    }

}