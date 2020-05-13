package com.example.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class MonumentoFragmentAmministatoreInserisci extends Fragment {

    DatabaseHelper db;
    Button mButtonInserisci;
    EditText mEditCitta;
    EditText mEditMonumento;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monumento_amministratore_inserisci, container, false);

        db = new DatabaseHelper(getContext());

        mEditCitta = view.findViewById(R.id.edittext_citta);
        mEditMonumento = view.findViewById(R.id.edittext_monumenti);
        mButtonInserisci = view.findViewById(R.id.button_inserisci);

        mButtonInserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = "inserimento";
                String citta = mEditCitta.getText().toString();
                String hotelbb = "";
                String gastronomia = "";
                String monumento = mEditMonumento.getText().toString();


                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                } else if (!monumento.trim().isEmpty()) {
                    if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        if (db.checkMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                            gastronomia = "";
                            hotelbb = "";
                            db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                            BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                            backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia , hotelbb);
                            Toast.makeText(getContext(), "Monumento inserito con successo", Toast.LENGTH_SHORT).show();
                        } else {
                            mEditMonumento.setError("Monumento gia presente");
                        }
                    } else {
                        mEditCitta.setError("Citta non presente");

                    }
                }else {
                    mEditMonumento.setError("Campo obbligatorio");

                }

            }
        });



        return view;
    }

}
