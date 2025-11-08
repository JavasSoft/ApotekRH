package model;

/**
 * Model class untuk tabel Supplier.
 * @author Epic
 */
public class Supplier {    

    private int IDSupplier;          // Primary Key, Auto Increment
    private String Kode;             // Kode supplier
    private String Nama;             // Nama supplier
    private String Email;            // Email supplier
    private String Alamat;           // Alamat supplier
    private String Telephone;        // Nomor telepon
    private String Kota;             // Kota
    private String Bank;             // Nama bank
    private String NomorRekening;    // Nomor rekening bank
    private String NamaRekening;     // Nama pemilik rekening
    private int Aktif;               // Status aktif (1 = aktif, 0 = tidak aktif)

    // Getter & Setter
    public int getIDSupplier() {
        return IDSupplier;
    }

    public void setIDSupplier(int IDSupplier) {
        this.IDSupplier = IDSupplier;
    }

    public String getKode() {
        return Kode;
    }

    public void setKode(String Kode) {
        this.Kode = Kode;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String Nama) {
        this.Nama = Nama;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
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
