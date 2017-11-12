package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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



        HabitTypeController htc = new HabitTypeController();


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
    } //HabitEventController controller = new HabitEventController();
    /*
    //todaysHabits = (ListView) findViewById(R.id.listView);

    //ArrayAdapter<HabitType> typeArrayAdapter = new ArrayAdapter<HabitType>(MainActivity.this, android.R.layout.simple_list_item_1);
    //getResources().getStringArray(R.array.forToday);
        //todaysHabits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(MainActivity.this, NewHabitEventActivity.class);
            //intent.putExtra("HabitTitle", todaysHabits.getItemAtPosition(i).toString());
            startActivity(intent);
        }

    });
        todaysHabits.setAdapter(typeArrayAdapter);
        */


    @Override
    protected void onStart() {
        super.onStart();
        displayNames = (ListView) findViewById(R.id.listView);
        HabitTypeStateManager.getHTStateManager().calculateHabitsForToday();
        ArrayList<HabitType> today = HabitTypeStateManager.HABITTYPES_FOR_TODAY;


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




    }

}