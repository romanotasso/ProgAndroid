package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IMieiViaggiActivity extends AppCompatActivity {

     DatabaseHelper db;
     Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_miei_viaggi);

        db = new DatabaseHelper(this);
        button = findViewById(R.id.romano);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getAllDataViaggi();
                if(res.getCount() == 0){
                    showMessage("Errore", "Nessun record trovato");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("Email: " + res.getString(0) + "\n");
                    buffer.append("Citta: " + res.getString(1) + "\n");
                    buffer.append("Nome: " + res.getString(2) + "\n\n");
                }
                showMessage("Viaggi:",buffer.toString());
            }
        });
    }

    public void showMessage(String  title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
