package com.mysocial.hackfest.classes;

public class MapData {
    private  String name;
    private double lat,lng;

    public MapData(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
