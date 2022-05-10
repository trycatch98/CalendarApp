package com.learn.adnroid.calendarapp;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

public class CalendarViewActivity extends AppCompatActivity {
    private LinearLayout dayOfWeek;
    private PagerAdapter currentAdapter, monthAdapter, weekAdapter;
    private ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        pager = findViewById(R.id.pager);
        dayOfWeek = findViewById(R.id.day_of_week);
        monthAdapter = new PagerAdapter(this);
        weekAdapter = new PagerAdapter(this);
        weekAdapter.setViewType(PagerAdapter.ViewType.VIEW_TYPE_WEEK);
        currentAdapter = monthAdapter;

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int year = currentAdapter.getCurrentYear(position);
                int month = currentAdapter.getCurrentMonth(position);
                setTitle(year + "년 " + (month + 1) + "월");
            }
        });

        pager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pager.setAdapter(monthAdapter);
                monthAdapter.setViewWidth(pager.getWidth());
                monthAdapter.setViewHeight(pager.getHeight());
                weekAdapter.setViewWidth(pager.getWidth());
                weekAdapter.setViewHeight(pager.getHeight());
                pager.setCurrentItem(monthAdapter.MID_POSITION, false);
                pager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) dayOfWeek.getLayoutParams();

        switch (item.getItemId()) {
            case R.id.month:
                pager.setAdapter(monthAdapter);
                params.leftMargin = 0;
                currentAdapter = monthAdapter;
                break;
            case R.id.week:
                pager.setAdapter(weekAdapter);
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                params.leftMargin = (int) (20 * metrics.density);
                currentAdapter = weekAdapter;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        pager.setCurrentItem(weekAdapter.MID_POSITION, false);
        return true;
    }
}