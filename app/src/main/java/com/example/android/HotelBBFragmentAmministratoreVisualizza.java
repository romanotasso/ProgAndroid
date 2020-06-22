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
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotelBBFragmentAmministratoreVisualizza extends Fragment {

    MyAdapter adapter;
    DatabaseHelper db;
    ListView myList;
    Cursor cittaHotel;
    ArrayList<String> hotel;
    ArrayList<String> citta  = new ArrayList<>();;
    ArrayList<Bitmap> foto;
    ArrayList<String> categorie;
    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;
    TextView nessunPunto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hotel_b_b_amministratore_visualizza, container, false);
        db = new DatabaseHelper(getContext());
        myList = view.findViewById(R.id.listaHotelBBVisualizza);
        myList.setVisibility(View.VISIBLE);
        cittaHotel = db.getAllDataHotelBB();
        foto = new ArrayList<>();
        refreshLayout = view.findViewById(R.id.swipe);
        hotel = new ArrayList<String>();
        citta = new ArrayList<String>();
        categorie = new ArrayList<String>();
        nessunPunto = view.findViewById(R.id.textNessunViaggio);

        for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
            hotel.add(cittaHotel.getString(0));
        }

        for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
            citta.add(cittaHotel.getString(1));
        }

        for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
            categorie.add(cittaHotel.getString(2));
        }


        if(hotel.size()==0){
            myList.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.GONE);
            nessunPunto.setVisibility(View.VISIBLE);
            nessunPunto.setText("Niente da visualizzare");
        }else {
            Set<String> remuveDuplicate1= new LinkedHashSet<String>(citta);
            ArrayList<String> appoggio1 = new ArrayList<>();
            appoggio1.addAll(remuveDuplicate1);

            BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
            backgroudWorkerPhoto.context = getContext();
            backgroudWorkerPhoto.hotel=hotel;
            backgroudWorkerPhoto.citta = appoggio1;
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

        return view;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> nomePunto;
        ArrayList<String> cittaLista;
        ArrayList<String> categorie;

        MyAdapter(Context c, ArrayList<String> hotel, ArrayList<String> citta,ArrayList<String> categorie) {
            super(c, R.layout.row, R.id.textViewDatiCitta, hotel);
            this.context = c;
            this.nomePunto = hotel;
            this.cittaLista = citta;
            this.categorie = categorie;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            TextView citta = row.findViewById(R.id.textViewCitta);
            TextView categoria = row.findViewById(R.id.textViewCategoria);
            ImageButton cancella = row.findViewById(R.id.id);
            citta.setText(cittaLista.get(position));
            images.setImageBitmap(foto.get(position));
            nome.setText(nomePunto.get(position));
            categoria.setText(categorie.get(position));

            final String nomeInteresse = nomePunto.get(position);
            final Context context = getContext();

            cancella.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CancellaDialogHotel cancellaDialog = new CancellaDialogHotel(getActivity(), context, nomeInteresse,cittaLista.get(position));
                    cancellaDialog.startLoadingDialog();
                }
            });
            return row;
        }
    }

    public class BackgroudWorkerPhoto extends AsyncTask<Void, Void, ArrayList<Bitmap>> {

        Context context;
        ArrayList<String> citta;
        ArrayList<String> hotel;
        final static String url_photoHotel = "http://progandroid.altervista.org/progandorid/FotoHotel/";

        @Override
        public ArrayList<Bitmap> doInBackground(Void... voids) {

            Bitmap immagine;
            String url;
            ArrayList<Bitmap> fotoBack = new ArrayList<Bitmap>();

            try {
                for(int i=0;i<citta.size();i++){
                    String cittaString = citta.get(i).replaceAll(" ", "%20");
                    for(int j=0;j<hotel.size();j++){
                        if(!db.checkHotel(hotel.get(j),cittaString)){
                            String hotelString = hotel.get(j).replaceAll(" ", "%20");
                            url = url_photoHotel + cittaString + hotelString + "JPG";
                            InputStream inputStream = new java.net.URL(url).openStream();
                            immagine = BitmapFactory.decodeStream(inputStream);
                            db.close();
                            if(immagine!=null){
                                fotoBack.add(immagine);
                            }
                        }else{
                            continue;
                        }
                    }
                }
                return fotoBack;
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            if (bitmaps != null) {
                returnFoto(bitmaps);
            }
        }
    }


    public void returnFoto(ArrayList<Bitmap> foto){
        this.foto.clear();
        this.foto.addAll(foto);
        adapter = new MyAdapter(getContext(),hotel,citta,categorie);
        myList.setAdapter(adapter);
    }



    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaHotel = db.getAllDataHotelBB();
                hotel = new ArrayList<String>();
                citta = new ArrayList<String>();
                categorie = new ArrayList<String>();

                for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                    hotel.add(cittaHotel.getString(0));
                }

                for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                    citta.add(cittaHotel.getString(1));
                }

                for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                    categorie.add(cittaHotel.getString(2));
                }

                Set<String> remuveDuplicate1= new LinkedHashSet<String>(citta);
                ArrayList<String> appoggio1 = new ArrayList<>();
                appoggio1.addAll(remuveDuplicate1);

                BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                backgroudWorkerPhoto.context = getContext();
                backgroudWorkerPhoto.hotel=hotel;
                backgroudWorkerPhoto.citta = appoggio1;
                backgroudWorkerPhoto.execute();
                break;
        }
    }
}
