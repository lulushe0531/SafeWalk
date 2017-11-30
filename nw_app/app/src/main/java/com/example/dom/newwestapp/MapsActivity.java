package com.example.dom.newwestapp;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import static com.example.dom.newwestapp.R.id.bottom;
import static com.example.dom.newwestapp.R.id.map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    protected UiSettings mUiSettings;
    private final static int ZOOM_MAGNIFIER = 15;
    private final static LatLng NEW_WEST = new LatLng(49.205766, -122.911331);
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 4;
    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final int REQUEST_PHONE_CALL = 3;
    protected boolean mLocationPermissionDenied = false;
    protected boolean isStreetSelected = false;
    protected int markerId;
    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+17788350531"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        startActivity(new Intent(this, SplashActivity.class));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_msg:
                                FragmentManager fm1 = getFragmentManager();
                                OptionSendMsgFragment dialogFragment1 = new OptionSendMsgFragment();
                                dialogFragment1.show(fm1, "Sample Fragment");
                                //sendLocationSMS();
                                return true;
                            case R.id.menu_call:
                                FragmentManager fm2 = getFragmentManager();
                                OptionCallFragment dialogFragment2 = new OptionCallFragment();
                                dialogFragment2.show(fm2, "Sample Fragment");
                                //emergencyCall();
                                return true;
                            case R.id.menu_rate:
                                rateLocation(isStreetSelected);
                                return true;
                        }
                        return true;
                    }
                });
    }

    /**
     * Requests the fine location permission. If a rationale with an additional explanation should
     * be shown to the user, displays a dialog that triggers the request.
     */
    public void requestLocationPermission(int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Display a dialog with rationale.
            PermissionUtils.RationaleDialog
                    .newInstance(requestCode, false).show(
                    getSupportFragmentManager(), "dialog");
        } else {
            // Location permission has not been granted yet, request it.
            PermissionUtils.requestPermission(this, requestCode,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_PERMISSION_REQUEST_CODE) {
            // Enable the My Location button if the permission has been granted.
            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Check permission
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                mLocationPermissionDenied = true;
                mMap.setMyLocationEnabled(false);
            }
        } else if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0, 0, 0, 200);
        requestLocationPermission(MY_LOCATION_PERMISSION_REQUEST_CODE);

        // Add a marker in NW and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NEW_WEST, ZOOM_MAGNIFIER));

        LoadStyledMap();
        addFireHallPolygons();
        addHospitalPolygons();
        addPolicePolygon();
        addIntersections();

        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker != null) {
                    isStreetSelected = true;
                    markerId = Integer.parseInt(marker.getTag().toString());
                    System.out.println(markerId);
                    IntersectionDao dao = new IntersectionDao(MapsActivity.this);
                    Intersection intsec = dao.findIntersectionById(markerId);
                    if (intsec.getRating() != null && intsec.getComments() != null) {
                        marker.setSnippet("Rating: " + intsec.getRating() + "\n" + "Comments: " + intsec.getComments());
                    }
                    marker.showInfoWindow();
                } else {
                    isStreetSelected = false;
                    marker.hideInfoWindow();
                }
                return true;
            }
        });

        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
    }

    public void addFireHallPolygons() {
        Polygon fireHallOne = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(49.219605, -122.908411),
                        new LatLng(49.220130, -122.907426),
                        new LatLng(49.220618, -122.908053),
                        new LatLng(49.220094, -122.909042))
                .strokeColor(Color.RED)
                .fillColor(Color.rgb(255, 179, 179)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        fireHallOne.setTag("Glenbrook Firehall");

        Polygon fireHallTwo = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(49.210384, -122.936618),
                        new LatLng(49.210096, -122.937142),
                        new LatLng(49.209888, -122.936841),
                        new LatLng(49.210154, -122.936328))
                .strokeColor(Color.RED)
                .fillColor(Color.rgb(255, 179, 179)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        fireHallTwo.setTag("New Westminster Fire & Rescue Services");

        Polygon qbFireHall = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(49.185770, -122.947971),
                        new LatLng(49.186730, -122.948888),
                        new LatLng(49.187160, -122.947831),
                        new LatLng(49.186235, -122.946950))
                .strokeColor(Color.RED)
                .fillColor(Color.rgb(255, 179, 179)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        qbFireHall.setTag("Queensborough Fire Hall");
    }

    public void addHospitalPolygons() {
        Polygon royalColumbiaHospital = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(49.225605, -122.892860),
                        new LatLng(49.227119, -122.892770),
                        new LatLng(49.227035, -122.890147),
                        new LatLng(49.225571, -122.890115))
                .strokeColor(Color.GREEN)
                .fillColor(Color.rgb(179, 255, 190)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        royalColumbiaHospital.setTag("Royal Columbian Hospital");

        Polygon qpCareHospital = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(49.217017, -122.903444),
                        new LatLng(49.217799, -122.901933),
                        new LatLng(49.216881, -122.901284),
                        new LatLng(49.216362, -122.902453))
                .strokeColor(Color.GREEN)
                .fillColor(Color.rgb(179, 255, 190)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        qpCareHospital.setTag("Queen's Park Extended Care Hospital");
    }

    public void addPolicePolygon() {
        Polygon policeBuilding = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(49.203631, -122.908286),
                        new LatLng(49.204071, -122.907515),
                        new LatLng(49.203708, -122.906989),
                        new LatLng(49.203268, -122.907832))
                .strokeColor(Color.BLUE)
                .fillColor(Color.rgb(179, 222, 255)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        policeBuilding.setTag("New Westminster Police Department");
    }
/**
    public void emergencyCall() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            } else {
                startActivity(intent);
            }
        } else {
            startActivity(intent);
        }
    }

    public void sendMessage() {
        // TODO: Send a message
        GPSHelper gps = new GPSHelper(this);
        gps.getMyLocation();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

        StringBuffer smsBody = new StringBuffer();
        smsBody.append("I'm in danger! This is my location: ");
        smsBody.append("http://maps.google.com/?q=");
        smsBody.append(gps.getLatitude());
        smsBody.append(",");
        smsBody.append(gps.getLongitude());

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, smsBody.toString());
        startActivity(i);
    }
*/
    public void rateLocation(boolean isSelected) {
        if (isSelected) {
            RateDFrag rating = RateDFrag.getInstance(markerId);
            rating.show(getFragmentManager(), "rating_dialog");
        }
    }

    public void addIntersections() {
        IntersectionDao intsecDao = new IntersectionDao(MapsActivity.this);
        List<Intersection> listOfIntsec = intsecDao.findAllIntsec();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);

        for (Intersection intersect : listOfIntsec) {
            System.out.println(intersect.getId());
            LatLng pos = new LatLng(intersect.getCoordY(), intersect.getCoordX());
            LatLng posplus = SphericalUtil.computeOffset(pos, 20, 320);
//            mMap.addMarker(new MarkerOptions().position(pos).title(intersect.getIntersectionName()).icon(icon)).setSnippet(String.valueOf(intersect.getId()));
            mMap.addMarker(new MarkerOptions().position(pos).title(intersect.getIntersectionName()).icon(icon)).setTag(String.valueOf(intersect.getId()));
            Polyline line = mMap.addPolyline(new PolylineOptions().add(pos, posplus).width(18).color(Color.rgb(255, 166, 102)));
            line.setClickable(true);
        }

        intsecDao.close();
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.rating));
            tvSnippet.setText(marker.getSnippet());

            return myContentsView;
        }
    }

    public void LoadStyledMap() {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.map_style);
        mMap.setMapStyle(style);
    }
}