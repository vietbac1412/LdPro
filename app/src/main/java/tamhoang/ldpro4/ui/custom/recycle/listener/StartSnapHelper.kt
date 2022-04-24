package tamhoang.ldpro4.ui.custom.recycle.listener

import android.view.View
import androidx.recyclerview.widget.*

class StartSnapHelper : LinearSnapHelper() {

    private lateinit var mVerticalHelper : OrientationHelper
    private lateinit var mHorizontalHelper : OrientationHelper

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        val intArray = intArrayOf(0,0)
        if (layoutManager.canScrollHorizontally()) {
            intArray[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
        } else intArray[0] = 0
        if (layoutManager.canScrollVertically()) {
            intArray[1] = distanceToStart(targetView, getVerticalHelper(layoutManager))
        } else intArray[1] = 0
        return intArray
    }

    private fun distanceToStart(targetView: View, helper : OrientationHelper) : Int {
        return helper.getDecoratedStart(targetView) - helper.startAfterPadding
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        when (layoutManager) {
            is LinearLayoutManager -> {
                return if (layoutManager.canScrollHorizontally()) {
                    getStartView(layoutManager, getHorizontalHelper(layoutManager))
                } else {
                    getStartView(layoutManager, getVerticalHelper(layoutManager))
                }
            }
            is GridLayoutManager -> {
                return if (layoutManager.canScrollHorizontally()) {
                    getStartView(layoutManager, getHorizontalHelper(layoutManager))
                } else {
                    getStartView(layoutManager, getVerticalHelper(layoutManager))
                }
            }
        }
        return super.findSnapView(layoutManager)
    }

    private fun getStartView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper) : View? {
        if (layoutManager is LinearLayoutManager) {
            val firstChildPosition = layoutManager.findFirstVisibleItemPosition()
            val isLastItem = layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1
            if (firstChildPosition == RecyclerView.NO_POSITION || isLastItem) {
                return null
            }
            val childView = layoutManager.findViewByPosition(firstChildPosition)
            return if (helper.getDecoratedEnd(childView) >= helper.getDecoratedMeasurement(childView)/2
                    && helper.getDecoratedEnd(childView) >0) {
                childView
            } else {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                    null
                } else {
                    layoutManager.findViewByPosition(firstChildPosition + 1)
                }
            }
        }
        if (layoutManager is GridLayoutManager) {
            val firstChildPosition = layoutManager.findFirstVisibleItemPosition()
            val isLastItem = layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1
            if (firstChildPosition == RecyclerView.NO_POSITION || isLastItem) {
                return null
            }
            val childView = layoutManager.findViewByPosition(firstChildPosition)
            return if (helper.getDecoratedEnd(childView) >= helper.getDecoratedMeasurement(childView)/2
                    && helper.getDecoratedEnd(childView) >0) {
                childView
            } else {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                    null
                } else {
                    layoutManager.findViewByPosition(firstChildPosition + 1)
                }
            }
        }
        return super.findSnapView(layoutManager)
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager) : OrientationHelper {
        if (!::mVerticalHelper.isInitialized) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return mVerticalHelper
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager) : OrientationHelper {
        if (!::mHorizontalHelper.isInitialized) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper
    }
}