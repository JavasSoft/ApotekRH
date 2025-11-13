/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Tbelid;
import java.sql.*;
import java.util.*;

public class TbelidDAO {
    private Connection conn;

    public TbelidDAO(Connection conn) {
        this.conn = conn;
    }

    // === INSERT DETAIL PEMBELIAN ===
    public void insert(Tbelid detil) throws SQLException {
        String sql = "INSERT INTO tbelid (IDBeliH, IDItemD, Qty, Harga, Total, QtyBase) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, detil.getIdBeliH());
            ps.setObject(2, detil.getIdItemD());
            ps.setDouble(3, detil.getQty());
            ps.setDouble(4, detil.getHarga());
            ps.setDouble(5, detil.getTotal());
            ps.setDouble(6, detil.getQtyBase());
            ps.executeUpdate();
        }
    }

    // === AMBIL DETAIL BERDASARKAN HEADER ===
    public List<Tbelid> getByHeader(int idBeliH) throws SQLException {
        List<Tbelid> list = new ArrayList<>();
        String sql = "SELECT * FROM tbelid WHERE IDBeliH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBeliH);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tbelid d = new Tbelid();
                    d.setIdBeliD(rs.getInt("IDBeliD"));
                    d.setIdItemD(rs.getInt("IDItemD"));
                    d.setQty(rs.getDouble("Qty"));
                    d.setHarga(rs.getDouble("Harga"));
                    d.setTotal(rs.getDouble("Total"));
                    d.setQtyBase(rs.getDouble("QtyBase"));
                    list.add(d);
                }
            }
        }
        return list;
    }
}
