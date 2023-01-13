package com.vicshop.vicmakgas;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class vicmak_display_adapter extends RecyclerView.Adapter<vicmak_display_adapter.MyHolder> {
    Context context;
    List<vicmak_display_p> list;
    ListenSelectedItem selected_item;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_vicmak_item, parent, false);

        return new MyHolder(view);
    }

    public vicmak_display_adapter(Context context, List<vicmak_display_p> list, ListenSelectedItem selected_item){
        this.context = context;
        this.list = list;
        this.selected_item = selected_item;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        if(list.get(position).getImage() == 0){

            if(list.get(position).getImage_url().equals("")){
                holder.item_image.setImageResource(R.drawable.ic_baseline_image_24);
            }else{

                Picasso.with(context).load(list.get(position).getImage_url()).into(holder.item_image);
            }
        }else{
            holder.item_image.setImageResource(list.get(position).getImage());
        }
        holder.itemGroupName.setText(list.get(position).getItemGroupName());

        holder.itemPrice.setText(list.get(position).getSelling_price());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_item.ListenSelectedItem_CartIncrement(holder.itemGroupName, holder.itemPrice, holder.item_image, -1);
            }
        });

        // checking the availability of the item to disable the selection button
        if(Integer.parseInt(list.get(position).getAvailability()) <= 0){
            holder.availability.setVisibility(View.VISIBLE);
            holder.button.setEnabled(false);
            holder.button.setBackgroundColor(Color.parseColor("#dfe5ec"));
        }else{
            // item is available
            holder.availability.setVisibility(View.GONE);
            holder.button.setEnabled(true);
            holder.button.setBackgroundColor(Color.parseColor("#00A2FF"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView itemGroupName, itemPrice, availability;
        ImageView item_image;
        Button button;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            itemGroupName = itemView.findViewById(R.id.item_name);
            item_image = itemView.findViewById(R.id.item_image);
            itemPrice = itemView.findViewById(R.id.price);
            button = itemView.findViewById(R.id.button_add_to_cart);

            availability = itemView.findViewById(R.id.availability);
        }
    }
}
