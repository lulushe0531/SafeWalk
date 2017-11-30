package com.example.dom.newwestapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Dom on 2017-11-27.
 */

public class Intersection {

    private int id;
    private int objectId;
    private String intersectionName;
    private double coordX;
    private double coordY;
    private String rating;
    private String comments;

    public Intersection(int id, String name, double x, double y) {
        this.id = 0;
        this.objectId = id;
        this.intersectionName = name;
        this.coordX = x;
        this.coordY = y;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getIntersectionName() {
        return intersectionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIntersectionName(String intersectionName) {

        this.intersectionName = intersectionName;
    }

    public double getCoordX() {
        return coordX;
    }

    public void setCoordX(double coordX) {
        this.coordX = coordX;
    }

    public double getCoordY() {
        return coordY;
    }

    public void setCoordY(double coordY) {
        this.coordY = coordY;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
