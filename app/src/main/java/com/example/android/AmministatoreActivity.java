package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AmministatoreActivity extends AppCompatActivity {

    DatabaseHelper db;
    Button mButtonDati;
    Button mButtonCancella;
    Button mButtonInserisci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amministatore);
        db = new DatabaseHelper(this);
        mButtonCancella = findViewById(R.id.button_cancella);
        mButtonInserisci = findViewById(R.id.button_inserisci);
        mButtonDati = findViewById(R.id.button_dati);

        mButtonDati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministatoreActivity.this, VisualizzaDatiAmministratoreActivity.class);
                startActivity(intent);
            }
        });

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministatoreActivity.this, CancellaAmministratoreActivity.class);
                startActivity(intent);
            }
        });

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministatoreActivity.this, InserisciAmministratoreActivity.class);
                startActivity(intent);
            }
        });

    }

}
