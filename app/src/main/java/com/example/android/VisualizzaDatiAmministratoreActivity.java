package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VisualizzaDatiAmministratoreActivity extends AppCompatActivity {

    Button mButtonCittà;
    Button mButtonMonumenti;
    Button mButtonGastronomia;
    Button mButtonHotelBB;
    Button mButtonUtente;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_dati_amministratore);

        db = new DatabaseHelper(this);
        mButtonCittà = findViewById(R.id.button_dati_città);
        mButtonMonumenti = findViewById(R.id.button_dati_monumenti);
        mButtonGastronomia = findViewById(R.id.button_dati_gastronomia);
        mButtonHotelBB = findViewById(R.id.button_dati_hotelBB);
        mButtonUtente = findViewById(R.id.button_dati_utente);
        mButtonUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getAllData();
                if(res.getCount() == 0){
                    showMessage("Errore", "Nessun record trovato");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){

                    buffer.append("Email: " + res.getString(0) + "\n");
                    buffer.append("Nome: " + res.getString(2) + "\n");
                    buffer.append("Cognome: " + res.getString(3) + "\n");
                    buffer.append("Città: " + res.getString(4) + "\n");
                    buffer.append("Sesso: " + res.getString(5) + "\n");
                    buffer.append("Data nascita: " + res.getString(6) + "\n\n");
                }
                showMessage("Utenti:",buffer.toString());
            }
        });

        mButtonCittà.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        mButtonMonumenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getAllDataMonumenti();
                if(res.getCount() == 0){
                    showMessage("Errore", "Nessun record trovato");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("Monumento: " + res.getString(1) + "\n");
                    buffer.append("Citta: " + res.getString(2) + "\n\n");
                }
                showMessage("Dati:",buffer.toString());
            }
        });

        mButtonGastronomia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getAllDataGastronomia();
                if(res.getCount() == 0){
                    showMessage("Errore", "Nessun record trovato");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){

                    buffer.append("Gastronomia: " + res.getString(1) + "\n");
                    buffer.append("Citta: " + res.getString(2) + "\n\n");
                }
                showMessage("Dati:",buffer.toString());
            }
        });

        mButtonHotelBB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getAllDataHotelBB();
                if(res.getCount() == 0){
                    showMessage("Errore", "Nessun record trovato");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){

                    buffer.append("Hotel/BB: " + res.getString(1) + "\n");
                    buffer.append("Citta: " + res.getString(2) + "\n\n");
                }
                showMessage("Dati:",buffer.toString());
            }
        });
    }

    public void showMessage(String  title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}