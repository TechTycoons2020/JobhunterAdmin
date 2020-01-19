package com.example.jobhunteradmin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobhunteradmin.models.Jobs;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class MailActivity extends AppCompatActivity {
    RecyclerView jobdetailsRV;
    private static final String TAG = "gps_act_test";

    //firestore Initialization
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference schoolcolref = db.collection("privatejobs");
    FirestoreRecyclerAdapter adapter;
    CollectionReference subscolref = db.collection("subscribers");
    ArrayList<String> emailAL = new ArrayList<>();
    String[] str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        //setting status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //initiating UI elements
        intiUI();
        
        //setting up firestore data
        setupFireStore();
    }

    private void intiUI() {
        jobdetailsRV = findViewById(R.id.jobdetailsRV);
        //setting layout manager for recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        jobdetailsRV.setLayoutManager(layoutManager);
    }

    void setupFireStore() {

        FirestoreRecyclerOptions<Jobs> options = new FirestoreRecyclerOptions.Builder<Jobs>().setQuery(schoolcolref, new SnapshotParser<Jobs>() {
            @NonNull
            @Override
            public Jobs parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                final Jobs post = new Jobs();

                post.setApplyby(snapshot.getString("applyby"));
                post.setCompanydesc(snapshot.getString("comdesc"));
                post.setCompanyname(snapshot.getString("company"));
                post.setExperience(snapshot.getString("exper"));
                post.setJobdesc(snapshot.getString("jobdesc"));
                post.setLocation(snapshot.getString("location"));
                post.setJobname(snapshot.getString("jobname"));
                post.setSalary(snapshot.getString("salary"));

                return post;
            }
        }).build();

        adapter = new FirestoreRecyclerAdapter<Jobs, FeedViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FeedViewHolder holder, int i, @NonNull final Jobs model) {
                //setting values for holder elements

                holder.jobname.setText(model.getJobname());
                holder.company.setText(model.getCompanyname());
                holder.location.setText(model.getLocation());
                holder.salary.setText(model.getSalary());
                holder.experience.setText(model.getExperience());
                holder.apply.setText(model.getApplyby());
                subscolref.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    final String email = document.getString("email");
                                    emailAL.add(email);
                                }
                                Object[] gfg= emailAL.toArray();
                                str = Arrays.copyOf(gfg,
                                        gfg.length,
                                        String[].class);

                            }
                        });
                holder.jobLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(str == null){
                            Toast.makeText(MailActivity.this, "Fetching subscribers...Please Wait", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent email = new Intent(Intent.ACTION_SEND);
                            email.putExtra(Intent.EXTRA_EMAIL, str);
                            email.putExtra(Intent.EXTRA_SUBJECT, "Avail Your Dream Jobs ");
                            email.putExtra(Intent.EXTRA_TEXT, model.getJobname()
                                    + "\n" + model.getCompanyname()
                                    + "\n" + model.getCompanyname()
                                    + "\n" + model.getLocation()
                                    + "\n" + model.getExperience()
                                    + "\n" + model.getSalary()
                                    + "\n" + model.getApplyby()
                                    + "\n");

                            //need this to prompts email client only
                            email.setType("message/rfc822");

                            startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        }
                    }
                });

            }

            @Override
            public FeedViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.jobitems, group, false);
                return new FeedViewHolder(view);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

        jobdetailsRV.setAdapter(adapter);
    }

    /*********************************************************/

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        public TextView jobname , company , location , salary , experience , apply;
        LinearLayout jobLL;

        public FeedViewHolder(View itemView) {
            super(itemView);

            jobname = itemView.findViewById(R.id.jobnameTV);
            company = itemView.findViewById(R.id.companyTV);
            location = itemView.findViewById(R.id.locationTV);
            salary= itemView.findViewById(R.id.salaryTV);
            experience= itemView.findViewById(R.id.experTV);
            apply= itemView.findViewById(R.id.applyTV);
            jobLL = itemView.findViewById(R.id.jobLL);

        }
    }

    /*********************************************************/

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
