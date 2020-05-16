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

public class RegisterUserActivity extends AppCompatActivity {

    private EditText editTextUserName, editTextUserEmail, editTextUserMobileNumber, editTextUserPassword;
    private Button buttonRegister;
    private ProgressDialog loadingBar;
    FirebaseAuth auth;
    DatabaseReference rootRef;
    String KEY_NAME = "name", KEY_MOB_NUM = "mob_number";
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        editTextUserName = findViewById(R.id.register_user_name_input);
        editTextUserEmail = findViewById(R.id.register_user_email_input);
        editTextUserMobileNumber = findViewById(R.id.register_user_phone_number_input);
        editTextUserPassword = findViewById(R.id.register_user_password_input);
        buttonRegister = findViewById(R.id.create_user_account_btn);
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
        String User_Name , User_Email,User_Mob_Number,User_Password;
        User_Name = editTextUserName.getText().toString();
        User_Email = editTextUserEmail.getText().toString();
        User_Mob_Number = editTextUserMobileNumber.getText().toString();
        User_Password = editTextUserPassword.getText().toString();

        if(TextUtils.isEmpty(User_Name))
        {
            Toast.makeText(this,"User Name is essential...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(User_Email))
        {
            Toast.makeText(this,"User Email is essential...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(User_Mob_Number))
        {
            Toast.makeText(this,"User Mobile Number is essential...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(User_Password))
        {
            Toast.makeText(this,"Enter your password...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, While we are checking credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validateAccount(User_Name,User_Email,User_Mob_Number,User_Password);

        }

    }

    private void validateAccount(final String user_name, String user_email, final String user_mob_number, String user_password) {
        auth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    rootRef = FirebaseDatabase.getInstance().getReference();
                    Toast.makeText(RegisterUserActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                    userId = auth.getCurrentUser().getUid();
                    HashMap<String,Object> userdata = new HashMap<>();
                    userdata.put(KEY_NAME,user_name);
                    userdata.put(KEY_MOB_NUM,user_mob_number);

                    rootRef.child("users").child(userId).updateChildren(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingBar.dismiss();;
                            Toast.makeText(RegisterUserActivity.this, "Data stored to database", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterUserActivity.this,UserLoginActivity.class));
                            RegisterUserActivity.this.finish();
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterUserActivity.this, "Error creating account...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }
        });
    }
}
