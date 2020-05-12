package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class InserisciAmministratoreActivity extends AppCompatActivity {

    DatabaseHelper db;
    Button mButtonInserisci;
    EditText mEditCitta;
    EditText mEditHotel;
    EditText mEditMonumento;
    EditText mEditGastronomia;
    EditText mEditEmail;
    EditText mEditPassword;

    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapterAmministratore pageAdapter;
    TabItem tabMonumento, tabRistoranti, tabHotelBB,tabCitta;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserisci_amministratore);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditCitta = findViewById(R.id.edittext_citta);
        mEditGastronomia = findViewById(R.id.edittext_gastronomia);
        mEditHotel = findViewById(R.id.edittext_hotel);
        mEditMonumento = findViewById(R.id.edittext_monumenti);
        mButtonInserisci = findViewById(R.id.button_inserisci);
        db = new DatabaseHelper(this);


        tabLayout = findViewById(R.id.tabLayoutAmministatore);
        tabMonumento = findViewById(R.id.monumentiAmministatore);
        tabRistoranti = findViewById(R.id.gastronomiaAmministatore);
        tabHotelBB = findViewById(R.id.hotel_bbAmministatore);
        tabCitta = findViewById(R.id.cittaAmministatore);
        viewPager = findViewById(R.id.viewPagerAmministatore);

        pageAdapter = new PageAdapterAmministratore(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    tabLayout.setBackgroundColor(ContextCompat.getColor(InserisciAmministratoreActivity.this, R.color.colorAccent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(InserisciAmministratoreActivity.this, R.color.colorAccent));
                    }
                } else if (tab.getPosition() == 2) {
                    tabLayout.setBackgroundColor(ContextCompat.getColor(InserisciAmministratoreActivity.this, R.color.darkGray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(InserisciAmministratoreActivity.this, R.color.darkGray));
                    }
                } else if (tab.getPosition()==3){
                    tabLayout.setBackgroundColor(ContextCompat.getColor(InserisciAmministratoreActivity.this, R.color.colorPrimaryDark));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(InserisciAmministratoreActivity.this, R.color.colorPrimaryDark));
                    }
                } else  {
                    tabLayout.setBackgroundColor(ContextCompat.getColor(InserisciAmministratoreActivity.this, R.color.colorPrimaryDark));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(InserisciAmministratoreActivity.this, R.color.colorPrimaryDark));
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

    //public void onInsert (View view) {




        //String type = "inserimento";

        /*if ((citta.trim().isEmpty()) && ((monumento.trim().isEmpty() && gastronomia.trim().isEmpty() && hotelbb.trim().isEmpty()))) {
            Toast.makeText(getApplicationContext(), "Inserisci i dati", Toast.LENGTH_SHORT).show();
        }else if(!(citta.trim().isEmpty())){
             if(monumento.trim().isEmpty() && gastronomia.trim().isEmpty() && hotelbb.trim().isEmpty()){
                 if (db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())) {
                     hotelbb = "";
                     gastronomia = "";
                     monumento = "";
                     db.inserisciCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                     Toast.makeText(getApplicationContext(), "Città inserita con successo", Toast.LENGTH_SHORT).show();
                     BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                     backgroudWorker.execute(type, citta, monumento, gastronomia, hotelbb);
                 } else {
                     Toast.makeText(getApplicationContext(), "Città gia presente", Toast.LENGTH_SHORT).show();
                 }
             }else if(gastronomia.trim().isEmpty()&&hotelbb.trim().isEmpty()){
                 if(!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())){
                     if(db.checkMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase(),citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())){
                         hotelbb = "";
                         gastronomia = "";
                         db.inserisciMonumento(monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase(), monumento=monumento.substring(0,1).toUpperCase()+monumento.substring(1).toLowerCase(), gastronomia, hotelbb);
                         Toast.makeText(getApplicationContext(), "Monumento inserito con successo", Toast.LENGTH_SHORT).show();
                     }else {
                         Toast.makeText(getApplicationContext(), "Monumento gia presente", Toast.LENGTH_SHORT).show();
                     }
                 }else {
                     Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                 }
             }else if(monumento.trim().isEmpty()&&hotelbb.trim().isEmpty()){
                 if(!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())){
                     if(db.checkGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())){
                         monumento = "";
                         hotelbb = "";
                         db.inserisciGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(), hotelbb);
                         Toast.makeText(getApplicationContext(), "Punto gastronomia inserito con successo", Toast.LENGTH_SHORT).show();
                     }else {
                         Toast.makeText(getApplicationContext(), "Punto gastronomia già presente", Toast.LENGTH_SHORT).show();
                     }
                 }else {
                     Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                 }
             }else if(monumento.trim().isEmpty()&&gastronomia.trim().isEmpty()){
                 if(!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())){
                     if(db.checkHotel(hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase(),citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase())){
                         monumento = "";
                         gastronomia = "";
                         db.inserisciHotelBB(hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia,hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Hotel/BB inserito con successo", Toast.LENGTH_SHORT).show();
                     }else {
                         Toast.makeText(getApplicationContext(), "Hotel/BB  già presente", Toast.LENGTH_SHORT).show();
                     }
                 }else {
                     Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                 }
             }else if(gastronomia.trim().isEmpty()&&((!monumento.trim().isEmpty()&& !(hotelbb.trim().isEmpty())))) {
                 if(!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())){
                     boolean veroMon;
                     boolean veroHot;
                     veroMon = db.checkMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                     veroHot = db.checkHotel(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                     if (veroHot == true && veroMon == true) {
                         gastronomia = "";
                         db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         db.inserisciHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Hotel/BB e Monumento inseriti con successo", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                     } else if (veroHot == true && veroMon == false) {
                         gastronomia = "";
                         monumento = "";
                         db.inserisciHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Hotel/BB inserito con successo, Monumento gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                     } else if (veroHot == false && veroMon == true) {
                         gastronomia = "";
                         hotelbb = "";
                         db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Monumento inserito con successo, Hotel/BB gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia, hotelbb);
                     }else if(veroHot == false && veroMon == false){
                         Toast.makeText(getApplicationContext(), "Monumento,Hotel/B&b già presenti", Toast.LENGTH_SHORT).show();
                     }
                 }else {
                     Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                 }
             }else if(hotelbb.trim().isEmpty()&&(!(monumento.trim().isEmpty()&&gastronomia.trim().isEmpty()))) {
                 if(!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())){
                     boolean veroMon;
                     boolean veroGast;
                     veroMon = db.checkMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                     veroGast = db.checkGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                     if (veroGast == true && veroMon == true) {
                         hotelbb = "";
                         db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         db.inserisciGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Punto Gastronomia e Monumento inseriti con successo", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(), hotelbb);
                     } else if (veroGast == true && veroMon == false) {
                         hotelbb = "";
                         monumento = "";
                         db.inserisciGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Punto Gastronomia inserito con successo, Monumento gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(), hotelbb);
                     } else if (veroGast == false && veroMon == true) {
                         gastronomia = "";
                         hotelbb = "";
                         db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Monumento inserito con successo, Punto Gastronomia gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia, hotelbb);
                     }else if(veroGast == false && veroMon == false){
                         Toast.makeText(getApplicationContext(), "Monumento,Punto Gastronomia già presenti", Toast.LENGTH_SHORT).show();
                     }
                 }else {
                     Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                 }
             }else if(monumento.trim().isEmpty()&&(!(hotelbb.trim().isEmpty()&&gastronomia.trim().isEmpty()))) {
                 if(!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())){
                     boolean veroHot;
                     boolean veroGast;
                     veroHot = db.checkHotel(hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase(),citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                     veroGast = db.checkGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                     if (veroGast == true && veroHot == true) {
                         monumento = "";
                         db.inserisciHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         db.inserisciGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Punto Gastronomia e Hotel/B&B inseriti con successo", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(), hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                     } else if (veroGast == true && veroHot == false) {
                         hotelbb = "";
                         monumento = "";
                         db.inserisciGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Punto Gastronomia inserito con successo, Hotel/B&B gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(), hotelbb);
                     } else if (veroGast == false && veroHot == true) {
                         gastronomia = "";
                         monumento = "";
                         db.inserisciHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Hotel/B&B inserito con successo,Punto Gastronomia gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                     }else if(veroGast == false && veroHot == false){
                         Toast.makeText(getApplicationContext(), "Hotel/B&B,Punto Gastronomia già presenti", Toast.LENGTH_SHORT).show();
                     }
                 }else {
                     Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                 }
             }else if(!(monumento.trim().isEmpty() && hotelbb.trim().isEmpty() && gastronomia.trim().isEmpty())){
                 if(!db.checkCitta(citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase())){
                     boolean veroHot;
                     boolean veroGast;
                     boolean veroMon;
                     veroHot = db.checkHotel(hotelbb=hotelbb.substring(0,1).toUpperCase()+hotelbb.substring(1).toLowerCase(),citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                     veroGast = db.checkGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta=citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                     veroMon = db.checkMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                     if(veroGast==true && veroHot ==true && veroMon == true){
                         db.inserisciHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         db.inserisciGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Monumento ,Punto Gastronomia e Hotel/B&B inseriti con successo", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(), hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                     }else if(veroGast==false && veroHot ==true && veroMon == true){
                         gastronomia = "";
                         db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         db.inserisciHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Hotel/B&B e Monumento inseriti con successo,Punto Gastronomia gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                     }else if(veroGast==true && veroHot ==false && veroMon == true){
                         hotelbb = "";
                         db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         db.inserisciGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Punto Gastronomia e  Monumento inseriti con successo,Hotel/B&B gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(), hotelbb);
                     }else if(veroGast==true && veroHot ==true && veroMon == false){
                         monumento = "";
                         db.inserisciHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         db.inserisciGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Punto Gastronomia e Hotel/B&B inseriti con successo, Monumento gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(), hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                     }else if(veroGast==false && veroHot ==false && veroMon == false){
                         Toast.makeText(getApplicationContext(), "Punto Gastronomia, Hotel/B&B e Monumento già presenti", Toast.LENGTH_SHORT).show();
                     }else if(veroGast==false && veroHot ==false && veroMon == true){
                         gastronomia = "";
                         hotelbb = "";
                         db.inserisciMonumento(monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Monumento inserito con successo,Punto Gastronomia e Hotel/B&B gia presenti", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento = monumento.substring(0, 1).toUpperCase() + monumento.substring(1).toLowerCase(), gastronomia, hotelbb);
                     }else if(veroGast==false && veroHot ==true && veroMon == false){
                         gastronomia = "";
                         monumento = "";
                         db.inserisciHotelBB(hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase(), citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Hotel/B&B inserito con successo,Punto Gastronomia e Monumento gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia, hotelbb = hotelbb.substring(0, 1).toUpperCase() + hotelbb.substring(1).toLowerCase());
                     }else if(veroGast==true && veroHot ==false && veroMon == false){
                         hotelbb = "";
                         monumento = "";
                         db.inserisciGastronomia(gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(),citta= citta.substring(0,1).toUpperCase() + citta.substring(1).toLowerCase());
                         Toast.makeText(getApplicationContext(), "Punto Gastronomia inserito con successo, Monumento e Hotel/B&B  gia presente", Toast.LENGTH_SHORT).show();
                         BackgroudWorker backgroudWorker = new BackgroudWorker(this);
                         backgroudWorker.execute(type, citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase(), monumento, gastronomia=gastronomia.substring(0,1).toUpperCase()+gastronomia.substring(1).toLowerCase(), hotelbb);
                     }
                 }else {
                     Toast.makeText(getApplicationContext(), "Citta non presente", Toast.LENGTH_SHORT).show();
                 }
             }
         }else if(citta.trim().isEmpty()){
             mEditCitta.setError("CAMPO OBBLIGATORIO");
             mEditCitta.requestFocus();

         }*/



    //}
}
