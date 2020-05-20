package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class VisualizzaDatiCMRHActivity extends AppCompatActivity {

    DatabaseHelper db;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapterAmministratoreVisualizzaCMRH pageAdapter;
    TabItem tabMonumento, tabRistoranti, tabHotelBB,tabCitta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_dati_c_m_r_h);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        tabLayout = findViewById(R.id.tabLayoutAmministratoreVisualizza);
        tabMonumento = findViewById(R.id.monumentiAmministatoreVisualizza);
        tabRistoranti = findViewById(R.id.gastronomiaAmministatoreVisualizza);
        tabHotelBB = findViewById(R.id.hotel_bbAmministatoreVisualizza);
        tabCitta = findViewById(R.id.cittaAmministatoreVisualizza);
        viewPager = findViewById(R.id.viewPagerAmministatoreVisualizza);

        pageAdapter = new PageAdapterAmministratoreVisualizzaCMRH(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    tabLayout.setBackgroundColor(ContextCompat.getColor(VisualizzaDatiCMRHActivity.this, R.color.orange));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaDatiCMRHActivity.this, R.color.orange));
                    }
                } else if (tab.getPosition() == 2) {
                    tabLayout.setBackgroundColor(ContextCompat.getColor(VisualizzaDatiCMRHActivity.this, R.color.orange));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaDatiCMRHActivity.this, R.color.orange));
                    }
                } else if (tab.getPosition()==3){
                    tabLayout.setBackgroundColor(ContextCompat.getColor(VisualizzaDatiCMRHActivity.this, R.color.orange));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaDatiCMRHActivity.this, R.color.orange));
                    }
                } else  {
                    tabLayout.setBackgroundColor(ContextCompat.getColor(VisualizzaDatiCMRHActivity.this, R.color.orange));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(VisualizzaDatiCMRHActivity.this, R.color.orange));
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
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

}
