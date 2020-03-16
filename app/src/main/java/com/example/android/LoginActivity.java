package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login";
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
        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mTextEmail.getText().toString();
                String password = mTextPassword.getText().toString();
                Boolean controlloMailPassword = db.checkEmailPassword(email,password);
                if((email.equals(USERNAME)) && (password.equals(PASSWORD))){
                    Intent amministratoreIntent = new Intent(LoginActivity.this, AmministatoreActivity.class);
                    startActivity(amministratoreIntent);
                }else if(controlloMailPassword == true){
                    Intent registerIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(registerIntent);
                }else{
                    Toast.makeText(getApplicationContext(),"Email o password sbagliate",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
