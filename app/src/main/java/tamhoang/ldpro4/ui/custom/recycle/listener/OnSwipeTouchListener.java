package tamhoang.ldpro4.ui.custom.recycle.listener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import tamhoang.ldpro4.ui.custom.recycle.listener.OnItemRecyclerClickListener.OnItemClickListener;

/**
 * @author : Pos365
 * @Skype : chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 02/03/2018.
 */
/**
 * Dung cho view bao gom ca recycler view (khong phai item trong recycler)
 * Dung ket hop duoc voi {@link OnItemClickListener}
 * Khong dung duoc voi {@link ItemRecyclerClickSupport} vi bi conflict su kien.
 * */
public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private Boolean isHorizontal = false;

    public OnSwipeTouchListener(Context context, Boolean isHorizontal) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.isHorizontal = isHorizontal;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                if (e1 != null && e2 != null) {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = isHorizontal;
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeTop();
                        } else {
                            onSwipeBottom();
                        }
                        result = !isHorizontal;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    public void onClick() {
    }

    public void onDoubleClick() {
    }

    public void onLongClick() {
    }
}
