package com.example.mywifiapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
        //scanMe = findViewById(R.id.scanme);

        locateMe = findViewById(R.id.locateme);
//        scanMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scanWifi();
//            }
//        });


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

                scanWifi();
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
                                mapView.setCurrentTPosition(new PointF((float)x, (float)y)); //JJ added in to try
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
                    }
                });
            }
        }
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



