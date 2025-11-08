package model;

public class Supplier {
    private int IDSupplier;
    private String kode;
    private String bandaUsaha;
    private String namaUsaha;
    private int tempo;
    private String nama;
    private String alamat;
    private String telephone;
    private String kota;
    private String Bank;
    private String NomorRekening;
    private String NamaRekening;
    private String IsAktif;

    // Getter dan Setter
    public int getIdSup() { return IDSupplier; }
    public void setIdSup(int idSup) { this.IDSupplier = idSup; }

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

    public void setEmail(String email) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

