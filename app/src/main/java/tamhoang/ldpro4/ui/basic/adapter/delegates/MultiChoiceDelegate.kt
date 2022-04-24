package tamhoang.ldpro4.ui.basic.adapter.delegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_custom_check_box.view.*
import tamhoang.ldpro4.R
import tamhoang.ldpro4.ui.basic.adapter.ItemCheckBox

class MultiChoiceDelegate (val activity : Activity,val action : (Int, Boolean) -> Unit) : AdapterDelegate<List<Any>>() {

    private val layoutInflater = activity.layoutInflater

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_custom_check_box, parent, false)
        return ViewHolder(view)
    }

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is ItemCheckBox
    }

    override fun onBindViewHolder(items: List<Any>, postion: Int, holder: RecyclerView.ViewHolder, payload: MutableList<Any>) {
        val data = items[postion] as ItemCheckBox
        val viewHolder = holder as ViewHolder
        viewHolder.binData(data)
    }

    inner class ViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
        fun binData (item: ItemCheckBox) {
//            view.checkBox.text = item.title
//            view.checkBox.isChecked = item.isCheck
//            view.checkBox.setOnCheckedChangeListener { _, check ->
//                action(adapterPosition, check)
//            }
        }
    }
}