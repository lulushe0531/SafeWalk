package com.example.dom.newwestapp;

/**
 * Created by Dom on 2017-11-27.
 */

public interface iIntersection {
    // place table name
    String INTSEC_TABLE_NAME = "Intersection";

    // place column names
    String INTSEC_ID = "ID";
    String INTSEC_OBJECT_ID = "objectId";
    String INTSEC_LOCATION = "name";
    String INTSEC_COORDINATE_X = "coordinateX";
    String INTSEC_COORDINATE_Y = "coordinateY";
    String INTSEC_RATING = "rating";
    String INTSEC_COMMENT = "comment";

    // create place table
    String CREATE_INTSEC_TABLE = "CREATE TABLE " + INTSEC_TABLE_NAME + "("
            + INTSEC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + INTSEC_OBJECT_ID + " INTEGER, "
            + INTSEC_LOCATION + " TEXT, "
            + INTSEC_COORDINATE_X + " REAL, "
            + INTSEC_COORDINATE_Y + " REAL, "
            + INTSEC_RATING + " TEXT, "
            + INTSEC_COMMENT + " TEXT);";

    // drop place table
    String DROP_INTSEC_TABLE = "DROP IF EXISTS " + INTSEC_TABLE_NAME;
}