package com.example.disdik_sulsel.aplikasi_presensi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class get_start extends AppCompatActivity {

    Button tombol_masuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);

        tombol_masuk = findViewById(R.id.tombol_masuk);

        tombol_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosign = new Intent(get_start.this,Login_activity.class );
                startActivity(gotosign);
                finish();
            }
        });


    }
}
