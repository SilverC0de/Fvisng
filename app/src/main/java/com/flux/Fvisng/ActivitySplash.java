package com.flux.Fvisng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.android.volley.toolbox.StringRequest;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorLighter));
        if (Build.VERSION.SDK_INT > 22) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);

                String mail = sharedPreferences.getString("mail", "");
                //boolean online = sharedPreferences.getBoolean("mail", false);

                String verified = sharedPreferences.getString("verified", "");
                if (verified.equals("ok")){
                    startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                    finish();
                } else if (mail.isEmpty()){
                    startActivity(new Intent(getApplicationContext(), ActivityIntro.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), ActivityView.class)); //otp
                    finish();
                }
            }
        }, 4000);
    }
}
