package com.example.administrator.sih2018;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerifierWorkSpace extends AppCompatActivity {

    FirebaseAuth auth;
    public String uid;
    DatabaseReference mDatabase;
    private RecyclerView recylceview;
    private ArrayList<String> Astatus;
    private ArrayList<Double> Alat;
    private ArrayList<Double> Along;
    private ArrayList<String> AparentNode;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifier_work_space);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Firebase.setAndroidContext(this);

        Intent intent=getIntent();
        uid=intent.getStringExtra("UID");

        Astatus=new ArrayList<>();
        Alat=new ArrayList<>();
        Along=new ArrayList<>();
        AparentNode=new ArrayList<>();

        Log.d("UID of verifier in work",uid);

        Log.d("Before recycle", uid);
        recylceview = (RecyclerView) findViewById(R.id.list_verify);
        Log.d("After recycle", "");

        recylceview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recylceview.setLayoutManager(mLayoutManager);
        Log.d("Before recycle", "Layoutmanager");


    }
    @Override
    public void onStart(){
        super.onStart();

        Log.d("Where verify", "onStart: ");
        Query userfEvents = mDatabase.orderByChild("type").equalTo(uid);

       userfEvents.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot child: dataSnapshot.getChildren()) {
                   Log.d("MainActivity parent key", child.getKey());
                   AparentNode.add(child.getKey());
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
               Log.e("MainActivity", "onCancelled", databaseError.toException());
           }
       });

        Log.d("SAomething",userfEvents.toString());
        FirebaseRecyclerAdapter<AdminAddTaskData,VerifierWorkSpace.PostviewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdminAddTaskData, VerifierWorkSpace.PostviewHolder>(
                AdminAddTaskData.class,
                R.layout.list_verify_card,
                VerifierWorkSpace.PostviewHolder.class,
                userfEvents


        ) {
            @Override
            protected void populateViewHolder(VerifierWorkSpace.PostviewHolder viewHolder, AdminAddTaskData model, final int position) {

                viewHolder.setTitle("Task "+(position+1));
               // viewHolder.setDesc(model.getDescription());
                viewHolder.setPhase(model.getPhaseSelected());
                viewHolder.setQuarter(model.getQuarter());
                viewHolder.setDeadLine(model.getDeadLine());
              //  viewHolder.setLat(model.getGeolat());
               // viewHolder.setLong(model.getGeolong());
               // viewHolder.setAddress(model.getAddress());

                //Setting Required stuffs
                Astatus.add(model.getStatus());
               // Alat.add(Double.parseDouble(model.getGeolat()));
                //Along.add(Double.parseDouble(model.getGeolong()));

                //Alat


                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(VerifierWorkSpace.this,VerifierTaskListActivity.class);
                        startActivity(intent);
                    }
                });



            }
        };
        recylceview.setAdapter(firebaseRecyclerAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.verifier_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
//            case R.id.action_remove:
//            {
//                Intent intent = new Intent(VerifierWorkSpace.this, AdminRemoveTaskActivity.class);
//                startActivity(intent);
//            }
             // return true;
            case R.id.action_signout:
                signout();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public  void  signout(){
//        auth.signOut();
        Intent intent = new Intent(VerifierWorkSpace.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
