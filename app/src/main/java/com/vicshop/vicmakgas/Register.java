package com.vicshop.vicmakgas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    private EditText name, email, phonenumber, street, nearest_building, area;
    private RadioGroup estate;
    private RadioButton estate_selected;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com");
    private FirebaseAuth mAuth;
    String textName,textEmail, textPhonenumber, textStreet, textNearestBuilding, textArea, textEstate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        phonenumber = findViewById(R.id.register_phone);
        street = findViewById(R.id.street);
        nearest_building = findViewById(R.id.nearest_building);
        area = findViewById(R.id.area);


        mAuth = FirebaseAuth.getInstance();

    }

    public void SubmitRegister(View view) {
        estate = findViewById(R.id.estate);
        int estate_id = estate.getCheckedRadioButtonId();

        estate_selected = findViewById(estate_id);



        textName = name.getText().toString();
        textEmail = email.getText().toString();
        textPhonenumber = phonenumber.getText().toString();
        textStreet = street.getText().toString();
        textNearestBuilding = nearest_building.getText().toString();
        textArea = area.getText().toString();
        textEstate = estate_selected.getText().toString();


        if(textName.equals("") || textEmail.equals("") || textPhonenumber.equals("") ||
                textStreet.equals("") || textNearestBuilding.equals("") || textArea.equals("") || textEstate.isEmpty()){
            Toast.makeText(this, "Cannot submit blank fields", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
        }

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(textPhonenumber)){
                    Toast.makeText(Register.this, "Already registered", Toast.LENGTH_SHORT).show();
                }else{

                    databaseReference.child("users").child(textPhonenumber).child("Name").setValue(textName);
                    databaseReference.child("users").child(textPhonenumber).child("Email").setValue(textEmail);
                    databaseReference.child("users").child(textPhonenumber).child("Estate").setValue(textEstate);
                    databaseReference.child("users").child(textPhonenumber).child("Street").setValue(textStreet);
                    databaseReference.child("users").child(textPhonenumber).child("Nearest_building").setValue(textNearestBuilding);
                    databaseReference.child("users").child(textPhonenumber).child("Area").setValue(textArea);

                    Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}