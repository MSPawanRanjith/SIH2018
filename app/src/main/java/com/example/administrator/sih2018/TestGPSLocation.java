package com.example.administrator.sih2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestGPSLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gpslocation);
        OkHttpClient client =new OkHttpClient();
    }
}
