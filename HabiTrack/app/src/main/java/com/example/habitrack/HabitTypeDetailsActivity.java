package com.example.habitrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class HabitTypeDetailsActivity extends AppCompatActivity {
    TextView progressNum;
    EditText titleEdit;
    EditText reasonEdit;
    EditText dateEdit;
    ProgressBar progressBar;
    //Button editButton;
    Button deleteButton;

    // checkboxes for days of the week
    CheckBox sundayBox;
    CheckBox mondayBox;
    CheckBox tuesdayBox;
    CheckBox wednesdayBox;
    CheckBox thursdayBox;
    CheckBox fridayBox;
    CheckBox saturdayBox;
    // Edit schedule toggle
    Button editSchedule;
    // Bool checker
    //Boolean canEditSchedule = Boolean.FALSE;
    // Date var
    Calendar date;
    // Request Code for date entry
    private final Integer DATE_ENTRY = 101;

    HabitTypeController htc = new HabitTypeController(this);

    TextView titleView;
    TextView reasonView;
    TextView dateView;

    private String titleString;
    private String reasonString;
    private String dateString;
    private Integer htID = -1;
    private ArrayList<Integer> schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_type_details);
        // Get intent
        Intent intent = getIntent();
        // Get htID
        htID = intent.getIntExtra("habitID", -1);
        // Get habitType
        final HabitType currHT = htc.getHabitType(htID);
        titleString = htc.getHabitTitle(htID);
        reasonString = currHT.getReason();
        final Calendar date = currHT.getStartDate();
        dateString = date.get(Calendar.MONTH) + "/" +
                date.get(Calendar.DATE) + "/" +
                date.get(Calendar.YEAR);

        // Get text views
        titleView = (TextView) findViewById(R.id.titleView);
        reasonView = (TextView) findViewById(R.id.reasonView);
        dateView = (TextView) findViewById(R.id.dateView);
        progressNum = (TextView) findViewById(R.id.statPercent);

        // Display the text
        titleView.setText(titleString); // title
        reasonView.setText(reasonString); // reason
        dateView.setText(dateString); // date

        // Set progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Set the max, and the progress
        //progressBar.setMax(htc.getMaxCounter(htID));
        progressBar.setMax(htc.getMaxCounter(htID));
        progressBar.setProgress(htc.getCompletedCounter(htID));
        progressNum.setText(htc.getCompletedCounter(htID).toString());

        // Get the edit text references
        titleEdit = (EditText) findViewById(R.id.titleBox);
        reasonEdit = (EditText) findViewById(R.id.commentBox);
        dateEdit = (EditText) findViewById(R.id.startDateBox);

        // Edit button
        //editButton = (Button) findViewById(R.id.button9);

        // Get the checkboxes for days of the week
        sundayBox = (CheckBox) findViewById(R.id.sunCheckBox);
        mondayBox = (CheckBox) findViewById(R.id.monCheckBox);
        tuesdayBox = (CheckBox) findViewById(R.id.tuesCheckBox);
        wednesdayBox = (CheckBox) findViewById(R.id.wedCheckBox);
        thursdayBox = (CheckBox) findViewById(R.id.thursCheckBox);
        fridayBox = (CheckBox) findViewById(R.id.friCheckBox);
        saturdayBox = (CheckBox) findViewById(R.id.satCheckBox);
        // Get the edit toggle
        editSchedule = (Button) findViewById(R.id.editSchedule);

        // delete button
        deleteButton = (Button) findViewById(R.id.deleteButton);

        titleView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                titleView.setVisibility(View.GONE);
                titleEdit.setVisibility(View.VISIBLE);
                titleEdit.setText(titleString);
                titleEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String newTitle = titleEdit.getText().toString();
                        htc.editHabitTypeTitle(htID, newTitle);
                    }
                });
                return false;
            }
        });
        titleEdit.setVisibility(View.GONE);
        titleView.setVisibility(View.VISIBLE);

        reasonView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                reasonView.setVisibility(View.GONE);
                reasonEdit.setVisibility(View.VISIBLE);
                reasonEdit.setText(reasonString);
                reasonEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String newReason = reasonEdit.getText().toString();
                        htc.editHabitTypeReason(htID, newReason);
                    }
                });
                return false;
            }
        });
        reasonEdit.setVisibility(View.GONE);
        reasonView.setVisibility(View.VISIBLE);

        dateView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i = new Intent(getApplicationContext(), DateSelector.class);
                startActivityForResult(i, DATE_ENTRY);

//                dateView.setVisibility(View.GONE);
//                dateEdit.setVisibility(View.VISIBLE);
//                dateEdit.setText(dateString);
//                dateEdit.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//                        String dateString = dateEdit.getText().toString();
//                        String[] parts = dateString.split("/");
//                        Integer month = Integer.parseInt(parts[0]);
//                        Integer day = Integer.parseInt(parts[1]);
//                        Integer year = Integer.parseInt(parts[2]);
////                        String expectedPattern = "MM/dd/yyyy";
////                        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern, Locale.CANADA);
////                        Calendar date = Calendar.getInstance();
//                        Calendar newDate = Calendar.getInstance();
//                        try {
//                            newDate.set(Calendar.MONTH, month);
//                            newDate.set(Calendar.DATE, day);
//                            newDate.set(Calendar.YEAR, year);
//
//                            htc.editHabitTypeStartDate(htID, newDate);
//                    /* exception, will make date of creation current date if not entered correctly or specified */
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Invalid date", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                });
                return false;
            }
        });
        dateEdit.setVisibility(View.GONE);
        dateView.setVisibility(View.VISIBLE);

        // delete button functionality
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                htc.deleteHabitType(htID);
                finish();
            }
        });

        // check boxes for the schedules
        schedule = currHT.getSchedule();
        for (Integer count = 0; count < schedule.size(); count++) {
            Integer res = schedule.get(count);
            if (res == Calendar.SUNDAY) {
                sundayBox.setChecked(Boolean.TRUE);
            } else if (res == Calendar.MONDAY) {
                mondayBox.setChecked(Boolean.TRUE);
            } else if (res == Calendar.TUESDAY) {
                tuesdayBox.setChecked(Boolean.TRUE);
            } else if (res == Calendar.WEDNESDAY) {
                wednesdayBox.setChecked(Boolean.TRUE);
            } else if (res == Calendar.THURSDAY) {
                thursdayBox.setChecked(Boolean.TRUE);
            } else if (res == Calendar.FRIDAY) {
                fridayBox.setChecked(Boolean.TRUE);
            } else if (res == Calendar.SATURDAY) {
                saturdayBox.setChecked(Boolean.TRUE);
            }
        }


        editSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> newSchedule;
                newSchedule = new ArrayList<>();
                if (sundayBox.isChecked()) {
                    newSchedule.add(Calendar.SUNDAY);
                }

                if (mondayBox.isChecked()) {
                    newSchedule.add(Calendar.MONDAY);
                }
                if (tuesdayBox.isChecked()) {
                    newSchedule.add(Calendar.TUESDAY);
                }
                if (wednesdayBox.isChecked()) {
                    newSchedule.add(Calendar.WEDNESDAY);
                }
                if (thursdayBox.isChecked()) {
                    newSchedule.add(Calendar.THURSDAY);
                }
                if (fridayBox.isChecked()) {
                    newSchedule.add(Calendar.FRIDAY);
                }
                if (saturdayBox.isChecked()) {
                    newSchedule.add(Calendar.SATURDAY);
                }

                Toast.makeText(getApplicationContext(), "Applying Edits",
                        Toast.LENGTH_SHORT).show();
                for (Integer log : newSchedule) {
                    Log.v("tag", Integer.toString(log));
                }
                htc.editHabitTypeSchedule(htID, newSchedule);
            }
        });
    }
        /*

        sundayBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canEditSchedule) {
                    ArrayList<Integer> newSchedule = htc.getSchedule(htID);
                    if (!sundayBox.isChecked()) {
                        //sundayBox.setChecked(Boolean.FALSE);
                        if (newSchedule.contains(Calendar.SUNDAY)) {
                            newSchedule.remove(Calendar.SUNDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Removed SUNDAY from Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //sundayBox.setChecked(Boolean.TRUE);
                        if (!newSchedule.contains(Calendar.SUNDAY)) {
                            newSchedule.add(Calendar.SUNDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Added SUNDAY to Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!sundayBox.isChecked()) {
                        sundayBox.setChecked(Boolean.TRUE);
                    } else {
                        sundayBox.setChecked(Boolean.FALSE);
                    }
                }
            }
        });

        mondayBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canEditSchedule) {
                    ArrayList<Integer> newSchedule = htc.getSchedule(htID);
                    if (!mondayBox.isChecked()) {
                        if (newSchedule.contains(Calendar.MONDAY)) {
                            newSchedule.remove(Calendar.MONDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Removed MONDAY from Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!newSchedule.contains(Calendar.MONDAY)) {
                            newSchedule.add(Calendar.MONDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Added MONDAY to Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!mondayBox.isChecked()) {
                        mondayBox.setChecked(Boolean.TRUE);
                    } else {
                        mondayBox.setChecked(Boolean.FALSE);
                    }
                }
            }
        });

        tuesdayBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canEditSchedule) {
                    ArrayList<Integer> newSchedule = htc.getSchedule(htID);
                    if (!tuesdayBox.isChecked()) {
                        if (newSchedule.contains(Calendar.TUESDAY)) {
                            newSchedule.remove(Calendar.TUESDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Removed TUESDAY from Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!newSchedule.contains(Calendar.TUESDAY)) {
                            newSchedule.add(Calendar.TUESDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Added TUESDAY to Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!tuesdayBox.isChecked()) {
                        tuesdayBox.setChecked(Boolean.TRUE);
                    } else {
                        tuesdayBox.setChecked(Boolean.FALSE);
                    }
                }
            }
        });

        wednesdayBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canEditSchedule) {
                    ArrayList<Integer> newSchedule = htc.getSchedule(htID);
                    if (!wednesdayBox.isChecked()) {
                        if (newSchedule.contains(Calendar.WEDNESDAY)) {
                            newSchedule.remove(Calendar.WEDNESDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Removed WEDNESDAY from Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!newSchedule.contains(Calendar.WEDNESDAY)) {
                            newSchedule.add(Calendar.WEDNESDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Added WEDNESDAY to Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!wednesdayBox.isChecked()) {
                        wednesdayBox.setChecked(Boolean.TRUE);
                    } else {
                        wednesdayBox.setChecked(Boolean.FALSE);
                    }
                }
            }
        });

        thursdayBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canEditSchedule) {
                    ArrayList<Integer> newSchedule = htc.getSchedule(htID);
                    if (!thursdayBox.isChecked()) {
                        if (newSchedule.contains(Calendar.THURSDAY)) {
                            newSchedule.remove(Calendar.THURSDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Removed THURSDAY from Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!newSchedule.contains(Calendar.THURSDAY)) {
                            newSchedule.add(Calendar.THURSDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Added THURSDAY to Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!thursdayBox.isChecked()) {
                        thursdayBox.setChecked(Boolean.TRUE);
                    } else {
                        thursdayBox.setChecked(Boolean.FALSE);
                    }
                }
            }
        });

        fridayBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canEditSchedule) {
                    ArrayList<Integer> newSchedule = htc.getSchedule(htID);
                    if (!fridayBox.isChecked()) {
                        if (newSchedule.contains(Calendar.FRIDAY)) {
                            newSchedule.remove(Calendar.FRIDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Removed FRIDAY from Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!newSchedule.contains(Calendar.FRIDAY)) {
                            newSchedule.add(Calendar.FRIDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Added FRIDAY to Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!fridayBox.isChecked()) {
                        fridayBox.setChecked(Boolean.TRUE);
                    } else {
                        fridayBox.setChecked(Boolean.FALSE);
                    }
                }
            }
        });

        saturdayBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canEditSchedule) {
                    ArrayList<Integer> newSchedule = htc.getSchedule(htID);
                    if (!saturdayBox.isChecked()) {
                        if (newSchedule.contains(Calendar.SATURDAY)) {
                            newSchedule.remove(Calendar.SATURDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Removed SATURDAY from Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!newSchedule.contains(Calendar.SATURDAY)) {
                            newSchedule.add(Calendar.SATURDAY);
                            htc.editHabitTypeSchedule(htID, newSchedule);
                            Toast.makeText(getApplicationContext(), "Added SATURDAY to Schedule",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!saturdayBox.isChecked()) {
                        saturdayBox.setChecked(Boolean.TRUE);
                    } else {
                        saturdayBox.setChecked(Boolean.FALSE);
                    }
                }
            }
        });
    }
*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == DATE_ENTRY) {
            if(resultCode == Activity.RESULT_OK){
                Integer year = data.getIntExtra("year", -1);
                Integer month = data.getIntExtra("month", -1);
                Integer day = data.getIntExtra("day", -1);
                if(!year.equals(-1) && !month.equals(-1) && !day.equals(-1)){
                    date = Calendar.getInstance();
                    date.set(Calendar.YEAR, year);
                    date.set(Calendar.MONTH, month);
                    date.set(Calendar.DATE, day);
//                    dateSelect.setVisibility(View.GONE);
//                    dateView.setVisibility(View.VISIBLE);
//                    dateEdit.setVisibility(View.VISIBLE);
                    dateView.setText(month.toString() + "/" + day.toString() + "/" + year.toString());
                    htc.editHabitTypeStartDate(htID, date);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

        //final String titleString = intent.getStringExtra("HabitTitle");
//        final int typeID = intent.getIntExtra("typeID", -1);

        //title.setText(titleString);
        //final String reasonString = htc.getHabitReason(typeID);
//        Calendar dateStart = htc.getStartDate(typeID);
        //title.setText(reasonString);
        //reason.setText(reasonString);
        //hDate.setText(dateStart.toString());

//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!(title.getText().toString().equals(titleString))) {
//                    htc.editHabitTypeTitle(typeID, title.getText().toString());
//                    Toast.makeText(getApplicationContext(), "Edited Title", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "No edits to be made", Toast.LENGTH_SHORT).show();
//                }
//                // Do we need to edit the reason?
//            }
//        });




    @Override
    protected void onStart() {
        super.onStart();


    }

}
