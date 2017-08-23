package com.gregbender.memorymatcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {

    int NUM_OF_IMAGES = 9;

    View.OnClickListener imageClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Card clickedCard = (Card)view;
            if (clickedCard.faceDown) {
                clickedCard.setImageResource(R.drawable.dog);
            }
            else {
                clickedCard.setImageResource(0);
            }
            clickedCard.faceDown = !clickedCard.faceDown;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < NUM_OF_IMAGES; i++) {
            Card newImageButton = new Card(this);
            newImageButton.setPadding(50, 50, 50, 50);
            newImageButton.setOnClickListener(imageClick);
            newImageButton.setActivated(false);
            ((LinearLayout)findViewById(R.id.mainlayout)).addView(newImageButton);

        }

    }



}
