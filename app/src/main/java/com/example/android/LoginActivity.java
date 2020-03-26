package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private static final String PASSWORD = "admin";
    private static final String USERNAME = "admin";
    EditText mTextEmail;
    EditText mTextPassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    DatabaseHelper db;
    ArrayList<String> città;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        mTextEmail = findViewById(R.id.edittext_email_login);
        mTextPassword = findViewById(R.id.edittext_password);
        mButtonLogin = findViewById(R.id.button_login);
        mTextViewRegister = findViewById(R.id.textview_register);
        città = new ArrayList<String>();
        città.add("Milano");
        città.add("Bologna");
        città.add("Roma");

        for (int i = 0; i < città.size(); i++) {
            if (db.checkCitta(città.get(i))) {
                db.inserisciCitta(città.get(i));
            } else if (!db.checkCitta(città.get(i))) {
                break;
            }
        }

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        /*mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                String email = mTextEmail.getText().toString();
                String password = mTextPassword.getText().toString();
                Boolean controlloMailPassword = db.checkEmailPassword(email,password);
                if((email.equals(USERNAME)) && (password.equals(PASSWORD))){
                    Intent amministratoreIntent = new Intent(LoginActivity.this, AmministatoreActivity.class);
                    startActivity(amministratoreIntent);
                    finish();
                }else if(controlloMailPassword == true){
                    Intent registerIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(registerIntent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Email o password sbagliate",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }*/

    }


    public void OnLogin(View view){
        String email = mTextEmail.getText().toString();
        String password= mTextPassword.getText().toString();
        String type = "login";

        BackgroudWorker backgroudWorker= new BackgroudWorker(this);
        backgroudWorker.execute(type,email,password);


    }
}