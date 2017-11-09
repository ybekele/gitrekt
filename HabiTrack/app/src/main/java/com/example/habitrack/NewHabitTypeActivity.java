package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewHabitTypeActivity extends AppCompatActivity {
    ArrayList<Integer> plan = new ArrayList<Integer>();
    Switch sundaySwitch;
    Switch mondaySwitch;
    Switch tuesdaySwitch;
    Switch wednesdaySwitch;
    Switch thursdaySwitch;
    Switch fridaySwitch;
    Switch saturdaySwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit_type);
        final HabitTypeController htc = new HabitTypeController();


        Button createButton = (Button) findViewById(R.id.button6);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newType = new Intent(NewHabitTypeActivity.this, MainActivity.class);
                EditText titleEntry = (EditText) findViewById(R.id.editText3);
                EditText reasonEntry = (EditText) findViewById(R.id.editText4);
                EditText dateEntry = (EditText) findViewById(R.id.editText5);

                String title = titleEntry.getText().toString();
                String reason = reasonEntry.getText().toString();

                /*
                Expected format for date string : "MM/dd/yyy"
                Guide from https://alvinalexander.com/java/simpledateformat-convert-string-to-date-formatted-parse
                 to convert String to date
                 */
                String dateString = dateEntry.getText().toString();
                String expectedPattern = "MM/dd/yyyy";
                SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
                Calendar date = Calendar.getInstance();
                try {
                    Date dateDate = formatter.parse(dateString);
                    date.setTime(dateDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                    date = Calendar.getInstance();
                }


                sundaySwitch = (Switch)findViewById(R.id.sunday);
                mondaySwitch = (Switch) findViewById(R.id.monday);
                tuesdaySwitch = (Switch) findViewById(R.id.tuesday);
                wednesdaySwitch = (Switch) findViewById(R.id.wednesday);
                thursdaySwitch = (Switch) findViewById(R.id.thursday);
                fridaySwitch = (Switch) findViewById(R.id.friday);
                saturdaySwitch = (Switch) findViewById(R.id.saturday);

                if(sundaySwitch.isChecked()) {
                    plan.add(Calendar.SUNDAY);
                }
                if(mondaySwitch.isChecked()) {
                    plan.add(Calendar.MONDAY);
                }
                if(tuesdaySwitch.isChecked()) {
                    plan.add(Calendar.TUESDAY);
                }
                if(wednesdaySwitch.isChecked()) {
                    plan.add(Calendar.WEDNESDAY);
                }
                if(thursdaySwitch.isChecked()) {
                    plan.add(Calendar.THURSDAY);
                }
                if(fridaySwitch.isChecked()) {
                    plan.add(Calendar.FRIDAY);
                }
                if(saturdaySwitch.isChecked()) {
                    plan.add(Calendar.SATURDAY);
                }

                if ((!(title.equals("")) && !(reason.equals("")) && plan != null)) {
                    htc.createNewHabitType(title, reason, date, plan);


                }

                else {
                    Toast.makeText(getApplicationContext(), "Invalid Creation", Toast.LENGTH_SHORT).show();


                }

                /*
                newType.putExtra("title", title);
                newType.putExtra("reason", reason);
                newType.putExtra("date", date);
                */
                startActivity(newType);
                finish();
            }
        });

    }

    public void CheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isChecked()) {
            if ((buttonView.getId()) == sundaySwitch.getId()) {
                plan.add(Calendar.SUNDAY);
            }
            if ((buttonView.getId()) == mondaySwitch.getId()) {
                plan.add(Calendar.MONDAY);
            }
            if ((buttonView.getId()) == tuesdaySwitch.getId()) {
                plan.add(Calendar.TUESDAY);
            }
            if ((buttonView.getId()) == wednesdaySwitch.getId()) {
                plan.add(Calendar.WEDNESDAY);
            }
            if ((buttonView.getId()) == thursdaySwitch.getId()) {
                plan.add(Calendar.THURSDAY);
            }
            if ((buttonView.getId()) == fridaySwitch.getId()) {
                plan.add(Calendar.FRIDAY);
            }
            if ((buttonView.getId()) == saturdaySwitch.getId()) {
                plan.add(Calendar.SATURDAY);
            }
        }

    }
    /*

    public void CheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(sundaySwitch.isChecked()) {
            plan.add(Calendar.SUNDAY);
        }
        if(mondaySwitch.isChecked()) {
            plan.add(Calendar.MONDAY);
        }
        if(tuesdaySwitch.isChecked()) {
            plan.add(Calendar.TUESDAY);
        }
        if(wednesdaySwitch.isChecked()) {
            plan.add(Calendar.WEDNESDAY);
        }
        if(thursdaySwitch.isChecked()) {
            plan.add(Calendar.THURSDAY);
        }
        if(fridaySwitch.isChecked()) {
            plan.add(Calendar.FRIDAY);
        }
        if(saturdaySwitch.isChecked()) {
            plan.add(Calendar.SATURDAY);
        }
    }
    */


}
