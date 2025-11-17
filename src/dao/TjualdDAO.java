/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Tjuald;
import java.sql.*;
import java.util.*;

public class TjualdDAO {
    private Connection conn;

    public TjualdDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Tjuald detil) throws SQLException {
        String sql = "INSERT INTO tjuald (IDJualH, IDItemD, Qty, Harga, Total, QtyBase) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, detil.getIdJualH());
            ps.setObject(2, detil.getIdItemD());
            ps.setDouble(3, detil.getQty());
            ps.setDouble(4, detil.getHarga());
            ps.setDouble(5, detil.getTotal());
            ps.setDouble(6, detil.getQtyBase());
            ps.executeUpdate();
        }
    }

    public List<Tjuald> getByHeader(int idJualH) throws SQLException {
        List<Tjuald> list = new ArrayList<>();
        String sql = "SELECT * FROM tjuald WHERE IDJualH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJualH);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tjuald d = new Tjuald();
                    d.setIdJualD(rs.getInt("IDJualD"));
                    d.setIdItemD(rs.getInt("IDItemD"));
                    d.setQty(rs.getDouble("Qty"));
                    d.setHarga(rs.getDouble("Harga"));
                    d.setTotal(rs.getDouble("Total"));
                    list.add(d);
                }
            }
        }
        return list;
    }
    
}
