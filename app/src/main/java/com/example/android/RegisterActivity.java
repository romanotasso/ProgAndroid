package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Registrazione";
    DatePickerDialog.OnDateSetListener mDataSetListner;
    EditText mTextNome;
    EditText mTextCognome;
    Button mButtonRegister;
    EditText mTextEmail;
    EditText mTextDataNascita;
    EditText mTextPassword;
    EditText mTextSesso;
    EditText mTextCitta;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        mTextNome = findViewById(R.id.edittext_nome);
        mTextCognome = findViewById(R.id.edittext_cognome);
        mTextEmail = findViewById(R.id.edittext_email);
        mTextPassword = findViewById(R.id.edittext_password1);
        mTextDataNascita = findViewById(R.id.edittext_data);
        mTextSesso = findViewById(R.id.edittext_sesso);
        mTextCitta = findViewById(R.id.edittext_citta);
        mButtonRegister = findViewById(R.id.button_register);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = mTextNome.getText().toString();
                String cognome = mTextCognome.getText().toString();
                String email = mTextEmail.getText().toString();
                String password = mTextPassword.getText().toString();
                //String data_nascita = mTextDataNascita.getText().toString();
                String sesso = mTextSesso.getText().toString();
                String citta = mTextCitta.getText().toString();
                if(email.equals("")||password.equals("")||nome.equals("")||cognome.equals("")||sesso.equals("")||citta.equals("")){
                    Toast.makeText(getApplicationContext(),"I campi sono vuoti",Toast.LENGTH_SHORT).show();
                }else{
                    Boolean check = db.checkEmail(email);
                    if(check==true){
                        Boolean inserisci = db.inserisci(email,password,nome,cognome,citta,sesso);
                        if(inserisci == true){
                            Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(registerIntent);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Email già esistente", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        mTextDataNascita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int anno = cal.get(Calendar.YEAR);
                int mese = cal.get(Calendar.MONTH);
                int giorno = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDataSetListner, anno, mese, giorno);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDataSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int giorno, int mese, int anno) {
                Log.d(TAG,"onDataSet: dd/mm//aaaa: " + giorno + "/" + mese + "/" + anno);
                mese = mese + 1;
                String date = giorno + "/" + mese + "/" + anno;
                mTextDataNascita.setText(date);
            }
        };

    }
}
