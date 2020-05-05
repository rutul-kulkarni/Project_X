package com.example.project_x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NGOActivity extends AppCompatActivity {
    private Button ngoRegisterBtn,ngoLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_g_o);

        ngoRegisterBtn = findViewById(R.id.ngo_register_btn);
        ngoLoginBtn = findViewById(R.id.ngo_login_btn);

        ngoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NGOActivity.this,NGOLoginActivity.class));
            }
        });

        ngoRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NGOActivity.this,RegisterNGOActivity.class));
            }
        });
    }
}
