package com.mysocial.hackfest.classes;

import java.io.Serializable;

public class LostNFound implements Serializable {

    private String id ;
    private String admissionNo ;
    private String email ;
    private String name ;
    private String title ;
    private String message ;
    private String imgUrl = "image" ;
    private String status ;
    private String claimedName ;
    private String claimedAdmiNo ;
    private String claimedEmail ;

    public LostNFound()
    {

    }

    public String getClaimedName() {
        return claimedName;
    }

    public void setClaimedName(String claimedName) {
        this.claimedName = claimedName;
    }

    public String getClaimedAdmiNo() {
        return claimedAdmiNo;
    }

    public void setClaimedAdmiNo(String claimedAdmiNo) {
        this.claimedAdmiNo = claimedAdmiNo;
    }

    public String getClaimedEmail() {
        return claimedEmail;
    }

    public void setClaimedEmail(String claimedEmail) {
        this.claimedEmail = claimedEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
