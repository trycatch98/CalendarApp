package com.learn.adnroid.calendarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthViewActivity extends AppCompatActivity {
    private CalendarAdapter adapter;
    private Calendar calendar;
    private int year, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        Intent intent = getIntent();

        this.calendar = Calendar.getInstance();

        this.year = intent.getIntExtra("CALENDAR_YEAR", this.calendar.get(Calendar.YEAR));
        this.month = intent.getIntExtra("CALENDAR_MONTH", this.calendar.get(Calendar.MONTH));

        this.calendar.set(year, month, 1);

        initView();
        drawCalendar();
    }

    private void initView() {
        TextView currentMonth = findViewById(R.id.month);
        currentMonth.setText(this.year + "년 " + (this.month + 1) + "월");

        ImageView prev = findViewById(R.id.prev);
        ImageView next = findViewById(R.id.next);

        prev.setOnClickListener(view -> {
            clickPrev();
        });

        next.setOnClickListener(view -> {
            clickNext();
        });

        GridView calendarView = findViewById(R.id.calendar);

        this.adapter = new CalendarAdapter();
        this.adapter.setOnItemClickListener(item -> {
            Toast.makeText(this, year + "." + (month + 1) + "." + item, Toast.LENGTH_SHORT).show();
        });
        calendarView.setAdapter(this.adapter);
    }

    private void drawCalendar() {
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < startDayOfWeek; i++) {
            list.add("");
        }
        for (int i = 1; i <= maxDay; i++) {
            list.add(i + "");
        }

        this.adapter.setItems(list);
    }

    private void clickPrev() {
        this.calendar.set(this.year, this.month - 1, 1);
        callCalendar();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
    }

    private void clickNext() {
        this.calendar.set(this.year, this.month + 1, 1);
        callCalendar();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
    }

    private void callCalendar() {
        Intent intent = new Intent(this, MonthViewActivity.class);
        intent.putExtra("CALENDAR_YEAR", this.calendar.get(Calendar.YEAR));
        intent.putExtra("CALENDAR_MONTH", this.calendar.get(Calendar.MONTH));
        startActivity(intent);
    }
}