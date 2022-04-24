package tamhoang.ldpro4.ui.custom.recycle

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 26/02/2018.
 */
open class BaseRecycleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), RecycleContract.Adapter {

    private lateinit var mRecyclerView: RecyclerView

    var delegateManager: AdapterDelegatesManager<List<Any>> = AdapterDelegatesManager()

    var list: MutableList<in Any> = emptyList<Any>().toMutableList()
    set(value) {
        list.clear()
        field = value
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun getItemViewType(position: Int): Int {
        return delegateManager.getItemViewType(list.toList() as List<Any>, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateManager.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateManager.onBindViewHolder(list.toList() as List<Any>, position, holder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        delegateManager.onBindViewHolder(list.toList() as List<Any>, position, holder, payloads)
    }

    override fun insertItem(position: Int, any: Any, isScroll: Boolean) {
        if (position >= 0 && any != null) {
            list.add(position, any)
            notifyItemInserted(position)

            if (isScroll) {
                mRecyclerView.scrollToPosition(position)
            }
        }
    }

    override fun insertItem(positionStart: Int, insertList: List<Any>, isScroll: Boolean) {
        if (positionStart >= 0 && insertList != null) {
            val itemCount = insertList.size
            if (itemCount > 0) {
                list.addAll(positionStart, insertList)
                notifyItemRangeInserted(positionStart, itemCount)

                if (isScroll) {
                    mRecyclerView.scrollToPosition(positionStart)
                }
            }
        }
    }

    override fun insertItem(insertList: List<Any>, isScroll: Boolean) {
        insertItem(itemCount, insertList, isScroll)
    }

    override fun loadMoreItem(insertList: List<Any>) {
        insertList?.let {
            var positionStart = itemCount
            if (positionStart > 0) {
                positionStart -= 1
                list.removeAt(positionStart)
                notifyItemRemoved(positionStart)
            }

            val itemCount = insertList.size
            if (itemCount > 0) {
                list.addAll(positionStart, insertList)
                notifyItemRangeInserted(positionStart, itemCount)
            }
        }
    }

    override fun updateItem(position: Int, isScroll: Boolean) {
        if (position in 0 until itemCount) {
            notifyItemChanged(position)

            if (isScroll) {
                mRecyclerView.scrollToPosition(position)
            }
        }
    }

    //https://stackoverflow.com/questions/32769009/recyclerview-notifyitemrangechanged-doesnt-show-new-data
    override fun updateItem(positionStart: Int, itemCount: Int, isScroll: Boolean) {
        if (positionStart >= 0 && itemCount > 0 && positionStart + itemCount <= getItemCount()) {
            notifyItemRangeChanged(positionStart, itemCount)

            if (isScroll) {
                mRecyclerView.scrollToPosition(positionStart)
            }
        }
    }

    override fun removeItem(position: Int, isScroll: Boolean) {
        if (position in 0 until itemCount) {
            list.removeAt(position)
            notifyItemRemoved(position)

            if (isScroll) {
                val positionNew = position - 1
                mRecyclerView.scrollToPosition(if (positionNew >= 0) positionNew else 0)
            }
        }
    }

    override fun removeItem(positionStart: Int, itemCount: Int, isScroll: Boolean) {
        if (positionStart >= 0 && itemCount > 0 && positionStart + itemCount <= getItemCount()) {
            for (i in 0 until itemCount) {
                list.removeAt(positionStart)
            }
            notifyItemRangeRemoved(positionStart, itemCount)

            if (isScroll) {
                val positionNew = positionStart - 1
                mRecyclerView.scrollToPosition(if (positionNew >= 0) positionNew else 0)
            }
        }
    }

    override fun movedItem(fromPosition: Int, toPosition: Int, isScroll: Boolean) {
        val sizeList = itemCount
        if (fromPosition in 0 until sizeList && toPosition >= 0 && toPosition < sizeList && fromPosition != toPosition) {
            list.removeAt(fromPosition)
            list[fromPosition]?.let { list.add(toPosition, it) }
            notifyItemMoved(fromPosition, toPosition)

            if (isScroll) {
                mRecyclerView.scrollToPosition(toPosition)
            }
        }
    }

    override fun clearAll() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun reloadAll(isScroll: Boolean, position: Int) {
        try {
            notifyDataSetChanged()

            if (isScroll) {
                mRecyclerView.scrollToPosition(position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}