package com.example.android;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;
import java.util.ArrayList;

public class CercaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper db;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    SearchView mysearchView;
    ListView myList;
    Cursor cittaHome;
    ArrayAdapter adapter;
    ArrayList<String> citta;
    TextView nome;
    TextView cognome;
    ImageView immagineProfilo;
    View hView;

    String email1;
    //////////////////////////////////////////////////////////////
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textLatLong, textAddress;
    private Button attiva_gps;
    private ResultReceiver resultReceiver;


    //@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca);

        db = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);

        hView=navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);
        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        DownloadImage downloadImage = new DownloadImage((getIntent().getExtras().getString("email")));
        downloadImage.execute();
        nome.setText(db.getNome(getIntent().getExtras().getString("email")));
        cognome.setText(db.getCognome(getIntent().getExtras().getString("email")));
        navigationView.setNavigationItemSelectedListener(this);

        email1 = getIntent().getExtras().getString("email");

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.citta).setVisible(false);

        navigationView.bringToFront();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView.setCheckedItem(R.id.cerca);

        mysearchView = findViewById(R.id.searchView);
        myList = findViewById(R.id.listView);
        myList.setVisibility(View.GONE);
        cittaHome = db.getAllDataCitta();
        citta = new ArrayList<String>();

        for (cittaHome.moveToFirst(); !cittaHome.isAfterLast(); cittaHome.moveToNext()) {
            citta.add(cittaHome.getString(0));
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, citta);
        myList.setAdapter(adapter);

        mysearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean check = db.checkCitta(query);
                if (!check) {
                    Intent intent = new Intent(CercaActivity.this, CittaActivity.class);
                    intent.putExtra("cittaSearch", query);
                    intent.putExtra("email", email1);
                    Toast.makeText(CercaActivity.this, "Città " + query + " presente", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(CercaActivity.this, "Città " + query + " non presente", Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                String text = s;
                if (TextUtils.isEmpty(text)) {
                    myList.setVisibility(View.GONE);
                    findViewById(R.id.attiva_gps).setVisibility(View.VISIBLE);
                    findViewById(R.id.checkCitta).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.attiva_gps).setVisibility(View.GONE);
                    findViewById(R.id.checkCitta).setVisibility(View.GONE);
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
                Intent intent = new Intent(CercaActivity.this, CittaActivity.class);
                intent.putExtra("cittaLista", città);
                intent.putExtra("email", email1);
                Toast.makeText(CercaActivity.this, "Città " + città + " presente", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        ////////////////////////
        resultReceiver = new AddressResultReciver(new Handler());
        textLatLong = findViewById(R.id.textLatLog);
        textAddress = findViewById(R.id.textAddress);
        attiva_gps = findViewById(R.id.attiva_gps);


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
                Intent intentHome = new Intent(CercaActivity.this, HomeActivity.class);
                intentHome.putExtra("email", email1);
                startActivity(intentHome);
                break;
            case R.id.cerca:
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(CercaActivity.this, ProfiloActivity.class);
                intentProfilo.putExtra("email", email1);
                startActivity(intentProfilo);
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(CercaActivity.this, SettingActivity.class);
                intentImpo.putExtra("email", email1);
                startActivity(intentImpo);
                break;
            case R.id.logout:
                Intent h = new Intent(CercaActivity.this, LoginActivity.class);
                startActivity(h);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permesso non abilitato", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getCurrentLocation() {

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(CercaActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(CercaActivity.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitudine = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitudine = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    textLatLong.setText(String.format("Latitudine: %s\nLongitune: %s", latitudine, longitudine));
                    Location location = new Location("providerN.A.");
                    location.setLatitude(latitudine);
                    location.setLongitude(longitudine);
                    fetchAddressFromLatLong(location);
                }
            }
        }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatLong(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Costanti.RECEIVER, resultReceiver);
        intent.putExtra(Costanti.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    class AddressResultReciver extends ResultReceiver {
        AddressResultReciver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Costanti.SUCCESS_RESULT) {
                textAddress.setText(resultData.getString(Costanti.RESULT_DATA_KEY));
            } else {
                Toast.makeText(CercaActivity.this, resultData.getString(Costanti.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onAttivaGPS(View view) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CercaActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            getCurrentLocation();
        } else {
            getCurrentLocation();
        }
    }

    public void onChecKCitta(View view) {
        String indirizzo = textAddress.getText().toString();
        String email = email1;
        String type = "checkCitta";

        BackgroudWorker backgroudWorker = new BackgroudWorker(this);
        backgroudWorker.execute(type, indirizzo, email);

    }

    private class DownloadImage extends AsyncTask<Void,Void,Bitmap>{

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
