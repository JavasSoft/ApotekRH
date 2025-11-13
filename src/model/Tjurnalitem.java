/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Tjurnalitem {
    private int idJurnal;
    private Date tanggal;
    private Integer idItem;
    private String kodeTrans;
    private String jenisTrans;
    private double qtyMasuk;
    private double qtyKeluar;
    private String satuan;
    private String keterangan;
    private String insertUser;
    private Timestamp insertTime;

    // Getter & Setter
    public int getIdJurnal() { return idJurnal; }
    public void setIdJurnal(int idJurnal) { this.idJurnal = idJurnal; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public Integer getIdItem() { return idItem; }
    public void setIdItem(Integer idItem) { this.idItem = idItem; }

    public String getKodeTrans() { return kodeTrans; }
    public void setKodeTrans(String kodeTrans) { this.kodeTrans = kodeTrans; }

    public String getJenisTrans() { return jenisTrans; }
    public void setJenisTrans(String jenisTrans) { this.jenisTrans = jenisTrans; }

    public double getQtyMasuk() { return qtyMasuk; }
    public void setQtyMasuk(double qtyMasuk) { this.qtyMasuk = qtyMasuk; }

    public double getQtyKeluar() { return qtyKeluar; }
    public void setQtyKeluar(double qtyKeluar) { this.qtyKeluar = qtyKeluar; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public String getInsertUser() { return insertUser; }
    public void setInsertUser(String insertUser) { this.insertUser = insertUser; }

    public Timestamp getInsertTime() { return insertTime; }
    public void setInsertTime(Timestamp insertTime) { this.insertTime = insertTime; }
}
