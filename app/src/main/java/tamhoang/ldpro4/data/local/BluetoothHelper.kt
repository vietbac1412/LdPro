package tamhoang.ldpro4.data.local

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 24/10/2018.
 */
@Singleton
class BluetoothHelper
@Inject
constructor(val bluetoothAdapter: BluetoothAdapter?,
            val uuid: UUID) {

    fun getPairedDevices(notInit: () -> Unit, empty: () -> Unit, notEmpty: (Set<BluetoothDevice>) -> Unit) {
        bluetoothAdapter?.let {
            if (it.isEnabled) {
                val pairedDevices = it.bondedDevices
                if (pairedDevices.isEmpty()) {
                    empty()
                } else {
                    notEmpty(pairedDevices)
                }
            } else {
                notInit()
            }
        }
    }

    private fun enableBluetooth(context: Context) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        context.startActivity(enableBtIntent)
    }
}