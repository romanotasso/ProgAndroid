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

public class MonumentoFragmentAmministratoreInserisci extends Fragment {

    private  static final int IMAGE_PICK_CODE = 1000;
    private  static final int PERMISSION_CODE = 1000;
    DatabaseHelper db;
    Button mButtonInserisci;
    EditText mEditCitta;
    EditText mEditMonumento;
    ImageButton mInserisciPhoto;
    ImageView photoMonumento;
    Uri filepath;
    String URL_UPLOAD ="http://progandroid.altervista.org/progandorid/uploadPhotoMonumento.php";
    int count =0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monumento_amministratore_inserisci, container, false);

        db = new DatabaseHelper(getContext());

        mEditCitta = view.findViewById(R.id.edittext_citta);
        mEditMonumento = view.findViewById(R.id.edittext_monumenti);
        mButtonInserisci = view.findViewById(R.id.button_inserisci);
        mInserisciPhoto= view.findViewById(R.id.button_addPhotoMonumento);
        photoMonumento = view.findViewById(R.id.monumento_immagine);
        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = "inserimento";
                String citta = mEditCitta.getText().toString();
                String hotelbb = "";
                String gastronomia = "";
                String monumento = mEditMonumento.getText().toString();

                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                } else if (!monumento.trim().isEmpty()) {
                    if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        if (db.checkMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            if(count==1){
                                gastronomia = "";
                                hotelbb = "";
                                db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                                BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                                backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia , hotelbb);
                                Toast.makeText(getContext(), R.string.inserisci_monumento, Toast.LENGTH_SHORT).show();
                                Bitmap image = ((BitmapDrawable)photoMonumento.getDrawable()).getBitmap();
                                new updateImage(image,citta,monumento).execute();
                            }else{
                                mButtonInserisci.setError("");
                            }
                        } else {
                            mEditMonumento.setError("Monumento gia presente");
                        }
                    } else {
                        mEditCitta.setError("Citta non presente");
                    }
                }else {
                    mEditMonumento.setError("Campo obbligatorio");
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
                        count = count+1;
                    }
                }else {
                    pickImageFromGallery();
                    count = count+1;
                }

            }
        });

        return view;
    }

    private void pickImageFromGallery(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }else {
                    Toast.makeText(getContext(),"Permesso negato",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == IMAGE_PICK_CODE && data!= null) {
            filepath = data.getData();
            photoMonumento.setImageURI(filepath);

        }
    }

    private class updateImage extends AsyncTask<Void, Void, Void> {

        Bitmap image;
        String citta;
        String monumento;

        public updateImage(Bitmap image, String citta,String monumento){

            this.image = image;
            this.citta = citta;
            this.monumento=monumento;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
            String cittaMonumento =citta+monumento;
            List<Pair<String,String>> params = new ArrayList<>();
            params.add(new Pair<>("cittaMonumento", cittaMonumento));
            params.add(new Pair<>("image", encodedImage));

            try {
                URL url = new URL(URL_UPLOAD);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("cittaMonumento","UTF-8")+"="+URLEncoder.encode(cittaMonumento,"UTF-8")+"&"+ URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(encodedImage,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine()) != null){
                    result  += line;
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