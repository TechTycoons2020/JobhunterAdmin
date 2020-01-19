package com.example.jobhunteradmin;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jobhunteradmin.utils.SharedPref;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity {
    EditText emailET, passwordET;
    Button loginBT, signupBT;
    ImageView googleIV;
    GifImageView progressIV;
    private static final String TAG = "login_test";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //Intialization of UI
        initUI();

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_option(emailET.getText().toString().trim(), passwordET.getText().toString().trim());
            }
        });

    }

    private void initUI() {
        emailET = findViewById(R.id.inputET);
        passwordET = findViewById(R.id.passwordET);
        loginBT = findViewById(R.id.loginBT);
        signupBT = findViewById(R.id.signupBT);
        googleIV = findViewById(R.id.googleIV);
        progressIV = findViewById(R.id.progressIV);
    }

    public void login_option(String email, String password) {
        progressIV.setVisibility(View.VISIBLE);

        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                SharedPref.putBoolean(getApplicationContext(), "sp_loggedin", true);
                                SharedPref.putString(getApplicationContext(), "sp_email", user.getEmail());
                                progressIV.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                progressIV.setVisibility(View.GONE);
                            }

                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "error-1: " + e.toString());
                            progressIV.setVisibility(View.GONE);
                        }
                    });
        } else {
            Toast.makeText(this, "Credentials Empty", Toast.LENGTH_SHORT).show();
            progressIV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(startMain);
        finishAffinity();
        finish();
    }
}
