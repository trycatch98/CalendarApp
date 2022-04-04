package com.learn.adnroid.calendarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CalendarAdapter extends BaseAdapter {
    private ArrayList<String> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

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
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_day, viewGroup, false);
        }

        TextView day = view.findViewById(R.id.day);
        String item = items.get(i);
        int color = R.color.black;;
        switch ((i + 1) % 7) {
            case 0:
                color = R.color.blue_700;
                break;
            case 1:
                color = R.color.red_700;
                break;
        }
        day.setText(item);
        day.setTextColor(view.getResources().getColor(color));
        day.setOnClickListener(view1 -> onItemClickListener.onItemClick(item));
        return view;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void onItemClick(String item);
    }
}
