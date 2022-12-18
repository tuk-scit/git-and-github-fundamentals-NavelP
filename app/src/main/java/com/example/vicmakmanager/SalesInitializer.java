package com.example.vicmakmanager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

public class SalesInitializer extends Fragment{

    public static ArrayList<gas_sales_p> vicmakGasSales;
    gas_sales_adapter gasAdapter;

    public static ArrayList<burner_regulator_pipes_grills_sales_p> vicmakBurnerSales;
    burner_regulator_pipes_grills_sales_adapter burnerAdapter;

    public static ArrayList<burner_regulator_pipes_grills_sales_p> vicmakRegulatorSales;
    burner_regulator_pipes_grills_sales_adapter regulatorAdapter;

    public static ArrayList<burner_regulator_pipes_grills_sales_p> vicmakPipesGrillsSales;
    burner_regulator_pipes_grills_sales_adapter pipes_grills_sales_adapter;

    RecyclerView gas, burner, regulator,grills_and_pipes;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");
    String sales_shop = "";
    Context context;
    LinearLayoutManager [] linearLayoutManager = new LinearLayoutManager[4];

    LinearLayout gasDisplay, burnerDisplay, regulatorDisplay, grills_pipes_Display;
    ImageView gas_arrow, burner_arrow, regulator_arrow, grills_and_pipes_arrow;
    RelativeLayout relativeLayout;

    public SalesInitializer(String sales_shop, Context context, RelativeLayout relativeLayout) {
        this.sales_shop = sales_shop;
        this.context = context;
        this.relativeLayout = relativeLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_vicmak_vinny_sales, container, false);

        vicmakGasSales = new ArrayList<>();
        vicmakBurnerSales = new ArrayList<>();
        vicmakRegulatorSales = new ArrayList<>();
        vicmakPipesGrillsSales = new ArrayList<>();

        initializeRecycleViews(view);

        gas_arrow = view.findViewById(R.id.gas_arrow);
        burner_arrow = view.findViewById(R.id.burner_arrow);
        regulator_arrow = view.findViewById(R.id.regulator_arrow);
        grills_and_pipes_arrow = view.findViewById(R.id.g_p_arrow);

        for(int i = 0; i < linearLayoutManager.length; i++){
            linearLayoutManager[i] = new LinearLayoutManager(getContext());
            linearLayoutManager[i].setOrientation(RecyclerView.VERTICAL);
        }

        if(!sales_shop.equals("total_sales")){
            FetchData();
        }

        gasDisplay = view.findViewById(R.id.displayGas);
        burnerDisplay = view.findViewById(R.id.displayBurner);
        regulatorDisplay = view.findViewById(R.id.displayRegulator);
        grills_pipes_Display = view.findViewById(R.id.displayGrills_Pipes);

        toggleItemVisibility();

        return view;
    }

    public void initializeRecycleViews(View view) {
        gas = view.findViewById(R.id.gas_sales_recycle);
        burner = view.findViewById(R.id.burner_sales_recycle);
        regulator = view.findViewById(R.id.regulator_sales_recycle);
        grills_and_pipes = view.findViewById(R.id.grills_and_pipes_sales_recycle);
    }

    private void toggleHeight(int height){
        final float scale = context.getResources().getDisplayMetrics().density;
        int px = (int) (height * scale + 0.5f);

        relativeLayout.getLayoutParams().height = px;
    }

    private void toggleItemVisibility() {
        gasDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gas.getVisibility() == View.GONE){
                    gas.setVisibility(View.VISIBLE);
                    gas_arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    toggleHeight(700);
                }else{
                    gas.setVisibility(View.GONE);
                    gas_arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                    toggleHeight(280);
                }
                burner.setVisibility(View.GONE);
                regulator.setVisibility(View.GONE);
                grills_and_pipes.setVisibility(View.GONE);
            }
        });

        burnerDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gas.setVisibility(View.GONE);
                if(burner.getVisibility() == View.GONE){
                    burner.setVisibility(View.VISIBLE);
                    burner_arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    toggleHeight(550);
                }else{
                    burner.setVisibility(View.GONE);
                    burner_arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                    toggleHeight(280);
                }
                regulator.setVisibility(View.GONE);
                grills_and_pipes.setVisibility(View.GONE);
            }
        });

        regulatorDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gas.setVisibility(View.GONE);
                burner.setVisibility(View.GONE);
                if(regulator.getVisibility() == View.GONE){
                    regulator.setVisibility(View.VISIBLE);
                    regulator_arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    toggleHeight(550);
                }else{
                    regulator.setVisibility(View.GONE);
                    regulator_arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                    toggleHeight(280);
                }
                grills_and_pipes.setVisibility(View.GONE);
            }
        });

        grills_pipes_Display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gas.setVisibility(View.GONE);
                burner.setVisibility(View.GONE);
                regulator.setVisibility(View.GONE);
                if(grills_and_pipes.getVisibility() == View.GONE){
                    grills_and_pipes.setVisibility(View.VISIBLE);
                    grills_and_pipes_arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    toggleHeight(550);
                }else{
                    grills_and_pipes.setVisibility(View.GONE);
                    grills_and_pipes_arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                    toggleHeight(280);
                }
            }
        });
    }

    public void FetchData(){
        databaseReference.child("delivered_orders").child(sales_shop).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int [][] gas_sold = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
                int [][][] credit_cash = {{{0, 0, 0}, {0, 0, 0}}, {{0, 0, 0}, {0, 0, 0}},
                        {{0, 0, 0}, {0, 0, 0}}, {{0, 0, 0}, {0, 0, 0}}};
                String [] gas_type = {"ProGas", "KGas", "RangiGas", "TotalGas"};
                int [] gas_images = {R.drawable.pro_2, R.drawable.k_gas_skg_d, R.drawable.rangi_6kg, R.drawable.tkg_gas_d};

                String [] burner_types = {"Generic", "Orgaz", "Patco", "Primus", "Sungas"};
                int [] burner_images = {R.drawable.generic, R.drawable.orgaz_burner, R.drawable.burner_display, R.drawable.primus_burner, R.drawable.sungas_burner};
                int [] burner_num = {0, 0, 0, 0, 0};
                int [][] burner_credit_cash = {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}};

                String [] regulator_types = {"13kg", "6kg", "Ampia", "Cosco", "Generic Tecno", "Pac"};
                int [] regulator_images = {R.drawable.regulator_d, R.drawable.regulator_d, R.drawable.ampia_regulator,
                        R.drawable.cosco_regulator, R.drawable.regulator_d, R.drawable.regulator_d};
                int [] regulator_num = {0, 0, 0, 0, 0, 0};
                int [][] regulator_credit_cash = {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}};

                String [] grills_pipes = {"Grills", "Pipes"};
                int [] grills_pipes_images = {R.drawable.grill_d, R.drawable.pipe_d};
                int [] grills_pipes_num = {0, 0};
                int [][] grills_pipes_credit_cash = {{0, 0}, {0, 0}};

                for(DataSnapshot credit_or_paid_delivery : snapshot.getChildren()){

                    for(DataSnapshot customer : credit_or_paid_delivery.getChildren()){

                        for(DataSnapshot customer_item : customer.getChildren()){

                            //for gas
                            if(IsGas(customer_item.child("item_name").getValue(String.class) + "")){

                                String [] item_properties = customer_item.child("item_name").getValue(String.class).split(" ");

                                if(item_properties[0].equals("ProGas")){
                                    if(item_properties[1].equals("6kg")){
                                        gas_sold[0][0] += 1;

                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[0][0][0] += 1;
                                        }else{
                                            credit_cash[0][1][0] += 1;
                                        }
                                    }

                                    else if(item_properties[1].equals("13kg")){
                                        gas_sold[0][1] += 1;
                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[0][0][1] += 1;
                                        }else{
                                            credit_cash[0][1][1] += 1;
                                        }
                                    }
                                    else if(item_properties[1].equals("25kg")){
                                        gas_sold[0][2] += 1;
                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[0][0][2] += 1;
                                        }else{
                                            credit_cash[0][1][2] += 1;
                                        }
                                    }
                                }

                                else if(item_properties[0].equals("KGas")){
                                    if(item_properties[1].equals("6kg")){
                                        gas_sold[1][0] += 1;
                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[1][0][0] += 1;
                                        }else{
                                            credit_cash[1][1][0] += 1;
                                        }
                                    }

                                    else if(item_properties[1].equals("13kg")){
                                        gas_sold[1][1] += 1;
                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[1][0][1] += 1;
                                        }else{
                                            credit_cash[1][1][1] += 1;
                                        }
                                    }
                                    else if(item_properties[1].equals("25kg")){
                                        gas_sold[1][2] += 1;
                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[1][0][2] += 1;
                                        }else{
                                            credit_cash[1][1][2] += 1;
                                        }
                                    }
                                }

                                else if(item_properties[0].equals("RangiGas")){
                                    if(item_properties[1].equals("6kg")){
                                        gas_sold[2][0] += 1;

                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[2][0][0] += 1;
                                        }else{
                                            credit_cash[2][1][0] += 1;
                                        }
                                    }

                                    else if(item_properties[1].equals("13kg")){
                                        gas_sold[2][1] += 1;

                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[2][0][1] += 1;
                                        }else{
                                            credit_cash[2][1][1] += 1;
                                        }
                                    }
                                    else if(item_properties[1].equals("25kg")){
                                        gas_sold[2][2] += 1;

                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[2][0][2] += 1;
                                        }else{
                                            credit_cash[2][1][2] += 1;
                                        }
                                    }
                                }

                                else if(item_properties[0].equals("TotalGas")){
                                    if(item_properties[1].equals("6kg")){
                                        gas_sold[3][0] += 1;

                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[3][0][0] += 1;
                                        }else{
                                            credit_cash[3][1][0] += 1;
                                        }
                                    }

                                    else if(item_properties[1].equals("13kg")){
                                        gas_sold[3][1] += 1;

                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[3][0][1] += 1;
                                        }else{
                                            credit_cash[3][1][1] += 1;
                                        }
                                    }
                                    else if(item_properties[1].equals("25kg")){
                                        gas_sold[3][2] += 1;

                                        if(customer_item.hasChild("credit_amount")){
                                            credit_cash[3][0][2] += 1;
                                        }else{
                                            credit_cash[3][1][2] += 1;
                                        }
                                    }
                                }

                            }
                            //for burner
                            if(IsBurner(customer_item.child("item_name").getValue(String.class) + "")){
                                for(int i = 0; i < burner_types.length; i++){
                                    if(customer_item.child("item_name").getValue(String.class).split(" ")[1]
                                            .equals(burner_types[i])){
                                        burner_num[i] += 1;
                                        int j = IndexOf(burner_types, customer_item.child("item_name").getValue(String.class).split(" ")[1]);
                                        if(customer_item.hasChild("credit_amount")){
                                            burner_credit_cash[j][0] += 1;
                                        }else{
                                            burner_credit_cash[j][1] += 1;
                                        }

                                        break;
                                    }
                                }
                            }
                            //for regulator
                            if(IsRegulator(customer_item.child("item_name").getValue(String.class) + "")){
                                for(int i = 0; i < regulator_types.length; i++){
                                    if(customer_item.child("item_name").getValue(String.class).split(" ")[1]
                                            .equals(regulator_types[i])){
                                        regulator_num[i] += 1;
                                        int j = IndexOf(regulator_types, customer_item.child("item_name").getValue(String.class).split(" ")[1]);
                                        if(customer_item.hasChild("credit_amount")){
                                            regulator_credit_cash[j][0] += 1;
                                        }else{
                                            regulator_credit_cash[j][1] += 1;
                                        }

                                        break;
                                    }
                                }
                            }
                            //for grills and pipes
                            if((customer_item.child("item_name").getValue(String.class) + "").equals("Grills") ||
                                    (customer_item.child("item_name").getValue(String.class) + "").equals("Pipes")){
                                String i_name = customer_item.child("item_name").getValue(String.class);

                                if(i_name.equals("Grills")){
                                    grills_pipes_num[0] += 1;

                                    if(customer_item.hasChild("credit_amount")){
                                        grills_pipes_credit_cash[0][0] += 1;
                                    }else{
                                        grills_pipes_credit_cash[0][1] += 1;
                                    }
                                }else if(i_name.equals("Pipes")){
                                    grills_pipes_num[1] += 1;

                                    if(customer_item.hasChild("credit_amount")){
                                        grills_pipes_credit_cash[1][0] += 1;
                                    }else{
                                        grills_pipes_credit_cash[1][1] += 1;
                                    }
                                }

                            }
                        }

                    }
                }

                int i = 0;

                vicmakGasSales = new ArrayList<>();
                for(i = 0; i < gas_type.length; i++){

                    int no_sold = gas_sold[i][0] + gas_sold[i][1] + gas_sold[i][2];
                    int credit_total = credit_cash[i][0][0] + credit_cash[i][0][1] + credit_cash[i][0][2];
                    int cash_total = credit_cash[i][1][0] + credit_cash[i][1][1] + credit_cash[i][1][2];
                    vicmakGasSales.add(new gas_sales_p(gas_type[i], gas_images[i], gas_sold[i][0] + "",
                            gas_sold[i][1] + "", gas_sold[i][2] + "",
                            credit_cash[i][0][0] + "", credit_cash[i][0][1] + "", credit_cash[i][0][2] + "",
                            credit_cash[i][1][0] + "", credit_cash[i][1][1] + "", credit_cash[i][1][2] + "",
                            no_sold + "", credit_total + "", cash_total + ""));

                }

                AdaptGas(vicmakGasSales);

                vicmakBurnerSales = new ArrayList<>();

                for(i = 0; i < burner_types.length; i++){
                    vicmakBurnerSales.add(new burner_regulator_pipes_grills_sales_p("Burner type",burner_images[i], burner_types[i],
                            burner_num[i] + "", burner_credit_cash[i][0] + "", burner_credit_cash[i][1] + ""));
                }

                AdaptBurner(vicmakBurnerSales);

                vicmakRegulatorSales = new ArrayList<>();

                for(i = 0; i < regulator_types.length; i++){
                    vicmakRegulatorSales.add(new burner_regulator_pipes_grills_sales_p("Regulator type", regulator_images[i], regulator_types[i],
                            regulator_num[i] + "", regulator_credit_cash[i][0] + "", regulator_credit_cash[i][1] + ""));
                }

                AdaptRegulator(vicmakRegulatorSales);

                vicmakPipesGrillsSales = new ArrayList<>();

                for(i = 0; i < grills_pipes.length; i++){
                    vicmakPipesGrillsSales.add(new burner_regulator_pipes_grills_sales_p(grills_pipes[i], grills_pipes_images[i], grills_pipes[i],
                            grills_pipes_num[i] + "", grills_pipes_credit_cash[i][0] + "", grills_pipes_credit_cash[i][1] + ""));
                }

                AdaptGrillsPipes(vicmakPipesGrillsSales);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void AdaptGrillsPipes(ArrayList<burner_regulator_pipes_grills_sales_p> vicmakPipesGrillsSales) {
        pipes_grills_sales_adapter = new burner_regulator_pipes_grills_sales_adapter(vicmakPipesGrillsSales, context);
        grills_and_pipes.setLayoutManager(linearLayoutManager[3]);
        grills_and_pipes.setAdapter(pipes_grills_sales_adapter);
    }

    public void AdaptRegulator(ArrayList<burner_regulator_pipes_grills_sales_p> vicmakRegulatorSales) {
        regulatorAdapter = new burner_regulator_pipes_grills_sales_adapter(vicmakRegulatorSales, context);
        regulator.setLayoutManager(linearLayoutManager[2]);
        regulator.setAdapter(regulatorAdapter);
    }

    public void AdaptBurner(ArrayList<burner_regulator_pipes_grills_sales_p> vicmakBurnerSales) {

        burnerAdapter = new burner_regulator_pipes_grills_sales_adapter(vicmakBurnerSales, context);
        burner.setLayoutManager(linearLayoutManager[1]);
        burner.setAdapter(burnerAdapter);
    }

    public void AdaptGas(ArrayList<gas_sales_p> vicmakGasSales) {
        gasAdapter = new gas_sales_adapter(vicmakGasSales, context);
        gas.setLayoutManager(linearLayoutManager[0]);
        gas.setAdapter(gasAdapter);

    }

    private boolean IsRegulator(String item_name) {

        item_name = item_name.split(" ")[0];

        if(item_name.equals("Regulators")) return true;

        return false;
    }

    private int IndexOf(String[] burner_types, String s) {

        for(int i = 0; i < burner_types.length; i++){
            if(s.equals(burner_types[i])){
                return i;
            }
        }

        return -1;
    }

    private boolean IsBurner(String item_name) {

        item_name = item_name.split(" ")[0];

        if(item_name.equals("Burners")) return true;

        return false;
    }

    private boolean IsGas(String item_name) {

        item_name = item_name.split(" ")[0];

        item_name = item_name.substring(((item_name.length() - 1) - 2), item_name.length());

        if(item_name.equals("Gas")) return true;

        return false;
    }

}
