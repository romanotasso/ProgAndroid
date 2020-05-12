package com.example.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class CittaFragmentAmministatoreCancella extends Fragment {

    DatabaseHelper db;
    Button mButtonCancella;
    EditText mEditCitta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_citta_amministratore_cancella, container, false);

        mButtonCancella = view.findViewById(R.id.button_cancella);
        mEditCitta = view.findViewById(R.id.edittext_citta);
        db = new DatabaseHelper(getContext());

        mButtonCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String citta = mEditCitta.getText().toString();
                String deleteCitta = "cancellaCitta";

                if ((citta.trim().isEmpty())) {
                    mEditCitta.setError("Campo Obbligatorio");
                    mEditCitta.findFocus();
                } else if (!(citta.trim().isEmpty())) {
                    if (!db.checkCitta(citta = citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())) {
                        db.deleteCitta((citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()));
                        Toast.makeText(getContext(), "Citta eliminata con successo", Toast.LENGTH_SHORT).show();
                        BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                        backgroudWorker.execute(deleteCitta, (citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase()));
                    } else {
                        mEditCitta.setError("Citta non presente");
                    }
                }

            }
        });


        return view;
    }
}
