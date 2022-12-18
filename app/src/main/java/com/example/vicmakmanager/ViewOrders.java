package com.example.vicmakmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.vicmakmanager.Orders.ManipulateOrders;
import com.example.vicmakmanager.Orders.orders_adapter;
import com.example.vicmakmanager.Orders.orders_p;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewOrders extends AppCompatActivity implements ManipulateOrders {

    RecyclerView recyclerView;
    public static ArrayList<orders_p> undelivered_orders;
    orders_adapter adapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.ViewOrderRecycleView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new orders_adapter(undelivered_orders, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);


    }

    private String determineCategory(String name){
        if(name.split(" ").length == 1){
            return name;
        }else{
            name = name.split(" ")[0];

            if(name.substring((name.length() - 3), name.length()).equals("Gas")){
                return "Gas";
            }else{
                return name;
            }
        }

    }

    @Override
    public void DeliverOrder(int order_number, String phoneNumber, String date_time, String payment_shop,
                             String payment_status, String delivery_guy,  String item_name, String deposit, String item_price) {

        databaseReference.child("users").child(phoneNumber).
                child("History").child(date_time).child("Status").setValue("Delivered");
        databaseReference.child("users").child(phoneNumber).
                child("History").child(date_time).child("Time-Delivered").setValue(getTime());

        databaseReference.child("users").child(phoneNumber).
                child("History").child(date_time).child("Date-Delivered").setValue(getDate());

        databaseReference.child("users").child(phoneNumber).
                child("History").child(date_time).child("delivery_guy").setValue(delivery_guy);

        databaseReference.child("users").child(phoneNumber).
                child("History").child(date_time).child("payment_status").setValue(payment_status);

        databaseReference.child("users").child(phoneNumber).
                child("History").child(date_time).child("payment_shop").setValue(payment_shop);

        if(payment_status.equals("Credit")){

            databaseReference.child("users").child(phoneNumber).
                    child("History").child(date_time).child("deposit").setValue(deposit);

            String credit_amount = (Integer.parseInt(item_price) - Integer.parseInt(deposit)) + "";

            databaseReference.child("users").child(phoneNumber).
                    child("History").child(date_time).child("credit_amount").setValue(credit_amount);

            //updating the credits table
            databaseReference.child("delivered_orders").child(payment_shop).child("credits").
                    child(phoneNumber).child(date_time.replace("/", "_")).child("deposit_paid").setValue(deposit);
            databaseReference.child("delivered_orders").child(payment_shop).child("credits").
                    child(phoneNumber).child(date_time.replace("/", "_")).child("credit_amount").setValue(credit_amount);
            databaseReference.child("delivered_orders").child(payment_shop).child("credits").
                    child(phoneNumber).child(date_time.replace("/", "_")).child("item_price").setValue(item_price);

            databaseReference.child("delivered_orders").child(payment_shop).child("credits").
                    child(phoneNumber).child(date_time.replace("/", "_")).child("item_name").setValue(item_name);

            databaseReference.child("delivered_orders").child(payment_shop).child("credits").
                    child(phoneNumber).child(date_time.replace("/", "_")).child("category").setValue(
                            determineCategory(item_name)
                    );
        }

        if(payment_status.equals("Paid on delivery")){

            //updating the cash paid table
            databaseReference.child("delivered_orders").child(payment_shop).child("paid_on_delivery").
                    child(phoneNumber).child(date_time.replace("/", "_")).child("item_name").setValue(item_name);
            databaseReference.child("delivered_orders").child(payment_shop).child("paid_on_delivery").
                    child(phoneNumber).child(date_time.replace("/", "_")).child("category").setValue(
                            determineCategory(item_name)
                    );

        }

        UpdateStock(item_name);

        undelivered_orders.remove(order_number);

        adapter = new orders_adapter(undelivered_orders, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void confirmOrder(String phone) {
        startActivity(new Intent(
                Intent.ACTION_DIAL, Uri.parse("tel:" + phone)
        ));
    }

    private void UpdateStock(String name){
        databaseReference.child("VicmakStock").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check if it is rangi
                String [] item_properties = name.split(" ");
                String item_name,item_sub_cat = "",item_sub_cat2 = "";

                if(item_properties.length == 3){
                    item_name = item_properties[0];
                    item_sub_cat = item_properties[1];
                    item_sub_cat2 = item_properties[2];

                }else if(item_properties.length == 2){
                    item_name = item_properties[0];
                    item_sub_cat = item_properties[1];


                }else{
                    item_name = item_properties[0];

                }

                for(DataSnapshot VicmakStockItem : snapshot.getChildren()){

                    if(VicmakStockItem.hasChild(item_name) ||
                    VicmakStockItem.getKey().equals(item_name)){

                        int current_full, current_empty, current_exchanged;

                        if(item_properties.length == 3){
                            current_full = Integer.parseInt(VicmakStockItem.child(item_name).child(item_sub_cat).child(item_sub_cat2).child("full")
                                    .getValue(String.class) + "");
                            current_empty = Integer.parseInt(VicmakStockItem.child(item_name).child(item_sub_cat).child(item_sub_cat2).child("empty")
                                    .getValue(String.class) + "");

                            current_exchanged = Integer.parseInt(VicmakStockItem.child(item_name).child(item_sub_cat).child(item_sub_cat2).child("exchanged")
                                    .getValue(String.class) + "");

                            current_empty += 1;
                            current_full -= 1;
                            current_exchanged += 1;


                            databaseReference.child("VicmakStock").child(VicmakStockItem.getKey()).child(item_name).child(item_sub_cat).child(item_sub_cat2).child("empty")
                                    .setValue(current_empty + "");
                            databaseReference.child("VicmakStock").child(VicmakStockItem.getKey()).child(item_name).child(item_sub_cat).child(item_sub_cat2).child("full")
                                    .setValue(current_full + "");
                            databaseReference.child("VicmakStock").child(VicmakStockItem.getKey()).child(item_name).child(item_sub_cat).child(item_sub_cat2).child("exchanged")
                                    .setValue(current_exchanged + "");
                        }else if(item_properties.length == 2){

                            if(VicmakStockItem.getKey().equals("Regulators") || VicmakStockItem.getKey().equals("Burners")){
                                int current_sold = Integer.parseInt(VicmakStockItem.child(item_sub_cat).child("sold").getValue(String.class) + "");
                                int current_stock = Integer.parseInt(VicmakStockItem.child(item_sub_cat).child("stock").getValue(String.class) + "");

                                current_sold += 1;
                                current_stock -= 1;

                                databaseReference.child("VicmakStock").child(VicmakStockItem.getKey()).child(item_sub_cat).child("sold").setValue(current_sold + "");
                                databaseReference.child("VicmakStock").child(VicmakStockItem.getKey()).child(item_sub_cat).child("stock").setValue(current_stock + "");

                            }else{
                                current_full = Integer.parseInt(VicmakStockItem.child(item_name).child(item_sub_cat).child("full").getValue(String.class) + "");
                                current_empty = Integer.parseInt(VicmakStockItem.child(item_name).child(item_sub_cat).child("empty").getValue(String.class) + "");
                                current_exchanged = Integer.parseInt(VicmakStockItem.child(item_name).child(item_sub_cat).child("exchanged").getValue(String.class) + "");

                                current_empty += 1;
                                current_full -= 1;
                                current_exchanged += 1;

                                databaseReference.child("VicmakStock").child(VicmakStockItem.getKey()).child(item_name).child(item_sub_cat).child("empty").
                                        setValue(current_empty + "");
                                databaseReference.child("VicmakStock").child(VicmakStockItem.getKey()).child(item_name).child(item_sub_cat).child("full").
                                        setValue(current_full + "");
                                databaseReference.child("VicmakStock").child(VicmakStockItem.getKey()).child(item_name).child(item_sub_cat).child("exchanged").
                                        setValue(current_exchanged + "");
                            }
                        }else {
                            int current_sold = Integer.parseInt(VicmakStockItem.child("sold").getValue(String.class) + "");
                            int current_stock = Integer.parseInt(VicmakStockItem.child("stock").getValue(String.class) + "");

                            current_sold += 1;
                            current_stock -= 1;

                            databaseReference.child("VicmakStock").child(VicmakStockItem.getKey()+"").child("sold").setValue(current_sold + "");
                            databaseReference.child("VicmakStock").child(VicmakStockItem.getKey()+"").child("stock").setValue(current_stock + "");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private String getDate() {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date = simpleDateFormat.format(calendar.getTime());

        return Date;
    }

    private String getTime() {
        calendar = Calendar.getInstance();

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        Date = simpleDateFormat.format(calendar.getTime());

        return Date;
    }

}