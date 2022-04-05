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
    //캘린더어댑터에 대한 참조변수
    private CalendarAdapter adapter;
    //달력 추상클래스에 대한 참조변수
    private Calendar calendar;
    //달력을 출력하기 위한 연월에 대한 변수
    private int year = -1, month = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        //이것을 사용해 앱의 다른 컴포넌트를 시작시키거나, 다른 앱들에게 메시지를 전달할 수 있습니다.
        Intent intent = getIntent();
        //Calendar 클래스를 활용하여 현재 날짜 정보를 얻어와서, 이를 바탕으로, 현재 년도의 현재 월 달력을 표시한다.
        this.calendar = Calendar.getInstance();
        //인텐트를 통해 Calendar 객체로부터 데이터를 전달받는다.
        this.year = intent.getIntExtra("CALENDAR_YEAR", this.calendar.get(Calendar.YEAR));
        this.month = intent.getIntExtra("CALENDAR_MONTH", this.calendar.get(Calendar.MONTH));
        //Calendar 객체로부터 전달받은 연월을 통해 해당 달의 첫번째 요일을 설정한다.
        this.calendar.set(year, month, 1);
        initView();
        //인텐트를 통해 전달받은 메세지를 바탕으로 GridView 형태로 달력을 출력한다
        drawCalendar();
    }

    private void initView() {
        TextView currentMonth = findViewById(R.id.month);
        //해당 컴포넌트의 출력 텍스트를 수정한다.
        currentMonth.setText(this.year + "년 " + (this.month + 1) + "월");

        ImageView prev = findViewById(R.id.prev);
        ImageView next = findViewById(R.id.next);
        //이전 버튼 클릭 시에 현재 표시된 달력의 연/월 에서
        //이전 월에 해당하는 연/월 정보를 파라미터로 전달하여 MonthViewActivity 를 새로이 시작시킨다.
        //이때, 현재화면에 표시된 액티비티는 종료시킨다.
        prev.setOnClickListener(view -> clickPrev());
        //다음 버튼 클릭 시에 현재 표시된 달력의 연/월 에서
        //이후 월에 해당하는 연/월 정보를 파라미터로 전달하여 MonthViewActivity 를 새로이 시작시킨다.
        //이때, 현재화면에 표시된 액티비티는 종료시킨다.
        next.setOnClickListener(view -> clickNext());

        GridView calendarView = findViewById(R.id.calendar);
        this.adapter = new CalendarAdapter();
        //특정한 날짜를 선택 시에, 해당 날짜의 연/월/일을 표시하는 Toast 메시지를 출력한다.
        this.adapter.setOnItemClickListener(item -> Toast.makeText(this, year + "." + (month + 1) + "." + item, Toast.LENGTH_SHORT).show());
        calendarView.setAdapter(this.adapter);
    }

    private void drawCalendar() {
        //getActualMaximum(Calendar.DAY_OF_MONTH) : 해당 월의 전체일수를 저장하여 maxDay 변수에 저장
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //Calendar.DAY_OF_WEEK : 입력한 달의 첫번째 날을 1일을 구한 뒤 startDayOfWeek 변수에 저장
        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //해당 달력의 첫번째 날에 해당하는 요일과 마지막 날에 해당하는 요일에 대한 반복문을 통한 구현
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < startDayOfWeek; i++) {
            list.add("");
        }
        for (int i = 1; i <= maxDay; i++) {
            list.add(i + "");
        }
        this.adapter.setItems(list);
    }

    //overridePendingTransition(R.anim.none, R.anim.horizon_exit)으로 화면전환 간 애니매이션 효과를 부여한다. -1달
    private void clickPrev() {
        this.calendar.set(this.year, this.month - 1, 1);
        callCalendar();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
    }

    //overridePendingTransition(R.anim.none, R.anim.horizon_exit)으로 화면전환 간 애니매이션 효과를 부여한다. +1달
    private void clickNext() {
        this.calendar.set(this.year, this.month + 1, 1);
        callCalendar();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
    }

    //MAP(N:V) : Extras를 활용하여 이름과 값의 쌍으로된 정보를 전달
    private void callCalendar() {
        Intent intent = new Intent(this, MonthViewActivity.class);
        intent.putExtra("CALENDAR_YEAR", this.calendar.get(Calendar.YEAR));
        intent.putExtra("CALENDAR_MONTH", this.calendar.get(Calendar.MONTH));
        startActivity(intent);
    }
}