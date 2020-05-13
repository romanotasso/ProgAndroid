package com.example.android;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    public String citta, cittaSearch, cittaLista, cittaDB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gastronomia_utente, container, false);

        db = new DatabaseHelper(getContext());

        String view1 = getActivity().getIntent().getExtras().getString("cittaDB");

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

        myList = view.findViewById(R.id.listaRistoranti);
        myList.setVisibility(View.VISIBLE);
        cittaRist = db.getAllDataRistorantiCitta(citta);
        rist = new ArrayList<String>();

        for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
            rist.add(cittaRist.getString(0));
        }

        MyAdapter adapter = new MyAdapter(getContext(), rist/*, images*/);
        myList.setAdapter(adapter);

        /*adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,rist);
        myList.setAdapter(adapter);*/

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
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);

            //images.setImageResource(rImg[position]);
            nome.setText(nomePunto.get(position));
            return row;
        }
    }
}
