package org.artoolkit.ar6.artracking.models;

/**
 * Created by duda on 8.12.2017.
 */

public class MarkerG extends Marker{
    private Image image;

    @Override
    public String getImage() {
        return image.getData();
    }

    @Override
    public void setImage(String image) {
        this.image = new Image(image);
    }
}
