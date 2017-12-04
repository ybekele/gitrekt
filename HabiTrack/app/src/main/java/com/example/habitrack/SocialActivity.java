package com.example.habitrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by yonaelbekele on 2017-11-28.
 */

public class SocialActivity extends AppCompatActivity {
    Button friendsBut;
    Button searchBut;
    Button mapsBut;
    Button requestBut;
    String liuName;
    NewUser liuID;
    private ArrayList<NewUser> allIds = new ArrayList<NewUser>();
    private ArrayList<HabitEvent> followingEvents = new ArrayList<HabitEvent>();
    ListView socialList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_activity);

        friendsBut = (Button) findViewById(R.id.friendsButton);
        searchBut = (Button) findViewById(R.id.searchButton);
        mapsBut = (Button) findViewById(R.id.mapButton);
        requestBut = (Button) findViewById(R.id.requestButton);
        socialList = findViewById(R.id.socialListview);

        followingEvents.clear();
        allIds = getCurrentUsers();
        liuID = getCurrentUser(allIds);
        for (Integer i = 0; i < liuID.usersFollowed.size(); i++) {
            /*
            go through usersFollowed and post their Habit Events
             */
        }


        friendsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewFriends = new Intent(getApplicationContext(), MyFollowing.class);
                startActivity(viewFriends);
            }
        });

        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewSearch = new Intent(getApplicationContext(), SearchUsers.class);
                startActivity(viewSearch);
            }
        });

        requestBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewRequests = new Intent(getApplicationContext(), myRequests.class);
                startActivity(viewRequests);
            }
        });


    }

    public NewUser getCurrentUser(ArrayList<NewUser> currentUsers) {
        SharedPreferences loggedInUserID = getApplicationContext().getSharedPreferences("userID", MODE_PRIVATE);
        liuName = loggedInUserID.getString("loggedInName", null);
        NewUser localUser = null;
        for (int i = 0; i < currentUsers.size(); i++) {
            if (currentUsers.get(i).getTitle().equals(liuName)) {
                localUser = currentUsers.get(i);
            }
        }
        return localUser;
    }

    public ArrayList<NewUser> getCurrentUsers() {

        ArrayList<NewUser> eu = new ArrayList<>();
        ElasticSearchController.GetUser getExistingUsers = new ElasticSearchController.GetUser();
        // may be changed
        getExistingUsers.execute("");
        Log.d("entered", eu.toString());
        try {
            eu = getExistingUsers.get();
            Log.d("existing", eu.toString());
        } catch (Exception e) {
            Log.i("Error", "Failed to get existing user ID's");
        }
        return eu;
    }


}

