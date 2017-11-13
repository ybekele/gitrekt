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
    private ArrayAdapter<String> adapter;
    HabitEventController habitEventController;
    ArrayList<String> todaysHabits = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        HabitTypeController htc = new HabitTypeController(this);
        HabitEventController hc = new HabitEventController(this);

        // Get IDs
        htc.loadHTID();
        hc.loadHEID();
        // Restore all HT and HE
        htc.loadFromFile();
        hc.loadFromFile();
        // Get Recent events and HabitTypes for today
        htc.getHabitTypesForToday();
        hc.updateRecentHabitEvents();


        createTypeButton = (Button) findViewById(R.id.button);
        allButton = (Button) findViewById(R.id.button2);
        historybutton = (Button) findViewById(R.id.button3);

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


    }


    @Override
    protected void onStart() {
        super.onStart();
        displayNames = (ListView) findViewById(R.id.listView);
        HabitTypeStateManager.getHTStateManager().calculateHabitsForToday();
        final ArrayList<HabitType> today = HabitTypeStateManager.getHabitTypesForToday();


        // Makes sure we have due Habits for today
        if (!(today.isEmpty())) {
            for (int i = 0; i < today.size(); i++) {
                HabitType ht = today.get(i);
                String stringTitle = ht.getTitle();
                Log.d("see",stringTitle);
                Log.d("seen","asdfasdfasdfasf");
                todaysHabits.add(stringTitle);

            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todaysHabits);
            displayNames.setAdapter(adapter);

            //todaysHabits.clear();
        }


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

    }

