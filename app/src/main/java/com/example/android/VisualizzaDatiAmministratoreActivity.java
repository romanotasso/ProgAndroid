package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;

public class VisualizzaDatiAmministratoreActivity extends AppCompatActivity {

    Button mButton;
    Button mButtonUtente;
    DatabaseHelper db;
    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    ImageView immagineProfilo;
    CardView pulsVisualizzaDati,pulsVisualizzaUtente;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_dati_amministratore);
        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaDatiAmministratoreActivity.this, R.color.orange));
        getSupportActionBar().setTitle("Visualizza Dati");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHelper(this);
        pulsVisualizzaDati = findViewById(R.id.pulsVisDati);
        pulsVisualizzaUtente = findViewById(R.id.visualizzaUtente);
        pulsVisualizzaDati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPunto = new Intent(VisualizzaDatiAmministratoreActivity.this, VisualizzaDatiCMRHActivity.class);
                startActivity(intentPunto);
            }
        });

        pulsVisualizzaUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUtente = new Intent(VisualizzaDatiAmministratoreActivity.this, VisualizzaUtenteActivity.class);
                startActivity(intentUtente);
            }
        });

    }

}
