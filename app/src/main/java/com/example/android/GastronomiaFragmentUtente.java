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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GastronomiaFragmentUtente extends Fragment {

    ListView myList;
    Cursor cittaRist;
    public ArrayList<Bitmap> foto;
    ArrayList<String> rist;
    ArrayList<String>categorie;
    DatabaseHelper db;
    ImageButton button;
    Spinner spinner;
    ArrayList<String> categorieFilter;

    public String citta, cittaSearch, cittaLista, cittaDB,email;

    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_gastronomia_utente, container, false);

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

        myList = view.findViewById(R.id.listaRistoranti);
        myList.setVisibility(View.VISIBLE);
        cittaRist = db.getAllDataRistorantiCitta(citta);
        rist = new ArrayList<String>();
        categorie=new ArrayList<>();

        for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
            rist.add(cittaRist.getString(0));
        }

        for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
            categorie.add(cittaRist.getString(1));
        }


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

        BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
        backgroudWorkerPhoto.context = getContext();
        backgroudWorkerPhoto.rist.addAll(rist);
        backgroudWorkerPhoto.nomeCitta = citta;
        backgroudWorkerPhoto.execute();

        categorieFilter = new ArrayList<>();
        spinner = view.findViewById(R.id.spinner);
        List<String> categorieList = new ArrayList<>();
        categorieFilter.add(0,"Filtra per : ");
        categorieFilter.add(1,"Filtra per : Gelateria");
        categorieFilter.add(2,"Filtra per : Pizzeria");
        categorieFilter.add(3,"Filtra per : Ristorante");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,categorieFilter);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                String categoria="";

                if(parent.getItemAtPosition(position).equals("Filtra per : ")) {
                    cittaRist = db.getAllDataRistorantiCitta(citta);
                    rist = new ArrayList<String>();
                    categorie=new ArrayList<>();

                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        rist.add(cittaRist.getString(0));
                    }
                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        categorie.add(cittaRist.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.rist.addAll(rist);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();

                }else if(parent.getItemAtPosition(position).equals("Filtra per : Gelateria")){
                    categoria="Gelateria";
                    cittaRist = db.getAllDataRistorantiCittaCategoria(citta,categoria);
                    rist = new ArrayList<String>();
                    categorie=new ArrayList<>();

                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        rist.add(cittaRist.getString(0));
                    }
                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        categorie.add(cittaRist.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.rist.addAll(rist);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(parent.getItemAtPosition(position).equals("Filtra per : Pizzeria")){

                    categoria="Pizzeria";
                    cittaRist = db.getAllDataRistorantiCittaCategoria(citta,categoria);
                    rist = new ArrayList<String>();
                    categorie=new ArrayList<>();

                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        rist.add(cittaRist.getString(0));
                    }
                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        categorie.add(cittaRist.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.rist.addAll(rist);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(parent.getItemAtPosition(position).equals("Filtra per : Ristorante")){

                    categoria="Ristorante";
                    cittaRist = db.getAllDataRistorantiCittaCategoria(citta,categoria);
                    rist = new ArrayList<String>();
                    categorie=new ArrayList<>();

                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        rist.add(cittaRist.getString(0));
                    }
                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        categorie.add(cittaRist.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.rist.addAll(rist);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return  view;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> nomePunto;
        ArrayList<String> categorie;

        MyAdapter(Context c, ArrayList<String> gastronomia,ArrayList<String> categorie) {
            super(c, R.layout.row_utente, R.id.textViewDatiCitta, gastronomia);
            this.context = c;
            this.nomePunto = gastronomia;
            this.categorie=categorie;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_utente, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            TextView categoria = row.findViewById(R.id.textViewCategoria);
            images.setImageBitmap(foto.get(position));
            TextView cittaNome = row.findViewById(R.id.textViewCitta);
            button = row.findViewById(R.id.id);
            categoria.setText(categorie.get(position));
            nome.setText(nomePunto.get(position));
            cittaNome.setText(citta);
            final String gastronomia = nomePunto.get(position);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AggiungiViaggioGastronomia viaggioGastronomia = new AggiungiViaggioGastronomia(getActivity(), context, citta, email, gastronomia);
                    viaggioGastronomia.startLoadingDialog();
                }
            });
            return row;
        }
    }

    public void refreshItems() {
        switch (refresh_count) {
            default:

                //cittaRist = db.getAllDataRistorantiCitta(citta);
                rist = new ArrayList<String>();
                categorie = new ArrayList<>();
                String categoria="";

                if(spinner.getSelectedItem().equals("Filtra per : ")) {
                    cittaRist = db.getAllDataRistorantiCitta(citta);
                    rist = new ArrayList<String>();
                    categorie=new ArrayList<>();

                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        rist.add(cittaRist.getString(0));
                    }
                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        categorie.add(cittaRist.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.rist.addAll(rist);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();

                }else if(spinner.getSelectedItem().equals("Filtra per : Gelateria")){
                    categoria="Gelateria";
                    cittaRist = db.getAllDataRistorantiCittaCategoria(citta,categoria);
                    rist = new ArrayList<String>();
                    categorie=new ArrayList<>();

                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        rist.add(cittaRist.getString(0));
                    }
                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        categorie.add(cittaRist.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.rist.addAll(rist);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(spinner.getSelectedItem().equals("Filtra per : Pizzeria")){

                    categoria="Pizzeria";
                    cittaRist = db.getAllDataRistorantiCittaCategoria(citta,categoria);
                    rist = new ArrayList<String>();
                    categorie=new ArrayList<>();

                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        rist.add(cittaRist.getString(0));
                    }
                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        categorie.add(cittaRist.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.rist.addAll(rist);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(spinner.getSelectedItem().equals("Filtra per : Ristorante")){

                    categoria="Ristorante";
                    cittaRist = db.getAllDataRistorantiCittaCategoria(citta,categoria);
                    rist = new ArrayList<String>();
                    categorie=new ArrayList<>();

                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        rist.add(cittaRist.getString(0));
                    }
                    for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
                        categorie.add(cittaRist.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.rist.addAll(rist);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }

                break;
        }
    }

    public class BackgroudWorkerPhoto extends AsyncTask<Void,Void, ArrayList<Bitmap>> {


        Context context;
        String nomeCitta;
        ArrayList<String> rist = new ArrayList<>();
        final static String url_photoGastronomia = "http://progandroid.altervista.org/progandorid/FotoGastronomia/";


        @Override
        public ArrayList<Bitmap> doInBackground(Void... voids) {

            Bitmap immagine;
            String url;
            ArrayList<Bitmap> fotoBack = new ArrayList<Bitmap>();

            try {
                for (int i = 0; i < rist.size(); i = i + 1) {
                    String nomeGastronomia =  rist.get(i).replaceAll(" ","%20");
                    url = url_photoGastronomia + nomeCitta +nomeGastronomia+ "JPG";
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

        this.foto.clear();
        this.foto.addAll(foto);
        MyAdapter adapter = new MyAdapter(getContext(), rist,categorie);
        myList.setAdapter(adapter);


    }



}
