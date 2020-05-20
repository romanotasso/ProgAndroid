package com.example.android;

import android.content.Context;
import android.database.Cursor;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GastronomiaFragmentUtente extends Fragment {

    ListView myList;
    Cursor cittaRist;
    //ArrayAdapter adapter;
    //int images [] = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground};
    ArrayList<String> rist;
    DatabaseHelper db;
    ImageButton button;

    public String citta, cittaSearch, cittaLista, cittaDB,email;

    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gastronomia_utente, container, false);

        db = new DatabaseHelper(getContext());

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

        for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
            rist.add(cittaRist.getString(0));
        }

        MyAdapter adapter = new MyAdapter(getContext(), rist/*, images*/);
        myList.setAdapter(adapter);

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

        return  view;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        //int rImg[];
        ArrayList<String> nomePunto;
        //Bitmap immagine[];

        MyAdapter(Context c, ArrayList<String> gastronomia/*, int imgs[]*/) {
            super(c, R.layout.row, R.id.textViewDatiCitta, gastronomia);
            this.context = c;
            this.nomePunto = gastronomia;
            //this. rImg = imgs;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_utente, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            TextView cittaNome = row.findViewById(R.id.textViewCitta);
            button = row.findViewById(R.id.id);

            //images.setImageResource(rImg[position]);
            nome.setText(nomePunto.get(position));
            cittaNome.setText(citta);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String type = "inserisciViaggio";
                    BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                    backgroudWorker.execute(type, email, citta, nomePunto.get(position), "Gastronomia");
                    db.inserisciViaggio(email, citta, nomePunto.get(position), "Gastronomia");
                }
            });
            return row;
        }
    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaRist = db.getAllDataRistorantiCitta(citta);
                rist = new ArrayList<String>();

                for (cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()) {
                    rist.add(cittaRist.getString(0));
                }

                final MyAdapter adapter = new MyAdapter(getContext(), rist/*, images*/);
                myList.setAdapter(adapter);
                break;
        }
    }
}
