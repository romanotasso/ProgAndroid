package com.example.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;




public class RegisterPhotoActivity extends AppCompatActivity {

    private  static final int IMAGE_PICK_CODE = 1000;
    private  static final int PERMISSION_CODE = 1000;
    ImageView immagineProfilo;
    Button addPhoto;
    Button skipPhoto;
    DatabaseHelper db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_photo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    immagineProfilo = findViewById(R.id.profilo_immagine);
    addPhoto = findViewById(R.id.button_addPhoto);
    skipPhoto = findViewById(R.id.button_skipPhoto);
    db = new DatabaseHelper(this);

    }


    public void skipPhoto(View view){

        String str_nome = getIntent().getExtras().getString("nome");
        String str_cognome = getIntent().getExtras().getString("cognome");
        String str_email = getIntent().getExtras().getString("email");
        String str_pass = getIntent().getExtras().getString("password");
        String str_citta = getIntent().getExtras().getString("citta");
        String str_sesso = getIntent().getExtras().getString("sesso");
        String str_data = getIntent().getExtras().getString("data_nascita");
        String type = "register";

        BackgroudWorker backgroudWorker = new BackgroudWorker(this);
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

        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        Toast.makeText(getApplicationContext(),"Registrazione avvenuta con successo",Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }

    public void addPhoto(View view){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String[] perimissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(perimissions,PERMISSION_CODE);
            }else{
                pickImageFromGallery();
            }
        }else {
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]==
                PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }else {
                    Toast.makeText(this,"Permesso negato",Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            immagineProfilo.setImageURI(data.getData());
        }
    }
}

