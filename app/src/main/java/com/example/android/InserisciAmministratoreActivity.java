package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InserisciAmministratoreActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button mButtonInserisci;
    EditText mEditCitta;
    EditText mEditHotel;
    EditText mEditMonumento;
    EditText mEditGastronomia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserisci_amministratore);
        mEditCitta = findViewById(R.id.edittext_citta);
        mEditGastronomia = findViewById(R.id.edittext_gastronomia);
        mEditHotel = findViewById(R.id.edittext_hotel);
        mEditMonumento = findViewById(R.id.edittext_monumenti);
        mButtonInserisci = findViewById(R.id.button_inserisci);
        db = new DatabaseHelper(this);

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((mEditCitta.getText().toString().trim().isEmpty()) && (mEditGastronomia.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty()) && (mEditMonumento.getText().toString().trim().isEmpty())){
                    Toast.makeText(getApplicationContext(), "I campi sono vuoti", Toast.LENGTH_SHORT).show();
                } else {
                    //INSERIMENTO MONUMENTO
                    if ((mEditCitta.getText().toString().trim().isEmpty()) && (mEditGastronomia.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty())) {
                        boolean checkMonumento = db.checkMonumento(mEditMonumento.getText().toString());
                        if (checkMonumento) {
                            boolean isInsert = db.inserisciMonumento(mEditMonumento.getText().toString());
                            if (isInsert) {
                                Toast.makeText(InserisciAmministratoreActivity.this, "Monumeto aggiunto", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InserisciAmministratoreActivity.this, AmministatoreActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(InserisciAmministratoreActivity.this, "Monumento già presente", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //INSERIMENTO CITTA'
                    if ((mEditMonumento.getText().toString().trim().isEmpty()) && (mEditGastronomia.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty())) {
                        boolean checkCitta = db.checkCitta(mEditCitta.getText().toString());
                        if (checkCitta) {
                            boolean isInsert = db.inserisciCitta(mEditCitta.getText().toString());
                            if (isInsert) {
                                Toast.makeText(InserisciAmministratoreActivity.this, "Città aggiunta", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InserisciAmministratoreActivity.this, AmministatoreActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(InserisciAmministratoreActivity.this, "Città già presente", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //INSERIMENTO GASTRONOMIA
                    if ((mEditCitta.getText().toString().trim().isEmpty()) && (mEditMonumento.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty())) {
                        boolean checkGastronomia = db.checkGastronomia(mEditGastronomia.getText().toString());
                        if (checkGastronomia) {
                            boolean isInsert = db.inserisciGastronomia(mEditGastronomia.getText().toString());
                            if (isInsert) {
                                Toast.makeText(InserisciAmministratoreActivity.this, "Gastronomia aggiunta", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InserisciAmministratoreActivity.this, AmministatoreActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(InserisciAmministratoreActivity.this, "Gastronomia già presente", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //INSERIMENTO HOTEL
                    if ((mEditCitta.getText().toString().trim().isEmpty()) && (mEditMonumento.getText().toString().trim().isEmpty()) && (mEditGastronomia.getText().toString().trim().isEmpty())) {
                        boolean checkHotel = db.checkHotelBB(mEditHotel.getText().toString());
                        if (checkHotel) {
                            boolean isInsert = db.inserisciHotelBB(mEditHotel.getText().toString());
                            if (isInsert) {
                                Toast.makeText(InserisciAmministratoreActivity.this, "Hotel/BB aggiunto", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InserisciAmministratoreActivity.this, AmministatoreActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(InserisciAmministratoreActivity.this, "Hotel/BB già presente", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
