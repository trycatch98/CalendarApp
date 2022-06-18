package com.learn.adnroid.calendarapp;

public class Detail {
    String id;
    String title;
    int year;
    int month;
    int day;
    int startHour;
    int endHour;
    String address;
    String note;
    int position;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartTime(String startTime) {
        String[] start = startTime.split(" ");
        year = Integer.parseInt(start[0]);
        month = Integer.parseInt(start[1]);
        day = Integer.parseInt(start[2]);
        startHour = Integer.parseInt(start[3]);
    }

    public void setEndTime(String endTime) {
        String[] end = endTime.split(" ");
        endHour = Integer.parseInt(end[3]);
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
