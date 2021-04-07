//package com.example.mywifiapp2;
//
//import androidx.annotation.NonNull;
//
//import com.example.mywifiapp2.wapcollector.Fingerprint;
//import com.example.mywifiapp2.wapcollector.XWiFi;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.GenericTypeIndicator;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class FingerprintsFirebaseHelper {
//    private FirebaseDatabase mDatabase;
//    private DatabaseReference fingerprintsRef;
//    private List<Fingerprint> fingerprintList = new ArrayList<>();
//
//    public interface DataStatus {
//        void DataIsLoaded(List<Fingerprint> fingerprints);
//        void DataIsInserted();
//        void DataIsUpdated();
//        void DataIsDeleted();
//    }
//
//    public FingerprintsFirebaseHelper(){
//        mDatabase = FirebaseDatabase.getInstance();
//        fingerprintsRef = mDatabase.getReference("Fingerprints");
//    }
//
//    public void readFingerprints(final DataStatus dataStatus){
//        fingerprintsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                fingerprintList.clear();
//                List<String> wifiData = new ArrayList<>();
//                for (DataSnapshot fingerprintX: dataSnapshot.getChildren()){ //Iterate thru 1, 2, 3, ... fingerprints
//                    Fingerprint readBackFingerprint = collectAllData(fingerprintX.getKey() ,fingerprintX.child("Coordinates"), fingerprintX.child("MAC Address")); //send 1 fingerprint thru for processing
//                    //Each of the Fingerprints will be reconstructed
//                    //And add into the Fingerprint list
//                    fingerprintList.add(readBackFingerprint);
//                }
//                dataStatus.DataIsLoaded(fingerprintList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    public Fingerprint collectAllData(String fingerprintNumber, DataSnapshot coordinates, DataSnapshot wifiData){
//        ArrayList<XWiFi> wifiDataList = new ArrayList<>();
//
//        String x = coordinates.child("x").getValue().toString();
//        String y = coordinates.child("y").getValue().toString();
//        Map<String,Object> wifiHashMap = (Map<String,Object>) wifiData.getValue();
//
//        for (Map.Entry<String, Object> entry : wifiHashMap.entrySet()){
//            String singleWifiStrength = (String) entry.getValue();
//            String singleMacAddress = entry.getKey();
//            XWiFi xWiFi = new XWiFi(singleMacAddress, Integer.valueOf(singleWifiStrength));
//            wifiDataList.add(xWiFi);
//        }
//        return new Fingerprint(fingerprintNumber, Float.parseFloat(x), Float.parseFloat(y), wifiDataList);
//    }
//}
