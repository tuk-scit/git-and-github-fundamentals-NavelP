package com.example.vicmakmanager.Gas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vicmakmanager.ActivityChanger;
import com.example.vicmakmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GasManagement extends AppCompatActivity implements view_item_form {
    RecyclerView recyclerView;
    List<gas_cylinders_p> list;
    List<gas_cylinder_p_with_summary> summary_list;
    gas_cylinder_adapter_summary adapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");
    int stock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_management);

        getSupportActionBar().hide();

        summary_list = new ArrayList<>();

        databaseReference.child("VicmakStock").child("Gas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stock = 0;
                for(DataSnapshot snap : snapshot.getChildren()){
                    list = new ArrayList<>();

                    if(snap.hasChild("TotalStock")){
                        continue;
                    }
                    for(DataSnapshot snap_weight : snap.getChildren()){

                        if(snap.getKey().equals("RangiGas")){
                            for(DataSnapshot rangi_type : snap_weight.getChildren()){
                                stock = 0;
                                stock += Integer.parseInt(rangi_type.child("stock").getValue(String.class));
                                String full = rangi_type.child("full").getValue(String.class);
                                String empty = rangi_type.child("empty").getValue(String.class);
                                list.add(new gas_cylinders_p(snap.getKey() + "", snap_weight.getKey() + " " +
                                        rangi_type.getKey(), stock + "",
                                        empty + "", full + "", R.drawable.pro_2, rangi_type.child("exchanged").getValue() + "",
                                        rangi_type.child("total_commission").getValue() + "",
                                        rangi_type.child("commission").getValue() + ""));
                            }
                        }else{
                            stock = 0;
                            stock += Integer.parseInt(snap_weight.child("stock").getValue().toString());
                            String full = snap_weight.child("full").getValue().toString();
                            String empty = snap_weight.child("empty").getValue().toString();
                            list.add(new gas_cylinders_p(snap.getKey() + "", snap_weight.getKey() + "", stock + "",
                                    empty + "", full + "", R.drawable.pro_2, snap_weight.child("exchanged").getValue() + "",
                                    snap_weight.child("total_commission").getValue() + "",
                                    snap_weight.child("commission").getValue() + ""));
                        }

                    }
                    summary_list.add(new gas_cylinder_p_with_summary(list));
                }

                recyclerView = findViewById(R.id.gas_cylinders_recycle_view_with_summary);
                recyclerView.setHasFixedSize(true);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);

                adapter = new gas_cylinder_adapter_summary(getApplicationContext(), summary_list, GasManagement.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onclick(String name, String weight) {
        Intent intent = new Intent(GasManagement.this, UpdateGas.class);
        intent.putExtra("item_name", name);
        intent.putExtra("item_weight", weight);
        startActivity(intent);
    }

    @Override
    public void onclick(String name, String weight, String RangiType) {
        Intent intent = new Intent(GasManagement.this, UpdateGas.class);
        intent.putExtra("item_name", name);
        intent.putExtra("item_weight", weight);
        intent.putExtra("rangi_type", RangiType);
        startActivity(intent);
    }
}