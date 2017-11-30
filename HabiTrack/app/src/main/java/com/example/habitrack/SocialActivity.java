package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by yonaelbekele on 2017-11-28.
 */

public class SocialActivity extends AppCompatActivity {
    Button friendsBut;
    Button searchBut;
    Button mapsBut;
    Button requestBut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_activity);

        friendsBut = (Button) findViewById(R.id.friendsButton);
        searchBut = (Button) findViewById(R.id.searchButton);
        mapsBut = (Button) findViewById(R.id.mapButton);
        requestBut = (Button) findViewById(R.id.requestButton);

        friendsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewFriends = new Intent(getApplicationContext(), MyFollowers.class);
                startActivity(viewFriends);
            }
        });
    }


}

