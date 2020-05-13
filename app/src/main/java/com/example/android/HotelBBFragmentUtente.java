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
public class HotelBBFragmentUtente extends Fragment {

    ListView myList;
    Cursor cittaHotel;
    ArrayAdapter adapter;
    ArrayList<String> hotel;
    DatabaseHelper db;

    public String citta, cittaSearch, cittaLista, cittaDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_b_b_utente, container, false);

        db = new DatabaseHelper(getContext());

        //String view1 = getActivity().getIntent().getExtras().getString("cittaDB");
        cittaSearch = getActivity().getIntent().getExtras().getString("cittaSearch");
        cittaLista = getActivity().getIntent().getExtras().getString("cittaLista");
        cittaDB = getActivity().getIntent().getExtras().getString("cittaDB");
        if ((cittaSearch == null) && (cittaLista == null)) {
            citta = cittaDB;
        } else if (cittaSearch == null) {
            citta = cittaLista;
        } else {
            citta = cittaSearch;
        }


        myList = view.findViewById(R.id.listaHotelBB);
        myList.setVisibility(View.VISIBLE);
        cittaHotel = db.getAllDataHotelBBCitta(citta);
        hotel = new ArrayList<String>();

        for (cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()) {
            hotel.add(cittaHotel.getString(0));
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, hotel);
        myList.setAdapter(adapter);

        return view;
    }
}
