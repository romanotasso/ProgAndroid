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
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper db;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    /////////////////////////////
    private  static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textLatLong, textAddress, textAttiva;
    private ProgressBar progressBar;
    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new CercaFragment());
        fragmentTransaction.commit();
        ////////////////////////
        resultReceiver = new AddressResultReciver(new Handler());
        textLatLong = findViewById(R.id.textLatLog);
        progressBar = findViewById(R.id.processBar);
        textAddress = findViewById(R.id.textAddress);
        textAttiva = findViewById(R.id.text_attiva_gps);
        db = new DatabaseHelper(this);


        findViewById(R.id.text_attiva_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                    getCurrentLocation();
                } else {
                    getCurrentLocation();
                }
            }
        });

        findViewById(R.id.text_attiva).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = db.checkCitta(textAddress.getText().toString());
                if(db.checkCitta(textAddress.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Città presente " + b, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "NIENTE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        if(menuItem.getItemId() == R.id.profilo){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new ProfiloFragment());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId() == R.id.impostazioni){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new SettingFragment());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId() == R.id.condividi){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new ShareFragment());
            fragmentTransaction.commit();
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
        progressBar.setVisibility(View.VISIBLE);

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
                } else {
                    progressBar.setVisibility(View.GONE);
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
            progressBar.setVisibility(View.GONE);
        }
    }

}
