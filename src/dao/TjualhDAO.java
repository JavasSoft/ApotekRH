/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Tjualh;
import java.sql.*;
import java.util.*;

public class TjualhDAO {
    private Connection conn;

    public TjualhDAO(Connection conn) {
        this.conn = conn;
    }
public String getLastKode(String prefix) throws SQLException {
        String sql = "SELECT Kode FROM tjualh WHERE Kode LIKE ? ORDER BY Kode DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Kode");
            }
        }
        return null;
    }
    public void insert(Tjualh jual) throws SQLException {
        String sql = "INSERT INTO tjualh (Kode, IDCust, Tanggal, JenisBayar, IDDokter, " +
                     "JatuhTempo, SubTotal, Diskon, Ppn, Total, Status, InsertUser) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jual.getKode());
            ps.setObject(2, jual.getIdCust());
            ps.setDate(3, jual.getTanggal());
            ps.setString(4, jual.getJenisBayar());
            ps.setObject(5, jual.getIdDokter());
            ps.setDate(6, jual.getJatuhTempo());
            ps.setDouble(7, jual.getSubTotal());
            ps.setDouble(8, jual.getDiskon());
            ps.setDouble(9, jual.getPpn());
            ps.setDouble(10, jual.getTotal());
            ps.setString(11, jual.getStatus());
            ps.setString(12, jual.getInsertUser());
            ps.executeUpdate();
        }
    }

    public List<Tjualh> getAll() throws SQLException {
        List<Tjualh> list = new ArrayList<>();
        String sql = "SELECT * FROM tjualh";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Tjualh j = new Tjualh();
                j.setIdJualH(rs.getInt("IDJualH"));
                j.setKode(rs.getString("Kode"));
                j.setTanggal(rs.getDate("Tanggal"));
                j.setTotal(rs.getDouble("Total"));
                j.setStatus(rs.getString("Status"));
                list.add(j);
            }
        }
        return list;
    }

    public void updateStatus(int idJualH, String status) throws SQLException {
        String sql = "UPDATE tjualh SET Status = ?, UpdateUser = ? WHERE IDJualH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, "system");
            ps.setInt(3, idJualH);
            ps.executeUpdate();
        }
    }
}
