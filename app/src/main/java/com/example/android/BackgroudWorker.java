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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BackgroudWorker extends AsyncTask<String, Void, String> {

    final static int VALORE_EMAIL = 0;
    final static int VALORE_NOME = 1;
    final static int VALORE_CCOGNOME = 2;
    final static int VALORE_CITTA = 3;
    final static int VALORE_SESSO = 4;
    final static int VALORE_DATA = 5;
    final static int VALORE_COUPON = 6;
    final static int VALORE_CODICE_FOTO = 7;
    final static int VALORE_NOME_CITTA = 0;
    final static int VALORE_NOME_MHG = 0;
    final static int VALORE_CITTA_MHG = 1;
    final static int VALORE_CATEGORIA_MHG = 2;
    final static int VALORE_EMAIL_VIAGGIO = 0;
    final static int VALORE_CITTA_VIAGGIO = 1;
    final static int VALORE_NOME_VIAGGIO = 2;
    final static int VALORE_RATING_VIAGGIO = 3;
    final static int VALORE_CATEGORIA_VIAGGIO = 4;
    final static int VALORE_TIPOLOGIA_VIAGGIO = 5;


    String emailDati;
    String cittaDati;
    String nomeDati;
    String ratingdati;
    String categoriaDati;
    String tipologiaDati;

    Context context;
    AlertDialog alertDialog;

    BackgroudWorker(Context ctx) {
        context = ctx;
    }

    DatabaseHelper d;

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String login_url = "http://progandroid.altervista.org/progandorid/login.php";
        String register_url = "http://progandroid.altervista.org/progandorid/registrazione.php";
        String update_url = "http://progandroid.altervista.org/progandorid/aggiornamento.php";
        String delete_url = "http://progandroid.altervista.org/progandorid/cancellazione.php";
        String insert_url = "http://progandroid.altervista.org/progandorid/inserimento.php";
        String riceviDatiUtente_url = "http://progandroid.altervista.org/progandorid/fornisciDatiUtente.php";
        String riceviDatiCitta_url = "http://progandroid.altervista.org/progandorid/fornisciDatiCitta.php";
        String riceviDatiHotel_url = "http://progandroid.altervista.org/progandorid/fornisciDatiHotelBB.php";
        String riceviDatiMonumenti_url = "http://progandroid.altervista.org/progandorid/fornisciDatiMonumento.php";
        String riceviDatiGastronomia_url = "http://progandroid.altervista.org/progandorid/fornisciDatiGastronomia.php";
        String deleteDati = "http://progandroid.altervista.org/progandorid/cancellazioneDati.php";
        String checkCitta = "http://progandroid.altervista.org/progandorid/checkCitta.php";
        String inserisciViaggio = "http://progandroid.altervista.org/progandorid/inserimentoViaggi.php";
        String deleteViaggio = "http://progandroid.altervista.org/progandorid/cancellazioneViaggi.php";
        String riceviDatiViaggio = "http://progandroid.altervista.org/progandorid/fornisciDatiViaggio.php";
        String updateRating = "http://progandroid.altervista.org/progandorid/updateRating.php";

        d = new DatabaseHelper(context.getApplicationContext());

        if (type.equals("cancellaHotel")) {
            try {
                String hotel = params[1];
                URL url = new URL(deleteDati);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("hotelbb", "UTF-8") + "=" + URLEncoder.encode(hotel, "UTF-8");
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
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("cancellaGastronomia")) {
            try {
                String gastronomia = params[1];
                URL url = new URL(deleteDati);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("gastronomia", "UTF-8") + "=" + URLEncoder.encode(gastronomia, "UTF-8");
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
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("cancellaMonumento")) {
            try {
                String monumento = params[1];
                URL url = new URL(deleteDati);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("monumento", "UTF-8") + "=" + URLEncoder.encode(monumento, "UTF-8");
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
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("cancellaCitta")) {
            try {
                String citta = params[1];
                URL url = new URL(deleteDati);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("citta", "UTF-8") + "=" + URLEncoder.encode(citta, "UTF-8");
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
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("aggiornamentoDatiGastronomia")) {
            try {
                URL url = new URL(riceviDatiGastronomia_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                int i = 0;
                String valori[] = new String[3];
                d.updateDatiGastronomia();
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                    if (i == VALORE_NOME_MHG) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_CITTA_MHG) {
                        valori[i] = line;
                        i= i+ 1;
                    }else if ((i==VALORE_CATEGORIA_MHG)){
                        valori[i] = line;
                        d.inserisciGastronomia(valori[0], valori[1],valori[2]);
                        i = 0;
                    }
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("aggiornamentoDatiMonumenti")) {
            try {
                URL url = new URL(riceviDatiMonumenti_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                int i = 0;
                String valori[] = new String[3];
                d.updateDatiMonumenti();
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                    if (i == VALORE_NOME_MHG) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_CITTA_MHG) {
                        valori[i] = line;
                        i = i + 1;
                    }else if(i== VALORE_CATEGORIA_MHG){
                        valori[i] = line;
                        d.inserisciMonumento(valori[0],valori[1],valori[2]);
                        i = 0;
                    }
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("aggiornamentoDatiUtente")) {
            try {
                URL url = new URL(riceviDatiUtente_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                int i = 0;
                String valori[] = new String[8];
                d.updateDatiUtente();
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                    if (i == VALORE_EMAIL) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_NOME) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_CCOGNOME) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_CITTA) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_SESSO) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_DATA) {
                        valori[i] = line;
                        i = i +1;
                    } else if (i == VALORE_COUPON) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_CODICE_FOTO) {
                        valori[i] = line;
                        d.inserisciUtente(valori[0], valori[1], valori[2], valori[3], valori[4], valori[5], valori[6], valori[7]);
                        i = 0;
                    }
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("aggiornamentoDatiCitta")) {
            try {
                URL url = new URL(riceviDatiCitta_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                int i = 0;
                String valori[] = new String[1];
                d.updateDatiCitta();
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                    if (i == VALORE_NOME_CITTA) {
                        valori[i] = line;
                        d.inserisciCitta(valori[0]);
                    }
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("aggiornamentoDatiHotel")) {
            try {
                URL url = new URL(riceviDatiHotel_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                int i = 0;
                String valori[] = new String[3];
                d.updateDatiHotel();
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                    if (i == VALORE_NOME_MHG) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_CITTA_MHG) {
                        valori[i] = line;
                        i = i + 1;
                    }else if(i == VALORE_CATEGORIA_MHG){
                        valori[i] = line;
                        d.inserisciHotelBB(valori[0], valori[1],valori[2]);
                        i = 0;
                    }
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("aggiornamentoDatiViaggio")) {
            try {
                URL url = new URL(riceviDatiViaggio);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                int i = 0;
                String valori[] = new String[6];
                d.updateDatiViaggio();
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                    if (i == VALORE_EMAIL_VIAGGIO) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_CITTA_VIAGGIO) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_NOME_VIAGGIO) {
                        valori[i] = line;
                        i = i + 1;
                    }else if(i==VALORE_RATING_VIAGGIO){
                        valori[i] = line;
                        i = i+1;
                    } else  if (i == VALORE_CATEGORIA_VIAGGIO) {
                        valori[i] = line;
                        i = i + 1;
                    } else if (i == VALORE_TIPOLOGIA_VIAGGIO){
                        valori[i] = line;
                        d.inserisciViaggio(valori[0], valori[1], valori[2], valori[3],valori[4],valori[5]);
                        i = 0;
                    }
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("login")) {
            try {
                String email = params[1];
                String password = params[2];
                emailDati = email;
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("register")) {
            try {
                String nome = params[1];
                String cognome = params[2];
                String email = params[3];
                String password = params[4];
                String citta = params[5];
                String sesso = params[6];
                String data = params[7];
                String coupon = params [8];
                String codiceFoto = params[9];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8") + "&"
                        + URLEncoder.encode("cognome", "UTF-8") + "=" + URLEncoder.encode(cognome, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("citta", "UTF-8") + "=" + URLEncoder.encode(citta, "UTF-8") + "&"
                        + URLEncoder.encode("sesso", "UTF-8") + "=" + URLEncoder.encode(sesso, "UTF-8") + "&"
                        + URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8") + "&"
                        + URLEncoder.encode("coupon", "UTF-8") + "=" + URLEncoder.encode(coupon, "UTF-8") + "&"
                        + URLEncoder.encode("codiceFoto", "UTF-8") + "=" + URLEncoder.encode(codiceFoto, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                //d.inserisciUtente(email, nome, cognome, citta, sesso, data, coupon, codiceFoto);
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("UpdateUtente")) {
            try {
                String email = params[1];
                String password = params[2];
                String nuova_pass = params[3];
                URL url = new URL(update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                        URLEncoder.encode("nuova_pass", "UTF-8") + "=" + URLEncoder.encode(nuova_pass, "UTF-8");
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("cancellaUtente")) {
            try {
                String email = params[1];
                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("inserimento")) {
            try {
                String citta = params[1];
                String monumento = params[2];
                String gastronomia = params[3];
                String hotelbb = params[4];
                String categoria = params[5];
                URL url = new URL(insert_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("citta", "UTF-8") + "=" + URLEncoder.encode(citta, "UTF-8") + "&" +
                        URLEncoder.encode("monumento", "UTF-8") + "=" + URLEncoder.encode(monumento, "UTF-8") + "&" +
                        URLEncoder.encode("gastronomia", "UTF-8") + "=" + URLEncoder.encode(gastronomia, "UTF-8") + "&" +
                        URLEncoder.encode("hotelbb", "UTF-8") + "=" + URLEncoder.encode(hotelbb, "UTF-8") + "&" +
                        URLEncoder.encode("categoria", "UTF-8") + "=" + URLEncoder.encode(categoria, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                if (d.checkCitta(citta)) {
                    d.inserisciCitta(citta);
                }
                if ((monumento == "") && (hotelbb == "")) {
                    d.inserisciGastronomia(gastronomia,citta,categoria);
                } else if ((gastronomia == "") && (monumento == "")) {
                    d.inserisciHotelBB(hotelbb,citta,categoria);
                } else if ((gastronomia == "") && (hotelbb == "")) {
                    d.inserisciMonumento(monumento,citta,categoria);
                }
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("checkCitta")) {
            try {
                String citta = params[1];
                String email = params[2];
                cittaDati = citta;
                emailDati = email;
                URL url = new URL(checkCitta);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("citta", "UTF-8") + "=" + URLEncoder.encode(citta, "UTF-8");
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
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("inserisciViaggio")) {
            try {
                String email = params[1];
                emailDati =params[1];
                String citta = params[2];
                cittaDati = params[2];
                String nome = params[3];
                nomeDati = params[3];
                String rating = params[4];
                ratingdati = params[4];
                String categoria = params[5];
                categoriaDati = params[5];
                String tipologia = params[6];
                tipologiaDati = params[6];
                URL url = new URL(inserisciViaggio);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("citta", "UTF-8") + "=" + URLEncoder.encode(citta, "UTF-8") + "&" +
                        URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8") + "&" +
                        URLEncoder.encode("categoria", "UTF-8") + "=" + URLEncoder.encode(categoria, "UTF-8")+ "&" +
                        URLEncoder.encode("rating", "UTF-8") + "=" + URLEncoder.encode(rating, "UTF-8") + "&" +
                        URLEncoder.encode("tipologia","UTF-8") + "=" + URLEncoder.encode(tipologia, "UTF-8");
                bufferedWriter.write(post_data);
                //d.inserisciViaggio(email,citta,nome,tipologia,rating);
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("cancellaViaggio")) {
            try {
                String email = params[1];
                String citta = params[2];
                String nome = params[3];
                String categoria = params[4];
                URL url = new URL(deleteViaggio);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("citta", "UTF-8") + "=" + URLEncoder.encode(citta, "UTF-8") + "&" +
                        URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8") + "&" +
                        URLEncoder.encode("categoria", "UTF-8") + "=" + URLEncoder.encode(categoria, "UTF-8");
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
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("updateRating")) {
            try {
                String email = params[1];
                String citta = params[2];
                String nome = params[3];
                String rating = params[4];
                String categoria = params[5];
                URL url = new URL(updateRating);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("citta", "UTF-8") + "=" + URLEncoder.encode(citta, "UTF-8") + "&" +
                        URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8") + "&" +
                        URLEncoder.encode("categoria", "UTF-8") + "=" + URLEncoder.encode(categoria, "UTF-8")+ "&" +
                        URLEncoder.encode("rating", "UTF-8") + "=" + URLEncoder.encode(rating, "UTF-8");
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

        if (result.equals("login success !!!!! Welcome")) {
            Intent intent = new Intent(context.getApplicationContext(), HomeActivity.class);
            intent.putExtra("email", emailDati);
            Toast.makeText(context.getApplicationContext(), "Ciao " + emailDati, Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        } else if (result.equals("login not success")) {
            Toast.makeText(context.getApplicationContext(), "Email o password errati", Toast.LENGTH_SHORT).show();
        } else if (result.equals("REGISTRAZIONE AVVENUTA CON SUCCESSO")) {
        } else if (result.equals("Attenzione, formato email non valido.")) {
            Toast.makeText(context.getApplicationContext(), "Attenzione, formato email non valido.", Toast.LENGTH_SHORT).show();
        } else if (result.equals("E-MAIL GIA ESISTENTE")) {
            Toast.makeText(context.getApplicationContext(), "E-MAIL GIA ESISTENTE", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Cancellazione avvenuta con successo")) {
            Toast.makeText(context.getApplicationContext(), "Cancellazione avvenuta con successo", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Errore email")) {
            Toast.makeText(context.getApplicationContext(), "Errore! Insersci una nuova mail", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Email non presente")) {
            Toast.makeText(context.getApplicationContext(), "Email non presente!", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Non presente")) {
            Toast.makeText(context.getApplicationContext(), "Email non presente!", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Aggiornamento avvenuto con successo")) {
            Toast.makeText(context.getApplicationContext(), "Password aggiornata con successo", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Errore aggiornamento")) {
            Toast.makeText(context.getApplicationContext(), "Password non aggiornata", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Password o email non corretti")) {
            Toast.makeText(context.getApplicationContext(), "Password o email non corretti", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Inserimento avvenuto con successo")) {
        } else if (result.equals("Errore nell'inserimento")) {
        } else if (result.equals("Citta non presente")) {
            Toast.makeText(context.getApplicationContext(), "Città non presente", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Inserimento citta avvenuto con successo")) {
            Toast.makeText(context.getApplicationContext(), "Inserimento città avvenuto con successo", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Errore citta")) {
            Toast.makeText(context.getApplicationContext(), "Errore città", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Citta presente")) {
            Toast.makeText(context.getApplicationContext(), "Città " + cittaDati + " presente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context.getApplicationContext(), CittaActivity.class);
            intent.putExtra("cittaDB", cittaDati);
            intent.putExtra("email", emailDati);
            context.startActivity(intent);
        } else if (result.equals("Viaggia già inserito")) {
            Toast.makeText(context.getApplicationContext(), "Viaggia già inserito", Toast.LENGTH_SHORT).show();
        } else if (result.equals("Inseriemento avvento con successo")) {
            Toast.makeText(context.getApplicationContext(), "Inseriemento avvento con successo", Toast.LENGTH_SHORT).show();
            d.inserisciViaggio(emailDati, cittaDati, nomeDati, ratingdati, categoriaDati, tipologiaDati);
        } else if (result.equals("Impossibile aggiungere viaggio")) {
            Toast.makeText(context.getApplicationContext(), "Impossibile aggiungere viaggio", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}





