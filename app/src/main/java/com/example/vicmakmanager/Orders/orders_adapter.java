package com.example.vicmakmanager.Orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicmakmanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class orders_adapter extends RecyclerView.Adapter<orders_adapter.myViewHolder> {

    ArrayList<orders_p> undelivered_orders;
    Context context;
    static String payment_status_final = "", payment_shop = "";
    ManipulateOrders manipulateOrders;

    public orders_adapter(ArrayList<orders_p> undelivered_orders, Context context, ManipulateOrders manipulateOrders) {
        this.undelivered_orders = undelivered_orders;
        this.context = context;
        this.manipulateOrders = manipulateOrders;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_undelivered_order, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.order_id.setText("Order Id: #" + undelivered_orders.get(position).getDate_time() + "*");
        holder.name.setText(undelivered_orders.get(position).getName());
        holder.phone.setText(undelivered_orders.get(position).getPhoneNumber());
        holder.location.setText(undelivered_orders.get(position).getLocation());
        holder.item_name.setText(undelivered_orders.get(position).getItem());
        holder.date_time.setText(undelivered_orders.get(position).getDate_time());
        holder.item_price.setText(undelivered_orders.get(position).getItem_price());

        holder.payment_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.paid_on_delivery:
                        payment_status_final = "Paid on delivery";
                        holder.deposit_amount.setVisibility(View.GONE);
                        holder.deposit_text.setVisibility(View.GONE);
                        break;

                    case R.id.credit:
                        payment_status_final = "Credit";
                        holder.deposit_amount.setVisibility(View.VISIBLE);
                        holder.deposit_text.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        holder.payment_shop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.vinny:
                        payment_shop = "vinny";
                        break;

                    case R.id.vicmak2:
                        payment_shop = "vicmak2";
                        break;
                }
            }
        });

        holder.mark_as_delivered_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = undelivered_orders.get(holder.getAdapterPosition()).getPhoneNumber();
                String date_time = undelivered_orders.get(holder.getAdapterPosition()).getDate_time().replace("/", "_");

                String item_name = holder.item_name.getText().toString().trim();

                String delivery_guy = holder.delivery_guy.getText().toString().trim();

                String deposit = holder.deposit_amount.getText().toString().trim();

                if(payment_status_final.isEmpty()){
                    Toast.makeText(context, "Select Payment Method", Toast.LENGTH_SHORT).show();
                }else if(payment_shop.isEmpty()){
                    Toast.makeText(context, "Select Payment Shop", Toast.LENGTH_SHORT).show();
                }else if(delivery_guy.isEmpty()){
                    Toast.makeText(context, "Please Enter your name", Toast.LENGTH_SHORT).show();
                }else if(payment_status_final.equals("Credit") && deposit.isEmpty()){
                    Toast.makeText(context, "Please Input deposit amount, enter zero if no deposit is paid", Toast.LENGTH_SHORT).show();
                }
                else if((payment_status_final.equals("Credit")) && (Integer.parseInt(undelivered_orders.get(holder.getAdapterPosition()).getItem_price())
                        < Integer.parseInt(deposit))){
                    Toast.makeText(context, "Deposit cannot be more than " + undelivered_orders.get(holder.getAdapterPosition()).getItem_price(), Toast.LENGTH_SHORT).show();
                }
                else{
                    if(payment_status_final.equals("Credit")){
                        manipulateOrders.DeliverOrder(holder.getAdapterPosition(), phoneNumber, date_time,payment_shop,
                                payment_status_final, delivery_guy, item_name, deposit, undelivered_orders.get(holder.getAdapterPosition()).getItem_price());
                    }else{
                        manipulateOrders.DeliverOrder(holder.getAdapterPosition(), phoneNumber, date_time,payment_shop,
                                payment_status_final, delivery_guy, item_name, "", undelivered_orders.get(holder.getAdapterPosition()).getItem_price());
                    }
                    holder.deposit_amount.setVisibility(View.GONE);
                    holder.deposit_text.setVisibility(View.GONE);
                }
            }
        });

        holder.confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manipulateOrders.confirmOrder(undelivered_orders.get(
                        holder.getAdapterPosition()
                ).getPhoneNumber());
            }
        });

    }

    @Override
    public int getItemCount() {
        return undelivered_orders.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView name, phone, location, item_name, date_time, deposit_text, item_price, order_id;
        Button mark_as_delivered_button, confirm_order;
        EditText delivery_guy, deposit_amount;
        RadioGroup payment_status, payment_shop;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ordered_person_name);
            phone = itemView.findViewById(R.id.ordered_person_phone);
            location = itemView.findViewById(R.id.ordered_person_Location);
            item_name = itemView.findViewById(R.id.ordered_person_item);
            date_time = itemView.findViewById(R.id.ordered_person_date_time);

            payment_status = itemView.findViewById(R.id.payment_status);
            payment_shop = itemView.findViewById(R.id.payment_shop);

            mark_as_delivered_button = itemView.findViewById(R.id.mark_as_delivered);
            delivery_guy = itemView.findViewById(R.id.delivery_guy);

            deposit_text = itemView.findViewById(R.id.depositText);
            deposit_amount = itemView.findViewById(R.id.deposit);

            item_price = itemView.findViewById(R.id.item_price);
            confirm_order = itemView.findViewById(R.id.confirm_order);
            order_id = itemView.findViewById(R.id.order_id);
        }
    }
}
