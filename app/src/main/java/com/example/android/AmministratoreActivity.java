package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AmministratoreActivity extends AppCompatActivity {

    DatabaseHelper db;
    Button mButtonDati;
    Button mButtonCancella;
    Button mButtonInserisci;
    Button mButtomLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amministratore);
        db = new DatabaseHelper(this);
        mButtonCancella = findViewById(R.id.button_cancella);
        mButtonInserisci = findViewById(R.id.button_inserisci);
        mButtonDati = findViewById(R.id.button_dati);
        mButtomLogout = findViewById(R.id.buttomLogout);

        mButtonDati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministratoreActivity.this, VisualizzaDatiAmministratoreActivity.class);
                startActivity(intent);
            }
        });

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministratoreActivity.this, CancellaAmministratoreActivity.class);
                startActivity(intent);
            }
        });

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministratoreActivity.this, InserisciAmministratoreActivity.class);
                startActivity(intent);
            }
        });

        mButtomLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministratoreActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

}
