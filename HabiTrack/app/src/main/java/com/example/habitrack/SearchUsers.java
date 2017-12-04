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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchUsers extends AppCompatActivity {

    ListView searchedUsersListView;
    Button searchUsersButton;
    private EditText searchedUsersEditText;
    private String searchText;
    private ArrayAdapter<String>  adapter;
    private ArrayList<NewUser> currentUsers = new ArrayList<>();
    private ArrayList<NewUser> matchedUsers = new ArrayList<>();
    private ArrayList<String> displayNames = new ArrayList<>();
    String liuName;
    NewUser loggedInUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        currentUsers = getCurrentUsers();
        Log.d("userslist", currentUsers.toString());
        for (int i = 0; i < currentUsers.size(); i++) {
            Log.d("us", currentUsers.get(i).getTitle());
        }
        searchedUsersListView = findViewById(R.id.searchUserListView);
        searchedUsersEditText = findViewById(R.id.searchUserEditText);
        searchUsersButton = findViewById(R.id.buttonsu);
        //searchedUsersAdapter = new ArrayAdapter<NewUser>(this, R.layout.list_item, R.id.searchUserListView, currentUsers);
        //searchedUsersListView.setAdapter(searchedUsersAdapter);

        searchUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchedUsers.clear();
                displayNames.clear();
                searchText = searchedUsersEditText.getText().toString();
                for (int i = 0; i < currentUsers.size(); i++) {
                    String currentUsersName = currentUsers.get(i).getTitle();
                    if (currentUsersName.toLowerCase().contains(searchText.toLowerCase())) {
                        matchedUsers.add(currentUsers.get(i));
                        displayNames.add(currentUsers.get(i).getTitle());

                    }
                }
                adapter.notifyDataSetChanged();
                Log.d("titles", displayNames.toString());
                Log.d("titlesEID", matchedUsers.toString());


            }
        });

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, displayNames);
        searchedUsersListView.setAdapter(adapter);

        searchedUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Integer index = i;
                loggedInUser = getCurrentUser(currentUsers);
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchUsers.this);
                builder.setMessage("Would you like to add this User as a friend?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (int j = 0; j < matchedUsers.size(); j++) {
                                    Log.d("matchedUser", matchedUsers.get(j).getTitle());
                                    Log.d("display", Integer.toString(index));
                                    Log.d("display", displayNames.get(index));
                                    Log.d("match", matchedUsers.get(j).getTitle());
                                    Log.d("matchdisplay", displayNames.get(index));

                                    if (matchedUsers.get(j).getTitle().equals(displayNames.get(index))) {
                                        if ((loggedInUser != null) && (loggedInUser != matchedUsers.get(j))) {
                                            matchedUsers.get(j).addRequest(loggedInUser);

                                            ElasticSearchController.AddNewUser addNewUser = new ElasticSearchController.AddNewUser();
                                            addNewUser.execute(matchedUsers.get(j));
                                            matchedUsers.get(j).followRequests.add(loggedInUser);
                                            //matchedUsers.get(j).requestsEID.add(loggedInUser.getTitle());
                                            ElasticSearchController.EditUser editUser = new ElasticSearchController.EditUser();
                                            editUser.execute(matchedUsers.get(j));

                                            Log.d("match", matchedUsers.get(j).toString());
                                            Log.d("matchLoggedIn", loggedInUser.toString());
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
        }




    @Override
    protected void onStart() {
        super.onStart();

        matchedUsers.clear();
        displayNames.clear();
        searchText = searchedUsersEditText.getText().toString();
        for (int i = 0; i < currentUsers.size(); i++) {
            String currentUsersName = currentUsers.get(i).getTitle();
            if (currentUsersName.contains(searchText)) {
                matchedUsers.add(currentUsers.get(i));
                displayNames.add(currentUsers.get(i).getTitle());
            }
        }
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
