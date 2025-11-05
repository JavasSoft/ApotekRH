package dao;

import model.Dokter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DokterDAO {
    private Connection conn;

    public DokterDAO(Connection conn) {
        this.conn = conn;
    }

    // Menambahkan customer baru
    public boolean insertDokter(Dokter dokter) {
        String sqlInsert = "INSERT INTO mdokter (Kode,  Nama, Email, Alamat, Telephone, Kota, Bank, NomorRekening, NamaRekening, Aktif) VALUES (?, ?, ?, ?, ?, ?, ?, ?,  ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setString(1, dokter.getKode());
            pstmt.setString(2, dokter.getNama());
            pstmt.setString(3, dokter.getEmail());
            pstmt.setString(4, dokter.getAlamat());
            pstmt.setString(5, dokter.getTelephone());
            pstmt.setString(6, dokter.getKota());
            pstmt.setString(7, dokter.getBank());
            pstmt.setString(8, dokter.getNomorRekening());
            pstmt.setString(9, dokter.getNamaRekening());
            pstmt.setInt(10, Integer.parseInt(dokter.getAktif()));
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 // Memperbarui data dokter berdasarkan ID
    public boolean updateDokter(int id, Dokter dokter) {
        String sqlUpdate = "UPDATE mdokter SET Kode=?, Nama=?, Email=?, Alamat=?, Telephone=?, Kota=?, Bank=?, NomorRekening=?, NamaRekening=?, Aktif=? WHERE IDDokter=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, dokter.getKode());
            pstmt.setString(2, dokter.getNama());
            pstmt.setString(3, dokter.getEmail());
            pstmt.setString(4, dokter.getAlamat());
            pstmt.setString(5, dokter.getTelephone());
            pstmt.setString(6, dokter.getKota());
            pstmt.setString(7, dokter.getBank());
            pstmt.setString(8, dokter.getNomorRekening());
            pstmt.setString(9, dokter.getNamaRekening());
            pstmt.setInt(10, Integer.parseInt(dokter.getAktif()));
            pstmt.setInt(11, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Menghapus dokter berdasarkan ID
    public boolean deleteDokter(int id) {
        String sqlDelete = "DELETE FROM mdokter WHERE IDDokter=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mengambil semua data dokter
    public List<Dokter> getAllDokter() {
        List<Dokter> dokterList = new ArrayList<>();
        String sqlSelect = "SELECT * FROM mdokter";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Dokter dokter = new Dokter();
                dokter.setIDDokter(rs.getInt("IDDokter"));
                dokter.setKode(rs.getString("Kode"));
                dokter.setNama(rs.getString("Nama"));
                dokter.setEmail(rs.getString("Email"));
                dokter.setAlamat(rs.getString("Alamat"));
                dokter.setTelephone(rs.getString("Telephone"));
                dokter.setKota(rs.getString("Kota"));
                dokter.setBank(rs.getString("Bank"));
                dokter.setNomorRekening(rs.getString("NomorRekening"));
                dokter.setNamaRekening(rs.getString("NamaRekening"));
                dokter.setAktif(String.valueOf(rs.getInt("Aktif"))); // 0 atau 1
                dokterList.add(dokter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dokterList;
    }

    // Mencari dokter berdasarkan ID
    public Dokter getDokterById(int id) {
        Dokter dokter = null;
        String sqlSelect = "SELECT * FROM mdokter WHERE IDDokter=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dokter = new Dokter();
                    dokter.setIDDokter(rs.getInt("IDDokter"));
                    dokter.setKode(rs.getString("Kode"));
                    dokter.setNama(rs.getString("Nama"));
                    dokter.setEmail(rs.getString("Email"));
                    dokter.setAlamat(rs.getString("Alamat"));
                    dokter.setTelephone(rs.getString("Telephone"));
                    dokter.setKota(rs.getString("Kota"));
                    dokter.setBank(rs.getString("Bank"));
                    dokter.setNomorRekening(rs.getString("NomorRekening"));
                    dokter.setNamaRekening(rs.getString("NamaRekening"));
                    dokter.setAktif(String.valueOf(rs.getInt("Aktif")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dokter;
    }

    // Mencari dokter berdasarkan nama
    public List<Dokter> searchDokterByName(String keyword) {
        List<Dokter> dokterList = new ArrayList<>();
        String sql = "SELECT * FROM mdokter WHERE Nama LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Dokter dokter = new Dokter();
                    dokter.setIDDokter(rs.getInt("IDDokter"));
                    dokter.setKode(rs.getString("Kode"));
                    dokter.setNama(rs.getString("Nama"));
                    dokter.setEmail(rs.getString("Email"));
                    dokter.setAlamat(rs.getString("Alamat"));
                    dokter.setTelephone(rs.getString("Telephone"));
                    dokter.setKota(rs.getString("Kota"));
                    dokter.setBank(rs.getString("Bank"));
                    dokter.setNomorRekening(rs.getString("NomorRekening"));
                    dokter.setNamaRekening(rs.getString("NamaRekening"));
                    dokter.setAktif(String.valueOf(rs.getInt("Aktif")));
                    dokterList.add(dokter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dokterList;
    }

    // Mendapatkan kode dokter terakhir dengan prefix tertentu
    public String getLastKode(String prefix) {
        String sql = "SELECT Kode FROM mdokter WHERE Kode LIKE ? ORDER BY Kode DESC LIMIT 1";
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

    // Mengecek apakah kode dokter sudah ada
    public boolean isKodeExists(String kode) {
        String sql = "SELECT COUNT(*) FROM mdokter WHERE Kode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Menghitung jumlah dokter
    public int countDokter() {
        int totalRecords = 0;
        String sqlCount = "SELECT COUNT(*) AS total FROM mdokter";
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