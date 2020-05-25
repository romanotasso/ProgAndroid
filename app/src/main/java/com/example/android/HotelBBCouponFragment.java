package com.example.android;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotelBBCouponFragment extends Fragment {

    ListView myList;
    Cursor cittaHotel;
    ArrayList<String> hotel;
    DatabaseHelper db;
    ImageButton button;
    public  ArrayList<Bitmap> foto;

    public String coupon, email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monumento_coupon, container, false);

        db = new DatabaseHelper(getContext());
        foto = new ArrayList<Bitmap>();
        email = getActivity().getIntent().getExtras().getString("email");
        coupon = getActivity().getIntent().getExtras().getString("coupon");
        coupon = coupon.substring(0, 1).toUpperCase() + coupon.substring(1).toLowerCase();

        myList = view.findViewById(R.id.listaMonumentoCoupon);
        myList.setVisibility(View.VISIBLE);
        cittaHotel = db.getDataCouponHotelBB(coupon);
        hotel = new ArrayList<String>();

        for(cittaHotel.moveToFirst(); !cittaHotel.isAfterLast(); cittaHotel.moveToNext()){
            hotel.add(cittaHotel.getString(0));
        }

        BackgroudWorkerPhoto backgroudWorkerPhoto = new BackgroudWorkerPhoto();
        backgroudWorkerPhoto.context = getContext();
        backgroudWorkerPhoto.hotel.addAll(hotel);
        backgroudWorkerPhoto.nomeCitta = coupon;
        backgroudWorkerPhoto.execute();
        return view;
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;

        ArrayList<String> nomePunto;


        MyAdapter(Context c, ArrayList<String> hotel) {
            super(c, R.layout.row_utente, R.id.textViewDatiCitta, hotel);
            this.context = c;
            this.nomePunto = hotel;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_utente, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView nome = row.findViewById(R.id.textViewDatiCitta);
            button = row.findViewById(R.id.id);
            images.setImageBitmap(foto.get(position));
            nome.setText(nomePunto.get(position));
            TextView cittaNome = row.findViewById(R.id.textViewCitta);
            cittaNome.setText(coupon);
            final String monumento = nomePunto.get(position);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AggiungiViaggioHotel viaggioHotel = new AggiungiViaggioHotel(getActivity(), context, coupon, email, monumento);
                    viaggioHotel.startLoadingDialog();
                    /*String type = "inserisciViaggio";
                    BackgroudWorker backgroudWorker = new BackgroudWorker(getContext());
                    backgroudWorker.execute(type, email, citta, nomePunto.get(position), "Monumento");
                    db.inserisciViaggio(email, citta, nomePunto.get(position), "Monumento");*/
                }
            });

            return row;
        }
    }

    public class BackgroudWorkerPhoto extends AsyncTask<Void,Void, ArrayList<Bitmap>> {


        Context context;
        String nomeCitta;
        ArrayList<String> hotel = new ArrayList<>();
        final static String url_photoHotel = "http://progandroid.altervista.org/progandorid/FotoHotel/";


        @Override
        public ArrayList<Bitmap> doInBackground(Void... voids) {

            Bitmap immagine;
            String url;
            ArrayList<Bitmap> fotoBack = new ArrayList<Bitmap>();

            try {
                for (int i = 0; i < hotel.size(); i = i + 1) {
                    String nomeHotel =  hotel.get(i).replaceAll(" ","%20");
                    url = url_photoHotel +nomeCitta +nomeHotel+"JPG";
                    InputStream inputStream = new java.net.URL(url).openStream();
                    immagine = BitmapFactory.decodeStream(inputStream);
                    if (!(immagine == null)) {
                        fotoBack.add(i, immagine);
                    }
                }
                return fotoBack;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            if(bitmaps!=null){
                returnFoto(bitmaps);
            }
        }
    }

    public void returnFoto(ArrayList<Bitmap> foto){

        this.foto.addAll(foto);
        MyAdapter adapter = new MyAdapter(getContext(),hotel);
        myList.setAdapter(adapter);

    }

}
