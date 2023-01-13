package com.vicshop.vicmakgas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity implements ListenSelectedItem {
    RecyclerView recyclerView;
    vicmak_display_adapter item_adapter;
    List<vicmak_display_p> list = new ArrayList<>();
    String selected_item = "Gas";
    BottomNavigationView bottomNavigationView;
    ImageView account_profile;
    TextView account_name;
    String Total_price = "0";

    Spinner spiner;
    TextView cart_counter;

    EditText search_item;

    public class OrderdItems {

        public String ordered_item_name;
        public int ordered_item_price;
        public Drawable getOrdered_item_image;

        public String getOrdered_item_name() {
            return ordered_item_name;
        }

        public void setOrdered_item_name(String ordered_item_name) {
            this.ordered_item_name = ordered_item_name;
        }

        public int getOrdered_item_price() {
            return ordered_item_price;
        }

        public void setOrdered_item_price(int ordered_item_price) {
            this.ordered_item_price = ordered_item_price;
        }

        public Drawable getGetOrdered_item_image() {
            return getOrdered_item_image;
        }

        public void setGetOrdered_item_image(Drawable getOrdered_item_image) {
            this.getOrdered_item_image = getOrdered_item_image;
        }

        public OrderdItems(String item_name, int item_price, Drawable getOrdered_item_image) {
            this.ordered_item_name = item_name;
            this.ordered_item_price = item_price;
            this.getOrdered_item_image = getOrdered_item_image;
        }
    }

    public List<OrderdItems> orderd_items;

    private class GetSimilarItems {
        public List<String> item_name = new ArrayList<>();
        public List<Integer> item_number = new ArrayList<>();

        public List<OrderdItems> list;

        public GetSimilarItems(List<OrderdItems> list) {
            this.list = list;
        }

        public void MatchSimilarItems() {
            int i, j;
            int counter = 1;

            for (i = 0; i < this.list.size(); i++) {
                item_name.add(this.list.get(i).getOrdered_item_name());
                for (j = 1; j < this.list.size(); j++) {
                    if (this.list.get(i).getOrdered_item_name().equals(this.list.get(j).getOrdered_item_name())) {
                        counter += 1;

                        this.list.remove(j);
                        j = j - 1;
                    }
                }
                this.item_number.add(counter);

                this.list.remove(i);
                i = i - 1;
                counter = 1;
            }
        }

        ;
    }

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    ArrayList<view_display_p> view_cart_list;
    public static RecyclerView recyclerViewCart;
    view_cart_adapter View_cart_adapter;
    View viewcart;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date;
    int total_price = 0;
    ImageView cart_icon;

    ListView searchedItems;

    List<String> SearchedItemsList;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com");
    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://vicmak-b2e1e.appspot.com");

    boolean rated_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        search_item = findViewById(R.id.search_bar);

        searchedItems = findViewById(R.id.searchedItems);
        searchedItems.setVisibility(View.GONE);

        spiner = (Spinner) findViewById(R.id.more_fragments);
        cart_counter = findViewById(R.id.cart_counter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.links, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);

        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_item = adapterView.getItemAtPosition(i).toString();

                databaseReference.child("VicmakStock").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (selected_item.equals("Gas")) {
                            list.clear();

                            for (DataSnapshot gas_type : snapshot.child("Gas").getChildren()) {

                                if (gas_type.hasChild("TotalStock")) continue;

                                for (DataSnapshot gas_weight : gas_type.getChildren()) {
                                    if (gas_weight.getKey().equals("25kg")) continue;

                                    if (gas_type.getKey().equals("RangiGas")) {
                                        for (DataSnapshot rangi_type : gas_weight.getChildren()) {
                                            // checking the availability of the item
                                            String num = rangi_type.child("full").getValue(String.class);
                                            list.add(new vicmak_display_p(gas_type.getKey() + " " + gas_weight.getKey() + " " + rangi_type.getKey(),
                                                    rangi_type.child("image_url").getValue(String.class),
                                                    rangi_type.child("selling_price").getValue(String.class),
                                                    num + "")
                                            );
                                        }
                                    } else {
                                        // checking the availability of the item
                                        String num = gas_weight.child("full").getValue(String.class);
                                        list.add(new vicmak_display_p(gas_type.getKey() + " " + gas_weight.getKey(),
                                                gas_weight.child("image_url").getValue(String.class),
                                                gas_weight.child("selling_price").getValue(String.class),
                                                num + "")
                                        );
                                    }
                                }


                            }
                            ItemDisplay();
                        } else if (selected_item.equals("Burners")) {
                            list.clear();
                            for (DataSnapshot burner : snapshot.child("Burners").getChildren()) {
                                // checking the availability of the item
                                String num = burner.child("stock").getValue(String.class);
                                if (burner.getKey().equals("general_info")) continue;
                                list.add(new vicmak_display_p("Burners " + burner.getKey(),
                                        burner.child("image_url").getValue(String.class),
                                        burner.child("selling_price").getValue(String.class),
                                        num + "")
                                );
                            }
                            ItemDisplay();
                        } else if (selected_item.equals("Grills")) {
                            list.clear();
                            String num = snapshot.child("Grills").child("stock").getValue(String.class);
                            list.add(new vicmak_display_p("Grills", R.drawable.grill_d,
                                    snapshot.child("Grills").child("selling_price").getValue(String.class),
                                    num + "")
                            );

                            ItemDisplay();
                        } else if (selected_item.equals("Pipes")) {
                            list.clear();
                            String num = snapshot.child("Pipes").child("stock").getValue(String.class);
                            list.add(new vicmak_display_p("Pipes", R.drawable.pipe_d,
                                    snapshot.child("Pipes").child("selling_price").getValue(String.class),
                                    num + "")
                            );

                            ItemDisplay();
                        } else if (selected_item.equals("Regulators")) {
                            list.clear();
                            for (DataSnapshot regulator : snapshot.child("Regulators").getChildren()) {
                                // checking the availability of the item
                                String num = regulator.child("stock").getValue(String.class);
                                if (regulator.getKey().equals("general_info")) continue;
                                list.add(new vicmak_display_p("Regulators " + regulator.getKey(),
                                        regulator.child("image_url").getValue(String.class),
                                        regulator.child("selling_price").getValue(String.class),
                                        num + "")
                                );

                            }
                            ItemDisplay();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ;
            }
        });

        bottomNavigationView = findViewById(R.id.vicmak_bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav);

        cart_icon = findViewById(R.id.cart_icon);

        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCart();
            }
        });
    }

    private void ItemDisplay() {

        recyclerView = findViewById(R.id.app_items);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        item_adapter = new vicmak_display_adapter(this, list, this);
        recyclerView.setAdapter(item_adapter);

    }

    @Override
    public void ListenSelectedItem_CartIncrement(TextView item_name, TextView item_price, ImageView item_image, int add_remove) {
        if (orderd_items == null) {
            orderd_items = new ArrayList<>();
        }

        findViewById(R.id.ToastCart).setVisibility(View.VISIBLE);

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.ToastCart).setVisibility(View.GONE);
            }
        };

        handler.postDelayed(runnable, 3000);

        findViewById(R.id.ToastCart).setVisibility(View.VISIBLE);
        int number_items = Integer.parseInt((String) cart_counter.getText());

        if (add_remove == -1) {
            number_items += 1;
        } else {
            number_items -= 1;
        }

        cart_counter.setText(String.valueOf(number_items));

        String new_item_price = item_price.getText().toString();

        if (add_remove == -1) {
            Total_price = (Integer.parseInt(Total_price) + Integer.parseInt(new_item_price)) + "";
        } else {
            Total_price = (Integer.parseInt(Total_price) - Integer.parseInt(new_item_price)) + "";
        }

        OrderdItems selected_item = new OrderdItems(item_name.getText().toString(), Integer.parseInt(new_item_price), item_image.getDrawable());

        if (add_remove == -1) {
            orderd_items.add(selected_item);
        } else {
            orderd_items.remove(add_remove);
        }
    }

    @Override
    public void addSimilarItem(TextView item_name, TextView item_price, ImageView item_image) {
        this.ListenSelectedItem_CartIncrement(item_name, item_price, item_image, -1);

        alertDialog.dismiss();
        this.ViewCart();
    }

    @Override
    public void removeSimilarItem(int adapter_position, TextView item_name, TextView item_price, ImageView item_image) {
        this.ListenSelectedItem_CartIncrement(item_name, item_price, item_image, adapter_position);

        alertDialog.dismiss();
        this.ViewCart();
    }

    public void ViewCart() {
        view_cart_list = new ArrayList<>();

        if (orderd_items == null) {
            orderd_items = new ArrayList<>();
        }

        alertDialogBuilder = new AlertDialog.Builder(this);
        viewcart = getLayoutInflater().inflate(R.layout.viewcart_activity, null);

        int i;

        for (i = 0; i < orderd_items.size(); i++) {
            view_cart_list.add(new view_display_p(orderd_items.get(i).getOrdered_item_name(), orderd_items.get(i).getOrdered_item_price() + "",
                    orderd_items.get(i).getGetOrdered_item_image()));
        }

        recyclerViewCart = viewcart.findViewById(R.id.viewcart_recycle);
        recyclerViewCart.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewCart.setLayoutManager(linearLayoutManager);

        View_cart_adapter = new view_cart_adapter(this, view_cart_list, this);
        recyclerViewCart.setAdapter(View_cart_adapter);

        alertDialogBuilder.setView(viewcart);

        alertDialogBuilder.setTitle("Ordered Items");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Go to Place Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setNegativeButton("Clear Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                orderd_items = new ArrayList<>();
                cart_counter.setText("0");
                dialogInterface.cancel();
                Total_price = "0";
            }
        });

        alertDialogBuilder.setMessage("Total Cost:  " + Total_price + "/=");
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void SearchOrder(View view) {
        String search_text = search_item.getText().toString().trim();

        SearchedItemsList = new ArrayList<>();

        if (search_text.isEmpty()) {
            Toast.makeText(this, "Please input item to search", Toast.LENGTH_SHORT).show();
        } else {
            list = new ArrayList<>();
            if ((search_text.charAt(0) == 'P') || (search_text.charAt(0) == 'p')) {
                SearchedItemsList = new ArrayList<>();

                SearchedItemsList.add("ProGas 6kg");
                SearchedItemsList.add("ProGas 13kg");
                SearchedItemsList.add("ProGas 25kg");
                SearchedItemsList.add("Complete ProGas 6kg");
                SearchedItemsList.add("Complete ProGas 13kg");
                SearchedItemsList.add("Complete ProGas 25kg");
            } else if ((search_text.charAt(0) == 'K') || (search_text.charAt(0) == 'k')) {
                SearchedItemsList = new ArrayList<>();

                SearchedItemsList.add("KGas 6kg");
                SearchedItemsList.add("KGas 13kg");
                SearchedItemsList.add("KGas 25kg");
                SearchedItemsList.add("Complete ProGas 6kg");
                SearchedItemsList.add("Complete ProGas 13kg");
                SearchedItemsList.add("Complete ProGas 25kg");
            } else if ((search_text.charAt(0) == 'R') || (search_text.charAt(0) == 'r')) {
                SearchedItemsList = new ArrayList<>();

                SearchedItemsList.add("RangiGas 6kg");
                SearchedItemsList.add("RangiGas 13kg");
                SearchedItemsList.add("RangiGas 25kg");
                SearchedItemsList.add("Complete RangiGas 6kg");
                SearchedItemsList.add("Complete RangiGas 13kg");
                SearchedItemsList.add("Complete RangiGas 25kg");
            } else if ((search_text.charAt(0) == 'T') || (search_text.charAt(0) == 't')) {
                SearchedItemsList = new ArrayList<>();

                SearchedItemsList.add("TotalGas 6kg");
                SearchedItemsList.add("TotalGas 13kg");
                SearchedItemsList.add("TotalGas 25kg");
                SearchedItemsList.add("Complete TotalGas 6kg");
                SearchedItemsList.add("Complete TotalGas 13kg");
                SearchedItemsList.add("Complete TotalGas 25kg");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, SearchedItemsList);
            searchedItems.setAdapter(adapter);

            searchedItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String selected_item = SearchedItemsList.get(i);

                    searchedItems.setVisibility(View.GONE);

                    databaseReference.child("VicmakStock").child("Gas").child(selected_item.split(" ")[0]).child(selected_item.split(" ")[1])
                            .addValueEventListener(new ValueEventListener() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    list.add(new vicmak_display_p((selected_item.split(" ")[0] + snapshot.getKey()),
                                            R.drawable.pro_2, snapshot.child("refilling_price").getValue() + ""));

                                    item_adapter.notifyDataSetChanged();
                                    ItemDisplay();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            });

            searchedItems.setVisibility(View.VISIBLE);
        }
    }

    public void PlaceOrder(View view) {

        if (orderd_items == null) {
            Toast.makeText(this, "You have not selected any item", Toast.LENGTH_SHORT).show();
            return;
        }

        if (AccountActivity.textPhonenumber.equals("")) {
            startActivity(new Intent(MainActivity.this, Login.class));
        } else {

            int i;

            for (i = 0; i < orderd_items.size(); i++) {
                total_price += orderd_items.get(i).ordered_item_price;
            }


            CountDownLatch done = new CountDownLatch(1);

            done.countDown();
            updateUserDbHistory();
            try {
                done.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            done.countDown();
            try {
                done.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cart_counter.setText("0");

            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

            sendToToken();

            view_cart_list = new ArrayList<>();

        }
    }

    public void sendToToken(){
        // This registration token comes from the client FCM SDKs.
        String registrationToken = "cdA3dyb4TXOpaDLKKj0vQx:APA91bEYqq9za-r-1FwdeqtzBR1gQOe6wLPcLDAiod6zVY38Oeb5oxCbZ_aMYSycVfmnO_SyBWfI0hMqiCUEdB14JHwGQRQmosDQYk6QgayerJRQ1tiK8gWf3RQOaqAEjY0H9ukzmlro";

        RemoteMessage.Builder messageBuilder = new RemoteMessage.Builder(registrationToken);
        messageBuilder.addData("title", "item was ordered").addData("body", "An item was ordered from your shop");

        RemoteMessage message = messageBuilder.build();
        FirebaseMessaging.getInstance().send(message);

        Toast.makeText(this, "send", Toast.LENGTH_SHORT).show();
                
    }

    private void updateUserDbHistory() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String textPhonenumber = AccountActivity.textPhonenumber;

                if (snapshot.child("users").hasChild(textPhonenumber)) {

                    int i;

                    for (i = 0; i < orderd_items.size(); i++) {
                        String myDate = (getDate() + "__" + getTime()).replace("/", "_");
                        databaseReference.child("users").child(textPhonenumber).child("History").child(myDate).child("item_name").setValue(orderd_items.get(i).ordered_item_name);
                        databaseReference.child("users").child(textPhonenumber).child("History").child(myDate).child("item_price").setValue(orderd_items.get(i).ordered_item_price + "");

                        String image_url = snapshot.child("ImagesMetaData").child(orderd_items.get(i).getOrdered_item_name()).getValue(String.class);
                        databaseReference.child("users").child(textPhonenumber).child("History").child(myDate).child("image_url").
                                setValue(image_url);

                        databaseReference.child("users").child(textPhonenumber).child("History").child(myDate).child("Status").setValue("Pending");
                        databaseReference.child("users").child(textPhonenumber).child("History").child(myDate).child("Date-Delivered").setValue("Pending");
                        databaseReference.child("users").child(textPhonenumber).child("History").child(myDate).child("Time-Delivered").setValue("Pending");

                    }

                    orderd_items = new ArrayList<>();
                    Total_price = "0";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private int getStock(GetSimilarItems similarItems, String item_name) {
        int j, stock = 0;

        for (j = 0; j < similarItems.item_name.size(); j++) {
            if (similarItems.item_name.get(j).equals(item_name)) {

                stock = similarItems.item_number.get(j);
                break;
            } else {

            }
        }
        return stock;
    }

    private String getDate() {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date = simpleDateFormat.format(calendar.getTime());

        return Date;
    }
    // method to get time

    private String getTime() {
        calendar = Calendar.getInstance();

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        Date = simpleDateFormat.format(calendar.getTime());

        return Date;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener nav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.account:

                    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(intent);
                    break;
                case R.id.settings:
                    Intent in = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(in);
                    break;
                case R.id.history:
                    Intent i = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(i);
                    break;
            }

            return true;
        }
    };

    public void Register(View view) {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

    public void Login(View view) {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);


    }

}