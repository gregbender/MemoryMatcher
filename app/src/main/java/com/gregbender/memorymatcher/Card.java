package com.gregbender.memorymatcher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.GridView;
import android.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;

public class Card extends ImageButton {

    Drawable image;
    Drawable imageBack;
    boolean faceDown = true;
    String letter;

    Card(Context view, String letter) {
        super(view);
        this.letter = letter;
        this.setOnClickListener(cardClickHandler);

        this.setPadding(50, 50, 50, 50);
        this.setLayoutParams(new GridView.LayoutParams(400, 400));
        this.setScaleType(ImageButton.ScaleType.CENTER_CROP);

        try {
            this.image = ContextCompat.getDrawable(this.getContext(), getResources().getIdentifier(letter, "drawable", "com.gregbender.memorymatcher"));
            this.imageBack = ContextCompat.getDrawable(this.getContext(), getResources().getIdentifier("dog", "drawable", "com.gregbender.memorymatcher"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.flipDown();
    }

    public void flip() {

        if (isFaceDown()) {
            flipUp();
        }
        else {
            flipDown();
        }

        Animation anim = new ScaleAnimation(
                0f, 1f, // Start and end values for the X axis scaling
                0f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(500);
        this.startAnimation(anim);
    }

    public void flipDown() {
        this.setImageDrawable(imageBack);
        this.faceDown = true;
    }

    public void flipUp() {
        EventBus.getDefault().post(new GameEvent(GameEventType.CARD_FLIP_UP));
        this.faceDown = false;
        this.setImageDrawable(this.image);
    }

    public boolean isFaceUp() {
        return !faceDown;
    }
    public boolean isFaceDown() {
        return faceDown;
    }

    View.OnClickListener cardClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EventBus.getDefault().post(new GameEvent(GameEventType.CARD_CLICK, (Card)view));
        }
    };
}