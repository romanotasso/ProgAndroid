package com.example.android;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class ActivityMap extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    String citta,luogo;
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mappa);

        toolbar = findViewById(R.id.toolbarNome);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Mappa citta");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        citta = getIntent().getExtras().getString("citta");
        luogo = getIntent().getExtras().getString("luogo");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




       // List<Address> addressList = null;

       /* if (map != null && citta != null && !citta.equals("")) {
            Geocoder geocoder = new Geocoder(ActivityMap.this);
            Toast.makeText(getApplicationContext(), citta, Toast.LENGTH_SHORT).show();
            try {
                addressList = geocoder.getFromLocationName(citta, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title(citta));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            ;
        } */
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        List<Address> addressList = null;
        map = googleMap;
        if (luogo != null && !luogo.equals("")) {
            Geocoder geocoder = new Geocoder(ActivityMap.this);
            try {
                addressList = geocoder.getFromLocationName(citta+" "+ luogo, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title(citta+" "+ luogo));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        }
    }
}