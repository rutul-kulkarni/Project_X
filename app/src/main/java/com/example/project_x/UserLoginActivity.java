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

public class UserLoginActivity extends AppCompatActivity {
    private EditText textEmail,textPassword;
    private Button buttonLogin;
    ProgressDialog loadingBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        textEmail = findViewById(R.id.login_user_email_input);
        textPassword = findViewById(R.id.login_user_password_input);
        buttonLogin = findViewById(R.id.login_user_btn);
        loadingBar = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = textEmail.getText().toString();
                password = textPassword.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(UserLoginActivity.this, "Enter Email....", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(UserLoginActivity.this, "Enter Password....", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please wait, While we are checking credentials...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Toast.makeText(UserLoginActivity.this, "Logged in succesfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UserLoginActivity.this,HomeMapActivity.class));
                                finish();
                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(UserLoginActivity.this, "Error logging in...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}
