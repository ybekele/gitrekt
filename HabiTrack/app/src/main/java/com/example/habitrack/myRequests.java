package com.example.habitrack;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class myRequests extends AppCompatActivity {
    String liuName;
    NewUser liu;
    ListView requestsListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<NewUser> requests = new ArrayList<>();
    private ArrayList<String> displayRequests = new ArrayList<>();
    ArrayList<NewUser> allUsers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);
        allUsers = getCurrentUsers();
        liu = getCurrentUser(allUsers);
        requests = liu.getFollowRequests();
        requestsListView = findViewById(R.id.requestList);

        for (Integer i = 0; i < requests.size(); i++) {
            displayRequests.add(requests.get(i).getTitle());
        }
        Log.d("requestsLog", requests.toString());
        Log.d("requestDisplay", displayRequests.toString());

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, displayRequests);
        requestsListView.setAdapter(adapter);

        requestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Integer index = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(myRequests.this);
                builder.setMessage("Would you like to allow this User to follow you?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (int j = 0; j < requests.size(); j++) {
                                    if (requests.get(j).getTitle().equals(displayRequests.get(index))) {
                                        if ((liu!= null) && (liu != requests.get(j))) {
                                            requests.get(j).addUsersFollowed(liu);
                                            requests.remove(j);
                                            ElasticSearchController.EditUser editUser = new ElasticSearchController.EditUser();
                                            editUser.execute(requests.get(j));
                                        }

                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        adapter.notifyDataSetChanged();


    }

    public ArrayList<NewUser> getCurrentUsers() {

        ArrayList<NewUser> eu = new ArrayList<>();
        ElasticSearchController.GetUser getExistingUsers = new ElasticSearchController.GetUser();
        // may be changed
        getExistingUsers.execute("");
        Log.d("entered", eu.toString());
        try {
            eu = getExistingUsers.get();
            Log.d("existing", eu.toString());
        } catch (Exception e) {
            Log.i("Error", "Failed to get existing user ID's");
        }
        return eu;
    }

    public NewUser getCurrentUser(ArrayList<NewUser> currentUsers) {
        SharedPreferences loggedInUserID = getApplicationContext().getSharedPreferences("userID", MODE_PRIVATE);
        liuName = loggedInUserID.getString("loggedInName", null);
        NewUser localUser = null;
        for (int i = 0; i < currentUsers.size(); i++) {
            if (currentUsers.get(i).getTitle().equals(liuName)) {
                localUser = currentUsers.get(i);
            }
        }
        return localUser;
    }
}

