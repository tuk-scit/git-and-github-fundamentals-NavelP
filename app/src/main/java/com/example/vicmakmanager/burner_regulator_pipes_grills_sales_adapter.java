package com.example.vicmakmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class burner_regulator_pipes_grills_sales_adapter extends RecyclerView.Adapter<burner_regulator_pipes_grills_sales_adapter.myViewHolder>{
    ArrayList<burner_regulator_pipes_grills_sales_p> sales;
    Context context;

    public burner_regulator_pipes_grills_sales_adapter(ArrayList<burner_regulator_pipes_grills_sales_p> sales, Context context) {
        this.sales = sales;
        this.context = context;
    }

    @NonNull
    @Override
    public burner_regulator_pipes_grills_sales_adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_burner_regulator_pipe_grill_sales_table_view, parent, false);

        return new burner_regulator_pipes_grills_sales_adapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull burner_regulator_pipes_grills_sales_adapter.myViewHolder holder, int position) {

        holder.i_image.setImageResource(sales.get(position).getBurner_image());
        holder.no_sold.setText(sales.get(position).getNo_sold());
        holder.no_cash.setText(sales.get(position).getNo_cash());
        holder.no_credit.setText(sales.get(position).getNo_credit());

        holder.i_name.setText(sales.get(position).getBurner_type());
        holder.item_type.setText(sales.get(position).getItem_type());
    }

    @Override
    public int getItemCount() {
        return sales.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView i_name, no_sold, no_credit, no_cash, item_type;
        ImageView i_image;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            i_name = itemView.findViewById(R.id.i_name);
            no_sold = itemView.findViewById(R.id.i_no_sold);
            no_credit = itemView.findViewById(R.id.i_credit);
            no_cash = itemView.findViewById(R.id.i_cash);

            i_image = itemView.findViewById(R.id.i_image);
            item_type = itemView.findViewById(R.id.item_type);

        }

    }
}
