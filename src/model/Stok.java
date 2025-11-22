package model;

import java.util.Date;

public class Stok {
    private int id;
    private Date tanggal;
    private int idItem;
    private String kode;
    private String nama;
    private String satuan;
    private double stok;
    private double hargaBeli;
    private double hargaJual;
    private boolean aktif;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    public double getStok() { return stok; }
    public void setStok(double stok) { this.stok = stok; }

    public double getHargaBeli() { return hargaBeli; }
    public void setHargaBeli(double hargaBeli) { this.hargaBeli = hargaBeli; }

    public double getHargaJual() { return hargaJual; }
    public void setHargaJual(double hargaJual) { this.hargaJual = hargaJual; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}
