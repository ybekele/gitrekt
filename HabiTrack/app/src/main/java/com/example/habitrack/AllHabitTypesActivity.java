package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    private ArrayAdapter<HabitTypeMetadata> adapter;
    //private static ArrayList<HabitType> typeList = new ArrayList<HabitType>();
    private static ArrayList<HabitTypeMetadata> htMDList = new ArrayList<HabitTypeMetadata>();

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
                String esID = htMDList.get(i).getEsID();
                if(esID == null || esID == ""){
                    Toast.makeText(getApplicationContext(), "This habit type has not been saved.\nIt cannot be edited currently edited.", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra("esID", esID);
                    startActivity(intent);
                }
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

        htMDList = HabitTypeStateManager.getHTStateManager().getHtMetadataAll();
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namesOfTypes);
        adapter = new ArrayAdapter<HabitTypeMetadata>(this, R.layout.list_item, htMDList);
        allTypes.setAdapter(adapter);
    }

}

