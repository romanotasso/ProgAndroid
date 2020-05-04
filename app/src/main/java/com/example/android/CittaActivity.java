package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class CittaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper db;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Button vediDati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citta);

        db = new DatabaseHelper(this);

        vediDati = findViewById(R.id.vediDati);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        vediDati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String citta = getIntent().getExtras().getString("citta");
                Cursor res = db.getAllDataMonumentiCitta(citta);
                if(res.getCount() == 0){
                    showMessage("Errore", "Nessun record trovato");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){

                    buffer.append("Nome: " + res.getString(0) + "\n");
                }
                showMessage("Monumento/i di " + citta + ":",buffer.toString());
            }
        });

        /*fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new CercaFragment());
        fragmentTransaction.commit();*/

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
            Intent h = new Intent(CittaActivity.this, LoginActivity.class);
            startActivity(h);
            finish();
        }
        if(menuItem.getItemId() == R.id.home){
            Intent h = new Intent(CittaActivity.this,HomeActivity.class);
            startActivity(h);
        }
        return true;
    }

    /*public void onShowDate(View view) {
        String citta = getIntent().getExtras().getString("citta");
        Cursor res = db.getAllDataMonumentiCitta(citta);
        if(res.getCount() == 0){
            showMessage("Errore", "Nessun record trovato");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){

            buffer.append("Nome: " + res.getString(0) + "\n");
        }
        showMessage("Monumento/i di " + citta + ":",buffer.toString());
    }*/

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
