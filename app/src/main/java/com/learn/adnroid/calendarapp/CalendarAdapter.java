package com.learn.adnroid.calendarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CalendarAdapter extends BaseAdapter {
// 달력 정보에 관한 배열 선언
    private ArrayList<String> items = new ArrayList<>();
// 이벤트리스터 참조변수 선언
    private OnItemClickListener onItemClickListener = null;

    @Override
    public int getCount() {
        return items.size();
    }
//특정 위치의 항목을 반환하기 위해 mThubIds 배열의 지정된 위치의 항목을 반환한다.
    @Override
    public Object getItem(int i) {
        return items.get(i);
    }
//특정위치의 항목 아이디를 반한하는 것인데, 여기서는 배열의 첨자를 항목의 아이디로 간주한다.
    @Override
    public long getItemId(int i) {
        return i;
    }
//getView 메소드는 첫번째 파라미터로 주어진 위치의 항목 뷰를 반환한다.
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//Context에 대한 정보를 불러온다.
        Context context = viewGroup.getContext();
        if (view == null) {
//LayoutInflater 클래스를 이용하여 일부분만을 차지하는 화면의 구성요소들을 XML 레이아웃으로부터 로딩하여 화면에 보여줄 수 있게한다.
//해당 객체를 LAYOUT_INFLATER_SERVICE라는 시스템 호출 함수를 호출하여 사용할 수 있도록 요청한다.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//item_day View 컴포넌트, 부분 레이아웃파일을 인플레이션(Inflation)하여 뷰그룹으로부터 메인레이아웃의 파일 내용으로 적용할 수 있도록 도와준다.
            view = inflater.inflate(R.layout.item_day, viewGroup, false);
        }
//토요일(=7%7=0)으로 파란색상을 부여하고 일요일(=8%7=1)으로 빨간색상을 부여하고 그 이외는 검은색상으로 부여하여 달력의 시각화를 통일감있게 표현한다.
        TextView day = view.findViewById(R.id.day);
        String item = items.get(i);
        int color = R.color.black;;
        switch ((i + 1) % 7) {
            case 0:
                color = R.color.blue;
                break;
            case 1:
                color = R.color.red;
                break;
        }
 // 정보(텍스트 및 컬러)를 수정한 뒤, 이벤트리스너를 넣은 뒤 반환한다.
        day.setText(item);
        day.setTextColor(view.getResources().getColor(color));
        day.setOnClickListener(view1 -> onItemClickListener.onItemClick(item));
        return view;
    }
//특정 위치의 항목을 반환하기 위해 mThubIds 배열의 지정된 위치의 항목을 반환한다.
    public void setItems(ArrayList<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }
// 항목 클릭 이벤트 처리 구현
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
// onItemClick(String item) 추상화 및 인터페이스
    interface OnItemClickListener {
        void onItemClick(String item);
    }
}
