package com.example.project_x;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;


public class HomeMapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button donateBtn, recieveBtn;

    FirebaseAuth fAuth;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<LiveDonors> list;
    DatabaseReference donors_live;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Location userLocation;

    ProgressDialog loadingBar;


    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(HomeMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();

        } else {
            ActivityCompat.requestPermissions(HomeMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        fAuth = FirebaseAuth.getInstance();

        donateBtn = findViewById(R.id.donateBtn);
        recieveBtn = findViewById(R.id.rcvBtn);
        loadingBar = new ProgressDialog(this);
        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeMapActivity.this, DonationFormActivity.class));

            }
        });

        recieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Searching Donors");
                loadingBar.setMessage("Searching nearby Donors..");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                getDonorsLocation();
            }
        });


        initViews();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }


    public void getCurrentLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mMap = googleMap;
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I Am Here!");
                            mMap.addMarker(markerOptions);
                            float zoomLevel = 18.0f; //This goes up to 21
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                            userLocation = location;

                        }
                    });
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeMapActivity.this, "Failed to aquire location" + e.getMessage(), Toast.LENGTH_SHORT).show();
                getCurrentLocation();
            }
        });
    }

    public void getDonorsLocation() {
        donors_live = FirebaseDatabase.getInstance().getReference("live donors");
        list = new ArrayList<>();
        donors_live.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dropShot : dataSnapshot.getChildren()) {
                    LiveDonors liveDonors = dropShot.getValue(LiveDonors.class);
                    list.add(liveDonors);
                }
                for (int i = 0; i < list.size(); i++) {
                    createMarker(list.get(i).getLatitude(), list.get(i).getLongitude(), list.get(i).getDonorName(), list.get(i).getDonorMobile(), list.get(i).getItemName());
                }
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeMapActivity.this, " " + databaseError + " ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createMarker(double latitude, double longitude, String donorName, String donorMobile, String itemName) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(donorName)
                .snippet("Mobile No: " + donorMobile + "\t\tItem to donate :" + itemName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        markerOptions.visible(false);
        Marker locationMarker = mMap.addMarker(markerOptions);
        LatLng yourLatLang = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        if (SphericalUtil.computeDistanceBetween(yourLatLang, locationMarker.getPosition()) < 10000) {
            locationMarker.setVisible(true);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        } else {
            Toast.makeText(this, "Permisson Denied!", Toast.LENGTH_SHORT).show();
        }
    }


    private void initViews() {
        Log.d("Rk", "initViews: started");

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_drawer);
        toolbar = findViewById(R.id.toolbar);

        navigationView.setNavigationItemSelectedListener(this);

    }

    private void logoutUser()
    {
        fAuth.signOut();
        Intent intent = new Intent(HomeMapActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
