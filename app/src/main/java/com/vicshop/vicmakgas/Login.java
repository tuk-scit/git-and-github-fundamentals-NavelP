package com.vicshop.vicmakgas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText email,password;
    private String textEmail, textPassword;
    private Button login;
    private String getEmail, getPassword;
    private ProgressBar progressBar;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login = findViewById(R.id.user_login);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

    }

    public void LogUser(View view) {

        textEmail = email.getText().toString();
        textPassword = password.getText().toString();

        if(textEmail.isEmpty() || textPassword.isEmpty()){
            Toast.makeText(this, "Cannot submit empty fields", Toast.LENGTH_SHORT).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(textPassword)){
                        getEmail = snapshot.child(textPassword).child("Email").getValue(String.class);

                        if(getEmail.equals(textEmail)){
                            AccountActivity.textPhonenumber = textPassword;
                            AccountActivity.emailAddress = textEmail;

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(Login.this, "Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Login.this, "Don't have account", Toast.LENGTH_SHORT).show();
                        // take the user to sign up
                        startActivity(new Intent(getApplicationContext(), Register.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Error: ", error.getMessage());
                }
            });
        }
    }

    public void StartSignUpActivity(View view) {
        startActivity(new Intent(getApplicationContext(), Register.class));
    }
}