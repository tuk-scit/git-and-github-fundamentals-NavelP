package com.example.vicmakmanager.Burners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vicmakmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

public class UpdateBurners extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");
    EditText selling_price, buying_price, stock;
    Button update, cancel;
    TextView heading;
    String textSellingPrice, textBuyingPrice, textStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_burners);

        getSupportActionBar().hide();

        heading = findViewById(R.id.update_burner_heading);
        selling_price = findViewById(R.id.selling_price);
        buying_price = findViewById(R.id.buying_price);
        stock = findViewById(R.id.burner_stock);

        Bundle bundle = getIntent().getExtras();

        assert bundle != null;

        String name = bundle.getString("item_name");
        String type = bundle.getString("item_type");

        heading.setText(name + " " + type);

        update = findViewById(R.id.update);
        cancel = findViewById(R.id.cancel);

        databaseReference.child("VicmakStock").child("Burners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selling_price.setText(snapshot.child(type).child("selling_price").getValue(String.class));
                buying_price.setText(snapshot.child(type).child("buying_price").getValue(String.class));
                stock.setText(snapshot.child(type).child("stock").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSellingPrice = selling_price.getText().toString().trim();
                textBuyingPrice = buying_price.getText().toString().trim();
                textStock = stock.getText().toString().trim();

                if (textBuyingPrice.isEmpty() || textSellingPrice.isEmpty() || textStock.isEmpty()) {
                    Toast.makeText(UpdateBurners.this, "Cannot submit blank fields", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("VicmakStock").child("Burners").addListenerForSingleValueEvent(new ValueEventListener() {
                        CountDownLatch done = new CountDownLatch(1);

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child("VicmakStock").child("Burners").child(type).child("stock").setValue(textStock);
                            databaseReference.child("VicmakStock").child("Burners").child(type).child("selling_price").setValue(textSellingPrice);
                            databaseReference.child("VicmakStock").child("Burners").child(type).child("buying_price").setValue(textBuyingPrice);

                            int commission = Integer.parseInt(textSellingPrice) - Integer.parseInt(textBuyingPrice);
                            databaseReference.child("VicmakStock").child("Burners").child(type).child("commission").setValue(commission + "");

                            int sold = Integer.parseInt(snapshot.child(type).child("sold").getValue(String.class) + "");
                            done.countDown();

                            try {
                                done.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            databaseReference.child("VicmakStock").child("Burners").child(type).child("total-commission").
                                    setValue((sold * commission) + "");

                            Toast.makeText(UpdateBurners.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BurnerManagement.class));
                finish();
            }
        });

    }
}