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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;
import java.util.ArrayList;

public class CittaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper db;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    SearchView mysearchView;
    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    ImageView immagineProfilo;
////////
    Cursor cittaHome;
    ArrayAdapter adapter;
    ArrayList<String> cittaArray;
    ListView myList;
////////
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

        mysearchView = findViewById(R.id.mySearchBar);
        mysearchView.setVisibility(View.VISIBLE);
        cittaSearch = getIntent().getExtras().getString("cittaSearch");
        cittaLista = getIntent().getExtras().getString("cittaLista");
        cittaDB = getIntent().getExtras().getString("cittaDB");
        email = getIntent().getExtras().getString("email");

        if((cittaSearch == null) && (cittaLista == null)) {
            citta = cittaDB;
        } else if(cittaSearch == null){
            citta = cittaLista;
        } else {
            citta = cittaSearch;
        }

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

        hView=navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);

        nome.setText(db.getNome(email));
        db.close();
        cognome.setText(db.getCognome(email));
        db.close();
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


        /////////
        myList = findViewById(R.id.listView);
        myList.setVisibility(View.GONE);
        cittaHome = db.getAllDataCitta();
        cittaArray = new ArrayList<String>();

        for (cittaHome.moveToFirst(); !cittaHome.isAfterLast(); cittaHome.moveToNext()) {
            cittaArray.add(cittaHome.getString(0));
        }
        db.close();

        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, cittaArray);
        myList.setAdapter(adapter);

        mysearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean check = db.checkCitta(query);
                if (!check) {
                    Intent intent = new Intent(CittaActivity.this, CittaActivity.class);
                    intent.putExtra("cittaSearch", query);
                    intent.putExtra("email", email);
                    Toast.makeText(CittaActivity.this, R.string.citta_presente, Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(CittaActivity.this, R.string.citta_non_presente, Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                viewPager.setVisibility(View.INVISIBLE);
                String text = s;
                if (TextUtils.isEmpty(text)) {
                    viewPager.setVisibility(View.VISIBLE);
                    myList.setVisibility(View.GONE);
                } else {
                    adapter.getFilter().filter(text);
                    myList.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String città = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(CittaActivity.this, CittaActivity.class);
                intent.putExtra("cittaLista", città);
                intent.putExtra("email", email);
                Toast.makeText(CittaActivity.this, R.string.citta_presente, Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        ////////////
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (menuItem.getItemId()) {
            case R.id.citta:
                break;
            case R.id.home:
                Intent intentHome = new Intent(CittaActivity.this, HomeActivity.class);
                intentHome.putExtra("email", email);
                startActivity(intentHome);
                break;
            case R.id.viaggi:
                Intent intentViaggi = new Intent(CittaActivity.this, IMieiViaggiActivity.class);
                intentViaggi.putExtra("email", email);
                startActivity(intentViaggi);
                break;
            case R.id.cerca:
                Intent intentCerca = new Intent(CittaActivity.this, CercaActivity.class);
                intentCerca.putExtra("email", email);
                startActivity(intentCerca);
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(CittaActivity.this, ProfiloActivity.class);
                intentProfilo.putExtra("email", email);
                startActivity(intentProfilo);
                break;
            case R.id.couponMenu:
                Intent intentCoupon = new Intent(CittaActivity.this, CouponActivity.class);
                intentCoupon.putExtra("email", email);
                startActivity(intentCoupon);
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(CittaActivity.this, SettingActivity.class);
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
            this.email = email.replaceAll("@","");
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