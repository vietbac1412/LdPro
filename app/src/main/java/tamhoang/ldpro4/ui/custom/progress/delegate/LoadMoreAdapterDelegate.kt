package tamhoang.ldpro4.ui.custom.progress.delegate

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_circle_progress.view.*
import tamhoang.ldpro4.R
import tamhoang.ldpro4.ui.custom.progress.model.LoadMore

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 23/03/2018.
 */
class LoadMoreAdapterDelegate(activity: Activity) : AdapterDelegate<List<Any>>() {

    private val inflater: LayoutInflater = activity.layoutInflater

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_circle_progress, parent, false))
    }

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is LoadMore
    }

    override fun onBindViewHolder(items: List<Any>, position: Int,
                                  holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val viewHolder = holder as LoadMoreAdapterDelegate.ViewHolder
        val loadMore = items[position]
    }

    inner class ViewHolder(itemVew: View) : RecyclerView.ViewHolder(itemVew) {
        val loadMoreView = itemView.baseCircleProgress
    }
}