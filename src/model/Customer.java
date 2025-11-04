/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Epic
 */
public class Customer {    
    private int idCus;          // IDCus: Primary Key, Auto Increment
    private String kode;        // Kode: Kode usaha, tipe varchar(50)
    private String namaUsaha;   // NamaUsaha: Nama usaha, tipe varchar(50)
    private int tempo;          // Tempo: Jangka waktu tempo (integer)
    private String nama;        // Nama: Nama orang yang terkait, tipe varchar(50)
    private String alamat;      // Alamat: Alamat lengkap, tipe varchar(100)
    private String telephone;   // Telephone: No telepon, tipe varchar(50)
    private String kota;        // Kota: Kota usaha, tipe varchar(50)
    private String isAktif;     // IsAktif: Status aktif (Ya/Tidak), tipe enum

    // Getters and setters
    public int getIdCus() {
        return idCus;
    }

    public void setIdCus(int idCus) {
        this.idCus = idCus;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }


    public String getNamaUsaha() {
        return namaUsaha;
    }

    public void setNamaUsaha(String namaUsaha) {
        this.namaUsaha = namaUsaha;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getIsAktif() {
        return isAktif;
    }

    public void setIsAktif(String isAktif) {
        this.isAktif = isAktif;
    }
}
