package dao;

import model.PurchaseOrderDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDetailDAO {
    private Connection conn;

    // Constructor untuk menerima koneksi database
    public PurchaseOrderDetailDAO(Connection conn) {
        this.conn = conn;
    }

    // Menyimpan Purchase Order Detail ke dalam database
    public void savePurchaseOrderDetail(PurchaseOrderDetail detail) throws SQLException {
        String sql = "INSERT INTO PurchaseOrderDetails (IDPoH, kode, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, detail.getIDPoH());
            statement.setString(2, detail.getKode());
            statement.setInt(3, detail.getQuantity());
            statement.setDouble(4, detail.getPrice());
            statement.executeUpdate();
        }
    }

    // Mengupdate Purchase Order Detail yang sudah ada di database
    public void updatePurchaseOrderDetail(PurchaseOrderDetail detail) throws SQLException {
        String sql = "UPDATE PurchaseOrderDetails SET IDPoH=?, kode=?, quantity=?, price=? WHERE IDPoD=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, detail.getIDPoH());
            statement.setString(2, detail.getKode());
            statement.setInt(3, detail.getQuantity());
            statement.setDouble(4, detail.getPrice());
            statement.setInt(5, detail.getIDPoD());
            statement.executeUpdate();
        }
    }

    // Menghapus Purchase Order Detail berdasarkan ID
    public void deletePurchaseOrderDetail(int IDPoD) throws SQLException {
        String sql = "DELETE FROM PurchaseOrderDetails WHERE IDPoD=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, IDPoD);
            statement.executeUpdate();
        }
    }

    // Mendapatkan Purchase Order Detail berdasarkan ID
    public PurchaseOrderDetail getPurchaseOrderDetailById(int IDPoD) throws SQLException {
        String sql = "SELECT * FROM PurchaseOrderDetails WHERE IDPoD=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, IDPoD);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new PurchaseOrderDetail(
                        resultSet.getInt("IDPoD"),
                        resultSet.getInt("IDPoH"),
                        resultSet.getString("kode"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("price")
                    );
                }
            }
        }
        return null;
    }

    // Mendapatkan semua Purchase Order Details
    public List<PurchaseOrderDetail> getAllPurchaseOrderDetails() throws SQLException {
        String sql = "SELECT * FROM PurchaseOrderDetails";
        List<PurchaseOrderDetail> details = new ArrayList<>();
        
        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                PurchaseOrderDetail detail = new PurchaseOrderDetail(
                    resultSet.getInt("IDPoD"),
                    resultSet.getInt("IDPoH"),
                    resultSet.getString("kode"),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("price")
                );
                details.add(detail);
            }
        }
        return details;
    }
}
