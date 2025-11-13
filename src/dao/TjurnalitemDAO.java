/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Tjurnalitem;
import java.sql.*;
import java.util.*;

public class TjurnalitemDAO {
    private Connection conn;

    public TjurnalitemDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Tjurnalitem item) throws SQLException {
        String sql = "INSERT INTO tjurnalitem (Tanggal, IDItem, KodeTrans, JenisTrans, " +
                     "QtyMasuk, QtyKeluar, Satuan, Keterangan, InsertUser) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, item.getTanggal());
            ps.setObject(2, item.getIdItem());
            ps.setString(3, item.getKodeTrans());
            ps.setString(4, item.getJenisTrans());
            ps.setDouble(5, item.getQtyMasuk());
            ps.setDouble(6, item.getQtyKeluar());
            ps.setString(7, item.getSatuan());
            ps.setString(8, item.getKeterangan());
            ps.setString(9, item.getInsertUser());
            ps.executeUpdate();
        }
    }

    public List<Tjurnalitem> getAll() throws SQLException {
        List<Tjurnalitem> list = new ArrayList<>();
        String sql = "SELECT * FROM tjurnalitem";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Tjurnalitem j = new Tjurnalitem();
                j.setIdJurnal(rs.getInt("IDJurnal"));
                j.setTanggal(rs.getDate("Tanggal"));
                j.setKodeTrans(rs.getString("KodeTrans"));
                j.setJenisTrans(rs.getString("JenisTrans"));
                j.setQtyMasuk(rs.getDouble("QtyMasuk"));
                j.setQtyKeluar(rs.getDouble("QtyKeluar"));
                list.add(j);
            }
        }
        return list;
    }
}
