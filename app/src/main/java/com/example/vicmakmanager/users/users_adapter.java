package com.example.vicmakmanager.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicmakmanager.R;

import java.util.ArrayList;

public class users_adapter extends RecyclerView.Adapter<users_adapter.myViewHolder> {

    Context context;
    ArrayList<users_p> users;
    userManipulation moreActions;

    public users_adapter(Context context, ArrayList<users_p> users, userManipulation moreActions) {
        this.context = context;
        this.users = users;
        this.moreActions = moreActions;
    }

    @NonNull
    @Override
    public users_adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_vicmak_user, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull users_adapter.myViewHolder holder, int position) {

        holder.alphDb_no.setText((position + 1) + "");
        holder.email.setText(users.get(position).getEmail());
        holder.phone.setText(users.get(position).getPhone());
        holder.more_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreActions.MoreActionsDialog(users.get(holder.getAdapterPosition()).getEmail(),
                        users.get(holder.getAdapterPosition()).getPhone());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreActions.MoreActionsDialog(users.get(holder.getAdapterPosition()).getEmail(),
                        users.get(holder.getAdapterPosition()).getPhone());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView alphDb_no, email,phone, more_actions;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            alphDb_no = itemView.findViewById(R.id.db_sort_num);
            email = itemView.findViewById(R.id.user_email);
            phone = itemView.findViewById(R.id.user_phone);
            more_actions = itemView.findViewById(R.id.more_actions);
        }
    }
}
