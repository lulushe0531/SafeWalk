package com.example.dom.newwestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Dom on 2017-11-27.
 */

public class Dao {
    private static final String TAG = Dao.class.getSimpleName();
    protected static final String WHERE = " = ?";

    // count total number of the object
    private static int count = 0;
    // current table name
    protected final String tableName;
    protected DatabaseHelper dbHelper;

    protected Dao(Context context, String tableName) {
        this.tableName = tableName;
        if (dbHelper == null) {
            dbHelper = DatabaseHelper.getInstance(context);
        }
        count++;
    }

    /**
     * Insert data to the table.
     *
     * @param values - data of the record.
     * @return success - true if insert successfully.
     */
    protected boolean insert(ContentValues values) {
        boolean success = false;
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            long result = sqLiteDatabase.insert(tableName, null, values);
            sqLiteDatabase.close();
            Log.d(TAG, "Insert " + result + " row");
            success = result > 0;
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return success;
    }

    /**
     * Update data from the table.
     *
     * @param columnName - name of the column.
     * @param args - arguments.
     * @param values - data of the record.
     * @return success - true if update successfully.
     */
    protected boolean update(String columnName, String[] args, ContentValues values) {
        boolean success = false;
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            long result = sqLiteDatabase.update(tableName, values, columnName + WHERE, args);
            sqLiteDatabase.close();
            Log.d(TAG, "Update " + result + " row");
            success = result == 1;
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return success;
    }

    /**
     * Delete data from the table.
     *
     * @param columnName - name of the column.
     * @param args - arguments.
     * @return success - true if delete successfully.
     */
    protected boolean delete(String columnName, String[] args) {
        boolean success = false;
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            int result = sqLiteDatabase.delete(tableName, columnName + WHERE, args);
            sqLiteDatabase.close();
            Log.d(TAG, "Delete " + result + " row");
            success = result > 0;
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return success;
    }

    /**
     * Close SQLite connection.
     */
    public void close() {
        try {
            if (dbHelper != null) {
                dbHelper.close();
                dbHelper = null;
                Log.d(TAG, "Close SQLite connection");
                count--;
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static int getCount() {
        return count;
    }
}