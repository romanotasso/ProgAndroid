package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText mTextNome;
    EditText mTextCognome;
    Button mButtonRegister;
    EditText mTextEmail;
    EditText mTextPassword;
    //EditText mTextDataNascita;
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
        //mTextDataNascita = findViewById(R.id.edittext_data_nascita);
        mTextSesso = findViewById(R.id.edittext_sesso);
        mTextCitta = findViewById(R.id.edittext_citta);
        mButtonRegister = findViewById(R.id.button_register);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // String nome = mTextNome.getText().toString();
                //String cognome = mTextCognome.getText().toString();
                String email = mTextEmail.getText().toString();
                String password = mTextPassword.getText().toString();
                //String data_nascita = mTextDataNascita.getText().toString();
                // String sesso = mTextSesso.getText().toString();
                //String citta = mTextCitta.getText().toString();
                if(email.equals("")||password.equals("")){
                    Toast.makeText(getApplicationContext(),"I campi sono vuoti",Toast.LENGTH_SHORT).show();
                }else{
                    Boolean check = db.checkEmail(email);
                    if(check==true){
                        Boolean inserisci = db.inserisci(email,password);
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

    }
}
