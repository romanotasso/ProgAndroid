package com.example.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {


    EditText mEditEmail;
    EditText mEditPassword;
    EditText mEditNewPassword;
    Button mAggiorna;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        mEditEmail = view.findViewById(R.id.edittext_email_agg);
        mEditPassword = view.findViewById(R.id.edittext_pass);
        mEditNewPassword = view.findViewById(R.id.edittext_new_pass);
        mAggiorna = view.findViewById(R.id.button_aggiorna);
        mAggiorna.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = mEditEmail.getText().toString();
                String password = mEditPassword.getText().toString();
                String nuova_pass = mEditNewPassword.getText().toString();

                String type = "UpdateUtente";

                if (email.trim().isEmpty() || password.trim().isEmpty() || nuova_pass.trim().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Inserisci i dati per l'aggiornamento", Toast.LENGTH_SHORT).show();
                } else {
                    BackgroudWorker backgroudWorker = new BackgroudWorker(getActivity().getApplicationContext());
                    backgroudWorker.execute(type, email, password, nuova_pass);
                }
            }
        });
        return view;
    }



}