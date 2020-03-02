package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button mButtonEsplora;
    Button mButtonViaggi;
    Button mButtonCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mButtonEsplora = findViewById(R.id.button_esplora);
        mButtonViaggi = findViewById(R.id.button_viaggi);
        mButtonCoupon = findViewById(R.id.button_coupon);

        mButtonEsplora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent esploraIntent = new Intent(HomeActivity.this, EsploraActivity.class);
                startActivity(esploraIntent);
            }
        });

        mButtonViaggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viaggiIntent = new Intent(HomeActivity.this, ViaggiActivity.class);
                startActivity(viaggiIntent);
            }
        });

        mButtonCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent couponIntent = new Intent(HomeActivity.this, CouponActivity.class);
                startActivity(couponIntent);
            }
        });
    }
}
