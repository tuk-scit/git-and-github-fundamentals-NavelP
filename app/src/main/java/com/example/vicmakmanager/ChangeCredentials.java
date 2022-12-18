package com.example.vicmakmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeCredentials extends AppCompatActivity {

    EditText email, phonenumber;

    Button register;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");

    private boolean oldMode = true;
    String oldPhonenumberText, oldEmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_credentials);

        getSupportActionBar().hide();

        email = findViewById(R.id.register_email);
        phonenumber = findViewById(R.id.register_phone);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldMode){
                   verifyDetails();
                }else {
                    changeCredentials();
                }
            }
        });

    }

    private void changeCredentials() {
        String newEmailText = email.getText().toString().trim();
        String newPhonenumberText = phonenumber.getText().toString().trim();

        if (newEmailText.isEmpty() || newPhonenumberText.isEmpty()) {
            Toast.makeText(ChangeCredentials.this, "Can not submit empty fields", Toast.LENGTH_SHORT).show();
        }else{
            databaseReference.child("adminCredentials").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().child(newPhonenumberText).child("email").setValue(newEmailText);
                    snapshot.child(oldPhonenumberText).getRef().setValue(null);

                    oldMode = true;
                    Toast.makeText(ChangeCredentials.this, "Credentials changed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void verifyDetails() {
        oldEmailText = email.getText().toString().trim();
        oldPhonenumberText = phonenumber.getText().toString().trim();

        if (oldEmailText.isEmpty() || oldPhonenumberText.isEmpty()) {
            Toast.makeText(ChangeCredentials.this, "Can not submit empty fields", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), ChangeCredentials.class));
        }
        else {
            databaseReference.child("adminCredentials").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (oldPhonenumberText.equals(snapshot.child(oldPhonenumberText).getKey()) &&
                            oldEmailText.equals(snapshot.child(oldPhonenumberText).child("email").getValue(String.class))) {
                        email.getText().clear();
                        phonenumber.getText().clear();
                        email.setHint("Enter new email");
                        phonenumber.setHint("Enter new phonenumber");

                        oldMode = false;

                    } else {
                        Toast.makeText(ChangeCredentials.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}