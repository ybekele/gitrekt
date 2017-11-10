package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
Button createTypeButton;
ListView todaysHabits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //HabitEventController controller = new HabitEventController();
        //
        todaysHabits = (ListView) findViewById(R.id.listView);

        ArrayAdapter<HabitType> typeArrayAdapter = new ArrayAdapter<HabitType>(MainActivity.this, android.R.layout.simple_list_item_1);
        getResources().getStringArray(R.array.forToday);
        todaysHabits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NewHabitEventActivity.class);
                intent.putExtra("HabitTitle", todaysHabits.getItemAtPosition(i).toString());
                startActivity(intent);
            }

        });
        todaysHabits.setAdapter(typeArrayAdapter);
        createTypeButton = (Button) findViewById(R.id.button);
        createTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newType = new Intent(getApplicationContext(), NewHabitTypeActivity.class);
                startActivity(newType);


            }
        });


    }
}
