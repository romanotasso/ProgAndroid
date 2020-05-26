package com.example.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;
    private String citta;
    private String email;
    DatabaseHelper db = new DatabaseHelper(activity);

    LoadingDialog(Activity myActivity,String citta,String email) {
        activity = myActivity;
        this.citta = citta;
        this.email = email;
    }

    @SuppressLint("ResourceType")
    void startLoadingDialog () {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Sei ad " + citta);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.vai_alla_scheda, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, CittaActivity.class);
                intent.putExtra("cittaSearch",citta);
                intent.putExtra("email",email);
                activity.startActivity(intent);

            }
        });

        builder.setNegativeButton(R.string.chiudi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }
}
