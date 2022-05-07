package com.mysocial.hackfest.classes;

import java.io.Serializable;

public class MessMenu implements Serializable {
    private String breakfast,lunch,dinner,date,snacks,day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public MessMenu(String breakfast, String lunch, String dinner, String date, String snacks) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.date = date;
        this.snacks=snacks;
        this.day=null;
    }
    public MessMenu(String breakfast, String lunch, String dinner, String date, String snacks,String day) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.date = date;
        this.snacks=snacks;
        this.day=day;
    }

    public String getSnacks() {
        return snacks;
    }

    public void setSnacks(String snacks) {
        this.snacks = snacks;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
