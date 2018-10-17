package org.artoolkit.ar6.artracking.models;

import android.graphics.Bitmap;
import android.net.wifi.ScanResult;

import org.artoolkit.ar6.artracking.helpers.Convert;

import java.util.ArrayList;

/**
 * Created by duda on 5.11.2017.
 */

public abstract class Marker {
    private String id;
    private String name;
    private String text;
    private ArrayList<Wifi> wifiList;

    protected Marker() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<Wifi> getWifiList() {
        return wifiList;
    }

    public void setWifiList(ArrayList<Wifi> wifiList) {
        this.wifiList = wifiList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getImage();

    public abstract void setImage(String image);

    public void setImage(Bitmap bitmap, int quality, int size) {
        setImage(Convert.toBase64(bitmap, quality, size));
    }
}
