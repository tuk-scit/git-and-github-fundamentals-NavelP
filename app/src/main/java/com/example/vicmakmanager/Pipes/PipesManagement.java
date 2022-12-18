package com.example.vicmakmanager.Pipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vicmakmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PipesManagement extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");
    TextView stock, sold, commission;

    TextView summaryTotalStock, summaryTotalSold, summaryTotalCommission;
    TextView singleTotalStock, singleTotalSold, singleTotalCommission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipes_management);

        getSupportActionBar().hide();

        stock = findViewById(R.id.pipes_stock);
        sold = findViewById(R.id.pipes_sold);
        commission = findViewById(R.id.pipes_commission);

        summaryTotalStock = findViewById(R.id.total_regulator_number_summary_t);
        summaryTotalSold = findViewById(R.id.sold_regulator_t);
        summaryTotalCommission = findViewById(R.id.regulator_commission_summary_t);

        singleTotalStock = findViewById(R.id.total_regulator_number_summary_13kg);
        singleTotalSold = findViewById(R.id.regulator_sold_summary_13kg);
        singleTotalCommission = findViewById(R.id.regulator_commission_summary_13kg);

        databaseReference.child("VicmakStock").child("Pipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stock.setText(snapshot.child("stock").getValue(String.class));
                sold.setText(snapshot.child("sold").getValue(String.class));
                commission.setText(snapshot.child("commission").getValue(String.class));

                singleTotalStock.setText(snapshot.child("stock").getValue(String.class));
                singleTotalSold.setText(snapshot.child("sold").getValue(String.class));
                singleTotalCommission.setText(snapshot.child("commission").getValue(String.class));

                summaryTotalStock.setText(snapshot.child("stock").getValue(String.class));
                summaryTotalSold.setText(snapshot.child("sold").getValue(String.class));
                summaryTotalCommission.setText(snapshot.child("total_commission").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Updatepipes(View view) {
        startActivity(new Intent(getApplicationContext(), UpdatePipes.class));
    }
}