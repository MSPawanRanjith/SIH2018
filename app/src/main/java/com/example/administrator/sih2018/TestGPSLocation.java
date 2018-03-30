package com.example.administrator.sih2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestGPSLocation extends AppCompatActivity {

    private String mResposnseString;
    private String mArea;
    private double mLat,mLang;
    private TextView mLatTextView,mLangTextView;
    private EditText mAreaEdittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gpslocation);

        mAreaEdittext=(EditText)findViewById(R.id.area);
        mLangTextView=(TextView)findViewById(R.id.lng);
        mLatTextView=(TextView)findViewById(R.id.lat);

        OkHttpClient client =new OkHttpClient();
        Request request = new Request.Builder().url("https://maps.google.com/maps/api/geocode/json?address="+"kle tech University&key=AIzaSyC-SY8PWH-BL_lm14Rm9axJNH2FOo-Z3sE").build();

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
                    double lng = mapResult.getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lng");

                    double lat = mapResult.getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lat");
                    mLat=lat;
                    mLang=lng;
                    mLatTextView.setText(Double.toString(mLat));
                    mLatTextView.setText(Double.toString(mLang));
                    Log.d("LAT and Long ","Lat : "+lat+" Long : "+lng);
                }catch(Exception e){
                    Log.d("Exception " , e.toString());
                }

            }
        });



    }
}
