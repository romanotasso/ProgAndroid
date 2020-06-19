package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AmministratoreActivity extends AppCompatActivity {

    DatabaseHelper db;
    Button mButtonDati;
    Button mButtonInserisci;
    Button mButtomLogout;
    Toolbar toolbar;

    CardView puslAdd,pulsVisualizza;

    @SuppressLint({"NewApi", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amministratore);
        getWindow().setStatusBarColor(ContextCompat.getColor(AmministratoreActivity.this, R.color.orange));
        db = new DatabaseHelper(this);
        puslAdd = findViewById(R.id.pulsAddAmm);
        pulsVisualizza = findViewById(R.id.visualizzaPuls);

        toolbar = findViewById(R.id.toolbar_log);
        setSupportActionBar(toolbar);

        pulsVisualizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministratoreActivity.this, VisualizzaDatiAmministratoreActivity.class);
                startActivity(intent);
            }
        });

        puslAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministratoreActivity.this, InserisciAmministratoreActivity.class);
                startActivity(intent);
            }
        });



        /*
        mButtomLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmministratoreActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_log, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                Intent intent = new Intent(AmministratoreActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
