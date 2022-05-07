package com.mysocial.hackfest.classes;

import java.io.Serializable;

public class Data implements Serializable {
    private String category = "hostel";
    private String complaintMessage ="";
    private String complaintPicUrl ="";
    private String complaintStatus = "Unseen";
    private String complaintTitle ="";
    private Boolean publicPrivate = false;
    private String rollNo = "";
    private Boolean seen = false;
    private String studentName = "Anonymous";
    private String studentProfilePicUrl = "";
    private int upVotes = 0;
    private String comment="Nothing here";
    private String email = "";

    public Data ()
    {

    }

    public Data(String category, String complaintMessage, String complaintPicUrl, String complaintStatus, String complaintTitle, Boolean publicPrivate, String rollNo, Boolean seen, String studentName, String studentProfilePicUrl, int upVotes, String comment, String email) {
        this.category = category;
        this.complaintMessage = complaintMessage;
        this.complaintPicUrl = complaintPicUrl;
        this.complaintStatus = complaintStatus;
        this.complaintTitle = complaintTitle;
        this.publicPrivate = publicPrivate;
        this.rollNo = rollNo;
        this.seen = seen;
        this.studentName = studentName;
        this.studentProfilePicUrl = studentProfilePicUrl;
        this.upVotes = upVotes;
        this.comment = comment;
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComplaintMessage() {
        return complaintMessage;
    }

    public void setComplaintMessage(String complaintMessage) {
        this.complaintMessage = complaintMessage;
    }

    public String getComplaintPicUrl() {
        return complaintPicUrl;
    }

    public void setComplaintPicUrl(String complaintPicUrl) {
        this.complaintPicUrl = complaintPicUrl;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public Boolean getPublicPrivate() {
        return publicPrivate;
    }

    public void setPublicPrivate(Boolean publicPrivate) {
        this.publicPrivate = publicPrivate;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentProfilePicUrl() {
        return studentProfilePicUrl;
    }

    public void setStudentProfilePicUrl(String studentProfilePicUrl) {
        this.studentProfilePicUrl = studentProfilePicUrl;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
