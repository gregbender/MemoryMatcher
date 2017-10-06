package com.gregbender.memorymatcher;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gregbender.memorymatcher.databinding.ActivityMainBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MediaPlayer soundCardSelect;
    MediaPlayer soundCorrectSelection;
    MediaPlayer soundWrong;
    MediaPlayer soundSuccess;
    GameBoard gameBoard;
    String word;

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

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void gameBoardEventHandlerBackground(GameEvent event) {

        if (event.type == GameEventType.CARD_FLIP_UP) {
            soundCardSelect.start();
        }
        else if (event.type == GameEventType.FINISHED) {
            soundSuccess.start();
        }
        else if (event.type == GameEventType.BAD_MOVE) {
                try {
                    Thread.sleep(1000);
                    soundWrong.start();
                    Thread.sleep(1000);
                    EventBus.getDefault().post(new GameEvent(GameEventType.ALL_FACE_DOWN));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            EventBus.getDefault().post(new GameEvent(GameEventType.END_OF_TURN));
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gameBoardEventHandler(GameEvent event) {
//        if (event.type == GameEventType.CARD_FLIP_UP) {
//            ((Chronometer)this.findViewById(R.id.chronometer3)).start();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSounds();
        //this.selectWord();


        gameBoard = new GameBoard(this, "dog");
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setGameboard(gameBoard);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GridView gridview = (GridView) findViewById(R.id.mainlayout);
        gridview.setAdapter(new ImageAdapter(this, gameBoard.allCards));
    }

    private void selectWord() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://yrahgf4cqk.execute-api.us-east-2.amazonaws.com/Prod/words";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<String>>(){}.getType();
                        List<String> prodList = gson.fromJson(response, type);
                        word = prodList.get(0);

                        // print your List<Product>
                        System.out.println("!!!!!!!!!!!!!!!!!!!prodList: " + prodList);
                        // Display the first 500 characters of the response string.
                        //      mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
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

        soundWrong = MediaPlayer.create(MainActivity.this, R.raw.wrong);
        soundWrong.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer soundWrong) {
                // soundWrong.release();
            }
        });

        soundSuccess = MediaPlayer.create(MainActivity.this, R.raw.success);
        soundSuccess.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer soundSuccess) {
                // soundWrong.release();
            }
        });
    }

}