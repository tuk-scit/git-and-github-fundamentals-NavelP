package com.example.vicmakmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class vicmak_commodities_adapter extends RecyclerView.Adapter<vicmak_commodities_adapter.MyHolder> {
    Context context;
    List<vicmak_commodity_p> list;
    private ActivityChanger activityChanger;
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_vicmak_commodity, parent, false);

        return new MyHolder(view);
    }

    public vicmak_commodities_adapter(Context context, List<vicmak_commodity_p> list, ActivityChanger activityChanger) {
        this.context = context;
        this.list = list;
        this.activityChanger = activityChanger;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.item_image.setImageResource(list.get(position).getImage());
        holder.item_name.setText(list.get(position).getItem_name());
        holder.item_count.setText(list.get(position).getTotal_count());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityChanger.LoginPage(holder.getAdapterPosition(), list);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView item_image;
        TextView item_name, item_count;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.vicmak_commodity_image);
            item_name = itemView.findViewById(R.id.vicmak_comodity_name_value);
            item_count = itemView.findViewById(R.id.vicmak_comodity_count_value);
        }
    }
}
