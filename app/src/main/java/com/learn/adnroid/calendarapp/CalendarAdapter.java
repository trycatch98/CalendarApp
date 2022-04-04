package com.learn.adnroid.calendarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayHolder> {
    private ArrayList<String> items;

    @NonNull
    @Override
    public DayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_day, parent, false);
        return new DayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayHolder holder, int position) {
        int color = R.color.black;;
        switch ((position + 1) % 7) {
            case 0:
                color = R.color.blue_700;
                break;
            case 1:
                color = R.color.red_700;
                break;
        }
        holder.bind(items.get(position), color);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    class DayHolder extends RecyclerView.ViewHolder {
        TextView day;
        public DayHolder(@NonNull View itemView) {
            super(itemView);
            this.day = itemView.findViewById(R.id.day);
        }

        public void bind(String day, int color) {
            this.day.setText(day);
            this.day.setTextColor(itemView.getResources().getColor(color));
        }
    }
}
