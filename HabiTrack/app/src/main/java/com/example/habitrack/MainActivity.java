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




public class MainActivity extends AppCompatActivity {
    Button createTypeButton;
    Button historybutton;
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
        historybutton = (Button) findViewById(R.id.button3);
        createTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newType = new Intent(getApplicationContext(), NewHabitTypeActivity.class);
                startActivity(newType);
            }
        });
        historybutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent newType = new Intent(getApplicationContext(), HabitHistory.class);
                startActivity(newType);
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        displayNames = (ListView) findViewById(R.id.listView);
        HabitTypeStateManager.getHTStateManager().calculateHabitsForToday();
        final ArrayList<HabitType> today = HabitTypeStateManager.getHabitTypesForToday();


        if (!(today.isEmpty())) {
            for (int i = 0; i < today.size(); i++) {
                HabitType ht = today.get(i);
                String stringTitle = ht.getTitle();
                Log.d("see",stringTitle);
                todaysHabits.add(stringTitle);

            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todaysHabits);
            displayNames.setAdapter(adapter);

            //todaysHabits.clear();
        }

        displayNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                intent = new Intent(MainActivity.this, NewHabitEventActivity.class);
                intent.putExtra("HabitTitle", displayNames.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });

        }

    }

