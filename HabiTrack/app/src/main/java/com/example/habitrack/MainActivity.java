package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private ListView displayNames;
    private ArrayList<HabitType> today = new ArrayList<HabitType>();
    //private ArrayAdapter<String> adapter;
    private ArrayAdapter<HabitType> adapter;
    ArrayList<String> todaysHabits = new ArrayList<>();
    HabitTypeController htc = new HabitTypeController(this);
    HabitEventController hec = new HabitEventController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Preliminary Setup
        // 1. Get the controllers
        // 2. Get IDs if they're saved. Otherwise, create new IDs
        htc.loadHTID();
        hec.loadHEID();
        // 3. Restore all HT and HE if saved
        htc.loadFromFile();
        hec.loadFromFile();
        // 4. Get Recent events and HabitTypes for today
        htc.generateHabitsForToday();
        hec.updateRecentHabitEvents();

        createTypeButton = (Button) findViewById(R.id.button);
        allButton = (Button) findViewById(R.id.button2);
        historybutton = (Button) findViewById(R.id.button3);
        displayNames = (ListView) findViewById(R.id.listView);

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
                Intent intent = new Intent(MainActivity.this, NewHabitEventActivity.class);
                intent.putExtra("HabitTitle", displayNames.getItemAtPosition(i).toString());
                Log.d("position", displayNames.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        htc.loadHTID();
        hec.loadHEID();
        // 3. Restore all HT and HE if saved
        htc.loadFromFile();
        hec.loadFromFile();
        // 4. Get Recent events and HabitTypes for today
        //htc.generateHabitsForToday();
        today = htc.getHabitTypesForToday();
        hec.updateRecentHabitEvents();


        adapter = new ArrayAdapter<HabitType>(this, R.layout.list_item, today);
        displayNames.setAdapter(adapter);

        }

    }

