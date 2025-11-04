/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Barang {
    private int IDBarang;
    private String Kode;
    private String Nama;
    private int IDKategori;
    private String Satuan;
    private double Beli;
    private double Jual;
    private String Keterangan;
    private String IsAktif;

    // Getters and Setters

    public int getIDBarang() {
        return IDBarang;
    }

    public void setIDBarang(int IDBarang) {
        this.IDBarang = IDBarang;
    }

    public String getKode() {
        return Kode;
    }

    public void setKode(String kode) {
        Kode = kode;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public int getIDKategori() {
        return IDKategori;
    }

    public void setIDKategori(int IDKategori) {
        this.IDKategori = IDKategori;
    }

    public String getSatuan() {
        return Satuan;
    }

    public void setSatuan(String satuan) {
        Satuan = satuan;
    }

    public double getBeli() {
        return Beli;
    }

    public void setBeli(double beli) {
        Beli = beli;
    }

    public double getJual() {
        return Jual;
    }

    public void setJual(double jual) {
        Jual = jual;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public String getIsAktif() {
        return IsAktif;
    }

    public void setIsAktif(String isAktif) {
        IsAktif = isAktif;
    }
}

