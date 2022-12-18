package com.example.vicmakmanager.Gas;

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

public class UpdateGas extends AppCompatActivity {

    EditText refilling_price, selling_price, empty_price, complete_price, full, empty;
    Button update, cancel;
    TextView heading;
    String textRefilling_price, textSelling_price, textEmpty_price, textComplete_price, textItemName, textItemWeight, textFull, textEmpty, textRangiType;
    Bundle bundle;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_gas);

        getSupportActionBar().hide();

        refilling_price = findViewById(R.id.refilling_price);
        selling_price = findViewById(R.id.selling_price);

        empty_price = findViewById(R.id.empty_cylinder_price);
        complete_price = findViewById(R.id.complete_gas_price);
        full = findViewById(R.id.full);
        empty = findViewById(R.id.empty);

        update = findViewById(R.id.update);
        cancel = findViewById(R.id.cancel);

        bundle = getIntent().getExtras();
        assert bundle != null;
        textItemName = bundle.getString("item_name");
        textItemWeight = bundle.getString("item_weight");
        heading = findViewById(R.id.update_gas_heading);

        if (textItemName.equals("RangiGas")) {
            textRangiType = bundle.getString("rangi_type");
            heading.setText("Update " + textItemName + " " + textItemWeight + " " + textRangiType);
        } else {
            heading.setText("Update " + textItemName + " " + textItemWeight);
        }

        databaseReference.child("VicmakStock").child("Gas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(textItemName)) {
                    if (textItemName.equals("RangiGas")) {
                        refilling_price.setText(snapshot.child(textItemName).child(textItemWeight).child(textRangiType).child("refilling_price").getValue().toString());
                        selling_price.setText(snapshot.child(textItemName).child(textItemWeight).child(textRangiType).child("selling_price").getValue().toString());
                        empty_price.setText(snapshot.child(textItemName).child(textItemWeight).child(textRangiType).child("empty_price").getValue().toString());
                        complete_price.setText(snapshot.child(textItemName).child(textItemWeight).child(textRangiType).child("complete_price").getValue().toString());
                        full.setText(snapshot.child(textItemName).child(textItemWeight).child(textRangiType).child("full").getValue().toString());
                        empty.setText(snapshot.child(textItemName).child(textItemWeight).child(textRangiType).child("empty").getValue().toString());
                    } else {
                        refilling_price.setText(snapshot.child(textItemName).child(textItemWeight).child("refilling_price").getValue().toString());
                        selling_price.setText(snapshot.child(textItemName).child(textItemWeight).child("selling_price").getValue().toString());
                        empty_price.setText(snapshot.child(textItemName).child(textItemWeight).child("empty_price").getValue().toString());
                        complete_price.setText(snapshot.child(textItemName).child(textItemWeight).child("complete_price").getValue().toString());
                        full.setText(snapshot.child(textItemName).child(textItemWeight).child("full").getValue().toString());
                        empty.setText(snapshot.child(textItemName).child(textItemWeight).child("empty").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GasManagement.class));
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textRefilling_price = refilling_price.getText().toString().trim();
                textSelling_price = selling_price.getText().toString().trim();
                textEmpty_price = empty_price.getText().toString().trim();

                textComplete_price = complete_price.getText().toString().trim();
                textFull = full.getText().toString().trim();
                textEmpty = empty.getText().toString().trim();

                if (textRefilling_price.isEmpty() || textSelling_price.isEmpty() || textEmpty_price.isEmpty() ||
                        textComplete_price.isEmpty() || textItemName.isEmpty() || textItemWeight.isEmpty() || textFull.isEmpty() || textEmpty.isEmpty()) {
                    Toast.makeText(UpdateGas.this, "Can't submit black fields", Toast.LENGTH_SHORT).show();
                } else {
                    CountDownLatch done = new CountDownLatch(1);

                    databaseReference.child("VicmakStock").child("Gas").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            int stock = Integer.parseInt(textFull) + Integer.parseInt(textEmpty);

                            //if the gas is Rangi
                            if (textItemName.equals("RangiGas")) {
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child(textRangiType).child("refilling_price")
                                        .setValue(textRefilling_price);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child(textRangiType).child("selling_price")
                                        .setValue(textSelling_price);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child(textRangiType).child("empty_price")
                                        .setValue(textEmpty_price);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child(textRangiType).child("complete_price")
                                        .setValue(textComplete_price);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child(textRangiType).child("stock")
                                        .setValue(stock + "");
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child(textRangiType).child("full")
                                        .setValue(textFull);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child(textRangiType).child("empty").setValue(textEmpty);

                                int commission = Integer.parseInt(textRefilling_price) - Integer.parseInt(textSelling_price);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child(textRangiType).child("commission")
                                        .setValue(commission + "");

                                String exchanged_cylinders = snapshot.child(textItemName).child(textItemWeight).child(textRangiType).child("exchanged").getValue(String.class);
                                done.countDown();
                                try {
                                    done.await();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child(textRangiType).child("total_commission")
                                        .setValue((Integer.parseInt(exchanged_cylinders) * commission) + "");

                            } else {
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child("refilling_price").setValue(textRefilling_price);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child("selling_price").setValue(textSelling_price);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child("empty_price").setValue(textEmpty_price);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child("complete_price").setValue(textComplete_price);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child("stock").setValue(stock + "");
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child("full").setValue(textFull);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child("empty").setValue(textEmpty);

                                int commission = Integer.parseInt(textRefilling_price) - Integer.parseInt(textSelling_price);
                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child("commission")
                                        .setValue(commission + "");

                                String exchanged_cylinders = snapshot.child(textItemName).child(textItemWeight).child("exchanged").getValue(String.class);
                                done.countDown();
                                try {
                                    done.await();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                databaseReference.child("VicmakStock").child("Gas").child(textItemName).child(textItemWeight).child("total_commission")
                                        .setValue((Integer.parseInt(exchanged_cylinders) * commission) + "");
                            }

                            Toast.makeText(UpdateGas.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
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