package com.example.vicmakmanager.Regulators;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicmakmanager.R;
import com.example.vicmakmanager.Regulators.regulator_p;

import java.util.List;

public class regulator_adapter extends RecyclerView.Adapter<regulator_adapter.MyHolder> {
    Context context;
    List<regulator_p> list;
    view_form_items update;

    public regulator_adapter(Context context, List<regulator_p> list, view_form_items update) {
        this.context = context;
        this.list = list;
        this.update = update;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_regulator_display, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.regulator_name.setText(list.get(position).getRegulator_type());
        holder.regulator_type.setText(list.get(position).getRegulator_type());
        holder.regulator_commission.setText(list.get(position).getCommission());
        holder.regulator_stock.setText(list.get(position).getStock());
        holder.sold_regulators.setText(list.get(position).getSold());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update.onclick(list, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView regulator_type, sold_regulators, regulator_stock, regulator_commission, regulator_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            regulator_type = itemView.findViewById(R.id.regulator_type);
            sold_regulators = itemView.findViewById(R.id.sold_regulators);
            regulator_stock = itemView.findViewById(R.id.regulator_stock);
            regulator_commission = itemView.findViewById(R.id.regulator_commission);
            regulator_name = itemView.findViewById(R.id.regulators_name);
        }
    }
}
