package tamhoang.ldpro4.ui.custom.groupdelegates

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_group_order.view.*
import tamhoang.ldpro4.R
import tamhoang.ldpro4.util.extension.convertToStringDateFormat

class GroupInventoryDelegate(val activity: Activity) : AdapterDelegate<List<Any>>() {

    private val mInflater: LayoutInflater = activity.layoutInflater

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = mInflater.inflate(R.layout.item_group_order, parent, false)
        return ViewHolder(view)
    }

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return true
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {
        val viewHolder = holder as ViewHolder
//        val order = items[position] as GroupInventory

        viewHolder.binOrder()
    }

    inner class ViewHolder(val view: View)
        : RecyclerView.ViewHolder(view) {
//        init {
//            view.tvTimeView.isSelected = true
//            view.tvCountOrdersView.isSelected = true
//        }

        fun binOrder() {
//            with(paymentData) {
//
//                view.tvTimeView.text = date.convertToStringDateFormat()
//
//                val suffix = activity.resources.getString(R.string.phieu_kiem_ke).toLowerCase()
//                val countDisplay = "${if (count < 10) "0" else ""}$count $suffix"
//
//                view.tvCountOrdersView.text = countDisplay
//
//            }
        }
    }
}