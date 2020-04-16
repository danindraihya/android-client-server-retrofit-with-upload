package com.example.appmahasiswa.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mahasiswa {

   @SerializedName("id")
   Integer pk;

   @SerializedName("nama")
   String nama;

   @SerializedName("alamat")
   String alamat;

   @SerializedName("foto")
   String foto;

    public Mahasiswa(String nama, String alamat, String foto) {
        this.nama = nama;
        this.alamat = alamat;
        this.foto = foto;
    }

    public Integer getPk() {
        return pk;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getFoto() {
        return foto;
    }
}
