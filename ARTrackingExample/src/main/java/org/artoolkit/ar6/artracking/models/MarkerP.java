package org.artoolkit.ar6.artracking.models;

import android.graphics.Bitmap;

import org.artoolkit.ar6.artracking.helpers.Convert;

/**
 * Created by duda on 8.12.2017.
 */

public class MarkerP extends Marker{
    private String image;

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public void setImage(String image) {
        this.image = image;
    }
}
