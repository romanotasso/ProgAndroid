package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterEmailPassActivity extends AppCompatActivity {

    public static final Pattern EMAIL_VALIDATION = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
    private EditText email;
    private EditText password;
    private EditText confPassword;
    private Button avanti;
    TextInputLayout passwordUp;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_emailpassword);

        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_pass);
        confPassword = (EditText) findViewById(R.id.reg_confpass);
        avanti = (Button) findViewById(R.id.button_avanti);
        passwordUp = findViewById(R.id.setPasswordUp);
    }

    public void nextStepRegistation(View view) {


        String str_pass = password.getText().toString();
        String str_confpass = confPassword.getText().toString();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(validateEmail(email)){
            if(!str_pass.isEmpty()){
                passwordUp.setPasswordVisibilityToggleEnabled(true);
                if(str_pass.equals(str_confpass)){
                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("password",str_pass);
                    startActivity(intent);
                }else {
                    confPassword.setError("Password non corrispondenti");
                }
            }else {
                passwordUp.setPasswordVisibilityToggleEnabled(false);
                password.setError("Campo obbligatorio");
            }
        }



    }

   private boolean validateEmail(EditText email){

        String email_str = email.getText().toString();

        if(email_str.isEmpty()){
            email.setError("Campo obbligatorio");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email_str).matches()){
            email.setError("Formato email non valido");
            return false;
        }else {
            email.setError(null);
            return true;
        }
   }



}



