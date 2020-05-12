package com.example.android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapterAmministratore extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterAmministratore(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CittaFragmentAmmistatore();
            case 1:
                return new MonumentoFragmentAmministatore();
            case 2:
                return new GastronomiaFragmentAmministatore();
            case 3:
                return new HotelBBFragmentAmministatore();
                default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}
