package com.example.disdik_sulsel.aplikasi_presensi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_activity extends AppCompatActivity {

    Button tombol_login;
    EditText xusername,xpassword;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        tombol_login = findViewById(R.id.tombol_login);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);

        tombol_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ////ubah tombol loading
                tombol_login.setEnabled(false);
                tombol_login.setText("Loading...");

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();


                /////cek username
                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Kolom Username tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    ////ubah tombol loading
                    tombol_login.setEnabled(true);
                    tombol_login.setText("LOGIN");

                }
                else {
                    /////cek password
                    if (password.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Kolom Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        ////ubah tombol loading
                        tombol_login.setEnabled(true);
                        tombol_login.setText("LOGIN");

                    } else {
                        ////cek DB
                        reference = FirebaseDatabase.getInstance().getReference().child("id_pemakai").child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    ////tampilkan pesan sukses login
                                    //Toast.makeText(getApplicationContext(),"Selamat Datang", Toast.LENGTH_SHORT).show();


                                    ///get password from firebase
                                    String password_fb = dataSnapshot.child("password").getValue().toString();

                                    //validate password
                                    if (password.equals(password_fb)){
                                        //save acount to phone
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key,xusername.getText().toString());
                                        editor.apply();
                                        ///pindah window / activity
                                        Intent gotohome = new Intent(Login_activity.this, presensi.class);
                                        startActivity(gotohome);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Password Salah !", Toast.LENGTH_SHORT).show();
                                        ////ubah tombol loading
                                        tombol_login.setEnabled(true);
                                        tombol_login.setText("LOGIN");
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(),"User Name tidak Ditemukan !", Toast.LENGTH_SHORT).show();
                                    ////ubah tombol loading
                                    tombol_login.setEnabled(true);
                                    tombol_login.setText("LOGIN");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }


            }
        });

    }
}
