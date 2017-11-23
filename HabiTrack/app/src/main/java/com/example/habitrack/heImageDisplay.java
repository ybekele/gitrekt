package com.example.habitrack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Handles the Habit Event Image being displayed
 */
public class heImageDisplay extends AppCompatActivity {

    ImageView imgView;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_he_image_display);

        imgView = (ImageView) findViewById(R.id.heImageView);
        backButton = (Button) findViewById(R.id.endImageViewButton);



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
