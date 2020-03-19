package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

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
    Button mButtonCancella;
    EditText mEditCitta;
    EditText mEditHotel;
    EditText mEditMonumento;
    EditText mEditGastronomia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancella_amministratore);
        mEditCitta = findViewById(R.id.edittext_citta);
        mEditGastronomia = findViewById(R.id.edittext_gastronomia);
        mEditHotel = findViewById(R.id.edittext_hotel);
        mEditMonumento = findViewById(R.id.edittext_monumenti);
        mButtonCancella = findViewById(R.id.button_cancella);
        db = new DatabaseHelper(this);

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((mEditCitta.getText().toString().trim().isEmpty()) && (mEditGastronomia.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty()) && (mEditMonumento.getText().toString().trim().isEmpty())){
                    Toast.makeText(getApplicationContext(), "I campi sono vuoti", Toast.LENGTH_SHORT).show();
                } else {

                    //CANCELLAZIONE MONUMENTO
                    if((mEditCitta.getText().toString().trim().isEmpty()) && (mEditGastronomia.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty())){
                        Integer deleteMonumento = db.deleteMonumento(mEditMonumento.getText().toString());
                        if(deleteMonumento > 0){
                            Toast.makeText(getApplicationContext(), "Monumento cancellato", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CancellaAmministratoreActivity.this, AmministatoreActivity.class);
                            startActivity(intent);
                        }
                    }

                    //CANCELLAZIONE CITTA'
                    if((mEditMonumento.getText().toString().trim().isEmpty()) && (mEditGastronomia.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty())){
                        Integer deleteCittà = db.deleteCitta(mEditCitta.getText().toString());
                        if(deleteCittà > 0){
                            Toast.makeText(getApplicationContext(), "Città cancellata", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CancellaAmministratoreActivity.this, AmministatoreActivity.class);
                            startActivity(intent);
                        }
                    }

                    //CANCELLAZIONE GASTRONOMIA
                    if((mEditCitta.getText().toString().trim().isEmpty()) && (mEditMonumento.getText().toString().trim().isEmpty()) && (mEditHotel.getText().toString().trim().isEmpty())){
                        Integer deleteGastronomia = db.deleteGastronomia(mEditGastronomia.getText().toString());
                        if(deleteGastronomia > 0){
                            Toast.makeText(getApplicationContext(), "Gastronomia cancellata", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CancellaAmministratoreActivity.this, AmministatoreActivity.class);
                            startActivity(intent);
                        }
                    }

                    //CANCELLAZIONE HOTELEBB
                    if((mEditCitta.getText().toString().trim().isEmpty()) && (mEditGastronomia.getText().toString().trim().isEmpty()) && (mEditMonumento.getText().toString().trim().isEmpty())){
                        Integer deleteHoteleBB = db.deleteHotelBB(mEditHotel.getText().toString());
                        if(deleteHoteleBB > 0){
                            Toast.makeText(getApplicationContext(), "Hotel/BB cancellato", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CancellaAmministratoreActivity.this, AmministatoreActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
}
