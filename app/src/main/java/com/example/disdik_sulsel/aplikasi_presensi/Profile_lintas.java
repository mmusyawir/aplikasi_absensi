package com.example.disdik_sulsel.aplikasi_presensi;

public class Profile_lintas {
    private String kab;
    private String kode;
    private boolean hasil;

    public Profile_lintas() {
    }

    public Profile_lintas(String kab, String kode, boolean hasil) {
        this.kab = kab;
        this.kode = kode;
        this.hasil = hasil;
    }

    public String getKab() {
        return kab;
    }

    public void setKab(String kab) {
        this.kab = kab;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public boolean getHasil() {
        return hasil;
    }

    public void setHasil(boolean hasil) {
        this.hasil = hasil;
    }

}

