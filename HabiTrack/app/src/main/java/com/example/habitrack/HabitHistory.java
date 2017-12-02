package com.example.habitrack;

import android.accessibilityservice.FingerprintGestureController;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HabitHistory extends AppCompatActivity {
    private ArrayList<HabitType> today = new ArrayList<HabitType>();
    HabitTypeController hc = new HabitTypeController(this);
    HabitEventController hec = new HabitEventController(this);

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    Button start_over;
    Button show_map;
    Integer changing = 1;


    //Intializing arrays

    ArrayList<ArrayList<String>> historyList = new ArrayList<ArrayList<String>>();
    List<String> comments_list = new ArrayList<String>();
    List<String> habit_title = new ArrayList<String>();
    List<String> all_habit_titles = new ArrayList<String>();
    List<HabitEvent> the_titles = new ArrayList<HabitEvent>();
    List<String> temp = new ArrayList<String>();
    ArrayList<String> listview_tracker = new ArrayList<String>();
    List<Integer> temp_tracker = new ArrayList<Integer>();

    int i;
    int a;

    @Override
    /**
     * Oncreate, the habit events are obtained and put on the listview
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);
        ListView lv = (ListView)findViewById(R.id.listView_history);
        final HabitEventController hc = new HabitEventController(this);
        HabitTypeController ht = new HabitTypeController(this);


        //Filling array with habit event titles
        for (i = 0; i < hc.getAllHabitEvent().size(); i++) {
            if(hc.getAllHabitEvent().get(i).getEmpty() == Boolean.FALSE) {
                String title = hc.getAllHabitEvent().get(i).getTitle();
                all_habit_titles.add(title);
                listview_tracker.add(hc.getAllHabitEvent().get(i).getHabitEventID().toString());
            }
        }

        Collections.reverse(all_habit_titles);
        Collections.reverse(listview_tracker);


        the_titles = hc.getAllHabitEvent();

        Collections.reverse(the_titles);





        start_over = (Button) findViewById(R.id.reset);
        show_map = (Button) findViewById(R.id.mapid);






        adapter = ((new ArrayAdapter<String>(HabitHistory.this, android.R.layout.simple_list_item_1, all_habit_titles)));



        lv.setAdapter(adapter);

        show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * If the user wants to reset the listview back to its original view, the following
             * function handles that
             */
            public void onClick(View view) {
                Intent intent = new Intent(HabitHistory.this, MapsActivity2.class);



                if(listview_tracker==null){
                    Log.d("ooo", "MarkersList" + "uuuuuuu");
                }
               // Log.d("ooo", "MarkersList" + listview_tracker.get(0).toString());
                intent.putStringArrayListExtra("tracker",listview_tracker);


                startActivity(intent);

            }
        });







        //Following resets listview to all habit event titles
        start_over.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * If the user wants to reset the listview back to its original view, the following
             * function handles that
             */
            public void onClick(View view) {
                HabitHistory attempt = new HabitHistory();
                all_habit_titles.clear();
                listview_tracker.clear();
                all_habit_titles = attempt.update_array();


                for (i = 0; i < hc.getAllHabitEvent().size(); i++) {
                    listview_tracker.add(hc.getAllHabitEvent().get(i).getHabitEventID().toString());
                }
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
            /**
             * if the user clicks on an item in the listview, ids, and the comments from the
             * habit events are passed to newhabiteventactivity
             */
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Intent intent = new Intent(HabitHistory.this, NewHabitEventActivity.class);
                Intent intent = new Intent(HabitHistory.this, HabitEventDetailsActivity.class);
                //intent.putExtra("HabitTitle", displayNames.getItemAtPosition(i).toString());
                //Log.d("position", displayNames.getItemAtPosition(i).toString());

                if(the_titles.get(i).getEmpty()== Boolean.FALSE) {
                    intent.putExtra("habitEventID", the_titles.get(i).getHabitEventID());
                    Log.d("last", "this is the id " + the_titles.get(i).getHabitEventID());
                    intent.putExtra("cm", hc.getAllHabitEvent().get(i).getComment());
                    intent.putExtra("title", hc.getAllHabitEvent().get(i).getTitle());
                }

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
                /**
                 * if the user clicks on the switch, and enters a search followed by pressing enter,
                 * their search is filtered based on the habit event comments;handled by the following
                 * If the switch isn't clicked, their search is filtered habit title, and is updated
                 * a letter at at time.
                 */
                public boolean onQueryTextSubmit (String s){
                    Switch simpleSwitch = (Switch) findViewById(R.id.switch2);
                    final Boolean switchState = simpleSwitch.isChecked();


                    Log.d("query2","this is the switch state= "+switchState);
                    if(switchState){
                        Log.d("query","came into the query");
                        all_habit_titles.clear();
                        listview_tracker.clear();
                        HabitEventController hc = new HabitEventController(getApplicationContext());
                        for(i=0;i<hc.getAllHabitEvent().size();i++) {
                            String comment = hc.getAllHabitEvent().get(i).getComment();
                            if(comment == null){
                                comment = " ";
                            }
                            String title = hc.getAllHabitEvent().get(i).getTitle();
                            comments_list.add(comment);
                            habit_title.add(title);
                            temp_tracker.add(hc.getAllHabitEvent().get(i).getHabitEventID());

                            Log.d("query3", "the comment = " + comments_list.get(i) + "------" + s + "-------" + comments_list.get(i).startsWith(s.toLowerCase()));


                        }

                        for(i=0;i<comments_list.size();i++){
                            if(comments_list.get(i).startsWith(s.toUpperCase()) || comments_list.get(i).startsWith(s.toLowerCase()))
                                {
                                    if(!(all_habit_titles.contains(habit_title.get(i)))) {
                                        all_habit_titles.add(habit_title.get(i));
                                        listview_tracker.add(temp_tracker.get(i).toString());
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
                HabitEventController hc = new HabitEventController(getApplicationContext());
                listview_tracker.clear();
                Switch simpleSwitch = (Switch) findViewById(R.id.switch2);
                final Boolean switchState = simpleSwitch.isChecked();

                if(switchState!=true) {
                    adapter.getFilter().filter(newText);
                }
                Log.d("checking","we came to the super");

                for(int i=0;i<all_habit_titles.size();i++){
                    if(all_habit_titles.get(i).startsWith(newText)){
                        listview_tracker.add(hc.getAllHabitEvent().get(i).getHabitEventID().toString());
                    }
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    //Following method fills an array with all habit titles and returns it.

    /**
     * if a user calls this function, the listview is cleared, and updated with
     * the names of all habit events created.
     * this is usually accessed when a user filters their search, and wants to return back
     * to their original listview.
     * @return all_titles(names from each habit event created by a user)
     */
    public ArrayList<String> update_array() {
        Log.d("checking","we updating array");
        HabitEventController hc = new HabitEventController(this);
        ArrayList<String> all_titles = new ArrayList<String>();
        all_titles.clear();
        for (i = 0; i < hc.getAllHabitEvent().size(); i++) {
            String title = hc.getAllHabitEvent().get(i).getTitle();
            Log.d("checking","this is the title"+title);
            all_titles.add(title);

        }

        Log.d("last","this is in the method"+all_titles.get(0));
        return(all_titles);

    }


}
