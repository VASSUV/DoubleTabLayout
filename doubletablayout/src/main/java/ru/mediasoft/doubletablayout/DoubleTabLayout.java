package ru.mediasoft.doubletablayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import ru.mediasoft.doubletablayout.adapter.DoubleTabAdapter;

public class DoubleTabLayout extends RecyclerView {
    private SnappingLinearLayoutManager layoutManager;
    private GestureDetector gestureDetector;
    private boolean enableScrolling = true;

    public DoubleTabLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DoubleTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DoubleTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setAdapter(@Nullable DoubleTabAdapter adapter) {
        super.setAdapter(adapter);
    }

    private void init(Context context) {
        layoutManager = new SnappingLinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        super.setLayoutManager(layoutManager);
        addOnScrollListener(getScrollListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Nullable
    @Override
    public SnappingLinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            View itemView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition());
            View parentView = itemView.findViewById(R.id.parent_container);
            enableScrolling = parentView.getHeight() <= e.getY();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            enableScrolling = true;
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (enableScrolling) return false;

            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
//                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (diffY > 0) {
//                        onSwipeBottom();
//                    } else {
//                        onSwipeTop();
//                    }
//                    result = true;
//                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeLeft() {
        int lastVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int count = layoutManager.getItemCount();
        smoothScrollToPosition((lastVisibleItemPosition == count - 1) ? lastVisibleItemPosition : (lastVisibleItemPosition + 1));
    }

    public void onSwipeRight() {
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        smoothScrollToPosition(firstVisibleItem == 0 ? 0 : (firstVisibleItem - 1));
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return !enableScrolling || super.onTouchEvent(ev);
    }

    @Override
    public void setLayoutManager(@Nullable LayoutManager layout) {
        Log.e("DoubleTabRecyclerView", "Set up LayoutManager is not supported");
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        if (adapter instanceof DoubleTabAdapter)
            super.setAdapter(adapter);
        else
            Log.e("DoubleTabRecyclerView", "A standard adapter is not supported. Use DoubleTabAdapter");
    }

    @NonNull
    private OnScrollListener getScrollListener() {
        return new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                correctFirstVisibleItemPosition();
            }
        };
    }

    public void correctFirstVisibleItemPosition() {
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        // int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        View view = layoutManager.findViewByPosition(firstVisibleItem);

        if (getAdapter() instanceof DoubleTabAdapter) {
            layoutManager.correctVisibilityItem(view);
        }
    }

}
