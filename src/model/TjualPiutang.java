package model;

import java.sql.Date;

public class TjualPiutang {
    private int idPiutang;
    private int idJualH;
    private String noFaktur;
    private Date tanggal;
    private Date jatuhTempo;
    private double sisaPiutang;

    public int getIdPiutang() { return idPiutang; }
    public void setIdPiutang(int idPiutang) { this.idPiutang = idPiutang; }

    public int getIdJualH() { return idJualH; }
    public void setIdJualH(int idJualH) { this.idJualH = idJualH; }

    public String getNoFaktur() { return noFaktur; }
    public void setNoFaktur(String noFaktur) { this.noFaktur = noFaktur; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public Date getJatuhTempo() { return jatuhTempo; }
    public void setJatuhTempo(Date jatuhTempo) { this.jatuhTempo = jatuhTempo; }

    public double getSisaPiutang() { return sisaPiutang; }
    public void setSisaPiutang(double sisaPiutang) { this.sisaPiutang = sisaPiutang; }
}
