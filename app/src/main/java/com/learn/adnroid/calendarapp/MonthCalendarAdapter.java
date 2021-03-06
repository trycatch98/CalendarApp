package com.learn.adnroid.calendarapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MonthCalendarAdapter extends BaseAdapter {
    // 달력 정보에 관한 배열 선언
    private ArrayList<String> items = new ArrayList<>();
    // 이벤트리스터 참조변수 선언
    private OnItemClickListener onItemClickListener = null;
    private int height = 0;
    private int selectPosition = -1;
    private ArrayList<Detail> details = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        ViewHolder holder;
        if (view == null) {
            //LayoutInflater 클래스를 이용하여 일부분만을 차지하는 화면의 구성요소들을 XML 레이아웃으로부터 로딩하여 화면에 보여줄 수 있게한다.
            //해당 객체를 LAYOUT_INFLATER_SERVICE라는 시스템 호출 함수를 호출하여 사용할 수 있도록 요청한다.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //item_day View 컴포넌트, 부분 레이아웃파일을 인플레이션(Inflation)하여 뷰그룹으로부터 메인레이아웃의 파일 내용으로 적용할 수 있도록 도와준다.
            view = inflater.inflate(R.layout.item_month, viewGroup, false);
            holder = new ViewHolder();
            holder.parent = view.findViewById(R.id.parent);
            holder.day = view.findViewById(R.id.day);
            holder.detail1 = view.findViewById(R.id.detail1);
            holder.detail2 = view.findViewById(R.id.detail2);
            if (this.height != 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, this.height));

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }
        holder.details = new ArrayList<>();

        //토요일(=7%7=0)으로 파란색상을 부여하고 일요일(=8%7=1)으로 빨간색상을 부여하고 그 이외는 검은색상으로 부여하여 달력의 시각화를 통일감있게 표현한다.
        String item = items.get(i);
        int color = R.color.black;

        switch ((i + 1) % 7) {
            case 0:
                color = R.color.blue_700;
                break;
            case 1:
                color = R.color.red_700;
                break;
        }
        // 정보(텍스트 및 컬러)를 수정한 뒤, 이벤트리스너를 넣은 뒤 반환한다.
        holder.day.setText(item);
        holder.day.setTextColor(view.getResources().getColor(color));
        if (i == selectPosition) {
            holder.parent.setBackgroundColor(Color.CYAN);
            holder.parent.setPadding(10, 10, 10, 10);
        }
        else {
            holder.parent.setBackgroundColor(Color.WHITE);
            holder.parent.setPadding(0, 0, 0, 0);
        }

        holder.detail1.setBackgroundColor(Color.WHITE);
        holder.detail1.setText("");
        holder.detail2.setBackgroundColor(Color.WHITE);
        holder.detail2.setText("");

        for (Detail d : details) {
            if (i == d.position) {
                if (holder.detail1.getText() != "" && holder.detail1.getText() != d.title) {
                    holder.detail2.setBackgroundColor(Color.CYAN);
                    holder.detail2.setText(d.title);
                }
                else {
                    holder.detail1.setBackgroundColor(Color.GREEN);
                    holder.detail1.setText(d.title);
                }
                holder.details.add(d);
            }
        }

        if (item.isEmpty())
            holder.parent.setOnClickListener(view1 -> {});
        else if (onItemClickListener != null)
            holder.parent.setOnClickListener(view1 -> {
                onItemClickListener.onItemClick(item, holder.details);
                selectPosition = i;
                notifyDataSetChanged();
            });

        return view;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setItemHeight(int height) {
        this.height = height;
        notifyDataSetChanged();
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    public void setDetail(ArrayList<Detail> details) {
        this.details = details;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // onItemClick(String item) 추상화 및 인터페이스
    interface OnItemClickListener {
        void onItemClick(String item, List<Detail> details);
    }

    class ViewHolder {
        LinearLayout parent;
        TextView day;
        TextView detail1;
        TextView detail2;
        ArrayList<Detail> details;
    }
}

