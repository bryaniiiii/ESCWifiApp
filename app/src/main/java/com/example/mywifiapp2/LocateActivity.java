package com.example.mywifiapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.mywifiapp2.mapview.PinView;
import com.example.mywifiapp2.utils.Logger;
import com.example.mywifiapp2.wapcollector.Fingerprint;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private FloatingActionButton locateMe;
    private PinView mapView;
    Uri mImageUri;

    private FloatingActionButton scanMe;
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
    FloatingActionButton FirebaseUpload;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localization_activity);
        mapView = (PinView) findViewById(R.id.mapImageView);
//        buttonScan = findViewById(R.id.scan);
//        locationName = findViewById(R.id.locationName);
//        buttonScan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scanWifi();
//            }
//        });
//        listView = findViewById(R.id.wifiList);



        FirebaseUpload = (FloatingActionButton) findViewById(R.id.load_localised_map_from_firebase);
        currentPosition = findViewById(R.id.currentLocation);
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
//        listView.setAdapter(adapter);
        scanMe = findViewById(R.id.scan);

        locateMe = findViewById(R.id.locateme);

        scanMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Scanning WiFi Fingerprint...");
                scanWifi();
            }
        });


        FirebaseUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocateActivity.this, TestingLoadSavedMap.class);
                startActivity(intent);
            }
        });

        locateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(scanList != null){
                    System.out.println("scanlist is not empty");
                    // instantiate Test Object

                    // using predict() knn to predict where user is

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
                                ArrayList<Point> position_list = new ArrayList<Point>(dataSet2.keySet());


                                float nearest1 = Float.MAX_VALUE;
                                float nearest2 = Float.MAX_VALUE;
                                float nearest3 = Float.MAX_VALUE;
                                Point nearest1_position = new Point(0,0);
                                Point nearest2_position = new Point(0,0);
                                Point nearest3_position = new Point(0,0);

                                int sum = 0;
                                for(Point point: position_list){
                                    for(String j:bssid){
                                        if(dataSet2.get(point).containsKey(j)){
                                            sum += Math.pow(((Long)dataSet2.get(point).get(j)).intValue()- bssid_rssi.get(j),2);
                                        }
                                        else{
                                            sum += Math.pow(bssid_rssi.get(j),2);
                                        }
                                    }
                                    float dev = (float) Math.sqrt(sum);
                                    float temp;
                                    Point temp_point;
                                    Point dev_point = point;
                                    if (dev < nearest1) {
                                        temp = nearest1;
                                        temp_point = nearest1_position;
                                        nearest1 = dev;
                                        nearest1_position = dev_point;
                                        dev = temp;
                                        dev_point = temp_point;
                                    }
                                    if(dev < nearest2){
                                        temp = nearest2;
                                        temp_point = nearest2_position;
                                        nearest2 = dev;
                                        nearest2_position = dev_point;
                                        dev = temp;
                                        dev_point = temp_point;
                                    }
                                    if(dev < nearest3){
                                        temp = nearest3;
                                        temp_point = nearest3_position;
                                        nearest3 = dev;
                                        nearest3_position = dev_point;
                                    }
                                    sum = 0;
                                }

                                double x,y;

                                if (nearest1 == 0 && nearest2 == 0 && nearest3 ==0) {
                                    x = (nearest1_position.getX() + nearest2_position.getX()+nearest3_position.getX())/3;
                                    y = (nearest1_position.getY() + nearest2_position.getY()+nearest3_position.getY())/3 ;
                                } else {
                                    x = nearest1_position.getX() * (nearest2 + nearest3) / (nearest1 + nearest2 + nearest3) +
                                            nearest2_position.getX() * (nearest1 + nearest3) / (nearest1 + nearest2 + nearest3) +
                                            nearest3_position.getX() * (nearest1 + nearest2) / (nearest1 + nearest2 + nearest3);
                                    y = nearest1_position.getY() * (nearest2 + nearest3) / (nearest1 + nearest2 + nearest3) +
                                            nearest2_position.getY() * (nearest1 + nearest3) / (nearest1 + nearest2 + nearest3) +
                                            nearest3_position.getY() * (nearest1 + nearest2) / (nearest1 + nearest2 + nearest3);
                                }
                                System.out.println(x + "," + y);
                                mapView.setCurrentTPosition(new PointF((float)x,(float)y));
                                return new Point(x,y);
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
                }
            }
        });

        Bundle b = getIntent().getExtras();
        if ( b!= null){
//            newString = (String) b.get("Imageselected");
//            mImageUri = Uri.parse(newString);
            if (b.getByteArray("IMAGE_DEVICE")!=null){ //From device
                byte[] byteArray = b.getByteArray("IMAGE_DEVICE");
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                mapView.setImage(ImageSource.bitmap(bmp));}
            
            else { //Loading the getExtras from Firebase
                String newString = null;
                newString = b.getString("Imageselected");
                mImageUri = Uri.parse(newString);
                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(LocateActivity.this).build();
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(config);
                imageLoader.loadImage(newString,new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        //super.onLoadingComplete(imageUri, view, loadedImage);
                        mapView.setImage(ImageSource.bitmap(loadedImage));
                        mapView.initialCoordManager(100, 200);
                        mapView.setCurrentTPosition(new PointF(50.0f, 300.0f)); //initial
                        checkFinishedPoints();
                    }
                });
            }
        }
    }

    private static final String MAP_INFO = "map_info";
    private static final String MAP_PATH = "map_path";
    private static final String MAP_WIDTH = "width";
    private static final String MAP_height = "height";

    private boolean tryLoadOldMap() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAP_INFO, MODE_PRIVATE);
        String path = sharedPreferences.getString(MAP_PATH, null);
        if (path == null)
            return false;
        else {
            float width = sharedPreferences.getFloat(MAP_WIDTH, 0);
            float height = sharedPreferences.getFloat(MAP_height, 0);
            loadMapImage(Uri.fromFile(new File(path)), width, height);
            return true;
        }
    }

    private void saveMapInfo(Uri uri, float width, float height) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAP_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MAP_PATH, getRealPathFromURI(uri));
        editor.putFloat(MAP_WIDTH, width);
        editor.putFloat(MAP_height, height);
        editor.apply();
    }

    //Pick picture from gallery is a uri not the actual file.
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }

        return result;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void checkFinishedPoints() {
        String type = "train";
        List<Fingerprint> fingerprints = new ArrayList<>();
        for (PointF p : Logger.getCollectedGrid(type)) {
            //int fingerprint_count = ((GlobalVariables) this.getApplication()).get_fingerprint_count();
            //fingerprints.add(new Fingerprint(String.valueOf(fingerprint_count), p.x, p.y));
            //((GlobalVariables) this.getApplication()).add_fingerprint_count();
            String name = String.valueOf(p.x) + "," + String.valueOf(p.y);
            fingerprints.add(new Fingerprint(name, p.x, p.y));
        }
        mapView.setFingerprintPoints(fingerprints);
    }

    private void scanWifi() {
        // perform 1 scan
        WifiScan wifiScan = new WifiScan(getApplicationContext(),LocateActivity.this);
        // store results of scan into wifiScan.scanList
        wifiScan.getWifiNetworksList();
        // store this list into scanList
        scanList = wifiScan.getScanList();
        System.out.println("Scan Finished");
        showToast("Scanning WiFi data...");
        System.out.println(scanList);
    }

    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        SubsamplingScaleImageView imageView;
        URL url;
        public LoadImage(SubsamplingScaleImageView PreviewImage){
            this.imageView = PreviewImage;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String URLlink = strings[0];
            Bitmap bitmap = null;
            try {
                if(!URLlink.isEmpty()){
                    url = new URL(URLlink);
                }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                //InputStream inputStream = new java.net.URL(URLlink).openStream();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mapView.setImage(ImageSource.bitmap(bitmap));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            mapView.setImage(ImageSource.uri(mImageUri));
            mapView.setCurrentTPosition(new PointF(1.0f, 1.0f)); //initial current position

        }
    }

    private void loadMapImage(final Uri selectedImage, float width, float height) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            mapView.setImage(ImageSource.bitmap(bitmap));
            mapView.initialCoordManager(width, height);
            mapView.setCurrentTPosition(new PointF(1.0f, 1.0f)); //initial current position
        }
    }
}



