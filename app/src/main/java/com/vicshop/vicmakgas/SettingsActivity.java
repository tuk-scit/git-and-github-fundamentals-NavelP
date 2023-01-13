package com.vicshop.vicmakgas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean to_be_remembered;
    Button button;
    static SharedPreferences sharedPreferencesRate;

    @SuppressLint({"UseSwitchCompatOrMaterialCode", "StaticFieldLeak"})
    public static Switch remember_me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        getSupportActionBar().hide();

        button = findViewById(R.id.rate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRate(SettingsActivity.this, getResources().getColor(android.R.color.transparent));
            }
        });
        sharedPreferences = getApplicationContext().getSharedPreferences("REMEMBER_ME", Context.MODE_PRIVATE);
        sharedPreferencesRate = getApplicationContext().getSharedPreferences("RATE_US", Context.MODE_PRIVATE);

        remember_me = findViewById(R.id.remember_me);
        to_be_remembered = sharedPreferences.getBoolean("rem_value", to_be_remembered);
        SettingsActivity.remember_me.setChecked(to_be_remembered);

        remember_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sharedPreferences.edit().putBoolean("rem_value", true).apply();
                    sharedPreferences.edit().putString("phoneNumber", AccountActivity.textPhonenumber).apply();

                }else{
                    sharedPreferences.edit().putBoolean("rem_value", false).apply();

                }
            }
        });
    }


    public static void displayRate(Context context, int color){
        rateDialogBox rate = new rateDialogBox(context, sharedPreferencesRate);
        rate.getWindow().setBackgroundDrawable(new ColorDrawable(color));
        rate.setCancelable(true);
        rate.show();
    }

}