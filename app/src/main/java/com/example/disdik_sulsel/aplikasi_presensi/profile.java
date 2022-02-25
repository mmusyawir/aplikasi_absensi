package com.example.disdik_sulsel.aplikasi_presensi;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {

    Button kembali, tombol_update, tombol_logout, ubah_foto;
    Uri photo_location;
    Integer photo_max = 1;
    ImageView fotox;
    EditText pass_baru, nama_user;
    TextView nama_layar, nip_layar;
    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String user_name_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fotox = findViewById(R.id.fotox);
        nama_layar = findViewById(R.id.nama_layar);
        nama_user = findViewById(R.id.nama_user);
        pass_baru = findViewById(R.id.pass_baru);
        tombol_update = findViewById(R.id.tombol_update);
        tombol_logout = findViewById(R.id.tombol_logout);
        ubah_foto = findViewById(R.id.ubah_foto);
        nip_layar = findViewById(R.id.nip_layar);
        kembali = findViewById(R.id.kembali);

        getUsernameLocal();
        reference = FirebaseDatabase.getInstance().getReference().child("id_pemakai").child(user_name_key_new);
        storage = FirebaseStorage.getInstance().getReference().child("foto_login").child(user_name_key_new);

        ///reference.addListenerForSingleValueEvent (Hanya Update saja tanpa realtime perubahan)
        reference.addValueEventListener(new ValueEventListener() {  //// addValuEventListener (Update secara realtime data langsung berubah)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nama_layar.setText(dataSnapshot.child("nama").getValue().toString());
                pass_baru.setText(dataSnapshot.child("password").getValue().toString());
                nip_layar.setText(dataSnapshot.child("nip").getValue().toString());
                nama_user.setText(dataSnapshot.child("nama").getValue().toString());
                Picasso.get().load(dataSnapshot.child("foto_login").getValue().toString()).centerCrop().fit().into(fotox);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ///fungsi tombol save
        tombol_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ///update data
                        dataSnapshot.getRef().child("nama").setValue(nama_user.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(pass_baru.getText().toString());
                        dataSnapshot.getRef().child("nip").setValue(nip_layar.getText().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ////pindah window setelah sukses update
                Intent home = new Intent(profile.this, presensi.class);
                startActivity(home);
                //////Cek Validasi apakah foto sudah ada atau belum
                if (photo_location != null) {
                    StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                    storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //String uri_photo = taskSnapshot.getDownloadUrl().toString();
                            //String uri_photo = taskSnapshot.getDownloadUrl();
                            final String uri_photo = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            reference.getRef().child("foto").setValue(uri_photo);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            ////pindah window setelah sukses update
                            Intent home = new Intent(profile.this, presensi.class);
                            startActivity(home);
                        }
                    });
                }
            }
        });


        ///tombol update foto
        ubah_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
        ///tombol kembali
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(profile.this, presensi.class);
                startActivity(home);
                finish();
            }
        });

        ///tombol log_out
        tombol_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////remove session login
                SharedPreferences sharedPreferences= getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, "");
                editor.apply();
                //// move to login
                Intent home1 = new Intent(profile.this, Login_activity.class);
                startActivity(home1);
                finish();
            }
        });


}
    ///// Upload / update foto
    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && requestCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.get().load(photo_location).centerCrop().fit().into(fotox);
        }
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        user_name_key_new = sharedPreferences.getString(username_key,"");
    }
}
