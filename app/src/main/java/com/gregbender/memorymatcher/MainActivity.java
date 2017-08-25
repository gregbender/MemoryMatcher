package com.gregbender.memorymatcher;

import android.media.MediaPlayer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.GridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MediaPlayer soundCardSelect;
    MediaPlayer soundCorrectSelection;
    GameBoard gameBoard;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    int NUM_OF_PAIRS = 5;




    View.OnClickListener cardClickHandler = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Card clickedCard = (Card)view;
            soundCardSelect.start();


            if (!gameBoard.isGameStarted()) {
                ((Chronometer)findViewById(R.id.chronometer3)).start();
            }

            if (gameBoard.getCardsFaceUpCount() <= 1 && clickedCard.isFaceDown() && !clickedCard.matched) {
                clickedCard.flip();
                EventBus.getDefault().post(new MatchCheckEvent("1"));
            }
        }
    };


    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(MatchCheckEvent event) {

        if (event.message.equals("1")) {
            if (gameBoard.getCardsFaceUpCount() == 2) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new MatchCheckEvent("2"));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSounds();
        gameBoard = new GameBoard();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // create all the cards
        for (int i = 0; i < NUM_OF_PAIRS; i++) {

            Card newCardOne = new Card(this, i);
            newCardOne.setOnClickListener(cardClickHandler);
            gameBoard.addCard(newCardOne);

            Card newCardTwo = new Card(this, i);
            newCardTwo.setOnClickListener(cardClickHandler);
            gameBoard.addCard(newCardTwo);

        }
        // shuffle and randomly add them to the view

        gameBoard.shuffleUpAndStart();
        GridView gridview = (GridView) findViewById(R.id.mainlayout);
        gridview.setAdapter(new ImageAdapter(this, gameBoard.allCards));
    }


    private void setupSounds() {

        soundCorrectSelection = MediaPlayer.create(MainActivity.this, R.raw.correct);
        soundCorrectSelection.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer soundCorrectSelection) {
                //soundCorrectSelection.release();
            }
        });

        soundCardSelect = MediaPlayer.create(MainActivity.this, R.raw.select);
        soundCardSelect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer soundCardSelect) {
               // soundCardSelect.release();
            }
        });
    }
}