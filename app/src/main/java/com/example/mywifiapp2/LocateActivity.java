package com.example.mywifiapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LocateActivity extends AppCompatActivity {
    private ArrayList<WifiAP> listofWifiAP = new ArrayList<>();
    private ArrayList<String> arrayListLocation = new ArrayList<>();
    private ArrayList<String> arrayListWifiAPs = new ArrayList<>();
    private ArrayList<String> arrayListBSSID = new ArrayList<>();
    private ArrayList<Object> arrayListofstuff = new ArrayList<>();
    private WifiManager wifiManager;
    private Testing locator;
    private TextView currentPosition;
    private Point currentCoordinates;
    private Button locateMe;
    private Button scanMe;
    private ListView listView;
    private Button buttonScan;
    private EditText locationName;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<WifiAP> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private List<ScanResult> scanList;
    //    private HashMap locationFirebase;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users").child(user.getUid()).child("Scan 1");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);
//        buttonScan = findViewById(R.id.scan);
//        locationName = findViewById(R.id.locationName);
//        buttonScan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scanWifi();
//            }
//        });
//        listView = findViewById(R.id.wifiList);

        currentPosition = findViewById(R.id.currentLocation);


//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
//        listView.setAdapter(adapter);
//
//
        scanMe = findViewById(R.id.scanme);

        locateMe = findViewById(R.id.locateme);

        scanMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanWifi();
            }
        });

        locateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(scanList != null){
                    System.out.println("scanlist is not empty");
                    // instantiate Test Object
                    Testing testing = new Testing(scanList);
                    // using predict() knn to predict where user is
                    Point result = null;
                    try {
                        result = testing.predict();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("92383312 result calculated");
                    if(result.getX()<0 || result.getY()<0){
                        Toast.makeText(LocateActivity.this, "Not able to make prediction for current position",Toast.LENGTH_LONG).show();
                    }
                    else{
                        currentPosition.setText(result.toString());
                    }
                }



            }
        });











    }


    private void scanWifi() {
        // perform 1 scan
        WifiScan wifiScan = new WifiScan(getApplicationContext(),LocateActivity.this);
        // store results of scan into wifiScan.scanList
        wifiScan.getWifiNetworksList();
        // store this list into scanList
        scanList = wifiScan.getScanList();
        System.out.println("Scan Finished");
        System.out.println(scanList);

    }



}



