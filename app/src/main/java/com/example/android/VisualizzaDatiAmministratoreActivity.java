package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VisualizzaDatiAmministratoreActivity extends AppCompatActivity {

    Button mButton;
    Button mButtonUtente;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_dati_amministratore);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        mButton = findViewById(R.id.button_dati);
        mButtonUtente = findViewById(R.id.button_utente);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPunto = new Intent(VisualizzaDatiAmministratoreActivity.this, VisualizzaDatiCMRHActivity.class);
                startActivity(intentPunto);
            }
        });

        mButtonUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUtente = new Intent(VisualizzaDatiAmministratoreActivity.this, VisualizzaUtenteActivity.class);
                startActivity(intentUtente);
            }
        });

    }

    /*public void showMessage(String  title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void onAggDatiUtente(View view){

        String type = "aggiornamentoDatiUtente";
        BackgroudWorker backgroudWorker = new BackgroudWorker(this);
        backgroudWorker.execute(type);
        Cursor res = db.getAllData();
        if(res.getCount() == 0){
            showMessage("Errore", "Nessun record trovato");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){

            buffer.append("Email: " + res.getString(0) + "\n");
            buffer.append("Nome: " + res.getString(1) + "\n");
            buffer.append("Cognome: " + res.getString(2) + "\n");
            buffer.append("Città: " + res.getString(3) + "\n");
            buffer.append("Sesso: " + res.getString(4) + "\n");
            buffer.append("Data nascita: " + res.getString(5) + "\n\n");
        }
        showMessage("Utenti:",buffer.toString());
    }

    public void onAggDatiCitta(View view){

        String type = "aggiornamentoDatiCitta";
        BackgroudWorker backgroudWorker = new BackgroudWorker(this);
        backgroudWorker.execute(type);

        Cursor res = db.getAllDataCitta();
        if(res.getCount() == 0){
            showMessage("Errore", "Nessun record trovato");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){

            buffer.append("Città: " + res.getString(0) + "\n\n");
        }
        showMessage("Dati:",buffer.toString());
    }

    public void onAggDatiHotel(View view){

        String type = "aggiornamentoDatiHotel";
        BackgroudWorker backgroudWorker = new BackgroudWorker(this);
        backgroudWorker.execute(type);

        Cursor res = db.getAllDataHotelBB();
        if(res.getCount() == 0){
            showMessage("Errore", "Nessun record trovato");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){

            buffer.append("Hotel/BB: " + res.getString(0) + "\n");
            buffer.append("Citta: " + res.getString(1) + "\n\n");
        }
        showMessage("Dati:",buffer.toString());
    }

    public void onAggDatiMonumenti(View view) {

        String type = "aggiornamentoDatiMonumenti";
        BackgroudWorker backgroudWorker = new BackgroudWorker(this);
        backgroudWorker.execute(type);

        Cursor res = db.getAllDataMonumenti();
        if(res.getCount() == 0){
            showMessage("Errore", "Nessun record trovato");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            buffer.append("Monumento: " + res.getString(0) + "\n");
            buffer.append("Citta: " + res.getString(1) + "\n\n");
        }
        showMessage("Dati:",buffer.toString());
    }

    public void onAggDatiGastronomia(View view) {

        String type = "aggiornamentoDatiGastronomia";
        BackgroudWorker backgroudWorker = new BackgroudWorker(this);
        backgroudWorker.execute(type);

        Cursor res = db.getAllDataGastronomia();
        if(res.getCount() == 0){
            showMessage("Errore", "Nessun record trovato");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){

            buffer.append("Gastronomia: " + res.getString(0) + "\n");
            buffer.append("Citta: " + res.getString(1) + "\n\n");
        }
        showMessage("Dati:",buffer.toString());
    }*/
}
