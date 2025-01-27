package com.example.android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapterAmministratoreInserisci extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterAmministratoreInserisci(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CittaFragmentAmministratoreInserisci();
            case 1:
                return new MonumentoFragmentAmministratoreInserisci();
            case 2:
                return new GastronomiaFragmentAmministratoreInserisci();
            case 3:
                return new HotelBBFragmentAmministratoreInserisci();
                default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}
