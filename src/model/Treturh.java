/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.util.List;

public class Treturh {
    private int idReturH;
    private String kode;
    private int idCust;
    private Date tanggal;
    private double total;
    private String status;
    private String insertUser;
    private String updateUser;
    private List<Treturd> details;

    // getter & setter
    public int getIdReturH() { return idReturH; }
    public void setIdReturH(int idReturH) { this.idReturH = idReturH; }

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public int getIdCust() { return idCust; }
    public void setIdCust(int idCust) { this.idCust = idCust; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getInsertUser() { return insertUser; }
    public void setInsertUser(String insertUser) { this.insertUser = insertUser; }
    
        public String getUpdateUser() { return updateUser; }
    public void setUpdatetUser(String updateUser) { this.updateUser = updateUser; }

    public List<Treturd> getDetails() { return details; }
    public void setDetails(List<Treturd> details) { this.details = details; }
}
