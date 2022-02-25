package com.example.disdik_sulsel.aplikasi_presensi;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ijin_hadir extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView tvDateResult,tvDateResult1;
    private Button btDatePicker,bt_selesai,nomor5,nomor10;
    DatabaseReference reference,reference1, reference2;
    String USERNAME_KEY = "usernamekey";
    String username_key= "";
    String user_name_key_new = "";
    String tanggal_pilih;
    String tgl_akhir,tgl_jadi;
    Date tgl1,tgl2;
    ImageView kembali;
    Button mulai,berakhir,proses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijin_hadir);
        getUsernameLocal();
        mulai=findViewById(R.id.bt_datepicker);
        berakhir=findViewById(R.id.bt_selesai);
        tvDateResult = (TextView) findViewById(R.id.tv_dateresult);
        bt_selesai = (Button) findViewById(R.id.bt_selesai);
        tvDateResult1 = (TextView) findViewById(R.id.tv_dateresult1);
        btDatePicker = (Button) findViewById(R.id.bt_datepicker);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        proses=findViewById(R.id.proses);

        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        bt_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog1();
            }
        });

        proses.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
//                new SweetAlertDialog(ijin_hadir.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Oops...")
//                        .setContentText("Data Tanggal "+tanggal_pilih+"  "+tgl_akhir)
//                        .show();
                Calendar start = Calendar.getInstance();
                start.setTime(tgl1);
                Calendar end = Calendar.getInstance();
                end.setTime(tgl2);

                for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                    tgl_jadi = dateFormatter.format(date.getTime());
//                    // Do your job here with `date`.
//                    System.out.println(date);
                    new SweetAlertDialog(ijin_hadir.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Data Tanggal "+tgl_jadi)
                            .show();
                }
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
                tgl1 = newDate.getTime();
//                lihat_data.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        reference= FirebaseDatabase.getInstance().getReference().child("presensi").child(tanggal_pilih).child(user_name_key_new);
//                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()){
//                                    view_hasil.setVisibility(View.VISIBLE);
//                                    if (dataSnapshot.child("pagi").getValue().toString().equals("Sudah")){
//                                        nomor1.setText("Sukses Presensi");
//                                    } else{
//                                        nomor1.setText("Belum Belum Presensi");
//                                    }
//                                    nomor2.setText(dataSnapshot.child("jam_pagi").getValue().toString());
//                                    nomor3.setText(dataSnapshot.child("lat").getValue().toString()+" / "+dataSnapshot.child("long").getValue().toString());
//                                    nomor4.setText(dataSnapshot.child("status_pagi").getValue().toString());
//                                    if (dataSnapshot.child("pulang").getValue().toString().equals("Sudah")){
//                                        nomor6.setText("Sukses Presensi");
//                                    } else{
//                                        nomor6.setText("Belum Presensi");
//                                    }
//                                    nomor7.setText(dataSnapshot.child("jam_pulang").getValue().toString());
//                                    nomor8.setText(dataSnapshot.child("lat_pulang").getValue().toString()+" / "+dataSnapshot.child("long_pulang").getValue().toString());
//                                    nomor9.setText(dataSnapshot.child("status_pulang").getValue().toString());
//                                    nomor5.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString("foto", dataSnapshot.child("foto").getValue().toString());
//                                            bundle.putString("status_absen", "FOTO PRESENSI PAGI");
//                                            bundle.putString("tanggal", tanggal_pilih);
//                                            Intent gotoprofile = new Intent(getApplicationContext(),popup_v_gambar.class );
//                                            gotoprofile.putExtras(bundle);
//                                            startActivity(gotoprofile);
//                                        }
//                                    });
//                                    nomor10.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString("foto", dataSnapshot.child("foto_pulang").getValue().toString());
//                                            bundle.putString("status_absen", "FOTO PRESENSI SIANG");
//                                            bundle.putString("tanggal", tanggal_pilih);
//                                            Intent gotoprofile = new Intent(getApplicationContext(),popup_v_gambar.class );
//                                            gotoprofile.putExtras(bundle);
//                                            startActivity(gotoprofile);
//                                        }
//                                    });
//                                } else{
//                                    view_hasil.setVisibility(View.GONE);
//                                    new SweetAlertDialog(report_absen.this, SweetAlertDialog.ERROR_TYPE)
//                                            .setTitleText("Oops...")
//                                            .setContentText("Data Tanggal "+tanggal_pilih+" Tidak Ditemukan / Anda Belum Absen pada tanggal ini!")
//                                            .show();
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                });
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showDateDialog1(){

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvDateResult1.setText("Tanggal dipilih : "+dateFormatter.format(newDate.getTime()));
                tgl_akhir = dateFormatter.format(newDate.getTime());
                tgl2 = newDate.getTime();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }




    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        user_name_key_new = sharedPreferences.getString(username_key,"");
    }
}