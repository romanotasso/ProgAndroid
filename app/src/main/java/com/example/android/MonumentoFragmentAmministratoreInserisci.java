package com.example.android;

import android.Manifest;
import android.annotation.SuppressLint;
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

public class MonumentoFragmentAmministratoreInserisci extends Fragment  {

    private  static final int IMAGE_PICK_CODE = 1000;
    private  static final int PERMISSION_CODE = 1000;
    DatabaseHelper db;
    Button mButtonInserisci;
    EditText mEditCitta;
    EditText mEditMonumento;
    TextView erroreCategoria;
    TextView erroreFoto;
    Spinner mSpinnerCategorie;
    ImageButton mInserisciPhoto;
    ImageView photoMonumento;
    Uri filepath;
    String URL_UPLOAD ="http://progandroid.altervista.org/progandorid/uploadPhotoMonumento.php";
    int count =0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monumento_amministratore_inserisci, container, false);

        db = new DatabaseHelper(getContext());

        mSpinnerCategorie = view.findViewById(R.id.spinnerMonumento);


        List<String> categorie = new ArrayList<>();
        categorie.add(0,"Scegli Categoria");
        categorie.add(1,"Chiesa");
        categorie.add(2,"Escursione");
        categorie.add(3,"Lido Balneare");
        categorie.add(4,"Monumento");
        categorie.add(5,"Museo");

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
        erroreFoto= view.findViewById(R.id.errorFoto);
        erroreCategoria = view.findViewById(R.id.errorCategoria);
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
                String categoria = mSpinnerCategorie.getSelectedItem().toString();


                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                } else if (!monumento.trim().isEmpty()) {
                    if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        if (db.checkMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            if(count==1){
                                if(!categoria.equals("Scegli Categoria")){
                                    gastronomia = "";
                                    hotelbb = "";
                                    //db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(),categoria = categoria.substring(0, 1).toUpperCase() + categoria.substring(1).toLowerCase(),citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                                    BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                                    backgroudWorker.execute(type,citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia , hotelbb,categoria = categoria.substring(0, 1).toUpperCase() + categoria.substring(1).toLowerCase());
                                    Toast.makeText(getContext(), R.string.inserisci_monumento, Toast.LENGTH_SHORT).show();
                                    Bitmap image = ((BitmapDrawable)photoMonumento.getDrawable()).getBitmap();
                                    new updateImage(image,citta,monumento).execute();
                                    count=0;
                                }else {
                                    erroreCategoria.setError("Campo obbligatorio");
                                    erroreCategoria.setVisibility(View.VISIBLE);
                                }
                            }else{
                                erroreFoto.setError("Inserire foto");
                                erroreFoto.setVisibility(View.VISIBLE);
                                count=0;
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
                    }
                }else {
                    pickImageFromGallery();

                }

            }
        });

        return view;
    }

    private void pickImageFromGallery(){
        erroreFoto.setError(null);
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
            count = count+1;
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