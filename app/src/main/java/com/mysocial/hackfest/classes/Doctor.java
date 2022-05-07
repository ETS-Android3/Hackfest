package com.mysocial.hackfest.classes;

public class Doctor {
    private String name;
    private String speciality;
    private String visitingTime;
    private String currentStatus;
    private String imageUrl;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public Doctor() {

    }

    public Doctor(String name, String speciality, String visitingTime, String currentStatus, String imageUrl,String date) {
        this.name = name;
        this.date=date;
        this.speciality = speciality;
        this.visitingTime = visitingTime;
        this.currentStatus = currentStatus;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getVisitingTime() {
        return visitingTime;
    }

    public void setVisitingTime(String visitingTime) {
        this.visitingTime = visitingTime;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
