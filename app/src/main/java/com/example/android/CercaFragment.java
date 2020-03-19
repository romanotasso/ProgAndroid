package com.example.android;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CercaFragment extends Fragment {

    View alwaysAppearingView;
    SearchView mysearchView;
    ListView myList;
    Cursor citta;
    ArrayAdapter adapter;
    DatabaseHelper db;
    ArrayList<String> citta1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cerca_fragment, container, false);

        mysearchView = view.findViewById(R.id.searchView);
        myList = view.findViewById(R.id.listView);
        db = new DatabaseHelper(getContext());

        citta = db.getAllDataCitta();
        citta1 = new ArrayList<String>();
        for(citta.moveToFirst(); !citta.isAfterLast(); citta.moveToNext()){
            citta1.add(citta.getString(0));
        }

        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,citta1);

        myList.setAdapter(adapter);

        /*for (int i=0;i<citta.size();i++){
           if(db.checkCitta(citta.get(i))){
               db.inserisciCitta(citta.get(i));
           }else if(!db.checkCitta(citta.get(i))){
               break;
           }
        }*/

        mysearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ShareFragment shareFragment = new ShareFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.container_fragment, shareFragment).commit();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String città = citta1.get(i);
                ShareFragment shareFragment = new ShareFragment();

                Bundle bundle = new Bundle();
                bundle.putString("Città selezionata: ", città);
                shareFragment.setArguments(bundle);

                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.container_fragment, shareFragment).commit();

            }
        });

        return view;
    }

}
