package com.example.android;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.List;


public class MappaFragment extends Fragment implements OnMapReadyCallback {

    String momumento;
    String citta;
    private final static String TAG = "MapFragment";
    int PLACE_PICKER_REQUEST = 1;
    GoogleMap map;
    SupportMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mappa, container, false);


        ((CittaActivity)getActivity()).toolbar.setTitle("Mappa");
        ((CittaActivity)getActivity()).actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        ((CittaActivity)getActivity()).actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        citta = ((CittaActivity)getActivity()).citta;


        Toast.makeText(getContext(),citta,Toast.LENGTH_SHORT).show();

        Bundle bundle = this.getArguments();
        momumento = bundle.getString("luogo");
        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        List <Address>addressList = null;

        Geocoder geocoder = new Geocoder(getContext());
        try {
            addressList = geocoder.getFromLocationName(citta,1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
        map.addMarker(new MarkerOptions().position(latLng).title(citta));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        mapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

    }
}