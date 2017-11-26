package com.example.habitrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 *  Handles all the Activities on the Main Page once Logged in
 */

public class MainActivity extends AppCompatActivity {

    // declare components
    Button createTypeButton;
    Button historybutton;
    Button allButton;
    Button logoutButton;
    private ListView displayNames;
    //private ArrayList<HabitType> today = new ArrayList<HabitType>();
    private ArrayList<HabitEvent> today = new ArrayList<HabitEvent>();
    //private ArrayAdapter<HabitType> adapter;
    private ArrayAdapter<HabitEvent> adapter;
    // Preliminary Setup
    // 1. Get the controllers
    HabitTypeController htc = new HabitTypeController(this);
    HabitEventController hec = new HabitEventController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createTypeButton = (Button) findViewById(R.id.createHabitButton);
        allButton = (Button) findViewById(R.id.allButton);
        historybutton = (Button) findViewById(R.id.historyButton);
        logoutButton = (Button) findViewById(R.id.button10);
        displayNames = (ListView) findViewById(R.id.listView);
// ------------------
        // Checks if app is in a logged in state. If not, goes to login page (SignupActivity)
        SharedPreferences loggedInPrefs = getApplicationContext().getSharedPreferences("loggedInStatus", MODE_PRIVATE);
        final SharedPreferences.Editor loggedInEditor = loggedInPrefs.edit();
        boolean isLoggedIn = loggedInPrefs.getBoolean("loggedIn", false);
        final Intent toLogIn = new Intent(getApplicationContext(), SignupActivity.class);
        if(!isLoggedIn) {
            startActivity(toLogIn);
        }

        //If logoutButton is clicked, change loggedIn to false and go to SignupAcitivty
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loggedInEditor.putBoolean("loggedIn", false);
                loggedInEditor.apply();
                startActivity(toLogIn);
            }
        });
// ------------------

        // Handles if user pressed CREATE button , redirects to create a new habit type class
        createTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newType = new Intent(getApplicationContext(), NewHabitTypeActivity.class);
                startActivity(newType);
            }
        });

        // Handles if user pressed HISTORY button , redirects to history class
        historybutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent newType = new Intent(getApplicationContext(), HabitHistory.class);
                //Intent newType = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(newType);
            }
        });

        // Handles if user pressed ALL button, redirects to all habit types
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewAll = new Intent(getApplicationContext(), AllHabitTypesActivity.class);
                startActivity(viewAll);
            }
        });

        // Handles the pressing of Habits on the Main Activity to launch a new habit event
        displayNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //intent.putExtra("habitID", today.get(i).getID());
                Intent intent;
                Integer heID = today.get(i).getHabitEventID();
                if(hec.getHabitEventIsEmpty(heID)) {
                    intent = new Intent(MainActivity.this, NewHabitEventActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, HabitEventDetailsActivity.class);
                }
                intent.putExtra("habitEventID", heID);
                intent.putExtra("habitTypeID", hec.getCorrespondingHabitTypeID(heID));
                startActivity(intent);
            }
        });

        // 2. load
        htc.loadHTID();
        hec.loadHEID();
        // 3. Restore all HT and HE if saved
        htc.loadFromFile();
        hec.loadFromFile();
        // 4. Get Recent events and HabitTypes for today
        htc.generateHabitsForToday();
        hec.updateRecentHabitEvents();

    }


    @Override
    protected void onStart() {
        super.onStart();
        today = hec.getHabitEventsForToday();

        adapter = new ArrayAdapter<HabitEvent>(this, R.layout.list_item, today);
        displayNames.setAdapter(adapter);

    }

        @Override
        protected void onResume() {
            super.onResume();

        }

    }

