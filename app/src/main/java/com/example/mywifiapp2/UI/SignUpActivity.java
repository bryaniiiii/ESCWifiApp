package com.example.mywifiapp2.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywifiapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends AppCompatActivity {
    private EditText emailEt, passwordEt1, passwordEt2;
    private Button SignUpButton;
    private TextView SignInTv;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEt = findViewById(R.id.email);
        passwordEt1 = findViewById(R.id.password1);
        passwordEt2 = findViewById(R.id.password2);
        SignUpButton = findViewById(R.id.register);
        progressDialog = new ProgressDialog(this);
        SignInTv = findViewById(R.id.signinTv);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        SignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Register() {
        String email = emailEt.getText().toString();
        String password1 = passwordEt1.getText().toString();
        String password2 = passwordEt2.getText().toString();
        if(TextUtils.isEmpty(email)){
            emailEt.setError("Enter your Email");
            return;
        }
        else if (TextUtils.isEmpty(password1)){
            passwordEt1.setError("Enter your Password");
            return;
        }
        else if (TextUtils.isEmpty(password2)){
            passwordEt2.setError("Confirm your Password");
            return;
        }
        else if (!password1.equals(password2)){
            passwordEt2.setError("Password different");
            return;
        }
        else if (password1.length()<6){
            passwordEt1.setError("Length should be > 5");
            return;
        }
        else if (!isValidEmail(email)){
            emailEt.setError("Invalid Email");
            return;
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, StartingActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_LONG).show();}
                progressDialog.dismiss();
            }
        });
    }
    public Boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && PatternsCompat.EMAIL_ADDRESS.matcher(target).matches());
    }

}
