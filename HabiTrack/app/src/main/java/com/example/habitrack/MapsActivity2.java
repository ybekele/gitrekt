package com.example.habitrack;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {



    String titleString;
    ArrayList<LatLng> friends_locations = new ArrayList<LatLng>();
    ArrayList<String> friends_IDs;
    ArrayList<String> all_IDs;
    Integer method_check;
    ToggleButton toggle;
    Boolean test;
    Marker m;  //reference to the marker


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    //private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);


    private static final int DEFAULT_ZOOM = 10;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    //widgets
    private EditText mSearchText;
    int i;
    String the_id;
    //Markers list
    List<Marker> Markerslist = new ArrayList<>();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        HabitTypeController hc = new HabitTypeController(this);
        HabitEventController hec = new HabitEventController(this);

        toggle = (ToggleButton)findViewById(R.id.toggleButton);
        test = true;


        friends_IDs = getIntent().getStringArrayListExtra("tracker");
        all_IDs = getIntent().getStringArrayListExtra("tracker2");

        Log.d("wtf", "friends "+friends_IDs);
        Log.d("wtf", "all "+all_IDs);
        //titleString = hc.getHabitTitle(htID);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Search box text
        mSearchText = (EditText) findViewById(R.id.input_search);




        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            Circle circle;




            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("wtf", "caamamasmfasefa");





                if (isChecked) {
                    //     getLocationPermission();
                    //     updateLocationUI();
                    //     getDeviceLocation();
                    Log.d("hiiii","not removed");
                    circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                            .radius(5000)
                            .strokeColor(Color.rgb(0, 136, 255))
                            .fillColor(Color.argb(20, 0, 136, 255)));


                    highlight();
                }
                else {

                        circle.remove();
                        Log.d("hiiii","removed");
                        switchButton();



                }
            }
        });



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
        HabitEventController hec = new HabitEventController(this);
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Toast.makeText(this, "Map is ready", LENGTH_LONG).show();
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();

//        if (friends_IDs != null) {
//            for (i = 0; i < friends_IDs.size(); i++) {
//                Log.d("rrr", "MarkersList" + friends_IDs.get(i));
//                the_id = friends_IDs.get(i);
//                Log.d("free", "ids" + the_id.toString());
//                friends_locations.add(hec.getHabitEventLocation(Integer.parseInt(the_id)));
//                Log.d("rrr", "the lcoation" + hec.getHabitEventLocation(Integer.parseInt(the_id)).toString());
//
//            }
//
//
//            //Get specific habitType title
//            for (i = 0;i< friends_locations.size();i++) {
//                m = mMap.addMarker(new MarkerOptions().position(friends_locations.get(i)).title(hec.getAllHabitEvent().get(i).getTitle()));
//                Markerslist.add(m);
//                //Log.d("frass", "MarkersList" + Markerslist.toString());
//
//
//            }
//        }

        //Search bar
        Log.d("eeee", "i told you souu");
        searchBar();
        switchButton();


    }




    public void highlight(){



        HabitEventController hec = new HabitEventController(getApplication());
        if(!(friends_locations.isEmpty())){
            friends_locations.clear();
        }
        if (!(Markerslist.isEmpty())) {
            for (Marker marker : Markerslist) {
                if (SphericalUtil.computeDistanceBetween(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), marker.getPosition()) > 5000) {
                    marker.setVisible(false);
                }
            }
            Markerslist.clear();
        }

        if (all_IDs != null) {
            for (i = 0; i < all_IDs.size(); i++) {
                Log.d("rrr", "MarkersList" + all_IDs.get(i));
                the_id = all_IDs.get(i);
                Log.d("free", "ids" + the_id.toString());
                friends_locations.add(hec.getHabitEventLocation(Integer.parseInt(the_id)));
                Log.d("rrr", "the lcoation" + hec.getHabitEventLocation(Integer.parseInt(the_id)).toString());

            }

            //Get specific habitType title
            for (i = 0; i < friends_locations.size(); i++) {
                m = mMap.addMarker(new MarkerOptions().visible(false).position(friends_locations.get(i)).title(hec.getAllHabitEvent().get(i).getTitle()));
                Markerslist.add(m);
            }
        }




        for (Marker marker : Markerslist) {
            if (SphericalUtil.computeDistanceBetween(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), marker.getPosition()) < 5000) {
                marker.setVisible(true);
            }


        }

    }

    public void switchButton(){
        Log.d("eeee", "i told you so");
        HabitEventController hec = new HabitEventController(getApplication());
        if(!(friends_locations.isEmpty())) {
            friends_locations.clear();
        }
        if (!(Markerslist.isEmpty())) {
            for (Marker marker : Markerslist) {
                marker.setVisible(false);

            }
            Markerslist.clear();
        }

        if (friends_IDs != null) {
            for (i = 0; i < friends_IDs.size(); i++) {
                Log.d("rrr", "MarkersList" + friends_IDs.get(i));
                the_id = friends_IDs.get(i);
                Log.d("free", "ids" + the_id.toString());
                friends_locations.add(hec.getHabitEventLocation(Integer.parseInt(the_id)));
                Log.d("rrr", "the lcoation" + hec.getHabitEventLocation(Integer.parseInt(the_id)).toString());

            }


            //Get specific habitType title
            for (i = 0;i< friends_locations.size();i++) {
                m = mMap.addMarker(new MarkerOptions().position(friends_locations.get(i)).title(hec.getAllHabitEvent().get(i).getTitle()));
                Markerslist.add(m);
                Log.d("frass", "MarkersList" + Markerslist.toString());


            }
        }




                }


    private void getDeviceLocation() {

        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            } else {
                                //Log.d(, "onComplete: current location is null");
                                Toast.makeText(MapsActivity2.this, "Unable to get current location", LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    //Search for a location or title of a maker
    public void geoLocate() {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity2.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e("TAG", "geoLocate IOException " + e.getMessage());

        }

        //Search for an address, place, city, etc.
        if (list.size() > 0) {

            Address address = list.get(0);
            Log.d("MAPP", "GEOLOCATE: found a location " + address.toString());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM));

        }


        //Search for a marker title.
        if (Markerslist.size() > 0) {
            for (Marker m : Markerslist) {
                if (m.getTitle().equals(searchString)) {
                    Log.d("MARKER", "GEOLOCATE: found a marker " + m.toString());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), DEFAULT_ZOOM));
                    break; // stop the loop
                }
            }


        }
    }

    private void searchBar() {
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER || keyEvent.getAction() == keyEvent.ACTION_DOWN) {
                    //execute method for searching
                    geoLocate();
                }
                return false;
            }
        });
    }

}
