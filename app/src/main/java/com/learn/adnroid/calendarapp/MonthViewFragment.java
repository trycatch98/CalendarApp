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

public class MonthViewFragment extends Fragment {
    private MonthCalendarAdapter adapter;
    private int year = -1, month = -1, maxDay = -1, startDayOfWeek = -1;
    private int viewHeight = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        Calendar calendar = Calendar.getInstance();
        Bundle args = getArguments();
        if (args != null) {
            this.year = args.getInt("YEAR");
            this.month = args.getInt("MONTH");
            this.viewHeight = args.getInt("VIEW_HEIGHT");
        }

        calendar.set(this.year, this.month, 1);
        this.maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        this.startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        initView(view);
        drawCalendar();
        return view;
    }

    private void initView(View view) {
        GridView calendarView = view.findViewById(R.id.calendar);
        this.adapter = new MonthCalendarAdapter();

        adapter.setItemHeight(viewHeight / 6);

        //특정한 날짜를 선택 시에, 해당 날짜의 연/월/일을 표시하는 Toast 메시지를 출력한다.
        this.adapter.setOnItemClickListener(item -> Toast.makeText(getActivity(), year + "." + (month + 1) + "." + item, Toast.LENGTH_SHORT).show());
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
