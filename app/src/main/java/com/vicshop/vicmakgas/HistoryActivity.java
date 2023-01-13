package com.vicshop.vicmakgas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    history_display_adapter adapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com");

    String textPhonenumber = AccountActivity.textPhonenumber;

    List<history_display_p> ordered_items;
    TextView history_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        getSupportActionBar().hide();

        history_status = findViewById(R.id.no_history);
        ordered_items = new ArrayList<>();
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(textPhonenumber.equals("")){
                    startActivity(new Intent(HistoryActivity.this, Login.class));
                }

                if(snapshot.hasChild(textPhonenumber)){

                    if(snapshot.child(textPhonenumber).child("History").getChildrenCount() == 0){
                        history_status.setText("No History, Try Ordering something");
                    }else{
                        for(DataSnapshot Child : snapshot.child(textPhonenumber).child("History").getChildren()){

                            ordered_items.add(new history_display_p(Child.child("Date-Delivered").getValue(String.class) +"",""+Child.getKey().split("__")[0].replace("_", "/"),
                                    ""+Child.child("Status").getValue(),""+Child.child("Time-Delivered").getValue(), ""+Child.getKey().split("__")[1],
                                    "" + Child.child("image_url").getValue(String.class), "" + Child.child("item_name").getValue(String.class),
                                    "" +Child.child("item_price").getValue(String.class)));
                        }

                        recyclerView = findViewById(R.id.history_recycle_view);
                        recyclerView.setHasFixedSize(true);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

                        recyclerView.setLayoutManager(linearLayoutManager);

                        adapter = new history_display_adapter(getApplicationContext(), ordered_items);
                        recyclerView.setAdapter(adapter);
                    }

                }else{
                    history_status.setText("No history, Don't have an account");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}