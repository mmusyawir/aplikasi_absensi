package com.example.disdik_sulsel.aplikasi_presensi;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;



public class absen_masuk extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener{
    public static final int RequestPermissionCode = 1;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView lati, longi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_masuk);

        ///select api google
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        lati = findViewById(R.id.lati);
        longi = findViewById(R.id.longi);



    }
    @Override
    protected void onStart(){
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop(){
        if (googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("absen_masuk","Koneksi Bermasalah : " + connectionResult.getErrorCode());
    }

    @Override public void onConnectionSuspended(int i) {
        Log.e("absen_masuk", "Tidak ada Koneksi");
    }

    @Override public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        } else {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            lati.setText("Latitude : " + String.valueOf(location.getLatitude()));
                            longi.setText("Longitude : " + String.valueOf(location.getLongitude()));
                        }
                    }
                });
    }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(absen_masuk.this,new String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
    }
}
