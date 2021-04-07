package com.example.mywifiapp2.wapcollector;

import java.util.ArrayList;

public class Fingerprint {
    public String name;
    public float x;
    public float y;
    public ArrayList<XWiFi> wifiData;

    public Fingerprint(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Fingerprint(String name, float x, float y, ArrayList<XWiFi> wifiData) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.wifiData = wifiData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public ArrayList<XWiFi> getWifiData() {
        return wifiData;
    }

    public void setWifiData(ArrayList<XWiFi> wifiData) {
        this.wifiData = wifiData;
    }
}
