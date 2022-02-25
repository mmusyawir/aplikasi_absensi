package com.example.disdik_sulsel.aplikasi_presensi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import android.location.LocationListener;


public class absen_in extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    ////Deklarasi Variabel
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker curentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private TextView nilai_jarak, kordinatxx, aku,nilai_stag ;
    private Button selfie,back1;
    private float distance=0;
    String Long,Lat,status_presensi;

    String USERNAME_KEY = "usernamekey";
    String username_key= "";
    String user_name_key_new = "";
    DatabaseReference reference,reference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_in);
        getUsernameLocal();
        ///////menghubungkan variabel dengan objek
        nilai_jarak = findViewById(R.id.nilai_jarak);
        aku = findViewById(R.id.aku);
        kordinatxx = findViewById(R.id.kordinatxx);
        selfie = findViewById(R.id.selfie);
        back1=findViewById(R.id.back1);
        nilai_stag = findViewById(R.id.nilai_stag);
        Bundle bundle = getIntent().getExtras();
        status_presensi = bundle.getString("status");

        ///go to absen_masuk
        selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////bundel digunakan untuk get variabel dan mengirimkan ke halaman tujuan
                Bundle bundle = new Bundle();
                bundle.putString("data1", aku.getText().toString());
                bundle.putString("data2", kordinatxx.getText().toString());
                bundle.putString("datalong", Long);
                bundle.putString("datalat", Lat);
                bundle.putString("status", status_presensi);
                Intent absen_self = new Intent(absen_in.this, w_selfie.class);
                absen_self.putExtras(bundle);
                startActivity(absen_self);
            }
        });
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ////////////Notifkasi Informasi
        new AlertDialog.Builder(this)
                .setTitle("Perhatikan Lokasi Anda !")
                .setMessage("Jika Lokasi tidak sesuai, kembali ke halaman sebelum nya, lalu menekan ulang tombol Absen Masuk, Hingga Sesuai Posisi. \n\nGPS anda harus dalam keadaan ON.")
                .setPositiveButton("Saya Mengerti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("absen_in", "Cek Data Anda");
                    }
                })
                .show();

        ///////memeriksa akses privasi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    ////////Memeriksa ijin untuk mengakses lokasi saat ini
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }
     }
    public boolean checkUserLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);

                    }
                    else
                    {
                        /////JIka ijin akses ditolak
                        Toast.makeText(this,"Ijin Di tolak..", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
        }
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(final Location location) {
        lastlocation = location;
        if (curentUserLocationMarker != null){
            curentUserLocationMarker.remove();
        }
        /////Memberi tanda (mark) pada lokasi layar user
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions= new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Lokasi Anda");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        curentUserLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //////zoom dari ketinggian
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));


        reference= FirebaseDatabase.getInstance().getReference().child("id_pemakai").child(user_name_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {
                /////get data radius from db
                reference1 = FirebaseDatabase.getInstance().getReference().child("jarak");
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot7) { ///dataSnapshot dijadikan unik agare tidak tabrakan dengan yang lain
                        ///deklarasikan variabel untuk get data from db
                        final String radius;
                        radius = dataSnapshot7.child("jarak").getValue().toString();
                        /////konversikan value string ke Double karena jarak di API google type Double
                        double radiusx = Double.parseDouble(radius);
                        ///////Mengambil lat long Sekolah
                        final String lat1,long1;
                        lat1 = dataSnapshot1.child("lat_sekolah").getValue().toString();
                        long1 = dataSnapshot1.child("long_sekolah").getValue().toString();
                        double str1 = Double.parseDouble(lat1);
                        double str2 = Double.parseDouble(long1);
                        // Menampilkan marker Sekolah
                        LatLng kantor = new LatLng(str1,str2); // Lokasi Sekolah
                        final LatLng rumahku = new LatLng(location.getLatitude(), location.getLongitude()); ////Mengambil lokasi saat ini
                        mMap.addMarker(new MarkerOptions().position(kantor).title("Sekolah"));///////Menampilkan nama Marker Sekolah
                        ////Membuat Garis Lurus antar dua titik (Polyline)
                        mMap.addPolyline(
                                new PolylineOptions()
                                        .add(kantor)
                                        .add(rumahku)
                                        .width(19f)
                                        .color(Color.RED));
                        /////Membuat Lingkaran Merah ( Batas Radius Toleransi )
                        mMap.addCircle(
                                new CircleOptions()
                                        .center(kantor)///// Set posisi tengah dari lokasi User
                                        .radius(radiusx) /////Toleransi Lingkaran / Radius Keberadaan dalam sekolah
                                        .strokeWidth(3f) ///////Ketebalan garis Lingkaran radius
                                        .strokeColor(Color.RED)  /////// Warna Lingkaran Radius
                                        .fillColor(Color.argb(70,150,50,50)) //// Ketebalan Warna (Alpha)
                        );
                        ////Menghitung Jarak
                        double latitude=str1;
                        double longitude=str2;
                        /////Lokasi User
                        Location crntLocation=new Location("crntlocation");
                        crntLocation.setLatitude(location.getLatitude());
                        crntLocation.setLongitude(location.getLongitude());
                        /////Lokasi Sekolah
                        Location newLocation=new Location("newlocation");
                        newLocation.setLatitude(latitude);
                        newLocation.setLongitude(longitude);
                        //////Menghitung lokasi antara dua titik dalam Kilometer
                        distance =crntLocation.distanceTo(newLocation) / 1000;
                        /////Menampilkan Pesan Setelah Berhasil Menghitung Jarak
                        nilai_jarak.setText("Berhasil Menghitung Jarak");
                        nilai_stag.setText(String.format("%.3f", new Double(distance))+" Meter");
                        ///////Menampilkan NIlai Total JArak yang berhasil dihitung
                        aku.setText(Double.toString(distance));
                        kordinatxx.setText(String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
                        Long = String.valueOf(location.getLongitude());
                        Lat = String.valueOf(location.getLatitude());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }

    }

    //////Check Permision
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //////function memeriksa data user pada device
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        user_name_key_new = sharedPreferences.getString(username_key,"");
    }
}
