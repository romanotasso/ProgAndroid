package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    EditText mTextNome;
    EditText mTextCognome;
    Button mButtonRegister;
    EditText mTextEmail;
    EditText mTextPassword;
    EditText mTextDataNascita;
    EditText mTextSesso;
    EditText mTextCitta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mTextNome = (EditText) findViewById(R.id.edittext_nome);
        mTextCognome = (EditText) findViewById(R.id.edittext_cognome);
        mTextEmail = (EditText) findViewById(R.id.edittext_email);
        mTextPassword = (EditText) findViewById(R.id.edittext_password1);
        mTextDataNascita = (EditText) findViewById(R.id.edittext_data_nascita);
        mTextSesso = (EditText) findViewById(R.id.edittext_sesso);
        mTextCitta = (EditText) findViewById(R.id.edittext_citta);
        mButtonRegister = (Button) findViewById(R.id.button_register);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

    }
}
