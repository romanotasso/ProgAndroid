package com.example.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

public class BackgroudWorker extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog alertDialog;
    BackgroudWorker(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String  login_url ="http://progandroid.altervista.org/progandorid/login.php";
        String  register_url ="http://progandroid.altervista.org/progandorid/registrazione.php";
        if(type.equals("login")){
            try {
                String email = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+ URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }if(type.equals("register")){
            try {
                String nome=params[1];
                String cognome=params[2];
                String email = params[3];
                String password = params[4];
                String citta=params[5];
                String sesso=params[6];
                String data=params[7];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("nome","UTF-8")+"="+URLEncoder.encode(nome,"UTF-8")+"&"
                        + URLEncoder.encode("cognome","UTF-8")+"="+URLEncoder.encode(cognome,"UTF-8")+"&"
                        + URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        + URLEncoder.encode("citta","UTF-8")+"="+URLEncoder.encode(citta,"UTF-8")+"&"
                        + URLEncoder.encode("sesso","UTF-8")+"="+URLEncoder.encode(sesso,"UTF-8")+"&"
                        + URLEncoder.encode("data","UTF-8")+"="+URLEncoder.encode(data,"UTF-8");
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    protected void onPreExecute() {

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Stato di Registrazione");
    }

    @Override
    protected void onPostExecute(String result) {

        alertDialog.setMessage(result);

        if(result.equals("login success !!!!! Welcome")) {
            Toast.makeText(context.getApplicationContext(), "login success !!!!! Welcome", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context.getApplicationContext(), HomeActivity.class);
            context.startActivity(intent);
        }else if(result.equals("REGISTRAZIONE AVVENUTA CON SUCCESSO")){
            Toast.makeText(context.getApplicationContext(),"Registrazione avvenuta con successo",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
            context.startActivity(intent);
        }else if(result.equals("Attenzione, formato email non valido.")){
            Toast.makeText(context.getApplicationContext(),"Attenzione, formato email non valido.",Toast.LENGTH_SHORT).show();
        }else if(result.equals("E-MAIL GIA ESISTENTE")){
            Toast.makeText(context.getApplicationContext(),"E-MAIL GIA ESISTENTE",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}





