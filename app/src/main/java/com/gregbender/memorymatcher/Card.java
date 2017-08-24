package com.gregbender.memorymatcher;

import android.content.Context;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import android.widget.GridView;
import android.widget.ImageButton;


import java.io.InputStream;
import java.net.URL;

/**
 * TODO: document your custom view class.
 */
public class Card extends ImageButton {
    boolean faceDown = true;
    boolean matched = false;
    Drawable image;
    Drawable imageBack;
    public int pairId;
    Card(Context view, int pairId) {
        super(view);
        this.setPadding(50, 50, 50, 50);
        this.setLayoutParams(new GridView.LayoutParams(300, 300));
        this.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        this.pairId = pairId;
        try {
            InputStream is = (InputStream) new URL("https://www.royalcanin.com/~/media/Royal-Canin/Product-Categories/dog-medium-landing-hero.ashx").getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            this.image = d;

            InputStream is2 = (InputStream) new URL("http://www.publicdomainpictures.net/pictures/40000/velka/suit-of-playing-cards-background.jpg").getContent();
            imageBack = Drawable.createFromStream(is2, "src name");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.flipDown();
    }
    public boolean isFaceUp() {
        return !faceDown;
    }
    public boolean isFaceDown() {
        return faceDown;
    }
    public void flip() {
        if (isFaceDown()) {
            flipUp();
        }
        else {
            flipDown();
        }

    }

    public void flipDown() {
        this.setImageDrawable(imageBack);
        this.faceDown = true;
    }

    public void flipUp() {
        this.faceDown = false;
        this.setImageDrawable(this.image);

    }
}