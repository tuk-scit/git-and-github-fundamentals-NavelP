package com.vicshop.vicmakgas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    Boolean to_be_remembered = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try{
                    sharedPreferences = getSharedPreferences("REMEMBER_ME", Context.MODE_PRIVATE);
                    to_be_remembered = sharedPreferences.getBoolean("rem_value", to_be_remembered);
                    SettingsActivity.remember_me.setChecked(to_be_remembered);

                }catch(NullPointerException e){
                    e.printStackTrace();
                }

                if(to_be_remembered){
                    AccountActivity.textPhonenumber = sharedPreferences.getString("phoneNumber", AccountActivity.textPhonenumber);

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
                finish();
            }
        }, 2000);

    }
}