package com.example.android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapterUtente extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterUtente(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MonumentoFragmentUtente();
            case 1:
                return new GastronomiaFragmentUtente();
            case 2:
                return new HotelBBFragmentUtente();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}
