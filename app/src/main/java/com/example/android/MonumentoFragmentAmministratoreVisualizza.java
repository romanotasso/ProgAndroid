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
public class MonumentoFragmentAmministratoreVisualizza extends Fragment {

    DatabaseHelper db;
    ListView myList;
    Cursor cittaMonu;
    ArrayList<String> monumento;
    ArrayList<String> citta;

    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monumento_amministratore_visualizza, container, false);

        db = new DatabaseHelper(getContext());

        refreshLayout = view.findViewById(R.id.swipe);

        myList = view.findViewById(R.id.listaMonumentoVisualizza);
        myList.setVisibility(View.VISIBLE);
        cittaMonu = db.getAllDataMonumenti();
        monumento = new ArrayList<String>();
        citta = new ArrayList<String>();

        for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
            monumento.add(cittaMonu.getString(0));
        }

        for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
            citta.add(cittaMonu.getString(1));
        }


        final MyAdapter adapter = new MyAdapter(getContext(), monumento, citta/*, images*/);
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

        MyAdapter(Context c, ArrayList<String> monumento, ArrayList<String> citta/*, int imgs[]*/) {
            super(c, R.layout.row, R.id.textViewDatiCitta, monumento);
            this.context = c;
            this.nomePunto = monumento;
            this.cittaLista = citta;
            // this. rImg = imgs;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            TextView citta = row.findViewById(R.id.textViewCitta);
            ImageButton cancella = row.findViewById(R.id.id);
            nome.setText(nomePunto.get(position));
            citta.setText(cittaLista.get(position));

            final String nomeInteresse = nomePunto.get(position);
            final Context context = getContext();

            cancella.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CancellaDialogMonumento cancellaDialogMonumento = new CancellaDialogMonumento(getActivity(),context, nomeInteresse);
                    cancellaDialogMonumento.startLoadingDialog();
                }
            });
            return row;
        }
    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaMonu = db.getAllDataMonumenti();
                monumento = new ArrayList<String>();

                for (cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()) {
                    monumento.add(cittaMonu.getString(0));
                }

                final MyAdapter adapter = new MyAdapter(getContext(), monumento, citta/*, images*/);
                myList.setAdapter(adapter);
                break;
        }
    }
}
