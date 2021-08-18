package app.techsol.lifesourcebloodbank;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import app.techsol.lifesourcebloodbank.Models.BookingModel;
import app.techsol.lifesourcebloodbank.Models.UserModel;

public class ReminderActivity extends AppCompatActivity {


    DatabaseReference CustomerReference;
    //    CustomerProfileAdapter mProductAdapter;
    RecyclerView mCustomerRecycVw;
    FirebaseAuth auth;
    FrameLayout view;
    private String userid;

    DatabaseReference UserRef;
    private String userPhoneNo;

    private Dialog dialog;
    private Button btnCancel;
    private EditText storyET;
    Button btnAddStory;
    private TextView donorStoryTV, seekerStoryTV;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        auth = FirebaseAuth.getInstance();
        userid = auth.getCurrentUser().getUid();
        CustomerReference = FirebaseDatabase.getInstance().getReference().child("Booking");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
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
                holder.remianingDaysTV.setText(NoofDays(model.getDonationdate()) + " Days left");
                holder.donationText.setText(model.getDonorname()+", You have a due date of Donation" );
                holder.donationDate.setText(model.getDonationdate() );
            }

            @NonNull
            @Override
            public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reminder_item_layout, viewGroup, false);
                CustomersViewHolder customersViewHolder = new CustomersViewHolder(view);
                return customersViewHolder;
            }
        };

        mCustomerRecycVw.setAdapter(adapter);
        adapter.startListening();

    }


    public static class CustomersViewHolder extends RecyclerView.ViewHolder {


        ImageView postImage, mDelCustomerBtn;
        TextView donationDate, donationText;
        TextView remianingDaysTV;
        LinearLayout mItemCountLin;
        Button btnOrderNow;
        CardView cardView;

        ImageView mPlusBtn, mMinusBtn;
        TextView mTotalCount;

        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);

            donationDate = itemView.findViewById(R.id.donationDate);
            remianingDaysTV = itemView.findViewById(R.id.remianingDaysTV);
            donationText = itemView.findViewById(R.id.donationText);


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    long NoofDays(String Dbdate) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate date2 = LocalDate.parse(Dbdate, dtf);

        long daysUntilExpiry = Duration.between(LocalDate.now().atStartOfDay(), date2.atStartOfDay()).toDays();


        return daysUntilExpiry;
    }

    private String getUserName(String userId) {
        UserRef.child(userId).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });
        return name;
    }


}
