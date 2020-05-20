package com.example.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

public class CancellaDialogMonumentoViaggio {

    private Activity activity;
    private AlertDialog dialog;
    private Context context;
    String citta, email, nome;

    CancellaDialogMonumentoViaggio (Activity activity, Context context, String citta, String email, String nome) {
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
        builder.setMessage("Sei sicuro di voler cancellare " + nome + " ?");
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
                backgroudWorker.execute(type, email, citta, nome, "Monumento");
                db.deleteViaggio(citta, email, nome, "Monumento");
            }
        });

        dialog = builder.create();
        dialog.show();
    }
}
