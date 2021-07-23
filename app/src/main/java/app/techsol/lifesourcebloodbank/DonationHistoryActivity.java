package app.techsol.lifesourcebloodbank;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.techsol.lifesourcebloodbank.Models.BookingModel;

public class DonationHistoryActivity extends AppCompatActivity {

    DatabaseReference CustomerReference;
    RecyclerView mCustomerRecycVw;
    FirebaseAuth auth;
    FrameLayout view;
    private String userid;
    private Dialog dialog;
    private Button btnCancel;
    private EditText storyET;
    Button btnAddStory;
    private TextView donorStoryTV, seekerStoryTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);
        auth = FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getUid();
        CustomerReference = FirebaseDatabase.getInstance().getReference().child("Booking");
        view = findViewById(R.id.customer_content_main);
        mCustomerRecycVw = findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mCustomerRecycVw.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<BookingModel> options = new FirebaseRecyclerOptions.Builder<BookingModel>()
                .setQuery(CustomerReference.orderByChild("seekerid").equalTo(userid), BookingModel.class)
                .build();

        final FirebaseRecyclerAdapter<BookingModel, CustomersViewHolder> adapter = new FirebaseRecyclerAdapter<BookingModel, CustomersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CustomersViewHolder holder, final int position, @NonNull final BookingModel model) {


                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width

                holder.userName.setText(model.getDonorname());
                holder.DateTV.setText(model.getDonationdate());
                holder.donorPhone.setText(model.getDonorid());
                holder.blodTypeTV.setText(model.getBloodtype());
                holder.AddStoryTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog(model.getId());
                    }
                });


                holder.donationReqTV.setText(model.getDonationstatus());
                if (model.getDonationstatus().equals("Accepted")){
                    holder.donationReqTV.setBackgroundColor(Color.GREEN);
                    holder.donationReqTV.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (model.getDonationstatus().equals("Requested")){
                    holder.donationReqTV.setBackgroundColor(getResources().getColor(R.color.gray));
                } else {
                    holder.donationReqTV.setTextColor(Color.WHITE);
                    holder.donationReqTV.setBackgroundColor(getResources().getColor(R.color.purple_700));

                }
                holder.donationReqTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (model.getSeekerid().equals(auth.getCurrentUser().getUid())) {
                            Toast.makeText(DonationHistoryActivity.this, "You Can't update Status", Toast.LENGTH_SHORT).show();
                        } else if (model.getDonationstatus().equals("Requested")) {

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Respond to Request")
                                    .setMessage("Are you sure you want to Donate blood?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            CustomerReference.child(model.getId()).child("donationstatus").setValue("Accepted");

                                            final Snackbar snackbar = Snackbar.make(view, "Offer Accepted Successfully", Snackbar.LENGTH_INDEFINITE);
                                            snackbar.setAction("Ok", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    holder.donationReqTV.setBackgroundColor(Color.GREEN);
                                                    snackbar.dismiss();
                                                }
                                            });
                                            snackbar.show();                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            CustomerReference.child(model.getId()).child("donationstatus").setValue("Declined");

                                            final Snackbar snackbar = Snackbar.make(view, "Offer Declined Successfully", Snackbar.LENGTH_INDEFINITE);
                                            snackbar.setAction("Ok", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    snackbar.dismiss();
                                                }
                                            });
                                            snackbar.show();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();



                        } else if (model.getDonationstatus().equals("Accepted")){
                            Toast.makeText(DonationHistoryActivity.this, "Offer Already Accepted", Toast.LENGTH_SHORT).show();
                        } else  {
                            Toast.makeText(DonationHistoryActivity.this, "Offer Already Declined", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                holder.viewStoryIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewStoryDialog(model.getSeekerstory(), model.getDonorstory());
                    }
                });


            }

            @NonNull
            @Override
            public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.donation_item_layout, viewGroup, false);
                CustomersViewHolder customersViewHolder = new CustomersViewHolder(view);
                return customersViewHolder;
            }
        };

        mCustomerRecycVw.setAdapter(adapter);
        adapter.startListening();

    }


    public static class CustomersViewHolder extends RecyclerView.ViewHolder {


        TextView userName;
        TextView DateTV;
        TextView donorPhone, blodTypeTV, donationReqTV, AddStoryTV;


        ImageView viewStoryIV;

        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName =  itemView.findViewById(R.id.userName);
            DateTV =  itemView.findViewById(R.id.DateTV);
            donorPhone = itemView.findViewById(R.id.donorPhone);
            blodTypeTV = itemView.findViewById(R.id.blodTypeTV);
            donationReqTV = itemView.findViewById(R.id.donationReqTV);
            AddStoryTV = itemView.findViewById(R.id.AddStoryTV);
            viewStoryIV=itemView.findViewById(R.id.viewStoryIV);
        }

    }

    private void openDialog(String id) {
        dialog = new Dialog(DonationHistoryActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.story_dialogbox_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_AppCompat_DayNight_Dialog_Alert;
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.setCancelable(true);

        storyET = dialog.findViewById(R.id.storyET);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnAddStory = dialog.findViewById(R.id.btnAddStory);
        btnAddStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerReference.child(id).child("seekerstory").setValue(storyET.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(DonationHistoryActivity.this, "Story added Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void viewStoryDialog(String seekerStory, String donorStory) {
        dialog = new Dialog(DonationHistoryActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.view_stoty_item_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_AppCompat_DayNight_Dialog_Alert;
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.setCancelable(true);

        if (donorStory==null){
            donorStory="No Story Added Yet";
        }
        if(seekerStory==null){
            seekerStory="No Story Added Yet";
        }
        donorStoryTV = dialog.findViewById(R.id.donorStoryTV);
        donorStoryTV.setText(donorStory);
        seekerStoryTV = dialog.findViewById(R.id.seekerStoryTV);
        seekerStoryTV.setText(seekerStory);
        dialog.show();
    }


}