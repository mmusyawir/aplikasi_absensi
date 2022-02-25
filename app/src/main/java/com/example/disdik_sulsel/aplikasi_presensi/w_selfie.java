package com.example.disdik_sulsel.aplikasi_presensi;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class w_selfie extends AppCompatActivity {

    TextView nomor1,nomor2;
    String USERNAME_KEY = "usernamekey";
    String username_key= "";
    String user_name_key_new = "";
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage,gambar_loading;
    Button cameraBtn,galleryBtn;
    String currentPhotoPath,Long,Lat,Jarak,status;
    StorageReference storageReference;
    DatabaseReference mdatabase,reference,reference1,reference2,reference3;
    TextView teks_tunggu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_selfie);
        getUsernameLocal();
        nomor1=findViewById(R.id.nomor1);
        nomor2=findViewById(R.id.nomor2);
        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        gambar_loading = findViewById(R.id.gambar_loading);
        teks_tunggu = findViewById(R.id.teks_tunggu);
        gambar_loading = (ImageView)findViewById(R.id.gambar_loading);
        Glide.with(w_selfie.this)
                .load(R.drawable.loading1)
                .into(gambar_loading);
        storageReference = FirebaseStorage.getInstance().getReference();
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

            Bundle bundle = getIntent().getExtras();
            Long = bundle.getString("datalong");
            Lat = bundle.getString("datalat");
            Jarak= (bundle.getString("data1"));
            status= (bundle.getString("status"));
            String Asd = (bundle.getString("data1"));
            String asdf = Asd.substring(0,5);
            nomor1.setText(asdf+" KM");
            nomor2.setText(bundle.getString("data2"));
    }
    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "Membutuhkan Ijin Akses ke Camera!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                uploadImageToFirebase(f.getName(), contentUri);
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                selectedImage.setImageURI(contentUri);
                uploadImageToFirebase(imageFileName, contentUri);
            }
        }
    }
    private void uploadImageToFirebase(String name, Uri contentUri) {
        gambar_loading.setVisibility(View.VISIBLE);
        teks_tunggu.setVisibility(View.VISIBLE);
        final String bln_folder = new SimpleDateFormat("MMM-yyyy", Locale.getDefault()).format(new Date());
        final StorageReference image = storageReference.child(bln_folder+"/"+ name);
        if (status.equals("1")){
            image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String path_foto = uri.toString();
                            final String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            reference = FirebaseDatabase.getInstance().getReference().child("presensi").child(tanggal).child(user_name_key_new);
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                    final String jam = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                    dataSnapshot.getRef().child("foto").setValue(path_foto);
                                    dataSnapshot.getRef().child("long").setValue(Long);
                                    dataSnapshot.getRef().child("lat").setValue(Lat);
                                    dataSnapshot.getRef().child("pagi").setValue("Sudah");
                                    dataSnapshot.getRef().child("pulang").setValue("Belum");
                                    dataSnapshot.getRef().child("tanggal").setValue(tanggal);
                                    dataSnapshot.getRef().child("jam_pagi").setValue(jam);
                                    dataSnapshot.getRef().child("jarak").setValue(Jarak+" Meter");
                                    ////rule pulang
                                    dataSnapshot.getRef().child("foto_pulang").setValue("https://firebasestorage.googleapis.com/v0/b/pendidikan-andalan.appspot.com/o/68986859-no-user-sign-illustration-.jpg?alt=media&token=a9eaaa2a-eb9f-4f91-8e04-3707d855dd55");
                                    dataSnapshot.getRef().child("long_pulang").setValue("-----");
                                    dataSnapshot.getRef().child("lat_pulang").setValue("-----");
                                    dataSnapshot.getRef().child("pulang").setValue("Belum");
                                    dataSnapshot.getRef().child("tanggal_pulang").setValue("-----");
                                    dataSnapshot.getRef().child("jam_pulang").setValue("-----");
                                    dataSnapshot.getRef().child("jarak_pulang").setValue("-----");
                                    dataSnapshot.getRef().child("status_pulang").setValue("-----");
                                    final double str1 = Double.parseDouble(Jarak);
//                                    reference3=FirebaseDatabase.getInstance().getReference().child("waktu");
//                                    reference3.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot7) {
//                                           String timex = dataSnapshot7.child("jam_masuk").getValue().toString();
////                                            String jam1 = new SimpleDateFormat(timex).format(new Date());
////                                            String jam2 = new SimpleDateFormat(jam).format(new Date());
//                                            Integer qq = Integer.parseInt(timex);
//                                            Integer ww = Integer.parseInt(jam);
////                                            Timestamp tt = Timestamp.valueOf(timex);
////                                            Timestamp yy = Timestamp.valueOf(jam);
////                                           if (ww > qq){
//                                               dataSnapshot.getRef().child("waktu_absen").setValue(ww);
////                                           } else {
////                                               dataSnapshot.getRef().child("waktu_absen").setValue("Tepat Waktu");
////                                           }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });

                                    reference1=FirebaseDatabase.getInstance().getReference().child("jarak");
                                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot9) {
                                            String njarak = dataSnapshot9.child("jarak").getValue().toString();
                                            double str2 = Double.parseDouble(njarak);
                                            if (str1 >= str2 ){
                                                dataSnapshot.getRef().child("status_pagi").setValue("Diluar Area Kantor");
                                            } else {
                                                dataSnapshot.getRef().child("status_pagi").setValue("Didalam Area Kantor");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            mdatabase = FirebaseDatabase.getInstance().getReference().child("id_pemakai").child(user_name_key_new);
                            mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshotc) {
                                    reference2=FirebaseDatabase.getInstance().getReference().child("presensi").child(tanggal).child(user_name_key_new);
                                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                            dataSnapshot1.getRef().child("nip").setValue(dataSnapshotc.child("nip").getValue().toString());
                                            dataSnapshot1.getRef().child("nama").setValue(dataSnapshotc.child("nama").getValue().toString());
                                            dataSnapshot1.getRef().child("kab").setValue(dataSnapshotc.child("kabupaten").getValue().toString());
                                            dataSnapshot1.getRef().child("nama_sek").setValue(dataSnapshotc.child("nama_sek").getValue().toString());
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Intent i = new Intent(w_selfie.this, sukses_absen.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
//                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        }
                    });

//                Toast.makeText(take_foto.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(w_selfie.this, "Upload Gagal, Ulangi!.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String path_foto = uri.toString();
                            final String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            reference = FirebaseDatabase.getInstance().getReference().child("presensi").child(tanggal).child(user_name_key_new);
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String jam = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                    dataSnapshot.getRef().child("foto_pulang").setValue(path_foto);
                                    dataSnapshot.getRef().child("long_pulang").setValue(Long);
                                    dataSnapshot.getRef().child("lat_pulang").setValue(Lat);
                                    dataSnapshot.getRef().child("pulang").setValue("Sudah");
                                    dataSnapshot.getRef().child("tanggal_pulang").setValue(tanggal);
                                    dataSnapshot.getRef().child("jam_pulang").setValue(jam);
                                    dataSnapshot.getRef().child("jarak_pulang").setValue(Jarak+" Meter");
                                    double str1 = Double.parseDouble(Jarak);
                                    if (str1 >= 0.100 ){
                                        dataSnapshot.getRef().child("status_pulang").setValue("Diluar Area Kantor");
                                    } else {
                                        dataSnapshot.getRef().child("status_pulang").setValue("Didalam Area Kantor");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            mdatabase = FirebaseDatabase.getInstance().getReference().child("id_pemakai").child(user_name_key_new);
                            mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshotc) {
                                    reference2=FirebaseDatabase.getInstance().getReference().child("presensi").child(tanggal).child(user_name_key_new);
                                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                            dataSnapshot1.getRef().child("nip").setValue(dataSnapshotc.child("nip").getValue().toString());
                                            dataSnapshot1.getRef().child("nama").setValue(dataSnapshotc.child("nama").getValue().toString());
                                            dataSnapshot1.getRef().child("kab").setValue(dataSnapshotc.child("kabupaten").getValue().toString());
                                            dataSnapshot1.getRef().child("nama_sek").setValue(dataSnapshotc.child("nama_sek").getValue().toString());
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Intent i = new Intent(w_selfie.this, sukses_absen.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
//                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        }
                    });

//                Toast.makeText(take_foto.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(w_selfie.this, "Upload Gagal, Ulangi!.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }



    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir     /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.disdik_sulsel.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    public void onBackPressed () {
    }
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return true;
        }
        return false;
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        user_name_key_new = sharedPreferences.getString(username_key,"");
    }
}
