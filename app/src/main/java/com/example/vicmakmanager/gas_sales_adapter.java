package com.example.vicmakmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class gas_sales_adapter extends RecyclerView.Adapter<gas_sales_adapter.myViewHolder> {
    ArrayList<gas_sales_p> sales;
    Context context;

    public gas_sales_adapter(ArrayList<gas_sales_p> sales, Context context) {
        this.sales = sales;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_gas_sales_table_view, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        String [] values = {
                sales.get(position).getGas_name(), sales.get(position).getNoSold6kg(), sales.get(position).getNoSold13kg(), sales.get(position).getNoSold25kg(),
                sales.get(position).getCredit6kg(), sales.get(position).getCredit13kg(), sales.get(position).getCredit25kg(),
                sales.get(position).getCash6kg(), sales.get(position).getCash13kg(), sales.get(position).getCash25kg(),
                sales.get(position).getNoSoldTotal(), sales.get(position).getCreditTotal(), sales.get(position).getCashTotal()
        };

        holder.gas_image.setImageResource(sales.get(position).getGas_image());

        int i;

        for(i = 0; i < values.length; i++){
            holder.gas_sales_textViews[i].setText(values[i]);
        }
    }

    @Override
    public int getItemCount() {
        return sales.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView [] gas_sales_textViews = new TextView[13];

        int [] ids = {R.id.gas_name, R.id.skgSold, R.id.tkgSold, R.id.tfkgSold, R.id.skgCredit, R.id.tkgCredit, R.id.tfkgCredit, R.id.skgCash, R.id.tkgCash, R.id.tfkgCash,
                        R.id.salesSoldTotal, R.id.salesCreditTotal, R.id.salesCashTotal};

        ImageView gas_image;



        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            gas_image = itemView.findViewById(R.id.gas_image);

            int i = 0;

            for(i = 0; i < gas_sales_textViews.length; i++){
                gas_sales_textViews[i] = itemView.findViewById(ids[i]);
            }
        }

    }
}
