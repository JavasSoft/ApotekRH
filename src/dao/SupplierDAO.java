package dao;

import model.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private Connection conn;

    public SupplierDAO(Connection conn) {
        this.conn = conn;
    }
    // Insert Supplier
    public boolean insertSupplier(Supplier supplier) {
        String sqlInsert = "INSERT INTO msup (Kode, NamaUsaha, Tempo, Nama, Alamat, Telephone, Kota, IsAktif) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setString(1, supplier.getKode());
            pstmt.setString(2, supplier.getNamaUsaha());
            pstmt.setInt(3, supplier.getTempo());
            pstmt.setString(4, supplier.getNama());
            pstmt.setString(5, supplier.getAlamat());
            pstmt.setString(6, supplier.getTelephone());
            pstmt.setString(7, supplier.getKota());
            pstmt.setString(8, supplier.getIsAktif());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // Update Supplier
    public boolean updateSupplier(int idSup, Supplier supplier) {
        String sqlUpdate = "UPDATE msup SET Kode=?, NamaUsaha=?, Tempo=?, Nama=?, Alamat=?, Telephone=?, Kota=?, IsAktif=? "
                         + "WHERE IDSup=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, supplier.getKode());
            pstmt.setString(2, supplier.getNamaUsaha());
            pstmt.setInt(3, supplier.getTempo());
            pstmt.setString(4, supplier.getNama());
            pstmt.setString(5, supplier.getAlamat());
            pstmt.setString(6, supplier.getTelephone());
            pstmt.setString(7, supplier.getKota());
            pstmt.setString(8, supplier.getIsAktif());
            pstmt.setInt(9, idSup); // Menggunakan IDSup
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // Delete Supplier
    public boolean deleteSupplier(int idSup) {
        String sqlDelete = "DELETE FROM msup WHERE IDSup=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
            pstmt.setInt(1, idSup);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // Get All Suppliers
    public List<Supplier> getAllSuppliers() {
        List<Supplier> supplierList = new ArrayList<>();
        String sqlSelect = "SELECT * FROM msup";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setIdSup(rs.getInt("IDSup"));
                supplier.setKode(rs.getString("Kode"));
                supplier.setNamaUsaha(rs.getString("NamaUsaha"));
                supplier.setTempo(rs.getInt("Tempo"));
                supplier.setNama(rs.getString("Nama"));
                supplier.setAlamat(rs.getString("Alamat"));
                supplier.setTelephone(rs.getString("Telephone"));
                supplier.setKota(rs.getString("Kota"));
                supplier.setIsAktif(rs.getString("IsAktif"));
                supplierList.add(supplier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierList;
    }
    // Get Supplier by ID
    public Supplier getSupplierById(int idSup) {
        Supplier supplier = null;
        String sqlSelect = "SELECT * FROM msup WHERE IDSup=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, idSup);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    supplier = new Supplier();
                    supplier.setIdSup(rs.getInt("IDSup"));
                    supplier.setKode(rs.getString("Kode"));
                    supplier.setNamaUsaha(rs.getString("NamaUsaha"));
                    supplier.setTempo(rs.getInt("Tempo"));
                    supplier.setNama(rs.getString("Nama"));
                    supplier.setAlamat(rs.getString("Alamat"));
                    supplier.setTelephone(rs.getString("Telephone"));
                    supplier.setKota(rs.getString("Kota"));
                    supplier.setIsAktif(rs.getString("IsAktif"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplier;
    }
    // Search Supplier by Name
    public List<Supplier> searchSupplierByName(String keyword) {
        List<Supplier> supplierList = new ArrayList<>();
        String sql = "SELECT * FROM msup WHERE Nama LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setIdSup(rs.getInt("IDSup"));
                    supplier.setKode(rs.getString("Kode"));
                    supplier.setNamaUsaha(rs.getString("NamaUsaha"));
                    supplier.setTempo(rs.getInt("Tempo"));
                    supplier.setNama(rs.getString("Nama"));
                    supplier.setAlamat(rs.getString("Alamat"));
                    supplier.setTelephone(rs.getString("Telephone"));
                    supplier.setKota(rs.getString("Kota"));
                    supplier.setIsAktif(rs.getString("IsAktif"));
                    supplierList.add(supplier);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierList;
    }
    // Count total suppliers
    public int countSuppliers() {
        int totalRecords = 0;
        String sqlCount = "SELECT COUNT(*) AS total FROM msup";
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
    
    public String getLastKode(String prefix) {
    String sql = "SELECT kode FROM msup WHERE kode LIKE ? ORDER BY kode DESC LIMIT 1";
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
}
