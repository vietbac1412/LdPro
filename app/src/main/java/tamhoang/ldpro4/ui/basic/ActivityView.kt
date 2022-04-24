package tamhoang.ldpro4.ui.basic

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import tamhoang.ldpro4pos365.ui.basic.fragment.BaseFragment
import java.util.*

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 27/02/2018.
 */
interface ActivityView : PermissionView {

    companion object {
        const val TYPE_ANIMATION_SLIDE_RIGHT_TO_LEFT = 0
        const val TYPE_ANIMATION_SLIDE_BOTTOM_TO_TOP = 1
    }

    fun createTypeface()

    fun createVariableReload(savedInstanceState: Bundle)

    /**
     * Hien thi dialog len man hinh thiet bi. Voi cac tham so truyen vao la string.
     *
     * @param title thong tin ngan gon (loi, canh bao, ...)
     * @param content mo ta chi tiet
     * @param positive dong y (ok)
     * @param positiveCallback action khi dong y
     * @param negative huy bo (cancel)
     * @param negativeCallback action khi huy bo
     * @param neutral trung lap (bo qua)
     * @param neutralCallback action khi bo qua
     * */
    fun showDialogBasic(title: String, content: String, isCancel: Boolean = false,
                        positive: String = "", positiveCallback: ((Any) -> Unit)? = null,
                        negative: String = "", negativeCallback: ((Any) -> Unit)? = null,
                        neutral: String = "", neutralCallback: ((Any) -> Unit)? = null
    )

    fun showDialogInput(inputType: Int, title: String, content: String,
                        hint: String, prefill: String = "",
                        positive: String, positiveCallback: ((Any) -> Unit)? = null
    )

    fun showDialogInput(inputType: Int, title: Int, content: Int,
                        hint: Int, prefill: String = "",
                        positive: Int, positiveCallback: ((Any) -> Unit)? = null
    )

    fun showDialogSingleChoice(title: String, array: Int, defaultIndex: Int = 0, isCancel: Boolean = false,
                               positive: String, positiveCallback: ((Any, String) -> Unit)? = null,
                               negative: String = "", negativeCallback: ((Any) -> Unit)? = null,
                               neutral: String = "", neutralCallback: ((Any) -> Unit)? = null,
                               cancelCallback: (() -> Unit)? = null
    )

    fun showDialogSingleChoice(title: String, collection: Collection<String>, defaultIndex: Int = 0, isCancel: Boolean = false,
                               positive: String, positiveCallback: ((Any, String) -> Unit)? = null,
                               negative: String = "", negativeCallback: ((Any) -> Unit)? = null,
                               neutral: String = "", neutralCallback: ((Any) -> Unit)? = null,
                               cancelCallback: (() -> Unit)? = null
    )

    fun showDialogSingleChoice(title: Int, array: Int, defaultIndex: Int = 0, isCancel: Boolean = false,
                               positive: Int, positiveCallback: ((Any, String) -> Unit)? = null,
                               negative: Int = 0, negativeCallback: ((Any) -> Unit)? = null,
                               neutral: Int = 0, neutralCallback: ((Any) -> Unit)? = null,
                               cancelCallback: (() -> Unit)? = null
    )

    fun showDialogSingleChoice(title: Int, collection: Collection<String>, defaultIndex: Int = 0, isCancel: Boolean = false,
                               positive: Int, positiveCallback: ((Any, String) -> Unit)? = null,
                               negative: Int = 0, negativeCallback: ((Any) -> Unit)? = null,
                               neutral: Int = 0, neutralCallback: ((Any) -> Unit)? = null,
                               cancelCallback: (() -> Unit)? = null
    )

    fun showDialogMultiChoice(title: String, array: Int, defaultIndex: List<Int> = emptyList(), isCancel: Boolean = false,
                               positive: String, positiveCallback: ((Any, String) -> Unit)? = null,
                               negative: String = "", negativeCallback: ((Any) -> Unit)? = null,
                               neutral: String = "", neutralCallback: ((Any) -> Unit)? = null,
                               cancelCallback: (() -> Unit)? = null
    )

    fun showDialogMultiChoice(title: String, collection: Collection<String>, defaultIndex: List<Int> = emptyList(), isCancel: Boolean = false,
                               positive: String, positiveCallback: ((Any, String) -> Unit)? = null,
                               negative: String = "", negativeCallback: ((Any) -> Unit)? = null,
                               neutral: String = "", neutralCallback: ((Any) -> Unit)? = null,
                               cancelCallback: (() -> Unit)? = null
    )

    fun showDialogMultiChoice(title: Int, array: Int, defaultIndex: List<Int> = emptyList(), isCancel: Boolean = false,
                               positive: Int, positiveCallback: ((Any, String) -> Unit)? = null,
                               negative: Int = 0, negativeCallback: ((Any) -> Unit)? = null,
                               neutral: Int = 0, neutralCallback: ((Any) -> Unit)? = null,
                               cancelCallback: (() -> Unit)? = null
    )

    fun showDialogMultiChoice(title: Int, collection: Collection<String>, defaultIndex: List<Int> = emptyList(), isCancel: Boolean = false,
                               positive: Int, positiveCallback: ((Any, String) -> Unit)? = null,
                               negative: Int = 0, negativeCallback: ((Any) -> Unit)? = null,
                               neutral: Int = 0, neutralCallback: ((Any) -> Unit)? = null,
                               cancelCallback: (() -> Unit)? = null
    )

    fun shoDialogBasicLongContent(title: Int, content: Int, positive: Int, negative: Int = 0)
    fun shoDialogBasicLongContent(title: String, content: String, positive: String, negative: String = "")

    /**
     * Hien thi dialog len man hinh thiet bi. Voi cac tham so truyen vao la int.
     *
     * @param title thong tin ngan gon (loi, canh bao, ...)
     * @param content mo ta chi tiet
     * @param positive dong y (ok)
     * @param positiveCallback action khi dong y
     * @param negative huy bo (cancel)
     * @param negativeCallback action khi huy bo
     * @param neutral trung lap (bo qua)
     * @param neutralCallback action khi bo qua
     * */
    fun showDialogBasic(title: Int, content: Int, isCancel: Boolean = false,
                        positive: Int = 0, positiveCallback: ((Any) -> Unit)? = null,
                        negative: Int = 0, negativeCallback: ((Any) -> Unit)? = null,
                        neutral: Int = 0, neutralCallback: ((Any) -> Unit)? = null
    )

    fun showDialogDelete(title: Int = 0, content: Int = 0, icon : Int? = null,
                         cancelOnTouchSide: Boolean = true,
                         positive: Int = 0, positiveCallback: ((Any) -> Unit)? = null,
                         negative: Int = 0, negativeCallback: ((Any) -> Unit)? = null)

    fun showDialogDelete(title: String = "", content: String = "", icon : Drawable? = null,
                         cancelOnTouchSide: Boolean = true,
                         positive: String = "", positiveCallback: ((Any) -> Unit)?= null,
                         negative: String = "", negativeCallback: ((Any) -> Unit)? = null)

    /**
     * Hien thi dialog progress cho muc dich cho doi. Bao gom hinh tron va thanh ngang
     *
     * @param title tieu de ngan gon
     * @param content noi dung chi tiet
     * @param isHorizontal tron (false) hoac ngang (true)
     * */
    fun showDialogProgress(title: String, content: String, isHorizontal: Boolean = false, isCancelTouchOutside : Boolean)

    /**
     * Hien thi dialog progress tron
     *
     * @param title tieu de ngan gon
     * @param content noi dung
     * */
    fun showDialogProgressCircle(title: String, content: String, isCancelTouchOutside : Boolean = true)

    /**
     * Hien thi dialog progress thanh ngang
     *
     * @param title tieu de
     * @param content noi dung
     * */
    fun showDialogProgressHorizontal(title: String, content: String, isCancelTouchOutside : Boolean = true)

    /**
     * Hien thi dialog progress tron
     *
     * @param title tieu de
     * @param content noi dung
     * */
    fun showDialogProgressCircle(title: Int, content: Int, isCancelTouchOutside : Boolean = true)

    /**
     * Hien thi dialog progress thanh ngang
     *
     * @param title tieu de
     * @param content noi dung
     * */
    fun showDialogProgressHorizontal(title: Int, content: Int, isCancelTouchOutside : Boolean = true)

    /**
     * Ham huy dialog
     * */
    fun dismissDialog()

    /**
     * An dialog
     * */
    fun hideDialog()

    /**
     * Hien thi toast message
     *
     * @param content
     * @param isLongTime
     * */
    fun showToast(content: String, isLongTime: Boolean = false, type: Int = 0, checkVersion: Boolean = false, gravity: Int = Gravity.CENTER)

    /**
     * Hien thi toast message
     *
     * @param content
     * @param isLongTime
     * */
    fun showToast(content: Int, isLongTime: Boolean = false, type: Int = 0, checkVersion: Boolean = false, gravity: Int = Gravity.CENTER)

    /**
     * Ham huy toast
     * */
    fun dismissToast()

    /**
     * Hien thi snack bar de show message va xu ly su kien
     * <p>
     * @param view view parent
     * @param message message thong bao
     * @param typeTime thoi gian hien thi: LENGTH_SHORT, LENGTH_LONG, LENGTH_INDEFINITE
     * @param actionName title action
     * @param onClickListener event onclick actionName
     * */
    fun showSnackBar(view: View, message: String, typeTime: Int = Snackbar.LENGTH_SHORT,
                     actionName: String? = null, onClickListener: View.OnClickListener? = null)

    /**
     * Hien thi snack bar de show message va xu ly su kien
     * <p>
     * @param view view parent
     * @param message message thong bao
     * @param typeTime thoi gian hien thi: LENGTH_SHORT, LENGTH_LONG, LENGTH_INDEFINITE
     * @param actionName title action
     * @param onClickListener event onclick actionName
     * */
    fun showSnackBar(view: View, message: Int, typeTime: Int = Snackbar.LENGTH_SHORT,
                     actionName: Int? = null, onClickListener: View.OnClickListener? = null)

    /**
     * Hide snack bar.
     * */
    fun dismissSnackBar()

    /**
     * @param layoutId
     * @param fragmentManager
     *
     * @return Fragment or null
     * */
    fun findingFragment(@IdRes layoutId: Int, fragmentManager: FragmentManager) : Fragment?

    /**
     * @param layoutId
     *
     * @return
     * */
    fun findingFragment(@IdRes layoutId: Int) : Fragment?

    /**
     * @param tag
     * @param fragmentManager
     *
     * @return Fragment or null
     * */
    fun findingFragment(tag: String, fragmentManager: FragmentManager) : Fragment?

    /**
     * @param tag
     *
     * @return
     * */
    fun findingFragment(tag: String) : Fragment?

    /**
     * Them mot fragment vao list fragment
     *
     * @param fragment
     * @param isRefresh
     *
     * @return true (add thanh cong) or false(fragment khong duoc add)
     * */
    fun addFragment(fragment: BaseFragment, isRefresh: Boolean = false) : Boolean

    /**
     * Them mot list fragment vao danh sach
     *
     * @param fragments
     * @param isRefresh
     *
     * @return Int so luong fragment duoc add thanh cong
     * */
    fun addFragment(fragments: List<BaseFragment>, isRefresh: Boolean = false) : Int

    fun displayFragment(@IdRes layoutContainer: Int, fragmentManager: FragmentManager,
                        tag: String, bundle: Bundle, isSaveCache: Boolean = false)

    fun displayFragment(@IdRes layoutContainer: Int, tag: String, bundle: Bundle, isSaveCache: Boolean = false)

    // truong hop load lai fragment
    fun reDisplayFragment(@IdRes layoutContainer: Int, tag: String, bundle: Bundle, isSaveCache: Boolean = false)
    /**
     * Hien thi fragment
     * <p>
     * @param layoutContainer la id cua FrameLayout chua fragment
     * @param fragmentManager bien quan ly fragment cua activity
     * @param tag ten cua fragment ClassFragment::class.java.canonicalName
     * @param tagParent ten cua fragment cha ClassFragment::class.java.canonicalName
     * @param bundle du lieu duoc truyen theo fragment (first data)
     * */
    fun displayMultiFragment(@IdRes layoutContainer: Int, fragmentManager: FragmentManager,
                             tag: String, tagParent: String,
                             bundle: Bundle)
    /**
     * Hien thi fragment
     * <p>
     * @param layoutContainer
     * @param tag
     * @param tagParent
     * @param bundle
     * */
    fun displayMultiFragment(@IdRes layoutContainer: Int,
                             tag: String, tagParent: String,
                             bundle: Bundle)

    /**
     *
     * */
    fun backStackFragment() : Boolean

    fun backStackToTopFragment() : Boolean

    /**
     * @param view view focus
     * */
    fun showSoftKeyboard(view: View)

    /**
     *
     * */
    fun hideSoftKeyboard(view: View?)

    fun hideSoftKeyboard()

    /**
     *
     * */
    fun startService()

    /**
     *
     * */
    fun stopService()

    /**
     *
     * <p>
     * @param activityOld activity cu
     * @param intent intent activityOld -> activityNew
     * @param isFinish true: Finish activity cu
     * */
    fun goToActivity(activityOld: Activity, intent: Intent, isFinish: Boolean = false, typeAnimation: Int = TYPE_ANIMATION_SLIDE_RIGHT_TO_LEFT)

    /**
     * Animation when change activity go ...
     * */
    fun overridePendingSlideFromRigthToLeft()

    fun overridePendingSlideFromBottomToTop()

    /**
     * Animation when change fragment go ...
     * */
    fun overridePendingSlideFromRightToLeftFragment(fragmentTransaction: FragmentTransaction)

    /**
     * Animation when change activity back ...
     * */
    fun overridePendingSlideFromLeftToRight()

    fun overridePendingSlideFromTopToBottom()

    /**
     * Animation when change fragment back ...
     * */
    fun overridePendingSlideFromLeftToRightFragment(fragmentTransaction: FragmentTransaction)

    /**
     * Set title cho action bar.
     * */
    fun setTitleActionBar(title: CharSequence)

    /**
     * Set subtitle cho action bar.
     * */
    fun setSubtitleActionBar(subtitle: CharSequence)

    /**
     * Set icon cho action bar.
     * */
    fun setIconActionBar(icon: Int)

    /**
     * Ẩn hiện icon home trên action bar (Icon cho phep trở về thay cho nút back)
     * */
    fun displayIconHomeActionbar(isShow: Boolean = true)

    /* Ẩn hiện ô tìm kiếm trên toolbar */
    fun showToolbarSearch(isShow: Boolean = true)

    fun <T> handlerMultiUIwh960(wh960: () -> T, default: () -> T): T
    fun <T> handlerMultiUIh960Default(w960: () -> T, h960default: () -> T): T
    fun <T> handlerMultiUI(w960: () -> T, h960: () -> T, default: () -> T): T

    fun isWh960Screen() : Boolean
    fun isW960Screen() : Boolean

//    fun getHtmlPrint(jsonContent: JsonContent, typeHeader: String, code: String = "", number: String = "") : String

    fun registerNetworkBroadcastForNougat()

    fun unregisterNetworkChanges()

    fun networkAction(state: Boolean)

    fun setDateSetResultListener(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int)
    fun setTimeSetResultListener(view: TimePickerDialog, hourOfDay: Int, minute: Int, second: Int)

    fun showDialogDate(date: Calendar, enableFuture : Boolean? = false)
    fun showDialogDate(date: Date, enableFuture: Boolean? = false)
    fun showDialogDateNow(enableFuture: Boolean? = false)

    fun showDialogTime(date: Calendar)
    fun showDialogTime(date: Date)
    fun showDialogTimeNow()
    fun showDialogDateV2(enableFutureDate : Boolean? = false, listener : (Date) -> Unit)
    fun showDialogDateV2(startDate : Date?, endDate : Date?, enableFutureDate : Boolean = false, listener: (Date, Date) -> Unit)

    fun slideUp(view: View)
    fun slideDown(view: View)

    fun showMessageCheckInternet()

    fun actionSearchBarcode(barcode: String)

    fun checkCamera(): Boolean
    fun goScanActivity()
    fun checkCameraPermission(): Boolean

//    fun showCamera()

    fun onBackPressedV2()
    fun onBackPressedV3()

    fun actionStartCashierServiceDone()

    fun checkDemoVNPay(): Boolean

    fun getWidthItemProduct(): Int
    fun getCountColumnProduct(): Int

//    fun display2ScreenPresent(serverEvents: ServerEvents, typeHeader: Int)

    fun showVideoDisplay()
    fun showVideoMenuDisplay(html: String)
    fun displayLCDDoubleString(isShow: Boolean, title: String, total : Double)

    fun checkScreenOn(): Boolean

    fun showRateApp()
    fun checkAndGoAppStore()
//    fun getWidthItemProductCustom() : Int

    fun checkPermission(permission: String)
    fun checkReadPermissionFlowGroup(permission: String)

    fun showQRCodeSecondScreen(qrCode : Bitmap?, isShow : Boolean, accountId : Int?)

    fun showNetworkStatus(status : Int)

    fun setPrintServ()
//    fun getPrintServ(): PrintService
}