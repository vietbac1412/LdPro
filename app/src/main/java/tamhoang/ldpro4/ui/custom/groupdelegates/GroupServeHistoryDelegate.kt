package tamhoang.ldpro4.ui.custom.groupdelegates

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_group_order.view.tvTimeView
import kotlinx.android.synthetic.main.item_group_serve_history.view.*
import tamhoang.ldpro4.R
import tamhoang.ldpro4.data.model.GroupServeHistory
import tamhoang.ldpro4.util.extension.convertToStringDateTime

class GroupServeHistoryDelegate(val activity: Activity) : AdapterDelegate<List<Any>>() {
    private val mInflater: LayoutInflater = activity.layoutInflater

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is GroupServeHistory
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = mInflater.inflate(R.layout.item_group_serve_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val viewHolder = holder as GroupServeHistoryDelegate.ViewHolder
        val serve = items[position] as GroupServeHistory

        viewHolder.bin(serve)
    }

    inner class ViewHolder(val view: View)
        : RecyclerView.ViewHolder(view) {
        init {
            view.tvTimeView.isSelected = true
            view.tvNameEmployee.isSelected = true
        }

        fun bin(paymentData: GroupServeHistory) {
            with(paymentData) {
                view.tvTimeView.text = date.convertToStringDateTime()
                view.tvNameEmployee.text = getNameDisplay()
                view.tvRoomName.text = roomName
            }
        }
    }
}