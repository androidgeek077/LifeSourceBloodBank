package app.techsol.lifesourcebloodbank;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.techsol.lifesourcebloodbank.Models.BookingModel;

public class DonationHistoryActivity extends AppCompatActivity {


    DatabaseReference CustomerReference;
    //    CustomerProfileAdapter mProductAdapter;
    RecyclerView mCustomerRecycVw;
    FirebaseAuth auth;
    FrameLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);
        auth = FirebaseAuth.getInstance();
        CustomerReference = FirebaseDatabase.getInstance().getReference().child("Booking");
        view = findViewById(R.id.customer_content_main);
        mCustomerRecycVw = findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mCustomerRecycVw.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<BookingModel> options = new FirebaseRecyclerOptions.Builder<BookingModel>()
                .setQuery(CustomerReference, BookingModel.class)
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
                holder.donationReqTV.setText(model.getDonationstatus());
                if (model.getDonationstatus().equals("Accepted")){
                    holder.donationReqTV.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (model.getDonationstatus().equals("Requested")){
                    holder.donationReqTV.setBackgroundColor(getResources().getColor(R.color.gray));
//                    holder.donationReqTV.setTextColor(Color.WHITE);
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
                            CustomerReference.child(model.getId()).child("donationstatus").setValue("Accepted");

                            final Snackbar snackbar = Snackbar.make(view, "User Added Successfully", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    holder.donationReqTV.setBackgroundColor(Color.GREEN);
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        } else if (model.getDonationstatus().equals("Accepted")){
                            holder.donationReqTV.setBackgroundColor(Color.RED);
                        }
                    }
                });


//                holder.mMinusBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        count=holder.mTotalCount.getText().toString();
//                        countInt=Integer.parseInt(count);
//                        incrementalCount=countInt--;
//
//                        holder.mTotalCount.setText(countInt+"");
//                    }
//                });


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


        ImageView postImage, mDelCustomerBtn;
        TextView userName;
        TextView DateTV;
        LinearLayout mItemCountLin;
        TextView donorPhone, blodTypeTV, donationReqTV;
        CardView cardView;


        ImageView mPlusBtn, mMinusBtn;
        TextView mTotalCount;

        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.userName);
            DateTV = (TextView) itemView.findViewById(R.id.DateTV);
            donorPhone = itemView.findViewById(R.id.donorPhone);
            blodTypeTV = itemView.findViewById(R.id.blodTypeTV);
            donationReqTV = itemView.findViewById(R.id.donationReqTV);
//            cardView=itemView.findViewById(R.id.cardVBtn);


        }

    }
}