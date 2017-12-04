package com.example.habitrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MyFollowing extends AppCompatActivity {
    ListView followersListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<NewUser> following = new ArrayList<>();
    private ArrayList<String> displayFollowing = new ArrayList<>();
    NewUser liu;
    String liuName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_following);
        SharedPreferences loggedInUserID = getApplicationContext().getSharedPreferences("userID", MODE_PRIVATE);
        ArrayList<NewUser> allUsers = getCurrentUsers();
        liu = getCurrentUser(allUsers);
        following = liu.usersFollowed;

        followersListView = findViewById(R.id.followersListview);
        for (Integer i = 0; i < following.size(); i++) {
            displayFollowing.add(following.get(i).getTitle());
        }

        Log.d("imfollowin", following.toString());
        Log.d("imfollowinTitles", displayFollowing.toString());

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, displayFollowing);
        followersListView.setAdapter(adapter);


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


