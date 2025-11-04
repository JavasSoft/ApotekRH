/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Barang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BarangDAO {
    private Connection conn;

    public BarangDAO(Connection conn) {
        this.conn = conn;
    }
    public boolean insertBarang(Barang barang) {
        String sqlInsert = "INSERT INTO mbarang (Kode, Nama, IDKategori, Satuan, Beli, Jual, Keterangan, IsAktif) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setString(1, barang.getKode());
            pstmt.setString(2, barang.getNama());
            pstmt.setInt(3, barang.getIDKategori());
            pstmt.setString(4, barang.getSatuan());
            pstmt.setDouble(5, barang.getBeli());
            pstmt.setDouble(6, barang.getJual());
            pstmt.setString(7, barang.getKeterangan());
            pstmt.setString(8, barang.getIsAktif());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateBarang(int id, Barang barang) {
        String sqlUpdate = "UPDATE mbarang SET Kode=?, Nama=?, IDKategori=?, Satuan=?, Beli=?, Jual=?, Keterangan=?, IsAktif=? WHERE IDBarang=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, barang.getKode());
            pstmt.setString(2, barang.getNama());
            pstmt.setInt(3, barang.getIDKategori());
            pstmt.setString(4, barang.getSatuan());
            pstmt.setDouble(5, barang.getBeli());
            pstmt.setDouble(6, barang.getJual());
            pstmt.setString(7, barang.getKeterangan());
            pstmt.setString(8, barang.getIsAktif());
            pstmt.setInt(9, id); // Menggunakan IDBarang
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteBarang(int id) {
        String sqlDelete = "DELETE FROM mbarang WHERE IDBarang=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Barang> getAllBarang() {
        List<Barang> barangList = new ArrayList<>();
        String sqlSelect = "SELECT * FROM mbarang";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {

                Barang barang = new Barang();
                barang.setIDBarang(rs.getInt("IDBarang"));
                barang.setKode(rs.getString("Kode"));
                barang.setNama(rs.getString("Nama"));
                barang.setIDKategori(rs.getInt("IDKategori"));
                barang.setSatuan(rs.getString("Satuan"));
                barang.setBeli(rs.getDouble("Beli"));
                barang.setJual(rs.getDouble("Jual"));
                barang.setKeterangan(rs.getString("Keterangan"));
                barang.setIsAktif(rs.getString("IsAktif"));
                barangList.add(barang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return barangList;
    }
    public Barang getBarangById(int id) {
        Barang barang = null;
        String sqlSelect = "SELECT * FROM mbarang WHERE IDBarang=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    barang = new Barang();
                    barang.setIDBarang(rs.getInt("IDBarang"));
                    barang.setKode(rs.getString("Kode"));
                    barang.setNama(rs.getString("Nama"));
                    barang.setIDKategori(rs.getInt("IDKategori"));
                    barang.setSatuan(rs.getString("Satuan"));
                    barang.setBeli(rs.getDouble("Beli"));
                    barang.setJual(rs.getDouble("Jual"));
                    barang.setKeterangan(rs.getString("Keterangan"));
                    barang.setIsAktif(rs.getString("IsAktif"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return barang;
    }
    public List<Barang> searchBarangByName(String keyword) {
        List<Barang> barangList = new ArrayList<>();
        String sql = "SELECT * FROM mbarang WHERE Nama LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Barang barang = new Barang();
                    barang.setIDBarang(rs.getInt("IDBarang"));
                    barang.setKode(rs.getString("Kode"));
                    barang.setNama(rs.getString("Nama"));
                    barang.setIDKategori(rs.getInt("IDKategori"));
                    barang.setSatuan(rs.getString("Satuan"));
                    barang.setBeli(rs.getDouble("Beli"));
                    barang.setJual(rs.getDouble("Jual"));
                    barang.setKeterangan(rs.getString("Keterangan"));
                    barang.setIsAktif(rs.getString("IsAktif"));
                    barangList.add(barang);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return barangList;
    }
    public String getLastKode(String prefix) {
        String sql = "SELECT kode FROM mbarang WHERE kode LIKE ? ORDER BY kode DESC LIMIT 1";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, prefix + "%");
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("kode");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null; // Jika tidak ada kode yang ditemukan
}
    public boolean isKodeExists(String kode) {
      String sql = "SELECT COUNT(*) FROM mbarang WHERE kode = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, kode);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; // Jika ada, mengembalikan true
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false; // Jika tidak ada
}
    public int countBarang() {
        int totalRecords = 0;
        String sqlCount = "SELECT COUNT(*) AS total FROM mbarang";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sqlCount);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return totalRecords;
    }
}
