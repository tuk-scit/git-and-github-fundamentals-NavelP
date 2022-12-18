package com.example.vicmakmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class view_refilling_details_adapter extends RecyclerView.Adapter<view_refilling_details_adapter.ViewHolder> {
    Context context;
    ArrayList<gas_refiller_p> cylinders;

    public view_refilling_details_adapter(Context context, ArrayList<gas_refiller_p> cylinders) {
        this.context = context;
        this.cylinders = cylinders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_refilling_cylinder_view_display, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.refill_item_name.setText(cylinders.get(position).getGas_name() + cylinders.get(position).getGas_weight());
        holder.refill_item_value.setText(cylinders.get(position).getCylinder_number());
    }

    @Override
    public int getItemCount() {
        return cylinders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView refill_item_name, refill_item_value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            refill_item_name = itemView.findViewById(R.id.refill_item_name);
            refill_item_value = itemView.findViewById(R.id.refill_item_value);
        }
    }
}
