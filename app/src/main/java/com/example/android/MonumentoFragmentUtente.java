package com.example.android;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MonumentoFragmentUtente extends Fragment{

    ListView myList;
    Cursor cittaMonu;
    ArrayList<String> monumento;
    DatabaseHelper db;
    MyAdapter adapter;
    ImageButton button;
    public  ArrayList<Bitmap> foto;



    public String citta, cittaSearch, cittaLista, cittaDB, email;

    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monumento_utente, container, false);
        db = new DatabaseHelper(getContext());

        foto = new ArrayList<Bitmap>();

        email = getActivity().getIntent().getExtras().getString("email");
        cittaSearch = getActivity().getIntent().getExtras().getString("cittaSearch");
        cittaLista = getActivity().getIntent().getExtras().getString("cittaLista");
        cittaDB = getActivity().getIntent().getExtras().getString("cittaDB");
        if((cittaSearch == null) && (cittaLista == null)) {
            citta = cittaDB;
        } else if(cittaSearch == null){
            citta = cittaLista;
        } else {
            citta = cittaSearch;
        }

        refreshLayout = view.findViewById(R.id.swipe);

        myList = view.findViewById(R.id.listaMonumento);
        myList.setVisibility(View.VISIBLE);
        cittaMonu = db.getAllDataMonumentiCitta(citta);
        monumento = new ArrayList<String>();

        for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
            monumento.add(cittaMonu.getString(0));
        }
        db.close();

        BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
        backgroudWorkerPhoto.context = getContext();
        backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
        backgroudWorkerPhoto.nomeCitta = citta;
        backgroudWorkerPhoto.execute();

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

        return  view;
    }


    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> nomePunto;

        MyAdapter(Context c, ArrayList<String> gastronomia) {
            super(c, R.layout.row_utente, R.id.textViewDatiCitta, gastronomia);
            this.context = c;
            this.nomePunto = monumento;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_utente, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            button = row.findViewById(R.id.id);
            images.setImageBitmap(foto.get(position));
            nome.setText(nomePunto.get(position));
            TextView cittaNome = row.findViewById(R.id.textViewCitta);
            cittaNome.setText(citta);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String type = "inserisciViaggio";
                    BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                    backgroudWorker.execute(type, email, citta, nomePunto.get(position), "Monumento");
                    db.inserisciViaggio(email, citta, nomePunto.get(position), "Monumento");
                }
            });

            return row;
        }
    }

    public class BackgroudWorkerPhoto extends AsyncTask<Void,Void, ArrayList<Bitmap>> {

        Context context;
        String nomeCitta;
        ArrayList<String> nomeMonumenti = new ArrayList<>();
        final static String url_photoMonumento = "http://progandroid.altervista.org/progandorid/FotoMonumenti/";

        @Override
        public ArrayList<Bitmap> doInBackground(Void... voids) {

            Bitmap immagine;
            String url;
            ArrayList<Bitmap> fotoBack = new ArrayList<Bitmap>();

            try {
                for (int i = 0; i < nomeMonumenti.size(); i = i + 1) {
                    String nomeMonumento =  nomeMonumenti.get(i).replaceAll(" ","%20");
                    url = url_photoMonumento + nomeCitta +nomeMonumento+ "JPG";
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
         adapter = new MyAdapter(getContext(),monumento);
        myList.setAdapter(adapter);

    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaMonu = db.getAllDataMonumentiCitta(citta);
                monumento = new ArrayList<String>();

                for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                    monumento.add(cittaMonu.getString(0));
                }

                final MyAdapter adapter = new MyAdapter(getContext(), monumento);
                myList.setAdapter(adapter);
                break;
        }
    }
}