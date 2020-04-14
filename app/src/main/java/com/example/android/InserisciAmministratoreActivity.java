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

       /* mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((mEditCitta.getText().toString().trim().isEmpty()) && (mEditGastronomia.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty()) && (mEditMonumento.getText().toString().trim().isEmpty())){
                    Toast.makeText(getApplicationContext(), "I campi sono vuoti", Toast.LENGTH_SHORT).show();
                } else {
                    //INSERIMENTO MONUMENTO
                    if ((mEditGastronomia.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty())) {
                        if(!(mEditMonumento.getText().toString().trim().isEmpty()) && !(mEditCitta.getText().toString().trim().isEmpty())){
                            boolean checkMonumento = db.checkMonumento(mEditMonumento.getText().toString());
                            boolean checkCitta = db.checkCitta(mEditCitta.getText().toString());
                            if (checkMonumento && !checkCitta) {
                                boolean isInsert = db.inserisciMonumento(mEditMonumento.getText().toString(),mEditCitta.getText().toString());
                                if (isInsert) {
                                    Toast.makeText(InserisciAmministratoreActivity.this, "Monumeto aggiunto", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(InserisciAmministratoreActivity.this, AmministatoreActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(InserisciAmministratoreActivity.this, "Monumento già presente o città non esistente", Toast.LENGTH_SHORT).show();
                            }
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
                    if (((mEditMonumento.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty()))) {
                        if(!(mEditGastronomia.getText().toString().trim().isEmpty()) && !(mEditCitta.getText().toString().trim().isEmpty())) {
                            boolean checkGastronomia = db.checkGastronomia(mEditGastronomia.getText().toString());
                            boolean checkCitta = db.checkCitta(mEditCitta.getText().toString());
                            if (checkGastronomia && !checkCitta) {
                                boolean isInsert = db.inserisciGastronomia(mEditGastronomia.getText().toString(), mEditCitta.getText().toString());
                                if (isInsert) {
                                    Toast.makeText(InserisciAmministratoreActivity.this, "Gastronomia aggiunta", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(InserisciAmministratoreActivity.this, AmministatoreActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(InserisciAmministratoreActivity.this, "Gastronomia già presente o città non esistente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    //INSERIMENTO HOTEL
                    if ((mEditMonumento.getText().toString().trim().isEmpty()) && (mEditGastronomia.getText().toString().trim().isEmpty())) {
                        if(!(mEditHotel.getText().toString().trim().isEmpty()) && !(mEditCitta.getText().toString().trim().isEmpty())) {
                            boolean checkHotel = db.checkHotelBB(mEditHotel.getText().toString());
                            boolean checkCitta = db.checkCitta(mEditCitta.getText().toString());
                            if (checkHotel && !checkCitta) {
                                boolean isInsert = db.inserisciHotelBB(mEditHotel.getText().toString(), mEditCitta.getText().toString());
                                if (isInsert) {
                                    Toast.makeText(InserisciAmministratoreActivity.this, "Hotel/BB aggiunto", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(InserisciAmministratoreActivity.this, AmministatoreActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(InserisciAmministratoreActivity.this, "Hotel/BB già presente o città non esistente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });*/
    }
    public void onUpdate (View view){

        String email = mEditEmail.getText().toString();
        String password = mEditPassword.getText().toString();
        String type = "UpdateUtente";

        if(email.trim().isEmpty() || password.trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Inserisci i dati per l'aggiornamento", Toast.LENGTH_SHORT).show();
        } else {
            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
            backgroudWorker.execute(type, email, password);
        }
    }


    public void onInsert (View view) {
        String citta = mEditCitta.getText().toString();
        String monumento = mEditMonumento.getText().toString();
        String gastronomia = mEditGastronomia.getText().toString();
        String hotelbb = mEditHotel.getText().toString();
        String type = "inserimento";

        if ((citta.trim().isEmpty()) && ((monumento.trim().isEmpty() && gastronomia.trim().isEmpty() && hotelbb.trim().isEmpty()))) {
            Toast.makeText(getApplicationContext(), "Inserisci i dati", Toast.LENGTH_SHORT).show();
        } else if ((monumento.trim().isEmpty() && gastronomia.trim().isEmpty() && hotelbb.trim().isEmpty())) {
            Toast.makeText(getApplicationContext(), "Inserisci i dati", Toast.LENGTH_SHORT).show();
        } else if (!citta.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"Città inserita con successo", Toast.LENGTH_SHORT).show();
            hotelbb = "";
            gastronomia = "";
            monumento = "";
            db.inserisciCitta(citta);
            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
            backgroudWorker.execute(type, citta, monumento, gastronomia, hotelbb);
        }  else if((hotelbb.trim().isEmpty() && gastronomia.trim().isEmpty())) {
            Toast.makeText(getApplicationContext(),"Monumento inserito con successo", Toast.LENGTH_SHORT).show();
            hotelbb = "";
            gastronomia = "";
            db.inserisciMonumento(monumento,citta);
            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
            backgroudWorker.execute(type, citta, monumento, gastronomia, hotelbb);
        } else if((monumento.trim().isEmpty() && hotelbb.trim().isEmpty())) {
            Toast.makeText(getApplicationContext(),"Gastronomia inserita con successo", Toast.LENGTH_SHORT).show();
            monumento = "";
            hotelbb = "";
            db.inserisciGastronomia(gastronomia,citta);
            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
            backgroudWorker.execute(type, citta, monumento, gastronomia, hotelbb);
        } else if((monumento.trim().isEmpty() && gastronomia.trim().isEmpty())) {
            Toast.makeText(getApplicationContext(), "Hotel/BB inserito con successo", Toast.LENGTH_SHORT).show();
            monumento = "";
            gastronomia = "";
            db.inserisciHotelBB(hotelbb, citta);
            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
            backgroudWorker.execute(type, citta, monumento, gastronomia, hotelbb);
        }
    }
}
