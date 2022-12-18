package com.example.vicmakmanager.Gas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicmakmanager.R;

import java.util.List;

public class gas_cylinders_adapter extends RecyclerView.Adapter<gas_cylinders_adapter.MyHolder> {
    Context context;
    List<gas_cylinders_p> list;
    view_item_form V;

    public gas_cylinders_adapter(Context context, List<gas_cylinders_p> list, view_item_form v) {
        this.context = context;
        this.list = list;
        this.V = v;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_gas_cylinder_display, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.gas_name.setText(list.get(position).getGas_name());
        holder.gas_image.setImageResource(list.get(position).getGas_image());
        holder.gas_weight.setText(list.get(position).getGas_weight());
        holder.total_gas_cylinder_number.setText(list.get(position).getTotal_gas_cylinder_number());
        holder.gas_cylinder_full.setText(list.get(position).getGas_cylinder_full());
        holder.gas_cylinder_empty.setText(list.get(position).getGas_cylinder_empty());
        holder.gas_cylinder_commission.setText(list.get(position).getCylinder_commission());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(holder.getAdapterPosition()).getGas_name().equals("RangiGas")){
                    V.onclick(holder.gas_name.getText().toString(), holder.gas_weight.getText().toString().split(" ")[0],
                            holder.gas_weight.getText().toString().split(" ")[1]);
                }else{
                    V.onclick(holder.gas_name.getText().toString(), holder.gas_weight.getText().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView gas_name, gas_weight, total_gas_cylinder_number, gas_cylinder_empty, gas_cylinder_full, gas_cylinder_commission;
        ImageView gas_image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            gas_name = itemView.findViewById(R.id.gas_name);
            gas_weight = itemView.findViewById(R.id.gas_weight);
            total_gas_cylinder_number = itemView.findViewById(R.id.total_gas_cylinder_number);
            gas_cylinder_empty = itemView.findViewById(R.id.gas_cylinder_empty);
            gas_cylinder_full = itemView.findViewById(R.id.gas_cylinder_full);
            gas_cylinder_commission = itemView.findViewById(R.id.gas_cylinder_commission);

            gas_image = itemView.findViewById(R.id.gas_image);
        }
    }
}
