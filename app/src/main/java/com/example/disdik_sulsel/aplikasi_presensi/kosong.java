package com.example.disdik_sulsel.aplikasi_presensi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class kosong extends AppCompatActivity {
    Button kembali;
    TextView pesanx;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String user_name_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kosong);

        getUsernameLocal();

        kembali = findViewById(R.id.kembali);
        pesanx = findViewById(R.id.pesanx);

        new AlertDialog.Builder(this)
                .setTitle("INFORMASI !")
                .setMessage("Untuk menu Presensi Menggunakan e-Panrita Optima. \n\n Dalam tahapan pengembangan.")
                .setPositiveButton("Saya Mengerti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("kosong", "Informasi Menu");
                    }
                })
                .show();

        ///tombol kembali
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(kosong.this, presensi.class);
                startActivity(home);
                finish();
            }
        });


    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        user_name_key_new = sharedPreferences.getString(username_key,"");
    }
}
