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
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
    ImageView immProfiloMiniatura;
    TextView nomeCognome;
    TextView cognome;
    TextView sesso;
    TextView data;
    TextView email;
    TextView citta;
    TextView coupon;
    Button indietro;
    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    ImageView immagineProfilo;
    String emailExtras;
    View hView;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        db = new DatabaseHelper(this);

        emailExtras = getIntent().getExtras().getString("email");

        toolbar = findViewById(R.id.toolbarNome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profilo");

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        hView = navigationView.getHeaderView(0);
        nomeCognome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);

        nomeCognome.setText(db.getNome(emailExtras));
        cognome.setText(db.getCognome(emailExtras));


        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.citta).setVisible(false);
        menu.findItem(R.id.cerca).setVisible(false);

        navigationView.bringToFront();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        immProfiloMiniatura = findViewById(R.id.immagineProfiloActivityProfilo);

        ProfiloActivity.DownloadImage downloadImage = new DownloadImage(emailExtras);
        downloadImage.execute();

        navigationView.setCheckedItem(R.id.profilo);

        cursor = db.getAllDataUtente(emailExtras);
        nomeCognome = findViewById(R.id.nome);
        nomeCognome.setEnabled(false);
        nomeCognome.setTextColor(getResources().getColor(R.color.nero));
        email = findViewById(R.id.email);
        email.setEnabled(false);
        email.setTextColor(getResources().getColor(R.color.nero));
        citta = findViewById(R.id.citta);
        citta.setEnabled(false);
        citta.setTextColor(getResources().getColor(R.color.nero));
        sesso = findViewById(R.id.sesso);
        sesso.setEnabled(false);
        sesso.setTextColor(getResources().getColor(R.color.nero));
        data = findViewById(R.id.data);
        data.setEnabled(false);
        data.setTextColor(getResources().getColor(R.color.nero));
        coupon = findViewById(R.id.coupon);
        coupon.setEnabled(false);
        coupon.setTextColor(getResources().getColor(R.color.nero));
        indietro = findViewById(R.id.indietro);

        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfiloActivity.this, HomeActivity.class);
                intent.putExtra("email", emailExtras);
                startActivity(intent);
            }
        });

        if (cursor.getCount() == 0) {
            Toast.makeText(ProfiloActivity.this, R.string.nessun_utente, Toast.LENGTH_SHORT).show();
            return;
        }
        while (cursor.moveToNext()) {

            String nome;
            String cognome;
            nome = cursor.getString(1);
            cognome = cursor.getString(2);
            String stringNomeCognome = nome + " " + cognome;
            nomeCognome.setText(stringNomeCognome);
            email.setText(cursor.getString(0));
            citta.setText(cursor.getString(3));
            sesso.setText(cursor.getString(4));
            data.setText(cursor.getString(5));
            coupon.setText("Coupon : "+cursor.getString(6));
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

        switch (menuItem.getItemId()) {
            case R.id.home:
                Intent intentHome = new Intent(ProfiloActivity.this, HomeActivity.class);
                intentHome.putExtra("email", emailExtras);
                startActivity(intentHome);
                break;
            case R.id.viaggi:
                Intent intentViaggi = new Intent(ProfiloActivity.this, IMieiViaggiActivity.class);
                intentViaggi.putExtra("email", emailExtras);
                startActivity(intentViaggi);
                break;
            case R.id.couponMenu:
                Intent intentCoupon = new Intent(ProfiloActivity.this, CouponActivity.class);
                intentCoupon.putExtra("email", emailExtras);
                startActivity(intentCoupon);
                break;
            case R.id.profilo:
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(ProfiloActivity.this, SettingPreferenceActivity.class);
                intentImpo.putExtra("email", emailExtras);
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

    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {

        String email;

        public DownloadImage(String email) {
            this.email = email;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            String url = "";
            Bitmap bitmap;

            try {
                if (db.getCodiceFoto(email).equals("1")) {
                    url = urlDownlaodImageProfilo + "standardJPG";
                    InputStream inputStream = new java.net.URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } else {
                    url = urlDownlaodImageProfilo + this.email.replaceAll("@", "") + "JPG";
                    InputStream inputStream = new java.net.URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {

                immagineProfilo.setImageBitmap(bitmap);
                immProfiloMiniatura.setImageBitmap(bitmap);
            }

            super.onPostExecute(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_overflow, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.overflowMenu:
                Intent intentCambiaPassword = new Intent(ProfiloActivity.this, SettingActivity.class);
                intentCambiaPassword.putExtra("email", emailExtras);
                startActivity(intentCambiaPassword);
                break;
        }
            return super.onOptionsItemSelected(item);
    }

}