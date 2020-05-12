package com.example.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class MonumentoFragmentAmministatoreCancella extends Fragment {

    EditText mEditCitta;
    EditText mEditMonumento;
    Button mButtonCancella;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monumento_amministratore_cancella, container, false);

        mEditMonumento = view.findViewById(R.id.edittext_monumenti);
        mButtonCancella = view.findViewById(R.id.button_cancella);
        mEditCitta = view.findViewById(R.id.edittext_citta);
        db = new DatabaseHelper(getContext());

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String citta = mEditCitta.getText().toString();
                String monumento = mEditMonumento.getText().toString();
                String deleteMonumento = "cancellaMonumento";

                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                }else if ((!(monumento.trim().isEmpty()))) {
                    if (!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        if ((!db.checkMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase()))) {
                            db.deleteMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase());
                            Toast.makeText(getContext(), "Monumento eliminato con successo", Toast.LENGTH_SHORT).show();
                            BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                            backgroudWorker.execute(deleteMonumento, monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase());
                        } else {
                            mEditMonumento.setError("Monumento non presente");
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