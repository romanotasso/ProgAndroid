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

public class HotelBBFragmentAmministratoreCancella extends Fragment {

    String urlCancellaImageHotel = "http://progandroid.altervista.org/progandorid/deletePhotoHotelBB.php";
    DatabaseHelper db;
    Button mButtonCancella;
    EditText mEditCitta;
    EditText mEditHotel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hotel_b_b_amministratore_cancella, container, false);

        mEditHotel = view.findViewById(R.id.edittext_hotel);
        mButtonCancella = view.findViewById(R.id.button_cancella);
        mEditCitta = view.findViewById(R.id.edittext_citta);
        db = new DatabaseHelper(getContext());

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String citta = mEditCitta.getText().toString();
                String hotel = mEditHotel.getText().toString();
                String deleteHotel = "cancellaHotel";

                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                }else if ((!(hotel.trim().isEmpty()))) {
                    if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        if ((!db.checkHotel(hotel = hotel.substring(0, 1).toUpperCase() + hotel.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase()))) {
                            db.deleteHotelBB(hotel = hotel.substring(0, 1).toUpperCase() + hotel.substring(1).toLowerCase());
                            Toast.makeText(getContext(), R.string.elimina_hotel, Toast.LENGTH_SHORT).show();
                            String path = (citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())+(hotel = hotel.substring(0, 1).toUpperCase() + hotel.substring(1).toLowerCase())+ "JPG";
                            deleteImageHotel deleteImageMonumento = new deleteImageHotel(path);
                            deleteImageMonumento.execute();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                            backgroudWorker.execute(deleteHotel, hotel = hotel.substring(0, 1).toUpperCase() + hotel.substring(1).toLowerCase());
                        } else {
                            mEditHotel.setError("Hotel/B&B non presente");
                        }
                    } else {
                        mEditCitta.setError("Citta non presente");
                    }
                }else {
                    mEditHotel.setError("Campo obbligatorio");
                }
            }
        });

        return view;
    }

    private class deleteImageHotel extends AsyncTask<Void,Void,Void> {

        String path;

        public deleteImageHotel(String path){
            this.path=path;
        }

        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(urlCancellaImageHotel);
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