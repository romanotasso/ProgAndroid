package com.example.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class HotelBBFragmentAmministatoreCancella extends Fragment {

    DatabaseHelper db;
    Button mButtonCancella;
    EditText mEditCitta;
    EditText mEditHotel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hotel_b_b_amministratore_cancella, container, false);

        mEditHotel = view.findViewById(R.id.edittext_hotel);
        mButtonCancella = view.findViewById(R.id.button_cancella);
        mEditCitta = view.findViewById(R.id.edittext_citta);
        db = new DatabaseHelper(getContext());

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String citta = mEditCitta.getText().toString();
                String hotel = mEditHotel.getText().toString();
                String deleteHotel = "cancellaHotel";

                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                }else if ((!(hotel.trim().isEmpty()))) {
                    if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        if ((!db.checkHotel(hotel = hotel.substring(0, 1).toUpperCase() + hotel.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase()))) {
                            db.deleteHotelBB(hotel = hotel.substring(0, 1).toUpperCase() + hotel.substring(1).toLowerCase());
                            Toast.makeText(getContext(), "Hotel/B&B eliminato con successo", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                            backgroudWorker.execute(deleteHotel, hotel = hotel.substring(0, 1).toUpperCase() + hotel.substring(1).toLowerCase());
                        } else {
                            mEditHotel.setError("Hotel/B&B non presente");
                        }
                    } else {
                        mEditCitta.setError("Citta non presente");
                    }
                }else {
                    mEditHotel.setError("Campo obbligatorio");
                }
            }
        });


        return view;
    }
}