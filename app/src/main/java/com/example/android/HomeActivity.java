package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    DatabaseHelper db;
    Button mButtonEsplora;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mButtonEsplora = findViewById(R.id.button_esplora);
        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView nvDrawe = findViewById(R.id.menu_laterale);
        setupDrawerContent(nvDrawe);

        mButtonEsplora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent esploraIntent = new Intent(HomeActivity.this, EsploraActivity.class);
                startActivity(esploraIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectItemDrawer(MenuItem menuItem){
        Fragment myFragment = null;

        Class fragmentClass;
        switch (menuItem.getItemId()){
            case R.id.profilo:
                fragmentClass = Profilo.class;
                break;
            case R.id.miei_viaggi:
                fragmentClass = IMieiViaggi.class;
                break;
            case R.id.logout:
                fragmentClass = Logout.class;
                break;
            default:
                fragmentClass = Profilo.class;
        }
        try{
            myFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent,myFragment).commit();
        menuItem.setCheckable(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
        myFragment.onDestroy();
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }
}
