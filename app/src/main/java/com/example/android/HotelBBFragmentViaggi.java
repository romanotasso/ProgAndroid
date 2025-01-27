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
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotelBBFragmentViaggi extends Fragment {

    ListView myList;
    Cursor cittaHotel;
    ArrayList<String> hotel;
    DatabaseHelper db;
    ImageButton button;
    String citta, email;
    ArrayList<Bitmap> fotoHotel;
    MyAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;
    ArrayList<String> ratingArray;
    TextView nessunPunto;
    ArrayList<String> categorie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hotel_b_b_viaggi, container, false);

        db = new DatabaseHelper(getContext());
        email = getActivity().getIntent().getExtras().getString("email");
        citta = getActivity().getIntent().getExtras().getString("citta");

        refreshLayout = view.findViewById(R.id.swipe);

        myList = view.findViewById(R.id.listaHotelBBViaggi);
        myList.setVisibility(View.VISIBLE);
        cittaHotel = db.getAllViaggiHotel(citta, email, "Hotel");
        hotel = new ArrayList<String>();
        fotoHotel = new ArrayList<Bitmap>();
        ratingArray = new ArrayList<>();
        categorie = new ArrayList<String>();
        nessunPunto = view.findViewById(R.id.textNessunViaggio);

        for(cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()){
            hotel.add(cittaHotel.getString(0));
        }

        for(cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()){
            ratingArray.add(cittaHotel.getString(1));
        }

        for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()){
            categorie.add(cittaHotel.getString(2));
        }

        if(hotel.size()==0){
            myList.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.GONE);
            nessunPunto.setVisibility(View.VISIBLE);
            nessunPunto.setText("Niente da visualizzare");
        }else {
            BackgroundWorker backgroundWorker = new BackgroundWorker();
            backgroundWorker.context= getContext();
            backgroundWorker.citta = citta;
            backgroundWorker.nomiHotel = hotel;
            backgroundWorker.execute();
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
        ArrayList<String> ratingAdapterArray;
        ArrayList<String> categorie;

        MyAdapter(Context c, ArrayList<String> monumento, ArrayList<String> ratingAdapterArray, ArrayList<String> categorie) {
            super(c, R.layout.row_i_miei_viaggi, R.id.textViewDatiCitta, monumento);
            this.context = c;
            this.nomePunto = monumento;
            this.ratingAdapterArray =ratingAdapterArray;
            this.categorie = categorie;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_i_miei_viaggi, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            TextView categoria = row.findViewById(R.id.textViewCategoria);
            RatingBar ratingBar = row.findViewById(R.id.ratingbar);
            button = row.findViewById(R.id.id);
            images.setImageBitmap(fotoHotel.get(position));
            nome.setText(nomePunto.get(position));
            categoria.setText(categorie.get(position));
            TextView cittaNome = row.findViewById(R.id.textViewCitta);
            ratingBar.setRating(Float.valueOf(ratingAdapterArray.get(position)));
            cittaNome.setText(citta);
            final String hotel = nomePunto.get(position);
            final String categori = categorie.get(position);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CancellaDialogHotelViaggio cancellaDialogMonumento = new CancellaDialogHotelViaggio(getActivity(), context, citta, email, hotel, categori);
                    cancellaDialogMonumento.startLoadingDialog();
                }
            });

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {

                    String type = "updateRating";
                    String type1 = "aggiornamentoDatiViaggio";
                    String ratingChanged = "";
                    ratingChanged=(String.valueOf(rating));
                    BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                    backgroudWorker.execute(type, email, citta, hotel,ratingChanged, categori);
                    BackgroudWorker backgroundWorkerOne = new BackgroudWorker(getContext());
                    backgroundWorkerOne.execute(type1);
                }
            });

            return row;
        }
    }


    public class BackgroundWorker extends AsyncTask<Void,Void,ArrayList<Bitmap>> {

        Context context;
        ArrayList<String> nomiHotel;
        String citta;
        final static String url_photoGHotel = "http://progandroid.altervista.org/progandorid/FotoHotel/";

        @Override
        protected ArrayList<Bitmap> doInBackground(Void... voids) {

            String url;
            Bitmap immagine;
            ArrayList<Bitmap> fotoBack = new ArrayList<>();

            try {
                for (int i = 0; i < nomiHotel.size(); i = i + 1) {
                    String nomeHotel =  nomiHotel.get(i).replaceAll(" ","%20");
                    citta.replaceAll(" ","%20");
                    url = url_photoGHotel + citta + nomeHotel + "JPG";
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

        this.fotoHotel.addAll(foto);
        adapter = new MyAdapter(getContext(),hotel,ratingArray, categorie);
        myList.setAdapter(adapter);

    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaHotel = db.getAllViaggiHotel(citta, email, "Hotel");
                hotel = new ArrayList<String>();
                categorie = new ArrayList<String>();
                ratingArray = new ArrayList<>();

                for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                    hotel.add(cittaHotel.getString(0));
                }

                for(cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()){
                    ratingArray.add(cittaHotel.getString(1));
                }

                for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()){
                    categorie.add(cittaHotel.getString(2));
                }

                final MyAdapter adapter = new MyAdapter(getContext(), hotel,ratingArray, categorie);
                myList.setAdapter(adapter);
                break;
        }
    }
}
