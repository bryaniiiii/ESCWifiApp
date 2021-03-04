package com.example.mywifiapp2;

public class WifiAP {

    private String SSID;
    private String BSSID;
    private String description;
    private double level;

    public WifiAP(String SSID, String BSSID, String description, double level) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.level = level;
        this.description = description;
    }

    public String getSSID() {
        return SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public String getDescription() {
        return description;
    }

    public double getLevel() {
        return level;
    }
}
