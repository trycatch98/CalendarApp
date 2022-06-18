package com.learn.adnroid.calendarapp;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthViewFragment extends Fragment {
    private MonthCalendarAdapter adapter;
    private int year = -1, month = -1, day = -1, maxDay = -1, startDayOfWeek = -1;
    private String now;
    private int viewHeight = -1;
    private DBHelper mDbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        Calendar calendar = Calendar.getInstance();
        Bundle args = getArguments();
        if (args != null) {
            this.year = args.getInt("YEAR");
            this.month = args.getInt("MONTH");
            this.day = args.getInt("DAY");
            this.now = args.getString("NOW");
            this.viewHeight = args.getInt("VIEW_HEIGHT");
        }

        calendar.set(this.year, this.month, 1);
        this.maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        this.startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        initView(view);
        drawCalendar();

        int nowYear = Integer.parseInt(now.split(" ")[0]);
        int nowMonth = Integer.parseInt(now.split(" ")[1]);
        int nowDay = Integer.parseInt(now.split(" ")[2]);

        if (year == nowYear && month == nowMonth)
            adapter.setSelectPosition(nowDay + startDayOfWeek - 2);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DetailViewActivity.class);
            intent.putExtra("YEAR", year);
            intent.putExtra("MONTH", month);
            intent.putExtra("DAY", day);
            startActivity(intent);
        });

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
                detail.position = detail.day + startDayOfWeek - 2;
                details.add(detail);
            }
        }

        adapter.setDetail(details);
    }

    private void initView(View view) {
        GridView calendarView = view.findViewById(R.id.calendar);
        this.adapter = new MonthCalendarAdapter();

        adapter.setItemHeight(viewHeight / 6);

        //특정한 날짜를 선택 시에, 해당 날짜의 연/월/일을 표시하는 Toast 메시지를 출력한다.
        this.adapter.setOnItemClickListener((item, details) -> {
            if (details.isEmpty()) {
                this.day = Integer.parseInt(item);
                Toast.makeText(getActivity(), year + "." + (month + 1) + "." + item, Toast.LENGTH_SHORT).show();
            }
            else if (details.size() == 1) {
                startDetail(details.get(0));
            }
            else {
                String[] detailArr = new String[details.size()];
                int size = 0;
                for(Detail temp : details){
                    detailArr[size++] = temp.title;
                }
                new AlertDialog.Builder(getContext())
                        .setTitle(year + "." + (month + 1) + "." + item + "일")
                        .setItems(detailArr, (DialogInterface.OnClickListener) (dialogInterface, i) -> {
                            Detail detail = details.get(i);
                            startDetail(detail);
                        })
                        .show();
            }
        });
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

    public void startDetail(Detail detail) {
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
}
