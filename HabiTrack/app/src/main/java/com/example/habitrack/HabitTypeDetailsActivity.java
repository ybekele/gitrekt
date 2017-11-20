package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class HabitTypeDetailsActivity extends AppCompatActivity {

    EditText title;
    EditText reason;
    EditText hDate;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_type_details);
        final HabitTypeController htc = new HabitTypeController(this);
        // Get intent
        Intent intent = getIntent();
        // Get htID
        Integer htID = intent.getIntExtra("habitID", -1);
        // Get habitType
        HabitType currHT = htc.getHabitType(htID);
        final String titleString = htc.getHabitTitle(htID);
        String reasonString = currHT.getReason();
        String dateString = currHT.getStartDate().toString();

        //final String titleString = intent.getStringExtra("HabitTitle");
        final int typeID = intent.getIntExtra("typeID", -1);
        title = (EditText) findViewById(R.id.editText8);
        reason = (EditText) findViewById(R.id.editText9);
        hDate = (EditText) findViewById(R.id.editText10);
        editButton = (Button) findViewById(R.id.button9);


        title.setText(titleString);
        //final String reasonString = htc.getHabitReason(typeID);
        Calendar dateStart = htc.getStartDate(typeID);
        //title.setText(reasonString);
        reason.setText(reasonString);
        hDate.setText(dateStart.toString());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(title.getText().toString().equals(titleString))) {
                    htc.editHabitTypeTitle(typeID, title.getText().toString());
                    Toast.makeText(getApplicationContext(), "Edited Title", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "No edits to be made", Toast.LENGTH_SHORT).show();
                }
                // Do we need to edit the reason?
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();


    }

}
