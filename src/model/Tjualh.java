package model;

import java.sql.Date;
import java.util.List;

public class Tjualh {
    private int idJualH;
    private String kode;
    private Integer idCust;
    private Date tanggal;
    private String jenisBayar;
    private Integer idDokter;
    private Date jatuhTempo;
    private double subTotal;
    private double diskon;
    private double ppn;
    private double total;
    private String status;
    private String insertUser;
    private java.sql.Timestamp insertTime;
    private String updateUser;
    private List<Tjuald> details;

    // === Tambahkan field untuk Customer dan Dokter ===
    private Customer customer;  
    private Dokter dokter;

    // --- Getter dan Setter existing ---
    public int getIdJualH() { return idJualH; }
    public void setIdJualH(int idJualH) { this.idJualH = idJualH; }

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public Integer getIdCust() { return idCust; }
    public void setIdCust(Integer idCust) { this.idCust = idCust; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public String getJenisBayar() { return jenisBayar; }
    public void setJenisBayar(String jenisBayar) { this.jenisBayar = jenisBayar; }

    public Integer getIdDokter() { return idDokter; }
    public void setIdDokter(Integer idDokter) { this.idDokter = idDokter; }

    public Date getJatuhTempo() { return jatuhTempo; }
    public void setJatuhTempo(Date jatuhTempo) { this.jatuhTempo = jatuhTempo; }

    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }

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

    public List<Tjuald> getDetails() { return details; }
    public void setDetails(List<Tjuald> details) { this.details = details; }

    // === Getter dan Setter untuk Customer dan Dokter ===
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Dokter getDokter() { return dokter; }
    public void setDokter(Dokter dokter) { this.dokter = dokter; }
}
