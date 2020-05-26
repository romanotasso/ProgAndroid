package com.example.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

public class AggiungiViaggioGastronomia {

    private Activity activity;
    private AlertDialog dialog;
    private Context context;
    String email, citta, nome;

    AggiungiViaggioGastronomia(Activity activity, Context context, String citta, String email, String nome) {
        this.activity = activity;
        this.context = context;
        this.citta = citta;
        this.email = email;
        this.nome = nome;
    }

    void startLoadingDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final DatabaseHelper db = new DatabaseHelper(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setTitle(R.string.aggiunta_viaggio);
        builder.setMessage(R.string.vuoi_aggiungere);
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String type = "inserisciViaggio";
                BackgroudWorker backgroudWorker = new BackgroudWorker(context);
                backgroudWorker.execute(type, email, citta, nome, "Gastronomia");
                db.inserisciViaggio(email, citta, nome, "Gastronomia");
            }
        });

        dialog = builder.create();
        dialog.show();
    }
}
