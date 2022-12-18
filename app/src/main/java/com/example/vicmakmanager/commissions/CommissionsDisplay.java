package com.example.vicmakmanager.commissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.vicmakmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CommissionsDisplay extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");

    String duration;
    TextView commissionsHeading;
    RecyclerView commissionsRecycle;

    ArrayList<single_day_commission_p> Single_day_commissions_list = new ArrayList<>();
    single_daily_commission_display_adapter Single_daily_commission_display_adapter;

    ArrayList<single_daily_commission_display_adapter> Single_month_commission_display_list = new ArrayList<>();
    single_monthly_commissions_display_adapter Single_month_commission_display_adapter;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String myDate;

    LocalDate currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commissions_display);

        getSupportActionBar().hide();

        currentDate = LocalDate.now();

        commissionsHeading = findViewById(R.id.commissionsHeading);
        commissionsRecycle = findViewById(R.id.commissionsRecycle);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        duration = bundle.getString("duration");

        commissionsHeading.setText("Vicmak Enterprise " + duration + " Commissions");

        DisplayCommissions();
    }

    private void DisplayCommissions() {
        databaseReference.child("PermStorage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int GasCommission = 0, BurnerCommission = 0, GrillCommission = 0, PipeCommission = 0, RegulatorCommission = 0;
                if (duration.equals("daily")) {
                    for (DataSnapshot credit_paid_on_delivery : snapshot.getChildren()) {

                        for (DataSnapshot storageItem : credit_paid_on_delivery.child(getDate()).getChildren()) {
                            if(storageItem.getKey().equals("Gas"))
                                GasCommission += Integer.parseInt(
                                        storageItem.child("commission").getValue(String.class)
                                );

                            if(storageItem.getKey().equals("Burners"))
                                BurnerCommission += Integer.parseInt(
                                        storageItem.child("commission").getValue(String.class)
                                );

                            if(storageItem.getKey().equals("Grills"))
                                GrillCommission += Integer.parseInt(
                                        storageItem.child("commission").getValue(String.class)
                                );

                            if(storageItem.getKey().equals("Pipes"))
                                PipeCommission += Integer.parseInt(
                                        storageItem.child("commission").getValue(String.class)
                                );

                            if(storageItem.getKey().equals("Regulators"))
                                RegulatorCommission += Integer.parseInt(
                                        storageItem.child("commission").getValue(String.class)
                                );
                        }
                    }

                    Single_day_commissions_list.add(new single_day_commission_p(
                            String.valueOf(currentDate.getDayOfMonth()), GasCommission + "", BurnerCommission + "", GrillCommission + "", PipeCommission + "",
                            RegulatorCommission + "", currentDate.getMonth().toString()
                    ));
                }
                if (duration.equals("monthly")) {

                }

                if (duration.equals("yearly")) {

                }


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

}