package com.example.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class VisualizzaUtenteActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView myList;
    Cursor utenteDB;
    ArrayList<String> email;
    ArrayList<String> nome;
    ArrayList<String> cognome;
    ImageButton button;
    public  ArrayList<Bitmap> foto;

    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_utente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        myList = findViewById(R.id.listaUtente);
        myList.setVisibility(View.VISIBLE);
        refreshLayout = findViewById(R.id.swipe);

        utenteDB = db.getAllData();
        email = new ArrayList<String>();
        nome = new ArrayList<String>();
        cognome = new ArrayList<String>();
        foto = new ArrayList<Bitmap>();

        for(utenteDB.moveToFirst(); !utenteDB.isAfterLast(); utenteDB.moveToNext()) {
            email.add(utenteDB.getString(0));
        }
        for(utenteDB.moveToFirst(); !utenteDB.isAfterLast(); utenteDB.moveToNext()) {
            nome.add(utenteDB.getString(1));
        }
        for(utenteDB.moveToFirst(); !utenteDB.isAfterLast(); utenteDB.moveToNext()) {
            cognome.add(utenteDB.getString(2));
        }

        /*BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
        backgroudWorkerPhoto.context = getApplicationContext();
        backgroudWorkerPhoto.nomeUtente.addAll(email);
        backgroudWorkerPhoto.execute();*/

        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), email, nome, cognome);
        myList.setAdapter(myAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> email;
        ArrayList<String> nome;
        ArrayList<String> cognome;


        MyAdapter(Context c, ArrayList<String> email, ArrayList<String> nome, ArrayList<String> cognome) {
            super(c, R.layout.row_utente, R.id.textViewDatiCitta, email);
            this.context = c;
            this.email = email;
            this.nome = nome;
            this.cognome = cognome;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_visualizza_utente, parent, false);

            ImageView immagineProfilo = row.findViewById(R.id.image);
            TextView emailUtente = row.findViewById(R.id.textViewEmail);
            TextView nomeUtente = row.findViewById(R.id.textViewNome);
            TextView cognomeUtente = row.findViewById(R.id.textViewCognome);
            button = row.findViewById(R.id.id);

            //immagineProfilo.setImageBitmap(foto.get(position));
            emailUtente.setText(email.get(position));
            nomeUtente.setText(nome.get(position));
            cognomeUtente.setText(cognome.get(position));

            /*VisualizzaUtenteActivity.DownloadImage downloadImage = new DownloadImage(email.get(position));
            downloadImage.execute();*/

            /*button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CancellaDialogUtente dialogUtente = new CancellaDialogUtente(getParent(), getContext(), email.get(position), nome.get(position), cognome.get(position));
                    dialogUtente.startLoadingDialog();
                }
            });*/

            return row;
        }
    }

    /*public class BackgroudWorkerPhoto extends AsyncTask<Void,Void, ArrayList<Bitmap>> {

        Context context;
        ArrayList<String> nomeUtente = new ArrayList<>();
        final static String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";

        @Override
        public ArrayList<Bitmap> doInBackground(Void... voids) {

            Bitmap immagine;
            String url;
            ArrayList<Bitmap> fotoBack = new ArrayList<Bitmap>();
            String a = null;

            try {
                for (int i = 0; i < nomeUtente.size(); i = i + 1) {
                    url = urlDownlaodImageProfilo + email.get(i) + "JPG";
                    InputStream inputStream = new java.net.URL(url).openStream();
                    immagine = BitmapFactory.decodeStream(inputStream);
                    if (!(immagine == null)) {
                        fotoBack.add(i, immagine);
                    }
                }
                return fotoBack;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            if(bitmaps!=null){
                returnFoto(bitmaps);
            }
        }
    }

    public void returnFoto(ArrayList<Bitmap> foto){

        this.foto.addAll(foto);
        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), email, nome, cognome);
        myList.setAdapter(myAdapter);

    }*/

    public void refreshItems() {
        switch (refresh_count) {
            default:
                utenteDB = db.getAllData();
                email = new ArrayList<String>();
                nome = new ArrayList<String>();
                cognome = new ArrayList<String>();

                for(utenteDB.moveToFirst(); !utenteDB.isAfterLast(); utenteDB.moveToNext()) {
                    email.add(utenteDB.getString(0));
                }
                for(utenteDB.moveToFirst(); !utenteDB.isAfterLast(); utenteDB.moveToNext()) {
                    nome.add(utenteDB.getString(1));
                }
                for(utenteDB.moveToFirst(); !utenteDB.isAfterLast(); utenteDB.moveToNext()) {
                    cognome.add(utenteDB.getString(2));
                }

                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), email, nome, cognome);
                myList.setAdapter(myAdapter);
                break;
        }
    }
}
