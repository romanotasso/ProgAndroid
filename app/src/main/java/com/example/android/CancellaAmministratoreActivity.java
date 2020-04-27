package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ArrowKeyMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

public class CancellaAmministratoreActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText mEditUtente;
    EditText mEditCitta;
    EditText mEditHotel;
    EditText mEditMonumento;
    EditText mEditGastronomia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancella_amministratore);
        mEditUtente = findViewById(R.id.edittext_utente);
        mEditCitta = findViewById(R.id.edittext_citta);
        mEditGastronomia = findViewById(R.id.edittext_gastronomia);
        mEditHotel = findViewById(R.id.edittext_hotel);
        mEditMonumento = findViewById(R.id.edittext_monumenti);

        db = new DatabaseHelper(this);

    }

    public void onDelete(View view) {
        String str_email = mEditUtente.getText().toString();
        String citta = mEditCitta.getText().toString(); ;
        String monumento = mEditMonumento.getText().toString();
        String gastronomia = mEditGastronomia.getText().toString();
        String hotelbb = mEditHotel.getText().toString();
        String typeUtenti = "cancellaUtente";
        String deleteCitta = "cancellaCitta";
        String deleteMonumento = "cancellaMonumento";
        String deleteHotel = "cancellaHotel";
        String deleteGastronomia = "cancellaGastronomia";



        if(str_email.trim().isEmpty()&&citta.trim().isEmpty()&&monumento.trim().isEmpty()&&gastronomia.trim().isEmpty()&&hotelbb.trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Inserisci dati da cancellare", Toast.LENGTH_SHORT).show();
        }else if(!(str_email.trim().isEmpty())){
            if(citta.trim().isEmpty()&&monumento.trim().isEmpty()&&gastronomia.trim().isEmpty()&&hotelbb.trim().isEmpty()){
                if(!(db.checkEmail(str_email))){
                    db.deleteDati(str_email);
                    BackgroudWorker backgroudWorker= new BackgroudWorker(this);
                    backgroudWorker.execute(typeUtenti,str_email);
                }else {
                    Toast.makeText(getApplicationContext(), "Utente non presente", Toast.LENGTH_SHORT).show();
                }
            }
        }else if(str_email.trim().isEmpty()) {
            if (monumento.trim().isEmpty() && gastronomia.trim().isEmpty() && hotelbb.trim().isEmpty()) {
                if (!db.checkCitta((citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()))) {
                    db.deleteCitta((citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()));
                    Toast.makeText(getApplicationContext(), "Citta eliminata con successo", Toast.LENGTH_SHORT).show();
                    BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                    backgroudWorker.execute(deleteCitta, (citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()));
                } else {
                    Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                }
            }
            if (gastronomia.trim().isEmpty() && hotelbb.trim().isEmpty()) {
                if ((!(monumento.trim().isEmpty()))) {
                        if((!(citta.trim().isEmpty()))){
                            if ((!db.checkMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase()))) {
                                  db.deleteMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                                  Toast.makeText(getApplicationContext(), "Monumento eliminato con successo", Toast.LENGTH_SHORT).show();
                                  BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                                  backgroudWorker.execute(deleteMonumento, monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                            } else {
                                Toast.makeText(getApplicationContext(), "Monumento non presente", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Inserire citta di riferimento", Toast.LENGTH_SHORT).show();
                        }
                }
            }if (monumento.trim().isEmpty() && hotelbb.trim().isEmpty()) {
                if ((!(citta.trim().isEmpty())) && (!(gastronomia.trim().isEmpty()))) {
                    if ((!db.checkGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase()))) {
                        db.deleteGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase());
                        Toast.makeText(getApplicationContext(), "Punto Gastronomia eliminato con successo", Toast.LENGTH_SHORT).show();
                        BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                        backgroudWorker.execute(deleteGastronomia, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase());
                    } else {
                        Toast.makeText(getApplicationContext(), "Punto Gastronomia non presente", Toast.LENGTH_SHORT).show();
                    }
                }
            }if (monumento.trim().isEmpty() && gastronomia.trim().isEmpty()) {
                if ((!(citta.trim().isEmpty())) && (!(hotelbb.trim().isEmpty()))) {
                    if ((!db.checkHotel(hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase()))) {
                        db.deleteHotelBB(hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase());
                        Toast.makeText(getApplicationContext(), "Hotel/BB eliminato con successo", Toast.LENGTH_SHORT).show();
                        BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                        backgroudWorker.execute(deleteHotel, hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase());
                    } else {
                        Toast.makeText(getApplicationContext(), "Hotel/BB non presente", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }
    }
}