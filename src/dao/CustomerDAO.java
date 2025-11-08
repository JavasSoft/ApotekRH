package dao;

import model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CustomerDAO {
    private Connection conn;

    public CustomerDAO(Connection conn) {
        this.conn = conn;
    }

    // Menambahkan customer baru
    public boolean insertCustomer(Customer customer) {
        String sqlInsert = "INSERT INTO mcus (Kode,  Nama, Email, Alamat, Telephone, Kota, Bank, NomorRekening, NamaRekening, Aktif) VALUES (?, ?, ?, ?, ?, ?, ?, ?,  ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setString(1, customer.getKode());
            pstmt.setString(2, customer.getNama());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getAlamat());
            pstmt.setString(5, customer.getTelephone());
            pstmt.setString(6, customer.getKota());
            pstmt.setString(7, customer.getBank());
            pstmt.setString(8, customer.getNomorRekening());
            pstmt.setString(9, customer.getNamaRekening());
           pstmt.setInt(10, customer.getAktif());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
 // Memperbarui data dokter berdasarkan ID
    public boolean updateCustomer(int id, Customer customer) {
        String sqlUpdate = "UPDATE mcus SET Kode=?, Nama=?, Email=?, Alamat=?, Telephone=?, Kota=?, Bank=?, NomorRekening=?, NamaRekening=?, Aktif=? WHERE IDCustomer=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, customer.getKode());
            pstmt.setString(2, customer.getNama());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getAlamat());
            pstmt.setString(5, customer.getTelephone());
            pstmt.setString(6, customer.getKota());
            pstmt.setString(7, customer.getBank());
            pstmt.setString(8, customer.getNomorRekening());
            pstmt.setString(9, customer.getNamaRekening());
            pstmt.setInt(10, customer.getAktif());
            pstmt.setInt(11, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Menghapus dokter berdasarkan ID
    public boolean deleteCustomer(int id) {
        String sqlDelete = "DELETE FROM mcus WHERE IDCustomer=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mengambil semua data dokter
    public List<Customer> getAllCustomer() {
        List<Customer> customerList = new ArrayList<>();
        String sqlSelect = "SELECT * FROM mcus";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setIDCustomer(rs.getInt("IDCustomer"));
                customer.setKode(rs.getString("Kode"));
                customer.setNama(rs.getString("Nama"));
                customer.setEmail(rs.getString("Email"));
                customer.setAlamat(rs.getString("Alamat"));
                customer.setTelephone(rs.getString("Telephone"));
                customer.setKota(rs.getString("Kota"));
                customer.setBank(rs.getString("Bank"));
                customer.setNomorRekening(rs.getString("NomorRekening"));
                customer.setNamaRekening(rs.getString("NamaRekening"));
                customer.setAktif(rs.getInt("Aktif")); // 0 atau 1
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
    }

    // Mencari dokter berdasarkan ID
    public Customer getCustomerById(int id) {
        Customer customer = null;
        String sqlSelect = "SELECT * FROM mcus WHERE IDCustomer=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer();
                    customer.setIDCustomer(rs.getInt("IDCustomer"));
                    customer.setKode(rs.getString("Kode"));
                    customer.setNama(rs.getString("Nama"));
                    customer.setEmail(rs.getString("Email"));
                    customer.setAlamat(rs.getString("Alamat"));
                    customer.setTelephone(rs.getString("Telephone"));
                    customer.setKota(rs.getString("Kota"));
                    customer.setBank(rs.getString("Bank"));
                    customer.setNomorRekening(rs.getString("NomorRekening"));
                    customer.setNamaRekening(rs.getString("NamaRekening"));
                   customer.setAktif(rs.getInt("Aktif"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    // Mencari dokter berdasarkan nama
    public List<Customer> searchCustomerByName(String keyword) {
        List<Customer> customerList = new ArrayList<>();
        String sql = "SELECT * FROM mcus WHERE Nama LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setIDCustomer(rs.getInt("IDCustomer"));
                    customer.setKode(rs.getString("Kode"));
                    customer.setNama(rs.getString("Nama"));
                    customer.setEmail(rs.getString("Email"));
                    customer.setAlamat(rs.getString("Alamat"));
                    customer.setTelephone(rs.getString("Telephone"));
                    customer.setKota(rs.getString("Kota"));
                    customer.setBank(rs.getString("Bank"));
                    customer.setNomorRekening(rs.getString("NomorRekening"));
                    customer.setNamaRekening(rs.getString("NamaRekening"));
                    customer.setAktif(rs.getInt("Aktif"));
                    customerList.add(customer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
    }

    // Mendapatkan kode dokter terakhir dengan prefix tertentu
    public String getLastKode(String prefix) {
        String sql = "SELECT Kode FROM mcus WHERE Kode LIKE ? ORDER BY Kode DESC LIMIT 1";
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
        String sql = "SELECT COUNT(*) FROM mcus WHERE Kode = ?";
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
    public int countCustomer() {
        int totalRecords = 0;
        String sqlCount = "SELECT COUNT(*) AS total FROM mcus";
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