package tamhoang.ldpro4.ui.custom.recycle.loadmore

import androidx.recyclerview.widget.RecyclerView
import tamhoang.ldpro4.ui.custom.recycle.BaseRecycleAdapter
import tamhoang.ldpro4.ui.custom.recycle.BaseRecycleView

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 23/03/2018.
 */
interface OnEndlessScrolling {
    /**
     * Load new data
     *
     * @see {http://stackoverflow.com/questions/39445330/cannot-call-notifyiteminserted-method-in-a-scroll-callback-recyclerview-v724-2}
     * <p>
     * view.post(() -> mAdapter.insertItem(positionStart, insertList));
     * [BaseRecycleAdapter.insertItem]
     */
     fun loadNextPage(page: Int, totalItemsCount: Int, view: RecyclerView)

    /**
     * Clear all data ---> new action.
     * <p>
     * mList.clear();
     * mAdapter.notifyDataSetChanged(); [BaseRecycleAdapter.reloadAll]
     * mEndlessScrollListener.resetState(); [BaseRecycleView.resetEndlessScrolling]
     */
     fun resetEndless()
}