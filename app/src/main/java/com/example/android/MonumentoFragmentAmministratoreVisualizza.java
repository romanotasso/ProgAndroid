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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonumentoFragmentAmministratoreVisualizza extends Fragment {

    DatabaseHelper db;
    ListView myList;
    Cursor cittaMonu;
    ArrayList<String> monumento;
    ArrayList<String> citta;
    ArrayList<String> categorie;
    public ArrayList<Bitmap> foto;
    MyAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;
    TextView nessunPunto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monumento_amministratore_visualizza, container, false);

        db = new DatabaseHelper(getContext());
        refreshLayout = view.findViewById(R.id.swipe);
        myList = view.findViewById(R.id.listaMonumentoVisualizza);
        myList.setVisibility(View.VISIBLE);
        cittaMonu = db.ordinaPerCittaMonumento();
        monumento = new ArrayList<String>();
        citta = new ArrayList<String>();
        foto = new ArrayList<Bitmap>();
        categorie = new ArrayList<>();
        nessunPunto = view.findViewById(R.id.textNessunViaggio);
        for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
            monumento.add(cittaMonu.getString(0));
        }

        for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
            citta.add(cittaMonu.getString(1));
        }

        for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
            categorie.add(cittaMonu.getString(2));
        }

        if(monumento.size()==0){
            myList.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.GONE);
            nessunPunto.setVisibility(View.VISIBLE);
            nessunPunto.setText("Niente da visualizzare");
        }else {
            Set<String> remuveDuplicate= new LinkedHashSet<String>(citta);
            ArrayList<String> appoggio = new ArrayList<>();
            appoggio.addAll(remuveDuplicate);

            BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
            backgroudWorkerPhoto.context = getContext();
            backgroudWorkerPhoto.monumenti=monumento;
            backgroudWorkerPhoto.citta = appoggio;
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


        MyAdapter(Context c, ArrayList<String> monumento, ArrayList<String> citta,ArrayList<String> categorie) {
            super(c, R.layout.row, R.id.textViewDatiCitta, monumento);
            this.context = c;
            this.nomePunto = monumento;
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
            nome.setText(nomePunto.get(position));
            citta.setText(cittaLista.get(position));
            categoria.setText(categorie.get(position));
            images.setImageBitmap(foto.get(position));
            final String nomeInteresse = nomePunto.get(position);
            final Context context = getContext();

            cancella.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CancellaDialogMonumento cancellaDialogMonumento = new CancellaDialogMonumento(getActivity(), context, nomeInteresse,cittaLista.get(position));
                    cancellaDialogMonumento.startLoadingDialog();
                }
            });
            return row;
        }
    }

    public class BackgroudWorkerPhoto extends AsyncTask<Void, Void, ArrayList<Bitmap>> {

        Context context;
        ArrayList<String> citta;
        ArrayList<String> monumenti;
        final static String url_photoMonumento = "http://progandroid.altervista.org/progandorid/FotoMonumenti/";
        int pos;

        @Override
        public ArrayList<Bitmap> doInBackground(Void... voids) {

            Bitmap immagine;
            String url;
            ArrayList<Bitmap> fotoBack = new ArrayList<Bitmap>();

            try {

                for(int i=0;i<citta.size();i++){
                    for(int j=0;j<monumenti.size();j++){
                        if(!(db.checkMonumento(monumenti.get(j),citta.get(i)))){
                            String cittaString = citta.get(i).replaceAll(" ", "%20");
                            String monuString = monumenti.get(j).replaceAll(" ", "%20");
                            url = url_photoMonumento + cittaString + monuString + "JPG";
                            InputStream inputStream = new java.net.URL(url).openStream();
                            immagine = BitmapFactory.decodeStream(inputStream);
                            if(immagine!=null){
                                fotoBack.add(immagine);
                            }
                        }else {
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
        adapter = new MyAdapter(getContext(),monumento,citta,categorie);
        myList.setAdapter(adapter);

    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaMonu = db.getAllDataMonumenti();
                monumento = new ArrayList<String>();
                citta = new ArrayList<String>();
                categorie = new ArrayList<>();

                for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                    monumento.add(cittaMonu.getString(0));
                }

                for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                    citta.add(cittaMonu.getString(1));
                }

                for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                    categorie.add(cittaMonu.getString(2));
                }


                Set<String> remuveDuplicate= new LinkedHashSet<String>(citta);
                ArrayList<String> appoggio = new ArrayList<>();
                appoggio.addAll(remuveDuplicate);

                BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                backgroudWorkerPhoto.context = getContext();
                backgroudWorkerPhoto.monumenti=monumento;
                backgroudWorkerPhoto.citta = appoggio;
                backgroudWorkerPhoto.execute();
                break;
        }
    }

}