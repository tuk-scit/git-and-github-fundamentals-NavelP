package com.example.vicmakmanager.Burners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vicmakmanager.R;
import com.example.vicmakmanager.Regulators.regulator_p;
import com.example.vicmakmanager.Regulators.view_form_items;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BurnerManagement extends AppCompatActivity implements view_burner_update_form {
    RecyclerView recyclerView;
    burner_adapter adapter;
    List<burner_p> list;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");
    TextView stockTotal, stockGeneric, stockOrgaz, stockPatco, stockPrimus, stockSungus;
    TextView soldTotal, soldGeneric, soldOrgaz, soldPatco, soldPrimus, soldSungus;
    TextView commissionTotal, commissionGeneric, commissionOrgaz, commissionPatco, commmissionPrimus, commissionSungus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burner_management);

        getSupportActionBar().hide();

        list = new ArrayList<>();

        recyclerView = findViewById(R.id.burners_recycle_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new burner_adapter(this, list, this);
        recyclerView.setAdapter(adapter);

        databaseReference.child("VicmakStock").child("Burners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total_stock = 0;
                for (DataSnapshot burner_type : snapshot.getChildren()) {
                    if (burner_type.getKey().equals("general_info")) continue;
                    list.add(new burner_p(burner_type.getKey() + "", burner_type.child("stock").getValue() + "",
                            burner_type.child("sold").getValue(String.class) + "", burner_type.child("commission").getValue(String.class) + "",
                            R.drawable.burner_display));

                    total_stock += Integer.parseInt(burner_type.child("stock").getValue(String.class) + "");
                }
                adapter.notifyDataSetChanged();

                databaseReference.child("VicmakStock").child("Burners").child("general_info").child("TotalStock").setValue(total_stock + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        stockTotal = findViewById(R.id.total_burner_number_summary_t);
        stockGeneric = findViewById(R.id.total_burner_number_summary_generic);
        stockOrgaz = findViewById(R.id.total_burner_number_summary_orgaz);
        stockPatco = findViewById(R.id.total_burner_number_summary_patco);
        stockPrimus = findViewById(R.id.total_burner_number_summary_primus);
        stockSungus = findViewById(R.id.total_burner_number_summary_sungus);


        soldTotal = findViewById(R.id.sold_summary_t);
        soldGeneric = findViewById(R.id.sold_summary_generic);
        soldOrgaz = findViewById(R.id.sold_summary_orgaz);
        soldPatco = findViewById(R.id.sold_summary_patco);
        soldPrimus = findViewById(R.id.sold_summary_primus);
        soldSungus = findViewById(R.id.sold_summary_sungus);

        commissionTotal = findViewById(R.id.commission_summary_t);
        commissionGeneric = findViewById(R.id.commission_summary_generc);
        commissionOrgaz = findViewById(R.id.commission_summary_orgaz);
        commissionPatco = findViewById(R.id.commission_summary_patco);
        commmissionPrimus = findViewById(R.id.commission_summary_primus);
        commissionSungus = findViewById(R.id.commission_summary_sungus);

        TextView[] totalStock = {stockGeneric, stockOrgaz, stockPatco, stockPrimus, stockSungus, stockTotal};
        TextView[] sold = {soldGeneric, soldOrgaz, soldPatco, soldPrimus, soldSungus, soldTotal};
        TextView[] commission = {commissionGeneric, commissionOrgaz, commissionPatco, commmissionPrimus, commissionSungus, commissionTotal};

        databaseReference.child("VicmakStock").child("Burners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int i = 0;

                for (DataSnapshot burner_type : snapshot.getChildren()) {

                    if (burner_type.getKey().equals("general_info")) {
                        soldTotal.setText(burner_type.child("sold").getValue(String.class));
                        commissionTotal.setText(burner_type.child("TotalCommission").getValue(String.class));
                        stockTotal.setText(burner_type.child("TotalStock").getValue(String.class));
                    } else {

                        totalStock[i].setText(burner_type.child("stock").getValue(String.class));
                        sold[i].setText(burner_type.child("sold").getValue(String.class));
                        commission[i].setText(burner_type.child("total-commission").getValue(String.class));

                    }
                    i+= 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onclick(List<burner_p> list, int Position) {
        Intent intent = new Intent(getApplicationContext(), UpdateBurners.class);
        intent.putExtra("item_name", "Burner");
        intent.putExtra("item_type", list.get(Position).getBurner_type());
        startActivity(intent);
    }
}