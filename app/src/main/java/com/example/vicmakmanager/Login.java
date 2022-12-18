package com.example.vicmakmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vicmakmanager.Burners.BurnerManagement;
import com.example.vicmakmanager.Gas.GasManagement;
import com.example.vicmakmanager.Grills.GrillsManagement;
import com.example.vicmakmanager.Pipes.PipesManagement;
import com.example.vicmakmanager.Regulators.RegulatorsManagement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    ProgressBar progressBar;
    EditText email, phonenumber;

    Button login;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");
    TextView change_credentials;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        email = findViewById(R.id.email);
        phonenumber = findViewById(R.id.password);

        change_credentials = findViewById(R.id.sign_up_intent);

        change_credentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChangeCredentials.class));
            }
        });

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        String commodity_name = bundle.getString("item_name");

        login = findViewById(R.id.user_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equals(" ") || phonenumber.getText().toString().trim().equals(" ")){
                    Toast.makeText(Login.this, "Can not use empty fields", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
                {
                    progressBar.setVisibility(View.VISIBLE);

                    databaseReference.child("adminCredentials").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String myEmail = email.getText().toString().trim();
                            String myPassword = phonenumber.getText().toString().trim();

                            if(!snapshot.hasChild(myPassword)){
                                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(
                                        getApplicationContext(), MainActivity.class
                                ));

                                finish();
                            }else{
                                String r_email = snapshot.child(myPassword).child("email").getValue(String.class);

                                if(r_email.equals(myEmail)){
                                    switch (commodity_name) {
                                        case "Gas":
                                            startActivity(new Intent(getApplicationContext(), GasManagement.class));
                                            finish();
                                            break;
                                        case "Burners":
                                            startActivity(new Intent(getApplicationContext(), BurnerManagement.class));
                                            finish();
                                            break;
                                        case "Grills":
                                            startActivity(new Intent(getApplicationContext(), GrillsManagement.class));
                                            finish();
                                            break;
                                        case "Pipes":
                                            startActivity(new Intent(getApplicationContext(), PipesManagement.class));
                                            finish();
                                            break;
                                        case "Regulators":
                                            startActivity(new Intent(getApplicationContext(), RegulatorsManagement.class));
                                            finish();
                                            break;
                                    }
                                }else{
                                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(
                                            getApplicationContext(), MainActivity.class
                                    ));

                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


    }

}