package com.example.amgadrady.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.amgadrady.chatapp.utils.Session;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.i(TAG , "In splash");
        if(Session.getInstance().isUserLoggedIn())
        {

            Log.i(TAG , "open MinaActivity");
            Intent intent = new Intent(this , MainActivity.class);
                    startActivity(intent);
                    finish();
            Log.i(TAG , "opened MinaActivity");

        }else
        {
            Log.i(TAG , "open LoginActivity");
            Intent intent = new Intent(this , LoginActivity.class);
            startActivity(intent);
            finish();
            Log.i(TAG , "opened LoginActivity");
         }

    }
}
