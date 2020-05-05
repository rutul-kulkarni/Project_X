package com.example.project_x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity {
    private Button continueAsUserBtn,continueAsNGOBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        continueAsUserBtn = findViewById(R.id.continue_as_user_btn);
        continueAsNGOBtn = findViewById(R.id.continue_as_ngo_btn);

        continueAsUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UserActivity.class));
            }
        });
        continueAsNGOBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NGOActivity.class));
            }
        });

    }

}
