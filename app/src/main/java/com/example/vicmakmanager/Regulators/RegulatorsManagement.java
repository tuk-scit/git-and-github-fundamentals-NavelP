package com.example.vicmakmanager.Regulators;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vicmakmanager.Burners.burner_adapter;
import com.example.vicmakmanager.Burners.burner_p;
import com.example.vicmakmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegulatorsManagement extends AppCompatActivity implements view_form_items{


    RecyclerView recyclerView;
    regulator_adapter adapter;
    List<regulator_p> list;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");

    TextView stockTotal, stock13kg, stock6kg, stockAmpia, stockcosco, stockGeneric_tecno, stockpac;
    TextView soldTotal, sold13kg, sold6kg, soldAmpia, soldcosco, soldGeneric_tecno, soldpac;
    TextView commissionTotal, commission13kg, commission6kg, commissionAmpia, commissioncosco, commissionGeneric_tecno, commissionpac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regulator_management);

        getSupportActionBar().hide();

        list = new ArrayList<>();

        recyclerView = findViewById(R.id.regulators_recycle_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new regulator_adapter(this, list, this);
        recyclerView.setAdapter(adapter);

        stockTotal = findViewById(R.id.total_regulator_number_summary_t);
        stock13kg = findViewById(R.id.total_regulator_number_summary_13kg);
        stock6kg = findViewById(R.id.total_regulator_number_summary_6kg);
        stockAmpia = findViewById(R.id.total_regulator_number_summary_ampia);
        stockcosco = findViewById(R.id.total_regulator_number_summary_cosco);
        stockGeneric_tecno = findViewById(R.id.total_regulator_number_summary_generic_tecno);
        stockpac = findViewById(R.id.total_regulator_number_summary_pac);


        soldTotal = findViewById(R.id.sold_regulator_t);
        sold13kg = findViewById(R.id.regulator_sold_summary_13kg);
        sold6kg = findViewById(R.id.regulator_sold_summary_6kg);
        soldAmpia = findViewById(R.id.sold_summary_ampia);
        soldcosco = findViewById(R.id.sold_summary_cosco);
        soldGeneric_tecno = findViewById(R.id.sold_summary_generic_tecno);
        soldpac = findViewById(R.id.sold_summary_pac);

        commissionTotal = findViewById(R.id.regulator_commission_summary_t);
        commission13kg = findViewById(R.id.regulator_commission_summary_13kg);
        commission6kg = findViewById(R.id.regulator_commission_summary_6kg);
        commissionAmpia = findViewById(R.id.commission_summary_ampia);
        commissioncosco = findViewById(R.id.commission_summary_cosco);
        commissionGeneric_tecno = findViewById(R.id.commission_summary_generic_tecno);
        commissionpac = findViewById(R.id.commission_summary_pac);


        databaseReference.child("VicmakStock").child("Regulators").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot regulator_type : snapshot.getChildren()){
                    if(regulator_type.getKey().equals("general_info")) continue;
                    list.add(new regulator_p(regulator_type.getKey() + "", regulator_type.child("stock").getValue() + "",
                            regulator_type.child("sold").getValue(String.class) + "", regulator_type.child("commission").getValue(String.class) + "",
                            R.drawable.regulator_d));

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TextView [] STOCK = {stock13kg, stock6kg, stockAmpia, stockcosco, stockGeneric_tecno, stockpac, stockTotal};
        TextView [] SOLD = {sold13kg, sold6kg, soldAmpia, soldcosco, soldGeneric_tecno, soldpac, soldTotal};
        TextView [] COMMISSION = {commission13kg, commission6kg, commissionAmpia, commissioncosco, commissionGeneric_tecno, commissionpac, commissionTotal};

        databaseReference.child("VicmakStock").child("Regulators").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;

                for (DataSnapshot regulator_type : snapshot.getChildren()) {

                    if (regulator_type.getKey().equals("general_info")) {
                        soldTotal.setText(regulator_type.child("sold").getValue(String.class));
                        commissionTotal.setText(regulator_type.child("TotalCommission").getValue(String.class));
                        stockTotal.setText(regulator_type.child("TotalStock").getValue(String.class));
                    } else {

                        STOCK[i].setText(regulator_type.child("stock").getValue(String.class));
                        SOLD[i].setText(regulator_type.child("sold").getValue(String.class));
                        COMMISSION[i].setText(regulator_type.child("total_commission").getValue(String.class));

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
    public void onclick(List<regulator_p> list, int position) {
        Intent intent = new Intent(getApplicationContext(), UpdateRegulators.class);
        intent.putExtra("item_name", "Regulator");
        intent.putExtra("item_type", list.get(position).getRegulator_type());
        startActivity(intent);
    }
}