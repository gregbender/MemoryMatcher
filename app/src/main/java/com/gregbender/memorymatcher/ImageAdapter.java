package com.gregbender.memorymatcher;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.List;

class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Card> allcards;
    public ImageAdapter(Context c, List<Card> allcards) {
        mContext = c;
        this.allcards = allcards;
    }

    public int getCount() {
        return 9;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        return allcards.get(position);
    }
}
