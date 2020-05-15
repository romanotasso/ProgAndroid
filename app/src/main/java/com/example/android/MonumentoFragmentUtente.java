package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonumentoFragmentUtente extends Fragment {

    ListView myList;
    Cursor cittaMonu;
    //ArrayAdapter adapter;
    ArrayList<String> monumento;
    int images [] = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground};
    DatabaseHelper db;
    Button button;

    public String citta, cittaSearch, cittaLista, cittaDB, email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monumento_utente, container, false);

        db = new DatabaseHelper(getContext());

        //String view1 = getActivity().getIntent().getExtras().getString("cittaDB");
        email = getActivity().getIntent().getExtras().getString("email");
        cittaSearch = getActivity().getIntent().getExtras().getString("cittaSearch");
        cittaLista = getActivity().getIntent().getExtras().getString("cittaLista");
        cittaDB = getActivity().getIntent().getExtras().getString("cittaDB");
        if((cittaSearch == null) && (cittaLista == null)) {
            citta = cittaDB;
        } else if(cittaSearch == null){
            citta = cittaLista;
        } else {
            citta = cittaSearch;
        }

        myList = view.findViewById(R.id.listaMonumento);
        myList.setVisibility(View.VISIBLE);
        cittaMonu = db.getAllDataMonumentiCitta(citta);
        monumento = new ArrayList<String>();

        for(cittaMonu.moveToFirst(); !cittaMonu.isAfterLast(); cittaMonu.moveToNext()){
            monumento.add(cittaMonu.getString(0));
        }

        MyAdapter adapter = new MyAdapter(getContext(), monumento, images);
        myList.setAdapter(adapter);

        /*adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,monumento);
        myList.setAdapter(adapter);*/
        return  view;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        int rImg[];
        ArrayList<String> nomePunto;
        //Bitmap immagine[];

        MyAdapter(Context c, ArrayList<String> gastronomia, int imgs[]) {
            super(c, R.layout.row, R.id.textViewDatiCitta, gastronomia);
            this.context = c;
            this.nomePunto = gastronomia;
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

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String type = "inserisciViaggio";
                    BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                    backgroudWorker.execute(type, email, citta, nomePunto.get(position));
                    db.inserisciViaggio(email, citta, nomePunto.get(position));
                    Intent intent = new Intent(getContext(), IMieiViaggiActivity.class);
                    startActivity(intent);
                }
            });

            return row;
        }
    }

}
