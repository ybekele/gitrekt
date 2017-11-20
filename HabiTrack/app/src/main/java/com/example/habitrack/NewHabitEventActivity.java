package com.example.habitrack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Handles the creation of New Habit Events
 */
public class NewHabitEventActivity extends AppCompatActivity {

    // declare components
    HabitEventController hec = new HabitEventController(this);
    HabitTypeController htc = new HabitTypeController(this);
    //CheckBox completed;
    TextView title;
    // Comment
    EditText comment;
    String commentString;
    Boolean isComment = Boolean.FALSE;
    //
    Button addImage;
    //ImageView eventImage;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    Button addEvent;
    int typeID = 0;
    // Photo
    Boolean isPhoto = Boolean.FALSE;
    Bitmap photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit_event);
        Intent intent = getIntent();
        //String titleString = intent.getStringExtra("HabitTitle");
        // Get incoming HT's ID
        final Integer htID = intent.getIntExtra("habitID", -1);
        // Get the interesting HT
        HabitType currHT = htc.getHabitType(htID);
        // Get interesting HT's attributes
        String titleString = currHT.getTitle();

        /* initialize views */
        // Set title
        //title = (TextView)findViewById(R.id.textView3);
        title = (TextView)findViewById(R.id.heTitleView);
        title.setText(titleString);

        //completed = (CheckBox)findViewById(R.id.checkBox);
        //title = (EditText) findViewById(R.id.editText7);

        // Get comment text box heCommenteditText
        // comment = (EditText) findViewById(R.id.editText6);
        comment = (EditText) findViewById(R.id.heCommentBox);
        // Image button
        addImage = (Button) findViewById(R.id.addImageGallery);
        //eventImage = (ImageView) findViewById(R.id.imageView);

        // Add event button & its functionality
        addEvent = (Button) findViewById(R.id.addEventButton);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verify comment
                commentString = comment.getText().toString();
                if(commentString.length() > 0){
                    isComment = Boolean.TRUE;
                }

                // call appropriate constructor for he
                if(!isComment && !isPhoto){
                    hec.createNewHabitEvent(htID);
                } else if (isComment && !isPhoto){
                    hec.createNewHabitEvent(htID, commentString);
                } else if (!isComment && isPhoto){
                    hec.createNewHabitEvent(htID, photo);
                } else {
                    Toast.makeText(NewHabitEventActivity.this, "Error Adding Habit Event",
                            Toast.LENGTH_SHORT).show();
                }
                finish();
//                if(isPhoto){
//                    hec.createNewHabitEvent(htID, photo);
//                }
//
//                if ((commentString.length() == 0) && (typeID != -1)) {
//                    Log.d("newID", Integer.toString(typeID));
//                    hec.createNewHabitEvent(htID);
//                }
                //}

                // exception, if user did leave a comment
                //if ((titleString.length() > 0) && (commentString.length() > 0) && (typeID != -1)) {
//                else if ((commentString.length() > 0) && (typeID != -1)) {
                    //habitEvent.createNewHabitEvent(typeID, commentString);
//                    hec.createNewHabitEvent(htID, commentString);
//                }

                /* Handles any error that may occur when trying to create a new habit event */
//                else {
//                    Log.d("typeID", Integer.toString(typeID));
                    //Log.d("title", titleString);

//                    Toast.makeText(NewHabitEventActivity.this, "Error Adding Habit Event", Toast.LENGTH_SHORT).show();
//                }

//                finish();
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
            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(imageUri);
                photo = BitmapFactory.decodeStream(inputStream);
                Integer imgSize = photo.getByteCount();
                if(imgSize > 65536){
                    Toast.makeText(NewHabitEventActivity.this, "Image is too large",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    isPhoto = Boolean.TRUE;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

}
