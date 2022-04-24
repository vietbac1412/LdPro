package tamhoang.ldpro4.ui.custom.recycle

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import tamhoang.ldpro4.R
import tamhoang.ldpro4.ui.custom.recycle.item.SpacesItemDecoration
import tamhoang.ldpro4.ui.custom.recycle.listener.StartSnapHelper
import tamhoang.ldpro4.ui.custom.recycle.loadmore.EndlessRecyclerViewScrollListener
import tamhoang.ldpro4.ui.custom.recycle.loadmore.OnEndlessScrolling

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 26/02/2018.
 */
class BaseRecycleView : RecyclerView, RecycleContract.View {

    companion object {
        const val TYPE_LINEAR_LAYOUT         = 0xa
        const val TYPE_GRID_LAYOUT           = 0xb
        const val TYPE_STAGGERED_GRID_LAYOUT = 0xc
        const val TYPE_FLEXBOX_LAYOUT = 0xd
    }

    private var mContext: Context
    private var mTypeLayout = TYPE_LINEAR_LAYOUT

    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mGridLayoutManager: GridLayoutManager
    private lateinit var mStaggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var mFlexboxLayoutManager: FlexboxLayoutManager
    private var mMaxHeight: Int = 0

    private lateinit var mItemDecoration: RecyclerView.ItemDecoration

    private var mEndlessScrollListener: EndlessRecyclerViewScrollListener? = null
    var onEndlessScrolling: OnEndlessScrolling? = null
        set(value) {
            field = value

            when (mTypeLayout) {
                TYPE_LINEAR_LAYOUT -> mEndlessScrollListener = object : EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        actionLoadMore(page, totalItemsCount, view)
                    }
                }
                TYPE_GRID_LAYOUT -> mEndlessScrollListener = object : EndlessRecyclerViewScrollListener(mGridLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        actionLoadMore(page, totalItemsCount, view)
                    }
                }
                TYPE_STAGGERED_GRID_LAYOUT -> mEndlessScrollListener = object : EndlessRecyclerViewScrollListener(mStaggeredGridLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        actionLoadMore(page, totalItemsCount, view)
                    }
                }
                TYPE_FLEXBOX_LAYOUT -> mEndlessScrollListener = object : EndlessRecyclerViewScrollListener(mFlexboxLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        actionLoadMore(page, totalItemsCount, view)
                    }
                }
            }

            mEndlessScrollListener?.let { addOnScrollListener(it) }
        }

    private var mItemWidth: Int = 0
    private var mSpanCount: Int = 0

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        mContext = context
        initialize(context, attrs)
    }

    init {
        itemAnimator = DefaultItemAnimator()
        setHasFixedSize(true)
    }

    private fun initialize(context: Context, attrs: AttributeSet) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.CustomMaxHeightScrollView)
        mMaxHeight = arr.getLayoutDimension(R.styleable.CustomMaxHeightScrollView_maxHeight, mMaxHeight)
        arr.recycle()
    }


    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var heightMeasureSpec = heightSpec
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthSpec, heightMeasureSpec)

        layoutManager?.let { updateSpanCountWhenRuntime(it, mTypeLayout, mSpanCount, mItemWidth) }
    }

    override fun initLayoutManager(typeLayout: Int, space: Int, spanCount: Int, isHorizontal: Boolean, isReverse: Boolean) {
        mTypeLayout = typeLayout
        when (mTypeLayout) {
            TYPE_LINEAR_LAYOUT -> {
                mLinearLayoutManager = LinearLayoutManager(
                        mContext,
                        if (isHorizontal) {
                            LinearLayoutManager.HORIZONTAL
                        } else {
                            LinearLayoutManager.VERTICAL
                        },
                        isReverse
                )

                mItemDecoration = SpacesItemDecoration
                        .SpacesItemDecorationBuilder(space)
                        .setLinearLayoutType(
                                if (isHorizontal) {
                                    DividerItemDecoration.HORIZONTAL
                                } else {
                                    DividerItemDecoration.VERTICAL
                                }
                        ).build()

                layoutManager = mLinearLayoutManager
            }
            TYPE_GRID_LAYOUT -> {
                if (spanCount > 0) {
                    mGridLayoutManager = GridLayoutManager(
                            mContext,
                            spanCount,
                            if (isHorizontal) {
                                GridLayoutManager.HORIZONTAL
                            } else {
                                GridLayoutManager.VERTICAL
                            },
                            isReverse
                    )

                    mItemDecoration = SpacesItemDecoration
                            .SpacesItemDecorationBuilder(space)
                            .setSpanCount(spanCount)
                            .build()
                    setMargin(space)

                    layoutManager = mGridLayoutManager
                }
            }
            TYPE_STAGGERED_GRID_LAYOUT -> {
                if (spanCount > 0) {
                    mStaggeredGridLayoutManager = StaggeredGridLayoutManager(spanCount, if (isHorizontal) StaggeredGridLayoutManager.HORIZONTAL else StaggeredGridLayoutManager.VERTICAL)

                    mItemDecoration = SpacesItemDecoration
                            .SpacesItemDecorationBuilder(space)
                            .setSpanCount(spanCount)
                            .build()
                    setMargin(space)

                    layoutManager = mStaggeredGridLayoutManager
                }
            }
            else -> {
                initLinearLayoutManager()
            }
        }

        addItemDecoration(mItemDecoration)
    }

    override fun initLinearLayoutManager(space: Int, isHorizontal: Boolean, isReverse: Boolean) {
        initLayoutManager(TYPE_LINEAR_LAYOUT, space, isHorizontal = isHorizontal, isReverse = isReverse)
    }

    override fun initGridLayoutManager(space: Int, spanCount: Int, itemWidth: Int,
                                       isHorizontal: Boolean, isReverse: Boolean) {
        mItemWidth = itemWidth
        mSpanCount = spanCount
        initLayoutManager(TYPE_GRID_LAYOUT, space, spanCount, isHorizontal, isReverse)
    }

    override fun initStaggeredGridLayoutManager(space: Int, spanCount: Int, itemWidth: Int,
                                                isHorizontal: Boolean, isReverse: Boolean) {
        mItemWidth = itemWidth
        mSpanCount = spanCount
        initLayoutManager(TYPE_STAGGERED_GRID_LAYOUT, space, spanCount, isHorizontal, isReverse)
    }

    override fun initFlexboxLayoutManager(isHorizontal: Boolean, isReverse: Boolean, flexWrap: Int, justifyContent: Int) {
        mTypeLayout = TYPE_FLEXBOX_LAYOUT
        val mFlexDirection = if (isHorizontal)
            if (isReverse) FlexDirection.ROW_REVERSE else FlexDirection.ROW
        else
            if (isReverse) FlexDirection.COLUMN_REVERSE else FlexDirection.COLUMN

        mFlexboxLayoutManager = FlexboxLayoutManager(mContext, mFlexDirection, flexWrap)

        layoutManager = mFlexboxLayoutManager
        (layoutManager as FlexboxLayoutManager).flexDirection = mFlexDirection
        (layoutManager as FlexboxLayoutManager).flexWrap = flexWrap
        (layoutManager as FlexboxLayoutManager).justifyContent = justifyContent

    }

    override fun updateSpanCountWhenRuntime(layoutManager: LayoutManager, typeLayout: Int,
                                            spanCount: Int, itemWidth: Int) {
        if (itemWidth > 0) {
            val spanCount = Math.max(Math.min(spanCount, measuredWidth / itemWidth), 1)
            when (typeLayout) {
                TYPE_GRID_LAYOUT -> {
                    (layoutManager as GridLayoutManager).spanCount = spanCount
                    if (adapter is RecycleContract.GridLayout) {
                        (adapter as RecycleContract.GridLayout).handlerSpanCount(this, spanCount)
                    }
                }
                TYPE_STAGGERED_GRID_LAYOUT -> {
                    (layoutManager as StaggeredGridLayoutManager).spanCount = spanCount
                }
            }
        }
    }

    override fun setLinearLayoutSnapHelper(typeGravity: Int, isPager: Boolean) {
        mLinearLayoutManager.let {
            when (typeGravity) {
                Gravity.CENTER -> {
                    val snapHelper = LinearSnapHelper()
                    onFlingListener = null
                    snapHelper.attachToRecyclerView(this)
                }
                else ->
                    if (isPager) {
                        //val gravityPagerSnapHelper = GravityPagerSnapHelper(typeGravity)
                        onFlingListener = null
                        //gravityPagerSnapHelper.attachToRecyclerView(this)
                    }
                    else {
                        //val gravitySnapHelper = GravitySnapHelper(typeGravity)
                        onFlingListener = null
                        //gravitySnapHelper.attachToRecyclerView(this)
                    }
            }
        }
    }

    override fun resetEndlessScrolling() : Boolean {
        if (mEndlessScrollListener != null) {
            mEndlessScrollListener?.resetState()
            return true
        }
        return false
    }

    private fun actionLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
        view.post {
            onEndlessScrolling?.loadNextPage(page, totalItemsCount, view)
        }
    }

    override fun setMargin(value: Int) {
        val marginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
        marginLayoutParams.setMargins(value, value, value, value)
        layoutParams = marginLayoutParams
    }

    override fun attachStartSnapHelper(startSnapHelper: StartSnapHelper) {
        startSnapHelper.attachToRecyclerView(this)
    }

    override fun removeItemDecor() {
        removeItemDecoration(mItemDecoration)
    }

}