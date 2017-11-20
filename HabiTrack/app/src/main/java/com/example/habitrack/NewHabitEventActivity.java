package com.example.habitrack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

/**
 * Handles the creation of New Habit Events
 */
public class NewHabitEventActivity extends AppCompatActivity {

    // declare components
    HabitEventController hec = new HabitEventController(this);
    HabitTypeController htc = new HabitTypeController(this);
    CheckBox completed;
    TextView title;
    EditText comment;
    Button addImage;
    ImageView eventImage;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    Button addEvent;
    int typeID = 0;
    Button map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit_event);
        Intent intent = getIntent();
        //String titleString = intent.getStringExtra("HabitTitle");
        // Get incoming HT's ID
        final Integer htID = intent.getIntExtra("habitID", -1);
        ArrayList<HabitType> namesList;
        // Get the interesting HT
        HabitType currHT = htc.getHabitType(htID);
        // Get interesting HT's attributes
        String titleString = currHT.getTitle();


        /* initialize views */
        title = (TextView)findViewById(R.id.textView3);
        title.setText(titleString);
        Log.d("workingTitle", titleString);
        completed = (CheckBox)findViewById(R.id.checkBox);
        title = (EditText) findViewById(R.id.editText7);
        comment = (EditText) findViewById(R.id.editText6);
        addImage = (Button) findViewById(R.id.button8);
        eventImage = (ImageView) findViewById(R.id.imageView);
        addEvent = (Button) findViewById(R.id.button7);
        map = (Button) findViewById(R.id.mapButton) ;
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        //get habit IDs for today from HabitTypeStateManager
        //HabitTypeStateManager.getHTStateManager().calculateHabitsForToday();
        //final ArrayList<HabitType> today = HabitTypeStateManager.getHTStateManager().getHabitTypesForToday();
//        HabitType iterater = null;
//        Log.d("stringTitle", titleString);
//
//        for (int j = 0; j < today.size(); j++)
//            iterater = today.get(j);
//            Log.d("iterator", iterater.getTitle());
//            if (titleString.equals(iterater.getTitle())) {
//                Log.d("iterator2", iterater.toString());
//                Log.d("ID", iterater.getID().toString());
//                typeID = iterater.getID() ;
//            }

        final String titleEvent = intent.getStringExtra("title");
        /*final String commentEvent = intent.getStringExtra("comment");
        if (commentEvent != null) {
            comment.setText(commentEvent);
        }
*/
        if (titleEvent != null) {
            title.setText(titleEvent);
        }



        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoMap = new Intent(NewHabitEventActivity.this, MapsActivity.class);
                NewHabitEventActivity.this.startActivity(gotoMap);
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addingEvent = new Intent(getApplicationContext(), MainActivity.class);
                String commentString = comment.getText().toString();
                //String titleString = title.getText().toString();

                // if user did NOT leave a comment
                //if ((titleString.length() > 0) && (commentString.length() == 0) && (typeID != -1)) {
                if ((commentString.length() == 0) && (typeID != -1)) {
                    Log.d("newID", Integer.toString(typeID));
                    //habitEvent.createNewHabitEvent(typeID);
                    hec.createNewHabitEvent(htID);
                }
                //}

                // exception, if user did leave a comment
                //if ((titleString.length() > 0) && (commentString.length() > 0) && (typeID != -1)) {
                else if ((commentString.length() > 0) && (typeID != -1)) {
                    //habitEvent.createNewHabitEvent(typeID, commentString);
                    hec.createNewHabitEvent(htID, commentString);
                }

                /* Handles any error that may occur when trying to create a new habit event */
                else {
                    Log.d("typeID", Integer.toString(typeID));
                    //Log.d("title", titleString);

                    Toast.makeText(NewHabitEventActivity.this, "Error Adding Habit Event", Toast.LENGTH_SHORT).show();
                }
                finish();
                //startActivity(addingEvent);
            }
        });
    }


    /*
    Opens the gallery in phone to select a photo
    Proper way to do it derived from this video: https://www.youtube.com/watch?v=OPnusBmMQTw
     */
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    /*
    Gets the data of the photo user has picked from gallery
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            eventImage.setImageURI(imageUri);

        }

    }

}
