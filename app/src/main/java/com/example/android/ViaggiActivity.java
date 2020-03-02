package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViaggiActivity extends AppCompatActivity {
    Button mButtonIndietro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viaggi);
        mButtonIndietro = findViewById(R.id.button_indietro);

        mButtonIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent indietroIntent = new Intent(ViaggiActivity.this,HomeActivity.class);
                startActivity(indietroIntent);
            }
        });
    }
}
