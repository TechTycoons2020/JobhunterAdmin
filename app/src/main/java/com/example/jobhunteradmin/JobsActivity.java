package com.example.jobhunteradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.creativityapps.gmailbackgroundlibrary.util.GmailSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JobsActivity extends AppCompatActivity {
    EditText jobnameTV, companyTV, locationTV, experTV, salaryTV, applybyTV, jobdescTV, comdescTV;
    DatePickerDialog picker;
    Button upload;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference jobcolref = db.collection("privatejobs");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        intiUI();

        applybyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(JobsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                applybyTV.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jobnameTV.getText().toString().isEmpty() || companyTV.getText().toString().isEmpty() || locationTV.getText().toString().isEmpty() || experTV.getText().toString().isEmpty() || salaryTV.getText().toString().isEmpty() || applybyTV.getText().toString().isEmpty() || jobdescTV.getText().toString().isEmpty() || comdescTV.getText().toString().isEmpty()) {
                    Toast.makeText(JobsActivity.this, "Please fill all required Fields", Toast.LENGTH_SHORT).show();
                } else {
                    final Map<String, Object> jobdetailsMAP = new HashMap<>();
                    jobdetailsMAP.put("jobname", jobnameTV.getText().toString().trim());
                    jobdetailsMAP.put("company", companyTV.getText().toString().trim());
                    jobdetailsMAP.put("location", locationTV.getText().toString().trim());
                    jobdetailsMAP.put("salary", salaryTV.getText().toString().trim());
                    jobdetailsMAP.put("exper", experTV.getText().toString().trim());
                    jobdetailsMAP.put("applyby", applybyTV.getText().toString().trim());
                    jobdetailsMAP.put("jobdesc", jobdescTV.getText().toString().trim());
                    jobdetailsMAP.put("comdesc", comdescTV.getText().toString().trim());


                    jobcolref.add(jobdetailsMAP)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    if (documentReference != null) {
                                        Toast.makeText(JobsActivity.this, "Upload successful !!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(JobsActivity.this, "Upload failure", Toast.LENGTH_SHORT).show();

                                }
                            });
                }

            }
        });
    }

    private void intiUI() {
        jobnameTV = findViewById(R.id.jobpositionTV);
        companyTV = findViewById(R.id.companynameTV);
        locationTV = findViewById(R.id.locationTV);
        experTV = findViewById(R.id.experienceTV);
        salaryTV = findViewById(R.id.salaryTV);
        applybyTV = findViewById(R.id.applybyTV);
        jobdescTV = findViewById(R.id.jobdescTV);
        comdescTV = findViewById(R.id.compdescTV);
        upload = findViewById(R.id.continueBT);
    }


}
