/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.McoaTipe;

public class McoaTipeDAOImpl implements McoaTipeDAO {

    // Koneksi ke database
    private Connection conn;

    // Constructor untuk inisialisasi koneksi
    public McoaTipeDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(McoaTipe mcoaTipe) throws Exception {
        String query = "INSERT INTO mcoatipe (nama_tipe) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, mcoaTipe.getNamaTipe());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<McoaTipe> getAll() throws Exception {
        String query = "SELECT * FROM mcoatipe";
        List<McoaTipe> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String namaTipe = rs.getString("nama_tipe");
                McoaTipe mcoaTipe = new McoaTipe(id, namaTipe);
                list.add(mcoaTipe);
            }
        }
        return list;
    }

    @Override
    public McoaTipe getById(int id) throws Exception {
        String query = "SELECT * FROM mcoatipe WHERE id = ?";
        McoaTipe mcoaTipe = null;
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String namaTipe = rs.getString("nama_tipe");
                    mcoaTipe = new McoaTipe(id, namaTipe);
                }
            }
        }
        return mcoaTipe;
    }

    @Override
    public void update(McoaTipe mcoaTipe) throws Exception {
        String query = "UPDATE mcoatipe SET nama_tipe = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, mcoaTipe.getNamaTipe());
            stmt.setInt(2, mcoaTipe.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String query = "DELETE FROM mcoatipe WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
