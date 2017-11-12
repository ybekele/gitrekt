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

public class NewHabitEventActivity extends AppCompatActivity {
    HabitEventController habitEvent;

    CheckBox completed;
    TextView title;
    EditText comment;
    Button addImage;
    ImageView eventImage;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    Button addEvent;
    int typeID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit_event);
        Intent intent = getIntent();
        String titleString = intent.getStringExtra(" HabitTitle");
        title = (TextView)findViewById(R.id.textView3);
        title.setText(titleString);
        completed = (CheckBox)findViewById(R.id.checkBox);
        title = (EditText) findViewById(R.id.editText7);
        comment = (EditText) findViewById(R.id.editText6);
        addImage = (Button) findViewById(R.id.button8);
        eventImage = (ImageView) findViewById(R.id.imageView);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });



        //get habit ID
        HabitTypeStateManager.getHTStateManager().calculateHabitsForToday();
        final ArrayList<HabitType> today = HabitTypeStateManager.getHabitTypesForToday();
        HabitType iterater = null;
        for (int j = 0; j < today.size(); j++)
            iterater = today.get(j);
            if ((iterater != null) && (iterater.getTitle().equals(titleString))) {
                Log.d("iterator", iterater.toString());
                typeID = iterater.getID() ;
            }




        addEvent = (Button) findViewById(R.id.button7);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addingEvent = new Intent(getApplicationContext(), MainActivity.class);
                String commentString = comment.getText().toString();
                String titleString = title.getText().toString();
                if ((titleString.length() > 0) && (commentString.length() == 0) && (typeID != -1)) {
                    habitEvent.createNewHabitEvent(typeID);
                }
                if ((titleString.length() > 0) && (commentString.length() > 0) && (typeID != -1)) {
                    habitEvent.createNewHabitEvent(typeID, commentString);
                }

                else {
                    Log.d("typeID", Integer.toString(typeID));
                    Log.d("title", titleString);

                    Toast.makeText(NewHabitEventActivity.this, "Error Adding Habit Event", Toast.LENGTH_SHORT).show();
                }
                //habitEvent.setTitle(titleString);
                //habitEvent.(commentString);
                startActivity(addingEvent);
            }
        });
    }

    //https://www.youtube.com/watch?v=OPnusBmMQTw
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            eventImage.setImageURI(imageUri);

        }

    }

}
