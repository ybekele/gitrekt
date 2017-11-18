package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class HabitTypeDetailsActivity extends AppCompatActivity {

    EditText titleEdit;
    EditText reasonEdit;
    EditText dateEdit;
    ProgressBar progressBar;
    Button editButton;
    Button deleteButton;

    TextView titleView;
    TextView reasonView;
    TextView dateView;

    private String titleString;
    private String reasonString;
    private String dateString;
    private Integer progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_type_details);
        final HabitTypeController htc = new HabitTypeController(this);
        // Get intent
        Intent intent = getIntent();
        // Get htID
        final Integer htID = intent.getIntExtra("habitID", -1);
        // Get habitType
        HabitType currHT = htc.getHabitType(htID);
        titleString = htc.getHabitTitle(htID);
        reasonString = currHT.getReason();
        Calendar date = currHT.getStartDate();
        dateString = date.get(Calendar.MONTH) + "/" +
                date.get(Calendar.DATE) + "/" +
                date.get(Calendar.YEAR);

        // Get text views
        titleView = (TextView) findViewById(R.id.titleView);
        reasonView = (TextView) findViewById(R.id.reasonView);
        dateView = (TextView) findViewById(R.id.dateView);

        // Display the text
        titleView.setText(titleString); // title
        reasonView.setText(reasonString); // reason
        dateView.setText(dateString); // date

        // Set progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Set the max, and the progress
        //progressBar.setMax(htc.getMaxCounter(htID));
        progressBar.setMax(100);
        //progressBar.setProgress(htc.getCompletedCounter(htID));
        progressBar.setProgress(35);


        // Get the edit text references
        titleEdit = (EditText) findViewById(R.id.titleBox);
        reasonEdit = (EditText) findViewById(R.id.commentBox);
        dateEdit = (EditText) findViewById(R.id.startDateBox);


        // Edit button
        editButton = (Button) findViewById(R.id.button9);

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
                dateView.setVisibility(View.GONE);
                dateEdit.setVisibility(View.VISIBLE);
                dateEdit.setText(dateString);
                dateEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String dateString = dateEdit.getText().toString();
                        String[] parts = dateString.split("/");
                        Integer month = Integer.parseInt(parts[0]);
                        Integer day = Integer.parseInt(parts[1]);
                        Integer year = Integer.parseInt(parts[2]);
//                        String expectedPattern = "MM/dd/yyyy";
//                        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern, Locale.CANADA);
//                        Calendar date = Calendar.getInstance();
                        Calendar newDate = Calendar.getInstance();
                        try {
                            newDate.set(Calendar.MONTH, month);
                            newDate.set(Calendar.DATE, day);
                            newDate.set(Calendar.YEAR, year);

                            htc.editHabitTypeStartDate(htID, newDate);
                    /* exception, will make date of creation current date if not entered correctly or specified */
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Invalid date", Toast.LENGTH_LONG).show();
                        }
                    }

                });
                return false;
            }
        });
        dateEdit.setVisibility(View.GONE);
        dateView.setVisibility(View.VISIBLE);


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                htc.deleteHabitType(htID);
                finish();
            }
        });


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




    }

    @Override
    protected void onStart() {
        super.onStart();


    }

}
