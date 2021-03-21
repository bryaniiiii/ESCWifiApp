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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonaddPoint;
    private EditText locationName;
    private int size = 0;
    private EditText pointX;
    private EditText pointY;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private Mapping mapper = new Mapping();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);
        buttonaddPoint = findViewById(R.id.addPoint);
        pointX = findViewById(R.id.pointX);
        pointY = findViewById(R.id.pointY);

        listView = findViewById(R.id.wifiList);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Wifi is disabled", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);


        scanWifi();

        buttonaddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapper.add_data(new Point(Double.parseDouble(pointX.getText().toString()), Double.parseDouble(pointY.getText().toString())), results);
                System.out.println("Data added!");
            }
        });

    }

    private void scanWifi() {

            // scan without any restrictions

            arrayList.clear();
            registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

            wifiManager.startScan();
            Toast.makeText(this, "Scanning Wifi...", Toast.LENGTH_SHORT).show();



    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult scanResult : results) {
                arrayList.add(" BSSID: \n" + scanResult.BSSID);
                adapter.notifyDataSetChanged();
            }
        }
    };

}