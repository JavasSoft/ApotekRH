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

    /* =====================================================
       GET LAST KODE
       ===================================================== */
    public String getLastKode(String prefix) throws SQLException {
        String sql = "SELECT Kode FROM treturh WHERE Kode LIKE ? ORDER BY Kode DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("Kode");
        }
        return null;
    }


    /* =====================================================
       CEK APAKAH PENJUALAN SUDAH HABIS DIRETUR
       ===================================================== */
    public boolean isJualHabis(int idJual) throws SQLException {
        String sql = """
            SELECT SUM(Qty - QtyRetur) AS Sisa
            FROM tjuald WHERE IDJualH=?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJual);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("Sisa") <= 0;
        }
        return false;
    }

    /* =====================================================
       UPDATE STATUS TJUALH
       ===================================================== */
    public void updateStatusJual(int idJual, String status) throws SQLException {
        String sql = "UPDATE tjualh SET Status=? WHERE IDJualH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, idJual);
            ps.executeUpdate();
        }
    }


    /* =====================================================
       SIMPAN RETUR (INSERT)
       ===================================================== */
    public void simpanRetur(Treturh retur) throws SQLException {

        String sqlH = "INSERT INTO treturh (Kode, IDCust, Tanggal, Total, Status, InsertUser, IDJualH) VALUES (?,?,?,?,?,?,?)";
        String sqlD = "INSERT INTO treturd (IDReturH, IDItem, Qty, Harga, Total, Satuan) VALUES (?,?,?,?,?,?)";

        try {
            conn.setAutoCommit(false);

            // --- Insert Header ---
            try (PreparedStatement ps = conn.prepareStatement(sqlH, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, retur.getKode());
                ps.setInt(2, retur.getIdCust());
                ps.setDate(3, retur.getTanggal());
                ps.setDouble(4, retur.getTotal());
                ps.setString(5, retur.getStatus());
                ps.setString(6, retur.getInsertUser());
                ps.setInt(7, retur.getIdjual());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    retur.setIdReturH(rs.getInt(1));
                }
            }

            // --- Insert Detail ---
            try (PreparedStatement ps = conn.prepareStatement(sqlD)) {
                for (Treturd d : retur.getDetails()) {

                    ps.setInt(1, retur.getIdReturH());
                    ps.setInt(2, d.getIdItem());
                    ps.setDouble(3, d.getQty());
                    ps.setDouble(4, d.getHarga());
                    ps.setDouble(5, d.getTotal());
                    ps.setString(6, d.getSatuan());
                    ps.executeUpdate();

                    // --- Update QtyRetur di tjuald ---
                    addQtyRetur(retur.getIdjual(), d.getIdItem(), d.getQty());

                    // --- Insert Jurnal ---
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

            // --- Update status jual jika habis ---
            if (isJualHabis(retur.getIdjual())) {
                updateStatusJual(retur.getIdjual(), "Cancel");
            }

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }


    /* =====================================================
       UPDATE RETUR
       ===================================================== */
    public void updateRetur(Treturh retur) throws SQLException {

        String sqlH = "UPDATE treturh SET IDCust=?, Tanggal=?, Total=?, Status=?, UpdateUser=? WHERE IDReturH=?";
        String sqlDelD = "DELETE FROM treturd WHERE IDReturH=?";
        String sqlDelJurnal = "DELETE FROM tjurnalitem WHERE KodeTrans=? AND JenisTrans='ReturJual'";
        String sqlD = "INSERT INTO treturd (IDReturH, IDItem, Qty, Harga, Total, Satuan) VALUES (?,?,?,?,?,?)";

        try {
            conn.setAutoCommit(false);

            // --- Kurangi QtyRetur lama ---
            resetQtyRetur(retur.getIdjual(), retur.getIdReturH());

            // --- Update header ---
            try (PreparedStatement ps = conn.prepareStatement(sqlH)) {
                ps.setInt(1, retur.getIdCust());
                ps.setDate(2, retur.getTanggal());
                ps.setDouble(3, retur.getTotal());
                ps.setString(4, retur.getStatus());
                ps.setString(5, retur.getUpdateUser());
                ps.setInt(6, retur.getIdReturH());
                ps.executeUpdate();
            }

            // --- Hapus detail lama ---
            try (PreparedStatement ps = conn.prepareStatement(sqlDelD)) {
                ps.setInt(1, retur.getIdReturH());
                ps.executeUpdate();
            }

            // --- Hapus jurnal lama ---
            try (PreparedStatement ps = conn.prepareStatement(sqlDelJurnal)) {
                ps.setString(1, retur.getKode());
                ps.executeUpdate();
            }

            // --- Insert detail baru ---
            try (PreparedStatement ps = conn.prepareStatement(sqlD)) {
                for (Treturd d : retur.getDetails()) {

                    ps.setInt(1, retur.getIdReturH());
                    ps.setInt(2, d.getIdItem());
                    ps.setDouble(3, d.getQty());
                    ps.setDouble(4, d.getHarga());
                    ps.setDouble(5, d.getTotal());
                    ps.setString(6, d.getSatuan());
                    ps.executeUpdate();

                    // update QtyRetur baru
                    addQtyRetur(retur.getIdjual(), d.getIdItem(), d.getQty());

                    // jurnal baru
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

            // cek apakah jual habis
            if (isJualHabis(retur.getIdjual())) {
                updateStatusJual(retur.getIdjual(), "Cancel");
            } else {
                updateStatusJual(retur.getIdjual(), "Open");
            }

            conn.commit();

        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }


    /* =====================================================
       HAPUS RETUR
       ===================================================== */
    public void hapusRetur(Treturh retur) throws SQLException {

        String sqlH = "DELETE FROM treturh WHERE IDReturH=?";
        String sqlJurnal = "DELETE FROM tjurnalitem WHERE KodeTrans=? AND JenisTrans='ReturJual'";

        try {
            conn.setAutoCommit(false);

            // --- Kurangi QtyRetur berdasarkan detail retur ---
            resetQtyRetur(retur.getIdjual(), retur.getIdReturH());

            // --- Hapus jurnal ---
            try (PreparedStatement ps = conn.prepareStatement(sqlJurnal)) {
                ps.setString(1, retur.getKode());
                ps.executeUpdate();
            }

            // --- Hapus header (detail ikut terhapus karena ON DELETE CASCADE) ---
            try (PreparedStatement ps = conn.prepareStatement(sqlH)) {
                ps.setInt(1, retur.getIdReturH());
                ps.executeUpdate();
            }

            // setelah hapus retur cek apakah jual masih ada qty
            if (isJualHabis(retur.getIdjual())) {
                updateStatusJual(retur.getIdjual(), "Cancel");
            } else {
                updateStatusJual(retur.getIdjual(), "Open");
            }

            conn.commit();

        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }


    /* =====================================================
       FUNGSI TAMBAHAN UNTUK QTY RETUR
       ===================================================== */

    // tambah qty retur
    private void addQtyRetur(int idJual, int idItem, double qty) throws SQLException {
        String sql = "UPDATE tjuald SET QtyRetur = QtyRetur + ? WHERE IDJualH=? AND IDItemD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, qty);
            ps.setInt(2, idJual);
            ps.setInt(3, idItem);
            ps.executeUpdate();
        }
    }

    // reset qty retur sesuai retur lama
    private void resetQtyRetur(int idJual, int idReturH) throws SQLException {
        String sql = "SELECT IDItem, Qty FROM treturd WHERE IDReturH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReturH);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idItem = rs.getInt("IDItem");
                double qty = rs.getDouble("Qty");

                // kurangi QtyRetur
                String upd = "UPDATE tjuald SET QtyRetur = QtyRetur - ? WHERE IDJualH=? AND IDItemD=?";
                try (PreparedStatement ps2 = conn.prepareStatement(upd)) {
                    ps2.setDouble(1, qty);
                    ps2.setInt(2, idJual);
                    ps2.setInt(3, idItem);
                    ps2.executeUpdate();
                }
            }
        }
    }
}
