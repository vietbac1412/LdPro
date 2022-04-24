package tamhoang.ldpro4.ui.basic.fragment

import android.view.KeyEvent

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 12/04/2018.
 */
interface FragmentView {
    fun backToParentFragment()

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean

    fun getPrintServ() : PrintService
}