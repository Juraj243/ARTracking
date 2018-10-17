package org.artoolkit.ar6.artracking.models;

/**
 * Created by krist on 20-Dec-17.
 */

public class Image {
    private int type;
    private String data;

    public Image(String data) {
        this.type = 0;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
