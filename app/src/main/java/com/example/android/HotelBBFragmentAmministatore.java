package com.example.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class HotelBBFragmentAmministatore extends Fragment {

    DatabaseHelper db;
    Button mButtonInserisci;
    EditText mEditCitta;
    EditText mEditHotel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hotel_b_b_amministratore, container, false);

        db = new DatabaseHelper(getContext());

        mEditCitta = view.findViewById(R.id.edittext_citta);
        mEditHotel = view.findViewById(R.id.edittext_hotel);
        mButtonInserisci = view.findViewById(R.id.button_inserisci);

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = "inserimento";
                String citta = mEditCitta.getText().toString();
                String monumento = "";
                String gastronomia = "";
                String hotelbb = mEditHotel.getText().toString();

                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                } else if (!hotelbb.trim().isEmpty()) {
                    if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        if (db.checkHotel(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            gastronomia = "";
                            monumento = "";
                            db.inserisciHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                            BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                            backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia , hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                            Toast.makeText(getContext(), "Hotel/B&B inserito con successo", Toast.LENGTH_SHORT).show();
                        } else {
                            mEditHotel.setError("Hotel/B&B gia presente");
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

    public void onClick(View view){







    }


}
