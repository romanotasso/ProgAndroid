package com.example.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class GastronomiaFragmentAmministatore extends Fragment {

    DatabaseHelper db;
    Button mButtonInserisci;
    EditText mEditCitta;
    EditText mEditGastronomia;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmente_gastronomia_amministratore, container, false);

        db = new DatabaseHelper(getContext());

        mEditCitta = view.findViewById(R.id.edittext_citta);
        mEditGastronomia = view.findViewById(R.id.edittext_gastronomia);
        mButtonInserisci = view.findViewById(R.id.button_inserisci);

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = "inserimento";
                String citta = mEditCitta.getText().toString();
                String hotelbb = "";
                String gastronomia = mEditGastronomia.getText().toString();
                String monumento = "";


                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                } else if (!gastronomia.trim().isEmpty()) {
                    if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        if (db.checkGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            monumento = "";
                            hotelbb = "";
                            db.inserisciGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                            BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                            backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase(), hotelbb);
                            Toast.makeText(getContext(), "Punto gastronomia inserito con successo", Toast.LENGTH_SHORT).show();
                        } else {
                            mEditGastronomia.setError("Punto gastronomia gia presente");
                        }
                    } else {
                        mEditCitta.setError("Citta non presente");
                    }
                }else {
                    mEditGastronomia.setError("Campo obbligatorio");
                }
            }
        });

        return view;
    }


}
