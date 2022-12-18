package com.example.vicmakmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class total_sales_adapter extends RecyclerView.Adapter<total_sales_adapter.myHolder> {
    Context context;
    ArrayList<total_sales_p> sales;

    public total_sales_adapter(Context context, ArrayList<total_sales_p> sales) {
        this.context = context;
        this.sales = sales;
    }

    @NonNull
    @Override
    public total_sales_adapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_total_sales, parent, false);

        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull total_sales_adapter.myHolder holder, int position) {
        holder.item_name.setText(sales.get(position).getItem_name());
        holder.item_commission.setText(sales.get(position).getItem_commission());
    }

    @Override
    public int getItemCount() {
        return sales.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        TextView item_name, item_commission;
        public myHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.vicmak_stock_item);
            item_commission = itemView.findViewById(R.id.item_commission);
        }
    }
}
