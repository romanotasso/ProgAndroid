package com.example.android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapterAmministratoreVisualizzaCMRH extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterAmministratoreVisualizzaCMRH(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CittaFragmentAmministratoreVisualizza();
            case 1:
                return new MonumentoFragmentAmministratoreVisualizza();
            case 2:
                return new GastronomiaFragmentAmministratoreVisualizza();
            case 3:
                return new HotelBBFragmentAmministratoreVisualizza();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
