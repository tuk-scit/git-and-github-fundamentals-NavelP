package com.example.vicmakmanager.commissions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicmakmanager.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class single_monthly_commissions_display_adapter extends RecyclerView.Adapter<single_monthly_commissions_display_adapter.myHolder> {
    ArrayList<single_day_commission_p> days;
    Context context;

    public single_monthly_commissions_display_adapter(ArrayList<single_day_commission_p> days, Context context) {
        this.days = days;
        this.context = context;
    }

    @NonNull
    @Override
    public single_monthly_commissions_display_adapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_monthly_commissions_display, parent, false);

        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull single_monthly_commissions_display_adapter.myHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        TextView month_name;
        RecyclerView month_days_commissions;
        public myHolder(@NonNull View itemView) {
            super(itemView);

            month_name = itemView.findViewById(R.id.monthName);
            month_days_commissions = itemView.findViewById(R.id.month_days_recycle);
        }
    }
}
