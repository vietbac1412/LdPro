package tamhoang.ldpro4.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import timber.log.Timber
import tamhoang.ldpro4pos365.ui.basic.BaseActivity


/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 31/08/2018.
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    private var isOnline: Boolean = false

    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (context is BaseActivity) {
                if (isOnline(context)) {
                    if (!isOnline) {
                        Timber.e("ON")
                        isOnline = true
                        context.networkAction(true)
                    }
                } else {
                    if (isOnline) {
                        Timber.e("OFF")
                        isOnline = false
                        context.networkAction(false)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isOnline(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            //should check null because in airplane mode it will be null
            netInfo != null && netInfo.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}