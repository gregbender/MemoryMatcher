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
        this.setLayoutParams(new GridView.LayoutParams(400, 400));
        this.setScaleType(ImageButton.ScaleType.CENTER_CROP);

        this.pairId = pairId;
        try {

            String url = "https://images-na.ssl-images-amazon.com/images/I/61AT2giKTVL._SL256_.jpg";

            if (pairId == 0) {
                url = "https://cdn1.medicalnewstoday.com/content/images/headlines/309/309358/dog-with-stethoscope-around-neck.jpg";
            }
            else if (pairId == 1) {
                url = "http://pa1.narvii.com/6044/6e2dbfab0bc1482d3e367991a0342c5b82250e81_hq.gif";
            }
            else if (pairId == 2) {
                url = "https://www.iconexperience.com/_img/v_collection_png/256x256/shadow/dog.png";

            }
            else if (pairId ==3) {
                url = "https://is3-ssl.mzstatic.com/image/thumb/Purple49/v4/f0/5f/3f/f05f3f7c-95f3-ac9f-44bd-f9df54183863/source/256x256bb.jpg";
            }
            else if (pairId == 4) {
                url = "https://static-s.aa-cdn.net/img/ios/1034197315/98546f507caa5a015484f78d8c8b6ea1";
            }
            else if (pairId == 5) {
                url = "https://pbs.twimg.com/profile_images/621531738078969856/KePrZ2Rk_400x400.jpg";
            }


            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name" + pairId);
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