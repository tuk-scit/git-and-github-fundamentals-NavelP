package com.example.vicmakmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

public class TotalSales extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");

    RecyclerView recyclerView;
    ArrayList<total_sales_p> sales;
    total_sales_adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_total_sales, container, false);

        sales = new ArrayList<>();

        recyclerView = view.findViewById(R.id.totalSalesRecycle);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference.child("VicmakStock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sales = new ArrayList<>();
                for(DataSnapshot vicmak_item : snapshot.getChildren()){
                    //if it is grills or pipes
                    if(vicmak_item.getKey().equals("Grills") || vicmak_item.getKey().equals("Pipes")){
                        sales.add(new total_sales_p(vicmak_item.getKey(), vicmak_item.child("total_commission").getValue(String.class)));
                    }else{
                        sales.add(new total_sales_p(vicmak_item.getKey(), vicmak_item.child("general_info").child("TotalCommission").getValue(String.class)));
                    }
                }

                adapter = new total_sales_adapter(getContext(), sales);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}
