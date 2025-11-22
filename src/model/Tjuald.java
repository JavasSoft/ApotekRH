/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Tjuald {
    private int idJualD;
    private Integer idJualH;
    private Integer idItemD;
    private double qty;
    private double harga;
    private double diskon;
    private double total;
    private double qtyBase;

    // Getter dan Setter
    public int getIdJualD() { return idJualD; }
    public void setIdJualD(int idJualD) { this.idJualD = idJualD; }

    public Integer getIdJualH() { return idJualH; }
    public void setIdJualH(Integer idJualH) { this.idJualH = idJualH; }

    public Integer getIdItemD() { return idItemD; }
    public void setIdItemD(Integer idItemD) { this.idItemD = idItemD; }

    public double getQty() { return qty; }
    public void setQty(double qty) { this.qty = qty; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
    
    public double getDiskon() { return diskon; }
    public void setDiskon(double diskon) { this.diskon = diskon; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public double getQtyBase() { return qtyBase; }
    public void setQtyBase(double qtyBase) { this.qtyBase = qtyBase; }
}
