package tamhoang.ldpro4.data

import android.content.Context

import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit

import tamhoang.ldpro4.data.local.DatabaseHelper
import tamhoang.ldpro4.data.local.PreferencesHelper
import tamhoang.ldpro4.data.model.*
import tamhoang.ldpro4.data.remote.Pos365Service
import tamhoang.ldpro4.util.extension.*
import tamhoang.ldpro4pos365.injection.ApplicationContext

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager
@Inject
constructor(private val mPos365Service: Pos365Service,
            private val mPreferencesHelper: PreferencesHelper,
            private val mDatabaseHelper: DatabaseHelper,
            @ApplicationContext private val context: Context,
            private val bus: EventBus,
            private val mRetrofitBuilder: Retrofit.Builder) {
    

}