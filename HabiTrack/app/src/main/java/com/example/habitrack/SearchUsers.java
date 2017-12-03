package com.example.habitrack;

import android.os.Bundle;
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
    private EditText searchedUsersEditText;
    private ListView searchedUsersListView;
    private Button searchUsersButton;
    private String searchText;
    private ArrayAdapter<String>  adapter;
    private ArrayList<NewUser> currentUsers = new ArrayList<>();
    private ArrayList<NewUser> matchedUsers = new ArrayList<>();
    private ArrayList<String> displayNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        currentUsers = getCurrentUsers();
        searchedUsersListView = findViewById(R.id.searchUserListView);
        searchedUsersEditText = findViewById(R.id.searchUserEditText);
        searchUsersButton = findViewById(R.id.buttonsu);
        searchedUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
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
                    if (currentUsersName.contains(searchText)) {
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
                for (int j = 0; j < matchedUsers.size(); j++){
                    if (matchedUsers.get(j).getTitle().equals(displayNames.get(i))) {
                        //matchedUsers.get(j).addRequest(PUT THE USERS OWN ID HERE );
                        
                    }
                }
//                AlertDialog.Builder adb = new AlertDialog.Builder(
//                        MainActivity.this);
            }
        });



    }

    @Override
    protected void onStart() {
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
        super.onStart();
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

}
