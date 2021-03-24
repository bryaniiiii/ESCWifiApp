package com.example.mywifiapp2.wapcollector;

import java.util.ArrayList;

public class XWiFi {
    public String mac;
    public int rssi;
    public ArrayList<Integer> rssiList;

    XWiFi(){
        rssiList = new ArrayList<>();
    }

    XWiFi(String mac, int rssi) {
        this.mac = mac;
        this.rssi = rssi;
        rssiList = new ArrayList<>();
        rssiList.add(rssi);
    }

    public String toString() {
        return String.format("mac:%s rssi:%d", mac, rssi);
    }
}
