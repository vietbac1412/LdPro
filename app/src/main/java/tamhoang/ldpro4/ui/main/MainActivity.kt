 package tamhoang.ldpro4.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Keep
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.google.android.material.navigation.NavigationView

import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.partial_navigation_header.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tamhoang.ldpro4.Application
import tamhoang.ldpro4.R
import tamhoang.ldpro4.data.BusEvent

import tamhoang.ldpro4.data.model.*



import tamhoang.ldpro4.util.NetUtil
import tamhoang.ldpro4.util.extension.*
//import tamhoang.ldpro4pos365.injection.component.DaggerApplicationComponent
import tamhoang.ldpro4pos365.injection.modul.ApiModule
import tamhoang.ldpro4pos365.injection.modul.ApplicationModule
import tamhoang.ldpro4pos365.injection.modul.DataModule
import tamhoang.ldpro4pos365.injection.modul.DbModule
import tamhoang.ldpro4pos365.ui.basic.BaseActivity
import java.util.*
import javax.inject.Inject

@Keep
open class MainActivity : BaseActivity(), MainContract.View {

    companion object {
        private const val BUNDLE_NAVIGATION_POSITION = "bundle_navigation_position"
    }

    @Inject lateinit var bus: EventBus
    @Inject lateinit var presenter: MainPresenter

    private var mDrawerLayoutView: DrawerLayout? = null
    private lateinit var mDrawerToggle: ActionBarDrawerToggle // Animation open/close drawable.

    private var mNavigationPosition = 0


    private lateinit var mOrderView: MenuItem

    open lateinit var mCurrentMenuItem: MenuItem

    private lateinit var mHeaderView: View

    private var imvStatus : ImageView? = null

    override fun createLayout() {
        activityComponent.inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_main)
    }

    override fun createVariableStart(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            displayOrderFragment()
        }
    }

    override fun createVariableReload(savedInstanceState: Bundle) {
        super.createVariableReload(savedInstanceState)

        mNavigationPosition = savedInstanceState.getInt(BUNDLE_NAVIGATION_POSITION)
    }

    override fun createVariableView() {

        // Drawer layout root.
        mDrawerLayoutView = findViewById(R.id.drawerLayoutView)
        mDrawerLayoutView?.let { drawerLayout ->
            mDrawerToggle = setupDrawerToggle()

        }

        // Setup drawer view
        setupDrawerContent(navigationView) // Handler event item click.
        mCurrentMenuItem = navigationView.menu.findItem(R.id.nav_order_fragment)

        mHeaderView = navigationView.getHeaderView(0)

        imvStatus = mHeaderView.imvStatus

        // Check UI show/hide menu.
        val menu  = navigationView.menu
        mOrderView        = menu.findItem(R.id.nav_order_fragment)


        setVersionItem()

    }

    fun setVersionItem(){
//        val versionString = resources.getString(R.string.phien_ban) + " " + BuildConfig.VERSION_NAME
//        val dateReleaseString = resources.getString(R.string.cap_nhat) + " " + BuildConfig.VERSION_CODE.convertVersionCodeToDateString()
//        val spannableString = SpannableString(versionString + "\n" + dateReleaseString)
//        spannableString.setSpan(RelativeSizeSpan(0.8f), versionString.length, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannableString.setSpan(ForegroundColorSpan(Color.LTGRAY), versionString.length, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        mVersion.title = spannableString
    }


    override fun createVariableNormal() {
        if (!bus.isRegistered(this)) bus.register(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Action open or close drawer.
        if (::mDrawerToggle.isInitialized && mDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return when (item.itemId) {

            else -> super.onOptionsItemSelected(item)
        }
    }

    // Handler event click.
    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            mNavigationPosition = menuItem.itemId
            selectDrawerItem(menuItem)
            true
        }
    }


    // Handler event item click.
    private fun selectDrawerItem(menuItem: MenuItem, isRootLoad: Boolean = false) {

        if (::mCurrentMenuItem.isInitialized && mCurrentMenuItem == menuItem && !isRootLoad) {
            mDrawerLayoutView?.closeDrawers() // Dong navigation

            return
        }
        when(menuItem.itemId) {

            R.id.nav_order_fragment -> {
                setChooseMenuItem(menuItem)
                displayOrderFragment()
            }

            else -> {
                setChooseMenuItem(menuItem)
                displayOrderFragment()
            }
        }
        mDrawerLayoutView?.closeDrawers() // Dong navigation
    }

    fun setChooseMenuItem(newMenuItem : MenuItem) {
        if (::mCurrentMenuItem.isInitialized) {
            val currentTitle = SpannableString(mCurrentMenuItem.title.toString())
            mCurrentMenuItem.title = currentTitle
        }

        newMenuItem.isCheckable = true
        mCurrentMenuItem = newMenuItem
    }

    // Animation open/close drawable.
    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        return ActionBarDrawerToggle(this, mDrawerLayoutView, toolbarView, R.string.drawer_open, R.string.drawer_close)
    }

    // Animation open/close drawable.
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (::mDrawerToggle.isInitialized) {
            mDrawerToggle.syncState() // Sync the toggle state after onRestoreInstanceState has occurred.
        }
    }

    // Animation open/close drawable.
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (::mDrawerToggle.isInitialized) {
            mDrawerToggle.onConfigurationChanged(newConfig) // Pass any configuration change to the drawer toggles
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(BUNDLE_NAVIGATION_POSITION, mNavigationPosition)
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        presenter.detachView()
        bus.unregister(this)

        super.onDestroy()
    }
    override fun displayChartFragment() {
        val bundle = Bundle()

    }

    //===========================================HANDLER==========================================//

    override fun displayOrderFragment() {
        displayOrderDefaultFragment()
    }

    private fun displayOrderDefaultFragment() {
        val bundle = Bundle()
//        bundle.putString(OrderFragment.BUNDLE_NAME_CLASS_CALL_FRAGMENT,
//                MainActivity::class.java.canonicalName!!)
//        displayFragment(
//                layoutContainer = R.id.flFragmentRootView,
//                tag = OrderFragment::class.java.canonicalName!!,
//                bundle = bundle)
    }


    override fun expiryDate() {
//        showDialogBasic(
//                R.string.thong_bao,
//                R.string.thong_bao_het_thoi_gian_su_dung,
//                false,
//                R.string.dong_y,
//                positiveCallback = {
//                    logout()
//                },
//                neutralCallback = {
//                    logout()
//                })
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (presenter.isBack) {
                    presenter.isBack = false
                    dismissToast()
                    super.onKeyUp(keyCode, event)
                } else {
                    presenter.isBack = true
                    true
                }
                true
            }
            else -> {
                super.onKeyUp(keyCode, event)
            }
        }
    }

    override fun showToastNotification(mess: String) {
        (application as Application).playSoundNotification()
    }


    override fun displayProgressLoading(isShow: Boolean) {
//        if (isShow) {
//            showDialogProgressCircle(R.string.thiet_lap_du_lieu, R.string.vui_long_doi_ffEl, false)
//        } else {
//            dismissDialog()
//        }
    }

    override fun networkAction(state: Boolean) {
        super.networkAction(state)

        showNetworkStatus(if (state) ONLINE else OFFLINE)
    }


    override fun setupDataFollowAccount(isChangeBranch: Boolean, isChangeFieldId: Boolean) {
//        (application as Application).applicationComponent =
//                DaggerApplicationComponent.builder().applicationModule(ApplicationModule(application))
//                        .dataModule(DataModule())
//                        .apiModule(ApiModule())
//                        .dbModule(DbModule())
//                        .build()
//        activityComponent.inject(this)
//        presenter.attachView(this)
//
//        presenter.mDataManager = (application as Application).applicationComponent.dataManager()

    }

    override fun displayProgressDialog(isShow: Boolean) {
        if (isShow) {
//            showDialogProgressCircle(R.string.thiet_lap_du_lieu, R.string.vui_long_doi_ffEl, false)
        } else {
            dismissDialog()
        }
    }


    override fun showNetworkStatus(status: Int) {
        super.showNetworkStatus(status)
//        when(status) {
//            ONLINE -> imvStatus?.background = resources.getDrawable(R.drawable.bg_secondary_oval)
//            OFFLINE -> imvStatus?.background = resources.getDrawable(R.drawable.bg_gray_oval)
//            ERROR -> imvStatus?.background = resources.getDrawable(R.drawable.bg_red_oval)
//        }
    }
}