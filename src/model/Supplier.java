package model;

public class Supplier {
    private int idSup;
    private String kode;
    private String bandaUsaha;
    private String namaUsaha;
    private int tempo;
    private String nama;
    private String alamat;
    private String telephone;
    private String kota;
    private String IsAktif;

    // Getter dan Setter
    public int getIdSup() { return idSup; }
    public void setIdSup(int idSup) { this.idSup = idSup; }

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public String getBandaUsaha() { return bandaUsaha; }
    public void setBandaUsaha(String bandaUsaha) { this.bandaUsaha = bandaUsaha; }

    public String getNamaUsaha() { return namaUsaha; }
    public void setNamaUsaha(String namaUsaha) { this.namaUsaha = namaUsaha; }

    public int getTempo() { return tempo; }
    public void setTempo(int tempo) { this.tempo = tempo; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getKota() { return kota; }
    public void setKota(String kota) { this.kota = kota; }


        public String getIsAktif() {
        return IsAktif;
    }

    public void setIsAktif(String isAktif) {
        IsAktif = isAktif;
    }
}

