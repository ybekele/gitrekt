package com.example.habitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HabitHistory extends AppCompatActivity {
    private ArrayList<HabitType> today = new ArrayList<HabitType>();
    HabitTypeController hc = new HabitTypeController(this);
    HabitEventController hec = new HabitEventController(this);

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    Button start_over;

    //Intializing arrays

    ArrayList<ArrayList<String>> historyList = new ArrayList<ArrayList<String>>();
    List<String> comments_list = new ArrayList<String>();
    List<String> habit_title = new ArrayList<String>();
    List<String> all_habit_titles = new ArrayList<String>();
    List<HabitType> the_titles = new ArrayList<HabitType>();
    List<String> temp = new ArrayList<String>();

    int i;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);
        ListView lv = (ListView)findViewById(R.id.listView_history);
        final HabitEventController hc = new HabitEventController(this);
        HabitTypeController ht = new HabitTypeController(this);



        //Log.d("hello","came back in here my dude");

        //Filling array with habit event titles
        for (i = 0; i < hc.getAllHabitEvent().size(); i++) {
            String title = hc.getAllHabitEvent().get(i).getTitle();
            all_habit_titles.add(title);
        }


        the_titles = ht.getAllHabitTypes();





        start_over = (Button) findViewById(R.id.reset);







        //Log.d("hello","histhee"+all_habit_titles.get(0).toString());


        //MAKE A TEMPORARY ARRAYLIST EQUAL THE ORIGINAL ARRAYLIST SO WHEN YOU YOU ARE LONGER
        //FILTERING YOUR SEARCH, YOU GET BACK YOUR ORIGINAL LISTVIEW

       /* if(temp.isEmpty()) {
            Log.d("temp","temp is empty");
            temp = all_habit_titles;
        }
        else{
            Log.d("temp","temp IS NOT empty");
            all_habit_titles = temp;
        }
*/
        adapter = ((new ArrayAdapter<String>(HabitHistory.this, android.R.layout.simple_list_item_1, all_habit_titles)));

        //adapter = new ArrayAdapter<String>(HabitHistory.this, android.R.layout.simple_list_item_1, arrayHabits);
        //adapter2 = new ArrayAdapter<String>(HabitHistory.this, android.R.layout.simple_list_item_1, arrayComments);

        //lv.setAdapter(adapter);
        //lv.setAdapter(adapter2);



        //Log.d("State", name_comments.toString());


        //adapter2 = ((new ArrayAdapter<String>(HabitHistory.this, android.R.layout.simple_list_item_1, R.id.text1)));



        lv.setAdapter(adapter);

        //Following resets listview to all habit event titles
        start_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabitHistory attempt = new HabitHistory();
                all_habit_titles.clear();
                all_habit_titles = attempt.update_array();
                //adapter.notifyDataSetChanged();
                adapter = ((new ArrayAdapter<String>(HabitHistory.this, android.R.layout.simple_list_item_1, all_habit_titles)));
                ListView lv = (ListView)findViewById(R.id.listView_history);
                lv.setAdapter(adapter);
                Log.d("last","this is uptop"+all_habit_titles.get(0));

            }
        });


        // Handles the pressing of Habits on the Main Activity to launch a new habit event
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HabitHistory.this, NewHabitEventActivity.class);
                //intent.putExtra("HabitTitle", displayNames.getItemAtPosition(i).toString());
                //Log.d("position", displayNames.getItemAtPosition(i).toString());
                intent.putExtra("habitID", the_titles.get(i).getID());
                //intent.putExtra("comment", hc.getAllHabitEvent().get(i).getComment() );
                intent.putExtra("title", hc.getAllHabitEvent().get(i).getTitle());
                startActivity(intent);
            }
        });

        //temp.clear();



    }



    //Handles the filtering option for the listview
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView)item.getActionView();
        //Switch simpleSwitch = (Switch) findViewById(R.id.switch2);
        //final Boolean switchState = simpleSwitch.isChecked();
        Log.d("checking","iterated over here");






        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                //Onquerytextsubmit filters by comments in habit event
                //following updates all_habit_titles accordingly

                @Override
                public boolean onQueryTextSubmit (String s){
                    Switch simpleSwitch = (Switch) findViewById(R.id.switch2);
                    final Boolean switchState = simpleSwitch.isChecked();

                    Toast.makeText(getApplicationContext(), "Total number of items: " + s, Toast.LENGTH_LONG).show();
                    Log.d("query2","this is the switch state= "+switchState);
                    if(switchState){
                        Log.d("query","came into the query");
                        all_habit_titles.clear();
                        HabitEventController hc = new HabitEventController(getApplicationContext());
                        for(i=0;i<hc.getAllHabitEvent().size();i++) {
                            String comment = hc.getAllHabitEvent().get(i).getComment();
                            if(comment == null){
                                comment = " ";
                            }
                            String title = hc.getAllHabitEvent().get(i).getTitle();
                            comments_list.add(comment);
                            habit_title.add(title);

                            //Log.d("query3", "the comment = " + comments_list.get(i) + "------" + s + "-------" + comments_list.get(i).startsWith(s.toLowerCase()));


                        }

                        for(i=0;i<comments_list.size();i++){
                            if(comments_list.get(i).startsWith(s.toUpperCase()) || comments_list.get(i).startsWith(s.toLowerCase()))
                                {
                                    if(!(all_habit_titles.contains(habit_title.get(i)))) {
                                        all_habit_titles.add(habit_title.get(i));
                                    }
                                }

                            }


                }

                adapter.notifyDataSetChanged();

                    Log.d("checking","we came to the othrt");

                    //HabitHistory attempt = new HabitHistory();
                    //attempt.update_array();


                return false;

            }

            //onQueryTextChange filters by habit titles

            @Override
            public boolean onQueryTextChange(String newText) {
                Switch simpleSwitch = (Switch) findViewById(R.id.switch2);
                final Boolean switchState = simpleSwitch.isChecked();

                if(switchState!=true) {
                    adapter.getFilter().filter(newText);
                }
                Log.d("checking","we came to the super");

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    //Following method fills an array with all habit titles and returns it.

    public ArrayList<String> update_array() {
        Log.d("checking","we updating array");
        HabitTypeController hc = new HabitTypeController(this);
        ArrayList<String> all_titles = new ArrayList<String>();
        all_titles.clear();
        for (i = 0; i < hc.getAllHabitTypes().size(); i++) {
            String title = hc.getAllHabitTypes().get(i).getTitle();
            Log.d("checking","this is the title"+title);
            all_titles.add(title);

        }

        Log.d("last","this is in the method"+all_titles.get(0));
        return(all_titles);

    }


}
