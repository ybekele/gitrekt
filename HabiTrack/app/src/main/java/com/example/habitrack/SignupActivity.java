package com.example.habitrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *
 * SignupActivity
 *
 * Version 1.00
 *
 * Created by hrai on 11/10/17.
 *
 */

public class SignupActivity extends AppCompatActivity {

    String userName, uID;
    Boolean userNameExists;
    EditText userInput;
    Button loginButton, signUpButton;
    ArrayList<NewUser> existingUserIDs = new ArrayList<>();
    //private int userIDKey = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userInput = (EditText) findViewById(R.id.editText);
        loginButton = (Button) findViewById(R.id.button5);
        signUpButton = (Button) findViewById(R.id.button9);


        ElasticSearchController esc = new ElasticSearchController();

        existingUserIDs = getUserIDs();

        /*
        * Shared Preference files to store values of the users ID as well as the logged in state of the app
        */
        final SharedPreferences sharedLoggedInStatus = getSharedPreferences("loggedInStatus", Context.MODE_PRIVATE);
        //final SharedPreferences sharedUserIDs = getSharedPreferences("userID", Context.MODE_PRIVATE);
        final SharedPreferences.Editor loggedInStatusEditor = sharedLoggedInStatus.edit();
        //final SharedPreferences.Editor userIDEditor = sharedUserIDs.edit();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Checks if a username key already exists in sharedUserIDs*/
                userName = userInput.getText().toString();
                for (int i = 0; i < existingUserIDs.size(); i++){
                    String uTitle = existingUserIDs.get(i).getTitle();
                    if(userName.equals(uTitle)){
                        userNameExists = true;
                    }
                }
                if (userNameExists) {
                    //uID = sharedUserIDs.getString("USERNAME", null);
                    /*Check to see if the username entered by user matches what was signed up.
                    * If it does, go to MainActivity
                    */
                    if(userName.equals(uID)){
                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                        loggedInStatusEditor.putBoolean("loggedIn", true);
                        loggedInStatusEditor.apply();
                        Intent loggedIn = new Intent (SignupActivity.this, MainActivity.class);
                        startActivity(loggedIn);
                    } else /*If username doesn't match what was saved, don't sign in.*/
                    {
                        Toast.makeText(getApplicationContext(), "Login Unsuccessful: Username incorrect. Username = " + uID, Toast.LENGTH_LONG).show();

                    }

                } else /*If no such key exists, user has not signed up yet*/
                {
                    Toast.makeText(getApplicationContext(), "Username not found. Please sign up.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        /*
         * Saves username entered by user into userIDs when Signup button is clicked and only if user has not signed up previously
         * */
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = userInput.getText().toString();
                for (int j = 0; j < existingUserIDs.size(); j++){
                    String uTitle = existingUserIDs.get(j).getTitle();
                    if(userName.equals(uTitle)){
                        userNameExists = true;
                    }
                }
                if(userNameExists) {
                    Toast.makeText(getApplicationContext(),"Account already exists. Please Login.", Toast.LENGTH_LONG).show();
                } else {
                    //userIDs.add(userName);
                    //userIDEditor.putString("USERNAME", userName);
                    //userIDEditor.apply();
                    uID = userName;
                    NewUser thisUser = new NewUser(userName);
                    ElasticSearchController.AddNewUser addNewUser = new ElasticSearchController.AddNewUser();
                    addNewUser.execute(thisUser);
                    Toast.makeText(getApplicationContext(),"Welcome to HabiTrack! You may now login.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public ArrayList<NewUser> getUserIDs(){

        ArrayList<NewUser> nu = new ArrayList<>();
        ElasticSearchController.GetUser getExistingUsers = new ElasticSearchController.GetUser();
        // may be changed
        getExistingUsers.execute("");
        try {
            nu = getExistingUsers.get();
        }
        catch (Exception e)
        {
            Log.i("Error","Failed to get existing user ID's");
        }

        return nu;
    }



}
