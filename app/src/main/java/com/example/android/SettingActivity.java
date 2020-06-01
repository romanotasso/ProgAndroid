package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;

public class SettingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DatabaseHelper db;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    EditText mEditEmail;
    EditText mEditPassword;
    EditText mEditNewPassword;
    Button mAggiorna;

    String urlDownlaodImageProfilo = "http://progandroid.altervista.org/progandorid/FotoProfilo/";
    ImageView immagineProfilo;

    TextView nome;
    TextView cognome;
    View hView;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        db = new DatabaseHelper(this);

        mEditEmail = findViewById(R.id.edittext_email_agg);
        mEditPassword = findViewById(R.id.edittext_pass);
        mEditNewPassword = findViewById(R.id.edittext_new_pass);
        mAggiorna = findViewById(R.id.button_aggiorna);

        email = getIntent().getExtras().getString("email");

        mAggiorna.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = mEditEmail.getText().toString();
                String password = mEditPassword.getText().toString();
                String nuova_pass = mEditNewPassword.getText().toString();

                String type = "UpdateUtente";

                if (email.trim().isEmpty() || password.trim().isEmpty() || nuova_pass.trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.inserisci_aggiornamento, Toast.LENGTH_SHORT).show();
                } else {
                    BackgroudWorker backgroudWorker = new BackgroudWorker(getApplicationContext());
                    backgroudWorker.execute(type, email, password, nuova_pass);
                }
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        hView=navigationView.getHeaderView(0);
        nome = hView.findViewById(R.id.textNome);
        cognome = hView.findViewById(R.id.textCognome);

        nome.setText(db.getNome(email));
        cognome.setText(db.getCognome(email));

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.citta).setVisible(false);
        menu.findItem(R.id.cerca).setVisible(false);

        navigationView.bringToFront();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        immagineProfilo = hView.findViewById(R.id.imageProfilo);
        SettingActivity.DownloadImage downloadImage = new DownloadImage((getIntent().getExtras().getString("email")));
        downloadImage.execute();

        navigationView.setCheckedItem(R.id.impostazioni);

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
                Intent intentHome = new Intent(SettingActivity.this, CercaActivity.class);
                intentHome.putExtra("email",email);
                startActivity(intentHome);
                break;
            case R.id.viaggi:
                Intent intentViaggi = new Intent(SettingActivity.this, IMieiViaggiActivity.class);
                intentViaggi.putExtra("email", email);
                startActivity(intentViaggi);
                break;
            case R.id.couponMenu:
                Intent intentCoupon = new Intent(SettingActivity.this, CouponActivity.class);
                intentCoupon.putExtra("email", email);
                startActivity(intentCoupon);
                break;
            case R.id.profilo:
                Intent intentProfilo = new Intent(SettingActivity.this, ProfiloActivity.class);
                intentProfilo.putExtra("email",email);
                startActivity(intentProfilo);
                break;
            case R.id.impostazioni:
                break;
            case R.id.logout:
                Intent h = new Intent(SettingActivity.this, LoginActivity.class);
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
