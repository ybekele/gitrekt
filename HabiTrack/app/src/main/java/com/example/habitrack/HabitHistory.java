package com.example.habitrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.SimpleTimeZone;

public class HabitHistory extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    public String[] teams = {"lakers", "man u", "man utd","orland", "orleans", "new orleans", "san fran"};

    ArrayList<ArrayList<String>> historyList = new ArrayList<ArrayList<String>>();
    Switch simpleSwitch = (Switch) findViewById(R.id.switch2);


    List<String> comments_list = new ArrayList<String>();
    List<String> habit_title = new ArrayList<String>();
    List<String> all_habit_titles = new ArrayList<String>();
    List<String> temp = new ArrayList<String>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);
        ListView lv = (ListView)findViewById(R.id.listView_history);


        getHistoryList();

        String[] teams = {"lakers", "man u", "man utd","orland", "orleans", "new orleans", "san fran"};

        //MAKE A TEMPORARY ARRAYLIST EQUAL THE ORIGINAL ARRAYLIST SO WHEN YOU YOU ARE LONGER
        //FILTERING YOUR SEARCH, YOU GET BACK YOUR ORIGINAL LISTVIEW

        if(temp.isEmpty()) {
            temp = all_habit_titles;
        }
        else{
            all_habit_titles = temp;
        }

        adapter = ((new ArrayAdapter<String>(HabitHistory.this, android.R.layout.simple_list_item_1, teams)));

        //adapter = new ArrayAdapter<String>(HabitHistory.this, android.R.layout.simple_list_item_1, arrayHabits);
        //adapter2 = new ArrayAdapter<String>(HabitHistory.this, android.R.layout.simple_list_item_1, arrayComments);

        //lv.setAdapter(adapter);
        //lv.setAdapter(adapter2);



        //Log.d("State", name_comments.toString());


        //adapter2 = ((new ArrayAdapter<String>(HabitHistory.this, android.R.layout.simple_list_item_1, R.id.text1)));



        lv.setAdapter(adapter);



        temp.clear();



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView)item.getActionView();
        Switch simpleSwitch = (Switch) findViewById(R.id.switch2);
        final Boolean switchState = simpleSwitch.isChecked();




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit (String s){
                Toast.makeText(getApplicationContext(), "Total number of items: " + s, Toast.LENGTH_LONG).show();
                /*if(switchstate ==true){
                    habiteventcontroller hec = new habiteventcontroller();
                    for(i=0;i<len(all_habitevents);i++){
                        string comment = i.getcomment();
                        string title = i.gettitle();
                        comments_list.add(comment);
                        habit_title.add(title);
                        teams.add("hello");

                        all_habit_titles.clear();


                        for(i=0;i<=comment.size();i++){
                            if(comments_list.get(i).startswith(s.touppercase()))
                            {
                             all_habit_titles.add(habit_title[i]);
                            }

                        }

                    }
                }*/


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {



                adapter.getFilter().filter(newText);







                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }













    private void getHistoryList(){
        HabitEventController hec = new HabitEventController();
        //HabitEventControlle
    }
}




/*
<ListView
        android:id="@+id/listView_history"
                android:layout_width="370dp"
                android:layout_height="259dp"
                android:layout_gravity="center_vertical|right"
                android:layout_marginTop="52dp"
                android:choiceMode="multipleChoice"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent" />
                */