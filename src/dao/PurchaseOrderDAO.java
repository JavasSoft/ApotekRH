/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.PurchaseOrder;

public class PurchaseOrderDAO {
    
    private Connection conn;

    // Constructor untuk menerima koneksi database
    public PurchaseOrderDAO(Connection conn) {
        this.conn = conn;
    }

    // Menyimpan Purchase Order ke dalam database
    public void savePurchaseOrder(PurchaseOrder purchaseOrder) throws SQLException {
        String sql = "INSERT INTO tpoh (Kode, IDSupp, Tanggal, Total, SubTotal, Ppn, IsAktif, Complite, Status, InsertTime, InsertUser, UpdateUser) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, purchaseOrder.getKode());
        statement.setInt(2, purchaseOrder.getIdSupp());
        statement.setString(3, purchaseOrder.getTanggal());
        statement.setDouble(4, purchaseOrder.getTotal());
        statement.setDouble(5, purchaseOrder.getSubTotal());
        statement.setDouble(6, purchaseOrder.getPpn());
        statement.setString(7, purchaseOrder.getIsAktif());
        statement.setString(8, purchaseOrder.getComplite());
        statement.setString(9, purchaseOrder.getStatus());
        statement.setString(10, purchaseOrder.getInsertTime());
        statement.setString(11, purchaseOrder.getInsertUser());
        statement.setString(12, purchaseOrder.getUpdateUser());

        statement.executeUpdate();
        statement.close();
    }
    
    // Update Purchase Order yang sudah ada di database
    public void updatePurchaseOrder(PurchaseOrder purchaseOrder) throws SQLException {
        String sql = "UPDATE tpoh SET Kode=?, IDSupp=?, Tanggal=?, Total=?, SubTotal=?, Ppn=?, IsAktif=?, Complite=?, Status=?, UpdateUser=? WHERE IDPoH=?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, purchaseOrder.getKode());
        statement.setInt(2, purchaseOrder.getIdSupp());
        statement.setString(3, purchaseOrder.getTanggal());
        statement.setDouble(4, purchaseOrder.getTotal());
        statement.setDouble(5, purchaseOrder.getSubTotal());
        statement.setDouble(6, purchaseOrder.getPpn());
        statement.setString(7, purchaseOrder.getIsAktif());
        statement.setString(8, purchaseOrder.getComplite());
        statement.setString(9, purchaseOrder.getStatus());
        statement.setString(10, purchaseOrder.getUpdateUser());
        statement.setInt(11, purchaseOrder.getIdPoH());

        statement.executeUpdate();
        statement.close();
    }
    
    // Menghapus Purchase Order berdasarkan ID
    public void deletePurchaseOrder(int idPoH) throws SQLException {
        String sql = "DELETE FROM tpoh WHERE IDPoH=?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, idPoH);

        statement.executeUpdate();
        statement.close();
    }

    // Mendapatkan Purchase Order berdasarkan ID
    public PurchaseOrder getPurchaseOrderById(int idPoH) throws SQLException {
        String sql = "SELECT * FROM tpoh WHERE IDPoH = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, idPoH);
        ResultSet resultSet = statement.executeQuery();

        PurchaseOrder purchaseOrder = null;
        if (resultSet.next()) {
            purchaseOrder = new PurchaseOrder(
                resultSet.getInt("IDPoH"),
                resultSet.getString("Kode"),
                resultSet.getInt("IDSupp"),
                resultSet.getString("Tanggal"),
                resultSet.getDouble("Total"),
                resultSet.getDouble("SubTotal"),
                resultSet.getDouble("Ppn"),
                resultSet.getString("IsAktif"),
                resultSet.getString("Complite"),
                resultSet.getString("Status"),
                resultSet.getString("InsertTime"),
                resultSet.getString("InsertUser"),
                resultSet.getString("UpdateUser")
            );
        }

        resultSet.close();
        statement.close();
        
        return purchaseOrder;
    }

    // Mendapatkan semua Purchase Orders
    public List<PurchaseOrder> getAllPurchaseOrders() throws SQLException {
        String sql = "SELECT * FROM tpoh";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        while (resultSet.next()) {
            PurchaseOrder purchaseOrder = new PurchaseOrder(
                resultSet.getInt("IDPoH"),
                resultSet.getString("Kode"),
                resultSet.getInt("IDSupp"),
                resultSet.getString("Tanggal"),
                resultSet.getDouble("Total"),
                resultSet.getDouble("SubTotal"),
                resultSet.getDouble("Ppn"),
                resultSet.getString("IsAktif"),
                resultSet.getString("Complite"),
                resultSet.getString("Status"),
                resultSet.getString("InsertTime"),
                resultSet.getString("InsertUser"),
                resultSet.getString("UpdateUser")
            );
            purchaseOrders.add(purchaseOrder);
        }

        resultSet.close();
        statement.close();
        
        return purchaseOrders;
    }
}
