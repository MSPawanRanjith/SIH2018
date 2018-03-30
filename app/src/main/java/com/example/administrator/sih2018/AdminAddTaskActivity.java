package com.example.administrator.sih2018;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Random;

public class AdminAddTaskActivity extends AppCompatActivity {

    private Button assign;
    private TextInputLayout descriptionoftask;
    private TextInputLayout mGeo_lat,mGeo_long;

    private String uid = "";

    private TextInputLayout deadLine;
    private DatabaseReference mDataBase;
    private ProgressDialog mProgress;
    static final int DIALOG_ID = 0;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private String mType;
    private String mDesc;
    private String mGeolat;
    private String mGeolong;
    private String mAddress;
    private String mStatus;
    private String mAdminUid;
    private Spinner mPhaseSpinner;
    private TextInputLayout mThreshold;

    private  String mDeadLineDate;
    private  String mPhaseSelected;
    private  int mThresholdGiven;
    private  String mQuarter;
    private int year_x, month_x, day_x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_task);

        Firebase.setAndroidContext(this);
        auth = FirebaseAuth.getInstance();

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mProgress=new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);

        mPhaseSpinner= (Spinner) findViewById(R.id.spinner_phase);
        mThreshold=(TextInputLayout)findViewById(R.id.threshold);

        assign=(Button) findViewById(R.id.assign);
        descriptionoftask=(TextInputLayout) findViewById(R.id.desc_work);


        deadLine=(TextInputLayout)findViewById(R.id.dead_line);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.phase, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPhaseSpinner.setAdapter(adapter);


        //calendar code


        deadLine.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                showDialog(0);
                Log.d("OnTouch","Hi Inside OnTOuch");
                return true;

            }
        });
        //spinner selection

        mPhaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            // your code here
            String selectedPhase=mPhaseSpinner.getSelectedItem().toString();
            mPhaseSelected=selectedPhase;
            Log.d("Spinner : ",selectedPhase);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
            // your code here
        }

    });



        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(AdminAddTaskActivity.this, AdminLoginActivity.class));
                    finish();
                }
            }
        };
        //Get UID of the user
        mAdminUid = user.getUid();

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadAssignment();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if (id == 0) {
            Log.d("Calendar ","Inside Dailouge");
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, dpickerListener, mYear, mMonth, mDay);
        }
        else
            return null;
    }

    private  DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year_x = i;
            month_x = i1 + 1;
            day_x = i2;
            mDeadLineDate=day_x+"/"+month_x+"/"+year_x;

            if(month_x>3 && month_x<7){
                mQuarter="Q1";
            }
            else if (month_x>6 && month_x<10){
                mQuarter="Q2";
            }
            else if (month_x>9&&month_x<=12){
                mQuarter="Q3";
            }
            else{
                mQuarter="Q4";
            }
            deadLine.getEditText().setText(day_x+"/"+month_x+"/"+year_x);
        }
    };

    public void uploadAssignment(){

        mProgress.setMessage("Validating task...");
        mProgress.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

            }}, 5000);



                    mProgress.setMessage("Assigning task...");



            handler.postDelayed(new Runnable() {
                public void run() {

                }}, 3000);
                  //  DatabaseReference newPost=mDataBase.push();

                    mDesc=descriptionoftask.getEditText().getText().toString();
//                    mGeolat = mGeo_lat.getEditText().getText().toString();
//                    mGeolong= mGeo_long.getEditText().getText().toString();
                    //mAddress =addressofsite.getEditText().getText().toString();
//                    date1 = ""+day_x;
//                    date1 = date1 + "/" + month_x;
//                    date1 = date1 + "/" + year_x;
                    //description = description + "\n" + "date :" + date1;
                    // get selected radio button from radioGroup
                    mType=getIntent().getStringExtra("EMP_UID");
                    mStatus="1";
//
                    mThresholdGiven=Integer.parseInt(mThreshold.getEditText().getText().toString());


                    AdminAddTaskData obj = new AdminAddTaskData(mDesc,mDeadLineDate,mPhaseSelected,mQuarter,mThresholdGiven,mType,mStatus,mAdminUid);
                    Random rand = new Random();


                    mDataBase.push().setValue(obj);
                   // mDataBase.child("fire").setValue(obj);
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(),"Successfully task assigned ",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AdminAddTaskActivity.this, AdminWorkSpace.class));
                    finish();

            Toast.makeText(getApplicationContext(),"Please enter the geo-location for minimum information",Toast.LENGTH_LONG).show();



    }
}
