package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class FetchAddressIntentService extends IntentService {

    private ResultReceiver resultReceiver;
    EditText t;
    DatabaseHelper db = new DatabaseHelper(this);

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            String errorMessage = "";
            resultReceiver = intent.getParcelableExtra(Costanti.RECEIVER);
            Location location = intent.getParcelableExtra(Costanti.LOCATION_DATA_EXTRA);
            if (location == null){
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }
            if (addresses == null || addresses.isEmpty()){
                deliverResultToReceiver(Costanti.FAILURE_RESULT, errorMessage);
            } else {
                String address = addresses.get(0).getLocality();
                ArrayList<String> addressFragments = new ArrayList<>();
                addressFragments.add(address);
                deliverResultToReceiver(Costanti.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")), addressFragments));
            }

        }
    }

    private void deliverResultToReceiver(int resultCode, String addressMessage){
        Bundle bundle = new Bundle();
        bundle.putString(Costanti.RESULT_DATA_KEY, addressMessage);
        resultReceiver.send(resultCode, bundle);
    }
}
