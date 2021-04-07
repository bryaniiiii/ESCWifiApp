package com.example.mywifiapp2.wapcollector;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

public class XWiFi{
    public String mac;
    public int rssi;
    public ArrayList<Integer> rssiList;

    public XWiFi(){
        rssiList = new ArrayList<>();
    }

    public XWiFi(String mac, int rssi) {
        this.mac = mac;
        this.rssi = rssi;
        rssiList = new ArrayList<>();
        rssiList.add(rssi);
    }

    public int getRssi() {
        return rssi;
    }
}
