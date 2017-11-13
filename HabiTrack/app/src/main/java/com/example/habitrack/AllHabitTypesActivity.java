package com.example.habitrack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AllHabitTypesActivity extends AppCompatActivity {
    private ListView allTypes;
    private ArrayAdapter<String> adapter;
    private static ArrayList<HabitType> typeList = new ArrayList<HabitType>();
    private static ArrayList<String> namesOfTypes = new ArrayList<String>();

    HabitTypeController htc = new HabitTypeController(this);
    HabitEventController hc = new HabitEventController(this);

    // Get IDs

    // Get IDs


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habit_types);
        allTypes = (ListView) findViewById(R.id.listView2);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Get IDs

        htc.loadHTID();
        hc.loadHEID();
        // Restore all HT and HE
        htc.loadFromFile();
        hc.loadFromFile();

        // Get Recent events and HabitTypes for today
        htc.getHabitTypesForToday();
        hc.updateRecentHabitEvents();
        typeList = HabitTypeStateManager.getHTStateManager().getAllHabitTypes();
        Log.d("start", "Entered start");
        Log.d("array", typeList.toString());
        if (!(typeList.isEmpty())) {
            for (int i = 0; i < typeList.size(); i++) {
                HabitType tempHt = typeList.get(i);
                String stringTitle = tempHt.getTitle();
                namesOfTypes.add(stringTitle);
                Log.d("stringTitle", stringTitle);
            }
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namesOfTypes);
        allTypes.setAdapter(adapter);
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("start", "Entered resume");
        namesOfTypes.clear();
        htc.getHabitTypesForToday();
        hc.updateRecentHabitEvents();
        typeList = HabitTypeStateManager.getHTStateManager().getAllHabitTypes();
        if (!(typeList.isEmpty())) {
            for (int i = 0; i < typeList.size(); i++) {
                HabitType tempHt = typeList.get(i);
                String stringTitle = tempHt.getTitle();
                namesOfTypes.add(stringTitle);
            }
        }
        adapter.notifyDataSetChanged();
    }
    */
}

