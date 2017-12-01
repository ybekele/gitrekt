package com.example.habitrack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class HabitEventDetailsActivity extends AppCompatActivity {
    private Integer htID = -1;
    private Integer heID = -1;
    Calendar date;
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
    Button shareButton;
    private String dateString;

    HabitTypeController htc = new HabitTypeController(this);
    HabitEventController he = new HabitEventController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_details);
        Intent intent = getIntent();
        htID = intent.getIntExtra("title", -1);
        heID = intent.getIntExtra("habitEventID", -1);
        typeName = he.getHabitEventTitle(heID);

        heDate = he.getHabitEvent(heID).getDate();
        heComment = he.getHabitEvent(heID).getComment();
        encodedImage = he.getHabitEventEncodedPhoto(heID);

        LatLng test = he.getHabitEventLocation(heID);

        shareButton = (Button) findViewById(R.id.shareBut);
     
        if(encodedImage != null) {
            ImageHandler.Decompressor decompressor = new ImageHandler.Decompressor();
            decompressor.execute(encodedImage);
            try {
                decodedImage = decompressor.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        eventName = he.getHabitEventTitle(heID);

        heTypeView = (TextView) findViewById(R.id.textView4);
        heDateView = (TextView) findViewById(R.id.eventDate);
        heCommentView = (EditText) findViewById(R.id.editText11);
        heImageView = (ImageView) findViewById(R.id.imageView2);
        editButton = (Button) findViewById(R.id.editHe);
        heImageView.setImageBitmap(decodedImage);

        Log.d("titles","this is the title "+ eventName);
        heTypeView.setText(typeName);

        final Calendar date = heDate;
        dateString = date.get(Calendar.MONTH) + "/" +
                date.get(Calendar.DATE) + "/" +
                date.get(Calendar.YEAR);

        heDateView.setText(dateString);
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


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myText = "I created a Habit event for " + typeName + " using the HabiTrack app with the comment " + heComment;
                String url = "http://www.twitter.com/intent/tweet?url=YOURURL&text=" + myText;
                Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                sharingIntent.setData(Uri.parse(url));
                startActivity(sharingIntent);
            }
        });








    }
}
