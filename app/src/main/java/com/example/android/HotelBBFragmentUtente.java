package com.example.android;

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
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotelBBFragmentUtente extends Fragment {

    Spinner spinner;
    ArrayList<String> categorieFilter;
    ListView myList;
    Cursor cittaHotel;
    ArrayList<Bitmap> foto;
    ArrayList<String> hotel;
    ArrayList<String> categorie;
    DatabaseHelper db;
    ImageButton button;
    TextView nessunPunto;

    public String citta, cittaSearch, cittaLista, cittaDB, email;

    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel_b_b_utente, container, false);
        db = new DatabaseHelper(getContext());
        foto = new ArrayList<Bitmap>();
        categorie = new ArrayList<>();
        email = getActivity().getIntent().getExtras().getString("email");
        cittaSearch = getActivity().getIntent().getExtras().getString("cittaSearch");
        cittaLista = getActivity().getIntent().getExtras().getString("cittaLista");
        cittaDB = getActivity().getIntent().getExtras().getString("cittaDB");

        if ((cittaSearch == null) && (cittaLista == null)) {
            citta = cittaDB;
        } else if (cittaSearch == null) {
            citta = cittaLista;
        } else {
            citta = cittaSearch;
        }

        refreshLayout = view.findViewById(R.id.swipe);

        myList = view.findViewById(R.id.listaHotelBB);
        myList.setVisibility(View.VISIBLE);
        cittaHotel = db.getAllDataHotelBBCitta(citta);

        foto = new ArrayList<Bitmap>();
        hotel = new ArrayList<String>();

        for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
            hotel.add(cittaHotel.getString(0));
        }

        for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
            categorie.add(cittaHotel.getString(1));
        }

        nessunPunto = view.findViewById(R.id.textNessunViaggio);

        if(hotel.size()==0){
            myList.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.GONE);
            nessunPunto.setVisibility(View.VISIBLE);
            nessunPunto.setText("Niente da visualizzare");
        }else {
            BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
            backgroudWorkerPhoto.context = getContext();
            backgroudWorkerPhoto.hotel.addAll(hotel);
            backgroudWorkerPhoto.nomeCitta = citta;
            backgroudWorkerPhoto.execute();
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

        categorieFilter = new ArrayList<>();
        spinner = view.findViewById(R.id.spinner);
        List<String> categorieList = new ArrayList<>();
        categorieFilter.add(0,"Filtra per:");
        categorieFilter.add(1,"Filtra per: Hotel");
        categorieFilter.add(2,"Filtra per: Bed and Breakfast");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,categorieFilter);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                String categoria="";
                if(parent.getItemAtPosition(position).equals("Filtra per:")){
                    cittaHotel = db.getAllDataHotelBBCitta(citta);
                    hotel = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        hotel.add(cittaHotel.getString(0));
                    }

                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        categorie.add(cittaHotel.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.hotel.addAll(hotel);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();

                }else if(parent.getItemAtPosition(position).equals("Filtra per: Hotel")){

                    categoria="Hotel";
                    cittaHotel = db.getAllDataHotelBBCittaCategoria(citta,"Hotel");
                    hotel = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        hotel.add(cittaHotel.getString(0));
                    }

                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        categorie.add(cittaHotel.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.hotel.addAll(hotel);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(parent.getItemAtPosition(position).equals("Filtra per: Bed and Breakfast")){

                    categoria="Hotel";
                    cittaHotel = db.getAllDataHotelBBCittaCategoria(citta,"Bed and Breakfast");
                    hotel = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        hotel.add(cittaHotel.getString(0));
                    }

                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        categorie.add(cittaHotel.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.hotel.addAll(hotel);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> nomePunto;
        ArrayList<String> categorie;

        MyAdapter(Context c, ArrayList<String> hotel, ArrayList<String> categorie) {
            super(c, R.layout.row_utente, R.id.textViewDatiCitta, hotel);
            this.context = c;
            this.nomePunto = hotel;
            this.categorie=categorie;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_utente, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            TextView cittaNome = row.findViewById(R.id.textViewCitta);
            Button mVediMappa = row.findViewById(R.id.vedi_su_mappa);
            Button button = row.findViewById(R.id.id);
            TextView categoria = row.findViewById(R.id.textViewCategoria);
            categoria.setText(categorie.get(position));
            nome.setText(nomePunto.get(position));
            images.setImageBitmap(foto.get(position));
            cittaNome.setText(citta);
            final String hotel = nomePunto.get(position);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AggiungiViaggioHotel viaggioHotel = new AggiungiViaggioHotel(getActivity(), context, citta, email, hotel, categorie.get(position));
                    viaggioHotel.startLoadingDialog();
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
        ArrayList<String> hotel = new ArrayList<>();
        final static String url_photoHotel = "http://progandroid.altervista.org/progandorid/FotoHotel/";


        @Override
        public ArrayList<Bitmap> doInBackground(Void... voids) {

            Bitmap immagine;
            String url;
            ArrayList<Bitmap> fotoBack = new ArrayList<Bitmap>();

            try {
                for (int i = 0; i < hotel.size(); i = i + 1) {
                    String nomeHotel =  hotel.get(i).replaceAll(" ","%20");
                    url = url_photoHotel +nomeCitta +nomeHotel+"JPG";
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
            }else {
                MyAdapter adapter = new MyAdapter(getContext(), hotel,categorie);
                myList.setAdapter(adapter);
            }
        }
    }

    public void returnFoto(ArrayList<Bitmap> foto){

        this.foto.clear();
        this.foto.addAll(foto);
        MyAdapter adapter = new MyAdapter(getContext(), hotel,categorie);
        myList.setAdapter(adapter);


    }

    public void refreshItems() {
        switch (refresh_count) {
            default:

                //cittaHotel = db.getAllDataHotelBBCitta(citta);
                hotel = new ArrayList<String>();
                categorie = new ArrayList<>();
                String categoria="";

                if(spinner.getSelectedItem().equals("Filtra per:")){
                    cittaHotel = db.getAllDataHotelBBCitta(citta);
                    hotel = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        hotel.add(cittaHotel.getString(0));
                    }

                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        categorie.add(cittaHotel.getString(1));
                    }

                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.hotel.addAll(hotel);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();

                }else if(spinner.getSelectedItem().equals("Filtra per: Hotel")){

                    categoria="Hotel";
                    cittaHotel = db.getAllDataHotelBBCittaCategoria(citta,"Hotel");
                    hotel = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        hotel.add(cittaHotel.getString(0));
                    }

                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        categorie.add(cittaHotel.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.hotel.addAll(hotel);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }else if(spinner.getSelectedItem().equals("Filtra per: Bed and Breakfast")){

                    categoria="Hotel";
                    cittaHotel = db.getAllDataHotelBBCittaCategoria(citta,"Bed and Breakfast");
                    hotel = new ArrayList<String>();
                    categorie = new ArrayList<String>();
                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        hotel.add(cittaHotel.getString(0));
                    }

                    for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                        categorie.add(cittaHotel.getString(1));
                    }
                    BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                    backgroudWorkerPhoto.context = getContext();
                    backgroudWorkerPhoto.hotel.addAll(hotel);
                    backgroudWorkerPhoto.nomeCitta = citta;
                    backgroudWorkerPhoto.execute();
                }

                break;
        }
    }
}
