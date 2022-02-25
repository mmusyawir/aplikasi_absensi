package com.example.disdik_sulsel.aplikasi_presensi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class presensi extends AppCompatActivity {

    Button kembali, masuk,pulang,laporan,ijin;
    ImageView fotox;
    TextView nama_user,sekolah, tanggal_absen,jam;
    DatabaseReference reference,reference1,reference2;
    CircleView profile_b;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String user_name_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presensi);

        //get tima and date
        Calendar calendar = Calendar.getInstance();
        final String curentDate = DateFormat.getDateInstance().format(calendar.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        final String curentTime = format.format(calendar.getTime());

        profile_b = findViewById(R.id.profile_b);
        kembali = findViewById(R.id.kembali);
        nama_user = findViewById(R.id.nama_user);
        sekolah = findViewById(R.id.sekolah);
        tanggal_absen=findViewById(R.id.tanggal_absen);
        fotox=findViewById(R.id.fotox);
        masuk=findViewById(R.id.masuk);
        pulang=findViewById(R.id.pulang);
        laporan=findViewById(R.id.laporan);
        ijin=findViewById(R.id.ijin);
        jam=findViewById(R.id.jam);

        getUsernameLocal();
        reference = FirebaseDatabase.getInstance().getReference().child("id_pemakai").child(user_name_key_new);
        final String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        reference1 = FirebaseDatabase.getInstance().getReference().child("presensi").child(tanggal).child(user_name_key_new);
        reference2 = FirebaseDatabase.getInstance().getReference().child("presensi").child(tanggal).child(user_name_key_new);

        ///tombol kembali
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            onBackPressed();
            }
        });

        ijin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ijin_m = new Intent(presensi.this, ijin_hadir.class);
                startActivity(ijin_m);
            }
        });
        ////report
        laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent absen_m = new Intent(presensi.this, report_absen.class);
                startActivity(absen_m);
            }
        });

        ///// masuk ke profile act
        profile_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(presensi.this,profile.class );
                startActivity(gotoprofile);


            }
        });

        //////absen pulang
        pulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        if (dataSnapshot1.exists()){
                            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshotc) {
                                    String absen_pagi;
                                    absen_pagi = dataSnapshotc.child("pulang").getValue().toString();
                                    if (absen_pagi.equals("Sudah")){
                                        new SweetAlertDialog(presensi.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Oops...")
                                                .setContentText("Presensi hari ini telah lengkap!")
                                                .show();
                                    } else{
                                        Bundle bundle = new Bundle();
                                        bundle.putString("status", "2");
                                        Intent absen_m = new Intent(presensi.this, absen_in.class);
                                        absen_m.putExtras(bundle);
                                        startActivity(absen_m);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else{
                            Bundle bundle = new Bundle();
                            bundle.putString("status", "2");
                            Intent absen_m = new Intent(presensi.this, absen_in.class);
                            absen_m.putExtras(bundle);
                            startActivity(absen_m);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        ///go to absen_masuk
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        if (dataSnapshot1.exists()){
                            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshotc) {
                                    String absen_pagi;
                                    absen_pagi = dataSnapshotc.child("pagi").getValue().toString();
                                    if (absen_pagi.equals("Sudah")){
                                      new SweetAlertDialog(presensi.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Oops...")
                                                .setContentText("Anda telah Presensi Pagi!")
                                                .show();
                                    } else{
                                        Bundle bundle = new Bundle();
                                        bundle.putString("status", "1");
                                        Intent absen_m = new Intent(presensi.this, absen_in.class);
                                        absen_m.putExtras(bundle);
                                        startActivity(absen_m);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else{
                            Bundle bundle = new Bundle();
                            bundle.putString("status", "1");
                            Intent absen_m = new Intent(presensi.this, absen_in.class);
                            absen_m.putExtras(bundle);
                            startActivity(absen_m);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        ////////tampilin data FB
        reference.addValueEventListener(new ValueEventListener() {  //// addValuEventListener (Update secara realtime data langsung berubah)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nama_user.setText(dataSnapshot.child("nama").getValue().toString());
                sekolah.setText(dataSnapshot.child("nama_sek").getValue().toString());
                Picasso.get().load(dataSnapshot.child("foto_login").getValue().toString()).centerCrop().fit().into(fotox);
                tanggal_absen.setText(curentDate);
                jam.setText(curentTime);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






















    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        user_name_key_new = sharedPreferences.getString(username_key,"");
    }
}