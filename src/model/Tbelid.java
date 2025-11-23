package model;

public class Tbelid {
    private int idBeliD;
    private Integer idBeliH;
    private Integer idItemD;
    private double qty;
    private double harga;
    private double diskon;
    private double total;
    private double qtyBase;

    // Getter dan Setter
    public int getIdBeliD() { return idBeliD; }
    public void setIdBeliD(int idBeliD) { this.idBeliD = idBeliD; }

    public Integer getIdBeliH() { return idBeliH; }
    public void setIdBeliH(Integer idBeliH) { this.idBeliH = idBeliH; }

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
