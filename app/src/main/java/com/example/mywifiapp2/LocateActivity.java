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
    private HashMap<String,Integer> bssid_rssi;
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
    private List<String> bssid;
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
                    bssid = new ArrayList<>();
                    bssid_rssi = new HashMap<>();
                    // from the scanResults obtained, generate hashmap and List
                    for(ScanResult ap: scanList){
                        if(20<Math.abs(ap.level)&&Math.abs(ap.level)<100){
                            bssid_rssi.put(ap.BSSID, ap.level);
                            bssid.add(ap.BSSID);
                        }
                    }
                    Mapping.get_data_for_testing(bssid, new Mapping.OnDataLoadedListener() {
                        @Override
                        public Point onFinishLoading(HashMap<Point, HashMap> dataSet) {
                            HashMap<Point, HashMap> dataSet2 = dataSet;
                            if (!dataSet2.isEmpty()) {
                                System.out.println("9999922222" + dataSet2);// Need to retrieve data from database first!! (either done here or in the testingMode activity)
                                ArrayList<Point> positionSet = new ArrayList<Point>(dataSet2.keySet());
                                int num_of_positions = dataSet2.size();
                                int num_of_bssids = bssid.size();

                                float nearest1 = Float.MAX_VALUE;
                                float nearest2 = Float.MAX_VALUE;

                                Point nearest1_position = new Point(0, 0);
                                Point nearest2_position = new Point(0, 0);

                                int sum = 0;
//        if (dataSet.isEmpty()) {
//            return new Point(-1, -1);
//        } else {
                                for (int i = 0; i < num_of_positions; i++) {
                                    for (int j = 0; j < num_of_bssids; j++) {
                                        sum += Math.pow((((Long) dataSet2.get(positionSet.get(i)).get(bssid.get(i))).intValue() - bssid_rssi.get(bssid.get(j))), 2);
                                    }
                                    float dev = (float) Math.sqrt(sum);
                                    if (dev < nearest1) {
                                        nearest1 = dev;
                                        nearest1_position = positionSet.get(i);
                                    } else if (dev < nearest2) {
                                        nearest2 = dev;
                                        nearest2_position = positionSet.get(i);
                                    }
                                    sum = 0;
                                }
//        }


                                double x = nearest1_position.getX() * nearest1 / (nearest1 + nearest2) +
                                        nearest2_position.getX() * nearest2 / (nearest1 + nearest2);
                                double y = nearest1_position.getY() * nearest1 / (nearest1 + nearest2) +
                                        nearest2_position.getY() * nearest2 / (nearest1 + nearest2);
                                currentPosition.setText(new Point(x, y).toString());
                                return new  Point(x, y);

                            }
                            else{
                                System.out.println("9992 coord not calculated"  );
                                currentPosition.setText(new Point(-1, -1).toString());
                                return new Point(-1, -1);

                            }
                        }

                        @Override
                        public Point onCancelled(DatabaseError error) {
                            System.out.println("9992 database error");
                            currentPosition.setText(new Point(-1, -1).toString());
                            return new Point(-1, -1);

                        }
                    });
//                    System.out.println("92383312 result calculated" + result);
//                    if(result.getX()<0 || result.getY()<0){
//                        Toast.makeText(LocateActivity.this, "Not able to make prediction for current position",Toast.LENGTH_LONG).show();
//                    }
//                    else{
//                        currentPosition.setText(result.toString());
//                    }
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



