package com.example.vicmakmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class credits_adapter extends RecyclerView.Adapter<credits_adapter.myViewHolder> {
    ArrayList<credits_p> debtors;
    Context context;
    creditInterface credit_interface;

    public credits_adapter(ArrayList<credits_p> debtors, Context context, creditInterface credit_interface) {
        this.debtors = debtors;
        this.context = context;
        this.credit_interface = credit_interface;
    }

    @NonNull
    @Override
    public credits_adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_credits_display, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull credits_adapter.myViewHolder holder, int position) {
        holder.phoneNumber.setText(debtors.get(position).getPhoneNumber());
        holder.credit_amount.setText(debtors.get(position).getCredit_amount());
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount_paid = holder.amount_paid.getText().toString().trim();
                String credit_amount = holder.credit_amount.getText().toString().trim();

                if(amount_paid.isEmpty()){
                    Toast.makeText(context, "Input Amount", Toast.LENGTH_SHORT).show();
                }else if (Integer.parseInt(amount_paid) > Integer.parseInt(credit_amount)){
                    Toast.makeText(context, "Amount exceeds credit balance", Toast.LENGTH_SHORT).show();
                }else{
                    credit_interface.payDebt(debtors.get(holder.getAdapterPosition()).getPhoneNumber() + "",
                            debtors.get(holder.getAdapterPosition()).getItem_name() + "", amount_paid, debtors.get(holder.getAdapterPosition()).getVicmakShop(),
                            credit_amount);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return debtors.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView phoneNumber, credit_amount;
        EditText amount_paid;
        Button pay;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            phoneNumber = itemView.findViewById(R.id.debtor_phoneNumber);
            credit_amount = itemView.findViewById(R.id.credit_amount);
            amount_paid = itemView.findViewById(R.id.amount_added);

            pay = itemView.findViewById(R.id.pay);
        }
    }
}
