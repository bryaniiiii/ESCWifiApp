package com.example.mywifiapp2;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.mywifiapp2.wapcollector.Fingerprint;
import com.example.mywifiapp2.wapcollector.IndoorCollectManager;
import com.example.mywifiapp2.wapcollector.XWiFi;
import com.example.mywifiapp2.mapview.PinView;
import com.example.mywifiapp2.utils.Logger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CollectionMapActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int DEFAULT_TRAIN_TIME = 6000;
    private static final int REQUEST_PICK_MAP = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;

    private RadioButton typeRadioButton;
    private Button startButton;
    private Button viewFingerprints_button;

    //private EditText strideEdit;
    private EditText xEdit;
    private EditText yEdit;
    private TextView xTextView;
    private TextView yTextView;
    FirebaseUser user;
    DatabaseReference database;
    SubsamplingScaleImageView imageToMap;
    StorageReference storage;
    Uri mImageUri;
    private IndoorCollectManager indoorCollectManager;

    //private Uri mImageUri;
    private String URLlink;
    EditText URLEntry;
    SubsamplingScaleImageView PreviewImage;
    Button DeviceUpload, UrlUpload, ConfirmURL,ConfirmImage,ChangeImage, FirebaseUpload;

    static String IMAGE_URL = "IMAGE_URL";
    static String IMAGE_DEVICE = "IMAGE_DEVICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        mapView = findViewById(R.id.mapImageView);
        viewFingerprints_button = findViewById(R.id.viewfingerprints);
        startButton = findViewById(R.id.start_collect);

        typeRadioButton = findViewById(R.id.type);
        xEdit = findViewById(R.id.position_x);
        yEdit = findViewById(R.id.position_y);
        //strideEdit = findViewById(R.id.stride_length);
        // strideEdit.addTextChangedListener(textWatcher);
        xEdit.addTextChangedListener(textWatcher);
        yEdit.addTextChangedListener(textWatcher);
        FloatingActionButton FirebaseUpload = findViewById(R.id.download_map_fab);
        FirebaseUpload.bringToFront();

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        storage = FirebaseStorage.getInstance().getReference(user.getUid());

        viewFingerprints_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), ViewFingerPrintActivity.class);
                //startActivity(intent);
            }
        });

        FloatingActionButton upload_fab = (FloatingActionButton) findViewById(R.id.pick_map_button);
        upload_fab.bringToFront();
        upload_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMapFromPhone();
            }
        });

        FloatingActionButton delete_prev_fab = (FloatingActionButton) findViewById(R.id.delete_prev);
        delete_prev_fab.bringToFront();
        delete_prev_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        FloatingActionButton delete_all_fab = (FloatingActionButton) findViewById(R.id.clear_fingerprints);
        delete_all_fab.bringToFront();
        delete_all_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("Scan 1").removeValue();
            }
        });

        xTextView = findViewById(R.id.x_label);
        yTextView = findViewById(R.id.y_label);

        typeRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkFinishedPoints();
            }
        });

        requestPermissionBeforeStart();


        //Bryan read uploaded map from Firebase
        //==========================================================================================

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        storage = FirebaseStorage.getInstance().getReference(user.getUid()).child("Upload");

        DeviceUpload = findViewById(R.id.DeviceUpload);

        PreviewImage = (SubsamplingScaleImageView)findViewById(R.id.PreviewImage);
        ConfirmImage = findViewById(R.id.button_confirm);
        ChangeImage = findViewById(R.id.button_changeImage);

        //ConfirmImage.setVisibility(View.GONE);
        //ChangeImage.setVisibility(View.GONE);

//        DeviceUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFileChoser();
//                DeviceUpload.setVisibility(View.GONE);
//                FirebaseUpload.setVisibility(View.GONE);
//                ConfirmImage.setVisibility(View.VISIBLE);
//                ChangeImage.setVisibility(View.VISIBLE);
//            }
//        });

        FirebaseUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CollectionMapActivity.this, ChooseMapFromFirebase.class);
                startActivity(intent);
            }
        });

//        ChangeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DeviceUpload.setVisibility(View.VISIBLE);
//                FirebaseUpload.setVisibility(View.VISIBLE);
//                ConfirmImage.setVisibility(View.GONE);
//                ChangeImage.setVisibility(View.GONE);
//            }
//        });

//        ConfirmImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                storage = FirebaseStorage.getInstance().getReference(user.getUid()).child("Upload");
//                if(mImageUri!= null){storage.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(getApplicationContext(), "The Map has been uploaded into firebase", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(),"The Map has failed to be uploaded into firebase", Toast.LENGTH_SHORT).show();
//                    }
//                });}
//
//                Intent intent = new Intent(CollectionMapActivity.this, MappingActivity.class);
//
//                PreviewImage.buildDrawingCache();
//                Bitmap bitmap_device = PreviewImage.getDrawingCache();
//                ByteArrayOutputStream bs = new ByteArrayOutputStream();
//                bitmap_device.compress(Bitmap.CompressFormat.PNG,50,bs);
//                intent.putExtra(IMAGE_DEVICE,bs.toByteArray());
//                startActivity(intent);
//            }
//        });
    }

    private void openFileChoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    //To solve some phone's cannot get the position permission, result will invoke "onRequestPermissionsResult"
    public void requestPermissionBeforeStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                return;
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        } else {
            indoorCollectManager = new IndoorCollectManager(this);
            indoorCollectManager.startCollectService();

            if (!tryLoadOldMap())
                selectMapFromPhone();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    indoorCollectManager = new IndoorCollectManager(this);
                    indoorCollectManager.startCollectService();
                    if (!tryLoadOldMap())
                        selectMapFromPhone();
                } else {
                    // permission denied could not use this app
                    showToast("This app could not work without permission");
                    CollectionMapActivity.this.finish();
                }
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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
//            if (editable.hashCode() == strideEdit.getText().hashCode()) {
//                if (strideEdit.getText().toString().trim().equals("")) {
//                    showToast("Stride length can't be null");
//                } else {
//                    //float strideLength = Float.valueOf(strideEdit.getText().toString());
//                    //mapView.setStride(strideLength);
//                }
//            } else if (ifUserInput) {
//                if (!xEdit.getText().toString().equals("") && !yEdit.getText().toString().equals("")) {
//                    PointF p = new PointF(Float.valueOf(xEdit.getText().toString()),
//                            Float.valueOf(yEdit.getText().toString()));
//                    mapView.setCurrentTPosition(p);
//                }
//            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_collect:
                startCollectData();
                break;
            case R.id.pick_map_button:
                selectMapFromPhone();
                break;
            case R.id.help_data:
                showToast(getResources().getString(R.string.help_data));
                break;
        }
    }

    private void checkFinishedPoints() {
        String type = typeRadioButton.isChecked() ? "train" : "test";
        List<Fingerprint> fingerprints = new ArrayList<>();
        for (PointF p : Logger.getCollectedGrid(type)) {
            //int fingerprint_count = ((GlobalVariables) this.getApplication()).get_fingerprint_count();
            //fingerprints.add(new Fingerprint(String.valueOf(fingerprint_count), p.x, p.y));
            //((GlobalVariables) this.getApplication()).add_fingerprint_count();
            String name = String.valueOf(p.x) + "," + String.valueOf(p.y);
            fingerprints.add(new Fingerprint(name, p.x, p.y));
        }

        mapView.setFingerprintPoints(fingerprints);
        showToast(type + " points number: " + fingerprints.size());
    }

    public void selectMapFromPhone() {
        showToast("Please choose a image.");
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_PICK_MAP);  //one can be replaced with any action code
    }

    public void readMapFromFirebase(){
        showToast("Loading map from Firebase...");

        Bundle b = getIntent().getExtras();
        if ( b!= null){
            if (b.getByteArray("IMAGE_DEVICE")!=null){
                byte[] byteArray = b.getByteArray("IMAGE_DEVICE");

                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imageToMap.setImage(ImageSource.bitmap(bmp));}
            else {
                String newString = null;
                newString = b.getString("Imageselected");
                mImageUri = Uri.parse(newString);
                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(CollectionMapActivity.this).build();
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(config);
                imageLoader.loadImage(newString, new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        //super.onLoadingComplete(imageUri, view, loadedImage);
                        imageToMap.setImage(ImageSource.bitmap(loadedImage));
                    }
                });
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case REQUEST_PICK_MAP:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    setMapWidthHeight(selectedImage);
                } else {
                    this.finish();
                    showToast("You must pick map to train data.");
                }
                break;

            default:
                break;
        }
    }

    private void setMapWidthHeight(final Uri selectedImage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Map Info");

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_map, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        final EditText editMapWidth = view.findViewById(R.id.map_width);
        final EditText editMapHeight = view.findViewById(R.id.map_height);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float width = Float.valueOf(editMapWidth.getText().toString());
                float height = Float.valueOf(editMapHeight.getText().toString());
                saveMapInfo(selectedImage, width, height);
                loadMapImage(selectedImage, width, height);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void startCollectData() {
        setGestureDetectorListener(false);
        startButton.setClickable(false);

        startButton.setText(R.string.scanning);
        indoorCollectManager.setScanPeriodMills(DEFAULT_TRAIN_TIME);
        indoorCollectManager.registerCollectorListener(new IndoorCollectManager.CollectorListener() {
            @Override
            public void onCollectFinished(final ArrayList<XWiFi> wifiData) {
                indoorCollectManager.unregisterCollectorListener();
                indoorCollectManager.stopScan();
                setGestureDetectorListener(true);
                saveFingerprintData(wifiData);
            }
        });

        indoorCollectManager.startScan(true);
    }

    private void saveFingerprintData(final ArrayList<XWiFi> wifiData) {
        PointF pos = mapView.getCurrentTCoord();
        Point currentCoord = new Point((double)pos.x, (double)pos.y);

        //int fingerprint_count = ((GlobalVariables) this.getApplication()).get_fingerprint_count();

        String coordString = String.valueOf(pos.x).replace(".", ":") + "," + String.valueOf(pos.y).replace(".", ":");

        Fingerprint fingerprint = new Fingerprint(coordString, pos.x, pos.y, wifiData); //Create a new fingerprint and x and y coord
        //((GlobalVariables) this.getApplication()).add_fingerprint_count();
        fingerprint.wifiData = wifiData;

        String type = typeRadioButton.isChecked() ? "train" : "test";
        updateCollectStatus(fingerprint);
        Logger.saveFingerprintData(type, fingerprint);
        for (int i = 0; i < wifiData.size(); i++){
            int rssi = fingerprint.wifiData.get(i).getRssi();
            String mac_address = fingerprint.wifiData.get(i).mac;
            //String locationNameString = String.valueOf(((GlobalVariables) this.getApplication()).get_fingerprint_count());
            database.child("Scan 1").child(coordString).child(mac_address).setValue(rssi);
            //database.child("Scan 1").child(locationNameString).child("Coordinates").setValue(currentCoord);
        }
    }

    private void updateCollectStatus(final Fingerprint fingerprint) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setClickable(true);
                startButton.setText(getResources().getText(R.string.start));
                mapView.addFingerprintPoint(fingerprint);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
            xTextView.setText(String.format(Locale.ENGLISH, "X(max:%.1f)", width));
            yTextView.setText(String.format(Locale.ENGLISH, "Y(max:%.1f)", height));
            checkFinishedPoints();
            setGestureDetectorListener(true);
        }
    }

    private PinView mapView;
    private GestureDetector gestureDetector = null;

    private void setGestureDetectorListener(boolean enable) {
        if (!enable)
            mapView.setOnTouchListener(null);
        else {
            if (gestureDetector == null) {
                gestureDetector = new GestureDetector(CollectionMapActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mapView.isReady()) {
                            //mapView.moveBySingleTap(e); //Original
                            mapView.moveToPosition(e); //Added
                            setTextWithoutTriggerListener();
                        } else {
                            Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
            }

            mapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });
        }
    }

    private boolean ifUserInput = true;

    private void setTextWithoutTriggerListener() {
        ifUserInput = false;

        xEdit.setText(String.format(Locale.ENGLISH, "%.2f", mapView.getCurrentTCoord().x));
        yEdit.setText(String.format(Locale.ENGLISH, "%.2f", mapView.getCurrentTCoord().y));

        ifUserInput = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        indoorCollectManager.stopCollectService();
    }
}
