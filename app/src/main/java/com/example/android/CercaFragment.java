package com.example.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CercaFragment extends Fragment {

    View alwaysAppearingView;
    SearchView mysearchView;
    ListView myList;
    ArrayList<String> list;
    ArrayAdapter adapter;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cerca_fragment, container, false);

        mysearchView = view.findViewById(R.id.searchView);
        myList = view.findViewById(R.id.listView);

        list = new ArrayList<String>();

        list.add("altamura");
        list.add("bari");
        list.add("bologna");
        list.add("firenze");
        list.add("messina");
        list.add("milano");
        list.add("palermo");
        list.add("roma");
        list.add("torino");
        list.add("venezia");

        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,list);
        myList.setAdapter(adapter);

        mysearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter.getFilter().filter(s);

                return false;
            }
        });




        return view;
    }

}
