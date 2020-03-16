package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FunzioniAmministratoreActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button mButtonAggiorna;
    Button mButtonCancella;
    EditText mEditNome;
    EditText mEditCognome;
    EditText mEditCitta;
    EditText mEditSesso;
    EditText mEditEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funzioni_amministratore);
        db = new DatabaseHelper(this);
        mEditNome = findViewById(R.id.edittext_nome);
        mEditCognome = findViewById(R.id.edittext_cognome);
        mEditCitta = findViewById(R.id.edittext_citta);
        mEditSesso = findViewById(R.id.edittext_sesso);
        mEditEmail = findViewById(R.id.edittext_email);
        mButtonAggiorna = findViewById(R.id.button_modifica);
        mButtonCancella = findViewById(R.id.button_cancella);


        mButtonAggiorna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUpdate = db.updateDati(mEditNome.getText().toString(), mEditCognome.getText().toString(), mEditCitta.getText().toString(), mEditSesso.getText().toString());
                if (isUpdate) {
                    Toast.makeText(FunzioniAmministratoreActivity.this, "Dati aggiornati", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FunzioniAmministratoreActivity.this, "Dati non aggiornati", Toast.LENGTH_SHORT).show();
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
