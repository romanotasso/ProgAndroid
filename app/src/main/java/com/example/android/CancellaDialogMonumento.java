package com.example.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.Toast;

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

public class CancellaDialogMonumento {

    private Activity activity;
    private AlertDialog dialog;
    private Context context;
    String punto;
    String citta;
    String URL_CANCELLA = "http://progandroid.altervista.org/progandorid/deletePhotoMonumento.php";

    CancellaDialogMonumento(Activity myActivity, Context context, String punto,String citta) {
        activity = myActivity;
        this.context = context;
        this.punto = punto;
        this.citta = citta;
    }

    void startLoadingDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final DatabaseHelper db = new DatabaseHelper(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setMessage("Sei sicuro di voler cancellare " + punto + " ?");
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String deleteMonumento = "cancellaMonumento";
                BackgroudWorker backgroudWorker = new BackgroudWorker(context);
                backgroudWorker.execute(deleteMonumento, punto);
                db.deleteMonumento(punto);
                deleteImageMonumento deleteImageMonumento = new deleteImageMonumento();
                deleteImageMonumento.execute();
            }
        });


        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }


    private class deleteImageMonumento extends AsyncTask<Void, Void, Void> {

        String cittaSenzaSpazio = citta.replaceAll(" ","%20");
        String monumentosenzaspazio = punto.replaceAll(" ","%20");
        String path = cittaSenzaSpazio+monumentosenzaspazio+"JPG";


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(URL_CANCELLA);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("path", "UTF-8") + "=" + URLEncoder.encode(path, "UTF-8");
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
