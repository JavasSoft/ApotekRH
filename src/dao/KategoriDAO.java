package dao;

import model.Kategori;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KategoriDAO {
    private Connection conn;

    public KategoriDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertKategori(Kategori kategori) {
        String sqlInsert = "INSERT INTO mkategori (Kode, Nama, Deskripsi, IsAktif) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setString(1, kategori.getKode());
            pstmt.setString(2, kategori.getNama());
            pstmt.setString(3, kategori.getDeskripsi());
            pstmt.setString(4, kategori.isAktif() ? "Ya" : "Tidak");
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateKategori(int id, Kategori kategori) {
        String sqlUpdate = "UPDATE mkategori SET Kode=?, Nama=?, Deskripsi=?, IsAktif=? WHERE IDKategori=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, kategori.getKode());
            pstmt.setString(2, kategori.getNama());
            pstmt.setString(3, kategori.getDeskripsi());
            pstmt.setString(4, kategori.isAktif() ? "Ya" : "Tidak");
            pstmt.setInt(5, id); // Menggunakan ID
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getLastKode(String duaHurufNama) {
    String lastKode = null;
    String sql = "SELECT MAX(Kode) AS MAX_Kode FROM mkategori WHERE Kode LIKE ?";
    
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, duaHurufNama + "%"); // Cari kode yang dimulai dengan dua huruf nama
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                lastKode = rs.getString("MAX_Kode");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return lastKode;
}
    public List<Kategori> getAllKategori() {
        List<Kategori> kategoriList = new ArrayList<>();
        String sqlSelect = "SELECT * FROM mkategori";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Kategori kategori = new Kategori(
                    rs.getInt("IDKategori"), // Ambil ID dari hasil query
                    rs.getString("Kode"),
                    rs.getString("Nama"),
                    rs.getString("Deskripsi"),
                    rs.getString("IsAktif").equals("Ya")
                );
                kategoriList.add(kategori);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return kategoriList;
    }
    public List<Kategori> searchKategoriByName(String keyword) {
    List<Kategori> kategoriList = new ArrayList<>();
    String sql = "SELECT * FROM mkategori WHERE Nama LIKE ?";
    
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, "%" + keyword + "%"); // Menambahkan wildcard untuk pencarian
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Kategori kategori = new Kategori(
                    rs.getInt("IDKategori"),
                    rs.getString("Kode"),
                    rs.getString("Nama"),
                    rs.getString("Deskripsi"),
                    rs.getString("IsAktif").equals("Ya")
                );
                kategoriList.add(kategori);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return kategoriList;
}
    public Kategori getKategoriById(int id) {
        Kategori kategori = null;
        String sqlSelect = "SELECT * FROM mkategori WHERE IDKategori=?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    kategori = new Kategori(
                        rs.getInt("IDKategori"),
                        rs.getString("Kode"),
                        rs.getString("Nama"),
                        rs.getString("Deskripsi"),
                        rs.getString("IsAktif").equals("Ya")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return kategori;
    }
    public boolean deleteKategori(int id) {
        String sqlDelete = "DELETE FROM mkategori WHERE IDKategori=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
            pstmt.setInt(1, id); // Menggunakan ID
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public int countKategori() {
        int totalRecords = 0;
        String sqlCount = "SELECT COUNT(*) AS total FROM mkategori";
        
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
//     public Kategori getKategoriByIndex(int index) {
//        String sql = "SELECT * FROM mkategori ORDER BY IDPeriode ASC LIMIT ?, 1";
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, index);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    return new Kategori(
//                        rs.getString("Kode"),
//                        rs.getString("TanggalAwal"),
//                        rs.getString("TanggalAkhir"),
//                        rs.getString("Keterangan"),
//                        rs.getString("Status"),
//                        rs.getString("Aktif").equals("Ya"),
//                        rs.getInt("IDPeriode") // Menambahkan IDPeriode
//                    );
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }}
