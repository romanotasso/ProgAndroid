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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Registrazione";
    DatePickerDialog.OnDateSetListener mDataSetListner;
    EditText mTextNome;
    EditText mTextCognome;
    Button mButtonRegister;
    EditText mTextEmail;
    EditText mTextDataNascita;
    EditText mTextPassword;
    EditText mTextCitta;
    DatabaseHelper db;
    RadioGroup radioGroup;
    RadioButton radioButtonSesso;


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
        radioGroup = findViewById(R.id.radio);
        mTextCitta = findViewById(R.id.edittext_citta);
        mButtonRegister = findViewById(R.id.button_register);

       /* mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = mTextNome.getText().toString();
                String cognome = mTextCognome.getText().toString();
                String email = mTextEmail.getText().toString();
                String password = mTextPassword.getText().toString();
                String data_nascita = mTextDataNascita.getText().toString();
                String sesso = mTextSesso.getText().toString();
                String citta = mTextCitta.getText().toString();
                if(email.trim().isEmpty()||password.trim().isEmpty()||nome.trim().isEmpty()||cognome.trim().isEmpty()||sesso.trim().isEmpty()||citta.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),"I campi sono vuoti",Toast.LENGTH_SHORT).show();
                }else{
                    Boolean check = db.checkEmail(email);
                    if(check==true){
                        Boolean inserisci = db.inserisciUtente(email,password,nome,cognome,citta,sesso,data_nascita);
                        if(inserisci == true){
                            Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(registerIntent);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Email già esistente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/

        mTextDataNascita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int anno = cal.get(Calendar.YEAR);
                int mese = cal.get(Calendar.MONTH);
                int giorno = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDataSetListner, anno, mese, giorno);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDataSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int giorno, int mese, int anno) {
                Log.d(TAG,"onDataSet:dd-MM-yyyy:" + anno + "/" + mese + "/" + giorno);
                mese = mese + 1;
                String date = anno + "/" + mese + "/" + giorno;
                mTextDataNascita.setText(date);
                Toast.makeText(getApplicationContext(),""+ date +"",Toast.LENGTH_SHORT).show();
            }
        };
    }

    public String checkButton (View v) {
        int radioID = radioGroup.getCheckedRadioButtonId();
        radioButtonSesso = findViewById(radioID);
        return radioButtonSesso.getText().toString();
    }

    public void onReg(View view){

        String str_nome = mTextNome.getText().toString();
        String str_cognome= mTextCognome.getText().toString();
        String str_email = mTextEmail.getText().toString();
        String str_pass = mTextPassword.getText().toString();
        String str_citta = mTextCitta.getText().toString();
        int radioID = radioGroup.getCheckedRadioButtonId();
        radioButtonSesso = findViewById(radioID);
        String str_sesso = radioButtonSesso.getText().toString();
        String str_data = mTextDataNascita.getText().toString();
        String type = "register";

        BackgroudWorker backgroudWorker= new BackgroudWorker(this);
        backgroudWorker.execute(type,str_nome,str_cognome,str_email,str_pass,str_citta,str_sesso,str_data);


    }
}
