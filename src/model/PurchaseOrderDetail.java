package model;

public class PurchaseOrderDetail {
    private int IDPoD;                // Unique identifier for the detail
    private int IDPoH;                // Foreign key to the Purchase Order
    private String kode;              // Code of the item
    private int quantity;             // Quantity of the item
    private double price;             // Price of the item
    private double subtotal;          // Subtotal for this item (quantity * price)

    // Constructors
    public PurchaseOrderDetail(int IDPoD, int IDPoH, String kode, int quantity, double price) {
        this.IDPoD = IDPoD;
        this.IDPoH = IDPoH;
        this.kode = kode;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = quantity * price; // Calculate subtotal
    }

    // Getters and Setters
    public int getIDPoD() {
        return IDPoD;
    }

    public void setIDPoD(int IDPoD) {
        this.IDPoD = IDPoD;
    }

    public int getIDPoH() {
        return IDPoH;
    }

    public void setIDPoH(int IDPoH) {
        this.IDPoH = IDPoH;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = quantity * this.price; // Recalculate subtotal
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        this.subtotal = this.quantity * price; // Recalculate subtotal
    }

    public double getSubtotal() {
        return subtotal;
    }

    // Optionally, you can override toString() for better logging
    @Override
    public String toString() {
        return "PurchaseOrderDetail{" +
                "IDPoD=" + IDPoD +
                ", IDPoH=" + IDPoH +
                ", kode='" + kode + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + subtotal +
                '}';
    }
}
