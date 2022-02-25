package com.example.disdik_sulsel.aplikasi_presensi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class report_absen extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView tvDateResult;
    private Button btDatePicker,lihat_data,nomor5,nomor10;
    Dialog myDialog;
    LinearLayout view_hasil;
    TextView nomor1,nomor2,nomor3,nomor4,nomor6,nomor7,nomor8,nomor9;
    DatabaseReference reference,reference1, reference2;
    String USERNAME_KEY = "usernamekey";
    String username_key= "";
    String user_name_key_new = "";
    String tanggal_pilih;
    ImageView kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_absen);
        getUsernameLocal();
        myDialog = new Dialog(this);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        view_hasil=findViewById(R.id.view_hasil);
        tvDateResult = (TextView) findViewById(R.id.tv_dateresult);
        btDatePicker = (Button) findViewById(R.id.bt_datepicker);
        kembali=findViewById(R.id.kembali);
        nomor1=findViewById(R.id.nomor1);
        nomor2=findViewById(R.id.nomor2);
        nomor3=findViewById(R.id.nomor3);
        nomor4=findViewById(R.id.nomor4);
        nomor6=findViewById(R.id.nomor6);
        nomor7=findViewById(R.id.nomor7);
        nomor8=findViewById(R.id.nomor8);
        nomor9=findViewById(R.id.nomor9);
        nomor5=findViewById(R.id.nomor5);
        nomor10=findViewById(R.id.nomor10);
        lihat_data=findViewById(R.id.lihat_data);

        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_hasil.setVisibility(View.GONE);
                lihat_data.setVisibility(View.GONE);
                showDateDialog();
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvDateResult.setText("Tanggal dipilih : "+dateFormatter.format(newDate.getTime()));
                tanggal_pilih = dateFormatter.format(newDate.getTime());
                lihat_data.setVisibility(View.VISIBLE);
                lihat_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference= FirebaseDatabase.getInstance().getReference().child("presensi").child(tanggal_pilih).child(user_name_key_new);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    view_hasil.setVisibility(View.VISIBLE);
                                    if (dataSnapshot.child("pagi").getValue().toString().equals("Sudah")){
                                        nomor1.setText("Sukses Presensi");
                                    } else{
                                        nomor1.setText("Belum Belum Presensi");
                                    }
                                    nomor2.setText(dataSnapshot.child("jam_pagi").getValue().toString());
                                    nomor3.setText(dataSnapshot.child("lat").getValue().toString()+" / "+dataSnapshot.child("long").getValue().toString());
                                    nomor4.setText(dataSnapshot.child("status_pagi").getValue().toString());
                                    if (dataSnapshot.child("pulang").getValue().toString().equals("Sudah")){
                                        nomor6.setText("Sukses Presensi");
                                    } else{
                                        nomor6.setText("Belum Presensi");
                                    }
                                    nomor7.setText(dataSnapshot.child("jam_pulang").getValue().toString());
                                    nomor8.setText(dataSnapshot.child("lat_pulang").getValue().toString()+" / "+dataSnapshot.child("long_pulang").getValue().toString());
                                    nomor9.setText(dataSnapshot.child("status_pulang").getValue().toString());
                                    nomor5.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("foto", dataSnapshot.child("foto").getValue().toString());
                                            bundle.putString("status_absen", "FOTO PRESENSI PAGI");
                                            bundle.putString("tanggal", tanggal_pilih);
                                            Intent gotoprofile = new Intent(getApplicationContext(),popup_v_gambar.class );
                                            gotoprofile.putExtras(bundle);
                                            startActivity(gotoprofile);
                                        }
                                    });
                                    nomor10.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("foto", dataSnapshot.child("foto_pulang").getValue().toString());
                                            bundle.putString("status_absen", "FOTO PRESENSI SIANG");
                                            bundle.putString("tanggal", tanggal_pilih);
                                            Intent gotoprofile = new Intent(getApplicationContext(),popup_v_gambar.class );
                                            gotoprofile.putExtras(bundle);
                                            startActivity(gotoprofile);
                                        }
                                    });
                                } else{
                                    view_hasil.setVisibility(View.GONE);
                                       new SweetAlertDialog(report_absen.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Data Tanggal "+tanggal_pilih+" Tidak Ditemukan / Anda Belum Absen pada tanggal ini!")
                                        .show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        user_name_key_new = sharedPreferences.getString(username_key,"");
    }
}