package tamhoang.ldpro4.ui.main

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import tamhoang.ldpro4.Application
import tamhoang.ldpro4.data.DataManager
import tamhoang.ldpro4.data.local.DatabaseHelper
import tamhoang.ldpro4.data.local.PreferencesHelper
import tamhoang.ldpro4pos365.injection.ApplicationContext
import tamhoang.ldpro4pos365.injection.ConfigPersistent
import tamhoang.ldpro4pos365.ui.basic.BaseActivity
import java.util.*
import javax.inject.Inject

@ConfigPersistent
class MainPresenter
@Inject constructor(var mDataManager: DataManager,
                    private val mDatabaseHelper: DatabaseHelper,
                    private val preferencesHelper: PreferencesHelper,
                    private val bus: EventBus,
                    @ApplicationContext private val context: Context) : MainContract.Presenter() {
    var dateFrom: String = ""
    var dateTo: String = ""

    private val mDisposables by lazy { CompositeDisposable() }
    private val mDate by lazy { Date() }

    var isBack = false

    override fun attachView(view: MainContract.View) {
        super.attachView(view)
        (context as Application).isLogin = true
    }

    override fun detachView() {
        (context as Application).isLogin = false
        super.detachView()
        mDisposables.clear()
    }

    private fun getBaseActivity() = view as BaseActivity

}