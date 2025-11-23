package model;

import java.sql.Date;
import java.util.List;

public class Tbelih {
    private int idBeliH;
    private String kode;
    private Integer idSupplier;
    private Date tanggal;
    private String jenisBayar;
    private Date jatuhTempo;
    private double subTotal;
    private double diskon;
    private double ppn;
    private double total;
    private double nominal;
    private String status;
    private String insertUser;
    private java.sql.Timestamp insertTime;
    private String updateUser;
    private List<Tbelid> details;

    // === Tambahkan field untuk Supplier ===
    private Supplier supplier;

    // --- Getter dan Setter ---
    public int getIdBeliH() { return idBeliH; }
    public void setIdBeliH(int idBeliH) { this.idBeliH = idBeliH; }

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public Integer getIdSupplier() { return idSupplier; }
    public void setIdSupplier(Integer idSupplier) { this.idSupplier = idSupplier; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public String getJenisBayar() { return jenisBayar; }
    public void setJenisBayar(String jenisBayar) { this.jenisBayar = jenisBayar; }

    public Date getJatuhTempo() { return jatuhTempo; }
    public void setJatuhTempo(Date jatuhTempo) { this.jatuhTempo = jatuhTempo; }

    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }

    public double getNominal() { return nominal; }
    public void setNominal(double nominal) { this.nominal = nominal; }

    public double getDiskon() { return diskon; }
    public void setDiskon(double diskon) { this.diskon = diskon; }

    public double getPpn() { return ppn; }
    public void setPpn(double ppn) { this.ppn = ppn; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getInsertUser() { return insertUser; }
    public void setInsertUser(String insertUser) { this.insertUser = insertUser; }

    public java.sql.Timestamp getInsertTime() { return insertTime; }
    public void setInsertTime(java.sql.Timestamp insertTime) { this.insertTime = insertTime; }

    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }

    public List<Tbelid> getDetails() { return details; }
    public void setDetails(List<Tbelid> details) { this.details = details; }

    // === Getter dan Setter untuk Supplier ===
    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }
}
