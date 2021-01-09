package com.example.safewomenhappywomen;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class fetchadd extends IntentService {
    private ResultReceiver resultReceiver;

    public fetchadd(){
        super("fetchadd");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            String errorMsg = "";
            resultReceiver = intent.getParcelableExtra(constants.RECEIVER);
            Location location = intent.getParcelableExtra(constants.LOCATION_DATA_EXTRA);
            if(location == null){
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> address = null;
            try{
                address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            }
            catch(Exception exception){
                errorMsg = exception.getMessage();
            }

            if(address == null || address.isEmpty()){
                deliverResulttoReciever(constants.FAILURE_RESULT, errorMsg);
            }
            else{
                Address address1 = address.get(0);
                ArrayList<String> addfragment = new ArrayList<>();
                for(int i=0; i<=address1.getMaxAddressLineIndex(); i++){
                    addfragment.add(address1.getAddressLine(i));
                }
                deliverResulttoReciever(constants.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")), addfragment));
            }
        }
    }

    private void deliverResulttoReciever(int resultCode, String addressMessage){
        Bundle bundle = new Bundle();
        bundle.putString(constants.RESULT_DATA_KEY, addressMessage);
        resultReceiver.send(resultCode, bundle);

    }

}
