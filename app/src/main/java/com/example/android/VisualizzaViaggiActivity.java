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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;
import java.util.ArrayList;

public class VisualizzaViaggiActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper db;
    ListView myList;
    ArrayAdapter adapter;
    Cursor cittaLista;
    ArrayList<String> citta;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    ImageView immagineProfilo;

    TextView nome;
    TextView cognome;
    View hView;
    String email;

    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapterViaggi pageAdapterViaggi;
    TabItem tabMonumento, tabRistoranti, tabHotelBB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_viaggi);

        db = new DatabaseHelper(this);

        email = getIntent().getExtras().getString("email");

        toolbar = findViewById(R.id.toolbarNome);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);

        hView=navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);
        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        VisualizzaViaggiActivity.DownloadImage downloadImage = new DownloadImage(email);
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

        tabLayout = findViewById(R.id.tabLayoutVisualizzaViaggi);
        tabMonumento = findViewById(R.id.monumentiVisualizzaViaggi);
        tabRistoranti = findViewById(R.id.gastronomiaVisualizzaViaggi);
        tabHotelBB = findViewById(R.id.hotel_bbVisualizzaViaggi);
        viewPager = findViewById(R.id.viewPagerVisualizzaViaggi);

        pageAdapterViaggi = new PageAdapterViaggi(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapterViaggi);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(VisualizzaViaggiActivity.this, R.color.orange_scuro_chiaro));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(VisualizzaViaggiActivity.this, R.color.orange_scuro_chiaro));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaViaggiActivity.this, R.color.orange));
                    }
                } else if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(VisualizzaViaggiActivity.this, R.color.orange_scuro_chiaro));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(VisualizzaViaggiActivity.this, R.color.orange_scuro_chiaro));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaViaggiActivity.this, R.color.orange));
                    }
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(VisualizzaViaggiActivity.this, R.color.orange_scuro_chiaro));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(VisualizzaViaggiActivity.this, R.color.orange_scuro_chiaro));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaViaggiActivity.this, R.color.orange));
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
                Intent intentHome = new Intent(VisualizzaViaggiActivity.this, HomeActivity.class);
                intentHome.putExtra("email", email);
                startActivity(intentHome);
                break;
            case R.id.viaggi:
                break;
            case R.id.couponMenu:
                Intent intentCoupon = new Intent(VisualizzaViaggiActivity.this, CouponActivity.class);
                intentCoupon.putExtra("email", email);
                startActivity(intentCoupon);
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(VisualizzaViaggiActivity.this, ProfiloActivity.class);
                intentProfilo.putExtra("email", email);
                startActivity(intentProfilo);
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(VisualizzaViaggiActivity.this, SettingActivity.class);
                intentImpo.putExtra("email", email);
                startActivity(intentImpo);
                break;
            case R.id.logout:
                Intent h = new Intent(VisualizzaViaggiActivity.this, LoginActivity.class);
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
