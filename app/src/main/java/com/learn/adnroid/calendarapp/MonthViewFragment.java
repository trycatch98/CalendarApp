package com.learn.adnroid.calendarapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthViewFragment extends Fragment {
    private CalendarAdapter adapter;
    private Calendar calendar;
    private int maxDay = -1, startDayOfWeek = -1;

    public MonthViewFragment(int year, int month) {
        this.calendar = Calendar.getInstance();

        this.calendar.set(year, month, 1);
        this.maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        this.startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        this.calendar = Calendar.getInstance();
        initView(view);
        drawCalendar();
        return view;
    }

    private void initView(View view) {
        GridView calendarView = view.findViewById(R.id.calendar);
        this.adapter = new CalendarAdapter();
        calendarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                adapter.setItemHeight(calendarView.getHeight() / 6);
                calendarView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //특정한 날짜를 선택 시에, 해당 날짜의 연/월/일을 표시하는 Toast 메시지를 출력한다.
//        this.adapter.setOnItemClickListener(item -> Toast.makeText(this, year + "." + (month + 1) + "." + item, Toast.LENGTH_SHORT).show());
        calendarView.setAdapter(this.adapter);

    }

    private void drawCalendar() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < startDayOfWeek; i++) {
            list.add("");
        }
        for (int i = 1; i <= maxDay; i++) {
            list.add(i + "");
        }
        for (int i = startDayOfWeek + maxDay; i <= 6 * 7; i++) {
            list.add("");
        }
        this.adapter.setItems(list);
    }
}
