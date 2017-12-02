package com.example.habitrack;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {



    String titleString;
    ArrayList<LatLng> friends_locations = new ArrayList<LatLng>();
    ArrayList<String> friends_IDs;
    Integer method_check;

    Marker m;  //reference to the marker


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.


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



        friends_IDs = getIntent().getStringArrayListExtra("tracker");

        //titleString = hc.getHabitTitle(htID);

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
        HabitEventController hec = new HabitEventController(this);
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Toast.makeText(this, "Map is ready", LENGTH_LONG).show();

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
                //Log.d("frass", "MarkersList" + Markerslist.toString());


            }
        }

        //Search bar
        searchBar();


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
