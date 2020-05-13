package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;

public class ProfiloActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper db;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    Cursor cursor;

    TextView nome;
    TextView cognome;
    TextView sesso;
    TextView data;
    TextView email;
    TextView citta;

    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    ImageView immagineProfilo;

    String e;

    View hView;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        db = new DatabaseHelper(this);

        e = getIntent().getExtras().getString("email");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        hView = navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);

        nome.setText(db.getNome(e));
        cognome.setText(db.getCognome(e));

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.citta).setVisible(false);
        menu.findItem(R.id.cerca).setVisible(false);

        navigationView.bringToFront();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        ProfiloActivity.DownloadImage downloadImage = new DownloadImage((getIntent().getExtras().getString("email")));
        downloadImage.execute();

        navigationView.setCheckedItem(R.id.profilo);

        cursor = db.getAllDataUtente(e);
        nome = findViewById(R.id.nome);
        cognome = findViewById(R.id.cognome);
        email = findViewById(R.id.email);
        citta = findViewById(R.id.citta);
        sesso = findViewById(R.id.sesso);
        data = findViewById(R.id.data);

        if (cursor.getCount() == 0) {
            Toast.makeText(ProfiloActivity.this, "Nessun utente", Toast.LENGTH_SHORT).show();
            return;
        }
        while (cursor.moveToNext()){
            nome.setText(cursor.getString(1));
            cognome.setText(cursor.getString(2));
            email.setText(cursor.getString(0));
            citta.setText(cursor.getString(3));
            sesso.setText(cursor.getString(4));
            data.setText(cursor.getString(5));
        }

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (menuItem.getItemId()){
            case R.id.home:
                Intent intentHome = new Intent(ProfiloActivity.this, HomeActivity.class);
                intentHome.putExtra("email",getIntent().getExtras().getString("email"));
                startActivity(intentHome);
                break;
            case R.id.profilo:
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(ProfiloActivity.this, SettingActivity.class);
                intentImpo.putExtra("email",getIntent().getExtras().getString("email"));
                startActivity(intentImpo);
                break;
            case R.id.logout:
                Intent h = new Intent(ProfiloActivity.this, LoginActivity.class);
                startActivity(h);
                finish();
                break;
        }
        return true;
    }

    private class DownloadImage extends AsyncTask<Void,Void, Bitmap> {

        String email;

        public DownloadImage(String email){
            this.email = email;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            String url = urlDownlaodImageProfilo + email + "JPG";
            Bitmap bitmap=null;

            try{

                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if(bitmap!=null){
                immagineProfilo.setImageBitmap(bitmap);
            }

            super.onPostExecute(bitmap);
        }
    }
}
