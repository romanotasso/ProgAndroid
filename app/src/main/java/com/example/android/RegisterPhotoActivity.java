package com.example.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RegisterPhotoActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1000;
    ImageView immagineProfilo;
    ImageButton addPhoto;
    Button skipPhoto;
    DatabaseHelper db;
    Bitmap bitmap;
    Uri filepath;
    String URL_UPLOAD = "http://progandroid.altervista.org/progandorid/uploadPhoto.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_photo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        immagineProfilo = findViewById(R.id.profilo_immagine);
        addPhoto = findViewById(R.id.button_addPhoto);
        skipPhoto = findViewById(R.id.button_skipPhoto);
        db = new DatabaseHelper(this);

    }

    public void skipPhoto(View view) {
        ArrayList<String> coupon = new ArrayList<String>();
        coupon.add("MILANO");
        coupon.add("ALTAMURA");
        coupon.add("BARI");
        coupon.add("ROMA");
        final Random random = new Random();
        String str_nome = getIntent().getExtras().getString("nome");
        String str_cognome = getIntent().getExtras().getString("cognome");
        String str_email = getIntent().getExtras().getString("email");
        String str_pass = getIntent().getExtras().getString("password");
        String str_citta = getIntent().getExtras().getString("citta");
        String str_sesso = getIntent().getExtras().getString("sesso");
        String str_data = getIntent().getExtras().getString("data_nascita");
        String couponUtente = coupon.get(random.nextInt(4));
        String type = "register";

        if (filepath == null) {
            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
            backgroudWorker.execute(type
                    , str_nome = str_nome.substring(0, 1).toUpperCase() + str_nome.substring(1).toLowerCase()
                    , str_cognome = str_cognome.substring(0, 1).toUpperCase() + str_cognome.substring(1).toLowerCase()
                    , str_email
                    , str_pass
                    , str_citta = str_citta.substring(0, 1).toUpperCase() + str_citta.substring(1).toLowerCase()
                    , str_sesso,
                    str_data,
                    couponUtente,"1");
            db.inserisciUtente(str_email,
                    str_nome = str_nome.substring(0, 1).toUpperCase() + str_nome.substring(1).toLowerCase()
                    , str_cognome = str_cognome.substring(0, 1).toUpperCase() + str_cognome.substring(1).toLowerCase()
                    , str_citta = str_citta.substring(0, 1).toUpperCase() + str_citta.substring(1).toLowerCase()
                    , str_sesso,
                    str_data,
                    couponUtente,
                    "1");

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            Toast.makeText(getApplicationContext(), R.string.registrazione_successo, Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } else {

            Bitmap image = ((BitmapDrawable) immagineProfilo.getDrawable()).getBitmap();
            new updateImage(image, str_email).execute();

            BackgroudWorker backgroudWorker = new BackgroudWorker(this);
            backgroudWorker.execute(type
                    , str_nome = str_nome.substring(0, 1).toUpperCase() + str_nome.substring(1).toLowerCase()
                    , str_cognome = str_cognome.substring(0, 1).toUpperCase() + str_cognome.substring(1).toLowerCase()
                    , str_email
                    , str_pass
                    , str_citta = str_citta.substring(0, 1).toUpperCase() + str_citta.substring(1).toLowerCase()
                    , str_sesso,
                    str_data,
                    couponUtente,"0");
            db.inserisciUtente(str_email,
                    str_nome = str_nome.substring(0, 1).toUpperCase() + str_nome.substring(1).toLowerCase()
                    , str_cognome = str_cognome.substring(0, 1).toUpperCase() + str_cognome.substring(1).toLowerCase()
                    , str_citta = str_citta.substring(0, 1).toUpperCase() + str_citta.substring(1).toLowerCase()
                    , str_sesso,
                    str_data, couponUtente,"0");

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            Toast.makeText(getApplicationContext(), R.string.registrazione_successo, Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }

    public void addPhoto(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] perimissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(perimissions, PERMISSION_CODE);
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, R.string.permesso_negato, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            filepath = data.getData();
            immagineProfilo.setImageURI(filepath);
        }
    }

    class updateImage extends AsyncTask<Void, Void, Void> {

        Bitmap image;
        String email;

        public updateImage(Bitmap image, String email) {
            this.image = image;
            this.email = email;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            List<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("email", email));
            params.add(new Pair<>("image", encodedImage));

            String emailCor = email.replaceAll("@","");

            try {

                URL url = new URL(URL_UPLOAD);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(emailCor, "UTF-8") + "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(encodedImage, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}

