package com.example.administrator.sih2018;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button adminLogin;
    private Button verfierLogin;

    private TextView mEnglish;

    public Configuration config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        adminLogin=(Button)findViewById(R.id.admin_button_id);
        verfierLogin=(Button)findViewById(R.id.verfier_button_id);

        mEnglish=(TextView)findViewById(R.id.english);
        mEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentLanguage = "en";
                Locale locale = new Locale(currentLanguage);
                Locale.setDefault(locale);

                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());

                Log.d("Inside English","English");
            }
        });

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminLoginIntent = new Intent(getBaseContext(),FingerPrintActivity.class);
                adminLoginIntent.putExtra("TYPE_OF_LOGIN","ADMIN");
                startActivity(adminLoginIntent);
            }
        });

        verfierLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent verfierLoginIntent = new Intent(getBaseContext(),FingerPrintActivity.class);
                verfierLoginIntent.putExtra("TYPE_OF_LOGIN","VERIFIER");
                startActivity(verfierLoginIntent);
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            // case R.id.action_remove:
            // delete();
            //   return true;
            case R.id.action_english:
            {
                String currentLanguage = "en";
                Locale locale = new Locale(currentLanguage);
                Locale.setDefault(locale);

                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());

                Log.d("Inside English","English");
            }


            case R.id.action_hindi:
            {
                String currentLanguage = "hi";
                Locale locale = new Locale(currentLanguage);
                Locale.setDefault(locale);

                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());

                Log.d("Inside Hindi","Hindi");
            }


            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
