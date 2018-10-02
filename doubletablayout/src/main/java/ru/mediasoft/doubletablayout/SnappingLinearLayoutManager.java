package ru.mediasoft.doubletablayout;


import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

public class SnappingLinearLayoutManager extends LinearLayoutManager {

    public SnappingLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private class TopSnappedSmoothScroller extends LinearSmoothScroller {
        public TopSnappedSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return SnappingLinearLayoutManager.this
                    .computeScrollVectorForPosition(targetPosition);
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }

        @Override
        protected int getHorizontalSnapPreference() {
            return SNAP_TO_START;
        }
    }

    public void correctVisibilityItem(View view) {
        int firstVisibleItem = findFirstVisibleItemPosition();
        int lastVisibleItem = findLastVisibleItemPosition();

        if (view != null) leftViewLayout(view);

        for (int i = firstVisibleItem + 1; i <= lastVisibleItem; i++) {
            view = findViewByPosition(i);

            if (view != null) rightViewLayout(view);
        }
    }

    private void rightViewLayout(View view) {
        final FrameLayout frameLayout = view.findViewById(R.id.parent_container);
        final View tab = frameLayout.getChildAt(0);
        tab.setX(0);
    }

    private void leftViewLayout(View view) {
        final FrameLayout frameLayout = view.findViewById(R.id.parent_container);
        final View tab = frameLayout.getChildAt(0);
        final int tabWidth = tab.getWidth();
        final int width = view.getWidth();
//                final int rvWidth = DoubleTabRecyclerView.this.getWidth();
        final float x = view.getX();
        if (x < 0)
            tab.setX((width + x < tabWidth) ? (width - tabWidth) : -x);
//                    tab.setX((width + x < rvWidth - tabWidth / 2) ? (width - rvWidth + tabWidth / 2) : -x);
        else
            tab.setX(0);
    }
}