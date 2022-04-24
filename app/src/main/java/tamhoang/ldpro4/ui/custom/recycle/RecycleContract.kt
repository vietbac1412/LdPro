package tamhoang.ldpro4.ui.custom.recycle

import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.JustifyContent
import tamhoang.ldpro4.ui.custom.recycle.listener.StartSnapHelper

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 26/02/2018.
 */
object RecycleContract {

    const val SPACES_ITEM = 16

    interface View {
        /**
         * Su dung duoc cho toan bo cac layout manager
         * <p>
         * @param typeLayout bap gom 3 loai
         * @param spanCount so cot su dung cho layout manager dang grid
         * @param isHorizontal true: hien thi theo chieu ngang, false: hien thi theo chieu doc
         * @param isReverse true: dao huong scroll
         * */
        fun initLayoutManager(typeLayout: Int,
                              space: Int = SPACES_ITEM,
                              spanCount: Int = 0,
                              isHorizontal: Boolean = false,
                              isReverse: Boolean = false)

        /**
         * Su dung cho LinearLayoutManager
         * <p>
         * @param isHorizontal
         * @param isReverse
         * */
        fun initLinearLayoutManager(space: Int = SPACES_ITEM,
                                    isHorizontal: Boolean = false,
                                    isReverse: Boolean = false)

        /**
         * Su dung cho GridLayoutManager
         * <p>
         * @param spanCount so cot nho nhat mac dinh
         * @param itemWidth kich thuoc cua 1 item
         * @param isHorizontal
         * @param isReverse
         * */
        fun initGridLayoutManager(space: Int = SPACES_ITEM,
                                  spanCount: Int,
                                  itemWidth: Int = -1,
                                  isHorizontal: Boolean = false,
                                  isReverse: Boolean = false)

        /**
         * Su dung cho StaggeredGridLayoutManager
         * <p>
         * @param spanCount so cot nho nhat mac dinh
         * @param itemWidth kich thuoc cua 1 item (dp)
         * @param isHorizontal
         * @param isReverse
         * */
        fun initStaggeredGridLayoutManager(space: Int = SPACES_ITEM,
                                           spanCount: Int,
                                           itemWidth: Int = -1,
                                           isHorizontal: Boolean = false,
                                           isReverse: Boolean = false)

        /**
         * Update spanCount theo kich thuoc man hinh
         * <p>
         * @param layoutManager
         * @param typeLayout
         * @param spanCount so cot mac dinh
         * @param itemWidth kich thuoc cua 1 item (dp)
         * */

        fun initFlexboxLayoutManager(isHorizontal: Boolean = true,
                                     isReverse: Boolean = false,
                                     flexWrap: Int = FlexWrap.WRAP,
                                     justifyContent: Int = JustifyContent.SPACE_EVENLY)

        fun updateSpanCountWhenRuntime(layoutManager: RecyclerView.LayoutManager,
                                       typeLayout: Int,
                                       spanCount: Int,
                                       itemWidth: Int)

        fun setLinearLayoutSnapHelper(typeGravity: Int = Gravity.CENTER, isPager: Boolean = false)

        fun resetEndlessScrolling(): Boolean

        fun setMargin(value: Int)

        fun attachStartSnapHelper(startSnapHelper: StartSnapHelper)

        fun removeItemDecor()
    }

    interface Adapter {
        fun insertItem(position: Int, any: Any, isScroll: Boolean = false)

        fun insertItem(positionStart: Int, insertList: List<Any>, isScroll: Boolean = false)

        fun insertItem(insertList: List<Any>, isScroll: Boolean = false)

        fun loadMoreItem(insertList: List<Any>)

        fun updateItem(position: Int, isScroll: Boolean = false)

        fun updateItem(positionStart: Int, itemCount: Int, isScroll: Boolean = false)

        fun removeItem(position: Int, isScroll: Boolean = false)

        fun removeItem(positionStart: Int, itemCount: Int, isScroll: Boolean = false)

        fun movedItem(fromPosition: Int, toPosition: Int, isScroll: Boolean = false)

        fun clearAll()

        fun reloadAll(isScroll: Boolean = false, position: Int = 0)
    }

    interface GridLayout {
        fun handlerSpanCount(recyclerView: RecyclerView, spanCount: Int)
    }
}