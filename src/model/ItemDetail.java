package model;

public class ItemDetail {
    private int IDDetail;
    private int IDItem;
    private String Satuan;
    private int Konversi;
    private double HargaJual;
    private double LabaPersen;

    public int getIDDetail() { return IDDetail; }
    public void setIDDetail(int IDDetail) { this.IDDetail = IDDetail; }

    public int getIDItem() { return IDItem; }
    public void setIDItem(int IDItem) { this.IDItem = IDItem; }

    public String getSatuan() { return Satuan; }
    public void setSatuan(String Satuan) { this.Satuan = Satuan; }

    public int getKonversi() { return Konversi; }
    public void setKonversi(int Konversi) { this.Konversi = Konversi; }

    public double getHargaJual() { return HargaJual; }
    public void setHargaJual(double HargaJual) { this.HargaJual = HargaJual; }

    public double getLabaPersen() { return LabaPersen; }
    public void setLabaPersen(double LabaPersen) { this.LabaPersen = LabaPersen; }
}
