package com.example.disdik_sulsel.aplikasi_presensi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class sukses_absen extends AppCompatActivity {

    ImageView gambar_loading;
    String user_name;
//    private FirebaseAuth mAuth;
    Button kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukses_absen);

        gambar_loading = findViewById(R.id.gambar_loading);
        kembali=findViewById(R.id.kembali);
//        mAuth = FirebaseAuth.getInstance();
//        if(mAuth.getCurrentUser() != null) {
            gambar_loading = (ImageView) findViewById(R.id.gambar_loading);
            Glide.with(getApplicationContext())
                    .load(R.drawable.berhasil)
                    .into(gambar_loading);


            kembali.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(getApplicationContext(),presensi.class));
                }
            });
//        }else{
//            finish();
//            startActivity(new Intent(getApplicationContext(),login.class));
//        }


    }
}