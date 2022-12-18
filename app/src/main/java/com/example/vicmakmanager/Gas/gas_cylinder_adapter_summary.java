package com.example.vicmakmanager.Gas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicmakmanager.R;

import java.util.ArrayList;
import java.util.List;

public class gas_cylinder_adapter_summary extends RecyclerView.Adapter<gas_cylinder_adapter_summary.MyHolder> {
    gas_cylinders_adapter cylinder_adapter;
    Context context;
    List<gas_cylinder_p_with_summary> list;
    view_item_form V;


    public gas_cylinder_adapter_summary(Context context, List<gas_cylinder_p_with_summary> list, view_item_form V) {
        this.context = context;
        this.list = list;
        this.V = V;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_gas_cylinder_display_with_summary, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        int j = 0;
        if(position == 3){
            j = position - 1;
        }
        holder.gas_type_heading.setText(list.get(position).getList().get(j).getGas_name()+ " Statistics");
        cylinder_adapter = new gas_cylinders_adapter(context, list.get(position).getList(), V);
        holder.recyclerView.setAdapter(cylinder_adapter);

        int counter = list.get(position).getList().size();
        int Total = 0, Filled = 0, Empty = 0, Exchanged = 0, Commission = 0;

        gas_cylinders_p inflate_list_item = new gas_cylinders_p("", "", "",
                "", "", 0, "", "", "");

        int i;

        for (i = 0; i < counter; i++) {
            inflate_list_item = list.get(position).getList().get(i);
            Total += Integer.parseInt(inflate_list_item.getTotal_gas_cylinder_number());
            Filled += Integer.parseInt(inflate_list_item.getGas_cylinder_full());
            Empty += Integer.parseInt(inflate_list_item.getGas_cylinder_empty());
            Exchanged += Integer.parseInt(inflate_list_item.getExchanged());
            Commission += Integer.parseInt(inflate_list_item.getTotal_commission());

            if (inflate_list_item.getGas_weight().equals("6kg")) {
                holder.sTotal.setText(inflate_list_item.getTotal_gas_cylinder_number());
                holder.sFilled.setText(inflate_list_item.getGas_cylinder_full());
                holder.sEmpty.setText(inflate_list_item.getGas_cylinder_empty());
                holder.sExchanged.setText(inflate_list_item.getExchanged());
                holder.sCommission.setText(inflate_list_item.getTotal_commission());
            } else if (inflate_list_item.getGas_weight().equals("13kg")) {
                holder.thTotal.setText(inflate_list_item.getTotal_gas_cylinder_number());
                holder.thFilled.setText(inflate_list_item.getGas_cylinder_full());
                holder.thEmpty.setText(inflate_list_item.getGas_cylinder_empty());
                holder.thExchanged.setText(inflate_list_item.getExchanged());
                holder.thCommission.setText(inflate_list_item.getTotal_commission());
            } else if (inflate_list_item.getGas_weight().equals("25kg")) {
                holder.tfTotal.setText(inflate_list_item.getTotal_gas_cylinder_number());
                holder.tfFilled.setText(inflate_list_item.getGas_cylinder_full());
                holder.tfEmpty.setText(inflate_list_item.getGas_cylinder_empty());
                holder.tfExchanged.setText(inflate_list_item.getExchanged());
                holder.tfCommission.setText(inflate_list_item.getTotal_commission());
            }

        }
        holder.tTotal.setText(Total + "");
        holder.tFilled.setText(Filled + "");
        holder.tEmpty.setText(Empty + "");
        holder.tExchanged.setText(Exchanged + "");
        holder.tCommission.setText(Commission + "");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        TextView tTotal, tFilled, tEmpty, tExchanged, tCommission;
        TextView sTotal, sFilled, sEmpty, sExchanged, sCommission;
        TextView thTotal, thFilled, thEmpty, thExchanged, thCommission;
        TextView tfTotal, tfFilled, tfEmpty, tfExchanged, tfCommission;
        TextView gas_type_heading;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            gas_type_heading = itemView.findViewById(R.id.gas_type);
            recyclerView = itemView.findViewById(R.id.gas_cylinders_recycle_view);
            recyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

            recyclerView.setLayoutManager(linearLayoutManager);

            tTotal = itemView.findViewById(R.id.total_gas_cylinder_number_summary_t);
            tFilled = itemView.findViewById(R.id.gas_cylinder_full_summary_t);
            tEmpty = itemView.findViewById(R.id.gas_cylinder_empty_summary_t);
            tExchanged = itemView.findViewById(R.id.gas_cylinder_exchanged_summary_t);
            tCommission = itemView.findViewById(R.id.gas_cylinder_commission_summary_t);

            sTotal = itemView.findViewById(R.id.total_gas_cylinder_number_summary_s);
            sFilled = itemView.findViewById(R.id.gas_cylinder_full_summary_s);
            sEmpty = itemView.findViewById(R.id.gas_cylinder_empty_summary_s);
            sExchanged = itemView.findViewById(R.id.gas_cylinder_exchanged_summary_s);
            sCommission = itemView.findViewById(R.id.gas_cylinder_commission_summary_s);

            thTotal = itemView.findViewById(R.id.total_gas_cylinder_number_summary_tkg);
            thFilled = itemView.findViewById(R.id.gas_cylinder_full_summary_tkg);
            thEmpty = itemView.findViewById(R.id.gas_cylinder_empty_summary_tkg);
            thExchanged = itemView.findViewById(R.id.gas_cylinder_exchanged_summary_tkg);
            thCommission = itemView.findViewById(R.id.gas_cylinder_commission_summary_tkg);

            tfTotal = itemView.findViewById(R.id.total_gas_cylinder_number_summary_tfkg);
            tfFilled = itemView.findViewById(R.id.gas_cylinder_full_summary_tfkg);
            tfEmpty = itemView.findViewById(R.id.gas_cylinder_empty_summary_tfkg);
            tfExchanged = itemView.findViewById(R.id.gas_cylinder_exchanged_summary_tfkg);
            tfCommission = itemView.findViewById(R.id.gas_cylinder_commission_summary_tfkg);
        }
    }
}
