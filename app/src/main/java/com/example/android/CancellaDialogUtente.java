package com.example.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;

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

public class CancellaDialogUtente {

    private Activity activity;
    private AlertDialog dialog;
    private Context context;
    String email, nome, cognome;
    final static String URL_CANCELLA = "http://progandroid.altervista.org/progandorid/deletePhotoProfilo.php";

    CancellaDialogUtente (Activity activity, Context context, String email, String nome, String cognome) {
        this.activity = activity;
        this.context = context;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
    }

    void startLoadingDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final DatabaseHelper db = new DatabaseHelper(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setTitle(R.string.cancellazione);
        builder.setMessage(R.string.sei_sicuro_cancellare);
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
                String type = "cancellaUtente";
                BackgroudWorker backgroudWorker = new BackgroudWorker(context);
                backgroudWorker.execute(type, email);
                String controllo = db.getCodiceFoto(email);
                db.deleteDati(email);
                if(controllo.equals("0")){
                    deleteImageProfilo deleteImageProfilo = new deleteImageProfilo();
                    deleteImageProfilo.execute();
                }
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private class deleteImageProfilo extends AsyncTask<Void, Void, Void> {

        String emailCor = email.replaceAll("@","");
        String path=emailCor+"JPG";

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
