package com.example.android;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.yinglan.shadowimageview.ShadowImageView;

import java.io.InputStream;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DatabaseHelper db;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    TextView nome;
    TextView cognome;
    TextView cercaCitta;
    CardView pulsCerca,pulsImieiViaggi,pulsCoupon;
    TextView iMieiViaggi;
    ImageView immagineProfilo;
    View hView;

    String email;
    //////////////////////////////////////////////////////////////
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textLatLong, textAddress;
    private ResultReceiver resultReceiver;

    Button buttonCerca;
    Button buttonViaggi;
    Button buttonCoupon;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        HomeActivity.DownloadImage downloadImage = new DownloadImage(email);
        downloadImage.execute();
        nome.setText(db.getNome(email));
        cognome.setText(db.getCognome(email));
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.citta).setVisible(false);
        menu.findItem(R.id.cerca).setVisible(false);
        menu.findItem(R.id.viaggi).setVisible(false);
        menu.findItem(R.id.couponMenu).setVisible(false);

        navigationView.bringToFront();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView.setCheckedItem(R.id.home);

        cercaCitta = findViewById(R.id.textViewCercaCitta);
        pulsCerca = findViewById(R.id.shadow);


        pulsCerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCerca = new Intent(HomeActivity.this, CittaAnteprimaActivity.class);
                intentCerca.putExtra("email",email);
                startActivity(intentCerca);
            }
        });

        iMieiViaggi= findViewById(R.id.textViewImieiViaggi);
        pulsImieiViaggi= findViewById(R.id.iMieiViaggiPuls);
        pulsImieiViaggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentViaggi = new Intent(HomeActivity.this, IMieiViaggiActivity.class);
                intentViaggi.putExtra("email", email);
                startActivity(intentViaggi);
            }
        });


        pulsCoupon = findViewById(R.id.CouponPuls);
        pulsCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCoupon = new Intent(HomeActivity.this, CouponActivity.class);
                intentCoupon.putExtra("email", email);
                startActivity(intentCoupon);
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
                break;
            case R.id.viaggi:
                Intent intentViaggi = new Intent(HomeActivity.this, IMieiViaggiActivity.class);
                intentViaggi.putExtra("email", email);
                startActivity(intentViaggi);
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(HomeActivity.this, ProfiloActivity.class);
                intentProfilo.putExtra("email", email);
                startActivity(intentProfilo);
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(HomeActivity.this, SettingPreferenceActivity.class);
                intentImpo.putExtra("email", email);
                startActivity(intentImpo);
                break;
            case R.id.logout:
                Intent h = new Intent(HomeActivity.this, LoginActivity.class);
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
