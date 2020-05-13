package com.example.android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GastronomiaFragmentAmministatoreCancella extends Fragment {

    String urlCancellaImageGastronomia = "http://progandroid.altervista.org/progandorid/deletePhotoGastronomia.php";
    DatabaseHelper db;
    Button mButtonCancella;
    EditText mEditCitta;
    EditText mEditGastronomia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gastronomia_amministratore_cancella, container, false);

        mEditGastronomia = view.findViewById(R.id.edittext_gastronomia);
        mButtonCancella = view.findViewById(R.id.button_cancella);
        mEditCitta = view.findViewById(R.id.edittext_citta);
        db = new DatabaseHelper(getContext());

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String citta = mEditCitta.getText().toString();
                String gastronomia = mEditGastronomia.getText().toString();
                String deleteGastronomia = "cancellaGastronomia";


                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                }else if ((!(gastronomia.trim().isEmpty()))) {
                        if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            if ((!db.checkGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase()))) {
                                db.deleteGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                                Toast.makeText(getContext(), "Punto Gastronomia eliminato con successo", Toast.LENGTH_SHORT).show();
                                String path = (citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())+(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase())+ "JPG";
                                deleteImageGastronomia deleteImageGastronomia = new deleteImageGastronomia(path);
                                deleteImageGastronomia.execute();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                                backgroudWorker.execute(deleteGastronomia, gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            } else {
                                mEditGastronomia.setError("Punto gastronomia non presente");
                            }
                        } else {
                            mEditCitta.setError("Citta non presente");
                        }
                }else {
                    mEditGastronomia.setError("Campo obbligatorio");
                }
            }
        });


        return view;
    }

    private class deleteImageGastronomia extends AsyncTask<Void,Void,Void> {

        String path;

        public deleteImageGastronomia(String path){
            this.path=path;
        }

        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(urlCancellaImageGastronomia);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("path", "UTF-8")+"="+URLEncoder.encode(path,"UTF-8");
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
    }
}
