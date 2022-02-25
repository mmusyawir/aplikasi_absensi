package com.example.disdik_sulsel.aplikasi_presensi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation anim_logo, anime_slogan;
    ImageView logo_aplikasi;
    TextView slogan_aplikasi;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String user_name_key_new = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getUsernameLocal();
        //load animasi
        anim_logo =AnimationUtils.loadAnimation(this,R.anim.anim_logo);
        anime_slogan=AnimationUtils.loadAnimation(this,R.anim.anime_slogan);

        ///load element animasi
        logo_aplikasi = findViewById(R.id.logo_sulsel_cth);
        slogan_aplikasi = findViewById(R.id.slogan_aplikasi);

        //run the anima
        logo_aplikasi.startAnimation(anim_logo);
        slogan_aplikasi.startAnimation(anime_slogan);



    }

    ///// memeriksa data ;ogin trakhir yang tersimpan pada device
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        user_name_key_new = sharedPreferences.getString(username_key,"");
        if (user_name_key_new.isEmpty()) {
            ///////Jiks id user tidak tersimpan pada device
            ////timer untuk pindah activity
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ///pindah Window / activity
                    Intent gogetstarted = new Intent(SplashActivity.this, get_start.class);
                    startActivity(gogetstarted);
                    finish();
                }
            }, 2500);

        }
        else {
            ////timer untuk pindah activity
            //////jika id user tersimpan dalam device
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ///pindah Window / activity
                    Intent godash= new Intent(SplashActivity.this, presensi.class);
                    startActivity(godash);
                    finish();

                }
            }, 2500);
        }
    }
}
