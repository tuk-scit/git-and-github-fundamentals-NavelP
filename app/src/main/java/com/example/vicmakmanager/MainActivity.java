package com.example.vicmakmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vicmakmanager.Orders.orders_p;
import com.example.vicmakmanager.commissions.CommissionsDisplay;
import com.example.vicmakmanager.users.ViewUsers;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements ActivityChanger {

    RecyclerView recyclerView;
    List<vicmak_commodity_p> list;
    vicmak_commodities_adapter adapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");

    TextView unordered_items_count;

    TabLayout tabLayout;
    ViewPager viewPager;
    VicmakSalesAdapter salesAdapter;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String myDate;
    String reset_status, critical_date, last_reset;

    RelativeLayout salesLayoutTabs;

    private String admin_email = null;
    private String admin_phoneNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.deep_yellow))
        );

        salesLayoutTabs = findViewById(R.id.salesLayoutTabs);
        FirebaseMessaging.getInstance().subscribeToTopic("orders_notify");

        ResetTimerManager();

        setTabLayout(salesLayoutTabs);

        unordered_items_count = findViewById(R.id.unordered_items_count);
        ViewOrders.undelivered_orders = new ArrayList<>();

        getCommission();

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ViewOrders.undelivered_orders = new ArrayList<>();
                int i = 0;
                for (DataSnapshot user : snapshot.getChildren()) {

                    for (DataSnapshot user_history : user.getChildren()) {

                        if (user_history.getKey().equals("History")) {

                            for (DataSnapshot single_item : user_history.getChildren()) {
                                String status = single_item.child("Status").getValue(String.class) + "";

                                if (status.equals("Pending")) {
                                    i += 1;
                                    String date_time = single_item.getKey();

                                    ViewOrders.undelivered_orders.add(new orders_p(user.child("Name").getValue(String.class) + "", user.getKey() + "",
                                            single_item.child("item_name").getValue(String.class) + "",
                                            "Area: " + user.child("Area").getValue(String.class) +
                                                    "\nStreet: " + user.child("Street").getValue(String.class) + "\nNearest Building: " + user.child("Nearest_building").
                                                    getValue(String.class),
                                            date_time.replace("_", "/"), single_item.child("item_price").getValue(String.class)));

                                    unordered_items_count.setText(i + "");
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list = new ArrayList<>();

        recyclerView = findViewById(R.id.vicmak_comodities);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);


        UpdateVicmakSummaryStock();

        VicmakItemsDisplaySummary();

        UpdateCreditsTable();

        fillGeneralInfo();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem d_commission = menu.findItem(R.id.view_vicmak_daily_commission_menu);
        MenuItem m_commission = menu.findItem(R.id.view_vicmak_monthly_commission_menu);
        MenuItem y_commission = menu.findItem(R.id.view_vicmak_yearly_commission_menu);

        MenuItem gas_analysis = menu.findItem(R.id.gas_analysis);
        MenuItem burner_analysis = menu.findItem(R.id.burner_analysis);
        MenuItem regulator_analysis = menu.findItem(R.id.regulator_analysis);
        MenuItem grills_analysis = menu.findItem(R.id.grills_analysis);
        MenuItem pipes_analysis = menu.findItem(R.id.pipes_analysis);

        if((admin_phoneNumber == null) && (admin_email == null)){
            d_commission.setVisible(false);
            m_commission.setVisible(false);
            y_commission.setVisible(false);

            gas_analysis.setVisible(false);
            burner_analysis.setVisible(false);
            regulator_analysis.setVisible(false);
            grills_analysis.setVisible(false);
            pipes_analysis.setVisible(false);
        }else if((admin_phoneNumber != null) && (admin_email != null)){
            d_commission.setVisible(true);
            m_commission.setVisible(true);
            y_commission.setVisible(true);

            gas_analysis.setVisible(true);
            burner_analysis.setVisible(true);
            regulator_analysis.setVisible(true);
            grills_analysis.setVisible(true);
            pipes_analysis.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vicmak_manager_option_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refill_cylinders_menu:
                startActivity(new Intent(getApplicationContext(), RefillingGas.class));
                return true;

            case R.id.view_vicmak_users_menu:
                startActivity(new Intent(getApplicationContext(), ViewUsers.class));
                return true;

            case R.id.print:

            case R.id.view_vicmak_product_analysis_menu:
                if((admin_phoneNumber == null) && (admin_email == null)){
                    LoginMoreOptions();
                }
                return true;

            case R.id.view_vicmak_daily_commission_menu:
                pushCommissionIntent("daily");
                return true;

            case R.id.view_vicmak_monthly_commission_menu:
                pushCommissionIntent("monthly");
                return true;

            case R.id.view_vicmak_yearly_commission_menu:
                pushCommissionIntent("yearly");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void pushCommissionIntent(String duration){
        startActivity(new Intent(
                getApplicationContext(), CommissionsDisplay.class
        ).putExtra("duration", duration));
    }

    private void LoginMoreOptions(){
        AlertDialog alertDialog;
        AlertDialog.Builder builder;

        EditText email, phoneNumber;

        builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.activity_login, null);
        view.findViewById(R.id.progress_bar).setVisibility(View.GONE);
        builder.setView(view);

        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.password);

        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(700, 800);
        alertDialog.getWindow().setGravity(Gravity.TOP);

        view.findViewById(R.id.user_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myEmail, myPassword;
                myEmail = email.getText().toString().trim();
                myPassword = phoneNumber.getText().toString().trim();

                if(TextUtils.isEmpty(myEmail) || TextUtils.isEmpty(myPassword)){
                    Toast.makeText(MainActivity.this, "Can not use empty values", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("adminCredentials").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.hasChild(myPassword)){
                                Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                            }else{
                                String r_email = snapshot.child(myPassword).child("email").getValue(String.class);

                                if(r_email.equals(myEmail)){
                                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    admin_phoneNumber = myPassword;
                                    admin_email = myEmail;
                                    alertDialog.dismiss();
                                }else{
                                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void UpdateVicmakSummaryStock() {
        databaseReference.child("VicmakStock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot vicmak_stock_item : snapshot.getChildren()) {

                    if (vicmak_stock_item.getKey().equals("Grills") ||
                            vicmak_stock_item.getKey().equals("Pipes")) continue;

                    int total_stock = 0;
                    for (DataSnapshot item_type : vicmak_stock_item.getChildren()) {
                        if (item_type.getKey().equals("general_info")) continue;

                        //if burner or regulator
                        if (vicmak_stock_item.getKey().equals("Burners") ||
                                vicmak_stock_item.getKey().equals("Regulators"))
                            total_stock += Integer.parseInt(item_type.child("stock").getValue(String.class));

                        //if gas
                        if (vicmak_stock_item.getKey().equals("Gas")) {
                            //if Rangi
                            if (item_type.getKey().equals("RangiGas")) {

                                for (DataSnapshot gas_weight : item_type.getChildren()) {

                                    for (DataSnapshot rangi_type : gas_weight.getChildren())
                                        total_stock += Integer.parseInt(rangi_type.child("stock").getValue(String.class));

                                }

                            } else {
                                for (DataSnapshot gas_weight : item_type.getChildren())
                                    total_stock += Integer.parseInt(gas_weight.child("stock").getValue(String.class));
                            }

                        }

                    }

                    vicmak_stock_item.getRef().child("general_info").child("TotalStock").setValue(total_stock + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void VicmakItemsDisplaySummary() {
        databaseReference.child("VicmakStock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                int image;
                int i = 0;
                int[] images = {R.drawable.sungas_burner, R.drawable.pro_2, R.drawable.regulator_d};
                for (DataSnapshot vicmak_stock_item : snapshot.getChildren()) {
                    if (vicmak_stock_item.getKey().equals("Grills") || vicmak_stock_item.getKey().equals("Pipes")) {
                        if (vicmak_stock_item.getKey().equals("Grills")) {
                            image = R.drawable.grill_d;
                        } else {
                            image = R.drawable.pipe_d;
                        }

                        list.add(new vicmak_commodity_p(vicmak_stock_item.getKey(), vicmak_stock_item.child("stock").getValue(String.class),
                                image));
                    } else {

                        list.add(new vicmak_commodity_p(vicmak_stock_item.getKey(), vicmak_stock_item.child("general_info").child("TotalStock").getValue(String.class),
                                images[i]));
                        i++;
                    }
                }

                adapter = new vicmak_commodities_adapter(getApplicationContext(), list, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void LoginPage(int position, List<vicmak_commodity_p> list) {
        Intent intent = new Intent(getApplicationContext(), Login.class);

        intent.putExtra("item_name", list.get(position).getItem_name());
        startActivity(intent);
    }

    public void ViewOrders(View view) {
        startActivity(new Intent(getApplicationContext(), ViewOrders.class));
    }

    private void setTabLayout(RelativeLayout salesTabLayoutView) {
        tabLayout = findViewById(R.id.CommissionsTabLayout);
        viewPager = findViewById(R.id.CommissionsPageViewer);

        salesAdapter = new VicmakSalesAdapter(getSupportFragmentManager(), salesTabLayoutView);
        salesAdapter.add();

        viewPager.setAdapter(salesAdapter);
        tabLayout.setupWithViewPager(viewPager);

        addIcons();
    }

    private void addIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_local_gas_station_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_local_gas_station_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_attach_money_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_credit_card_24);
    }

    private void UpdateCreditsTable() {
        databaseReference.child("credits").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getDate() {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        myDate = simpleDateFormat.format(calendar.getTime());

        return myDate;
    }

    private String getTime() {
        calendar = Calendar.getInstance();

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        myDate = simpleDateFormat.format(calendar.getTime());

        return myDate;
    }


    private void ResetTimerManager() {

        databaseReference.child("ResetManager").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String current_date = getDate();
                reset_status = snapshot.child("reset_status").getValue(String.class);
                last_reset = snapshot.child("last_reset").getValue(String.class);
                critical_date = snapshot.child("critical_date").getValue(String.class);

                if (!TextUtils.isEmpty(reset_status) && !TextUtils.isEmpty(last_reset) && !TextUtils.isEmpty(critical_date)) {
                    //1st condition is, reset_status should be false
                    //2st condition is, critical time is less than current time

                    if (reset_status.equals("false") || criticalTimeIsLess(current_date, critical_date)) {
                        resetTables();
                        Toast.makeText(MainActivity.this, "resetting", Toast.LENGTH_SHORT).show();
                        snapshot.getRef().child("reset_status").setValue("true");

                        try {
                            critical_date = IncrementCriticalDate(critical_date);
                            snapshot.getRef().child("critical_date").setValue(critical_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        snapshot.getRef().child("last_reset").setValue(getDate() + " " + getTime());
                    } else {
                       // Toast.makeText(MainActivity.this, "cannot reset", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String IncrementCriticalDate(String critical_date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(critical_date));
        c.add(Calendar.DATE, 1);  // number of days to add
        critical_date = sdf.format(c.getTime());  // dt is now the new date

        return critical_date;

    }

    private void resetTables() {
        //clearing the delivered orders table
        databaseReference.child("delivered_orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot vicmak_shop : snapshot.getChildren()) {
                    for (DataSnapshot credit_pay_on_delivery : vicmak_shop.getChildren()) {
                        for (DataSnapshot customer : credit_pay_on_delivery.getChildren()) {
                            for (DataSnapshot customer_order : customer.getChildren()) {
                                String category = customer_order.child("category").getValue(String.class);
                                //check if credit of not
                                if (credit_pay_on_delivery.getKey().equals("credits")) {
                                    for (DataSnapshot customer_order_val : customer_order.getChildren()) {
                                        databaseReference.child("PermStorage").child(credit_pay_on_delivery.getKey() + "").
                                                child(getDate()).child(category).child(customer.getKey() + "").
                                                child(customer_order.getKey() + "").child(customer_order_val.getKey() + "").setValue(
                                                        customer_order_val.getValue(String.class)
                                                );
                                    }

                                } else {
                                    databaseReference.child("PermStorage").child(credit_pay_on_delivery.getKey() + "").
                                            child(getDate()).child(category).child(customer.getKey() + "").
                                            child(customer_order.getKey() + "").child("item_name").setValue(
                                                    customer_order.child("item_name").getValue(String.class)
                                            );

                                }

                            }
                        }
                    }
                }

                snapshot.getRef().setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //resetting the database
        databaseReference.child("VicmakStock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot vicmak_item : snapshot.getChildren()){
                    //if grill or pipe
                    if(vicmak_item.getKey().equals("Grills") || vicmak_item.getKey().equals("Pipes")) {
                        vicmak_item.getRef().child("sold").setValue("0");

                    }

                    //if burner or regulator
                    if(vicmak_item.getKey().equals("Burners") || vicmak_item.getKey().equals("Regulators")){

                        for(DataSnapshot b_r_type : vicmak_item.getChildren()){
                            if(b_r_type.getKey().equals("general_info")) continue;

                            b_r_type.getRef().child("sold").setValue("0");
                        }

                    }

                    //if gas
                    if(vicmak_item.getKey().equals("Gas")){
                        for(DataSnapshot gas_type : vicmak_item.getChildren()){
                            if(gas_type.getKey().equals("general_info")) continue;
                            for(DataSnapshot gas_weight : gas_type.getChildren()){
                                //if rangi
                                if(gas_type.getKey().equals("RangiGas")){

                                    for(DataSnapshot rangi_type : gas_weight.getChildren()){
                                        rangi_type.getRef().child("exchanged").setValue("0");
                                    }

                                }else
                                {
                                    gas_weight.getRef().child("exchanged").setValue("0");
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean criticalTimeIsLess(String current_date, String p_critical_date) {
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        Date myCurrentDate, criticalDate;

        try {
            myCurrentDate = simpleDateFormat.parse(current_date);
            criticalDate = simpleDateFormat.parse(p_critical_date);

            if (myCurrentDate.compareTo(criticalDate) == 0) {
                return true;
            }
            if (myCurrentDate.compareTo(criticalDate) > 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getCommission() {
        databaseReference.child("VicmakStock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot vicmak_item : snapshot.getChildren()) {
                    if (vicmak_item.getKey().equals("Gas")) {
                        int general_total_commission = 0;
                        for (DataSnapshot gas_type : vicmak_item.getChildren()) {

                            if (gas_type.getKey().equals("general_info")) continue;

                            for (DataSnapshot gas_weight : gas_type.getChildren()) {
                                //if it is rangi
                                if (gas_type.getKey().equals("RangiGas")) {

                                    for (DataSnapshot rangi_type : gas_weight.getChildren()) {
                                        int exchanged = Integer.parseInt(rangi_type.child("exchanged").getValue(String.class));

                                        int commission = Integer.parseInt(rangi_type.child("selling_price").getValue(String.class)) -
                                                Integer.parseInt(rangi_type.child("refilling_price").getValue(String.class));

                                        rangi_type.getRef().child("commission").setValue(commission + "");

                                        rangi_type.getRef().child("total_commission").setValue((commission * exchanged) + "");

                                        general_total_commission += (commission * exchanged);
                                    }
                                } else {

                                    int exchanged = Integer.parseInt(gas_weight.child("exchanged").getValue(String.class) + "");

                                    int commission = Integer.parseInt(gas_weight.child("selling_price").getValue(String.class)) -
                                            Integer.parseInt(gas_weight.child("refilling_price").getValue(String.class));

                                    gas_weight.getRef().child("commission").setValue(commission + "");

                                    gas_weight.getRef().child("total_commission").setValue((commission * exchanged) + "");

                                    general_total_commission += (commission * exchanged);
                                }
                            }
                        }

                        vicmak_item.getRef().child("general_info").child("TotalCommission").setValue(general_total_commission + "");

                    } else {
                        if (vicmak_item.getKey().equals("Grills") || vicmak_item.getKey().equals("Pipes")) {
                            int sold = Integer.parseInt(vicmak_item.child("sold").getValue(String.class));
                            int commission = Integer.parseInt(vicmak_item.child("commission").getValue(String.class));

                            int total_commission = sold * commission;

                            vicmak_item.getRef().child("total_commission").setValue(total_commission + "");
                        } else {
                            int total_general_commission = 0;
                            for (DataSnapshot burner_regulators : vicmak_item.getChildren()) {

                                if (burner_regulators.getKey().equals("general_info")) continue;

                                int sold = Integer.parseInt(burner_regulators.child("sold").getValue(String.class));
                                int commission = (Integer.parseInt(burner_regulators.child("selling_price").getValue(String.class)) -
                                        Integer.parseInt(burner_regulators.child("buying_price").getValue(String.class)));

                                burner_regulators.getRef().child("commission").setValue(commission + "");

                                int total_commission = sold * commission;

                                total_general_commission += total_commission;
                                burner_regulators.getRef().child("total_commission").setValue(total_commission + "");
                            }

                            vicmak_item.getRef().child("general_info").child("TotalCommission").setValue(total_general_commission + "");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fillGeneralInfo() {
        databaseReference.child("VicmakStock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total_stock, total_exchanged, total_sold;
                for (DataSnapshot vicmak_item : snapshot.getChildren()) {
                    total_stock = 0;
                    //for gas
                    if (vicmak_item.getKey().equals("Gas")) {
                        total_exchanged = 0;
                        for (DataSnapshot gas_type : vicmak_item.getChildren()) {

                            if (gas_type.getKey().equals("general_info")) continue;

                            for (DataSnapshot gas_weight : gas_type.getChildren()) {
                                //if gas is rangi
                                if (gas_type.getKey().equals("RangiGas")) {
                                    for (DataSnapshot rangi_type : gas_weight.getChildren()) {

                                        total_exchanged += Integer.parseInt(rangi_type.child("exchanged").getValue(String.class));

                                        int stock = Integer.parseInt(rangi_type.child("full").getValue(String.class)) +
                                                Integer.parseInt(rangi_type.child("empty").getValue(String.class));

                                        rangi_type.getRef().child("stock").setValue(stock + "");

                                        total_stock += Integer.parseInt(rangi_type.child("stock").getValue(String.class) + "");

                                    }
                                } else {
                                    total_exchanged += Integer.parseInt(gas_weight.child("exchanged").getValue(String.class));

                                    int stock = Integer.parseInt(gas_weight.child("full").getValue(String.class)) +
                                            Integer.parseInt(gas_weight.child("empty").getValue(String.class));

                                    gas_weight.getRef().child("stock").setValue(stock + "");

                                    total_stock += Integer.parseInt(gas_weight.child("stock").getValue(String.class) + "");

                                }
                            }
                        }
                        vicmak_item.getRef().child("general_info").child("TotalExchanged").setValue(total_exchanged + "");

                        vicmak_item.getRef().child("general_info").child("TotalStock").setValue(total_stock + "");
                    }
                    //for burner and regulator
                    else if (vicmak_item.getKey().equals("Burners") || vicmak_item.getKey().equals("Regulators")) {
                        total_sold = 0;

                        for (DataSnapshot burner_regulator : vicmak_item.getChildren()) {

                            if (burner_regulator.getKey().equals("general_info")) continue;

                            total_sold += Integer.parseInt(burner_regulator.child("sold").getValue(String.class));

                            total_stock += Integer.parseInt(burner_regulator.child("stock").getValue(String.class) + "");

                        }

                        vicmak_item.getRef().child("general_info").child("sold").setValue(total_sold + "");
                        vicmak_item.getRef().child("general_info").child("TotalStock").setValue(total_stock + "");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}