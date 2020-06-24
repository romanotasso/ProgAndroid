package com.example.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

public class CancellaDialogGastronomiaViaggio {

    private Activity activity;
    private Context context;
    private AlertDialog dialog;
    String email, citta, nome, categoria;

    CancellaDialogGastronomiaViaggio (Activity activity, Context context, String citta, String email, String nome, String categoria) {
        this.activity = activity;
        this.context = context;
        this.citta = citta;
        this.email = email;
        this.nome = nome;
        this.categoria = categoria;
    }

    void startLoadingDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final DatabaseHelper db = new DatabaseHelper(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setTitle(R.string.cancellazione);
        builder.setMessage(R.string.sei_sicuro_cancellare);
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
                String type = "cancellaViaggio";
                BackgroudWorker backgroudWorker = new BackgroudWorker(context);
                backgroudWorker.execute(type, email, citta, nome, categoria);
                db.deleteViaggio(citta, email, nome, categoria);
            }
        });

        dialog = builder.create();
        dialog.show();
    }
}
