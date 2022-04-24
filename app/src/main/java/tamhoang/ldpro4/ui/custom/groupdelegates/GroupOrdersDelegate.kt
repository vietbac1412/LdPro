package tamhoang.ldpro4.ui.custom.groupdelegates

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_group_order.view.*
import tamhoang.ldpro4.R
import tamhoang.ldpro4.data.model.GroupOrder
import tamhoang.ldpro4.util.extension.convertToStringWithDayInWeek
import tamhoang.ldpro4.util.extension.formatPriceTimeDescription

class GroupOrdersDelegate(val activity : Activity) : AdapterDelegate<List<Any>>()  {
    private val mInflater: LayoutInflater = activity.layoutInflater

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = mInflater.inflate(R.layout.item_group_order, parent, false)
        return ViewHolder(view)
    }

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is GroupOrder
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {
        val viewHolder = holder as ViewHolder
        val order = items[position] as GroupOrder

        viewHolder.binOrder(order)
    }

    inner class ViewHolder(val view: View)
        : RecyclerView.ViewHolder(view) {
        init {
            view.tvTimeView.isSelected = true
            view.tvCountOrdersView.isSelected = true
        }

        fun binOrder(paymentData: GroupOrder) {
            with(paymentData) {

                view.tvTimeView.text = date.convertToStringWithDayInWeek()

                val countDisplay = "${if (count < 10) "0" else ""}${count.toDouble().formatPriceTimeDescription()} ${activity.resources.getString(R.string.hoa_don)}"

                view.tvCountOrdersView.text = countDisplay

            }
        }
    }
}