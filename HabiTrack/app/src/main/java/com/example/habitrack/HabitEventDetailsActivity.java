package com.example.habitrack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class HabitEventDetailsActivity extends AppCompatActivity {
    private Integer htID = -1;
    private Integer heID = -1;
    Button editButton;
    TextView heTypeView;
    TextView heDateView;
    EditText heCommentView;
    ImageView heImageView;
    String typeName;
    String eventName;
    String heComment;
    Calendar heDate;
    String encodedImage;
    Bitmap decodedImage;
    String newComment;
    HabitTypeController htc = new HabitTypeController(this);
    HabitEventController he = new HabitEventController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_details);
        Intent intent = getIntent();
        htID = intent.getIntExtra("habitTypeID", -1);
        heID = intent.getIntExtra("habitEventID", -1);
        typeName = htc.getHabitType(htID).getTitle();
        heDate = he.getHabitEvent(heID).getDate();
        heComment = he.getHabitEvent(heID).getComment();
        encodedImage = he.getHabitEventEncodedPhoto(heID);
        ImageHandler.Decompressor decompressor = new ImageHandler.Decompressor();
        decompressor.execute(encodedImage);
        try {
            decodedImage = decompressor.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        eventName = he.getHabitEventTitle(heID);

        heTypeView = (TextView) findViewById(R.id.textView4);
        heDateView = (TextView) findViewById(R.id.eventDate);
        heCommentView = (EditText) findViewById(R.id.editText11);
        heImageView = (ImageView) findViewById(R.id.imageView2);
        editButton = (Button) findViewById(R.id.editHe);
        heImageView.setImageBitmap(decodedImage);

        Log.d("titles","this is the title "+ eventName);
        heTypeView.setText(eventName);
        heDateView.setText(heDate.toString());
        heCommentView.setText(heComment);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newComment = heCommentView.getText().toString();
                he.getHabitEvent(heID).setComment(newComment);
                Toast.makeText(getApplicationContext(), "Applying Edits",
                        Toast.LENGTH_SHORT).show();
            }
        });








    }
}
