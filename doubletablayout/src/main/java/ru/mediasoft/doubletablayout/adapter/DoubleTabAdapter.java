package ru.mediasoft.doubletablayout.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import ru.mediasoft.doubletablayout.DoubleTabLayout;
import ru.mediasoft.doubletablayout.R;
import ru.mediasoft.doubletablayout.SnappingLinearLayoutManager;

public abstract class DoubleTabAdapter
        <PVH extends ViewHolder, CVH extends ViewHolder>
        extends RecyclerView.Adapter<TabViewHolder> {

    private SnappingLinearLayoutManager layoutManager;

    public DoubleTabAdapter(SnappingLinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    protected abstract PVH onCreateParentHolder(FrameLayout parentContainer, int position);

    protected abstract CVH onCreateChildHolder(LinearLayout childsContainer, int positionParent, int positionInParent);

    protected abstract void onBindParentHolder(PVH parentHolder, int position);

    protected abstract void onBindChildHolder(CVH childHolder, int positionParent, int positionInParent);

    protected abstract int getChildItemCount(int parentPosition);

    @NonNull
    @Override
    public TabViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        TabViewHolder tabViewHolder = new TabViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_double_tab, viewGroup, false));
        tabViewHolder.parentRecycler = (DoubleTabLayout)viewGroup;
        return tabViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TabViewHolder tabViewHolder, int position) {
        if (tabViewHolder instanceof TabViewHolder) {
            final TabViewHolder viewHolder = (TabViewHolder) tabViewHolder;
            bindParent(viewHolder.parentContainer, position);
            bindChilds(viewHolder.childsContainer, position);

            changeWidth(viewHolder.parentContainer, viewHolder.childsContainer, viewHolder.parentContainer.getChildAt(0));
            changeWidth(viewHolder.childsContainer, viewHolder.parentContainer, viewHolder.childsContainer);

            layoutManager.correctVisibilityItem(viewHolder.itemView);
        }
    }

    private void changeWidth(final ViewGroup container, final ViewGroup secondContainer, final View viewForEquals) {
        if (viewForEquals != null) {
            viewForEquals.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams lp = container.getLayoutParams();
                    lp.width = viewForEquals.getWidth() <= secondContainer.getWidth() ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
                    container.setLayoutParams(lp);
                }
            });
        }
    }

    private void bindParent(FrameLayout parentContainer, int position) {
        PVH parentHolder = null;
        View parentView = parentContainer.getChildAt(0);
        if (parentView == null) {
            parentHolder = onCreateParentHolder(parentContainer, position);
            parentView = parentHolder.itemView;
            parentView.setTag(parentHolder);
            parentContainer.addView(parentView);
        } else if (!(parentView.getTag() instanceof ViewHolder)) {
            parentContainer.removeAllViews();
            parentHolder = onCreateParentHolder(parentContainer, position);
            parentView = parentHolder.itemView;
            parentView.setTag(parentHolder);
            parentContainer.addView(parentView);
        }
        if (parentHolder == null)
            parentHolder = (PVH) parentView.getTag();

        onBindParentHolder(parentHolder, position);

    }

    private void bindChilds(LinearLayout childsContainer, int position) {
        final int childCount = getChildItemCount(position);
        View childView;
        CVH childHolder;
        for (int i = 0; i < childCount; i++) {
            childView = childsContainer.getChildAt(i);
            childHolder = null;
            if (childView == null) {
                childHolder = onCreateChildHolder(childsContainer, position, i);
                childView = childHolder.itemView;
                childView.setTag(childHolder);
                childsContainer.addView(childView);
            } else if (!(childView.getTag() instanceof ViewHolder)) {
                childsContainer.removeView(childView);
                childHolder = onCreateChildHolder(childsContainer, position, i);
                childView = childHolder.itemView;
                childView.setTag(childHolder);
                childsContainer.addView(childView, i);
            }
            if (childHolder == null)
                childHolder = (CVH) childView.getTag();
            onBindChildHolder(childHolder, position, i);
        }

        if (childsContainer.getChildCount() > childCount) {
            childsContainer.removeViews(childCount, childsContainer.getChildCount() - childCount);
        }
    }

}