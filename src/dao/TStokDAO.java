package dao;

import java.sql.*;
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
}
