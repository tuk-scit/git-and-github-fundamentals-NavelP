package com.example.vicmakmanager.Grills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vicmakmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

public class UpdateGrills extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");
    Button update, cancel;
    TextView buying_price, selling_price, stock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_grills);

        getSupportActionBar().hide();

        buying_price = findViewById(R.id.buying_price);
        selling_price = findViewById(R.id.selling_price);
        stock = findViewById(R.id.stock);

        update = findViewById(R.id.update);
        cancel = findViewById(R.id.cancel);


        databaseReference.child("VicmakStock").child("Grills").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                buying_price.setText(snapshot.child("buying_price").getValue(String.class));
                selling_price.setText(snapshot.child("selling_price").getValue(String.class));
                stock.setText(snapshot.child("stock").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GrillsManagement.class));
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(buying_price.getText().toString().trim().isEmpty() || selling_price.getText().toString().trim().isEmpty() ||
                stock.getText().toString().trim().isEmpty()){
                    Toast.makeText(UpdateGrills.this, "Cannot submit blank fields", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("VicmakStock").child("Grills").addListenerForSingleValueEvent(new ValueEventListener() {
                        CountDownLatch done = new CountDownLatch(1);
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child("VicmakStock").child("Grills").child("buying_price").setValue(buying_price.getText().toString().trim());
                            databaseReference.child("VicmakStock").child("Grills").child("selling_price").setValue(selling_price.getText().toString().trim());
                            databaseReference.child("VicmakStock").child("Grills").child("stock").setValue(stock.getText().toString().trim());

                            int commission = Integer.parseInt(selling_price.getText().toString().trim()) - Integer.parseInt(buying_price.getText().toString().trim());

                            databaseReference.child("VicmakStock").child("Grills").child("commission").setValue(commission + "");

                            int sold = Integer.parseInt(snapshot.child("sold").getValue(String.class) + "");
                            done.countDown();
                            try{
                                done.await();
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }
                            databaseReference.child("VicmakStock").child("Grills").child("total_commission").setValue((sold * commission) + "");

                            Toast.makeText(UpdateGrills.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
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