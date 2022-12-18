package com.example.vicmakmanager.commissions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicmakmanager.R;

import java.util.ArrayList;

public class single_daily_commission_display_adapter extends RecyclerView.Adapter<single_daily_commission_display_adapter.myHolder> {

    ArrayList<single_day_commission_p> commissions;
    Context context;


    public single_daily_commission_display_adapter(ArrayList<single_day_commission_p> commissions, Context context) {
        this.commissions = commissions;
        this.context = context;
    }

    @NonNull
    @Override
    public single_daily_commission_display_adapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_day_commission, parent, false);

        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull single_daily_commission_display_adapter.myHolder holder, int position) {
        holder.date.setText(commissions.get(position).getDate());
        holder.gas.setText(commissions.get(position).getGas_commission());
        holder.regulator.setText(commissions.get(position).getRegulator_commission());
        holder.burner.setText(commissions.get(position).getBurner_commission());
        holder.grill.setText(commissions.get(position).getGrill_commission());
        holder.pipe.setText(commissions.get(position).getPipe_commission());
    }

    @Override
    public int getItemCount() {
        return commissions.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        TextView date, gas, regulator, burner, grill, pipe;
        public myHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.Date);
            gas = itemView.findViewById(R.id.Gas_Commission);
            regulator = itemView.findViewById(R.id.Regulator_Commission);
            burner = itemView.findViewById(R.id.Burner_Commission);
            grill = itemView.findViewById(R.id.Grill_Commission);
            pipe = itemView.findViewById(R.id.Pipe_Commission);
        }
    }
}
