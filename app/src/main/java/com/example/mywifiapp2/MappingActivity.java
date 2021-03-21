package com.example.mywifiapp2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingActivity extends AppCompatActivity {
//
//    private WifiManager wifiManager;
//    private ListView listView;
//    private Button buttonaddPoint;
//    private EditText locationName;
//    private int size = 0;
//    private EditText pointX;
//    private EditText pointY;
//    private List<ScanResult> results;
//    private ArrayList<String> arrayList = new ArrayList<>();
//    private ArrayAdapter adapter;
//    private Mapping mapper = new Mapping();
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference();
    FirebaseUser user;
    DatabaseReference database;
    // create reference to firebase, create wifi header



    private StringBuilder stringBuilder = new StringBuilder();
    private TextView textViewWifiNetworks;
    private Button buttonClick;
    private List<ScanResult> scanList;
    private ListView listView_wifiList;
    private ArrayList<String> wifiList;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        textViewWifiNetworks = findViewById(R.id.txtWifiNetworks);
        listView_wifiList = findViewById(R.id.listView_wifi);
        wifiList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,wifiList);
        buttonClick = findViewById(R.id.button_click);

        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWifiNetworksList();


                textViewWifiNetworks.setVisibility(View.GONE);
                listView_wifiList.setAdapter(arrayAdapter);

            }
        });
//        buttonaddPoint = findViewById(R.id.addPoint);
//        pointX = findViewById(R.id.pointX);
//        pointY = findViewById(R.id.pointY);
//
//        listView = findViewById(R.id.wifiList);
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        if (!wifiManager.isWifiEnabled()) {
//            Toast.makeText(this, "Wifi is disabled", Toast.LENGTH_LONG).show();
//            wifiManager.setWifiEnabled(true);
//        }
//
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
//        listView.setAdapter(adapter);
//
//
//        scanWifi();
//
//        buttonaddPoint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mapper.add_data(new Point(Double.parseDouble(pointX.getText().toString()), Double.parseDouble(pointY.getText().toString())), results);
//                System.out.println("Data added!");
//            }
//        });

    }

//    private void scanWifi() {
//
//            // scan without any restrictions
//
//            arrayList.clear();
//            registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//
//            wifiManager.startScan();
//            Toast.makeText(this, "Scanning Wifi...", Toast.LENGTH_SHORT).show();
//
//
//
//    }
//
//    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            results = wifiManager.getScanResults();
//            unregisterReceiver(this);
//
//            for (ScanResult scanResult : results) {
//                arrayList.add(" BSSID: \n" + scanResult.BSSID);
//                adapter.notifyDataSetChanged();
//            }
//        }
//    };
private void getWifiNetworksList() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    final WifiManager wifiManager =
            (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    registerReceiver(new BroadcastReceiver() {

        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        public void onReceive(Context context, Intent intent) {
            //stringBuilder = new StringBuilder();
            scanList = wifiManager.getScanResults();
            System.out.println(scanList.size());
            wifiList.add("Number Of Wifi connections : " + " " + scanList.size() + "\n\n");
            for (int i = 0; i < scanList.size(); i++) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(new Integer(i + 1).toString() + ". ");
                stringBuilder.append(String.format("Name: %s,\nBSSID: %s,\nRSSI: %s\n",(scanList.get(i)).SSID,(scanList.get(i)).BSSID,(scanList.get(i)).level));
                wifiList.add(stringBuilder.toString());
                String Mac_address = scanList.get(i).BSSID;
                Integer rssi = scanList.get(i).level;
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild(Mac_address)){database.child("Scan").child("Nearby WIFI Data Values").child(Mac_address).setValue(rssi);}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //sb.append("\n\n");
            }
            arrayAdapter.notifyDataSetChanged();
            //textViewWifiNetworks.setText(stringBuilder);
            //System.out.println(stringBuilder);


        }

    }, filter);


    boolean startScan = wifiManager.startScan();
    if(!startScan){
        Toast.makeText(MappingActivity.this,"Please Enable Access of Location",Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(myIntent);
    }

}
}