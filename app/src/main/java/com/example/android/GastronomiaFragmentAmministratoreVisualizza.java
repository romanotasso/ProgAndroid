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
public class GastronomiaFragmentAmministratoreVisualizza extends Fragment {

    DatabaseHelper db;
    ListView myList;
    Cursor cittaGast;
    ArrayList<String> gastronomia;
    ArrayList<String> citta;
    ArrayList<String> categorie;
    ArrayList<Bitmap> foto;
    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;
    MyAdapter adapter;
    TextView nessunPunto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gastronomia_amministratore_visualizza, container, false);

        db = new DatabaseHelper(getContext());
        nessunPunto = view.findViewById(R.id.textNessunViaggio);
        myList = view.findViewById(R.id.listaRistorantiVisualizza);
        myList.setVisibility(View.VISIBLE);
        cittaGast = db.ordinaPerCittaCibo();
        gastronomia = new ArrayList<String>();
        refreshLayout = view.findViewById(R.id.swipe);
        citta = new ArrayList<String>();
        foto = new ArrayList<Bitmap>();
        categorie = new ArrayList<String>();

        for (cittaGast.moveToFirst(); !cittaGast.isAfterLast(); cittaGast.moveToNext()) {
            gastronomia.add(cittaGast.getString(0));
        }

        for (cittaGast.moveToFirst(); !cittaGast.isAfterLast(); cittaGast.moveToNext()) {
            citta.add(cittaGast.getString(1));
        }

        for (cittaGast.moveToFirst(); !cittaGast.isAfterLast(); cittaGast.moveToNext()) {
            categorie.add(cittaGast.getString(2));
        }

        if(gastronomia.size()==0){
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
            backgroudWorkerPhoto.gastronomia=gastronomia;
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

        MyAdapter(Context c, ArrayList<String> gastronomia, ArrayList<String> citta,ArrayList<String> categorie) {
            super(c, R.layout.row, R.id.textViewDatiCitta, gastronomia);
            this.context = c;
            this.nomePunto = gastronomia;
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
                    final CancellaDialogGastronomia cancellaDialog = new CancellaDialogGastronomia(getActivity(), context, nomeInteresse,cittaLista.get(position));
                    cancellaDialog.startLoadingDialog();
                }
            });
            return row;
        }
    }


    public class BackgroudWorkerPhoto extends AsyncTask<Void, Void, ArrayList<Bitmap>> {


        Context context;
        ArrayList<String> citta;
        ArrayList<String> gastronomia;
        final static String url_photoGastr = "http://progandroid.altervista.org/progandorid/FotoGastronomia/";
        int pos;

        @Override
        public ArrayList<Bitmap> doInBackground(Void... voids) {

            Bitmap immagine;
            String url;
            ArrayList<Bitmap> fotoBack = new ArrayList<Bitmap>();

            try {
                for(int i=0;i<citta.size();i++){
                    for(int j=0;j<gastronomia.size();j++){
                        if(!(db.checkGastronomia(gastronomia.get(j),citta.get(i)))){
                            String gastrString = gastronomia.get(j).replaceAll(" ", "%20");
                            String cittaString = citta.get(i).replaceAll(" ", "%20");
                            url = url_photoGastr +cittaString +gastrString+"JPG";
                            InputStream inputStream = new java.net.URL(url).openStream();
                            immagine = BitmapFactory.decodeStream(inputStream);
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
        adapter = new MyAdapter(getContext(), gastronomia, citta,categorie);
        myList.setAdapter(adapter);

    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaGast = db.getAllDataGastronomia();
                gastronomia = new ArrayList<String>();
                citta = new ArrayList<String>();
                categorie = new ArrayList<String>();

                for (cittaGast.moveToFirst(); !cittaGast.isAfterLast(); cittaGast.moveToNext()) {
                    gastronomia.add(cittaGast.getString(0));
                }

                for (cittaGast.moveToFirst(); !cittaGast.isAfterLast(); cittaGast.moveToNext()) {
                    citta.add(cittaGast.getString(1));
                }

                for (cittaGast.moveToFirst(); !cittaGast.isAfterLast(); cittaGast.moveToNext()) {
                    categorie.add(cittaGast.getString(2));
                }

                Set<String> remuveDuplicate1= new LinkedHashSet<String>(citta);
                ArrayList<String> appoggio1 = new ArrayList<>();
                appoggio1.addAll(remuveDuplicate1);

                BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
                backgroudWorkerPhoto.context = getContext();
                backgroudWorkerPhoto.gastronomia=gastronomia;
                backgroudWorkerPhoto.citta = appoggio1;
                backgroudWorkerPhoto.execute();
                break;
        }
    }
}
