package com.gregbender.memorymatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {


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

    List<Card> allCards = new ArrayList<Card>();
    boolean gameStarted = false;
    public int getCardsFaceUpCount() {
        int count = 0;
        for (Card i : allCards) {
            if (i.isFaceUp()) {
                count++;
            }
        }
        return count;
    }

    View.OnClickListener cardClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Card clickedCard = (Card)view;


            if (!gameStarted) {
                ((Chronometer)findViewById(R.id.chronometer3)).start();
            }


            MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.staple);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.release();
                }

            });
            mp.start();








            if (getCardsFaceUpCount() <= 1 && clickedCard.isFaceDown() && !clickedCard.matched) {
                clickedCard.flip();
                EventBus.getDefault().post(new MatchCheckEvent("1"));
            }
        }
    };

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(MatchCheckEvent event) {

        if (event.message.equals("1")) {
            if (getCardsFaceUpCount() == 2) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new MatchCheckEvent("2"));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkForMatch(MatchCheckEvent event) {

        if (event.message.equals("2")) {

            Card first = null;
            Card second = null;

                for (Card i : allCards) {
                    if (i.isFaceUp()) {

                        if (first == null) {
                            first = i;
                        }
                        else if (second == null) {
                            second = i;
                        }

                        i.flip();
                    }



                }

                if (first.pairId == second.pairId) {
                    first.setImageResource(0);
                    second.setImageResource(0);
                    first.matched = true;
                    second.matched = true;
                    MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.correct);
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            mp.release();
                        }

                    });
                    mp.start();
                }
                else {
                    first.flipDown();
                    second.flipDown();
                }


            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // create all the cards
        for (int i = 0; i < NUM_OF_PAIRS; i++) {

            Card newCardOne = new Card(this, i);
            newCardOne.setOnClickListener(cardClickHandler);
            allCards.add(newCardOne);

            Card newCardTwo = new Card(this, i);
            newCardTwo.setOnClickListener(cardClickHandler);
            allCards.add(newCardTwo);

        }
        // shuffle and randomly add them to the view
        Collections.shuffle(allCards);

        GridView gridview = (GridView) findViewById(R.id.mainlayout);
        gridview.setAdapter(new ImageAdapter(this, allCards));
    }
}