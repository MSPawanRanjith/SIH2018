package com.example.administrator.sih2018;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AdminRemoveTaskActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private String uid;
    private Query userfEvents;
    private FirebaseRecyclerAdapter<AdminAddTaskData,PostviewHolder> firebaseRecyclerAdapter;
    private RecyclerView recylceview;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_remove_task);

        Firebase.setAndroidContext(this);

        auth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference chatRef = mDataBase.child("");
        Query userfEvents = mDatabase.orderByChild("uid").equalTo(uid);
        recylceview = (RecyclerView) findViewById(R.id.list);
        recylceview.setHasFixedSize(true);
        recylceview.setLayoutManager(new LinearLayoutManager(this));

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(AdminRemoveTaskActivity.this, AdminLoginActivity.class));
                    finish();
                }

            }

        };



    }

    @Override
    protected void onStart() {
        super.onStart();
        Query userfEvents = mDatabase.orderByChild("adminuid").equalTo(uid);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdminAddTaskData, PostviewHolder>(
                AdminAddTaskData.class,
                R.layout.list_verify_card,
                PostviewHolder.class,
                userfEvents

        ) {
            @Override
            protected void populateViewHolder(PostviewHolder viewHolder, final AdminAddTaskData model, final int position) {
                viewHolder.setTitle("Task "+(position+1));
                //viewHolder.setDesc(model.getDescription());
               // viewHolder.setLat(model.getGeolat());
               // viewHolder.setLong(model.getGeolong());
               // viewHolder.setAddress(model.getAddress());
                viewHolder.setPhase(model.getPhaseSelected());
                viewHolder.setQuarter(model.getQuarter());
                viewHolder.setDeadLine(model.getDeadLine());
               // if(model.getStatus().equals("1")){
                 //   viewHolder.setStatus("Task Not Completed");
                //}
                //else {
                  //  viewHolder.setStatus(model.getStatus());
                //}

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminRemoveTaskActivity.this);

                        builder.setTitle("View Report");
                        //builder.setMessage("Are you sure?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                //if(model.getStatus().equals("1")) {
                                    startActivity(new Intent(AdminRemoveTaskActivity.this, Report.class));
                                //}
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }


        };
        recylceview.setAdapter(firebaseRecyclerAdapter);




    }

    public static class PostviewHolder extends RecyclerView.ViewHolder{
        View mview;

        public PostviewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setTitle(String mJobtitle){
            TextView posttitle = (TextView) mview.findViewById(R.id.job_title);
            posttitle.setText(mJobtitle);
        }
        public void setDesc(String mJobdesc){
            TextView posttitle = (TextView) mview.findViewById(R.id.job_desc);
            posttitle.setText(mJobdesc);
        }
        public void setLat(String mJoblat){
            TextView posttitle = (TextView) mview.findViewById(R.id.job_lat);
            posttitle.setText(mJoblat);
        }
        public void setLong(String mJoblang){
            TextView posttitle=(TextView)mview.findViewById(R.id.job_long);
            posttitle.setText(mJoblang);

        }
        public void setAddress(String mJobaddress){
            TextView posttitle=(TextView)mview.findViewById(R.id.job_address);
            posttitle.setText(mJobaddress);

        }
        public void setStatus(String mJobstatus){
            TextView posttitle=(TextView)mview.findViewById(R.id.status_of_event);
            posttitle.setText(mJobstatus);

        }
        public void setPhase(String mPhase){
            TextView posttitle=(TextView)mview.findViewById(R.id.phase_verifier);
            posttitle.setText(mPhase);

        }
        public void setQuarter(String mQuarter){
            TextView posttitle=(TextView)mview.findViewById(R.id.quarter_verifier);
            posttitle.setText(mQuarter);

        }
        public void setDeadLine(String mDeadLine){
            TextView posttitle=(TextView)mview.findViewById(R.id.deadline_verifier);
            posttitle.setText(mDeadLine);

        }

    }
}
