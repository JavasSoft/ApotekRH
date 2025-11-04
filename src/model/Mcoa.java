package model;

public class Mcoa {
    private String kodeAkun;
    private String namaAkun;
    private String saldoNormal;
    private int tipeAkunId;
    private String jenisTrans;
    private String arusKas;
    private String keterangan;
    private boolean dataAktif;
    private boolean subledger;
    private String parentKodeAkun;

    // Constructor
    public Mcoa(String kodeAkun, String namaAkun, String saldoNormal, int tipeAkunId, 
                String jenisTrans, String arusKas, String keterangan, boolean dataAktif, 
                boolean subledger, String parentKodeAkun) {
        this.kodeAkun = kodeAkun;
        this.namaAkun = namaAkun;
        this.saldoNormal = saldoNormal;
        this.tipeAkunId = tipeAkunId;
        this.jenisTrans = jenisTrans;
        this.arusKas = arusKas;
        this.keterangan = keterangan;
        this.dataAktif = dataAktif;
        this.subledger = subledger;
        this.parentKodeAkun = parentKodeAkun;
    }

    // Getters and Setters
    public String getKodeAkun() {
        return kodeAkun;
    }

    public void setKodeAkun(String kodeAkun) {
        this.kodeAkun = kodeAkun;
    }

    public String getNamaAkun() {
        return namaAkun;
    }

    public void setNamaAkun(String namaAkun) {
        this.namaAkun = namaAkun;
    }

    public String getSaldoNormal() {
        return saldoNormal;
    }

    public void setSaldoNormal(String saldoNormal) {
        this.saldoNormal = saldoNormal;
    }

    public int getTipeAkunId() {
        return tipeAkunId;
    }

    public void setTipeAkunId(int tipeAkunId) {
        this.tipeAkunId = tipeAkunId;
    }

    public String getJenisTrans() {
        return jenisTrans;
    }

    public void setJenisTrans(String jenisTrans) {
        this.jenisTrans = jenisTrans;
    }

    public String getArusKas() {
        return arusKas;
    }

    public void setArusKas(String arusKas) {
        this.arusKas = arusKas;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public boolean isDataAktif() {
        return dataAktif;
    }

    public void setDataAktif(boolean dataAktif) {
        this.dataAktif = dataAktif;
    }

    public boolean isSubledger() {
        return subledger;
    }

    public void setSubledger(boolean subledger) {
        this.subledger = subledger;
    }

    public String getParentKodeAkun() {
        return parentKodeAkun;
    }

    public void setParentKodeAkun(String parentKodeAkun) {
        this.parentKodeAkun = parentKodeAkun;
    }

    @Override
    public String toString() {
        return "Mcoa{" +
                "kodeAkun='" + kodeAkun + '\'' +
                ", namaAkun='" + namaAkun + '\'' +
                ", saldoNormal='" + saldoNormal + '\'' +
                ", tipeAkunId=" + tipeAkunId +
                ", jenisTrans='" + jenisTrans + '\'' +
                ", arusKas='" + arusKas + '\'' +
                ", keterangan='" + keterangan + '\'' +
                ", dataAktif=" + dataAktif +
                ", subledger=" + subledger +
                ", parentKodeAkun='" + parentKodeAkun + '\'' +
                '}';
    }
}
