/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Tbelih;
import java.sql.*;
import java.util.*;

public class TbelihDAO {
    private Connection conn;

    public TbelihDAO(Connection conn) {
        this.conn = conn;
    }

    // === INSERT HEADER PEMBELIAN ===
    public void insert(Tbelih beli) throws SQLException {
        String sql = "INSERT INTO tbelih (Kode, IDSupplier, Tanggal, JenisBayar, " +
                     "JatuhTempo, SubTotal, Diskon, Ppn, Total, Status, InsertUser) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, beli.getKode());
            ps.setObject(2, beli.getIdSupplier());
            ps.setDate(3, beli.getTanggal());
            ps.setString(4, beli.getJenisBayar());
            ps.setDate(5, beli.getJatuhTempo());
            ps.setDouble(6, beli.getSubTotal());
            ps.setDouble(7, beli.getDiskon());
            ps.setDouble(8, beli.getPpn());
            ps.setDouble(9, beli.getTotal());
            ps.setString(10, beli.getStatus());
            ps.setString(11, beli.getInsertUser());
            ps.executeUpdate();
        }
    }

    // === GET SEMUA DATA HEADER ===
    public List<Tbelih> getAll() throws SQLException {
        List<Tbelih> list = new ArrayList<>();
        String sql = "SELECT * FROM tbelih ORDER BY IDBeliH DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Tbelih b = new Tbelih();
                b.setIdBeliH(rs.getInt("IDBeliH"));
                b.setKode(rs.getString("Kode"));
                b.setTanggal(rs.getDate("Tanggal"));
                b.setTotal(rs.getDouble("Total"));
                b.setStatus(rs.getString("Status"));
                list.add(b);
            }
        }
        return list;
    }

    // === UPDATE STATUS TRANSAKSI ===
    public void updateStatus(int idBeliH, String status) throws SQLException {
        String sql = "UPDATE tbelih SET Status = ?, UpdateUser = ? WHERE IDBeliH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, "system");
            ps.setInt(3, idBeliH);
            ps.executeUpdate();
        }
    }
}
