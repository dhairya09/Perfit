package com.example.dhairyapujara.perfit_submit;

import android.graphics.Bitmap;

/**
 * Created by Dhairya Pujara on 07-04-2017.
 */

public class ImageItem {
    private Bitmap image;


    public ImageItem(Bitmap image) {
        super();
        this.image = image;

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}