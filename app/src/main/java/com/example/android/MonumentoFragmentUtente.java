package com.example.android;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonumentoFragmentUtente extends Fragment {

    ListView myList;
    Cursor cittaMonu;
    ArrayAdapter adapter;
    ArrayList<String> monumento;
    DatabaseHelper db;

    public String citta, cittaSearch, cittaLista, cittaDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monumento_utente, container, false);

        db = new DatabaseHelper(getContext());

        //String view1 = getActivity().getIntent().getExtras().getString("cittaDB");

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

        myList = view.findViewById(R.id.listaMonumento);
        myList.setVisibility(View.VISIBLE);
        cittaMonu = db.getAllDataMonumentiCitta(citta);
        monumento = new ArrayList<String>();

        for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
            monumento.add(cittaMonu.getString(0));
        }

        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,monumento);
        myList.setAdapter(adapter);
        return  view;
    }

}
