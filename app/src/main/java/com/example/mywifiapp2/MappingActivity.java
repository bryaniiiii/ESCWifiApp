package com.example.mywifiapp2;

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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MappingActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonScan;
    private EditText locationName;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);
        buttonScan = findViewById(R.id.scan);
        locationName = findViewById(R.id.locationName);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanWifi();
            }
        });
        listView = findViewById(R.id.wifiList);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Wifi is disabled", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);


        scanWifi();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                myRef.child("Location").setValue(String.valueOf(locationName.getText()));
                myRef.child("Location").child(String.valueOf(locationName.getText())).child(results.get(position).SSID).child("SSID").setValue(results.get(position).SSID);
                myRef.child("Location").child(String.valueOf(locationName.getText())).child(results.get(position).SSID).child("BSSID").setValue(results.get(position).BSSID);
                myRef.child("Location").child(String.valueOf(locationName.getText())).child(results.get(position).SSID).child("Description").setValue(results.get(position).capabilities);
                myRef.child("Location").child(String.valueOf(locationName.getText())).child(results.get(position).SSID).child("RSSI").setValue(results.get(position).level);



            }
        });


    }

    private void scanWifi() {
        if (!wifiManager.isScanThrottleEnabled()) {
            // scan without any restrictions

            arrayList.clear();
            registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

            wifiManager.startScan();
            Toast.makeText(this, "Scanning Wifi...", Toast.LENGTH_SHORT).show();

        }

    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult scanResult : results) {
                arrayList.add(" BSSID: \n" + scanResult.BSSID + "\n" + " SSID: \n" + scanResult.SSID + "\n" + " Description: \n" + scanResult.capabilities + "\n" + " RSSI: \n" + scanResult.level);
                adapter.notifyDataSetChanged();
            }
        }
    };

}


