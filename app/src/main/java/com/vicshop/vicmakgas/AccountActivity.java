package com.vicshop.vicmakgas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AccountActivity extends AppCompatActivity {
    EditText name, email, phonenumber, address;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vicmak-b2e1e-default-rtdb.firebaseio.com");
    public static String textPhonenumber = "";
    public static String emailAddress = "";

    Button delete_account, save_account;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    View dialogViewSet;
    Button dialogOk, dialogCancel;
    TextView dialogTextMessage, dialogActionHeading;

    ProgressBar progress;
    String gName, gEmail, gPhonenumber, gArea, gStreet, gNearestBuilding, gAddress;
    String account_exists;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        getSupportActionBar().hide();

        sharedPreferences = getApplication().getSharedPreferences("REMEMBER_ME", Context.MODE_PRIVATE);

        dialogViewSet = getLayoutInflater().inflate(R.layout.account_dialog, null);
        dialogOk = dialogViewSet.findViewById(R.id.ok);
        dialogCancel = dialogViewSet.findViewById(R.id.cancel);

        progress = dialogViewSet.findViewById(R.id.progress);

        dialogActionHeading = dialogViewSet.findViewById(R.id.account_action_heading);
        dialogTextMessage = dialogViewSet.findViewById(R.id.account_text_message);

        name = findViewById(R.id.account_name);
        email = findViewById(R.id.account_email);
        phonenumber = findViewById(R.id.account_phonenumber);
        address = findViewById(R.id.account_address);

        delete_account = findViewById(R.id.delete_account);
        save_account = findViewById(R.id.save_account);

        displayAccountDetails();

        builder = new AlertDialog.Builder(this);
        builder.setView(dialogViewSet);
        builder.setCancelable(true);

        alertDialog = builder.create();

        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogActionHeading.setText("Deleting Account");
                dialogTextMessage.setText("Are you sure you want to delete this Account? The action is irreversible");

                dialogOk.setText("Delete");
                dialogCancel.setText("Ignore");

                alertDialog.show();
            }
        });

        save_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogActionHeading.setText("Edit Account");
                dialogTextMessage.setText("This action will edit your account details. Are you sure you want to change your account details?");

                dialogOk.setText("Edit");
                dialogCancel.setText("Ignore");

                alertDialog.show();
            }
        });

        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogOk.getText().equals("Delete")) {

                    dialogActionHeading.setText("Deleting Account");
                    dialogTextMessage.setVisibility(View.GONE);
                    dialogOk.setVisibility(View.GONE);
                    dialogCancel.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            databaseReference.child("users").child(textPhonenumber).setValue(null);
                            progress.setVisibility(View.GONE);
                            dialogActionHeading.setText("Account deleted");
                            dialogTextMessage.setText("Your account has been deleted successfully!");
                            dialogTextMessage.setVisibility(View.VISIBLE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertDialog.dismiss();

                                    Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }, 1500);

                        }
                    }, 3000);


                } else if (dialogOk.getText().equals("Edit")) {

                    gName = name.getText().toString();
                    gEmail = email.getText().toString();
                    gPhonenumber = phonenumber.getText().toString();

                    gAddress = address.getText().toString();

                    if (gName.isEmpty() || gEmail.isEmpty() || gPhonenumber.isEmpty() || gAddress.isEmpty()) {
                        Toast.makeText(AccountActivity.this, "Cannot submit blank fields", Toast.LENGTH_SHORT).show();
                    } else {
                        //validating phonenumber
                        if (validatePhoneNumber(gPhonenumber)) {
                            //testing for the address input
                            if (getCommaNumber(gAddress) != 2) {
                                Toast.makeText(AccountActivity.this, "Invalid address, input your Area, Street and Nearest building \nSeparates by commas", Toast.LENGTH_SHORT).show();
                            } else {

                                //check if there is a user with the new account number
                                databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if ((snapshot.hasChild(gPhonenumber)) && (!gPhonenumber.equals(textPhonenumber))) {
                                            account_exists = "true";
                                        } else {
                                            account_exists = "false";
                                            alertDialog.show();

                                            dialogActionHeading.setText("Updating Account");
                                            dialogTextMessage.setVisibility(View.GONE);
                                            dialogOk.setVisibility(View.GONE);
                                            dialogCancel.setVisibility(View.GONE);
                                            progress.setVisibility(View.VISIBLE);

                                            gAddress = gAddress.replaceAll("\\s", "");

                                            gArea = gAddress.split(",")[0];
                                            gStreet = gAddress.split(",")[1];
                                            gNearestBuilding = gAddress.split(",")[2];

                                            String[] children = {"Area", "Email", "Name", "Nearest_building", "Street"};
                                            String[] values = {gArea, gEmail, gName, gNearestBuilding, gStreet};

                                            for (int i = 0; i < children.length; i++) {
                                                databaseReference.child("users").child(gPhonenumber).child(children[i]).setValue(values[i]);
                                            }

                                            //checking if the phonenumber was different
                                            if (!textPhonenumber.equals(gPhonenumber)) {
                                                //we the copy the history of the old phonenumber to the new number and then delete the old account number

                                                copyHistory(textPhonenumber, gPhonenumber);
                                                //if it copies correctly
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progress.setVisibility(View.GONE);
                                                        dialogActionHeading.setText("Account Updated");
                                                        dialogTextMessage.setText("Your account has been Updated successfully!");
                                                        dialogTextMessage.setVisibility(View.VISIBLE);

                                                        databaseReference.child("users").child(textPhonenumber).setValue(null);
                                                        textPhonenumber = gPhonenumber;

                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                displayAccountDetails();

                                                                sharedPreferences.edit().putString("phoneNumber", gPhonenumber).apply();

                                                                alertDialog.dismiss();
                                                                dialogCancel.setVisibility(View.VISIBLE);
                                                                dialogOk.setVisibility(View.VISIBLE);
                                                            }
                                                        }, 1500);
                                                    }
                                                }, 3000);

                                            } else {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progress.setVisibility(View.GONE);
                                                        dialogActionHeading.setText("Account Updated");
                                                        dialogTextMessage.setText("Your account has been Updated successfully!");
                                                        dialogTextMessage.setVisibility(View.VISIBLE);

                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                displayAccountDetails();

                                                                alertDialog.dismiss();
                                                                dialogCancel.setVisibility(View.VISIBLE);
                                                                dialogOk.setVisibility(View.VISIBLE);
                                                            }
                                                        }, 1500);

                                                    }
                                                }, 3000);
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        } else {
                            Toast.makeText(AccountActivity.this, "Phonenumber should begin with 07 and ,make sure they are 10 digits with no alphabet or other characters", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void displayAccountDetails() {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(textPhonenumber)) {
                    name.setText("" + snapshot.child(textPhonenumber).child("Name").getValue());
                    email.setText("" + snapshot.child(textPhonenumber).child("Email").getValue());
                    phonenumber.setText(textPhonenumber);
                    address.setText("" + snapshot.child(textPhonenumber).child("Area").getValue() + ", " + snapshot.child(textPhonenumber).child("Street").getValue() +
                            ", " + snapshot.child(textPhonenumber).child("Nearest_building").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private int getCommaNumber(String s) {
        int i, count = 0;
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ',') {
                count++;
            }
        }

        return count;
    }

    private boolean validatePhoneNumber(String PhoneNumber) {
        if ((PhoneNumber.charAt(0) != '0') || PhoneNumber.charAt(1) != '7') {
            return false;
        }

        if (PhoneNumber.length() != 10) {
            return false;
        }

        for (int i = 0; i < 10; i++) {
            if ((PhoneNumber.charAt(i) < 48) || (PhoneNumber.charAt(i) > 57)) {
                return false;
            }

        }

        return true;
    }

    private void copyHistory(String oldAccountNumber, String newAccountNumber) {

        databaseReference.child("users").child(oldAccountNumber).child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot history_item : snapshot.getChildren()) {
                    for (DataSnapshot history_item_detail : history_item.getChildren()) {
                        databaseReference.child("users").child(newAccountNumber).child("History").child(history_item.getKey())
                                .child(history_item_detail.getKey()).setValue(history_item_detail.getValue(String.class));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}