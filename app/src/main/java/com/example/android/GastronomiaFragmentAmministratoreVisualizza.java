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
public class GastronomiaFragmentAmministratoreVisualizza extends Fragment {

    DatabaseHelper db;
    ListView myList;
    Cursor cittaGast;
    ArrayList<String> gastronomia;
    ArrayList<String> citta;
    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gastronomia_amministratore_visualizza, container, false);

        db = new DatabaseHelper(getContext());

        myList = view.findViewById(R.id.listaRistorantiVisualizza);
        myList.setVisibility(View.VISIBLE);
        cittaGast = db.getAllDataGastronomia();
        gastronomia = new ArrayList<String>();
        refreshLayout = view.findViewById(R.id.swipe);
        citta = new ArrayList<String>();

        for (cittaGast.moveToFirst(); !cittaGast.isAfterLast(); cittaGast.moveToNext()) {
            gastronomia.add(cittaGast.getString(0));
        }

        for (cittaGast.moveToFirst(); !cittaGast.isAfterLast(); cittaGast.moveToNext()) {
            citta.add(cittaGast.getString(1));
        }

        MyAdapter adapter = new MyAdapter(getContext(), gastronomia, citta/*, images*/);
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
        ArrayList<String> cittaLista;
        //Bitmap immagine[];

        MyAdapter(Context c, ArrayList<String> gastronomia, ArrayList<String> citta/*, int imgs[]*/) {
            super(c, R.layout.row, R.id.textViewDatiCitta, gastronomia);
            this.context = c;
            this.nomePunto = gastronomia;
            this.cittaLista = citta;
            // this. rImg = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            TextView citta = row.findViewById(R.id.textViewCitta);

            ImageButton cancella = row.findViewById(R.id.id);
            citta.setText(cittaLista.get(position));

            //images.setImageResource(rImg[position]);
            nome.setText(nomePunto.get(position));

            final String nomeInteresse = nomePunto.get(position);
            final Context context = getContext();

            cancella.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CancellaDialogGastronomia cancellaDialog = new CancellaDialogGastronomia(getActivity(), context, nomeInteresse);
                    cancellaDialog.startLoadingDialog();
                }
            });
            return row;
        }
    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaGast = db.getAllDataGastronomia();
                gastronomia = new ArrayList<String>();

                for (cittaGast.moveToFirst(); !cittaGast.isAfterLast(); cittaGast.moveToNext()) {
                    gastronomia.add(cittaGast.getString(0));
                }

                final MyAdapter adapter = new MyAdapter(getContext(), gastronomia, citta/*, images*/);
                myList.setAdapter(adapter);
                break;
        }
    }
}
