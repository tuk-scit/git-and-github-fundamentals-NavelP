package com.example.vicmakmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vicmakmanager.Regulators.RefillingManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RefillingGas extends AppCompatActivity implements RefillingManager {

    refill_cylinder_adapter adapter;
    ArrayList<refill_cylinder_p> cylinders;
    RecyclerView recyclerView;
    String image_url;

    Button displayDialog;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");

    public static ArrayList<gas_refiller_p> GasesToRefill = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refilling_gas);

        getSupportActionBar().hide();
        cylinders = new ArrayList<>();

        displayDialog = findViewById(R.id.refill_gas_button);

        displayDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayOnDialogBox();
            }
        });

        databaseReference.child("VicmakStock").child("Gas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cylinders = new ArrayList<>();
                for (DataSnapshot gas_name : snapshot.getChildren()) {
                    if (gas_name.getKey().equals("general_info")) {
                        continue;
                    }
                    for (DataSnapshot gas_weight : gas_name.getChildren()) {

                        if (gas_weight.getKey().equals("25kg")) continue;

                        if (gas_name.getKey().equals("RangiGas")) {

                            for (DataSnapshot rangi_type : gas_weight.getChildren()) {
                                image_url = rangi_type.child("image_url").getValue(String.class);

                                String gas_name_d = gas_name.getKey();
                                String gas_weight_d = gas_weight.getKey() + " " + rangi_type.getKey();
                                String gas_price = rangi_type.child("refilling_price").getValue(String.class);

                                cylinders.add(new refill_cylinder_p(image_url, gas_name_d, gas_weight_d, gas_price));
                            }

                        } else {
                            image_url = gas_weight.child("image_url").getValue(String.class);

                            String gas_name_d = gas_name.getKey();
                            String gas_weight_d = gas_weight.getKey();
                            String gas_price = gas_weight.child("refilling_price").getValue(String.class);

                            cylinders.add(new refill_cylinder_p(image_url, gas_name_d, gas_weight_d, gas_price));
                        }
                    }
                }
                recyclerView = findViewById(R.id.refillRecycleView);

                recyclerView.setHasFixedSize(true);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(gridLayoutManager);

                adapter = new refill_cylinder_adapter(getApplicationContext(), cylinders, RefillingGas.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void DisplayOnDialogBox() {
        view_refilling_details_adapter confirmDetailsAdapter;
        Button submitDetails, editDetails;

        View confirm_refilling_view = getLayoutInflater().inflate(R.layout.refiller_manager, null);

        submitDetails = confirm_refilling_view.findViewById(R.id.submitCylinders);
        editDetails = confirm_refilling_view.findViewById(R.id.editDetails);


        RecyclerView ConfirmRecyclerView = confirm_refilling_view.findViewById(R.id.viewRefillingCylinders);

        ConfirmRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ConfirmRecyclerView.setLayoutManager(linearLayoutManager);

        confirmDetailsAdapter = new view_refilling_details_adapter(getApplicationContext(), GasesToRefill);
        ConfirmRecyclerView.setAdapter(confirmDetailsAdapter);

        AlertDialog alert;
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(RefillingGas.this);
        builder.setView(confirm_refilling_view);

        alert = builder.create();

        alert.show();

        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("VicmakStock").child("Gas").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (int i = 0; i < GasesToRefill.size(); i++) {
                            String current_empty_cylinders;

                            if(IsRangi(GasesToRefill.get(i).getGas_weight())){
                                current_empty_cylinders = snapshot.child(GasesToRefill.get(i).getGas_name()).child(GasesToRefill.get(i).getGas_weight().split(" ")[0]).
                                        child(GasesToRefill.get(i).getGas_weight().split(" ")[1]).child("empty")
                                        .getValue(String.class);
                            }
                            else {
                                current_empty_cylinders = snapshot.child(GasesToRefill.get(i).getGas_name()).child(GasesToRefill.get(i).getGas_weight()).child("empty")
                                        .getValue(String.class);
                            }
                            String new_empty = (Integer.parseInt(current_empty_cylinders) - Integer.parseInt(GasesToRefill.get(i).getCylinder_number())) + "";

                            if (Integer.parseInt(new_empty) < 0) {
                                AlertDialog alert;
                                AlertDialog.Builder builder = new AlertDialog.Builder(RefillingGas.this);
                                builder.setTitle("Submitting Invalid Data");
                                builder.setMessage("Empty cylinders entered for " + GasesToRefill.get(i).getGas_name() + " " + GasesToRefill.get(i).getGas_weight()
                                        + " is below threshold");

                                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                alert = builder.create();

                                alert.show();

                                break;
                            } else {

                                if(IsRangi(GasesToRefill.get(i).getGas_weight())){

                                    databaseReference.child("VicmakStock").child("Gas").child(GasesToRefill.get(i).getGas_name()).
                                            child(GasesToRefill.get(i).getGas_weight().split(" ")[0]).child(GasesToRefill.get(i).getGas_weight().split(" ")[1])
                                            .child("empty").setValue(new_empty);
                                    String current_full_cylinders = snapshot.child(GasesToRefill.get(i).getGas_name()).
                                            child(GasesToRefill.get(i).getGas_weight().split(" ")[0]).child(GasesToRefill.get(i).getGas_weight().split(" ")[1])
                                            .child("full").getValue(String.class);

                                    String new_full_cylinders = (Integer.parseInt(current_full_cylinders) + Integer.parseInt(GasesToRefill.get(i).getCylinder_number())) + "";

                                    databaseReference.child("VicmakStock").child("Gas").child(GasesToRefill.get(i).getGas_name()).
                                            child(GasesToRefill.get(i).getGas_weight().split(" ")[0]).child(GasesToRefill.get(i).getGas_weight().split(" ")[1])
                                            .child("full").setValue(new_full_cylinders);
                                }else{
                                    databaseReference.child("VicmakStock").child("Gas").child(GasesToRefill.get(i).getGas_name()).child(GasesToRefill.get(i).getGas_weight())
                                            .child("empty").setValue(new_empty);
                                    String current_full_cylinders = snapshot.child(GasesToRefill.get(i).getGas_name()).child(GasesToRefill.get(i).getGas_weight()).child("full")
                                            .getValue(String.class);

                                    String new_full_cylinders = (Integer.parseInt(current_full_cylinders) + Integer.parseInt(GasesToRefill.get(i).getCylinder_number())) + "";

                                    databaseReference.child("VicmakStock").child("Gas").child(GasesToRefill.get(i).getGas_name()).child(GasesToRefill.get(i).getGas_weight())
                                            .child("full").setValue(new_full_cylinders);
                                }
                            }
                        }

                        alert.dismiss();
                        GasesToRefill = new ArrayList<>();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public void strikeBGColor(View itemView) {
        itemView.setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.item_bg))
        );
    }

    private boolean IsRangi(String gas_weight) {
        return gas_weight.split(" ").length != 1;
    }
}