package com.example.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class IMieiViaggiActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

     DatabaseHelper db;
     ListView myList;
     MyAdapter adapterList;
     Cursor cittaLista;
     ArrayList<String> citta;
     ArrayList<Bitmap> foto;
     DrawerLayout drawerLayout;
     ActionBarDrawerToggle actionBarDrawerToggle;
     Toolbar toolbar;
     NavigationView navigationView;

     TextView textViaggio;

     String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
     ImageView immagineProfilo;

     TextView nome;
     TextView cognome;
     View hView;
     String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_miei_viaggi);

        db = new DatabaseHelper(this);

        email = getIntent().getExtras().getString("email");

        myList = findViewById(R.id.listaViaggi);
        myList.setVisibility(View.VISIBLE);
        cittaLista = db.getAllDataViaggi(email);
        citta = new ArrayList<String>();

        toolbar = findViewById(R.id.toolbarNome);
        toolbar.setTitle("I tuoi Viaggi");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);

        hView=navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);
        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        IMieiViaggiActivity.DownloadImage downloadImage = new DownloadImage(email);
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
        foto = new ArrayList<>();

        for(cittaLista.moveToFirst(); !cittaLista.isAfterLast(); cittaLista.moveToNext()){
            citta.add(cittaLista.getString(0));
        }

        int n = citta.size();
        String cittaViaggio;
       /* if (n == 0) {
            cittaViaggio = getResources().getQuantityString(R.plurals.le_citt_da_te_visitate, n);
            textViaggio.setText(cittaViaggio);
        } else if (n == 1) {
            cittaViaggio = getResources().getQuantityString(R.plurals.le_citt_da_te_visitate, n);
            textViaggio.setText(cittaViaggio);
        } else {
            cittaViaggio = getResources().getQuantityString(R.plurals.le_citt_da_te_visitate, n);
            textViaggio.setText(cittaViaggio);
        }*/


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String città = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(IMieiViaggiActivity.this, VisualizzaViaggiActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("citta", città);
                startActivity(intent);
            }
        });

        BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
        backgroudWorkerPhoto.nomeCitta.addAll(citta);
        backgroudWorkerPhoto.execute();

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
                Intent intentHome = new Intent(IMieiViaggiActivity.this, HomeActivity.class);
                intentHome.putExtra("email", email);
                startActivity(intentHome);
                break;
            case R.id.viaggi:
                break;
            case R.id.couponMenu:
                Intent intentCoupon = new Intent(IMieiViaggiActivity.this, CouponActivity.class);
                intentCoupon.putExtra("email", email);
                startActivity(intentCoupon);
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(IMieiViaggiActivity.this, ProfiloActivity.class);
                intentProfilo.putExtra("email", email);
                startActivity(intentProfilo);
                break;
            case R.id.impostazioni:
                Intent intentImpo = new Intent(IMieiViaggiActivity.this, SettingActivity.class);
                intentImpo.putExtra("email", email);
                startActivity(intentImpo);
                break;
            case R.id.logout:
                Intent h = new Intent(IMieiViaggiActivity.this, LoginActivity.class);
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
        adapterList = new MyAdapter(getApplicationContext(),citta,this.foto);
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
