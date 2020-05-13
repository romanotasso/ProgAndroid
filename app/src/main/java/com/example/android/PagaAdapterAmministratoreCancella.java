package com.example.android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagaAdapterAmministratoreCancella extends FragmentPagerAdapter {

    private int numOfTabs;

    public PagaAdapterAmministratoreCancella(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CittaFragmentAmministratoreCancella();
            case 1:
                return new MonumentoFragmentAmministratoreCancella();
            case 2:
                return new GastronomiaFragmentAmministratoreCancella();
            case 3:
                return new HotelBBFragmentAmministratoreCancella();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
