package com.example.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class CittaFragmentAmmistatore extends Fragment {

    EditText mEditCitta;
    Button mButtonInserisci;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_citta_amministatore, container, false);

        db = new DatabaseHelper(getContext());

        mEditCitta = view.findViewById(R.id.edittext_citta);

        mButtonInserisci = view.findViewById(R.id.button_inserisci);

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    String type = "inserimento";
                    String citta = mEditCitta.getText().toString();
                    String hotelbb="";
                    String gastronomia="";
                    String monumento="";

                    if ((citta.trim().isEmpty())) {
                        mEditCitta.setError("Campo Obbligatorio");
                        mEditCitta.findFocus();
                    } else if (!(citta.trim().isEmpty())) {
                        if (db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            db.inserisciCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                            Toast.makeText(getContext(), "Città inserita con successo", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                            backgroudWorker.execute(type,citta,monumento,gastronomia,hotelbb);
                        } else {
                            mEditCitta.setError("Citta gia presente");
                            mEditCitta.findFocus();
                        }
                    }
                }

        });


        return view;
    }



}
