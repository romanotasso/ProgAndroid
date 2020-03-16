package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FunzioniAmministratoreActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button mButtonInserisci;
    Button mButtonCancella;
    EditText mEditCitta;
    EditText mEditEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funzioni_amministratore);
        db = new DatabaseHelper(this);
        mEditCitta = findViewById(R.id.edittext_citta);
        mEditEmail = findViewById(R.id.edittext_email);
        mButtonInserisci = findViewById(R.id.button_inserisci);
        mButtonCancella = findViewById(R.id.button_cancella);

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInsert = db.inserisciCitta(mEditCitta.getText().toString());
                if (isInsert) {
                    Toast.makeText(FunzioniAmministratoreActivity.this, "Città aggiuntai", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FunzioniAmministratoreActivity.this, "Città non aggiunta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer cancellaRiga = db.deleteDati(mEditEmail.getText().toString());
                if(cancellaRiga>0){
                    Toast.makeText(FunzioniAmministratoreActivity.this, "Dati cancellati", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FunzioniAmministratoreActivity.this, "Dati non cancellati ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
