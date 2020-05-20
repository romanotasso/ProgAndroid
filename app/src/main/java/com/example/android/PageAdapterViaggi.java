package com.example.android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapterViaggi extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterViaggi(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MonumentoFragmentViaggi();
            case 1:
                return new GastronomiaFragmentViaggi();
            case 2:
                return new HotelBBFragmentViaggi();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
