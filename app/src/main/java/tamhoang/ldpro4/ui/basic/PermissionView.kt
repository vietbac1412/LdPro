package tamhoang.ldpro4.ui.basic

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 25/02/2018.
 */
interface PermissionView {
    fun showDangerousPermissions()

    fun showRationalForDangerousPermission()

    fun onDangerousPermissionDenied()

    fun onDangerousPermissionNeverAskAgain()
}