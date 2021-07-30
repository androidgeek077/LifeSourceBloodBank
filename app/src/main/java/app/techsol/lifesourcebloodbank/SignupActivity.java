package app.techsol.lifesourcebloodbank;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.techsol.lifesourcebloodbank.Models.UserModel;

public class SignupActivity extends AppCompatActivity implements LocationListener {

    EditText NameET, EmailET, PasswordET, PhoneET;
    String nameStr, emailStr, passwprdStr, phoneStr, userLat, userLong, bloodTypeStr;
    Spinner bloodTypeSpnr;
    Button signupBtn;
    TextView gotoLoginTV;
    LinearLayout view;
    ProgressBar mProgressBar;
    FirebaseAuth auth;
    DatabaseReference userRef;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        view = findViewById(R.id.ll);

        mProgressBar = findViewById(R.id.mProgressBar);
        NameET = findViewById(R.id.NameET);
        EmailET = findViewById(R.id.EmailET);
        PasswordET = findViewById(R.id.PasswordET);
        PhoneET = findViewById(R.id.PhoneET);
        bloodTypeSpnr = findViewById(R.id.bloodTypeSpnr);
        signupBtn = findViewById(R.id.signupBtn);

        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStrings();
                if (nameStr.isEmpty()) {
                    NameET.setError("Enter Name");
                } else if (emailStr.isEmpty()) {
                    EmailET.setError("Enter Email");
                } else if (passwprdStr.isEmpty()) {
                    PasswordET.setError("Enter Password");
                } else if (phoneStr.isEmpty()) {
                    PhoneET.setError("Enter Phone No");
                } else if (phoneStr.length() < 11) {
                    PhoneET.setError("Enter a valid PhoneNo");
                } else if (userLat.isEmpty() || userLong.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please Enable Location First", Toast.LENGTH_SHORT).show();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(emailStr, passwprdStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userid = auth.getCurrentUser().getUid();
                                UserModel model = new UserModel(userid, nameStr, emailStr, phoneStr, bloodTypeStr, userLat, userLong);
                                userRef.child(userid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mProgressBar.setVisibility(View.GONE);
                                            final Snackbar snackbar = Snackbar.make(view, "User Added Successfully", Snackbar.LENGTH_INDEFINITE);
                                            snackbar.setAction("Ok", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                                    snackbar.dismiss();
                                                }
                                            });
                                            snackbar.show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    void getStrings() {
        nameStr=NameET.getText().toString();
        emailStr=EmailET.getText().toString();
        passwprdStr=PasswordET.getText().toString();
        phoneStr=PhoneET.getText().toString();
        bloodTypeStr=bloodTypeSpnr.getSelectedItem().toString();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        userLat = location.getLatitude() + "";
        userLong = location.getLongitude() + "";

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}