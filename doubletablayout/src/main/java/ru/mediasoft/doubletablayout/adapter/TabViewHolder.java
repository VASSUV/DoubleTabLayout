package ru.mediasoft.doubletablayout.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import ru.mediasoft.doubletablayout.DoubleTabLayout;
import ru.mediasoft.doubletablayout.R;

public class TabViewHolder extends RecyclerView.ViewHolder {
    FrameLayout parentContainer;
    LinearLayout childsContainer;
    public DoubleTabLayout parentRecycler;

    TabViewHolder(@NonNull View rootView) {
        super(rootView);
        parentContainer = rootView.findViewById(R.id.parent_container);
        childsContainer = rootView.findViewById(R.id.childs_container);
    }
}
