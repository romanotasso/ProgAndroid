package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class EsploraActivity extends AppCompatActivity {
    private static final String TAG = "Espolora";
    Button mButtonIndietro;
    TextView mEditData;
    DatePickerDialog.OnDateSetListener mDataSetListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esplora);
       mButtonIndietro = findViewById(R.id.button_indietro);
        mButtonIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent indietroIntent = new Intent(EsploraActivity.this,HomeActivity.class);
                startActivity(indietroIntent);
            }
        });
      /* mEditData = findViewById(R.id.textView);

        mEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int anno = cal.get(Calendar.YEAR);
                int mese = cal.get(Calendar.MONTH);
                int giorno = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(EsploraActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDataSetListner, anno, mese, giorno);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDataSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int giorno, int mese, int anno) {
                Log.d(TAG,"onDataSet: dd/mm//aaaa: " + giorno + "/" + mese + "/" + anno);
                mese = mese + 1;
                String date = giorno + "/" + mese + "/" + anno;
                mEditData.setText(date);
            }
        };*/
    }
}
