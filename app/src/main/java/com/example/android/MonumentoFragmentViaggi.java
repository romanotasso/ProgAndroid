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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonumentoFragmentViaggi extends Fragment {

    ListView myList;
    Cursor cittaMonu;
    ArrayList<String> monumento;
    ArrayList<Bitmap> fotoMonumenti;
    DatabaseHelper db;
    ImageButton button;
    String citta, email;
    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;
    MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monumento_viaggi, container, false);

        db = new DatabaseHelper(getContext());
        email = getActivity().getIntent().getExtras().getString("email");
        citta = getActivity().getIntent().getExtras().getString("citta");

        refreshLayout = view.findViewById(R.id.swipe);

        myList = view.findViewById(R.id.listaMonumentoViaggi);
        myList.setVisibility(View.VISIBLE);
        cittaMonu = db.getAllViaggiMonumento(citta, email,"Monumento");
        monumento = new ArrayList<String>();
        fotoMonumenti = new ArrayList<Bitmap>();

        for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
            monumento.add(cittaMonu.getString(0));
        }

        BackgroundWorker backgroudWorker = new BackgroundWorker();
        backgroudWorker.context = getContext();
        backgroudWorker.citta = citta;
        backgroudWorker.nomeMonumenti = monumento;
        backgroudWorker.execute();

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

        MyAdapter(Context c, ArrayList<String> monumento/*, int imgs[]*/) {
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
            images.setImageBitmap(fotoMonumenti.get(position));
            nome.setText(nomePunto.get(position));
            TextView cittaNome = row.findViewById(R.id.textViewCitta);
            cittaNome.setText(citta);
            final String monumento = nomePunto.get(position);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CancellaDialogMonumentoViaggio cancellaDialogMonumento = new CancellaDialogMonumentoViaggio(getActivity(), context, citta, email, monumento);
                    cancellaDialogMonumento.startLoadingDialog();
                }
            });

            return row;
        }
    }

    public class BackgroundWorker extends AsyncTask<Void,Void,ArrayList<Bitmap>>{

        Context context;
        ArrayList<String> nomeMonumenti;
        String citta;
        final static String url_photoMonumento = "http://progandroid.altervista.org/progandorid/FotoMonumenti/";

        @Override
        protected ArrayList<Bitmap> doInBackground(Void... voids) {

            String url;
            Bitmap immagine;
            ArrayList<Bitmap> fotoBack = new ArrayList<>();

            try {
                for (int i = 0; i < nomeMonumenti.size(); i = i + 1) {
                    String nomeMonumento =  nomeMonumenti.get(i).replaceAll(" ","%20");
                    citta.replaceAll(" ","%20");
                    url = url_photoMonumento + citta +nomeMonumento+ "JPG";
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

        this.fotoMonumenti.addAll(foto);
        adapter = new MyAdapter(getContext(),monumento);
        myList.setAdapter(adapter);

    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaMonu = db.getAllViaggiMonumento(citta, email,"Monumento");
                monumento = new ArrayList<String>();

                for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                    monumento.add(cittaMonu.getString(0));
                }

                final MyAdapter adapter = new MyAdapter(getContext(), monumento/*, images*/);
                myList.setAdapter(adapter);
                break;
        }
    }
}