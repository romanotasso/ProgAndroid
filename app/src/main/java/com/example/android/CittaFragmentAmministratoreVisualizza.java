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
public class CittaFragmentAmministratoreVisualizza extends Fragment {

    DatabaseHelper db;
    ListView myList;
    Cursor cittaLista;
    ArrayList<String> citta;
    ArrayList<Bitmap> foto;
    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;
    MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_citta_amministratore_visualizza, container, false);
        db = new DatabaseHelper(getContext());
        myList = view.findViewById(R.id.listaCittaVisualizza);
        myList.setVisibility(View.VISIBLE);
        cittaLista = db.getAllDataCitta();
        citta = new ArrayList<String>();
        foto = new ArrayList<>();
        refreshLayout = view.findViewById(R.id.swipe);

        for (cittaLista.moveToFirst(); !cittaLista.isAfterLast(); cittaLista.moveToNext()) {
            citta.add(cittaLista.getString(0));
        }

        BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
        backgroudWorkerPhoto.context = getContext();
        backgroudWorkerPhoto.nomeCitta.addAll(citta);
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

        return view;
    }


    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> nomePunto;

        MyAdapter(Context c, ArrayList<String> citta) {
            super(c, R.layout.row_citta, R.id.textViewDatiCitta, citta);
            this.context = c;
            this.nomePunto = citta;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_citta, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            ImageButton cancella = row.findViewById(R.id.id);
            nome.setText(nomePunto.get(position));
            images.setImageBitmap(foto.get(position));
            final String nomeInteresse = nomePunto.get(position);
            final Context context = getContext();

            cancella.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CancellaDialogCitta cancellaDialog = new CancellaDialogCitta(getActivity(),context, nomeInteresse);
                    cancellaDialog.startLoadingDialog();
                }
            });

            return row;
        }
    }


    public class BackgroudWorkerPhoto extends AsyncTask<Void,Void, ArrayList<Bitmap>> {


        Context context;
        ArrayList<String> nomeCitta = new ArrayList<>();
        final static String url_photoCitta = "http://progandroid.altervista.org/progandorid/FotoCitta/";


        @Override
        public ArrayList<Bitmap> doInBackground(Void... voids) {

            Bitmap immagine;
            String url;
            ArrayList<Bitmap> fotoBack = new ArrayList<Bitmap>();

            try {
                for (int i = 0; i < nomeCitta.size(); i = i + 1) {
                        String citta =  nomeCitta.get(i).replaceAll(" ","%20");
                        url = url_photoCitta+citta+"JPG";
                        InputStream inputStream = new java.net.URL(url).openStream();
                        immagine = BitmapFactory.decodeStream(inputStream);
                            if (!(immagine == null)) {
                                fotoBack.add(immagine);
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
        adapter = new MyAdapter(getContext(),citta);
        myList.setAdapter(adapter);

    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaLista = db.getAllDataCitta();
                citta = new ArrayList<String>();
                for (cittaLista.moveToFirst(); !cittaLista.isAfterLast(); cittaLista.moveToNext()) {
                    citta.add(cittaLista.getString(0));
                }
                db.close();
                final MyAdapter adapter = new MyAdapter(getContext(), citta/*, images*/);
                myList.setAdapter(adapter);
                break;
        }
    }
}
