/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Tjualh;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Tjuald;
import model.Dokter;
import model.Customer;

public class TjualhDAO {
    private Connection conn;
    private Customer customer; // <-- objek Customer
    private Dokter dokter;     // <-- objek Dokter

    public TjualhDAO(Connection conn) {
        this.conn = conn;
    }
// Tambahkan getter/setter untuk dokter dan customer
//    public Dokter getDokter() {
//        return dokter;
//    }
//
//    public void setDokter(Dokter dokter) {
//        this.dokter = dokter;
//    }
//
//    public Customer getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }

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
        List<Tjualh> listPenjualan = new ArrayList<>();

        String sqlHeader =
                "SELECT tjualh.*, " +
                " mdokter.Kode AS KodeDokter, "+
                " mdokter.Nama AS NamaDokter, " +
                " mcus.Kode AS KodeCust, " +
                " mcus.Nama AS NamaCust " +
                "FROM tjualh " +
                "LEFT JOIN mdokter ON tjualh.IDDokter = mdokter.IDDokter " +
                "LEFT JOIN mcus ON tjualh.IDCust = mcus.IDCustomer " +
                "WHERE tjualh.Tanggal BETWEEN ? AND ? " +
                "ORDER BY tjualh.Tanggal ASC";

        String sqlDetail = "SELECT * FROM tjuald WHERE IDJualH = ?";

        try (PreparedStatement preparedStatementHeader = conn.prepareStatement(sqlHeader)) {

            preparedStatementHeader.setDate(1, tanggalAwal);
            preparedStatementHeader.setDate(2, tanggalAkhir);

            ResultSet resultSetHeader = preparedStatementHeader.executeQuery();

            while (resultSetHeader.next()) {

                // ==== Ambil Header Penjualan ====
                Tjualh penjualan = new Tjualh();
                penjualan.setIdJualH(resultSetHeader.getInt("IDJualH"));
                penjualan.setKode(resultSetHeader.getString("Kode"));
                penjualan.setIdCust(resultSetHeader.getInt("IDCust"));
                penjualan.setIdDokter(resultSetHeader.getInt("IDDokter"));
                penjualan.setTanggal(resultSetHeader.getDate("Tanggal"));
                penjualan.setJenisBayar(resultSetHeader.getString("JenisBayar"));
                penjualan.setJatuhTempo(resultSetHeader.getDate("JatuhTempo"));
                penjualan.setSubTotal(resultSetHeader.getDouble("SubTotal"));
                penjualan.setDiskon(resultSetHeader.getDouble("Diskon"));
                penjualan.setPpn(resultSetHeader.getDouble("Ppn"));
                penjualan.setTotal(resultSetHeader.getDouble("Total"));
                penjualan.setStatus(resultSetHeader.getString("Status"));
                penjualan.setInsertUser(resultSetHeader.getString("InsertUser"));
                penjualan.setUpdateUser(resultSetHeader.getString("UpdateUser"));

                // ==== Ambil Dokter ====
                Dokter dokter = new Dokter();
                dokter.setIDDokter(resultSetHeader.getInt("IDDokter"));
                dokter.setKode(resultSetHeader.getString("KodeDokter"));
                dokter.setNama(resultSetHeader.getString("NamaDokter"));
                penjualan.setDokter(dokter);

                // ==== Ambil Customer ====
                Customer customer = new Customer();
                customer.setIDCustomer(resultSetHeader.getInt("IDCust"));
                customer.setKode(resultSetHeader.getString("KodeCust"));
                customer.setNama(resultSetHeader.getString("NamaCust"));
                penjualan.setCustomer(customer);

                // ==== Ambil Detail Penjualan ====
                try (PreparedStatement preparedStatementDetail = conn.prepareStatement(sqlDetail)) {
                    preparedStatementDetail.setInt(1, penjualan.getIdJualH());
                    ResultSet resultSetDetail = preparedStatementDetail.executeQuery();

                    List<Tjuald> listDetail = new ArrayList<>();
                    while (resultSetDetail.next()) {
                        Tjuald detail = new Tjuald();
                        detail.setIdJualD(resultSetDetail.getInt("IDJualD"));
                        detail.setIdJualH(resultSetDetail.getInt("IDJualH"));
                        detail.setIdItemD(resultSetDetail.getInt("IDItemD"));
                        detail.setQty(resultSetDetail.getDouble("Qty"));
                        detail.setHarga(resultSetDetail.getDouble("Harga"));
                        detail.setTotal(resultSetDetail.getDouble("Total"));
                        listDetail.add(detail);
                    }

                    penjualan.setDetails(listDetail);
                }

                listPenjualan.add(penjualan);
            }
        }

        return listPenjualan;
    }

}

