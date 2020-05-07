package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mEditEmail = findViewById(R.id.edittext_email_agg);
        mEditPassword = findViewById(R.id.edittext_pass);
        mEditNewPassword = findViewById(R.id.edittext_new_pass);
        mAggiorna = findViewById(R.id.button_aggiorna);

        mAggiorna.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = mEditEmail.getText().toString();
                String password = mEditPassword.getText().toString();
                String nuova_pass = mEditNewPassword.getText().toString();

                String type = "UpdateUtente";

                if (email.trim().isEmpty() || password.trim().isEmpty() || nuova_pass.trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Inserisci i dati per l'aggiornamento", Toast.LENGTH_SHORT).show();
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

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        if(menuItem.getItemId() == R.id.profilo){
            Intent intent = new Intent(this, ProfiloActivity.class);
            startActivity(intent);
        }
        if(menuItem.getItemId() == R.id.impostazioni){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        if(menuItem.getItemId() == R.id.condividi){
            Intent intent = new Intent(this, ShareActivity.class);
            startActivity(intent);
        }
        if(menuItem.getItemId() == R.id.logout){
            Intent h = new Intent(this, LoginActivity.class);
            startActivity(h);
            finish();
        }
        if(menuItem.getItemId() == R.id.home){
            Intent h = new Intent(this,HomeActivity.class);
            startActivity(h);
        }
        return true;
    }
}
