package com.learn.adnroid.calendarapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Calendar;

public class PagerAdapter extends FragmentStateAdapter {
    private int year = -1, month = -1;
    private final Calendar calendar = Calendar.getInstance();
    public final int MID_POSITION = Integer.MAX_VALUE / 2;

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        this.calendar.set(year, month + position - MID_POSITION, 1);
        return new MonthViewFragment(year, this.calendar.get(Calendar.MONTH));
    }

    public int getCurrentYear(int position) {
        this.calendar.set(year, month + position - MID_POSITION, 1);
        return this.calendar.get(Calendar.YEAR);
    }

    public int getCurrentMonth(int position) {
        this.calendar.set(year, month + position - MID_POSITION, 1);
        return this.calendar.get(Calendar.MONTH);
    }

    @Override
    public int getItemCount() {
        // Integer Max(2147483647)
        return Integer.MAX_VALUE;
    }
}
