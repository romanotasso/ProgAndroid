package com.example.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

public class CancellaDialogGastronomia {

    private Activity activity;
    private AlertDialog dialog;
    private Context context;
    String punto;

    CancellaDialogGastronomia(Activity myActivity, Context context, String punto) {
        activity = myActivity;
        this.context = context;
        this.punto = punto;
    }

    void startLoadingDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final DatabaseHelper db = new DatabaseHelper(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setMessage("Sei sicuro di voler cancellare " + punto + " ?");
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
                String deleteGastronomia = "cancellaGastronomia";
                BackgroudWorker backgroudWorker = new BackgroudWorker(context);
                backgroudWorker.execute(deleteGastronomia, punto);
                db.deleteGastronomia(punto);
            }
        });


        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }

}
