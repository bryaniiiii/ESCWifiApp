//package com.example.mywifiapp2;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.wifi.ScanResult;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.example.mywifiapp2.wapcollector.Fingerprint;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ViewFingerPrintActivity extends AppCompatActivity {
//
//    private ListView listView;
//    private Button back;
//    private ArrayList<String> arrayList = new ArrayList<>();
//    private ArrayAdapter fingerprint_adapter;
//    private RecyclerView mRecyclerView;
//    private Mapping mapper = new Mapping();
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference fingerprintsRef = database.getReference("Fingerprints");
//    private ArrayList<Fingerprint> fingerprints_List = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.view_fingerprints);
//        mRecyclerView = (RecyclerView) findViewById(R.id.fingerprints_RecycleView);
//        new FingerprintsFirebaseHelper().readFingerprints(new FingerprintsFirebaseHelper.DataStatus() {
//            @Override
//            public void DataIsLoaded(List<Fingerprint> fingerprints) {
//                new RecyclerView_Config().setConfig(mRecyclerView, ViewFingerPrintActivity.this, fingerprints);
//            }
//
//            @Override
//            public void DataIsInserted() {
//            }
//
//            @Override
//            public void DataIsUpdated() {
//            }
//
//            @Override
//            public void DataIsDeleted() {
//            }
//        });
//    }
//
//
//}
