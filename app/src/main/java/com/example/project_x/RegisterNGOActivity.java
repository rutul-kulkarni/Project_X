package com.example.project_x;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterNGOActivity extends AppCompatActivity {

    private EditText editTextNGOName,editTextNGOEmail,editTextNGOMobileNumber,editTextNGOPassword;
    private Button buttonRegister;
    private ProgressDialog loadingBar;
    FirebaseAuth auth;
    DatabaseReference rootRef;
    String KEY_NAME = "name" , KEY_MOB_NUM = "mob_number";
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_n_g_o);

        editTextNGOName = findViewById(R.id.register_ngo_name_input);
        editTextNGOEmail = findViewById(R.id.register_ngo_email_input);
        editTextNGOMobileNumber = findViewById(R.id.register_ngo_mobile_input);
        editTextNGOPassword = findViewById(R.id.register_ngo_password_input);
        buttonRegister = findViewById(R.id.create_ngo_account_btn);
        loadingBar = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void createAccount() {
        String NGO_Name , NGO_Email,NGO_Mob_Number,NGO_Password;
        NGO_Name = editTextNGOName.getText().toString();
        NGO_Email = editTextNGOEmail.getText().toString();
        NGO_Mob_Number = editTextNGOMobileNumber.getText().toString();
        NGO_Password = editTextNGOPassword.getText().toString();

        if(TextUtils.isEmpty(NGO_Name))
        {
            Toast.makeText(this,"NGO Name is essential...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(NGO_Email))
        {
            Toast.makeText(this,"NGO Email is essential...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(NGO_Mob_Number))
        {
            Toast.makeText(this,"NGO Mobile Number is essential...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(NGO_Password))
        {
            Toast.makeText(this,"Enter your password...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, While we are checking credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validateAccount(NGO_Name,NGO_Email,NGO_Mob_Number,NGO_Password);

        }

    }

    private void validateAccount(final String ngo_name, final String ngo_email, final String ngo_mob_number, String ngo_password) {
        auth.createUserWithEmailAndPassword(ngo_email,ngo_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    rootRef = FirebaseDatabase.getInstance().getReference();
                    Toast.makeText(RegisterNGOActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                    userId = auth.getCurrentUser().getUid();
                    HashMap<String,Object> ngodata = new HashMap<>();
                    ngodata.put(KEY_NAME,ngo_name);
                    ngodata.put(KEY_MOB_NUM,ngo_mob_number);

                    rootRef.child("users").child(userId).updateChildren(ngodata).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingBar.dismiss();;
                            Toast.makeText(RegisterNGOActivity.this, "Data stord to database", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterNGOActivity.this,NGOLoginActivity.class));
                            RegisterNGOActivity.this.finish();
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterNGOActivity.this, "Error creating account...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }
        });
    }
}
