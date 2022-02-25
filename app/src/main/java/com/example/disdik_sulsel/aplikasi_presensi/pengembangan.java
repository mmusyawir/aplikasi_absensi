package com.example.disdik_sulsel.aplikasi_presensi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class pengembangan extends AppCompatActivity {

    Button kembali;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String user_name_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengembangan);

        getUsernameLocal();

        kembali = findViewById(R.id.kembali);

        ///tombol kembali
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(pengembangan.this, presensi.class);
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
