package com.example.vicmakmanager.Regulators;

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

public class UpdateRegulators extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");
    TextView heading, buying_price, selling_price, stock;
    String textBuyingPrice, textSellingPrice, textStock;
    Button update, cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_regulators);

        getSupportActionBar().hide();

        heading = findViewById(R.id.update_regulator_heading);
        buying_price = findViewById(R.id.buying_price);
        selling_price = findViewById(R.id.selling_price);
        stock = findViewById(R.id.stock);

        update = findViewById(R.id.update);
        cancel = findViewById(R.id.cancel);


        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        String regulator_name = bundle.getString("item_name");
        String regulator_type = bundle.getString("item_type");

        heading.setText(regulator_name + " " + regulator_type);

        databaseReference.child("VicmakStock").child("Regulators").child(regulator_type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stock.setText(snapshot.child("stock").getValue(String.class));
                buying_price.setText(snapshot.child("buying_price").getValue(String.class));
                selling_price.setText(snapshot.child("selling_price").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegulatorsManagement.class));
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                textBuyingPrice = buying_price.getText().toString().trim();
                textSellingPrice = selling_price.getText().toString().trim();
                textStock = stock.getText().toString().trim();

                if(textSellingPrice.isEmpty() || textBuyingPrice.isEmpty() || textStock.isEmpty()){
                    Toast.makeText(UpdateRegulators.this, "Cannot submit blank details", Toast.LENGTH_SHORT).show();
                }else{

                    databaseReference.child("VicmakStock").child("Regulators").child(regulator_type).addListenerForSingleValueEvent(new ValueEventListener() {
                        CountDownLatch done = new CountDownLatch(1);
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            databaseReference.child("VicmakStock").child("Regulators").child(regulator_type).child("buying_price").setValue(textBuyingPrice);
                            databaseReference.child("VicmakStock").child("Regulators").child(regulator_type).child("selling_price").setValue(textSellingPrice);
                            databaseReference.child("VicmakStock").child("Regulators").child(regulator_type).child("stock").setValue(textStock);

                            int commission = Integer.parseInt(snapshot.child("selling_price").getValue(String.class) + "") - Integer.parseInt(snapshot.child("selling_price").getValue(String.class) + "");
                            int sold = Integer.parseInt(snapshot.child("sold").getValue(String.class) + "");
                            int total_commission = sold * commission;
                            done.countDown();
                            try{
                                done.await();
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }
                            databaseReference.child("VicmakStock").child("Regulators").child(regulator_type).child("commission").setValue(commission + "");
                            databaseReference.child("VicmakStock").child("Regulators").child(regulator_type).child("total-commission").setValue(total_commission + "");

                            Toast.makeText(UpdateRegulators.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
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