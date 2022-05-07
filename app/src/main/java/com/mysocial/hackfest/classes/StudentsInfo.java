package com.mysocial.hackfest.classes;

import java.io.Serializable;

public class StudentsInfo implements Serializable {
    private String name;
    private String admNo;
    private String profilePicUri = null;
    private String email;
    private String hostel;

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public StudentsInfo(){

    }

    public StudentsInfo(String name, String admNo, String profilePicUri, String email,String hostel) {
        this.name = name;
        this.hostel=hostel;
        this.admNo = admNo;
        this.profilePicUri = profilePicUri;
        this.email = email;
    }

    public String getName() {
        return name;
    }   

    public void setName(String name) {
        this.name = name;
    }

    public String getAdmNo() {
        return admNo;
    }

    public void setAdmNo(String admNo) {
        this.admNo = admNo;
    }

    public String getProfilePicUri() {
        return profilePicUri;
    }

    public void setProfilePicUri(String profilePicUri) {
        this.profilePicUri = profilePicUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
