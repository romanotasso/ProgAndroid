package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    Button mButtonCancellaCitta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funzioni_amministratore);
        db = new DatabaseHelper(this);
        mEditCitta = findViewById(R.id.edittext_citta);
        mEditEmail = findViewById(R.id.edittext_email);
        mButtonInserisci = findViewById(R.id.button_inserisci);
        mButtonCancella = findViewById(R.id.button_cancella);
        mButtonCancellaCitta = findViewById(R.id.button_cancella_citta);

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditCitta.getText().toString().equals("")||mEditCitta.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Il campo è vuoto",Toast.LENGTH_SHORT).show();
                } else {
                    boolean checkCitta = db.checkCitta(mEditCitta.getText().toString());
                    if (checkCitta) {
                        boolean isInsert = db.inserisciCitta(mEditCitta.getText().toString());
                        if (isInsert) {
                            Toast.makeText(FunzioniAmministratoreActivity.this, "Città aggiunta", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FunzioniAmministratoreActivity.this, AmministatoreActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(FunzioniAmministratoreActivity.this, "Città già presente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditCitta.getText().toString().equals("")||mEditCitta.getText().toString().trim().isEmpty()) {
                    Integer cancellaRigaUtente = db.deleteDati(mEditEmail.getText().toString());
                    if (cancellaRigaUtente > 0) {
                        Toast.makeText(FunzioniAmministratoreActivity.this, "Dati cancellati", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FunzioniAmministratoreActivity.this, AmministatoreActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(FunzioniAmministratoreActivity.this, "Dati non cancellati ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Integer cancellaRigaCitta = db.deleteCitta(mEditCitta.getText().toString());
                    if (cancellaRigaCitta > 0) {
                        Toast.makeText(FunzioniAmministratoreActivity.this, "Dati cancellati", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FunzioniAmministratoreActivity.this, AmministatoreActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(FunzioniAmministratoreActivity.this, "Dati non cancellati ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }





    }



