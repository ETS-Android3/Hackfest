package com.mysocial.hackfest.classes;

public class Club {
    private String club_image_url;
    private String club_name;
    private String club_details1;
    private String year_of_foundation;
    private String sac_room;
    private String youtube;
    private String facebook;
    private String instagram;

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }


    public Club() {

    }
    public String getClub_image_url() {
        return club_image_url;
    }

    public void setClub_image_url(String club_image_url) {
        this.club_image_url = club_image_url;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public String getClub_details1() {
        return club_details1;
    }

    public void setClub_details1(String club_details1) {
        this.club_details1 = club_details1;
    }

    public Club(String club_image_url, String club_name, String club_details1, String year_of_foundation, String sac_room, String youtube, String facebook,String instagram) {
        this.club_image_url = club_image_url;
        this.club_name = club_name;
        this.club_details1 = club_details1;
        this.year_of_foundation = year_of_foundation;
        this.sac_room = sac_room;
        this.youtube = youtube;
        this.facebook = facebook;
        this.instagram=instagram;
    }

    public String getYear_of_foundation() {
        return year_of_foundation;
    }

    public void setYear_of_foundation(String year_of_foundation) {
        this.year_of_foundation = year_of_foundation;
    }

    public String getSac_room() {
        return sac_room;
    }

    public void setSac_room(String sac_room) {
        this.sac_room = sac_room;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}
