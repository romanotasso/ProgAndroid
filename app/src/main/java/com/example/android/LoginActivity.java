package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private static final String PASSWORD = "admin";
    private static final String USERNAME = "admin";
    EditText mTextEmail;
    EditText mTextPassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        mTextEmail = findViewById(R.id.edittext_email_login);
        mTextPassword = findViewById(R.id.edittext_password);
        mButtonLogin = findViewById(R.id.button_login);
        mTextViewRegister = findViewById(R.id.textview_register);

        BackgroudWorker backgroudWorkerUtente = new BackgroudWorker(this);
        String typeUtente = "aggiornamentoDatiUtente";
        backgroudWorkerUtente.execute(typeUtente);

        BackgroudWorker backgroudWorkerCitta = new BackgroudWorker(this);
        String typeCitta = "aggiornamentoDatiCitta";
        backgroudWorkerCitta.execute(typeCitta);

        BackgroudWorker backgroudWorkerHotel = new BackgroudWorker(this);
        String typeHotel = "aggiornamentoDatiHotel";
        backgroudWorkerHotel.execute(typeHotel);

        BackgroudWorker backgroudWorkerGastronomia = new BackgroudWorker(this);
        String typeGastronomia = "aggiornamentoDatiGastronomia";
        backgroudWorkerGastronomia.execute(typeGastronomia);

        BackgroudWorker backgroudWorkerMonumento = new BackgroudWorker(this);
        String typeMonumenti = "aggiornamentoDatiMonumenti";
        backgroudWorkerMonumento.execute(typeMonumenti);

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterEmailPassActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    public void onLogin(View view){

        String email = mTextEmail.getText().toString();
        String password= mTextPassword.getText().toString();
        String type = "login";

        if(email.trim().isEmpty()||password.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"Compilare tutti i campi", Toast.LENGTH_SHORT).show();
        }else {
            if((email.equals(USERNAME)) && (password.equals(PASSWORD))){
                Intent amministratoreIntent = new Intent(LoginActivity.this, AmministratoreActivity.class);
                startActivity(amministratoreIntent);
                finish();
            }else{
                BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                backgroudWorker.execute(type,email,password);
            }
        }
    }
}