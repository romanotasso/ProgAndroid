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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_b_b_viaggi, container, false);

        db = new DatabaseHelper(getContext());
        email = getActivity().getIntent().getExtras().getString("email");
        citta = getActivity().getIntent().getExtras().getString("citta");

        refreshLayout = view.findViewById(R.id.swipe);

        myList = view.findViewById(R.id.listaHotelBBViaggi);
        myList.setVisibility(View.VISIBLE);
        cittaHotel = db.getAllViaggiHotel(citta, email,"Hotel");
        hotel = new ArrayList<String>();
        fotoHotel = new ArrayList<Bitmap>();

        for(cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()){
            hotel.add(cittaHotel.getString(0));
        }

      BackgroundWorker backgroundWorker = new BackgroundWorker();
        backgroundWorker.context= getContext();
        backgroundWorker.citta = citta;
        backgroundWorker.nomiHotel = hotel;
        backgroundWorker.execute();

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

        MyAdapter(Context c, ArrayList<String> monumento) {
            super(c, R.layout.row, R.id.textViewDatiCitta, monumento);
            this.context = c;
            this.nomePunto = monumento;

        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            button = row.findViewById(R.id.id);
            images.setImageBitmap(fotoHotel.get(position));
            nome.setText(nomePunto.get(position));
            TextView cittaNome = row.findViewById(R.id.textViewCitta);
            cittaNome.setText(citta);
            final String hotel = nomePunto.get(position);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CancellaDialogHotelViaggio cancellaDialogMonumento = new CancellaDialogHotelViaggio(getActivity(), context, citta, email, hotel);
                    cancellaDialogMonumento.startLoadingDialog();
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
                    url = url_photoGHotel + citta +nomeHotel+ "JPG";
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
        adapter = new MyAdapter(getContext(),hotel);
        myList.setAdapter(adapter);

    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaHotel = db.getAllViaggiHotel(citta, email,"Hotel");
                hotel = new ArrayList<String>();

                for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
                    hotel.add(cittaHotel.getString(0));
                }

                final MyAdapter adapter = new MyAdapter(getContext(), hotel/*, images*/);
                myList.setAdapter(adapter);
                break;
        }
    }
}
