package com.example.android;


import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;

public class CercaFragment extends Fragment {


    SearchView mysearchView;
    ListView myList;
    Cursor cittaHome;
    ArrayAdapter adapter;
    DatabaseHelper db;
    ArrayList<String> citta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cerca_fragment, container, false);

        mysearchView = view.findViewById(R.id.searchView);
        myList = view.findViewById(R.id.listView);
        db = new DatabaseHelper(getContext());
        myList.setVisibility(View.GONE);
        cittaHome = db.getAllDataCitta();
        citta = new ArrayList<String>();

        for(cittaHome.moveToFirst(); !cittaHome.isAfterLast(); cittaHome.moveToNext()){
            citta.add(cittaHome.getString(0));
        }

        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,citta);

        myList.setAdapter(adapter);



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

                String text = s;

                if(TextUtils.isEmpty(text)){
                    myList.setVisibility(View.GONE);
                }
                else {
                    adapter.getFilter().filter(text);
                    myList.setVisibility(View.VISIBLE);
                }
                return true;

            }

        }
        );


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String città = citta.get(i);
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
