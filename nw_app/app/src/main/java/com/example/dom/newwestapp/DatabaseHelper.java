package com.example.dom.newwestapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dom on 2017-11-27.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NewWest.db";
    // Create helper instance
    private static DatabaseHelper instance;

    /**
     * DatabaseHelper constructor
     *
     * @param context - current context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Start database instance.
     *
     * @param context - current context.
     * @return databaseHelper - current instance.
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    /**
     * Create tables in SQLite database
     *
     * @param sqLiteDatabase - SQLiteDatabase object
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "Create SQLite database version " + DATABASE_VERSION);
        try {
            sqLiteDatabase.execSQL(iIntersection.CREATE_INTSEC_TABLE);
        } catch (SQLiteException e) {
            Log.e(TAG, "Cannot create table - " + e.getMessage());
        }
    }

    /**
     * Upgrade tables in SQLite database
     *
     * @param sqLiteDatabase - SQLiteDatabase object
     * @param oldVersion     - current version in the device
     * @param newVersion     - updated version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            Log.i(TAG, "Upgrade SQLite database from version " + oldVersion + " to version " + newVersion);
            try {
                sqLiteDatabase.execSQL(iIntersection.DROP_INTSEC_TABLE);
                this.onCreate(sqLiteDatabase);
            } catch (SQLiteException e) {
                Log.e(TAG, "Cannot upgrade table - " + e.getMessage());
            }
        }
    }
}
