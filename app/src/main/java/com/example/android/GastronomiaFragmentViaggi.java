package com.example.android;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GastronomiaFragmentViaggi extends Fragment {

    ListView myList;
    Cursor cittaRist;
    ArrayList<String> gastronomia;
    DatabaseHelper db;
    ImageButton button;
    String citta, email;

    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gastronomia_viaggi, container, false);

        db = new DatabaseHelper(getContext());
        email = getActivity().getIntent().getExtras().getString("email");
        citta = getActivity().getIntent().getExtras().getString("citta");

        refreshLayout = view.findViewById(R.id.swipe);

        myList = view.findViewById(R.id.listaGastronomiaViaggi);
        myList.setVisibility(View.VISIBLE);
        cittaRist = db.getAllViaggiGastronomia(citta, email,"Gastronomia");
        gastronomia = new ArrayList<String>();

        for(cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()){
            gastronomia.add(cittaRist.getString(0));
        }

        MyAdapter adapter = new MyAdapter(getContext(), gastronomia/*, images*/);
        myList.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        return view;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        //int rImg[];
        ArrayList<String> nomePunto;
        //Bitmap immagine[];

        MyAdapter(Context c, ArrayList<String> monumento/*, int imgs[]*/) {
            super(c, R.layout.row, R.id.textViewDatiCitta, monumento);
            this.context = c;
            this.nomePunto = monumento;
            //this. rImg = imgs;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            button = row.findViewById(R.id.id);

            //images.setImageResource(rImg[position]);
            nome.setText(nomePunto.get(position));
            TextView cittaNome = row.findViewById(R.id.textViewCitta);
            cittaNome.setText(citta);
            final String gastronomia = nomePunto.get(position);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CancellaDialogGastronomiaViaggio cancellaDialogMonumento = new CancellaDialogGastronomiaViaggio(getActivity(), context, citta, email, gastronomia);
                    cancellaDialogMonumento.startLoadingDialog();
                }
            });

            return row;
        }
    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaRist = db.getAllViaggiGastronomia(citta, email,"Gastronomia");
                gastronomia = new ArrayList<String>();

                for (cittaRist.moveToFirst(); !cittaRist.isAfterLast(); cittaRist.moveToNext()) {
                    gastronomia.add(cittaRist.getString(0));
                }

                final MyAdapter adapter = new MyAdapter(getContext(), gastronomia/*, images*/);
                myList.setAdapter(adapter);
                break;
        }
    }
}
