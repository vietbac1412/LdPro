package tamhoang.ldpro4.ui.custom.recycle.item

import android.graphics.Rect
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 19/03/2018.
 */

/**
 * Xac dinh khoang cach cho cac item trong RecyclerView.
 *
 * Decorator which adds spacing around the tiles in a Grid layout RecyclerView. Apply to a RecyclerView with:
 * SpacesItemDecoration decoration = new SpacesItemDecoration(16);
 * mRecyclerView.addItemDecoration(decoration);
 * <p>
 * Feel free to add any value you wish for SpacesItemDecoration. That value determines the amount of spacing.
 * Source: http://blog.grafixartist.com/pinterest-masonry-layout-staggered-grid/
 *
 * https://github.com/jaychang0917/SimpleRecyclerView/blob/master/library/src/main/java/com/jaychang/srv/decoration/LinearSpacingItemDecoration.java
 * https://github.com/cymcsg/UltimateRecyclerView/blob/master/UltimateRecyclerView/ultimaterecyclerview/src/main/java/com/marshalchen/ultimaterecyclerview/grid/GridSpacingItemDecoration.java
 * https://gist.github.com/liangzhitao/e57df3c3232ee446d464
 */
class SpacesItemDecoration(builder: SpacesItemDecorationBuilder) : RecyclerView.ItemDecoration() {
    private var mSpace: Int            = 0
    private var mSpanCount: Int        = 0
    private var mLinearLayoutType: Int = 0

    init {
        mSpace = builder.space
        mSpanCount = builder.spanCount
        mLinearLayoutType = builder.linearLayoutType
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (mSpace > 0) {
            if (mSpanCount > 0) {
                outRect.left   = mSpace
                outRect.right  = mSpace
                outRect.bottom = mSpace
                outRect.top    = mSpace
            } else {
                if (mLinearLayoutType == LinearLayoutManager.HORIZONTAL) {
                    outRect.right = mSpace
                } else if (mLinearLayoutType == LinearLayoutManager.VERTICAL) {
                    outRect.bottom = mSpace
                }
            }
        }
    }

    class SpacesItemDecorationBuilder(val space: Int) {
        var spanCount        = -1
        var linearLayoutType = -1

        fun setSpanCount(spanCount: Int): SpacesItemDecorationBuilder {
            this.spanCount = spanCount
            return this
        }

        fun setLinearLayoutType(linearLayoutType: Int): SpacesItemDecorationBuilder {
            this.linearLayoutType = linearLayoutType
            return this
        }

        fun build(): SpacesItemDecoration {
            return SpacesItemDecoration(this)
        }
    }
}