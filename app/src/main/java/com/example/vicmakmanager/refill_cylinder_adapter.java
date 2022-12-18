package com.example.vicmakmanager;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicmakmanager.Regulators.RefillingManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class refill_cylinder_adapter extends RecyclerView.Adapter<refill_cylinder_adapter.ViewHolder>{

    Context context;
    ArrayList<refill_cylinder_p> cylinders;
    RefillingManager refillingManager;

    public refill_cylinder_adapter(Context context, ArrayList<refill_cylinder_p> cylinders, RefillingManager refillingManager) {
        this.context = context;
        this.cylinders = cylinders;
        this.refillingManager = refillingManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_refill_gas_display, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       if(cylinders.get(position).getImage_url().equals("")){
            holder.gasImage.setImageResource(R.drawable.ic_baseline_image_24);
        }else{
            Picasso.with(context).load(cylinders.get(position).getImage_url()).into(holder.gasImage);
        }

        holder.gas_name.setText(cylinders.get(position).getGas_name());

        holder.gas_weight.setText(cylinders.get(position).getGas_weight());

        holder.refill_price.setText(cylinders.get(position).getGas_price());

        holder.submit_single_cylinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cylinder_number = holder.cylinder_number.getText().toString().trim();

                if(cylinder_number.equals("")){
                    cylinder_number = "0";
                }

                refillingManager.strikeBGColor(holder.itemView);

                RefillingGas.GasesToRefill.add(new gas_refiller_p(cylinders.get(holder.getAdapterPosition()).getGas_name(), cylinders.get(holder.getAdapterPosition()).getGas_weight(),
                        cylinder_number));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cylinders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView gasImage;
        TextView gas_name, gas_weight, refill_price;
        EditText cylinder_number;
        Button submit_single_cylinder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gas_name = itemView.findViewById(R.id.refill_gas_name);
            gas_weight = itemView.findViewById(R.id.refill_gas_weight);
            gasImage = itemView.findViewById(R.id.refill_image);
            refill_price = itemView.findViewById(R.id.refill_gas_price);
            cylinder_number = itemView.findViewById(R.id.refill_gas_number);
            submit_single_cylinder = itemView.findViewById(R.id.submit_single_cylinder);
        }
    }
}
