package tamhoang.ldpro4pos365.util.extension

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity


/**
 * @author : Hanet Electronics
 * @Skype  : chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email  : muoick@hanet.com
 * @Website: http://hanet.com/
 * @Project: Music
 * Created by CHUKIMMUOI on 2/2/2018.
 */

/**
 * Kiểm tra kết nối internet của thiết bị.
 *
 * @return giá trị boolean có kết nối(true), không có kết nối (false).
 * */
fun Context.isNetworkConnected() : Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

/**
 * Tắt bật BroadcastReceiver.
 *
 * @param componentClass: Tên class. eg: ClassName::class.java
 * @param enable trạng thái on(true) hay off(false)
 * */
fun Context.toggleAndroidComponent(componentClass: Class<*>, enable: Boolean) {
    val componentName = ComponentName(this, componentClass)

    val newState = if (enable)
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    else
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED

    packageManager.setComponentEnabledSetting(componentName, newState, PackageManager.DONT_KILL_APP)
}

fun Context.isRunService(serviceName: String): Boolean {
    val manager = getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
    manager.getRunningServices(Integer.MAX_VALUE).forEach {
        if (it.service.className == serviceName) {
            return true
        }
    }
    return false
}