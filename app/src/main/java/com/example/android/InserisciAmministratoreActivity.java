package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
    EditText mEditEmail;
    EditText mEditPassword;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserisci_amministratore);
        mEditCitta = findViewById(R.id.edittext_citta);
        mEditGastronomia = findViewById(R.id.edittext_gastronomia);
        mEditHotel = findViewById(R.id.edittext_hotel);
        mEditMonumento = findViewById(R.id.edittext_monumenti);
        mButtonInserisci = findViewById(R.id.button_inserisci);
        mEditEmail = findViewById(R.id.edittext_email_agg);
        mEditPassword = findViewById(R.id.edittext_pass);
        db = new DatabaseHelper(this);


    }

    public void onInsert (View view) {

        String citta = mEditCitta.getText().toString(); ;
        String monumento = mEditMonumento.getText().toString();
        String gastronomia = mEditGastronomia.getText().toString();
        String hotelbb = mEditHotel.getText().toString();


        String type = "inserimento";

        if ((citta.trim().isEmpty()) && ((monumento.trim().isEmpty() && gastronomia.trim().isEmpty() && hotelbb.trim().isEmpty()))) {
            Toast.makeText(getApplicationContext(), "Inserisci i dati", Toast.LENGTH_SHORT).show();
        } else if (!(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()).trim().isEmpty() && (db.checkCitta(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()))) {
            Toast.makeText(getApplicationContext(), "Città inserita con successo", Toast.LENGTH_SHORT).show();
            hotelbb = "";
            gastronomia = "";
            monumento = "";
            db.inserisciCitta(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
            backgroudWorker.execute(type, citta, monumento, gastronomia, hotelbb);
        }else if ((!(citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()).trim().isEmpty()) && !(db.checkCitta(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()))&& monumento.trim().isEmpty() && gastronomia.trim().isEmpty() && hotelbb.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(),"Città già presente", Toast.LENGTH_SHORT).show();
        } else if ((monumento.trim().isEmpty() && gastronomia.trim().isEmpty() && hotelbb.trim().isEmpty())) {
            Toast.makeText(getApplicationContext(), "Inserisci i dati", Toast.LENGTH_SHORT).show();
        }  else if((!(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()).trim().isEmpty()) && (hotelbb.trim().isEmpty() && gastronomia.trim().isEmpty())) {
            if((db.checkMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase()))){
                Toast.makeText(getApplicationContext(),"Monumento inserito con successo", Toast.LENGTH_SHORT).show();
                hotelbb = "";
                gastronomia = "";
                db.inserisciMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                backgroudWorker.execute(type, citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase(), monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase(), gastronomia, hotelbb);
            }else {
                Toast.makeText(getApplicationContext(),"Monumento gia presente", Toast.LENGTH_SHORT).show();
            }
        } else if((!(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()).trim().isEmpty()) && (monumento.trim().isEmpty() && hotelbb.trim().isEmpty())) {
            if((db.checkGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase()))){
                Toast.makeText(getApplicationContext(),"Gastronomia inserita con successo", Toast.LENGTH_SHORT).show();
                monumento = "";
                hotelbb = "";
                db.inserisciGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                backgroudWorker.execute(type, citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(), hotelbb);
            }else {
                Toast.makeText(getApplicationContext(),"Gastronomia gia presente", Toast.LENGTH_SHORT).show();
            }
        } else if((!(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()).trim().isEmpty()) && (monumento.trim().isEmpty() && gastronomia.trim().isEmpty())) {
            if((db.checkHotel(hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase()))){
                Toast.makeText(getApplicationContext(), "Hotel/BB inserito con successo", Toast.LENGTH_SHORT).show();
                monumento = "";
                gastronomia = "";
                db.inserisciHotelBB(hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase(), citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                backgroudWorker.execute(type, citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia, hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase());
            }else {
                Toast.makeText(getApplicationContext(), "Hotel/BB gia presente", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
