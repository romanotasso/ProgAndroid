package com.example.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class GastronomiaFragmentAmministatoreCancella extends Fragment {

    DatabaseHelper db;
    Button mButtonCancella;
    EditText mEditCitta;
    EditText mEditGastronomia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gastronomia_amministratore_cancella, container, false);

        mEditGastronomia = view.findViewById(R.id.edittext_gastronomia);
        mButtonCancella = view.findViewById(R.id.button_cancella);
        mEditCitta = view.findViewById(R.id.edittext_citta);
        db = new DatabaseHelper(getContext());

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String citta = mEditCitta.getText().toString();
                String gastronomia = mEditGastronomia.getText().toString();
                String deleteGastronomia = "cancellaGastronomia";


                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                }else if ((!(gastronomia.trim().isEmpty()))) {
                        if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            if ((!db.checkGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase()))) {
                                db.deleteGastronomia(gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                                Toast.makeText(getContext(), "Punto Gastronomia eliminato con successo", Toast.LENGTH_SHORT).show();
                                BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                                backgroudWorker.execute(deleteGastronomia, gastronomia = gastronomia.substring(0, 1).toUpperCase() + gastronomia.substring(1).toLowerCase());
                            } else {
                                mEditGastronomia.setError("Punto gastronomia non presente");
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
