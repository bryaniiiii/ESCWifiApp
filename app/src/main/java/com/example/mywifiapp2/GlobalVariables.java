package com.example.mywifiapp2;

import android.app.Application;

public class GlobalVariables extends Application {

    public static int fingerprint_count = 0;

    public int get_fingerprint_count() {
        return fingerprint_count;
    }

    public void set_fingerprint_count(int fingerprint_count) {
        this.fingerprint_count = fingerprint_count;
    }

    public void add_fingerprint_count() {
        this.fingerprint_count++;
    }
    public void clear_fingerprint_count() {
        this.fingerprint_count = 0;
    }

}