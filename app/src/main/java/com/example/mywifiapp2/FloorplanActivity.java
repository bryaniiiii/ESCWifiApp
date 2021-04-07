//package com.example.mywifiapp2;
//
//import android.app.DownloadManager;
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptor;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.GroundOverlay;
//import com.google.android.gms.maps.model.GroundOverlayOptions;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static android.os.Environment.DIRECTORY_DOWNLOADS;
//
//// Add ground overlay to map
//public class FloorplanActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener, OnSeekBarChangeListener, OnMapReadyCallback, GoogleMap.OnGroundOverlayClickListener {
//
//    private static final int TRANSPARENCY_MAX = 100;
//
//    private static final int BEARING_MOVE = 17;
//
//    // This is the exact latlng for SUTD (Don't change it)
//    private static LatLng SUTD = new LatLng(1.34, 103.962);
//
//    //private static final LatLng NEAR_SUTD = new LatLng(SUTD.latitude - 0.001, SUTD.longitude - 0.025);
//
//    private final List<BitmapDescriptor> images = new ArrayList<BitmapDescriptor>();
//
//    private List<LatLng> saveMarker = new ArrayList<LatLng>();
//
//    private GroundOverlay groundOverlay;
//
//    private GroundOverlay groundOverlayRotated;
//
//    private SeekBar transparencyBar;
//
//    private SeekBar rotationBar;
//
//    private int currentEntry = 0;
//
//    FirebaseStorage firebaseStorage;
//    StorageReference storageReference;
//    StorageReference ref;
//
//    Button Clear;
//    Button Download;
//
//    private GoogleMap map;
//    private TextView tapTextView;
//
//    // TODO: Somehow tie this reference point to the user's selected LatLng point where the map is placed
//    // This is our reference point on the map
//    Point reference = new Point(0,0);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_floorplan);
//
//        // Create transparency bar to adjust opacity of floor plan
//        transparencyBar = findViewById(R.id.transparencySeekBar);
//        transparencyBar.setMax(TRANSPARENCY_MAX);
//        transparencyBar.setProgress(0);
//
//        // Create rotation bar
//        rotationBar = findViewById(R.id.rotationSeekBar);
//        rotationBar.setMax(BEARING_MOVE);
//        rotationBar.setProgress(0);
//
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap map) {
//        this.map = map;
//
//        // Register a listener to respond to clicks on GroundOverlays.
//        this.map.setOnGroundOverlayClickListener(this);
//
//        // Register a listener to respond to clicks on the Map.
//        this.map.setOnMapClickListener(this);
//
//        Clear = findViewById(R.id.ClearImages);
//        Download = findViewById(R.id.downloadImage);
//        tapTextView = findViewById(R.id.tap_text);
//
//        Clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                map.clear();
//            }
//        });
//
//        Download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                download();
//
//            }
//        });
//
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SUTD, 18));
//
//        // Remove any existing images
//        images.clear();
//
//        // TODO: Download images from firebase and convert them into bitmap format
//        // Add bitmap images to the images array
//
//        images.add(BitmapDescriptorFactory.fromResource(R.drawable.sutdmap));
//        images.add(BitmapDescriptorFactory.fromResource(R.drawable.download));
//
//
//        // Night Fiesta Overlay (Features --> Rotated, clickable to adjust transparency)
//        /*groundOverlayRotated = map.addGroundOverlay(new GroundOverlayOptions()
//                .image(images.get(1)).anchor(0, 1)
//                .position(NEAR_SUTD, 4300f, 3025f)
//                .bearing(30)
//                .clickable(((CheckBox) findViewById(R.id.toggleClickability)).isChecked()));*/
//
//        // SUTD Floorplan Overlay (Adjustable through transparency bar)
//        /*groundOverlay = map.addGroundOverlay(new GroundOverlayOptions()
//                .image(images.get(currentEntry)).anchor(0, 1)
//                .position(SUTD, 86f, 65f));*/
//
//        transparencyBar.setOnSeekBarChangeListener(this);
//        rotationBar.setOnSeekBarChangeListener(this);
//
//        // Override the default content description on the view, for accessibility mode.
//        // Ideally this string would be localised.
//        map.setContentDescription("Google Map with ground overlay.");
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        if (groundOverlay != null && seekBar == transparencyBar) {
//            groundOverlay.setTransparency((float) progress / (float) TRANSPARENCY_MAX);
//        }
//        if (groundOverlay != null && seekBar == rotationBar) {
//            groundOverlay.setBearing((float) progress * (float) BEARING_MOVE);
//        }
//    }
//
//    // Download image from firebase
//    // If future authentication is required for firebase, go to RULES setting in firebase and change == to != NULL
//    public void download(){
//        storageReference = firebaseStorage.getInstance().getReference();
//        ref = storageReference.child("SUTD MAP.png");
//
//        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                String url = uri.toString();
//                downloadFile(FloorplanActivity.this, "SUTD MAP", ".png", DIRECTORY_DOWNLOADS, url);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }
//
//    // Download manager to retrieve file from firebase
//    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url){
//
//        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.parse(url);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
//
//        downloadManager.enqueue(request);
//    }
//
//    // Change to another image
//    public void switchImage(View view) {
//        currentEntry = (currentEntry + 1) % images.size();
//        groundOverlay.setImage(images.get(currentEntry));
//    }
//
//    // TODO: Place the points of type Point() on the overlay when it is clicked
//    @Override
//    public void onGroundOverlayClick(GroundOverlay groundOverlay) {
//        // Toggle transparency value between 0.0f and 0.5f. Initial default value is 0.0f.
//        groundOverlayRotated.setTransparency(0.5f - groundOverlayRotated.getTransparency());
//
//
//
//
//    }
//
//    /**
//     * Toggles the clickability of the smaller, rotated overlay based on the state of the View that
//     * triggered this call.
//     * This callback is defined on the CheckBox in the layout for this Activity.
//     */
//    public void toggleClickability(View view) {
//        if (groundOverlayRotated != null) {
//            groundOverlayRotated.setClickable(((CheckBox) view).isChecked());
//        }
//
//    }
//
//    @Override
//    public void onMapClick(LatLng latLng) {
//        // Displays the latlng coord that was clicked
//        tapTextView.setText("tapped, point=" + latLng);
//
//        // Add a reference marker to the marker array
//        saveMarker.add(latLng);
//
//        // Add a marker, which acts as the reference point
//        map.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title("Reference Point")
//                .draggable(true)
//                .anchor(0.5f,0.5f)
//                .flat(true)
//                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.sutdmap)));
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SUTD, 16));
//
//        // After the second marker has been placed, we assign the latlng bounds
//        if (saveMarker.size() == 2){
//            LatLngBounds floorplanBounds = new LatLngBounds(
//                    saveMarker.get(0),
//                    saveMarker.get(1));
//
//            // Display Floorplan Overlay
//            // Currently, the default anchor is used (middle of floor plan)
//            groundOverlay = map.addGroundOverlay(new GroundOverlayOptions()
//                    .image(images.get(currentEntry))
//                    .bearing(0)
//                    .positionFromBounds(floorplanBounds)
//                    .clickable(true));
//
//            // Clear the marker references in the array so that the array can be reused
//            saveMarker.clear();
//        }
//    }
//}