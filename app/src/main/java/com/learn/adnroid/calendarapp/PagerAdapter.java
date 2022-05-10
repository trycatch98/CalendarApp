package com.learn.adnroid.calendarapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Calendar;

public class PagerAdapter extends FragmentStateAdapter {
    private int year = -1, month = -1, day = -1;
    private final Calendar calendar = Calendar.getInstance();
    public final int MID_POSITION = Integer.MAX_VALUE / 2;
    private int viewWidth = -1, viewHeight = -1;
    private ViewType viewType = ViewType.VIEW_TYPE_MONTH;

    enum ViewType {
        VIEW_TYPE_MONTH, VIEW_TYPE_WEEK;
    }

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DATE);
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (viewType) {
            case VIEW_TYPE_MONTH:
                fragment = new MonthViewFragment();
                this.calendar.set(year, month + position - MID_POSITION, day);
                break;
            case VIEW_TYPE_WEEK:
                fragment = new WeekViewFragment();
                this.calendar.set(year, month, day + ((position - MID_POSITION) * 7));
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("YEAR", this.calendar.get(Calendar.YEAR));
        bundle.putInt("MONTH", this.calendar.get(Calendar.MONTH));
        bundle.putInt("DAY", this.calendar.get(Calendar.DATE));

        bundle.putInt("VIEW_HEIGHT", viewHeight);
        bundle.putInt("VIEW_WIDTH", viewWidth);

        fragment.setArguments(bundle);
        return fragment;
    }

    public int getCurrentYear(int position) {
        switch (viewType) {
            case VIEW_TYPE_MONTH:
                this.calendar.set(year, month + position - MID_POSITION, day);
                break;
            case VIEW_TYPE_WEEK:
                this.calendar.set(year, month, day + ((position - MID_POSITION) * 7));
                break;
        }
        return this.calendar.get(Calendar.YEAR);
    }

    public int getCurrentMonth(int position) {
        switch (viewType) {
            case VIEW_TYPE_MONTH:
                this.calendar.set(year, month + position - MID_POSITION, day);
                break;
            case VIEW_TYPE_WEEK:
                this.calendar.set(year, month, day + ((position - MID_POSITION) * 7));
                break;
        }
        return this.calendar.get(Calendar.MONTH);
    }

    @Override
    public int getItemCount() {
        // Integer Max(2147483647)
        return Integer.MAX_VALUE;
    }
}
