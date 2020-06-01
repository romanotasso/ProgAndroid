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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

public class HotelBBFragmentAmministratoreInserisci extends Fragment {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1000;
    DatabaseHelper db;
    Spinner mSpinnerCategorie;
    Button mButtonInserisci;
    EditText mEditCitta;
    EditText mEditHotel;
    TextView erroreCategoria;
    TextView erroreFoto;
    ImageButton mInserisciPhoto;
    ImageView photoHotel;
    Uri filepath;
    String URL_UPLOAD = "http://progandroid.altervista.org/progandorid/updatePhotoHotel.php";
    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hotel_b_b_amministratore_inserisci, container, false);

        db = new DatabaseHelper(getContext());

        mSpinnerCategorie = view.findViewById(R.id.spinnerHotel);

        List<String> categorie = new ArrayList<>();
        categorie.add(0,"Scegli Categoria");
        categorie.add(1,"Hotel");
        categorie.add(2,"Bed and Breakfast");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item,categorie);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinnerCategorie.setAdapter(adapterSpinner);
        mSpinnerCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).equals("Scegli Categoria")){

                }else {
                    String item = parent.getItemAtPosition(position).toString();
                    erroreCategoria.setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        mEditCitta = view.findViewById(R.id.edittext_citta);
        mEditHotel = view.findViewById(R.id.edittext_hotel);
        mButtonInserisci = view.findViewById(R.id.button_inserisci);
        mInserisciPhoto = view.findViewById(R.id.button_addPhotoHotel);
        photoHotel = view.findViewById(R.id.hotel_immagine);
        erroreFoto= view.findViewById(R.id.errorFoto);
        erroreCategoria = view.findViewById(R.id.errorCategoria);

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = "inserimento";
                String citta = mEditCitta.getText().toString();
                String monumento = "";
                String gastronomia = "";
                String hotelbb = mEditHotel.getText().toString();
                String categoria = mSpinnerCategorie.getSelectedItem().toString();

                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                } else if (!hotelbb.trim().isEmpty()) {
                    if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        if (db.checkHotel(hotelbb = hotelbb.substring(0,1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            if(count==1){
                                if(!categoria.equals("Scegli Categoria")) {
                                    gastronomia = "";
                                    monumento = "";
                                    //db.inserisciHotelBB(hotelbb = hotelbb.substring(0,1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                                    BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                                    backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), categoria = categoria.substring(0, 1).toUpperCase() + categoria.substring(1).toLowerCase());
                                    Toast.makeText(getContext(), R.string.inserisci_hotel, Toast.LENGTH_SHORT).show();
                                    Bitmap image = ((BitmapDrawable) photoHotel.getDrawable()).getBitmap();
                                    new updateImage(image, citta, hotelbb).execute();
                                    count=0;
                                }else {
                                    erroreCategoria.setError("Campo obbligatorio");
                                    erroreCategoria.setVisibility(View.VISIBLE);
                                }
                            }else {
                                erroreFoto.setError("Inserire foto");
                                erroreFoto.setVisibility(View.VISIBLE);
                                count=0;
                            }
                        } else {
                            mEditHotel.setError("Hotel/B&B gia presente");
                        }
                    } else {
                        mEditCitta.setError("Citta non presente");
                    }
                } else {
                    mEditHotel.setError("Campo obbligatorio");

                }
            }
        });

        mInserisciPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] perimissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(perimissions,PERMISSION_CODE);
                    }else{
                        pickImageFromGallery();
                    }
                }else {
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getContext(), "Permesso negato", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            filepath = data.getData();
            photoHotel.setImageURI(filepath);
            count = count+1;
        }
    }

    private class updateImage extends AsyncTask<Void, Void, Void> {

        Bitmap image;
        String citta;
        String hotel;

        public updateImage(Bitmap image, String citta, String hotel) {

            this.image = image;
            this.citta = citta;
            this.hotel = hotel;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            String cittaHotel = citta + hotel;
            List<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("cittaHotel", cittaHotel));
            params.add(new Pair<>("image", encodedImage));

            try {

                URL url = new URL(URL_UPLOAD);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("cittaHotel", "UTF-8") + "=" + URLEncoder.encode(cittaHotel, "UTF-8") + "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(encodedImage, "UTF-8");
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