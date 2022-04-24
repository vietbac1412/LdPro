package tamhoang.ldpro4.ui.custom.groupdelegates

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_group_order.view.*
import tamhoang.ldpro4.R
import tamhoang.ldpro4.data.model.GroupOrderStock
import tamhoang.ldpro4.util.extension.convertToStringDateFormat

class GroupOrderStockDelegate(val activity: Activity) : AdapterDelegate<List<Any>>() {

    val mInflater : LayoutInflater = activity.layoutInflater

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is GroupOrderStock
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = mInflater.inflate(R.layout.item_group_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payload: MutableList<Any>) {
        val viewHolder = holder as ViewHolder
        val result = items[position] as GroupOrderStock
        viewHolder.binData(result)
    }

    inner class ViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        fun binData(group : GroupOrderStock) {
            with(group){
                view.tvTimeView.text = date.convertToStringDateFormat()
                val suffix = activity.getString(R.string.ma_nhap_hang).toLowerCase()
                view.tvCountOrdersView.text = "$count $suffix"
            }
        }
    }
}