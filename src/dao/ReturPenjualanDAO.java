/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Treturh;
import model.Treturd;
import model.Tjurnalitem;
import java.sql.*;
import java.util.List;

public class ReturPenjualanDAO {
    private Connection conn;
    private TjurnalitemDAO jurnalDAO;

    public ReturPenjualanDAO(Connection conn) {
        this.conn = conn;
        this.jurnalDAO = new TjurnalitemDAO(conn);
    }

    public void simpanRetur(Treturh retur) throws SQLException {
        String sqlH = "INSERT INTO treturh (Kode, IDCust, Tanggal, Total, Status, InsertUser) VALUES (?,?,?,?,?,?)";
        String sqlD = "INSERT INTO treturd (IDReturH, IDItem, Qty, Harga, Total, Satuan) VALUES (?,?,?,?,?,?)";

        try {
            conn.setAutoCommit(false);

            // === Simpan header ===
            try (PreparedStatement psH = conn.prepareStatement(sqlH, Statement.RETURN_GENERATED_KEYS)) {
                psH.setString(1, retur.getKode());
                psH.setInt(2, retur.getIdCust());
                psH.setDate(3, retur.getTanggal());
                psH.setDouble(4, retur.getTotal());
                psH.setString(5, retur.getStatus());
                psH.setString(6, retur.getInsertUser());
                psH.executeUpdate();

                ResultSet rsKey = psH.getGeneratedKeys();
                if (rsKey.next()) {
                    retur.setIdReturH(rsKey.getInt(1));
                } else {
                    throw new SQLException("Gagal mendapatkan IDReturH.");
                }
            }

            // === Simpan detail ===
            try (PreparedStatement psD = conn.prepareStatement(sqlD, Statement.RETURN_GENERATED_KEYS)) {
                for (Treturd d : retur.getDetails()) {
                    psD.setInt(1, retur.getIdReturH());
                    psD.setInt(2, d.getIdItem());
                    psD.setDouble(3, d.getQty());
                    psD.setDouble(4, d.getHarga());
                    psD.setDouble(5, d.getTotal());
                    psD.setString(6, d.getSatuan());
                    psD.executeUpdate();

                    // === Simpan jurnal item ===
                    Tjurnalitem j = new Tjurnalitem();
                    j.setTanggal(retur.getTanggal());
                    j.setIdItem(d.getIdItem());
                    j.setKodeTrans(retur.getKode());
                    j.setJenisTrans("ReturJual");
                    j.setQtyMasuk(d.getQty());
                    j.setQtyKeluar(0);
                    j.setSatuan(d.getSatuan());
                    j.setKeterangan("Retur penjualan " + retur.getKode());
                    j.setInsertUser(retur.getInsertUser());
                    jurnalDAO.insert(j);
                }
            }

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    public void updateRetur(Treturh retur) throws SQLException {
    String sqlH = "UPDATE treturh SET IDCust=?, Tanggal=?, Total=?, Status=?, UpdateUser=? WHERE IDReturH=?";
    String sqlDeleteDetail = "DELETE FROM treturd WHERE IDReturH=?";
    String sqlD = "INSERT INTO treturd (IDReturH, IDItem, Qty, Harga, Total, Satuan) VALUES (?,?,?,?,?,?)";
    String sqlDeleteJurnal = "DELETE FROM tjurnalitem WHERE KodeTrans=? AND JenisTrans='ReturJual'";

    try {
        conn.setAutoCommit(false);

        // === Update header ===
        try (PreparedStatement psH = conn.prepareStatement(sqlH)) {
            psH.setInt(1, retur.getIdCust());
            psH.setDate(2, retur.getTanggal());
            psH.setDouble(3, retur.getTotal());
            psH.setString(4, retur.getStatus());
            psH.setString(5, retur.getUpdateUser());
            psH.setInt(6, retur.getIdReturH());
            psH.executeUpdate();
        }

        // === Hapus detail lama ===
        try (PreparedStatement psDel = conn.prepareStatement(sqlDeleteDetail)) {
            psDel.setInt(1, retur.getIdReturH());
            psDel.executeUpdate();
        }

        // === Hapus jurnal lama ===
        try (PreparedStatement psJ = conn.prepareStatement(sqlDeleteJurnal)) {
            psJ.setString(1, retur.getKode());
            psJ.executeUpdate();
        }

        // === Insert detail baru + jurnal baru ===
        try (PreparedStatement psD = conn.prepareStatement(sqlD)) {
            for (Treturd d : retur.getDetails()) {
                psD.setInt(1, retur.getIdReturH());
                psD.setInt(2, d.getIdItem());
                psD.setDouble(3, d.getQty());
                psD.setDouble(4, d.getHarga());
                psD.setDouble(5, d.getTotal());
                psD.setString(6, d.getSatuan());
                psD.executeUpdate();

                // jurnal
                Tjurnalitem j = new Tjurnalitem();
                j.setTanggal(retur.getTanggal());
                j.setIdItem(d.getIdItem());
                j.setKodeTrans(retur.getKode());
                j.setJenisTrans("ReturJual");
                j.setQtyMasuk(d.getQty());
                j.setQtyKeluar(0);
                j.setSatuan(d.getSatuan());
                j.setKeterangan("Update retur penjualan " + retur.getKode());
                j.setInsertUser(retur.getUpdateUser());
                jurnalDAO.insert(j);
            }
        }

        conn.commit();
    } catch (Exception ex) {
        conn.rollback();
        throw ex;
    } finally {
        conn.setAutoCommit(true);
    }
}
    public void hapusRetur(Treturh retur) throws SQLException {
    String sqlH = "DELETE FROM treturh WHERE IDReturH=?";
    String sqlJurnal = "DELETE FROM tjurnalitem WHERE KodeTrans=? AND JenisTrans='ReturJual'";

    try {
        conn.setAutoCommit(false);

        // Hapus jurnal dulu
        try (PreparedStatement psJ = conn.prepareStatement(sqlJurnal)) {
            psJ.setString(1, retur.getKode());
            psJ.executeUpdate();
        }

        // Hapus header (detail otomatis ikut jika ON DELETE CASCADE)
        try (PreparedStatement psH = conn.prepareStatement(sqlH)) {
            psH.setInt(1, retur.getIdReturH());
            psH.executeUpdate();
        }

        conn.commit();
    } catch (Exception ex) {
        conn.rollback();
        throw ex;
    } finally {
        conn.setAutoCommit(true);
    }
}


}
