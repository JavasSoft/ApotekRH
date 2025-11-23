package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Stok;

public class TStokDAO {

    private Connection conn;

    public TStokDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertBatchWithJurnal(List<Stok> list, String currentUser) throws SQLException {
        conn.setAutoCommit(false);

        String sqlStok = "INSERT INTO tstok (IDItem, Kode, Tanggal, Nama, Satuan, Stok, HargaBeli, HargaJual, aktif) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlJurnal = "INSERT INTO tjurnalitem (Tanggal, IDItem, KodeTrans, JenisTrans, QtyMasuk, QtyKeluar, Satuan, Keterangan, InsertUser) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlUpdateMitem = "UPDATE mitem SET Stok=?, HargaBeli=? WHERE IDItem=?";
        String sqlUpdateMitemd = "UPDATE mitemd SET HargaJual=? WHERE IDItem=? AND Satuan=?";

        try (PreparedStatement psStok = conn.prepareStatement(sqlStok);
             PreparedStatement psJurnal = conn.prepareStatement(sqlJurnal);
             PreparedStatement psUpdateMitem = conn.prepareStatement(sqlUpdateMitem);
             PreparedStatement psUpdateMitemd = conn.prepareStatement(sqlUpdateMitemd)) {

            for (Stok t : list) {
                // --- Insert ke tstok ---
                psStok.setInt(1, t.getIdItem());
                psStok.setString(2, t.getKode());
                psStok.setDate(3, new java.sql.Date(t.getTanggal().getTime()));
                psStok.setString(4, t.getNama());
                psStok.setString(5, t.getSatuan());
                psStok.setDouble(6, t.getStok());
                psStok.setDouble(7, t.getHargaBeli());
                psStok.setDouble(8, t.getHargaJual());
                psStok.setBoolean(9, t.isAktif());
                psStok.addBatch();

                // --- Insert ke tjurnalitem ---
                psJurnal.setDate(1, new java.sql.Date(t.getTanggal().getTime()));
                psJurnal.setInt(2, t.getIdItem());
                psJurnal.setString(3, t.getKode());
                psJurnal.setString(4, "StokAwal");
                psJurnal.setDouble(5, t.getStok());
                psJurnal.setDouble(6, 0.0);
                psJurnal.setString(7, t.getSatuan());
                psJurnal.setString(8, "Stok Awal " + t.getKode());
                psJurnal.setString(9, currentUser);
                psJurnal.addBatch();

                // --- Ambil konversi dari mitemd ---
                double konversi = 1.0;
                try (PreparedStatement psKonv = conn.prepareStatement(
                        "SELECT Konversi FROM mitemd WHERE IDItem=? AND Satuan=?")) {
                    psKonv.setInt(1, t.getIdItem());
                    psKonv.setString(2, t.getSatuan());
                    ResultSet rs = psKonv.executeQuery();
                    if (rs.next()) konversi = rs.getDouble("Konversi");
                }

                // --- Hitung stok baru sesuai satuan ---
                double stokBaru;
                try (PreparedStatement psCekSatuan = conn.prepareStatement(
                        "SELECT SatuanBesar, Satuan FROM mitemd WHERE IDItem=? LIMIT 1")) {
                    psCekSatuan.setInt(1, t.getIdItem());
                    ResultSet rsSatuan = psCekSatuan.executeQuery();
                    if (rsSatuan.next()) {
                        String satuanBesar = rsSatuan.getString("SatuanBesar");
                        String satuanKecil = rsSatuan.getString("Satuan");
                        if (t.getSatuan().equalsIgnoreCase(satuanBesar)) {
                            stokBaru = t.getStok() * konversi;
                        } else {
                            stokBaru = t.getStok() / konversi;
                        }
                    } else {
                        stokBaru = t.getStok();
                    }
                }

                // --- Update mitem (Stok & HargaBeli) ---
                psUpdateMitem.setDouble(1, stokBaru);
                psUpdateMitem.setDouble(2, t.getHargaBeli());
                psUpdateMitem.setInt(3, t.getIdItem());
                psUpdateMitem.addBatch();

                // --- Update mitemd (HargaJual per satuan) ---
                psUpdateMitemd.setDouble(1, t.getHargaJual());
                psUpdateMitemd.setInt(2, t.getIdItem());
                psUpdateMitemd.setString(3, t.getSatuan());
                psUpdateMitemd.addBatch();
            }

            // --- Eksekusi batch ---
            psStok.executeBatch();
            psJurnal.executeBatch();
            psUpdateMitem.executeBatch();
            psUpdateMitemd.executeBatch();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    public List<Stok> getAll() throws SQLException {
    List<Stok> list = new ArrayList<>();

    String sql = 
    "SELECT tstok.*, mitem.Kode AS KodeItem " +
    "FROM tstok " +
    "LEFT JOIN mitem ON tstok.IDItem = mitem.IDItem " +
    "ORDER BY tstok.Tanggal, tstok.IDStok";


    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Stok s = new Stok();
            s.setId(rs.getInt("IDStok"));
            s.setIdItem(rs.getInt("IDItem"));
            s.setKode(rs.getString("Kode"));
            s.setKodeItem(rs.getString("KodeItem"));
            s.setTanggal(rs.getDate("Tanggal"));
            s.setNama(rs.getString("Nama"));
            s.setSatuan(rs.getString("Satuan"));
            s.setStok(rs.getDouble("Stok"));
            s.setHargaBeli(rs.getDouble("HargaBeli"));
            s.setHargaJual(rs.getDouble("HargaJual"));
            s.setAktif(rs.getBoolean("Aktif"));

            list.add(s);
        }
    }

    return list;
    }
    
    public String getLastKode(String prefix) throws SQLException { 
    String sql = "SELECT Kode FROM tstok WHERE Kode LIKE ? ORDER BY Kode DESC LIMIT 1";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, prefix + "%");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("Kode");
        }
    }
    return null;
}
    
    public void updateBatchWithJurnal(List<Stok> list, String currentUser) throws SQLException {
    conn.setAutoCommit(false);

    String sqlUpdateStok = 
        "UPDATE tstok SET Tanggal=?, Nama=?, Satuan=?, Stok=?, HargaBeli=?, HargaJual=?, aktif=? " +
        "WHERE IDStok=?";

    String sqlUpdateJurnal = 
        "UPDATE tjurnalitem SET Tanggal=?, QtyMasuk=?, Satuan=?, Keterangan=?, InsertUser=? " +
        "WHERE IDItem=? AND KodeTrans=? AND JenisTrans='StokAwal'";

    String sqlUpdateMitem = 
        "UPDATE mitem SET Stok=?, HargaBeli=? WHERE IDItem=?";

    String sqlUpdateMitemd = 
        "UPDATE mitemd SET HargaJual=? WHERE IDItem=? AND Satuan=?";

    try (PreparedStatement psStok = conn.prepareStatement(sqlUpdateStok);
         PreparedStatement psJurnal = conn.prepareStatement(sqlUpdateJurnal);
         PreparedStatement psUpdateMitem = conn.prepareStatement(sqlUpdateMitem);
         PreparedStatement psUpdateMitemd = conn.prepareStatement(sqlUpdateMitemd)) {

        for (Stok t : list) {

            // === UPDATE tstok ===
            psStok.setDate(1, new java.sql.Date(t.getTanggal().getTime()));
            psStok.setString(2, t.getNama());
            psStok.setString(3, t.getSatuan());
            psStok.setDouble(4, t.getStok());
            psStok.setDouble(5, t.getHargaBeli());
            psStok.setDouble(6, t.getHargaJual());
            psStok.setBoolean(7, t.isAktif());
            psStok.setInt(8, t.getId());
            psStok.addBatch();

            // === UPDATE tjurnalitem ===
            psJurnal.setDate(1, new java.sql.Date(t.getTanggal().getTime()));
            psJurnal.setDouble(2, t.getStok());
            psJurnal.setString(3, t.getSatuan());
            psJurnal.setString(4, "Update Stok " + t.getKode());
            psJurnal.setString(5, currentUser);
            psJurnal.setInt(6, t.getIdItem());
            psJurnal.setString(7, t.getKode());
            psJurnal.addBatch();

            // === Hitung stok aktual di mitem ===
            double konversi = 1;
            try (PreparedStatement psKonv = conn.prepareStatement(
                "SELECT Konversi FROM mitemd WHERE IDItem=? AND Satuan=?")) {
                psKonv.setInt(1, t.getIdItem());
                psKonv.setString(2, t.getSatuan());
                ResultSet rs = psKonv.executeQuery();
                if (rs.next()) konversi = rs.getDouble("Konversi");
            }

            double stokBaru = t.getStok() * konversi;

            // === UPDATE mitem ===
            psUpdateMitem.setDouble(1, stokBaru);
            psUpdateMitem.setDouble(2, t.getHargaBeli());
            psUpdateMitem.setInt(3, t.getIdItem());
            psUpdateMitem.addBatch();

            // === UPDATE harga jual mitemd ===
            psUpdateMitemd.setDouble(1, t.getHargaJual());
            psUpdateMitemd.setInt(2, t.getIdItem());
            psUpdateMitemd.setString(3, t.getSatuan());
            psUpdateMitemd.addBatch();
        }

        // Eksekusi batch update
        psStok.executeBatch();
        psJurnal.executeBatch();
        psUpdateMitem.executeBatch();
        psUpdateMitemd.executeBatch();

        conn.commit();

    } catch (SQLException e) {
        conn.rollback();
        throw e;
    } finally {
        conn.setAutoCommit(true);
    }
}

    
}