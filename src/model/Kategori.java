package model;

public class Kategori {
    private int id; // Menambahkan ID
    private String kode;
    private String nama;
    private String deskripsi;
    private boolean isAktif;

    public Kategori(int id, String kode, String nama, String deskripsi, boolean isAktif) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.isAktif = isAktif;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public boolean isAktif() {
        return isAktif;
    }

    public void setAktif(boolean isAktif) {
        this.isAktif = isAktif;
    }
}
