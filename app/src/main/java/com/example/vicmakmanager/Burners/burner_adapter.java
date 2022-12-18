package com.example.vicmakmanager.Burners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicmakmanager.R;

import java.util.List;

public class burner_adapter extends RecyclerView.Adapter<burner_adapter.MyHolder> {
    Context context;
    List<burner_p> list;
    view_burner_update_form update;

    public burner_adapter(Context context, List<burner_p> list, view_burner_update_form update) {
        this.context = context;
        this.list = list;
        this.update = update;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_burner_display, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.burner_name.setText(list.get(position).getBurner_type());
        holder.burner_type.setText(list.get(position).getBurner_type());
        holder.burner_commission.setText(list.get(position).getCommission());
        holder.burner_stock.setText(list.get(position).getStock());
        holder.sold_burners.setText(list.get(position).getSold());

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
        TextView burner_type, sold_burners, burner_stock, burner_commission, burner_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            burner_type = itemView.findViewById(R.id.burner_type);
            sold_burners = itemView.findViewById(R.id.sold_burners);
            burner_stock = itemView.findViewById(R.id.burner_stock);
            burner_commission = itemView.findViewById(R.id.burner_commission);
            burner_name = itemView.findViewById(R.id.burner_name);
        }
    }
}
