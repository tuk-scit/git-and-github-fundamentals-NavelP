package com.vicshop.vicmakgas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class history_display_adapter extends RecyclerView.Adapter<history_display_adapter.Myholder> {
    Context context;
    List<history_display_p> list;
    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_single_item, parent, false);

        return new Myholder(view);
    }

    public history_display_adapter(Context context, List<history_display_p> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        if(!list.get(position).getImage_url().equals("")){
            Picasso.with(context).load(list.get(position).getImage_url()).into(holder.item_image);
        }

        holder.item_name.setText(list.get(position).getItem_name());
        holder.item_price.setText(list.get(position).getItem_price());
        holder.date_ordered.setText(list.get(position).getDate_ordered());
        holder.time_ordered.setText(list.get(position).getTime_ordered());
        holder.status.setText(list.get(position).getStatus());
        holder.date_delivered.setText(list.get(position).getStatus());
        holder.time_delivered.setText(list.get(position).getTime_delivered());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myholder extends RecyclerView.ViewHolder{
        TextView item_price, item_name, date_ordered, date_delivered, time_ordered, time_delivered, status;
        ImageView item_image;

        public Myholder(@NonNull View itemView) {
            super(itemView);

            item_price = itemView.findViewById(R.id.ordered_item_history_price);
            item_name = itemView.findViewById(R.id.ordered_item_history_name);
            date_delivered = itemView.findViewById(R.id.date_delivered_label_value);

            date_ordered = itemView.findViewById(R.id.date_label_value);
            time_ordered = itemView.findViewById(R.id.time_label_value);
            time_delivered = itemView.findViewById(R.id.time_delivered_label_value);

            status = itemView.findViewById(R.id.status_label_value);

            item_image = itemView.findViewById(R.id.ordered_item_history_image);
        }
    }
}
