package app.techsol.lifesourcebloodbank;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.techsol.lifesourcebloodbank.Models.BookingModel;
import app.techsol.lifesourcebloodbank.Models.UserModel;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    DatabaseReference BookMechanic;
    FirebaseAuth auth;

    LatLng current;
    String[] polylines;
    private static final String DirectionUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    String apiKey = "AIzaSyBe6jPPwp3T121djiVmLpW0FhodNdpSYfQ";

    private ArrayList<String> mAdmins = new ArrayList<>();
    private ArrayList<String> mAdminsIds = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;

    private Double StdLatitude, StdLonitude;
    ArrayList mLocationList, mLongList;

    DatabaseReference StudentRef;
    ArrayList mLocationArr;
    Double latDouble, langDouble;
    FirebaseAuth mAuth;
    DatabaseReference MechanicLocation;
    MapView     mMapView;
    private GoogleMap googleMap;
    private LatLng mylocation;
    private Dialog dialog;
    private LatLng mechaniclocation;
    private TextView distanceTV;
    private Button btnBook;
    private Button btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MechanicLocation = FirebaseDatabase.getInstance().getReference("Users");
        BookMechanic = FirebaseDatabase.getInstance().getReference("Booking");
        auth=FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                LatLng Sargodha = new LatLng(32.0740, 72.6861);
                LatLng kachar_Pur = new LatLng(32.092743, 72.733996);
                LatLng ChakNo52 = new LatLng(32.147929, 72.694703);
                LatLng GPO = new LatLng(32.073278, 72.668703);

//                googleMap.addMarker(new MarkerOptions().position(Sargodha).title("Home").snippet("Sargodha"));
//                googleMap.addMarker(new MarkerOptions().position(kachar_Pur).title("Nazir AutoShop").snippet("0345-8757847"));
//                googleMap.addMarker(new MarkerOptions().position(ChakNo52).title("Bismillah Workshop").snippet("0300-3453463"));
//                googleMap.addMarker(new MarkerOptions().position(GPO).title("Sargodha Motors").snippet("0483217654"));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(Sargodha).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                getAllDonors();

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if (location != null) {
                                    mylocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    // Logic to handle location object
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
//
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                                            .zoom(12)                   // Sets the zoom
                                            .bearing(90)                // Sets the orientation of the camera to east
                                            .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                            .build();                   // Creates a CameraPosition from the builder
                                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                }

                            }
                        });
            }

        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        current = new LatLng(location.getLatitude(), location.getLongitude());
//        Toast.makeText(this, ""+current, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private void getAllDonors() {
        MechanicLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> adminsList = dataSnapshot.getChildren();
                    for (DataSnapshot admins : adminsList) {
                        UserModel model = admins.getValue(UserModel.class);
                        AddMarkerToMap(Double.parseDouble(model.getUserlat()), Double.parseDouble(model.getUserlong()), model.getName(), model.getBloodtype(), model.getPhoneno());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddMarkerToMap(Double Lat, Double Lng, String name, String type, String phoneno) {
        mechaniclocation = new LatLng(Lat, Lng);
        googleMap.addMarker(new MarkerOptions().position(mechaniclocation).title(name+", Blood Type: "+type).snippet(phoneno));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull @NotNull Marker marker) {
                if (!(mylocation.latitude==0.0)){
                    String str = marker.getTitle();
                    List<String> elephantList = Arrays.asList(str.split(","));
                    openDialog(String.format("%.2f", distance(mylocation.latitude, mylocation.longitude, marker.getPosition().latitude, marker.getPosition().longitude)),  elephantList.get(0), elephantList.get(1), marker.getSnippet(), auth.getCurrentUser().getUid());
                } else  {
                    Toast.makeText(MapsActivity.this, "please get your location first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openDialog(String distance, String DonorName, String bloodType, String donorId, String seekerid) {
        dialog = new Dialog(MapsActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.donor_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_AppCompat_DayNight_Dialog_Alert;
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.setCancelable(true);
        distanceTV = dialog.findViewById(R.id.distanceTV);
        distanceTV.setText(distance+" KMs");
        btnBook = dialog.findViewById(R.id.btnBook);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushid=BookMechanic.push().getKey();
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                BookingModel model=new BookingModel(pushid,seekerid, donorId, DonorName,bloodType, currentDate, "Requested");
                BookMechanic.child(pushid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MapsActivity.this, "Request for donation sent Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        btnCancel = dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}


