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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;

public class VisualizzaCouponActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper db;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    TextView nome;
    TextView cognome;
    ImageView immagineProfilo;
    View hView;

    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapterCoupon pageAdapterCoupon;
    TabItem tabMonumento, tabRistoranti, tabHotelBB;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_coupon);

        db = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarNome);
        setSupportActionBar(toolbar);

        email = getIntent().getExtras().getString("email");

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);

        hView=navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);
        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        VisualizzaCouponActivity.DownloadImage downloadImage = new DownloadImage(email);
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

        tabLayout = findViewById(R.id.tabLayoutCoupon);
        tabMonumento = findViewById(R.id.monumentiCoupon);
        tabRistoranti = findViewById(R.id.gastronomiaCoupon);
        tabHotelBB = findViewById(R.id.hotel_bbCoupon);
        viewPager = findViewById(R.id.viewPagerCoupon);

        pageAdapterCoupon = new PageAdapterCoupon(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapterCoupon);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(VisualizzaCouponActivity.this, R.color.orange_scuro_chiaro));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(VisualizzaCouponActivity.this, R.color.orange_scuro_chiaro));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaCouponActivity.this, R.color.orange));
                    }
                } else if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(VisualizzaCouponActivity.this, R.color.orange_scuro_chiaro));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(VisualizzaCouponActivity.this, R.color.orange_scuro_chiaro));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaCouponActivity.this, R.color.orange));
                    }
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(VisualizzaCouponActivity.this, R.color.orange_scuro_chiaro));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(VisualizzaCouponActivity.this, R.color.orange_scuro_chiaro));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaCouponActivity.this, R.color.orange));
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

        switch (menuItem.getItemId()){
            case R.id.home:
                Intent intentHome = new Intent(VisualizzaCouponActivity.this, HomeActivity.class);
                intentHome.putExtra("email", email);
                startActivity(intentHome);
            case R.id.viaggi:
                Intent intentViaggi = new Intent(VisualizzaCouponActivity.this, IMieiViaggiActivity.class);
                intentViaggi.putExtra("email", email);
                startActivity(intentViaggi);
                break;
            case R.id.couponMenu:
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(VisualizzaCouponActivity.this, ProfiloActivity.class);
                intentProfilo.putExtra("email", email);
                startActivity(intentProfilo);
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(VisualizzaCouponActivity.this, SettingActivity.class);
                intentImpo.putExtra("email", email);
                startActivity(intentImpo);
                break;
            case R.id.logout:
                Intent h = new Intent(VisualizzaCouponActivity.this, LoginActivity.class);
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
