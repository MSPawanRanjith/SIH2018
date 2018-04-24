package com.example.administrator.sih2018;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
//import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ValidateGPSActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    ProgressDialog mProgress,mUpload;
    String mRecievedAddress;
    Double mRecievedLong;
    Double mCurrLat,mCurrLong;
    private final static int CAMERA_REQUEST_CODE=1;
    DatabaseReference mDatabase;
    private StorageReference mStorage;
    public  Uri photoURI;
    Uri file,currentImageUri;
    public File mediaStorageDir,imagePath;
    public boolean sucess=false;
    CaameraPhoto cameraPhoto;
    String mResposnseString;

    //helper for photoURi

    String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_gps);


        cameraPhoto=new CaameraPhoto(this);


        mProgress=new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);
        mUpload=new ProgressDialog(this);
        mUpload.setCanceledOnTouchOutside(false);

        mProgress.setMessage("Validating Location...");
        mProgress.show();

        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapFragment.getView().setVisibility(View.INVISIBLE);
        Toast.makeText(getBaseContext(), "SetFragment", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMapReady (GoogleMap googleMap){
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }




    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

       // TextView text = (TextView) findViewById(R.id.textView);
        mRecievedAddress=getIntent().getStringExtra("ADDRESS");
        //mRecievedLong=getIntent().getDoubleExtra("GEO_LONG",0.0);

        //Log.d("GeoLatitude",Double.toString(mRecievedAddress));
        //Log.d("GeoLangitude",Double.toString(mRecievedLong));

        DecimalFormat df = new DecimalFormat("#.####");
        //mRecievedLat = Double.valueOf(df.format(mRecievedLat));
        //mRecievedLong=Double.valueOf(df.format(mRecievedLong));

        //Log.d("GPS LOCATION", Double.toString(location.getLatitude()) + " Latitude");
        //Log.d("GPS LOCATION", Double.toString(location.getLongitude()) + " Longitude");

        mCurrLat=Double.valueOf(df.format(location.getLatitude()));
        mCurrLong=Double.valueOf(df.format(location.getLongitude()));

        //TextView text1 = (TextView) findViewById(R.id.textView1);

        Log.d("GPS LOCATION trim", mCurrLat+ " Latitude");
        Log.d("GPS LOCATION trim", mCurrLong+ " Longitude");




        //Actual working stuff
        OkHttpClient client =new OkHttpClient();
        Request request = new Request.Builder().url("https://maps.google.com/maps/api/geocode/json?latlng="+mCurrLat+","+mCurrLong+"&key=AIzaSyC-SY8PWH-BL_lm14Rm9axJNH2FOo-Z3sE").build();

        Call call =client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Notification message ","Inside onFailure");

                return;

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("Notification message ","Inside onResponse");
                mResposnseString=response.body().string();
                Log.d("Notif : ",mResposnseString);

                //parsing json
                try {
                    JSONObject mapJson = new JSONObject(mResposnseString);
                    JSONArray mapResult=mapJson.getJSONArray("results");
                    String mAddress = mapResult.getJSONObject(0)
                            .getString("formatted_address");

                    Log.d("Address found",mAddress);
                    if(mRecievedAddress.equals(" ")){
                        throw new Exception();
                    }
                    if(mAddress.toLowerCase().contains(mRecievedAddress.toLowerCase())){

                        //Sucess
                        sucess=true;
                        Log.d("Sucess !!!","Lol Done FInally");
                        if(sucess==true){
                            Intent listIntent=new Intent(ValidateGPSActivity.this,VerifierTaskListActivity.class);
                            startActivity(listIntent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Location Not Matching",Toast.LENGTH_LONG).show();
                            Intent listIntent=new Intent(ValidateGPSActivity.this,VerifierTaskListActivity.class);
                            startActivity(listIntent);
                        }
                    }
                }catch(Exception e){
                    Log.d("Exception " , e.toString());
                }

            }
        });







        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        mProgress.dismiss();
        Toast.makeText(getBaseContext(), "connection changed", Toast.LENGTH_SHORT).show();

    }
    //Camrea activity override



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(getBaseContext(), "connection failed", Toast.LENGTH_SHORT).show();
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

}
