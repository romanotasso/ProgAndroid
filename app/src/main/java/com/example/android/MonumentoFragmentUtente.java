package com.example.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransitionImpl;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MonumentoFragmentUtente extends Fragment{



    ListView myList;
    Cursor cittaMonu;
    ArrayList<String> monumento;
    ArrayList<String> categorie;
    Spinner spinner;
    ArrayList<String> categorieFilter;
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
        categorie = new ArrayList<>();
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

        monumento = new ArrayList<String>();
        cittaMonu = db.getAllDataMonumentiCitta(citta);
        for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
            monumento.add(cittaMonu.getString(0));
        }
        for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
            categorie.add(cittaMonu.getString(1));
        }

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

        categorieFilter = new ArrayList<>();
        spinner = view.findViewById(R.id.spinner);
        List<String> categorieList = new ArrayList<>();
        categorieFilter.add(0,"Filtra per : ");
        categorieFilter.add(1,"Filtra per : Chiesa");
        categorieFilter.add(2,"Filtra per : Escursione");
        categorieFilter.add(3,"Filtra per : Lido Balneare");
        categorieFilter.add(4,"Filtra per : Monumento");
        categorieFilter.add(5,"Filtra per : Museo");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,categorieFilter);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                String categoria="";
                if(parent.getItemAtPosition(position).equals("Filtra per : ")){
                    cittaMonu = db.getAllDataMonumentiCitta(citta);
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();

                }else if(parent.getItemAtPosition(position).equals("Filtra per : Chiesa")){
                    categoria="Chiesa";
                    cittaMonu = db.getAllDataMonumentiCittaCategoria(citta,"Chiesa");
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();

                }else if(parent.getItemAtPosition(position).equals("Filtra per : Escursione")){
                    categoria="Escursione";
                    cittaMonu = db.getAllDataMonumentiCittaCategoria(citta,"Escursione");
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(parent.getItemAtPosition(position).equals("Filtra per : Lido Balneare")){
                    categoria="Lido Balneare";
                    cittaMonu = db.getAllDataMonumentiCittaCategoria(citta,"Lido Balneare");
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(parent.getItemAtPosition(position).equals("Filtra per : Monumento")){
                    categoria="Monumento";
                    cittaMonu = db.getAllDataMonumentiCittaCategoria(citta,"Monumento");
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(parent.getItemAtPosition(position).equals("Filtra per : Museo")){
                    categoria="Museo";
                    cittaMonu = db.getAllDataMonumentiCittaCategoria(citta,"Museo");
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
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
            this.nomePunto = monumento;
            this.categorie=categorie;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_utente, parent, false);
            ImageView images = row.findViewById(R.id.image);
            Button mVediMappa = row.findViewById(R.id.vedi_su_mappa);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            TextView categoria = row.findViewById(R.id.textViewCategoria);
            button = row.findViewById(R.id.id);
            images.setImageBitmap(foto.get(position));
            nome.setText(nomePunto.get(position));
            categoria.setText(categorie.get(position));
            TextView cittaNome = row.findViewById(R.id.textViewCitta);
            cittaNome.setText(citta);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AggiungiViaggioMonumento viaggioMonumento = new AggiungiViaggioMonumento(getActivity(), getContext(), citta, email, nomePunto.get(position));
                    viaggioMonumento.startLoadingDialog();
                }
            });

            mVediMappa.setOnClickListener((new View.OnClickListener() {


                @Override
                public void onClick(View v) {

                    MappaFragment mappaFragment = new MappaFragment();
                    Intent intent = new Intent(getContext(),ActivityMap.class);
                    intent.putExtra("citta",citta);
                    intent.putExtra("luogo",nomePunto.get(position));
                    startActivity(intent);

                   /* MappaFragment mappaFragment = new MappaFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("luogo", nomePunto.get(position));
                    mappaFragment.setArguments(bundle);
                    ((CittaActivity)getActivity()).callFragmentMap(mappaFragment);
                    */
                }
            }));

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

        this.foto.clear();
        this.foto.addAll(foto);
        adapter = new MyAdapter(getContext(),monumento,categorie);
        myList.setAdapter(adapter);

    }

    public void refreshItems() {

        switch (refresh_count) {
            default:

                //cittaMonu = db.getAllDataMonumentiCitta(citta);
                monumento = new ArrayList<String>();
                categorie = new ArrayList<>();
                String categoria="";

                if(spinner.getSelectedItem().equals("Filtra per : ")){
                    cittaMonu = db.getAllDataMonumentiCitta(citta);
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();

                }else if(spinner.getSelectedItem().equals("Filtra per : Chiesa")){
                    categoria="Chiesa";
                    cittaMonu = db.getAllDataMonumentiCittaCategoria(citta,categoria);
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();

                }else if(spinner.getSelectedItem().equals("Filtra per : Escursione")){
                    categoria="Escursione";
                    cittaMonu = db.getAllDataMonumentiCittaCategoria(citta,categoria);
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(spinner.getSelectedItem().equals("Filtra per : Lido Balneare")){
                    categoria="Lido Balneare";
                    cittaMonu = db.getAllDataMonumentiCittaCategoria(citta,"Lido Balneare");
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(spinner.getSelectedItem().equals("Filtra per : Monumento")){
                    categoria="Monumento";
                    cittaMonu = db.getAllDataMonumentiCittaCategoria(citta,"Monumento");
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(spinner.getSelectedItem().equals("Filtra per : Museo")){
                    categoria="Museo";
                    cittaMonu = db.getAllDataMonumentiCittaCategoria(citta,"Museo");
                    monumento = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                        monumento.add(cittaMonu.getString(0));
                    }
                    for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
                        categorie.add(cittaMonu.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.nomeMonumenti.addAll(monumento);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }
        }
    }
}