package com.example.vicmakmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Credits extends Fragment implements creditInterface{

    ArrayList<credits_p> debtors;
    credits_adapter adapter;
    RecyclerView recyclerView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_credits, container, false);

        recyclerView = view.findViewById(R.id.credit_recycle);

        debtors = new ArrayList<>();

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference.child("delivered_orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot shop : snapshot.getChildren()){
                    for(DataSnapshot debtor : shop.child("credits").getChildren()){
                        for(DataSnapshot ordered_item : debtor.getChildren()){
                            if(!ordered_item.child("credit_amount").getValue(String.class).equals("0")){
                                debtors.add(new credits_p(debtor.getKey() + "",
                                        ordered_item.child("credit_amount").getValue(String.class) + "", ordered_item.getKey() + "",
                                        shop.getKey()));
                            }
                        }
                    }
                }

                adapter = new credits_adapter(debtors, getContext(), Credits.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    @Override
    public void payDebt(String phoneNumber, String date_time, String amount_paid,  String vicmakShop,  String credit_amount) {
        int credit_balance = Integer.parseInt(credit_amount) - Integer.parseInt(amount_paid);
        databaseReference.child("delivered_orders").child(vicmakShop).child("credits").
                child(phoneNumber).child(date_time).child("credit_amount").setValue(credit_balance + "");

        databaseReference.child("users").child(phoneNumber).child("History").
                child(date_time).child("credit_amount").setValue(credit_balance + "");

        if(credit_balance == 0){
            databaseReference.child("users").child(phoneNumber).child("History").
                    child(date_time).child("payment_status").setValue("Paid");

            databaseReference.child("delivered_orders").child(vicmakShop).child("credits").
                    child(phoneNumber).child(date_time).setValue(null);
        }
    }
}
