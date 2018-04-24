package com.example.administrator.sih2018;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VerifierPriceActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    ProgressDialog mProgress,mUpload;
    Double mRecievedLat,mRecievedLong;
    Double mCurrLat,mCurrLong;
    String mResposnseString;

    public EditText mPriceEdit;
    public EditText mAddress;
    public Spinner mQtySpiiner;
    public Button mSaveBtn;

    private DatabaseReference mDataBase;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    public String selectedQty;
    public String price,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifier_price);

        Firebase.setAndroidContext(this);
        auth = FirebaseAuth.getInstance();

        mDataBase = FirebaseDatabase.getInstance().getReference();

        mPriceEdit=(EditText)findViewById(R.id.price_entry);
        mAddress=(EditText)findViewById(R.id.add_entry);
        mQtySpiiner=(Spinner)findViewById(R.id.spinner_qty);
        mSaveBtn=(Button)findViewById(R.id.save_value);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.INVISIBLE);
        Toast.makeText(getBaseContext(), "SetFragment", Toast.LENGTH_SHORT).show();








        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.qtymou, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mQtySpiiner.setAdapter(adapter);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price=mPriceEdit.getText().toString();
                address=mAddress.getText().toString();

                Intent intent=new Intent(VerifierPriceActivity.this,ValidateGPSActivity.class);
                intent.putExtra("ADDRESS",address);
                startActivity(intent);


            }
        });

        mQtySpiiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String selectedPhase=mQtySpiiner.getSelectedItem().toString();
                selectedQty=selectedPhase;
                Log.d("Spinner : ",selectedPhase);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


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
        //mRecievedLat=getIntent().getDoubleExtra("GEO_LAT",0.0);
        //mRecievedLong=getIntent().getDoubleExtra("GEO_LONG",0.0);

        //Log.d("GeoLatitude",Double.toString(mRecievedLat));
        //Log.d("GeoLangitude",Double.toString(mRecievedLong));

        DecimalFormat df = new DecimalFormat("#.####");
        //mRecievedLat = Double.valueOf(df.format(mRecievedLat));
        //mRecievedLong=Double.valueOf(df.format(mRecievedLong));

        Log.d("GPS LOCATION", Double.toString(location.getLatitude()) + " Latitude");
        Log.d("GPS LOCATION", Double.toString(location.getLongitude()) + " Longitude");

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
                    String mRecievedAddress = mapResult.getJSONObject(0)
                            .getString("formatted_address");

                    Log.d("Address found",mRecievedAddress);
                }catch(Exception e){
                    Log.d("Exception " , e.toString());
                }

            }
        });














        //Log.d("GeoLatitude",Double.toString(mRecievedLat));
        //Log.d("GeoLangitude",Double.toString(mRecievedLong));


        //move map camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        //mProgress.dismiss();
        Toast.makeText(getBaseContext(), "connection changed", Toast.LENGTH_SHORT).show();

    }

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
