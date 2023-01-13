package com.vicshop.vicmakgas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class view_cart_adapter extends RecyclerView.Adapter<view_cart_adapter.MyHolder>{

    Context context;
    List<view_display_p> list;
    ListenSelectedItem listenSelectedItem;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_cart_single_item, parent, false);

        return new MyHolder(view);
    }

    public view_cart_adapter(Context context, List<view_display_p> list, ListenSelectedItem listenSelectedItem){
        this.context = context;
        this.list = list;
        this.listenSelectedItem = listenSelectedItem;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.item_image.setImageDrawable(list.get(position).getImage());
        holder.itemGroupName.setText(list.get(position).getItemGroupName());

        holder.item_price.setText(list.get(position).getItem_price());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ;
            }
        });

        holder.add_similar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenSelectedItem.addSimilarItem(holder.itemGroupName, holder.item_price, holder.item_image);
            }
        });

        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenSelectedItem.removeSimilarItem(holder.getAdapterPosition(), holder.itemGroupName, holder.item_price, holder.item_image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView itemGroupName;
        ImageView item_image;
        TextView item_price;

        ImageView add_similar_item, remove_item;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            itemGroupName = itemView.findViewById(R.id.view_cart_item_name);
            item_image = itemView.findViewById(R.id.view_cart_image);
            item_price = itemView.findViewById(R.id.view_cart_item_price);

            add_similar_item = itemView.findViewById(R.id.add_similar_item);
            remove_item = itemView.findViewById(R.id.remove_item);
        }
    }
}
