package com.example.vicmakmanager.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vicmakmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewUsers extends AppCompatActivity implements userManipulation{

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com/");
    RecyclerView recyclerView;
    ArrayList<users_p> users;
    ArrayList<users_p> custom_search_users;
    users_adapter adapter;

    EditText search_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        getSupportActionBar().hide();

        search_user = findViewById(R.id.search_user);

        recyclerView = findViewById(R.id.vicmak_users);
        users = new ArrayList<>();

        search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_user.setHint("Search user by phone number");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                custom_search_users = new ArrayList<>();
                String input_num = search_user.getText().toString().trim();
                for(users_p user : users){
                    if(user.getPhone().startsWith(input_num)){
                        custom_search_users.add(user);
                    }
                }
                adapter = new users_adapter(ViewUsers.this, custom_search_users, ViewUsers.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                search_user.setHint("Search user by phone number");
            }
        });

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = new ArrayList<>();
                for(DataSnapshot user_phone : snapshot.getChildren()){
                    users.add(new users_p(user_phone.child("Email").getValue(String.class),
                            user_phone.getKey()));
                }
                adapter = new users_adapter(ViewUsers.this, users, ViewUsers.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void MoreActionsDialog(String email, String phone) {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUsers.this);
        Button delete, contact;

        View view = getLayoutInflater().inflate(R.layout.activity_vicmak_user_dialog, null);

        delete = view.findViewById(R.id.Delete);
        contact = view.findViewById(R.id.Contact);

        info(view, email, phone);

        builder.setView(view);

        alert = builder.create();

        alert.show();

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(
                        Intent.ACTION_DIAL, Uri.parse("tel:" + phone)
                ));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alert2;
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ViewUsers.this);
                builder2.setTitle("Confirm Account Deletion");
                builder2.setMessage("Are you sure you want to delete this Account? \nAll Data related to this account will be permanently deleted\nincluding the History" +
                        "\nBut commissions done before the date of deletion will be reserved");
                builder2.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child("users").child(phone).setValue(null);
                        dialogInterface.dismiss();
                        alert.dismiss();
                    }
                });

                builder2.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        alert.dismiss();
                    }
                });
                alert2 = builder2.create();
                alert2.show();

            }
        });

    }

    private void info(View view, String email, String phone) {
        TextView dialog_email, dialog_phone;
        dialog_email = view.findViewById(R.id.dialog_email);
        dialog_phone = view.findViewById(R.id.dialog_phone);

        dialog_email.setText(email);
        dialog_phone.setText(phone);
    }
}