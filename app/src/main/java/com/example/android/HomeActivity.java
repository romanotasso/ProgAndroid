package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper db;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    SearchView mysearchView;
    ListView myList;
    Cursor cittaHome;
    ArrayAdapter adapter;
    ArrayList<String> citta;
    TextView nome;
    TextView cognome;
    View hView;
    String email;
    //////////////////////////////////////////////////////////////
    private  static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    private TextView textLatLong, textAddress;
    private Button attiva_gps;
    private ResultReceiver resultReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        hView=navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);
        nome.setText(db.getNome(getIntent().getExtras().getString("email")));
        cognome.setText(db.getCognome(getIntent().getExtras().getString("email")));
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        mysearchView = findViewById(R.id.searchView);
        myList = findViewById(R.id.listView);
        myList.setVisibility(View.GONE);
        cittaHome = db.getAllDataCitta();
        citta = new ArrayList<String>();

        for(cittaHome.moveToFirst(); !cittaHome.isAfterLast(); cittaHome.moveToNext()){
            citta.add(cittaHome.getString(0));
        }

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,citta);
        myList.setAdapter(adapter);

        mysearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), CittaActivity.class);
                startActivity(intent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                String text = s;
                if(TextUtils.isEmpty(text)){
                    myList.setVisibility(View.GONE);
                }
                else {
                    adapter.getFilter().filter(text);
                    myList.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String città = citta.get(i);
                //ShareFragment shareFragment = new ShareFragment();
                Intent intent = new Intent(getApplicationContext(), CittaActivity.class);
                //Bundle bundle = new Bundle();
                //bundle.putString("Città selezionata: ", città);
                //shareFragment.setArguments(bundle);
                startActivity(intent);

                /*FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.container_fragment, shareFragment).commit();*/

            }
        });


        /*fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new CercaFragment());
        fragmentTransaction.commit();*/
        ////////////////////////
        resultReceiver = new AddressResultReciver(new Handler());
        textLatLong = findViewById(R.id.textLatLog);
        textAddress = findViewById(R.id.textAddress);
        attiva_gps = findViewById(R.id.attiva_gps);


        /*findViewById(R.id.attiva_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                    getCurrentLocation();
                } else {
                    getCurrentLocation();
                }
            }
        });*/

        /*findViewById(R.id.text_attiva).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = db.checkCitta(textAddress.getText().toString());
                if(db.checkCitta(textAddress.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Città presente " + b, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "NIENTE", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        if(menuItem.getItemId() == R.id.profilo){
            Intent intent = new Intent(this, ProfiloActivity.class);
            startActivity(intent);
            /*fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new ProfiloFragment());
            fragmentTransaction.commit();*/
        }
        if(menuItem.getItemId() == R.id.impostazioni){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            /*fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new SettingFragment());
            fragmentTransaction.commit();*/
        }
        if(menuItem.getItemId() == R.id.condividi){
            Intent intent = new Intent(this, ShareActivity.class);
            startActivity(intent);
            /*fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new ShareFragment());
            fragmentTransaction.commit();*/
        }
        if(menuItem.getItemId() == R.id.logout){
            Intent h = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(h);
            finish();
        }
        if(menuItem.getItemId() == R.id.home){
            Intent h = new Intent(HomeActivity.this,HomeActivity.class);
            startActivity(h);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            } else {
                Toast.makeText(this,"Permesso non abilitato", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getCurrentLocation() {

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(HomeActivity.this).requestLocationUpdates(locationRequest, new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(HomeActivity.this).removeLocationUpdates(this);
                if(locationResult != null && locationResult.getLocations().size() > 0){
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitudine = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitudine = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    textLatLong.setText(String.format("Latitudine: %s\nLongitune: %s",latitudine,longitudine));
                    Location location = new Location("providerN.A.");
                    location.setLatitude(latitudine);
                    location.setLongitude(longitudine);
                    fetchAddressFromLatLong(location);
                }
            }
        }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatLong(Location location){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Costanti.RECEIVER, resultReceiver);
        intent.putExtra(Costanti.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    class AddressResultReciver extends ResultReceiver{
        AddressResultReciver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Costanti.SUCCESS_RESULT){
                textAddress.setText(resultData.getString(Costanti.RESULT_DATA_KEY));
            } else {
                Toast.makeText(HomeActivity.this, resultData.getString(Costanti.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onAttivaGPS(View view) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            getCurrentLocation();
        } else {
            getCurrentLocation();
        }
    }

    public void onChecKCitta(View view) {
        String indirizzo = textAddress.getText().toString();
        String type = "checkCitta";

        BackgroudWorker backgroudWorker = new BackgroudWorker(this);
        backgroudWorker.execute(type,indirizzo);

    }
}
