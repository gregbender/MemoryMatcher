package com.gregbender.memorymatcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * TODO: document your custom view class.
 */
public class Card extends ImageButton {
    boolean faceDown = true;
    boolean matched = false;

    Card(Context view) {
        super(view);
    }

}
