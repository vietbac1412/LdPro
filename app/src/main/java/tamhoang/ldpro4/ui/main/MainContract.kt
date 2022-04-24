package tamhoang.ldpro4.ui.main

import android.app.Activity
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import tamhoang.ldpro4.data.CashiersService
import tamhoang.ldpro4.data.model.*
import tamhoang.ldpro4.ui.basic.ActivityView
import tamhoang.ldpro4pos365.ui.basic.BaseActivity
import tamhoang.ldpro4pos365.ui.basic.BasePresenter
import tamhoang.ldpro4pos365.ui.basic.MvpView

object MainContract {

    interface View: MvpView, ActivityView {
        fun displayChartFragment()
        fun displayOrderFragment()

        fun expiryDate()

        fun showToastNotification(mess: String)

        fun displayProgressLoading(isShow: Boolean = true)

        fun setupDataFollowAccount(isChangeBranch: Boolean = false, isChangeFieldId: Boolean = false)

        fun displayProgressDialog(isShow: Boolean)

    }

    abstract class Presenter: BasePresenter<View>() {

    }
}