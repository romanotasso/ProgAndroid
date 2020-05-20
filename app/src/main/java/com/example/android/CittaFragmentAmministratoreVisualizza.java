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
public class CittaFragmentAmministratoreVisualizza extends Fragment {

    DatabaseHelper db;
    ListView myList;
    Cursor cittaLista;
    ArrayList<String> citta;
    SwipeRefreshLayout refreshLayout;
    int refresh_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_citta_amministratore_visualizza, container, false);

        db = new DatabaseHelper(getContext());

        myList = view.findViewById(R.id.listaCittaVisualizza);
        myList.setVisibility(View.VISIBLE);
        cittaLista = db.getAllDataCitta();
        citta = new ArrayList<String>();
        refreshLayout = view.findViewById(R.id.swipe);

        for (cittaLista.moveToFirst(); !cittaLista.isAfterLast(); cittaLista.moveToNext()) {
            citta.add(cittaLista.getString(0));
        }

        MyAdapter adapter = new MyAdapter(getContext(), citta/*, images*/);
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

        MyAdapter(Context c, ArrayList<String> citta/*, int imgs[]*/) {
            super(c, R.layout.row, R.id.textViewDatiCitta, citta);
            this.context = c;
            this.nomePunto = citta;
            // this. rImg = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_citta, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);

            ImageButton cancella = row.findViewById(R.id.id);

            //images.setImageResource(rImg[position]);
            nome.setText(nomePunto.get(position));

            final String nomeInteresse = nomePunto.get(position);
            final Context context = getContext();

            cancella.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CancellaDialogCitta cancellaDialog = new CancellaDialogCitta(getActivity(),context, nomeInteresse);
                    cancellaDialog.startLoadingDialog();
                }
            });

            return row;
        }
    }

    public void refreshItems() {
        switch (refresh_count) {
            default:
                cittaLista = db.getAllDataCitta();
                citta = new ArrayList<String>();

                for (cittaLista.moveToFirst(); !cittaLista.isAfterLast(); cittaLista.moveToNext()) {
                    citta.add(cittaLista.getString(0));
                }

                final MyAdapter adapter = new MyAdapter(getContext(), citta/*, images*/);
                myList.setAdapter(adapter);
                break;
        }
    }
}
