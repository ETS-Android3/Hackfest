package com.mysocial.hackfest.classes;

import java.io.Serializable;

public class Updates implements Serializable {
    private String Date;
    private String content;
    private String title;
    private Boolean isNotice;
    private String clubName;
    private Boolean ismEvent;
    private String imageUrl;
    private String pdfUrl;
    private String fbLink,instaLink,otherLink,webLink;

    public Updates(String date, String content, String title, Boolean isNotice, String clubName, Boolean ismEvent, String imageUrl, String pdfUrl, String fbLink, String instaLink, String otherLink, String webLink) {
        Date = date;
        this.content = content;
        this.title = title;
        this.isNotice = isNotice;
        this.clubName = clubName;
        this.ismEvent = ismEvent;
        this.imageUrl = imageUrl;
        this.pdfUrl = pdfUrl;
        this.fbLink = fbLink;
        this.instaLink = instaLink;
        this.otherLink = otherLink;
        this.webLink = webLink;
    }

    public Boolean getIsmEvent() {
        return ismEvent;
    }

    public void setIsmEvent(Boolean ismEvent) {
        this.ismEvent = ismEvent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getInstaLink() {
        return instaLink;
    }

    public void setInstaLink(String instaLink) {
        this.instaLink = instaLink;
    }

    public String getOtherLink() {
        return otherLink;
    }

    public void setOtherLink(String otherLink) {
        this.otherLink = otherLink;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }



    public Updates(String date, String content, String title, Boolean isNotice, String clubName, String imageUrl, String pdfUrl, String fbLink, String instaLink, String otherLink, String webLink) {
        Date = date;
        this.content = content;
        this.title = title;
        this.isNotice = isNotice;
        this.clubName = clubName;
        this.imageUrl = imageUrl;
        this.pdfUrl = pdfUrl;
        this.fbLink = fbLink;
        this.instaLink = instaLink;
        this.otherLink = otherLink;
        this.webLink = webLink;
    }


 
    public Updates()
    {

    }

    public Boolean getNotice() {
        return isNotice;
    }

    public void setNotice(Boolean notice) {
        isNotice = notice;
    }
}
