package com.example.habitrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    // Create variables
    private ListView eventsView;
    private ArrayAdapter<HabitEvent> adapter;
    private ArrayList<HabitEvent> eventsList;
    private EditText searchBox;
    private CheckBox commentCheckBox;
    private ImageButton searchButton;

    // Get controllers
    HabitTypeController htc = new HabitTypeController(this);
    HabitEventController hec = new HabitEventController(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Attach variables to view elements
        eventsView = (ListView) findViewById(R.id.historyListView);
        searchBox = (EditText) findViewById(R.id.searchBox);
        commentCheckBox = (CheckBox) findViewById(R.id.commentsCheckBox);
        searchButton = (ImageButton) findViewById(R.id.searchButton);

        eventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HistoryActivity.this, HabitEventDetailsActivity.class);
                intent.putExtra("habitEventID", eventsList.get(i).getHabitEventID());
                intent.putExtra("habitTypeID", eventsList.get(i).getHabitTypeID());
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        eventsList = HabitEventStateManager.getHEStateManager().getAllHabitEvents();

        adapter = new ArrayAdapter<HabitEvent>(this, R.layout.list_item, eventsList);
        eventsView.setAdapter(adapter);

    }
}
