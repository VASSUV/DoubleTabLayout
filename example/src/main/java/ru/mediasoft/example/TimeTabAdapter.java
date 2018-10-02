package ru.mediasoft.example;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.mediasoft.doubletablayout.SnappingLinearLayoutManager;
import ru.mediasoft.doubletablayout.adapter.DoubleTabAdapter;
import ru.mediasoft.doubletablayout.adapter.ViewHolder;

import static java.util.Calendar.*;

public class TimeTabAdapter extends DoubleTabAdapter<TimeTabAdapter.DayViewHolder, TimeTabAdapter.HourViewHolder> {
    TimeTabAdapter(SnappingLinearLayoutManager layoutManager) {
        super(layoutManager);
    }

    @Override
    protected DayViewHolder onCreateParentHolder(FrameLayout parentContainer, int position) {
        return new DayViewHolder(LayoutInflater.from(parentContainer.getContext()).inflate(R.layout.item_day, parentContainer, false));
    }

    @Override
    protected HourViewHolder onCreateChildHolder(LinearLayout childsContainer, int positionParent, int positionInParent) {
        return new HourViewHolder(LayoutInflater.from(childsContainer.getContext()).inflate(R.layout.item_hour, childsContainer, false));
    }

    @Override
    protected void onBindParentHolder(DayViewHolder parentHolder, int position) {
        parentHolder.dayView.setText(position == 0 ? "Сегодня" : getDate(position));
    }

    private String getDate(int position) {
        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(DAY_OF_MONTH, position);
        return String.format("%02d.%02d", calendar.get(DAY_OF_MONTH), calendar.get(MONTH));
    }

    @Override
    protected void onBindChildHolder(HourViewHolder childHolder, int positionParent, int positionInParent) {
        childHolder.hourView.setText(String.format("%02d:00", positionInParent));
    }

    @Override
    protected int getChildItemCount(int parentPosition) {
        return 24;
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class DayViewHolder extends ViewHolder {
        TextView dayView;

        DayViewHolder(@NonNull View rootView) {
            super(rootView);
            dayView = rootView.findViewById(R.id.text_view);
        }
    }

    class HourViewHolder extends ViewHolder {
        TextView hourView;

        HourViewHolder(@NonNull View rootView) {
            super(rootView);
            hourView = rootView.findViewById(R.id.text_view);
        }
    }

}
