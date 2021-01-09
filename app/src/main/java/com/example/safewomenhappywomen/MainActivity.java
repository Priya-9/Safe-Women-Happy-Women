package com.example.safewomenhappywomen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private EditText output = findViewById(R.id.output);
    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultReceiver = new AddressResultReciever(new Handler());
        Button btn = findViewById(R.id.danger);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("contact", MODE_APPEND);
                String p1 = sh.getString("phone1", "");
                String p2 = sh.getString("phone2", "");
                String e = sh.getString("email", "");
                Toast.makeText(getApplicationContext(), p1+"   "+p2+"   "+e+"  !!!",Toast.LENGTH_SHORT).show();

                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {   ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);     }
                else{
                    getCurrentLocation();   }
            }
        });


        BottomNavigationView bnv = findViewById(R.id.bottomnavi);
        bnv.setSelectedItemId(R.id.home_icon);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.add_contact_icon : startActivity(new Intent(getApplicationContext(), add_contacts.class));
                    overridePendingTransition(0, 0);
                    return true;
                    case R.id.home : return true;
                    case R.id.instruction_icon : startActivity(new Intent(getApplicationContext(), instruction.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
            else {
                Toast.makeText(this, "Permission denied !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation(){
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {   ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);     }

        LocationServices.getFusedLocationProviderClient(MainActivity.this).requestLocationUpdates(locationRequest, new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(MainActivity.this).removeLocationUpdates(this);
                if(locationResult != null && locationResult.getLocations().size()>0){
                    int latestlocationindex = locationResult.getLocations().size()-1;
                    double latitude = locationResult.getLocations().get(latestlocationindex).getLatitude();
                    double longitude = locationResult.getLocations().get(latestlocationindex).getLongitude();

                    Location location = new Location("providerNA");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    fetchaddlatlong(location);
                }
            }
        }, Looper.getMainLooper());
    }

    private void fetchaddlatlong(Location location){
        Intent intent = new Intent(this, fetchadd.class);
        intent.putExtra(constants.RECEIVER, resultReceiver);
        intent.putExtra(constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }
    private class AddressResultReciever extends ResultReceiver{

        AddressResultReciever(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == constants.SUCCESS_RESULT){
                output.setText(resultData.getString(constants.RESULT_DATA_KEY));
            }
            else{
                Toast.makeText(MainActivity.this, resultData.getString(constants.RESULT_DATA_KEY),Toast.LENGTH_SHORT).show();
            }
        }
    }
}