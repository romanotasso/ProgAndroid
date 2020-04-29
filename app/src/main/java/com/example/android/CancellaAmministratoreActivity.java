package com.example.android;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.view.View;

import android.widget.EditText;

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
                        if(!db.checkCitta(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())){
                            if ((!db.checkMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase(),citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()))) {
                                db.deleteMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                                Toast.makeText(getApplicationContext(), "Monumento eliminato con successo", Toast.LENGTH_SHORT).show();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                                backgroudWorker.execute(deleteMonumento, monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                            } else {
                                Toast.makeText(getApplicationContext(), "Monumento non presente o non corrispondete alla citta", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        mEditCitta.setError("CAMPO OBBLIGATORIO");
                        mEditCitta.requestFocus();
                        //Toast.makeText(getApplicationContext(), "Inserire citta di riferimento", Toast.LENGTH_SHORT).show();
                    }
                }
            }if (monumento.trim().isEmpty() && hotelbb.trim().isEmpty()) {
                if ((!(gastronomia.trim().isEmpty()))) {
                    if((!(citta.trim().isEmpty()))){
                        if(!db.checkCitta(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            if ((!db.checkGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase()))) {
                                db.deleteGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                                Toast.makeText(getApplicationContext(), "Punto Gastronomia eliminato con successo", Toast.LENGTH_SHORT).show();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                                backgroudWorker.execute(deleteGastronomia, gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            } else {
                                Toast.makeText(getApplicationContext(), "Punto Gastronomia non presente", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        mEditCitta.setError("CAMPO OBBLIGATORIO");
                        mEditCitta.requestFocus();
                        //Toast.makeText(getApplicationContext(), "Inserire citta di riferimento", Toast.LENGTH_SHORT).show();
                    }

                }
            }if (monumento.trim().isEmpty() && gastronomia.trim().isEmpty()) {
                if (!(hotelbb.trim().isEmpty())) {
                    if((!(citta.trim().isEmpty()))){
                        if(!db.checkCitta(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            if ((!db.checkHotel(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase()))) {
                                db.deleteHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                                Toast.makeText(getApplicationContext(), "Hotel/BB eliminato con successo", Toast.LENGTH_SHORT).show();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                                backgroudWorker.execute(deleteHotel, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                            } else {
                                Toast.makeText(getApplicationContext(), "Hotel/BB non presente", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        mEditCitta.setError("CAMPO OBBLIGATORIO");
                        mEditCitta.requestFocus();
                    }

                }
            }if((gastronomia.trim().isEmpty()&&((!monumento.trim().isEmpty()&& !(hotelbb.trim().isEmpty()))))){
                if(!(citta.trim().isEmpty())) {
                    if (!db.checkCitta(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        boolean veroMon;
                        boolean veroHot;
                        veroMon = db.checkMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                        veroHot = db.checkHotel(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                            if(veroMon==false && veroHot == false){
                                db.deleteHotelBB(hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase());
                                db.deleteMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                                Toast.makeText(getApplicationContext(), "Monumento e Hotel/BB eliminati con successo", Toast.LENGTH_SHORT).show();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                                BackgroudWorker backgroudWorker1 = new BackgroudWorker(this);
                                backgroudWorker.execute(deleteMonumento, monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                                backgroudWorker1.execute(deleteHotel, hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase());
                            }else if(veroMon==true && veroHot == false){
                                db.deleteHotelBB(hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase());
                                Toast.makeText(getApplicationContext(), "Hotel/BB eliminato con successo , Monumento non trovato o non corrispondete alla citta", Toast.LENGTH_SHORT).show();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                                backgroudWorker.execute(deleteHotel, hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase());
                            }else if(veroMon==false && veroHot == true){
                                db.deleteMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                                Toast.makeText(getApplicationContext(), "Monumento eliminato con successo , Hotel/BB non trovato o non corrispondete alla citta", Toast.LENGTH_SHORT).show();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                                backgroudWorker.execute(deleteMonumento, monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                            }else if(veroMon==true && veroHot == true){
                                Toast.makeText(getApplicationContext(), "Monumento , Hotel/BB non trovati o non corrispondete alle citta", Toast.LENGTH_SHORT).show();
                            }
                    }else{
                        Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    mEditCitta.setError("CAMPO OBBLIGATORIO");
                    mEditCitta.requestFocus();
                }
            }if(hotelbb.trim().isEmpty()&&((!monumento.trim().isEmpty()&& !gastronomia.trim().isEmpty()))){
                if(!(citta.trim().isEmpty())) {
                    if (!db.checkCitta(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        boolean veroMon;
                        boolean veroGast;
                        veroMon = db.checkMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                        veroGast = db.checkGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                            if(veroGast==false && veroMon ==false){
                                db.deleteMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                                db.deleteGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase());
                                Toast.makeText(getApplicationContext(), "Punto Gastronomia e Monumento eliminati con successo", Toast.LENGTH_SHORT).show();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                                BackgroudWorker backgroudWorker1 = new BackgroudWorker(this);
                                backgroudWorker.execute(deleteMonumento, monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                                backgroudWorker1.execute(deleteGastronomia, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase());
                            }else if(veroGast==true && veroMon ==false){
                                db.deleteMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                                Toast.makeText(getApplicationContext(), "Monumento eliminato con successo,Punto Gastronomia non trovato o non corrispondente alla citta", Toast.LENGTH_SHORT).show();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                                backgroudWorker.execute(deleteMonumento, monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase());
                            }else if(veroGast==false && veroMon ==true){
                                db.deleteGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase());
                                Toast.makeText(getApplicationContext(), "Punto Gastronomia eliminato con successo,Monumento non trovato o non corrispondente alla citta", Toast.LENGTH_SHORT).show();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                                backgroudWorker.execute(deleteGastronomia, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase());
                            }else if(veroGast==true && veroMon ==true){
                                Toast.makeText(getApplicationContext(), "Punto Gastronomia,Monumento non trovati o non corrispondete alle citta", Toast.LENGTH_SHORT).show();
                            }
                    }else{
                        Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    mEditCitta.setError("CAMPO OBBLIGATORIO");
                    mEditCitta.requestFocus();
                }
            }if(monumento.trim().isEmpty()&&((!hotelbb.trim().isEmpty()&& !gastronomia.trim().isEmpty()))){
                boolean veroHot;
                boolean veroGast;
                if(!(citta.trim().isEmpty())) {
                    if (!db.checkCitta(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        veroHot = db.checkHotel(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                        veroGast = db.checkGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                        if (veroGast == false && veroHot == false) {
                            db.deleteGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            db.deleteHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                            Toast.makeText(getApplicationContext(), "Punto Gastronomia e Hotel/B&B eliminati con successo", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                            BackgroudWorker backgroudWorker1 = new BackgroudWorker(this);
                            backgroudWorker.execute(deleteGastronomia, gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            backgroudWorker1.execute(deleteHotel, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                        } else if (veroGast == true && veroHot == false) {
                            db.deleteHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                            Toast.makeText(getApplicationContext(), "Hotel/BB eliminato con successo , Punto Gastronomia non trovato o non corrispondete alla citta", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                            backgroudWorker.execute(deleteHotel, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                        } else if (veroGast == false && veroHot == true) {
                            db.deleteGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            Toast.makeText(getApplicationContext(), "Hotel/BB eliminato con successo , Punto Gastronomia non trovato o non corrispondete alla citta", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                            backgroudWorker.execute(deleteGastronomia, gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                        } else if (veroGast == true && veroHot == true) {
                            Toast.makeText(getApplicationContext(), "Punto Gastronomia,Hotel/BB non trovati o non corrispondete alle citta", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    mEditCitta.setError("CAMPO OBBLIGATORIO");
                    mEditCitta.requestFocus();
                }
            }if((!monumento.trim().isEmpty() && !hotelbb.trim().isEmpty() && !gastronomia.trim().isEmpty())){
                if(!(citta.trim().isEmpty())) {
                    if(!db.checkCitta(citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        boolean veroHot;
                        boolean veroGast;
                        boolean veroMon;
                        veroHot = db.checkHotel(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                        veroGast = db.checkGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                        veroMon = db.checkMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                        if (veroGast == false && veroHot == false && veroMon == false) {
                            db.deleteGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            db.deleteHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                            db.deleteMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase());
                            Toast.makeText(getApplicationContext(), "Monumento,Punto Gastronomia,Hotel/B&B eliminati con successo", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                            backgroudWorker.execute(deleteGastronomia, gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            BackgroudWorker backgroudWorker1 = new BackgroudWorker(this);
                            backgroudWorker1.execute(deleteHotel, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                            BackgroudWorker backgroudWorker2 = new BackgroudWorker(this);
                            backgroudWorker2.execute(deleteMonumento, monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase());
                        } else if (veroGast == true && veroHot == false && veroMon == false) {
                            db.deleteHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                            db.deleteMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase());
                            Toast.makeText(getApplicationContext(), "Monumento,Hotel/B&B eliminati con successo. Punto Gastronomia non trovato o non corrispondente alla citta", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                            BackgroudWorker backgroudWorker1 = new BackgroudWorker(this);
                            backgroudWorker.execute(deleteHotel, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                            backgroudWorker1.execute(deleteMonumento, monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase());
                        } else if (veroGast == false && veroHot == false && veroMon == true) {
                            db.deleteGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            db.deleteHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                            Toast.makeText(getApplicationContext(), "Punto Gastronomia,Hotel/B&B eliminati con successo. Monumento non trovato o non corrispondente alla citta", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                            BackgroudWorker backgroudWorker1 = new BackgroudWorker(this);
                            backgroudWorker.execute(deleteGastronomia, gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            backgroudWorker1.execute(deleteHotel, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                        } else if (veroGast == false && veroHot == true && veroMon == false) {
                            db.deleteGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            db.deleteMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase());
                            Toast.makeText(getApplicationContext(), "Punto Gastronomia,Monumento eliminati con successo. Hotel/B&B non trovato o non corrispondente alla citta", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                            BackgroudWorker backgroudWorker1 = new BackgroudWorker(this);
                            backgroudWorker.execute(deleteGastronomia, gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            backgroudWorker1.execute(deleteMonumento, monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase());
                        } else if (veroGast == true && veroHot == true && veroMon == true) {
                            Toast.makeText(getApplicationContext(), "Punto Gastronomia,Hotel/BB e Monumento non trovati o non corrispondeti alle citta", Toast.LENGTH_SHORT).show();
                        }else if(veroGast == true && veroHot == true && veroMon == false){
                            db.deleteMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase());
                            Toast.makeText(getApplicationContext(), "Monumento eliminati con successo. Punto Gastronomia e Hotel/B&B non trovato o non corrispondente alla citta", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                            backgroudWorker.execute(deleteMonumento, monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase());
                        }else if(veroGast == true && veroHot == false && veroMon == true){
                            db.deleteHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                            Toast.makeText(getApplicationContext(), "Hotel/BB eliminato con successo ,Monumento e Punto Gastronomia non trovato o non corrispondete alla citta", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                            backgroudWorker.execute(deleteHotel, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                        }else if(veroGast == false && veroHot == true && veroMon == true){
                            db.deleteGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase());
                            Toast.makeText(getApplicationContext(), "Punto Gastronomia eliminato con successo,Monumento e Hotel/BB non trovato o non corrispondente alla citta", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                            backgroudWorker.execute(deleteGastronomia, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase());
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    mEditCitta.setError("CAMPO OBBLIGATORIO");
                    mEditCitta.requestFocus();
                }
            }
        }
    }
}