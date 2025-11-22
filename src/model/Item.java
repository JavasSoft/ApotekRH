package model;
import java.util.List;

public class Item {
    private int IDItem;
    private String Kode;
    private String Nama;
    private double HargaBeli;
    private String Kategori;
    private double Stok;
    private int Aktif;
    private List<ItemDetail> details;
    

    public int getIDItem() { return IDItem; }
    public void setIDItem(int IDItem) { this.IDItem = IDItem; }

    public String getKode() { return Kode; }
    public void setKode(String Kode) { this.Kode = Kode; }

    public String getNama() { return Nama; }
    public void setNama(String Nama) { this.Nama = Nama; }

    public double getHargaBeli() { return HargaBeli; }
    public void setHargaBeli(double HargaBeli) { this.HargaBeli = HargaBeli; }

    public String getKategori() { return Kategori; }
    public void setKategori(String Kategori) { this.Kategori = Kategori; }

    public double getStok() { return Stok; }
    public void setStok(double Stok) { this.Stok = Stok; }


public int getAktif() {
    return Aktif;
}

public void setAktif(int Aktif) {
    this.Aktif = Aktif;
}


    public List<ItemDetail> getDetails() { return details; }
    public void setDetails(List<ItemDetail> details) { this.details = details; }
}
