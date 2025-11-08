package dao;

import java.sql.*;
import java.util.*;
import model.*;

public class ItemDAO {
    private Connection conn;

    public ItemDAO(Connection conn) {
        this.conn = conn;
    }

    // INSERT ITEM + DETAIL
    public boolean insertItem(Item item) {
        String sqlItem = "INSERT INTO mitem (Kode, Nama, HargaBeli, Kategori, Aktif) VALUES (?, ?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO mitemd (IDItem, Satuan, Konversi, HargaJual, LabaPersen) VALUES (?, ?, ?, ?, ?)";
        try {
            conn.setAutoCommit(false);

            // Insert master item
            PreparedStatement pstmt = conn.prepareStatement(sqlItem, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, item.getKode());
            pstmt.setString(2, item.getNama());
            pstmt.setDouble(3, item.getHargaBeli());
            pstmt.setString(4, item.getKategori());
            pstmt.setInt(5, item.getAktif());
            pstmt.executeUpdate();

            // Ambil IDItem yang baru
            ResultSet rs = pstmt.getGeneratedKeys();
            int idItem = 0;
            if (rs.next()) {
                idItem = rs.getInt(1);
            }

            // Insert detail
            PreparedStatement pDetail = conn.prepareStatement(sqlDetail);
            for (ItemDetail d : item.getDetails()) {
                pDetail.setInt(1, idItem);
                pDetail.setString(2, d.getSatuan());
                pDetail.setInt(3, d.getKonversi());
                pDetail.setDouble(4, d.getHargaJual());
                pDetail.setDouble(5, d.getLabaPersen());
                pDetail.addBatch();
            }
            pDetail.executeBatch();

            conn.commit();
            return true;
        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (Exception ex) {}
        }
    }

    // GET SEMUA ITEM + DETAIL
    public List<Item> getAllItems() {
        List<Item> list = new ArrayList<>();
        String sqlItem = "SELECT * FROM mitem";
        String sqlDetail = "SELECT * FROM mitemd WHERE IDItem=?";

        try (PreparedStatement psItem = conn.prepareStatement(sqlItem);
             ResultSet rsItem = psItem.executeQuery()) {

            while (rsItem.next()) {
                Item item = new Item();
                item.setIDItem(rsItem.getInt("IDItem"));
                item.setKode(rsItem.getString("Kode"));
                item.setNama(rsItem.getString("Nama"));
                item.setHargaBeli(rsItem.getDouble("HargaBeli"));
                item.setKategori(rsItem.getString("Kategori"));
                item.setAktif(rsItem.getInt("Aktif"));

                // Ambil detail-nya
                PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                psDetail.setInt(1, item.getIDItem());
                ResultSet rsDetail = psDetail.executeQuery();

                List<ItemDetail> details = new ArrayList<>();
                while (rsDetail.next()) {
                    ItemDetail d = new ItemDetail();
                    d.setIDDetail(rsDetail.getInt("IDDetail"));
                    d.setSatuan(rsDetail.getString("Satuan"));
                    d.setKonversi(rsDetail.getInt("Konversi"));
                    d.setHargaJual(rsDetail.getDouble("HargaJual"));
                    d.setLabaPersen(rsDetail.getDouble("LabaPersen"));
                    details.add(d);
                }
                item.setDetails(details);
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
        public String getLastKode(String prefix) {
        String sql = "SELECT Kode FROM mitem WHERE Kode LIKE ? ORDER BY Kode DESC LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, prefix + "%");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Kode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
        
    public boolean updateItemWithDetail(Item item, ItemDetail detail) {
    String sqlUpdateItem = "UPDATE mitem SET Nama=?, Kategori=?, HargaBeli=?, Aktif=? WHERE IDItem=?";
    String sqlDeleteDetail = "DELETE FROM mitemd WHERE IDItem=?";
    String sqlInsertDetail = "INSERT INTO mitemd (IDItem, Satuan, Konversi, HargaJual, LabaPersen) VALUES (?, ?, ?, ?, ?)";

    try {
        conn.setAutoCommit(false);

        // Update master item
        try (PreparedStatement psItem = conn.prepareStatement(sqlUpdateItem)) {
            psItem.setString(1, item.getNama());
            psItem.setString(2, item.getKategori());
            psItem.setDouble(3, item.getHargaBeli());
            psItem.setInt(4, item.getAktif());
            psItem.setInt(5, item.getIDItem());
            psItem.executeUpdate();
        }

        // Hapus detail lama
        try (PreparedStatement psDel = conn.prepareStatement(sqlDeleteDetail)) {
            psDel.setInt(1, item.getIDItem());
            psDel.executeUpdate();
        }

        // Insert detail baru
        try (PreparedStatement psDet = conn.prepareStatement(sqlInsertDetail)) {
            psDet.setInt(1, item.getIDItem());
            psDet.setString(2, detail.getSatuan());
            psDet.setInt(3, detail.getKonversi());
            psDet.setDouble(4, detail.getHargaJual());
            psDet.setDouble(5, detail.getLabaPersen());
            psDet.executeUpdate();
        }

        conn.commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    } finally {
        try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
    }
}


}
