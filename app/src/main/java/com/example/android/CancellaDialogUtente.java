package com.example.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

public class CancellaDialogUtente {

    private Activity activity;
    private AlertDialog dialog;
    private Context context;
    String email, nome, cognome;

    CancellaDialogUtente (Activity activity, Context context, String email, String nome, String cognome) {
        this.activity = activity;
        this.context = context;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
    }

    void startLoadingDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final DatabaseHelper db = new DatabaseHelper(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setMessage("Sei sicuro di voler cancellare " + nome + " " + cognome + " ?");
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
                String type = "cancellaUtente";
                BackgroudWorker backgroudWorker = new BackgroudWorker(context);
                backgroudWorker.execute(type, email);
                db.deleteDati(email);
            }
        });

        dialog = builder.create();
        dialog.show();
    }
}