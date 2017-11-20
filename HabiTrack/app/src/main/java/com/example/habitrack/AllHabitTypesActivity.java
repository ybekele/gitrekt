package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Will display all the Habit Types on Screen.
 * User able to click on Habit Types to redirect to HabitType Details page
 */
public class AllHabitTypesActivity extends AppCompatActivity {
    // declare components
    // int typeID = 0;
    private ListView allTypes;
    //private ArrayAdapter<String> adapter;
    private ArrayAdapter<HabitType> adapter;
    private static ArrayList<HabitType> typeList = new ArrayList<HabitType>();
    // private static ArrayList<String> namesOfTypes = new ArrayList<String>();

    HabitTypeController htc = new HabitTypeController(this);

    /**
     * Initializes the ListView.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habit_types);
        // initialize ListView
        allTypes = (ListView) findViewById(R.id.listView2);

        allTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AllHabitTypesActivity.this, HabitTypeDetailsActivity.class);
                //intent.putExtra("HabitTitle", allTypes.getItemAtPosition(i).toString());
                intent.putExtra("habitID", typeList.get(i).getID());
                startActivity(intent);
//                final ArrayList<HabitType> today = HabitTypeStateManager.getHTStateManager().getHabitTypesForToday();
//                HabitType iterater = null;
//                for (int j = 0; j < today.size(); j++)
//                    iterater = today.get(j);
//                Log.d("iterator", iterater.getTitle());
//                if (allTypes.getItemAtPosition(i).toString().equals(iterater.getTitle())) {
//                    typeID = iterater.getID() ;
//                }
//                intent.putExtra("typeID", typeID);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    /**
     * Get IDs and reload data from File every time app is in onStart
     * Will populize the array for names that will be displayed
     * and handle the necessary IDs for when a Habit Type is selected
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Restore all HT
//        htc.loadFromFile();

        // Get Recent events and HabitTypes for today
        //htc.getHabitTypesForToday();
//        htc.generateHabitsForToday();
        typeList = HabitTypeStateManager.getHTStateManager().getAllHabitTypes();
//        Log.d("start", "Entered start");
//        Log.d("array", typeList.toString());
//        if (!(typeList.isEmpty())) {
//            for (int i = 0; i < typeList.size(); i++) {
//                HabitType tempHt = typeList.get(i);
//                String stringTitle = tempHt.getTitle();
//                namesOfTypes.add(stringTitle);
//                Log.d("stringTitle", stringTitle);
//            }
//        }

        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namesOfTypes);
        adapter = new ArrayAdapter<HabitType>(this, R.layout.list_item, typeList);
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

