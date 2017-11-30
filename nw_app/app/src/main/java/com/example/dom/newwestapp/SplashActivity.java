package com.example.dom.newwestapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final String URL = "http://opendata.newwestcity.ca/downloads/intersections/INTERSECTIONS.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new JSONReader().execute();
    }

    private class JSONReader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                pullIntersectionData();

            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        private void pullIntersectionData() throws JSONException {
            HttpHandler http = new HttpHandler();
            String jsonStr = http.makeServiceCall(URL);

            if (jsonStr == null) {
                return;
            }
            IntersectionDao intsecDao = new IntersectionDao(SplashActivity.this);
            JSONArray intsecJSONArray = new JSONArray(jsonStr);
            for (int i = 0; i < intsecJSONArray.length(); i++) {
                JSONObject jsonObject = intsecJSONArray.getJSONObject(i);
                Intersection intsec = new Intersection(
                        jsonObject.getInt("OBJECTID"),
                        jsonObject.getString("Location"),
                        jsonObject.getDouble("X"),
                        jsonObject.getDouble("Y"));
                intsecDao.insertOrUpdate(intsec);
            }

            intsecDao.close();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SplashActivity.this.finish();
        }
    }
}
