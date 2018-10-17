package org.artoolkit.ar6.artracking.models;

import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by duda on 8.12.2017.
 */

public class Wifi implements Parcelable {
    private String bssid;
    private String ssid;
    private int level;

    public Wifi(ScanResult scanResult) {
        bssid = scanResult.BSSID;
        ssid = scanResult.SSID;
        level = scanResult.level;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bssid);
        dest.writeString(ssid);
        dest.writeInt(level);
    }

    private Wifi(Parcel in) {
        bssid = in.readString();
        ssid = in.readString();
        level = in.readInt();
    }

    public static final Parcelable.Creator<Wifi> CREATOR = new Parcelable.Creator<Wifi>() {
        public Wifi createFromParcel(Parcel in) {
            return new Wifi(in);
        }

        public Wifi[] newArray(int size) {
            return new Wifi[size];
        }
    };
}
