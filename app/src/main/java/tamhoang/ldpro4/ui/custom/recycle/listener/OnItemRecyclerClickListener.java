package tamhoang.ldpro4.ui.custom.recycle.listener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author : Pos365
 * @Skype : chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 07/03/2018.
 */
/**
 * Dung de xac dinh su kien onClick va onLongClick cua item trong recycler view
 * Co the dung chung voi {@link OnSwipeTouchListener}
 * */
public class OnItemRecyclerClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;

    private GestureDetector mGestureDetector;

    public OnItemRecyclerClickListener(Context context, final RecyclerView recyclerView,
                                       OnItemClickListener listener) {
        mListener = listener;

        mGestureDetector
                = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mListener != null) {
                    mListener.onLongItemClick(recyclerView, recyclerView.getChildAdapterPosition(child), child);
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {
        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(recyclerView, recyclerView.getChildAdapterPosition(childView), childView);
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener {
        public void onItemClick(RecyclerView recyclerView, int position, View view);

        public void onLongItemClick(RecyclerView recyclerView, int position, View view);
    }
}
