package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HabitTypeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_type_details);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String titleString = intent.getStringExtra("HabitTitle");
        int typeID = intent.getIntExtra("typeID", -1);

    }
}
