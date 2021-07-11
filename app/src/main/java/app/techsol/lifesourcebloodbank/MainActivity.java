package app.techsol.lifesourcebloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    TextView goToSignup, forgetPasswordTV;
    TextInputEditText emailET, PasswordET;
    String emailStr, passwordStr;
    FirebaseAuth auth;

    Button loginBtn;
    private ProgressBar mProgressBar;
    private String IdStr;
    PreferenceManager manager;

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        if (auth.getUid() != null) {
//            Intent intent = new Intent(getBaseContext(), BottomNavActivity.class);
//            startActivity(intent);
//            finish();
        }
    }

    // Configure Google Sign In


    // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        forgetPasswordTV = findViewById(R.id.forgetPasswordTV);
//        manager=new PreferenceManager(getBaseContext());


//        forgetPasswordTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getBaseContext(), ForgetPasswordActivity.class));
//            }
//        });


        auth = FirebaseAuth.getInstance();


        emailET = findViewById(R.id.EmailET);

        PasswordET = findViewById(R.id.PasswordET);

        mProgressBar = findViewById(R.id.mProgressBar);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailStr = emailET.getText().toString();
                passwordStr = PasswordET.getText().toString();
                if (emailStr.isEmpty()) {
                    emailET.setError("Enter Email");
                } else if (passwordStr.isEmpty()) {
                    PasswordET.setError("Enter password");
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                IdStr=auth.getCurrentUser().getUid();
//                                getUserData(IdStr);
                                mProgressBar.setVisibility(View.GONE);
//                                startActivity(new Intent(getBaseContext(), BottomNavActivity.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        goToSignup = findViewById(R.id.AccountTV);
        goToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), SignupActivity.class));
            }
        });


    }

    DatabaseReference ref;
    String userId,nameStr, myemailStr, profileImgUrl, userLat, userLong;
//    void getUserData(String UserId) {
//        ref= FirebaseDatabase.getInstance().getReference("User").child(UserId);
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()){
//                    nameStr=dataSnapshot.child("username").getValue().toString();
//                    manager.setUserName(nameStr);
//                    Toast.makeText(MainActivity.this, manager.getUserName(), Toast.LENGTH_SHORT).show();
//
//                    userId = dataSnapshot.child("userId").getValue().toString();
//                    manager.setUserId(userId);
//                    myemailStr=dataSnapshot.child("emailId").getValue().toString();
//                    manager.setUserEmail(myemailStr);
//                    profileImgUrl=dataSnapshot.child("profileImgUrl").getValue().toString();
//                    manager.setUserProfileImgUrl(profileImgUrl);
//                    userLat = dataSnapshot.child("userLat").getValue().toString();
//                    manager.setUserLat(userLat);
//                    userLong = dataSnapshot.child("userLong").getValue().toString();
//                    manager.setUserLong(userLong);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

}
