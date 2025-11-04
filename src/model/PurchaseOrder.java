/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class PurchaseOrder {
    private int idPoH;
    private String kode;
    private int idSupp;
    private String tanggal; // Bisa juga pakai `LocalDateTime`
    private double total;
    private double subTotal;
    private double ppn;
    private String isAktif;
    private String complite;
    private String status;
    private String insertTime; // Bisa juga pakai `LocalDate`
    private String insertUser;
    private String updateUser;

    // Constructor
    public PurchaseOrder(int idPoH, String kode, int idSupp, String tanggal, double total, double subTotal, double ppn,
                         String isAktif, String complite, String status, String insertTime, String insertUser, String updateUser) {
        this.idPoH = idPoH;
        this.kode = kode;
        this.idSupp = idSupp;
        this.tanggal = tanggal;
        this.total = total;
        this.subTotal = subTotal;
        this.ppn = ppn;
        this.isAktif = isAktif;
        this.complite = complite;
        this.status = status;
        this.insertTime = insertTime;
        this.insertUser = insertUser;
        this.updateUser = updateUser;
    }

    // Getters dan Setters
    public int getIdPoH() {
        return idPoH;
    }

    public void setIdPoH(int idPoH) {
        this.idPoH = idPoH;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public int getIdSupp() {
        return idSupp;
    }

    public void setIdSupp(int idSupp) {
        this.idSupp = idSupp;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getPpn() {
        return ppn;
    }

    public void setPpn(double ppn) {
        this.ppn = ppn;
    }

    public String getIsAktif() {
        return isAktif;
    }

    public void setIsAktif(String isAktif) {
        this.isAktif = isAktif;
    }

    public String getComplite() {
        return complite;
    }

    public void setComplite(String complite) {
        this.complite = complite;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getInsertUser() {
        return insertUser;
    }

    public void setInsertUser(String insertUser) {
        this.insertUser = insertUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
