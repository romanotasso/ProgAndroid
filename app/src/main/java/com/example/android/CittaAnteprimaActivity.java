package com.example.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CittaAnteprimaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper db;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    ImageView immagineProfilo;
    ArrayList<Bitmap>foto;
    Cursor cittaHome;
    ArrayList<String> cittaArray;
    ListView myList;
    MyAdapter adapterList;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tabMonumento, tabRistoranti, tabHotelBB;

    public String citta, cittaSearch, cittaLista, cittaDB;

    TextView nome;
    TextView cognome;
    View hView;
    String email;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textLatLong, textAddress;
    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citta_anteprima);

        email = getIntent().getExtras().getString("email");

        if((cittaSearch == null) && (cittaLista == null)) {
            citta = cittaDB;
        } else if(cittaSearch == null){
            citta = cittaLista;
        } else {
            citta = cittaSearch;
        }

        db = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarNome);
        toolbar.setTitle("Cerca citta");
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
        cognome.setText(db.getCognome(email));
        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        DownloadImage downloadImage = new DownloadImage(email);
        downloadImage.execute();

        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.citta);

        /*Visualizzazione dati*/
        tabLayout = findViewById(R.id.tabLayout);
        tabMonumento = findViewById(R.id.monumenti);
        tabRistoranti = findViewById(R.id.gastronomia);
        tabHotelBB = findViewById(R.id.hotel_bb);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(CittaAnteprimaActivity.this, R.color.coloreLogo));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(CittaAnteprimaActivity.this, R.color.coloreLogo));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(CittaAnteprimaActivity.this, R.color.orange));
                    }
                } else if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(CittaAnteprimaActivity.this, R.color.coloreLogo));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(CittaAnteprimaActivity.this, R.color.coloreLogo));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(CittaAnteprimaActivity.this, R.color.orange));
                    }
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(CittaAnteprimaActivity.this, R.color.coloreLogo));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(CittaAnteprimaActivity.this, R.color.coloreLogo));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(CittaAnteprimaActivity.this, R.color.orange));
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

        /////////
        myList = findViewById(R.id.listView);
        myList.setVisibility(View.GONE);
        cittaHome = db.getAllDataCitta();
        cittaArray = new ArrayList<String>();
        foto = new ArrayList<>();

        for (cittaHome.moveToFirst(); !cittaHome.isAfterLast(); cittaHome.moveToNext()) {
            cittaArray.add(cittaHome.getString(0));
        }

        adapterList = new MyAdapter(getApplicationContext(),cittaArray,this.foto);
        myList.setAdapter(adapterList);


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String città = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(CittaAnteprimaActivity.this, CittaActivity.class);
                intent.putExtra("cittaLista", città);
                intent.putExtra("email", email);
                //Toast.makeText(CittaActivity.this, R.string.citta_presente, Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        resultReceiver = new AddressResultReciver(new Handler());
        textLatLong = findViewById(R.id.textLatLog);
        textAddress = findViewById(R.id.textAddress);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CittaAnteprimaActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            getCurrentLocation();
        } else {
            getCurrentLocation();
        }

        BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
        backgroudWorkerPhoto.nomeCitta.addAll(cittaArray);
        backgroudWorkerPhoto.execute();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (menuItem.getItemId()) {
            case R.id.citta:
                break;
            case R.id.home:
                Intent intentHome = new Intent(CittaAnteprimaActivity.this, HomeActivity.class);
                intentHome.putExtra("email", email);
                startActivity(intentHome);
                break;
            case R.id.viaggi:
                Intent intentViaggi = new Intent(CittaAnteprimaActivity.this, IMieiViaggiActivity.class);
                intentViaggi.putExtra("email", email);
                startActivity(intentViaggi);
                break;
            case R.id.cerca:
                Intent intentCerca = new Intent(CittaAnteprimaActivity.this, CittaAnteprimaActivity.class);
                intentCerca.putExtra("email", email);
                startActivity(intentCerca);
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(CittaAnteprimaActivity.this, ProfiloActivity.class);
                intentProfilo.putExtra("email", email);
                startActivity(intentProfilo);
                break;
            case R.id.couponMenu:
                Intent intentCoupon = new Intent(CittaAnteprimaActivity.this, CouponActivity.class);
                intentCoupon.putExtra("email", email);
                startActivity(intentCoupon);
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(CittaAnteprimaActivity.this, SettingPreferenceActivity.class);
                intentImpo.putExtra("email", email);
                startActivity(intentImpo);
                break;
            case R.id.logout:
                Intent h = new Intent(CittaAnteprimaActivity.this, LoginActivity.class);
                startActivity(h);
                finish();
                break;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cerca, menu);

        MenuItem cerca = menu.findItem(R.id.cerca);
        SearchView searchView = (SearchView) cerca.getActionView();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.substring(0,1).toUpperCase() + query.substring(1).toLowerCase();
                boolean check = db.checkCitta(query);
                if (!check) {
                    Intent intent = new Intent(CittaAnteprimaActivity.this, CittaActivity.class);
                    intent.putExtra("cittaSearch", query);
                    intent.putExtra("email", email);
                    Toast.makeText(CittaAnteprimaActivity.this, R.string.citta_presente, Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(CittaAnteprimaActivity.this, R.string.citta_non_presente, Toast.LENGTH_LONG).show();
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
                    ((Filterable)myList.getAdapter()).getFilter().filter(text);
                    myList.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, R.string.permesso_non_abilitatto, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getCurrentLocation() {

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(CittaAnteprimaActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(CittaAnteprimaActivity.this).removeLocationUpdates(this);
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
                if(!db.checkCitta(resultData.getString(Costanti.RESULT_DATA_KEY))){
                    db.close();
                    LoadingDialog loadingDialog = new LoadingDialog(CittaAnteprimaActivity.this,resultData.getString(Costanti.RESULT_DATA_KEY),email);
                    loadingDialog.startLoadingDialog();
                }

            } else {
                Toast.makeText(CittaAnteprimaActivity.this, resultData.getString(Costanti.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class BackgroudWorkerPhoto extends AsyncTask<Void,Void, ArrayList<Bitmap>> {

        ArrayList<String> nomeCitta = new ArrayList<>();
        final static String url_photoCitta = "http://progandroid.altervista.org/progandorid/FotoCitta/";

        @Override
        public ArrayList<Bitmap> doInBackground(Void... voids) {

            Bitmap immagine;
            String url;
            ArrayList<Bitmap> fotoBack = new ArrayList<Bitmap>();

            try {
                for (int i = 0; i < nomeCitta.size(); i = i + 1) {
                    String citta =  nomeCitta.get(i).replaceAll(" ","%20");
                    url = url_photoCitta + citta +"JPG";
                    InputStream inputStream = new java.net.URL(url).openStream();
                    immagine = BitmapFactory.decodeStream(inputStream);
                    if (!(immagine == null)) {
                        fotoBack.add(i, immagine);
                    }
                }
                return fotoBack;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            if(bitmaps!=null){
                returnFoto(bitmaps);
            }
        }
    }

    public void returnFoto(ArrayList<Bitmap> foto){

        this.foto.clear();
        this.foto.addAll(foto);
        MyAdapter adapterList;
        adapterList = new MyAdapter(getApplicationContext(),cittaArray,this.foto);
        myList.setAdapter(adapterList);

    }

    public class MyAdapter extends ArrayAdapter<String> implements Filterable {

        Context context;
        ArrayList<String> citta;
        ArrayList<String> cittaTemp;
        ArrayList<Bitmap> foto;
        CustomFiler cs;

        MyAdapter(Context c, ArrayList<String> citta,ArrayList<Bitmap> foto) {
            super(c, R.layout.row_listview);
            this.context = c;
            this.citta = citta;
            this.foto = foto;
            this.cittaTemp = citta;
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return citta.get(position);
        }
        @Override
        public int getCount() {
            return citta.size();
        }
        @Override
        public long getItemId(int position) {

            return position;
        }
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_listview, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            nome.setText(citta.get(position));
            images.setImageBitmap(foto.get(position));
            return row;
        }
        @NonNull
        @Override
        public Filter getFilter() {

            if(cs ==null){

                cs = new CustomFiler();
            }

            return cs;
        }

        class CustomFiler extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults result = new FilterResults();

                if(constraint!=null && constraint.length()>0){
                    constraint = constraint.toString().toUpperCase();
                    ArrayList<String> filters = new ArrayList<>();

                    for(int i=0;i<cittaTemp.size();i++){
                        if(cittaTemp.get(i).toUpperCase().contains(constraint)){
                            filters.add(cittaTemp.get(i));
                        }
                    }
                    result.count = filters.size();
                    result.values = filters;
                }else {
                    result.count = cittaTemp.size();
                    result.values = cittaTemp;
                }

                return result;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                citta = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        }

    }

}