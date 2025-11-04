package dao;

import model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private Connection conn;

    public CustomerDAO(Connection conn) {
        this.conn = conn;
    }

    // Menambahkan customer baru
    public boolean insertCustomer(Customer customer) {
        String sqlInsert = "INSERT INTO mcus (Kode,  NamaUsaha, Tempo, Nama, Alamat, Telephone, Kota, IsAktif) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setString(1, customer.getKode());
            pstmt.setString(2, customer.getNamaUsaha());
            pstmt.setInt(3, customer.getTempo());
            pstmt.setString(4, customer.getNama());
            pstmt.setString(5, customer.getAlamat());
            pstmt.setString(6, customer.getTelephone());
            pstmt.setString(7, customer.getKota());
            pstmt.setString(8, customer.getIsAktif());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Memperbarui customer berdasarkan ID
    public boolean updateCustomer(int id, Customer customer) {
        String sqlUpdate = "UPDATE mcus SET Kode=?,  NamaUsaha=?, Tempo=?, Nama=?, Alamat=?, Telephone=?, Kota=?, IsAktif=? WHERE IDCus=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, customer.getKode());
            pstmt.setString(2, customer.getNamaUsaha());
            pstmt.setInt(3, customer.getTempo());
            pstmt.setString(4, customer.getNama());
            pstmt.setString(5, customer.getAlamat());
            pstmt.setString(6, customer.getTelephone());
            pstmt.setString(7, customer.getKota());
            pstmt.setString(8, customer.getIsAktif());
            pstmt.setInt(9, id); // Menggunakan IDCus
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Menghapus customer berdasarkan ID
    public boolean deleteCustomer(int id) {
        String sqlDelete = "DELETE FROM mcus WHERE IDCus=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mengambil semua data customer
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        String sqlSelect = "SELECT * FROM mcus";

        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setIdCus(rs.getInt("IDCus"));
                customer.setKode(rs.getString("Kode"));
                customer.setNamaUsaha(rs.getString("NamaUsaha"));
                customer.setTempo(rs.getInt("Tempo"));
                customer.setNama(rs.getString("Nama"));
                customer.setAlamat(rs.getString("Alamat"));
                customer.setTelephone(rs.getString("Telephone"));
                customer.setKota(rs.getString("Kota"));
                customer.setIsAktif(rs.getString("IsAktif"));
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
    }

    // Mencari customer berdasarkan ID
    public Customer getCustomerById(int id) {
        Customer customer = null;
        String sqlSelect = "SELECT * FROM mcus WHERE IDCus=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer();
                    customer.setIdCus(rs.getInt("IDCus"));
                    customer.setKode(rs.getString("Kode"));
                    customer.setNamaUsaha(rs.getString("NamaUsaha"));
                    customer.setTempo(rs.getInt("Tempo"));
                    customer.setNama(rs.getString("Nama"));
                    customer.setAlamat(rs.getString("Alamat"));
                    customer.setTelephone(rs.getString("Telephone"));
                    customer.setKota(rs.getString("Kota"));
                    customer.setIsAktif(rs.getString("IsAktif"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    // Mencari customer berdasarkan nama
    public List<Customer> searchCustomerByName(String keyword) {
        List<Customer> customerList = new ArrayList<>();
        String sql = "SELECT * FROM mcus WHERE Nama LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setIdCus(rs.getInt("IDCus"));
                    customer.setKode(rs.getString("Kode"));
                    customer.setNamaUsaha(rs.getString("NamaUsaha"));
                    customer.setTempo(rs.getInt("Tempo"));
                    customer.setNama(rs.getString("Nama"));
                    customer.setAlamat(rs.getString("Alamat"));
                    customer.setTelephone(rs.getString("Telephone"));
                    customer.setKota(rs.getString("Kota"));
                    customer.setIsAktif(rs.getString("IsAktif"));
                    customerList.add(customer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
    }

    // Mendapatkan kode customer terakhir dengan prefix tertentu
    public String getLastKode(String prefix) {
        String sql = "SELECT kode FROM mcus WHERE kode LIKE ? ORDER BY kode DESC LIMIT 1";
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

    // Mengecek apakah kode customer sudah ada
    public boolean isKodeExists(String kode) {
        String sql = "SELECT COUNT(*) FROM mcus WHERE kode = ?";
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

    // Menghitung jumlah customer
    public int countCustomers() {
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
