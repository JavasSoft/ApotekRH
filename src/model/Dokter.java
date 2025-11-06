/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Epic
 */
public class Dokter {    
    private int IDDokter;          // IDDokter: Primary Key, Auto Increment
    private String kode;        // Kode: Kode usaha, tipe varchar(50)
    private String Nama;   // NamaUsaha: Nama usaha, tipe varchar(50)
    // Tempo: Jangka waktu tempo (integer)
    private String Email;        // Nama: Nama orang yang terkait, tipe varchar(50)
    private String Alamat;      // Alamat: Alamat lengkap, tipe varchar(100)
    private String Telephone;   // Telephone: No telepon, tipe varchar(50)
    private String Kota;        // Kota: Kota usaha, tipe varchar(50)
    private String Bank;        // Kota: Kota usaha, tipe varchar(50)
    private String NomorRekening;        // Kota: Kota usaha, tipe varchar(50)
    private String NamaRekening;        // Kota: Kota usaha, tipe varchar(50)
    private int Aktif;     // IsAktif: Status aktif (Ya/Tidak), tipe enum

    // Getters and setters
    public int getIDDokter() {
        return IDDokter;
    }

    public void setIDDokter(int IDDokter) {
        this.IDDokter = IDDokter;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }


    public String getNama() {
        return Nama;
    }

    public void setNama(String Nama) {
        this.Nama = Nama;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String Alamat) {
        this.Alamat = Alamat;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

    public String getKota() {
        return Kota;
    }

    public void setKota(String Kota) {
        this.Kota = Kota;
    }
    
    public String getBank() {
        return Bank;
    }

    public void setBank(String Bank) {
        this.Bank = Bank;
    }
    
       public String getNomorRekening() {
        return NomorRekening;
    }

    public void setNomorRekening(String NomorRekening) {
        this.NomorRekening = NomorRekening;
    }
    
        public String getNamaRekening() {
        return NamaRekening;
    }

    public void setNamaRekening(String NamaRekening) {
        this.NamaRekening = NamaRekening;
    }



public int getAktif() {
    return Aktif;
}

public void setAktif(int Aktif) {
    this.Aktif = Aktif;
}

}
