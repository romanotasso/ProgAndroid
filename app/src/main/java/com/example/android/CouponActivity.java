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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;

public class CouponActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText inserisci;
    DatabaseHelper db;
    Button button;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    TextView nome;
    TextView cognome;
    ImageView immagineProfilo;
    View hView;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        db = new DatabaseHelper(this);
        inserisci = findViewById(R.id.inserisci_coupon);
        button = findViewById(R.id.coupon);

        toolbar = findViewById(R.id.toolbarNome);
        setSupportActionBar(toolbar);

        email = getIntent().getExtras().getString("email");

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);

        hView=navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);
        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        CouponActivity.DownloadImage downloadImage = new DownloadImage(email);
        downloadImage.execute();
        nome.setText(db.getNome(email));
        cognome.setText(db.getCognome(email));
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.citta).setVisible(false);
        menu.findItem(R.id.cerca).setVisible(false);

        navigationView.bringToFront();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView.setCheckedItem(R.id.couponMenu);

        Cursor couponDB = db.getCouponUtente(email);
        String coupon = null;

        for(couponDB.moveToFirst(); !couponDB.isAfterLast(); couponDB.moveToNext()){
           coupon = couponDB.getString(0);
        }

        final String finalCoupon = coupon;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inserisci.getText().toString().trim().isEmpty()){
                    Toast.makeText(CouponActivity.this, R.string.inserire_coupon, Toast.LENGTH_SHORT).show();
                } else {
                    if ((inserisci.getText().toString().equals(finalCoupon))) {
                        Intent intent = new Intent(CouponActivity.this, VisualizzaCouponActivity.class);
                        intent.putExtra("coupon", inserisci.getText().toString());
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CouponActivity.this, R.string.coupon_non_valido, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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
                Intent intentHome = new Intent(CouponActivity.this, HomeActivity.class);
                intentHome.putExtra("email", email);
                startActivity(intentHome);
            case R.id.viaggi:
                Intent intentViaggi = new Intent(CouponActivity.this, IMieiViaggiActivity.class);
                intentViaggi.putExtra("email", email);
                startActivity(intentViaggi);
                break;
            case R.id.couponMenu:
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(CouponActivity.this, ProfiloActivity.class);
                intentProfilo.putExtra("email", email);
                startActivity(intentProfilo);
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(CouponActivity.this, SettingPreferenceActivity.class);
                intentImpo.putExtra("email", email);
                startActivity(intentImpo);
                break;
            case R.id.logout:
                Intent h = new Intent(CouponActivity.this, LoginActivity.class);
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

            String url="";
            Bitmap bitmap=null;

            try{
                if(db.getCodiceFoto(email).equals("1")){
                    url = urlDownlaodImageProfilo+"standardJPG";
                    InputStream inputStream = new java.net.URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }else {
                    url = urlDownlaodImageProfilo + this.email.replaceAll("@","") + "JPG";
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

            if(bitmap!=null){
                immagineProfilo.setImageBitmap(bitmap);
            }

            super.onPostExecute(bitmap);
        }
    }
}
