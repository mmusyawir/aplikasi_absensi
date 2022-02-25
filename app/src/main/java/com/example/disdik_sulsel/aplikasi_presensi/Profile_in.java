package com.example.disdik_sulsel.aplikasi_presensi;

public class Profile_in {

    private String nama_sekolah;
    private String alamat;
    private String kode_sekolah;
    private boolean hasil;
    private String kab;

    public Profile_in() {
    }

    public Profile_in(String nama_sekolah, String alamat, String kode_sekolah, boolean hasil,String kab) {
        this.nama_sekolah = nama_sekolah;
        this.alamat = alamat;
        this.kode_sekolah = kode_sekolah;
        this.hasil = hasil;
        this.kab = kab;
    }

    public String getNama_sekolah() {
        return nama_sekolah;
    }

    public void setNama_sekolah(String nama_sekolah) {
        this.nama_sekolah = nama_sekolah;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKode_sekolah() {
        return kode_sekolah;
    }

    public void setKode_sekolah(String kode_sekolah) {
        this.kode_sekolah = kode_sekolah;
    }

    public boolean getHasil() {
        return hasil;
    }

    public void setHasil(boolean hasil) {
        this.hasil = hasil;
    }

    public String getKab() {
        return kab;
    }

    public void setKab(String kab) {
        this.kab = kab;
    }
}
