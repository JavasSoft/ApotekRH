package model;

public class Periode {
    private String kode;
    private String tanggalAwal;
    private String tanggalAkhir;
    private String keterangan;
    private String status;
    private boolean aktif;
    private int idperiode;

    public Periode(String kode, String tanggalAwal, String tanggalAkhir, String keterangan, String status, boolean aktif, int idPeriode) {
        this.kode = kode;
        this.tanggalAwal = tanggalAwal;
        this.tanggalAkhir = tanggalAkhir;
        this.keterangan = keterangan;
        this.status = status;
        this.aktif = aktif;
        this.idperiode = idPeriode; // Menyimpan idPeriode ke dalam atribut idperiode
    }

    // Getter dan Setter
    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getTanggalAwal() {
        return tanggalAwal;
    }

    public void setTanggalAwal(String tanggalAwal) {
        this.tanggalAwal = tanggalAwal;
    }

    public String getTanggalAkhir() {
        return tanggalAkhir;
    }

    public void setTanggalAkhir(String tanggalAkhir) {
        this.tanggalAkhir = tanggalAkhir;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public int getIdPeriode() { // Getter untuk idperiode
        return idperiode;
    }

    public void setIdPeriode(int idPeriode) { // Setter untuk idperiode
        this.idperiode = idPeriode;
    }
}
