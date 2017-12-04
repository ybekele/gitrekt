package com.example.habitrack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MyFollowing extends AppCompatActivity {
    ListView followersListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<NewUser> following = new ArrayList<>();
    private ArrayList<String> displayFollowing = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_following);

        followersListView = findViewById(R.id.followersListview);
        for (Integer i = 0; i < following.size(); i++) {
            displayFollowing.add(following.get(i).getTitle());
        }

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, displayFollowing);
        followersListView.setAdapter(adapter);


    }
}
