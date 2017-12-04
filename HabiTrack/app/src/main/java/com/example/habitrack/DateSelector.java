package com.example.habitrack;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class DateSelector extends AppCompatActivity {

    DatePicker datePicker;
    Button doneButton;

    // Date vars
    Integer year;
    Integer month;
    Integer day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selector);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        doneButton = (Button) findViewById(R.id.dateDoneButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = datePicker.getYear();
                month = datePicker.getMonth();
                day = datePicker.getDayOfMonth();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("year", year);
                returnIntent.putExtra("month", month+1);
                returnIntent.putExtra("day", day);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
