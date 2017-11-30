package com.example.dom.newwestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dom on 2017-11-27.
 */

public class IntersectionDao extends Dao {
    private static final String TAG = IntersectionDao.class.getSimpleName();

    public IntersectionDao(Context context) {
        super(context, iIntersection.INTSEC_TABLE_NAME);
    }

    /**
     * Insert a place
     *
     * @param intsec - intersection object
     */
    public void insert(Intersection intsec) {
        ContentValues values = new ContentValues();
        values.put(iIntersection.INTSEC_OBJECT_ID, intsec.getObjectId());
        values.put(iIntersection.INTSEC_LOCATION, intsec.getIntersectionName());
        values.put(iIntersection.INTSEC_COORDINATE_X, intsec.getCoordX());
        values.put(iIntersection.INTSEC_COORDINATE_Y, intsec.getCoordY());
        Log.i(TAG, "Inserting " + intsec.getObjectId());
        super.insert(values);
    }

    /**
     * Update a place
     *
     * @param intsec - intersection object
     */
    public void update(Intersection intsec) {
        ContentValues values = new ContentValues();
        String[] args = {String.valueOf(intsec.getObjectId())};
        Log.i(TAG, "Updating " + intsec.getObjectId());
        super.update(iIntersection.INTSEC_ID, args, values);
    }

    /**
     * Delete a place
     *
     * @param intsecID - intersection ID
     */
    public void delete(int intsecID) {
        String[] args = {String.valueOf(intsecID)};
        Log.i(TAG, "Deleting placeId " + intsecID);
        super.delete(iIntersection.INTSEC_ID, args);
    }

    /**
     * Insert or update a place
     *
     * @param intsec - intersection
     */
    public void insertOrUpdate(Intersection intsec) {
        ContentValues values = new ContentValues();
        values.put(iIntersection.INTSEC_OBJECT_ID, intsec.getObjectId());
        values.put(iIntersection.INTSEC_LOCATION, intsec.getIntersectionName());
        values.put(iIntersection.INTSEC_COORDINATE_X, intsec.getCoordX());
        values.put(iIntersection.INTSEC_COORDINATE_Y, intsec.getCoordY());
        String[] args = {String.valueOf(intsec.getObjectId())};
        boolean success = super.update(iIntersection.INTSEC_OBJECT_ID, args, values);
        if (!success) {
            values.put(iIntersection.INTSEC_OBJECT_ID, intsec.getObjectId());
            super.insert(values);
        }
    }

    public Intersection findIntersectionById(int id) {
        Intersection intersection = null;
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT * FROM " + iIntersection.INTSEC_TABLE_NAME
                    + " WHERE " + iIntersection.INTSEC_ID + " = '" + id + "';", null);
            if (cursor.moveToFirst()) {
                intersection = new Intersection(cursor.getInt(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4));
                intersection.setId(cursor.getInt(0));
                intersection.setRating(cursor.getString(5));
                intersection.setComments(cursor.getString(6));
            }
            cursor.close();
            sqLiteDatabase.close();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return intersection;
    }

    public void updateRatingById(int id, String rating, String comment) {
        ContentValues values = new ContentValues();
        values.put(iIntersection.INTSEC_RATING, rating);
        values.put(iIntersection.INTSEC_COMMENT, comment);
        String[] args = {String.valueOf(id)};
        super.update(iIntersection.INTSEC_ID, args, values);
    }

    public List<Intersection> findAllIntsec() {
        List<Intersection> intersections = null;
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT * FROM " + iIntersection.INTSEC_TABLE_NAME, null);
            int count = cursor.getCount();
            Log.d(TAG, "Found events " + count + " row");
            if (count > 0 && cursor.moveToFirst()) {
                intersections = new ArrayList<>(count);
                do {
                    Intersection intersection = new Intersection(cursor.getInt(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4));
                    intersection.setId(cursor.getInt(0));
                    intersections.add(intersection);
                } while (cursor.moveToNext());
            }
            cursor.close();
            sqLiteDatabase.close();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return intersections;
    }
}