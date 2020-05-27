package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;
import java.util.ArrayList;

public class IMieiViaggiActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

     DatabaseHelper db;
     ListView myList;
     ArrayAdapter adapter;
     Cursor cittaLista;
     ArrayList<String> citta;

     DrawerLayout drawerLayout;
     ActionBarDrawerToggle actionBarDrawerToggle;
     Toolbar toolbar;
     NavigationView navigationView;

     TextView textViaggio;

     String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
     ImageView immagineProfilo;

     TextView nome;
     TextView cognome;
     View hView;
     String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_miei_viaggi);

        db = new DatabaseHelper(this);

        email = getIntent().getExtras().getString("email");

        myList = findViewById(R.id.listaViaggi);
        myList.setVisibility(View.VISIBLE);
        cittaLista = db.getAllDataViaggi(email);
        citta = new ArrayList<String>();

        textViaggio = findViewById(R.id.textViaggi);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);

        hView=navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);
        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        IMieiViaggiActivity.DownloadImage downloadImage = new DownloadImage(email);
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

        navigationView.setCheckedItem(R.id.viaggi);

        for(cittaLista.moveToFirst(); !cittaLista.isAfterLast(); cittaLista.moveToNext()){
            citta.add(cittaLista.getString(0));
        }

        int n = citta.size();
        String cittaViaggio;
        if (n == 0) {
            cittaViaggio = getResources().getQuantityString(R.plurals.le_citt_da_te_visitate, n);
            textViaggio.setText(cittaViaggio);
        } else if (n == 1) {
            cittaViaggio = getResources().getQuantityString(R.plurals.le_citt_da_te_visitate, n);
            textViaggio.setText(cittaViaggio);
        } else {
            cittaViaggio = getResources().getQuantityString(R.plurals.le_citt_da_te_visitate, n);
            textViaggio.setText(cittaViaggio);
        }

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,citta);
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String città = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(IMieiViaggiActivity.this, VisualizzaViaggiActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("citta", città);
                startActivity(intent);
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

        switch (menuItem.getItemId()) {
            case R.id.home:
                Intent intentHome = new Intent(IMieiViaggiActivity.this, HomeActivity.class);
                intentHome.putExtra("email", email);
                startActivity(intentHome);
                break;
            case R.id.viaggi:
                break;
            case R.id.couponMenu:
                Intent intentCoupon = new Intent(IMieiViaggiActivity.this, CouponActivity.class);
                intentCoupon.putExtra("email", email);
                startActivity(intentCoupon);
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(IMieiViaggiActivity.this, ProfiloActivity.class);
                intentProfilo.putExtra("email", email);
                startActivity(intentProfilo);
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(IMieiViaggiActivity.this, SettingActivity.class);
                intentImpo.putExtra("email", email);
                startActivity(intentImpo);
                break;
            case R.id.logout:
                Intent h = new Intent(IMieiViaggiActivity.this, LoginActivity.class);
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
