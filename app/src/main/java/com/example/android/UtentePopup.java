package com.example.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class UtentePopup {
    private Context context;
    private AlertDialog dialog;
    private String coupon;

    UtentePopup(Context context, String coupon) {
        this.context = context;
        this.coupon = coupon;
    }

    @SuppressLint("ResourceType")
    void startLoadingDialog () {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Coupon");
        builder.setMessage("Questo è il tuo coupon "+ coupon);
        builder.setCancelable(false);
        builder.setPositiveButton("Riscatta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        /*builder.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/
        dialog = builder.create();
        dialog.show();
    }
}
