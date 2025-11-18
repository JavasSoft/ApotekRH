/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Tjualh;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Tjuald;

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

    String sqlHeader = "SELECT * FROM tjualh";
    String sqlDetail = "SELECT * FROM tjuald WHERE IDJualH = ?";

    try (
        PreparedStatement psHeader = conn.prepareStatement(sqlHeader);
        ResultSet rsHeader = psHeader.executeQuery()
    ) {
        while (rsHeader.next()) {

            // ==== Ambil header ====
            Tjualh h = new Tjualh();
            h.setIdJualH(rsHeader.getInt("IDJualH"));
            h.setKode(rsHeader.getString("Kode"));
            h.setIdCust(rsHeader.getInt("IDCust"));
            h.setTanggal(rsHeader.getDate("Tanggal"));
            h.setJenisBayar(rsHeader.getString("JenisBayar"));
            h.setIdDokter(rsHeader.getInt("IDDokter"));
            h.setJatuhTempo(rsHeader.getDate("JatuhTempo"));
            h.setSubTotal(rsHeader.getDouble("SubTotal"));
            h.setDiskon(rsHeader.getDouble("Diskon"));
            h.setPpn(rsHeader.getDouble("Ppn"));
            h.setTotal(rsHeader.getDouble("Total"));
            h.setStatus(rsHeader.getString("Status"));
            h.setInsertUser(rsHeader.getString("InsertUser"));
            h.setUpdateUser(rsHeader.getString("UpdateUser"));

            // ==== Ambil detail ====
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            psDetail.setInt(1, h.getIdJualH());
            ResultSet rsDetail = psDetail.executeQuery();

            List<Tjuald> details = new ArrayList<>();
            while (rsDetail.next()) {
                Tjuald d = new Tjuald();
                d.setIdJualD(rsDetail.getInt("IDJualD"));
                d.setIdJualH(rsDetail.getInt("IDJualH"));
                d.setIdItemD(rsDetail.getInt("IDItemD"));
                d.setQty(rsDetail.getDouble("Qty"));
                d.setHarga(rsDetail.getDouble("Harga"));
                //d.setDiskon(rsDetail.getDouble("Diskon"));
                d.setTotal(rsDetail.getDouble("Total"));

                details.add(d);
            }

            h.setDetails(details); // SET detail ke header
            list.add(h);
        }

    } catch (Exception e) {
        e.printStackTrace();
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
    public List<Tjualh> getByDateRange(Date tanggalAwal, Date tanggalAkhir) throws SQLException {
    List<Tjualh> list = new ArrayList<>();

    String sqlHeader = "SELECT * FROM tjualh WHERE Tanggal BETWEEN ? AND ? ORDER BY Tanggal ASC";
    String sqlDetail = "SELECT * FROM tjuald WHERE IDJualH = ?";

    try (
        PreparedStatement psHeader = conn.prepareStatement(sqlHeader)
    ) {
        psHeader.setDate(1, tanggalAwal);
        psHeader.setDate(2, tanggalAkhir);

        ResultSet rsHeader = psHeader.executeQuery();

        while (rsHeader.next()) {

            // ==== Ambil Header ====
            Tjualh h = new Tjualh();
            h.setIdJualH(rsHeader.getInt("IDJualH"));
            h.setKode(rsHeader.getString("Kode"));
            h.setIdCust(rsHeader.getInt("IDCust"));
            h.setTanggal(rsHeader.getDate("Tanggal"));
            h.setJenisBayar(rsHeader.getString("JenisBayar"));
            h.setIdDokter(rsHeader.getInt("IDDokter"));
            h.setJatuhTempo(rsHeader.getDate("JatuhTempo"));
            h.setSubTotal(rsHeader.getDouble("SubTotal"));
            h.setDiskon(rsHeader.getDouble("Diskon"));
            h.setPpn(rsHeader.getDouble("Ppn"));
            h.setTotal(rsHeader.getDouble("Total"));
            h.setStatus(rsHeader.getString("Status"));
            h.setInsertUser(rsHeader.getString("InsertUser"));
            h.setUpdateUser(rsHeader.getString("UpdateUser"));

            // ==== Ambil Detail ====
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            psDetail.setInt(1, h.getIdJualH());
            ResultSet rsDetail = psDetail.executeQuery();

            List<Tjuald> details = new ArrayList<>();
            while (rsDetail.next()) {
                Tjuald d = new Tjuald();
                d.setIdJualD(rsDetail.getInt("IDJualD"));
                d.setIdJualH(rsDetail.getInt("IDJualH"));
                d.setIdItemD(rsDetail.getInt("IDItemD"));
                d.setQty(rsDetail.getDouble("Qty"));
                d.setHarga(rsDetail.getDouble("Harga"));
                d.setTotal(rsDetail.getDouble("Total"));
                details.add(d);
            }

            h.setDetails(details);
            list.add(h);
        }
    }

    return list;
}

}
