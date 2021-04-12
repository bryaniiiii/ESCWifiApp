package com.example.mywifiapp2.UI;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mywifiapp2.R;
import com.google.firebase.auth.FirebaseAuth;

public class StartingActivity extends AppCompatActivity {
    private Button mapMe;
    private Button locateMe;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        mapMe = findViewById(R.id.mapme);
        locateMe = findViewById(R.id.locateme);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(StartingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        locateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocateActivity.class);
                startActivity(intent);
            }
        });


        mapMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseImage.class);
                startActivity(intent);

            }
        });
    }
}
