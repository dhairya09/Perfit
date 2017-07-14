package com.example.dhairyapujara.perfit_submit.ImageAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Dhairya Pujara on 10-03-2017.
 */

public class ImageAdapter extends BaseAdapter
{
    private Context mContext;
    private Bitmap b1;
    private Bitmap b2;
    private Bitmap b3;
    private Bitmap b4;

    public Bitmap[] mThumbIds = {
           b1,b2,b3,b4

    };
    public ImageAdapter(Context c)
    {
        mContext = c;
    }

    public ImageAdapter(Bitmap b1,Bitmap b2,Bitmap b3,Bitmap b4)
    {
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
        this.b4 = b4;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(mContext);
        //imageView.setImageBitmap(mThumbIds[position]);
        imageView.setImageBitmap(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
        return imageView;

    }
}

