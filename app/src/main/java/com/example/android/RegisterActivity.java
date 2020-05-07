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
    EditText mTextDataNascita;
    EditText mTextCitta;
    //DatabaseHelper db;
    RadioGroup radioGroup;
    RadioButton radioButtonSesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //db = new DatabaseHelper(this);
        mTextNome = findViewById(R.id.edittext_nome);
        mTextCognome = findViewById(R.id.edittext_cognome);
        mTextDataNascita = findViewById(R.id.edittext_data);
        radioGroup = findViewById(R.id.radio);
        mTextCitta = findViewById(R.id.edittext_citta);
        mButtonRegister = findViewById(R.id.button_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextDataNascita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int anno = cal.get(Calendar.YEAR);
                int mese = cal.get(Calendar.MONTH);
                int giorno = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDataSetListner, anno, mese, giorno);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDataSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int giorno, int mese, int anno) {
                Log.d(TAG, "onDataSet:dd-MM-yyyy:" + anno + "/" + mese + "/" + giorno);
                mese = mese + 1;
                String date = anno + "/" + mese + "/" + giorno;
                mTextDataNascita.setText(date);
            }
        };
    }

    public String checkButton(View v) {
        int radioID = radioGroup.getCheckedRadioButtonId();
        radioButtonSesso = findViewById(radioID);
        return radioButtonSesso.getText().toString();
    }

    public void onReg(View view) {

        String str_nome = mTextNome.getText().toString();
        String str_cognome = mTextCognome.getText().toString();
        String str_email = getIntent().getExtras().getString("email");
        String str_pass = getIntent().getExtras().getString("password");
        String str_citta = mTextCitta.getText().toString();
        int radioID = radioGroup.getCheckedRadioButtonId();
        radioButtonSesso = findViewById(radioID);
        String str_sesso = radioButtonSesso.getText().toString();
        String str_data = mTextDataNascita.getText().toString();
        //String type = "register";

        if (str_nome.trim().isEmpty() || str_cognome.trim().isEmpty() || str_citta.trim().isEmpty() || str_email.trim().isEmpty() || str_pass.trim().isEmpty() || str_sesso.trim().isEmpty() || str_data.trim().isEmpty()) {
            if (str_nome.trim().isEmpty()) {
                mTextNome.setError("CAMPO OBBLIGATORIO");
            }
            if (str_cognome.trim().isEmpty()) {
                mTextCognome.setError("CAMPO OBBLIGATORIO");
            }
            if (str_citta.trim().isEmpty()) {
                mTextCitta.setError("CAMPO OBBLIGATORIO");
            }
        } else {
                /*BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                backgroudWorker.execute(type
                        , str_nome = str_nome.substring(0, 1).toUpperCase() + str_nome.substring(1).toLowerCase()
                        , str_cognome = str_cognome.substring(0, 1).toUpperCase() + str_cognome.substring(1).toLowerCase()
                        , str_email
                        , str_pass
                        , str_citta = str_citta.substring(0, 1).toUpperCase() + str_citta.substring(1).toLowerCase()
                        , str_sesso,
                        str_data);
                db.inserisciUtente(str_email,
                        str_nome = str_nome.substring(0, 1).toUpperCase() + str_nome.substring(1).toLowerCase()
                        , str_cognome = str_cognome.substring(0, 1).toUpperCase() + str_cognome.substring(1).toLowerCase()
                        , str_citta = str_citta.substring(0, 1).toUpperCase() + str_citta.substring(1).toLowerCase()
                        , str_sesso,
                        str_data);
*/
            Intent intent = new Intent(getApplicationContext(),RegisterPhotoActivity.class);
            intent.putExtra("nome",str_nome);
            intent.putExtra("cognome",str_cognome);
            intent.putExtra("email",str_email);
            intent.putExtra("password",str_pass);
            intent.putExtra("sesso",str_sesso);
            intent.putExtra("data_nascita",str_data);
            intent.putExtra("citta",str_citta);
            startActivity(intent);
        }
    }

}