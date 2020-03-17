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
    Button mButtonCitta;
    Button mButtonCancellaCitta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amministatore);
        db = new DatabaseHelper(this);
        mButtonDati = findViewById(R.id.button_dati);
        mButtonCancella = findViewById(R.id.button_cancella);
        mButtonInserisci = findViewById(R.id.button_inserisci);
        mButtonCitta = findViewById(R.id.button_dati_citta);
        mButtonCancellaCitta = findViewById(R.id.button_cancella_citta);

        mButtonDati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getAllData();
                if(res.getCount() == 0){
                    showMessage("Errore", "Nessun record trovato");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("EMAIL: " + res.getString(0) + "\n");
                    //buffer.append("PASSWORD: " + res.getString(1) + "\n");
                    buffer.append("NOME: " + res.getString(2) + "\n");
                    buffer.append("COGNOME: " + res.getString(3) + "\n");
                    buffer.append("CITTA': " + res.getString(4) + "\n");
                    buffer.append("SESSO: " + res.getString(5) + "\n\n");
                }
                showMessage("Dati:",buffer.toString());
            }
        });

        mButtonCitta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getAllDataCitta();
                if(res.getCount() == 0){
                    showMessage("Errore", "Nessun record trovato");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("NOME: " + res.getString(1) + "\n\n");
                }
                showMessage("Dati:",buffer.toString());
            }
        });

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministatoreActivity.this, FunzioniAmministratoreActivity.class);
                startActivity(intent);
            }
        });

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministatoreActivity.this, FunzioniAmministratoreActivity.class);
                startActivity(intent);
            }
        });

        mButtonCancellaCitta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministatoreActivity.this, FunzioniAmministratoreActivity.class);
                startActivity(intent);
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
