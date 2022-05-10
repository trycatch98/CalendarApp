package com.learn.adnroid.calendarapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class WeekViewFragment extends Fragment {
    private WeekCalendarAdapter adapter, dayOfWeekAdapter, timeAdapter;
    private int year = -1;
    private int month = -1;
    private int day = -1;
    private int maxDay = -1;
    private int startDayOfWeek = -1;
    private int viewWidth = -1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        Calendar calendar = Calendar.getInstance();
        Bundle args = getArguments();
        if (args != null) {
            this.year = args.getInt("YEAR");
            this.month = args.getInt("MONTH");
            this.day = args.getInt("DAY");
            this.viewWidth = args.getInt("VIEW_WIDTH");
        }
        calendar.set(this.year, this.month, this.day);
        this.maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        this.startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        initView(view);
        drawCalendar();
        return view;
    }

    private void initView(View view) {
        GridView dayOfWeekView = view.findViewById(R.id.day_of_week);
        GridView calendarView = view.findViewById(R.id.calendar);
        GridView timeView = view.findViewById(R.id.time);

        this.adapter = new WeekCalendarAdapter();
        this.dayOfWeekAdapter = new WeekCalendarAdapter();
        this.timeAdapter = new WeekCalendarAdapter();

        adapter.setItemHeight(viewWidth / 7);
        dayOfWeekAdapter.setItemHeight(viewWidth / 7);
        timeAdapter.setItemHeight(viewWidth / 7);

        timeView.setAdapter(timeAdapter);
        dayOfWeekView.setAdapter(dayOfWeekAdapter);
        this.dayOfWeekAdapter.setOnItemClickListener(position -> {});


        //특정한 날짜를 선택 시에, 해당 날짜의 연/월/일을 표시하는 Toast 메시지를 출력한다.
        this.adapter.setOnItemClickListener(position -> Toast.makeText(getActivity(), "position = " + position, Toast.LENGTH_SHORT).show());
        calendarView.setAdapter(adapter);
    }

    private void drawCalendar() {
        ArrayList<String> list = new ArrayList<>();

        int start = day - startDayOfWeek + 1;
        int end = start + 7;

        for (int i = start; i < end; i++) {
            if (i > maxDay)
                list.add((i - maxDay) + "");
            else
                list.add(i + "");
        }

        this.dayOfWeekAdapter.setItems(list);

        list = new ArrayList<>();
        for (int i = 0; i < 24 * 7; i++) {
            list.add("");
        }
        this.adapter.setItems(list);

        list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list.add(i + "");
        }
        this.timeAdapter.setItems(list);
    }
}
