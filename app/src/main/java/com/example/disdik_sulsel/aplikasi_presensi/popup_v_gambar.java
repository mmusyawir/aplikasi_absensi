package com.example.disdik_sulsel.aplikasi_presensi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class popup_v_gambar extends AppCompatActivity {
    String url, status_absen,tanggal_pilih;
    ImageView foto,kembali;
    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key= "";
    String user_name_key_new = "";
    TextView statusnya;
    WebView picview;
//    Button putar;
//    int clickcount=0;
//    public popup_v_gambar() {
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_v_gambar);
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("foto");
        status_absen = (bundle.getString("status_absen"));
        tanggal_pilih = (bundle.getString("tanggal"));
//        putar=findViewById(R.id.putar);
        statusnya=findViewById(R.id.status);
        foto = findViewById(R.id.foto);
        kembali=findViewById(R.id.kembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        Context ctx = null;
//        Picasso.with(ctx).load(url).into(foto);
        statusnya.setText(status_absen);
        picview = (WebView)findViewById(R.id.picview);
        picview.loadUrl(url);
        picview.getSettings().setUseWideViewPort(true);
        picview.setInitialScale(1);
//        Picasso.get().load(url).centerCrop().fit().into(foto);
//        putar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickcount=clickcount+1;
//                if(clickcount==1){
//                    foto.setRotation(90);
//                }else if(clickcount==2){
//                    foto.setRotation(180);
//                }else if(clickcount==3){
//                    foto.setRotation(270);
//                }else if(clickcount==4){
//                    foto.setRotation(360);
//                    clickcount=0;
//                }
//            }
//        });
//        reference = FirebaseDatabase.getInstance().getReference().child("presensi").child(tanggal_pilih).child(user_name_key_new);
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                status.setText(status_absen);
//                Picasso.get().load(url).centerCrop().fit().into(foto);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        user_name_key_new = sharedPreferences.getString(username_key,"");
    }
}