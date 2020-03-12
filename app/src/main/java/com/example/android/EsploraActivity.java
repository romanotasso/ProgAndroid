package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class EsploraActivity extends AppCompatActivity {
    private static final String TAG = "Espolora";
    Button mButtonIndietro;
    TextView mEditData;
    DatePickerDialog.OnDateSetListener mDataSetListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esplora);
        mButtonIndietro = findViewById(R.id.button_indietro);
        mButtonIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent indietroIntent = new Intent(EsploraActivity.this,HomeActivity.class);
                startActivity(indietroIntent);
            }
        });

        Log.d(TAG, "onCreate: Started");

    }
}
