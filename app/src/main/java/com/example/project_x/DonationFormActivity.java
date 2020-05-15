package com.example.project_x;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonationFormActivity extends AppCompatActivity {

    Spinner itemNameSpinner;
    EditText donorNameET,donorMobileET,itemQuantitET;
    Button donateItemBtn;
    String userId;
    ProgressDialog loadingBar;

    FusedLocationProviderClient fusedLocationProviderClient;

    DatabaseReference donorRef;
    LiveDonors donors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_form);

        donorNameET = findViewById(R.id.donor_name);
        donorMobileET = findViewById(R.id.donor_mob_number);
        itemQuantitET = findViewById(R.id.donation_item_quantity);
        itemNameSpinner = findViewById(R.id.donation_item_name_spinner);
        donateItemBtn = findViewById(R.id.donateItemBtn);
        loadingBar = new ProgressDialog(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.item_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemNameSpinner.setAdapter(adapter);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        donorRef = FirebaseDatabase.getInstance().getReference().child("live donors").child(userId);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        donateItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Donating");
                loadingBar.setMessage("Donating the Item");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                donateItem();
            }
        });

    }

    private void donateItem() {
        final String donorName,donorMobile,donationItemName,donationItemQuantity;
        donorName = donorNameET.getText().toString();
        donorMobile = donorMobileET.getText().toString();
        donationItemName = itemNameSpinner.getSelectedItem().toString();
        donationItemQuantity = itemQuantitET.getText().toString();

        if (TextUtils.isEmpty(donorName)) {
            donorNameET.setError("Enter your Name");
            return;
        }
        if (TextUtils.isEmpty(donorMobile)) {
            donorMobileET.setError("Enter your Name");
            return;
        }
        if (TextUtils.isEmpty(donationItemQuantity)) {
            itemQuantitET.setError("Enter your Name");
            return;
        }

        final Task<Location > task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                {
                    donors = new LiveDonors(location.getLatitude(),location.getLongitude(),donationItemName,donorName,donorMobile,donationItemQuantity);
                    donorRef.setValue(donors).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Toast.makeText(DonationFormActivity.this, "Donation Added , Thanks for your contribution", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DonationFormActivity.this,HomeMapActivity.class));
                                finish();
                            }
                            else {
                                loadingBar.dismiss();
                                Toast.makeText(DonationFormActivity.this, "Not saved to database", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(DonationFormActivity.this, "Failed to fetch your location", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
