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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

public class CittaFragmentAmministratoreInserisci extends Fragment {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1000;
    EditText mEditCitta;
    Button mButtonInserisci;
    ImageButton mInserisciPhoto;
    DatabaseHelper db;
    ImageView photoCitta;
    Uri filepath;
    String URL_UPLOAD = "http://progandroid.altervista.org/progandorid/uploadPhotoCitta.php";
    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_citta_amministratore_inserisci, container, false);

        db = new DatabaseHelper(getContext());

        mEditCitta = view.findViewById(R.id.edittext_citta);
        mInserisciPhoto = view.findViewById(R.id.button_addPhotoCitta);
        mButtonInserisci = view.findViewById(R.id.button_inserisci);
        photoCitta = view.findViewById(R.id.citta_immagine);

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String type = "inserimento";
                String citta = mEditCitta.getText().toString();
                String hotelbb = "";
                String gastronomia = "";
                String monumento = "";
                String categoria ="";

                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                    mEditCitta.findFocus();
                } else if (!(citta.trim().isEmpty())) {
                    if (db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        if (count == 1) {
                            db.inserisciCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                            Toast.makeText(getContext(), R.string.inserisci_citta, Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                            backgroudWorker.execute(type, citta, monumento, gastronomia, hotelbb,categoria);
                            Bitmap image = ((BitmapDrawable) photoCitta.getDrawable()).getBitmap();
                            new updateImage(image, citta).execute();
                            count=0;
                        } else {
                            mButtonInserisci.setError("");
                            count =0;
                        }
                    } else {
                        mEditCitta.setError("Citta già presente");
                        mEditCitta.findFocus();
                    }
                }
            }

        });


        mInserisciPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] perimissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(perimissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });

        return view;
    }

    private void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getContext(), R.string.permesso_negato, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            filepath = data.getData();
            photoCitta.setImageURI(filepath);
            count = count + 1;
        }
    }

    private class updateImage extends AsyncTask<Void, Void, Void> {

        Bitmap image;
        String citta;

        public updateImage(Bitmap image, String citta) {
            this.image = image;
            this.citta = citta;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            List<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("citta", citta));
            params.add(new Pair<>("image", encodedImage));

            try {

                URL url = new URL(URL_UPLOAD);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("citta", "UTF-8") + "=" + URLEncoder.encode(citta, "UTF-8") + "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(encodedImage, "UTF-8");
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
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }


    }


}