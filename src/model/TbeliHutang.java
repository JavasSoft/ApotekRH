package model;

import java.sql.Date;

public class TbeliHutang {
    private int idHutang;       // primary key
    private int idBeliH;        // header pembelian
    private int idSupplier;     // supplier
    private String noFaktur;    // no faktur pembelian
    private Date tanggal;       // tanggal pembelian
    private Date jatuhTempo;    // jatuh tempo pembayaran
    private double sisaHutang;  // sisa hutang

    // Getter & Setter
    public int getIdHutang() { return idHutang; }
    public void setIdHutang(int idHutang) { this.idHutang = idHutang; }

    public int getIdBeliH() { return idBeliH; }
    public void setIdBeliH(int idBeliH) { this.idBeliH = idBeliH; }

    public int getIdSupplier() { return idSupplier; }
    public void setIdSupplier(int idSupplier) { this.idSupplier = idSupplier; }

    public String getNoFaktur() { return noFaktur; }
    public void setNoFaktur(String noFaktur) { this.noFaktur = noFaktur; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public Date getJatuhTempo() { return jatuhTempo; }
    public void setJatuhTempo(Date jatuhTempo) { this.jatuhTempo = jatuhTempo; }

    public double getSisaHutang() { return sisaHutang; }
    public void setSisaHutang(double sisaHutang) { this.sisaHutang = sisaHutang; }
}
