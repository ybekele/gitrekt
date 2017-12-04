package com.example.habitrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


/**
 *  Handles all the Activities on the Main Page once Logged in
 */

public class MainActivity extends AppCompatActivity {

    // bool var for connection status
    Boolean isConnected;
    // userID
    String currentUserID;

    // declare components
    Button createTypeButton;
    Button historybutton;
    Button allButton;
    Button logoutButton;
    Button socialButton;
    ImageButton refreshButton;
    private ListView displayNames;
    //private ArrayList<HabitType> today = new ArrayList<HabitType>();
    private ArrayList<HabitEvent> today = new ArrayList<HabitEvent>();
    //private ArrayAdapter<HabitType> adapter;
    private ArrayAdapter<HabitEvent> adapter;
    // Preliminary Setup
    // 1. Get the controllers
    HabitTypeController htc = new HabitTypeController(this);
    HabitEventController hec = new HabitEventController(this);
    // Get Filemanager
    FileManager fileManager = new FileManager(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get connection status
        isConnected = isOnline();

        createTypeButton = (Button) findViewById(R.id.createHabitButton);
        allButton = (Button) findViewById(R.id.allButton);
        historybutton = (Button) findViewById(R.id.historyButton);
        refreshButton = findViewById(R.id.refreshButton);
        logoutButton = (Button) findViewById(R.id.button10);
        socialButton = (Button) findViewById(R.id.button4);
        displayNames = (ListView) findViewById(R.id.listView);


        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()) {
            Toast.makeText(this, "Wifi", Toast.LENGTH_LONG).show();
        } else if (mobile.isConnectedOrConnecting ()) {
            Toast.makeText(this, "Mobile 3G ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
        }
// ------------------
        // Checks if app is in a logged in state. If not, goes to login page (SignupActivity)
        SharedPreferences loggedInPrefs = getApplicationContext().getSharedPreferences("loggedInStatus", MODE_PRIVATE);
        SharedPreferences loggedInUserID = getApplicationContext().getSharedPreferences("userID", MODE_PRIVATE);
        final SharedPreferences.Editor loggedInEditor = loggedInPrefs.edit();
        boolean isLoggedIn = loggedInPrefs.getBoolean("loggedIn", false);
        String liUserID = loggedInUserID.getString("userID", null);
        final Intent toLogIn = new Intent(getApplicationContext(), SignupActivity.class);
        if(!isLoggedIn) {
            startActivity(toLogIn);
        }


        //if Social button --> to social activity to interact with other participants
        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent social = new Intent(getApplicationContext(), SocialActivity.class);
                startActivity(social);
            }
        });

        //If logoutButton is clicked, change loggedIn to false and go to SignupAcitivty
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loggedInEditor.putBoolean("loggedIn", false);
                loggedInEditor.apply();
                startActivity(toLogIn);
            }
        });
// ------------------

        currentUserID = liUserID;



        // Handles if user pressed CREATE button , redirects to create a new habit type class
        createTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newType = new Intent(getApplicationContext(), NewHabitTypeActivity.class);
                newType.putExtra("connection", isConnected);
                newType.putExtra("currentUserID", currentUserID);
                startActivity(newType);
            }
        });

        // Handles if user pressed HISTORY button , redirects to history class
        historybutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent newType = new Intent(getApplicationContext(), HabitHistory.class);
                //Intent newType = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(newType);
            }
        });

        // Handles if user pressed ALL button, redirects to all habit types
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewAll = new Intent(getApplicationContext(), AllHabitTypesActivity.class);
                startActivity(viewAll);
            }
        });

        // Handles the pressing of Habits on the Main Activity to launch a new habit event
        displayNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //intent.putExtra("habitID", today.get(i).getID());
                Intent intent;
                Integer heID = today.get(i).getHabitEventID();
                String esID = today.get(i).getHabitTypeEsID();
                if(hec.getHabitEventIsEmpty(heID)) {
                    intent = new Intent(MainActivity.this, NewHabitEventActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, HabitEventDetailsActivity.class);
                }
                intent.putExtra("habitEventID", heID);
                intent.putExtra("habitTypeID", hec.getCorrespondingHabitTypeID(heID));
                intent.putExtra("connection", isConnected);
                intent.putExtra("habitTypeEsID", esID);
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnected = isOnline();
                hec.updateHabitEvents(isConnected, currentUserID);
                adapter.notifyDataSetChanged();

            }
        });

//  --------------------------------TEST COMMANDS BELOW -- MUST BE REMOVED ------------------
//        HabitEvent testHE = new HabitEvent(1000, 2000);
//        testHE.setUserID("test");
//        ElasticSearchController.AddHabitEvent addHabitEvent = new ElasticSearchController.AddHabitEvent();
//        addHabitEvent.execute(testHE);
//
//        ElasticSearchController.GetHabitEvent getHabitEvent = new ElasticSearchController.GetHabitEvent();
//        getHabitEvent.execute("user", "test");
//
//        ElasticSearchController.GetUser users = new ElasticSearchController.GetUser();
//        users.execute("");
//        ArrayList<HabitType> test = htc.getHabitTypeElasticSearch();
//
//        NewUser user = new NewUser("testUser3");
//        ElasticSearchController.AddNewUser addNewUser = new ElasticSearchController.AddNewUser();
//        addNewUser.execute(user);
//        NewUser user2 = new NewUser("testUser2");
//        addNewUser.execute(user2);
//        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
//        getUser.execute();
//        try {
//            ArrayList<NewUser> allUsers = getUser.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        // 2. load
//        htc.loadHTID();
//        hec.loadHEID();
//        hec.createNewHabitEvent(1000, isConnected, currentUserID);
//        hec.doOfflineTasks();
//        ArrayList<Integer> plan = new ArrayList<Integer>();
//        plan.add(Calendar.SUNDAY);
//        HabitTypeMetadata htmd1 = new HabitTypeMetadata(1001, "esid1");
//        htmd1.setTitle("title1");
//        htmd1.setScheduledToday(Boolean.TRUE);
//        htmd1.setSchedule(plan);
//        htmd1.setCanBeScheduled(Boolean.TRUE);
//
//        HabitTypeStateManager.getHTStateManager().addMetadata(htmd1);
//        fileManager.save(fileManager.HT_METADATA_MODE);
//  --------------------------------TEST COMMANDS ABOVE -- MUST BE REMOVED ------------------

        // 0. load IDs
        htc.loadHTID();
        hec.loadHEID();
        // 1. load HT Metadata
        fileManager.load(fileManager.HT_METADATA_MODE);
        // 2. load habit events for today
        fileManager.load(fileManager.TODAY_HE_MODE);
        // 2. calculate all the hts for today, using htmds
        htc.getHabitTypesForToday();
        // 3. calculate the hes for today, using the previously created htmdfortoday list
        htc.generateHabitsForToday(isConnected, currentUserID);
        //hec.generateEventsForToday(isConnected, currentUserID);
        // 3. Restore all HT and HE if saved
//        htc.loadFromFile();
//        hec.loadFromFile();
        // 4. Get Recent events and HabitTypes for today
//        htc.generateHabitsForToday(isConnected, currentUserID);
//        hec.updateRecentHabitEvents();

    }


    @Override
    protected void onStart() {
        super.onStart();
        today = hec.getHabitEventsForToday();

        adapter = new ArrayAdapter<HabitEvent>(this, R.layout.list_item, today);
        displayNames.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hec.updateHabitEvents(isConnected, currentUserID);

    }

    // Code taken from: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

