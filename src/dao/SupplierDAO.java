package dao;

import model.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class SupplierDAO {
    private Connection conn;

    public SupplierDAO(Connection conn) {
        this.conn = conn;
    }

    // Menambahkan supplier baru
    public boolean insertSupplier(Supplier supplier) {
        String sqlInsert = "INSERT INTO msupp (Kode, Nama, Email, Alamat, Telephone, Kota, Bank, NomorRekening, NamaRekening, Aktif) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setString(1, supplier.getKode());
            pstmt.setString(2, supplier.getNama());
            pstmt.setString(3, supplier.getEmail());
            pstmt.setString(4, supplier.getAlamat());
            pstmt.setString(5, supplier.getTelephone());
            pstmt.setString(6, supplier.getKota());
            pstmt.setString(7, supplier.getBank());
            pstmt.setString(8, supplier.getNomorRekening());
            pstmt.setString(9, supplier.getNamaRekening());
            pstmt.setInt(10, supplier.getAktif());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Memperbarui data supplier berdasarkan ID
    public boolean updateSupplier(int id, Supplier supplier) {
        String sqlUpdate = "UPDATE msupp SET Kode=?, Nama=?, Email=?, Alamat=?, Telephone=?, Kota=?, Bank=?, NomorRekening=?, NamaRekening=?, Aktif=? WHERE IDSupplier=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, supplier.getKode());
            pstmt.setString(2, supplier.getNama());
            pstmt.setString(3, supplier.getEmail());
            pstmt.setString(4, supplier.getAlamat());
            pstmt.setString(5, supplier.getTelephone());
            pstmt.setString(6, supplier.getKota());
            pstmt.setString(7, supplier.getBank());
            pstmt.setString(8, supplier.getNomorRekening());
            pstmt.setString(9, supplier.getNamaRekening());
            pstmt.setInt(10, supplier.getAktif());
            pstmt.setInt(11, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Menghapus supplier berdasarkan ID
    public boolean deleteSupplier(int id) {
        String sqlDelete = "DELETE FROM msupp WHERE IDSupplier=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mengambil semua data supplier
    public List<Supplier> getAllSupplier() {
        List<Supplier> supplierList = new ArrayList<>();
        String sqlSelect = "SELECT * FROM msupp";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setIDSupplier(rs.getInt("IDSupplier"));
                supplier.setKode(rs.getString("Kode"));
                supplier.setNama(rs.getString("Nama"));
                supplier.setEmail(rs.getString("Email"));
                supplier.setAlamat(rs.getString("Alamat"));
                supplier.setTelephone(rs.getString("Telephone"));
                supplier.setKota(rs.getString("Kota"));
                supplier.setBank(rs.getString("Bank"));
                supplier.setNomorRekening(rs.getString("NomorRekening"));
                supplier.setNamaRekening(rs.getString("NamaRekening"));
                supplier.setAktif(rs.getInt("Aktif"));
                supplierList.add(supplier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierList;
    }

    // Mencari supplier berdasarkan ID
    public Supplier getSupplierById(int id) {
        Supplier supplier = null;
        String sqlSelect = "SELECT * FROM msupp WHERE IDSupplier=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    supplier = new Supplier();
                    supplier.setIDSupplier(rs.getInt("IDSupplier"));
                    supplier.setKode(rs.getString("Kode"));
                    supplier.setNama(rs.getString("Nama"));
                    supplier.setEmail(rs.getString("Email"));
                    supplier.setAlamat(rs.getString("Alamat"));
                    supplier.setTelephone(rs.getString("Telephone"));
                    supplier.setKota(rs.getString("Kota"));
                    supplier.setBank(rs.getString("Bank"));
                    supplier.setNomorRekening(rs.getString("NomorRekening"));
                    supplier.setNamaRekening(rs.getString("NamaRekening"));
                    supplier.setAktif(rs.getInt("Aktif"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplier;
    }

    // Mencari supplier berdasarkan nama
    public List<Supplier> searchSupplierByName(String keyword) {
        List<Supplier> supplierList = new ArrayList<>();
        String sql = "SELECT * FROM msupp WHERE Nama LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setIDSupplier(rs.getInt("IDSupplier"));
                    supplier.setKode(rs.getString("Kode"));
                    supplier.setNama(rs.getString("Nama"));
                    supplier.setEmail(rs.getString("Email"));
                    supplier.setAlamat(rs.getString("Alamat"));
                    supplier.setTelephone(rs.getString("Telephone"));
                    supplier.setKota(rs.getString("Kota"));
                    supplier.setBank(rs.getString("Bank"));
                    supplier.setNomorRekening(rs.getString("NomorRekening"));
                    supplier.setNamaRekening(rs.getString("NamaRekening"));
                    supplier.setAktif(rs.getInt("Aktif"));
                    supplierList.add(supplier);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierList;
    }

    // Mendapatkan kode supplier terakhir dengan prefix tertentu
    public String getLastKode(String prefix) {
        String sql = "SELECT Kode FROM msupp WHERE Kode LIKE ? ORDER BY Kode DESC LIMIT 1";
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

    // Mengecek apakah kode supplier sudah ada
    public boolean isKodeExists(String kode) {
        String sql = "SELECT COUNT(*) FROM msupp WHERE Kode = ?";
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

    // Menghitung jumlah supplier
    public int countSupplier() {
        int totalRecords = 0;
        String sqlCount = "SELECT COUNT(*) AS total FROM msupp";
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
