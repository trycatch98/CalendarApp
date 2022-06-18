package com.learn.adnroid.calendarapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class WeekViewFragment extends Fragment {
    private WeekCalendarAdapter adapter, dayOfWeekAdapter, timeAdapter;
    private int year = -1;
    private int month = -1;
    private int day = -1;
    private int hour = -1;
    private int maxDay = -1;
    private int startDayOfWeek = -1;
    private int viewWidth = -1;
    private String now;
    ArrayList<String> dayList = new ArrayList<>();
    private DBHelper mDbHelper;


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
            this.now = args.getString("NOW");
            this.viewWidth = args.getInt("VIEW_WIDTH");
        }
        calendar.set(this.year, this.month, this.day);
        this.maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        this.startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        initView(view);
        drawCalendar();

        int nowYear = Integer.parseInt(now.split(" ")[0]);
        int nowMonth = Integer.parseInt(now.split(" ")[1]);
        int nowDay = Integer.parseInt(now.split(" ")[2]);
        if (year == nowYear && month == nowMonth && nowDay == day) {
            for (int i = 0; i < dayList.size(); i++) {
                if (day == Integer.parseInt(dayList.get(i))) {
                    this.dayOfWeekAdapter.setSelectPosition(i);
                    int hour = calendar.get(Calendar.HOUR);
                    adapter.setSelectPosition((hour * 7) + i);
                }
            }

        }
        mDbHelper = new DBHelper(getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Cursor cursor = mDbHelper.getAllUsersByMethod();
        ArrayList<Detail> details = new ArrayList<>();
        while (cursor.moveToNext()) {
            Detail detail = new Detail();
            detail.setId(cursor.getString(0));
            detail.setTitle(cursor.getString(1));
            detail.setStartTime(cursor.getString(2));
            detail.setEndTime(cursor.getString(3));
            detail.setAddress(cursor.getString(4));
            detail.setNote(cursor.getString(5));
            if (year == detail.year && month == detail.month) {
                for (int i = 0; i < dayList.size(); i++) {
                    if (detail.day == Integer.parseInt(dayList.get(i))) {
                        detail.position = (detail.startHour * 7) + i;
                        details.add(detail);
                    }
                }
            }
        }

        adapter.setDetail(details);
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
        this.dayOfWeekAdapter.setOnItemClickListener((position, detail) -> {});


        //특정한 날짜를 선택 시에, 해당 날짜의 연/월/일을 표시하는 Toast 메시지를 출력한다.
        this.adapter.setOnItemClickListener((position, detail) -> {
            if (detail == null) {
                Toast.makeText(getActivity(), "position = " + position, Toast.LENGTH_SHORT).show();
                this.day = Integer.parseInt(dayList.get(position % 7));
                this.hour = position / 7;
                this.dayOfWeekAdapter.setSelectPosition(position % 7);
            }
            else {
                Intent intent = new Intent(getContext(), DetailViewActivity.class);
                intent.putExtra("ID", detail.id);
                intent.putExtra("YEAR", detail.year);
                intent.putExtra("MONTH", detail.month);
                intent.putExtra("DAY", detail.day);
                intent.putExtra("HOUR", detail.startHour);
                intent.putExtra("END_HOUR", detail.endHour);
                intent.putExtra("TITLE", detail.title);
                intent.putExtra("ADDRESS", detail.address);
                intent.putExtra("NOTE", detail.note);

                startActivity(intent);
            }
        });
        calendarView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DetailViewActivity.class);
            intent.putExtra("YEAR", year);
            intent.putExtra("MONTH", month);
            intent.putExtra("DAY", day);
            intent.putExtra("HOUR", hour);
            startActivity(intent);
        });
    }

    private void drawCalendar() {
        int start = day - startDayOfWeek + 1;
        int end = start + 7;

        for (int i = start; i < end; i++) {
            if (i > maxDay)
                dayList.add((i - maxDay) + "");
            else if (i < 1) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, day);
                dayList.add(calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + i + "");
            }
            else
                dayList.add(i + "");
        }

        this.dayOfWeekAdapter.setItems(dayList);

        ArrayList<String> list;

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
