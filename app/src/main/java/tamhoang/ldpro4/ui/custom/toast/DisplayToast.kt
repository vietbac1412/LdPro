package tamhoang.ldpro4.ui.custom.toast

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import es.dmoral.toasty.Toasty
import me.drakeet.support.toast.ToastCompat
import tamhoang.ldpro4pos365.ui.basic.BaseActivity

/**
 * @author : Pos365
 * @Skype : chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 2/20/19.
 */
class DisplayToast(private val mContext: Context, private val isLongTime: Boolean, private val type: Int, private var mText: String) : Runnable {

    private var mToast: Toast? = null

    override fun run() {
        val time = if (isLongTime) Toast.LENGTH_LONG else Toast.LENGTH_SHORT

        mToast = when(type){
            BaseActivity.TOAST_WARNING -> Toasty.warning(mContext, mText, time)
            else -> Toasty.normal(mContext, mText, time)
        }
        mToast?.setGravity(Gravity.CENTER, 0, 0)

        mToast?.show()
    }
}
