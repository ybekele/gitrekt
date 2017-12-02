package com.example.habitrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    Integer htID;
    Integer heID;
    String titleString;
    ArrayList<LatLng> friends_locations = new ArrayList<LatLng>();
    ArrayList<String> friends_IDs;
    Integer method_check;

    Marker m;  //reference to the marker

    private GoogleMap mMap;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private static final int DEFAULT_ZOOM = 10;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    //widgets
    private EditText mSearchText;
    int i;
    String the_id;
    //Markers list
    List<Marker> Markerslist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        // Get incoming HT's ID
        HabitTypeController hc = new HabitTypeController(this);
        HabitEventController hec = new HabitEventController(this);
        htID = intent.getIntExtra("htID", -1);
        heID = intent.getIntExtra("heID", -1);



        titleString = hc.getHabitTitle(htID);




        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Search box text
        mSearchText = (EditText) findViewById(R.id.input_search);
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

        mMap.setPadding(0, 200, 0, 0);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Toast.makeText(this, "Map is ready", LENGTH_LONG).show();
        mMap.setOnMyLocationButtonClickListener(this);

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        //TEMPORARY MAYBE
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                createMarker(latLng);

            }
        });


        //Search bar
        searchBar();

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
                                createMarker(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                            } else {
                                //Log.d(, "onComplete: current location is null");
                                Toast.makeText(MapsActivity.this, "Unable to get current location", LENGTH_LONG).show();
                            }
                        }
                    }
                });
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

    public void createMarker(final LatLng latLng) {
        //Creates a dialog, prompts the user to create a marker or not.
        new AlertDialog.Builder(this).setTitle("Confirm")
                .setMessage("Do you want to add Marker?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //If location is not null, set position/marker to location clicked on the map.
                                //Else if a marker already exist overwrite it and add a new marker.
                                if (m != null) {
                                    m.setPosition(latLng);
                                } else {
                                    //Get specific habitType title
                                    m = mMap.addMarker(new MarkerOptions().position(latLng).title(titleString));
                                    Markerslist.add(m);
                                    Log.d("frass", "MarkersList" + Markerslist.toString());
                                }
                                dialog.dismiss();
                            }
                        }
                )
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //do nothing
                                dialog.dismiss();
                            }
                        }
                ).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HabitEventController hec = new HabitEventController(this);

        if (m != null && m.getPosition() != null) {
            hec.setHabitEventLocation(heID, m.getPosition());
        }

        if (m == null || m.getPosition() == null) {
            finish();
        }
        //I removed this call because the app will close and crash before displaying the dialog, Don't if it affects the saving.
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
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

    //Search for a location or title of a maker
    public void geoLocate() {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
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

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", LENGTH_LONG).show();
        return false;
    }



}