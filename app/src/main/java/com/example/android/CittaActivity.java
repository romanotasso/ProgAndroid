package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;

public class CittaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper db;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    ImageView immagineProfilo;

    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapterUtente pageAdapterUtente;
    TabItem tabMonumento, tabRistoranti, tabHotelBB;

    public String citta, cittaSearch, cittaLista, cittaDB;

    TextView nome;
    TextView cognome;
    View hView;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citta);

        cittaSearch = getIntent().getExtras().getString("cittaSearch");
        cittaLista = getIntent().getExtras().getString("cittaLista");
        cittaDB = getIntent().getExtras().getString("cittaDB");
        email = getIntent().getExtras().getString("email");

        db = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        email = getIntent().getExtras().getString("email");
        hView=navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);

        nome.setText(db.getNome(email));
        cognome.setText(db.getCognome(email));

        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        CittaActivity.DownloadImage downloadImage = new DownloadImage(email);
        downloadImage.execute();

        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.citta);

        /*Visualizzazione dati*/
        tabLayout = findViewById(R.id.tabLayout);
        tabMonumento = findViewById(R.id.monumenti);
        tabRistoranti = findViewById(R.id.gastronomia);
        tabHotelBB = findViewById(R.id.hotel_bb);
        viewPager = findViewById(R.id.viewPager);

        pageAdapterUtente = new PageAdapterUtente(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapterUtente);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(CittaActivity.this, R.color.orange));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(CittaActivity.this, R.color.orange));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(CittaActivity.this, R.color.orange));
                    }
                } else if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(CittaActivity.this, R.color.orange));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(CittaActivity.this, R.color.orange));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(CittaActivity.this, R.color.orange));
                    }
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(CittaActivity.this, R.color.orange));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(CittaActivity.this, R.color.orange));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(CittaActivity.this, R.color.orange));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (menuItem.getItemId()) {
            case R.id.citta:
                break;
            case R.id.home:
                Intent intentHome = new Intent(this, HomeActivity.class);
                intentHome.putExtra("email", email);
                startActivity(intentHome);
                break;
            case R.id.cerca:
                Intent intentCerca = new Intent(this, CercaActivity.class);
                intentCerca.putExtra("email", email);
                startActivity(intentCerca);
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(this, ProfiloActivity.class);
                intentProfilo.putExtra("email", email);
                startActivity(intentProfilo);
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(this, SettingActivity.class);
                intentImpo.putExtra("email", email);
                startActivity(intentImpo);
                break;
            case R.id.logout:
                Intent h = new Intent(CittaActivity.this, LoginActivity.class);
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
