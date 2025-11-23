package dao;

import model.Tbelih;
import model.Tbelid;
import model.Item;
import model.ItemDetail;
import model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TbelihDAO {
    private Connection conn;
    private Supplier supplier; // <-- objek Supplier

    public TbelihDAO(Connection conn) {
        this.conn = conn;
    }

    public String getLastKode(String prefix) throws SQLException {
        String sql = "SELECT Kode FROM tbelih WHERE Kode LIKE ? ORDER BY Kode DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Kode");
            }
        }
        return null;
    }

    public void insert(Tbelih beli) throws SQLException {
        String sql = "INSERT INTO tbelih (Kode, IDSupplier, Tanggal, JenisBayar, JatuhTempo, " +
                     "SubTotal, Diskon, Ppn, Total, Nominal, Status, InsertUser) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, beli.getKode());
            ps.setInt(2, beli.getIdSupplier());
            ps.setDate(3, beli.getTanggal());
            ps.setString(4, beli.getJenisBayar());
            ps.setDate(5, beli.getJatuhTempo());
            ps.setDouble(6, beli.getSubTotal());
            ps.setDouble(7, beli.getDiskon());
            ps.setDouble(8, beli.getPpn());
            ps.setDouble(9, beli.getTotal());
            ps.setDouble(10, beli.getNominal());
            ps.setString(11, beli.getStatus());
            ps.setString(12, beli.getInsertUser());
            ps.executeUpdate();
        }
    }

    public List<Tbelih> getAll() throws SQLException {
        List<Tbelih> list = new ArrayList<>();
        String sqlHeader = "SELECT * FROM tbelih";
        String sqlDetail = "SELECT * FROM tbelid WHERE IDBeliH = ?";

        try (PreparedStatement psHeader = conn.prepareStatement(sqlHeader);
             ResultSet rsHeader = psHeader.executeQuery()) {

            while (rsHeader.next()) {
                Tbelih h = new Tbelih();
                h.setIdBeliH(rsHeader.getInt("IDBeliH"));
                h.setKode(rsHeader.getString("Kode"));
                h.setIdSupplier(rsHeader.getInt("IDSupplier"));
                h.setTanggal(rsHeader.getDate("Tanggal"));
                h.setJenisBayar(rsHeader.getString("JenisBayar"));
                h.setJatuhTempo(rsHeader.getDate("JatuhTempo"));
                h.setSubTotal(rsHeader.getDouble("SubTotal"));
                h.setDiskon(rsHeader.getDouble("Diskon"));
                h.setPpn(rsHeader.getDouble("Ppn"));
                h.setTotal(rsHeader.getDouble("Total"));
                h.setNominal(rsHeader.getDouble("Nominal"));
                h.setStatus(rsHeader.getString("Status"));
                h.setInsertUser(rsHeader.getString("InsertUser"));
                h.setUpdateUser(rsHeader.getString("UpdateUser"));

                // Ambil detail
                try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                    psDetail.setInt(1, h.getIdBeliH());
                    ResultSet rsDetail = psDetail.executeQuery();

                    List<Tbelid> details = new ArrayList<>();
                    while (rsDetail.next()) {
                        Tbelid d = new Tbelid();
                        d.setIdBeliD(rsDetail.getInt("IDBeliD"));
                        d.setIdBeliH(rsDetail.getInt("IDBeliH"));
                        d.setIdItemD(rsDetail.getInt("IDItemD"));
                        d.setQty(rsDetail.getDouble("Qty"));
                        d.setHarga(rsDetail.getDouble("Harga"));
                        d.setDiskon(rsDetail.getDouble("Diskon"));
                        d.setTotal(rsDetail.getDouble("Total"));
                        details.add(d);
                    }
                    h.setDetails(details);
                }

                list.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Tbelih getById(int idBeliH) {
        Tbelih h = null;
        String sqlHeader = "SELECT * FROM tbelih WHERE IDBeliH = ?";
        String sqlDetail = "SELECT * FROM tbelid WHERE IDBeliH = ?";

        try (PreparedStatement ps = conn.prepareStatement(sqlHeader)) {
            ps.setInt(1, idBeliH);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                h = new Tbelih();
                h.setIdBeliH(rs.getInt("IDBeliH"));
                h.setKode(rs.getString("Kode"));
                h.setIdSupplier(rs.getInt("IDSupplier"));
                h.setTanggal(rs.getDate("Tanggal"));
                h.setJenisBayar(rs.getString("JenisBayar"));
                h.setJatuhTempo(rs.getDate("JatuhTempo"));
                h.setSubTotal(rs.getDouble("SubTotal"));
                h.setDiskon(rs.getDouble("Diskon"));
                h.setPpn(rs.getDouble("Ppn"));
                h.setTotal(rs.getDouble("Total"));
                h.setNominal(rs.getDouble("Nominal"));
                h.setStatus(rs.getString("Status"));
            }

            if (h != null) {
                try (PreparedStatement psd = conn.prepareStatement(sqlDetail)) {
                    psd.setInt(1, idBeliH);
                    ResultSet rsd = psd.executeQuery();
                    List<Tbelid> details = new ArrayList<>();
                    while (rsd.next()) {
                        Tbelid d = new Tbelid();
                        d.setIdBeliD(rsd.getInt("IDBeliD"));
                        d.setIdBeliH(rsd.getInt("IDBeliH"));
                        d.setIdItemD(rsd.getInt("IDItemD"));
                        d.setQty(rsd.getDouble("Qty"));
                        d.setHarga(rsd.getDouble("Harga"));
                        d.setDiskon(rsd.getDouble("Diskon"));
                        d.setTotal(rsd.getDouble("Total"));
                        details.add(d);
                    }
                    h.setDetails(details);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return h;
    }

    public void updateStatus(int idBeliH, String status) throws SQLException {
        String sql = "UPDATE tbelih SET Status = ?, UpdateUser = ? WHERE IDBeliH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, "system");
            ps.setInt(3, idBeliH);
            ps.executeUpdate();
        }
    }

    public List<Tbelih> getBySupplier(int idSupplier) throws SQLException {
        List<Tbelih> list = new ArrayList<>();
        String sql = "SELECT * FROM tbelih WHERE IDSupplier = ? ORDER BY Tanggal DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSupplier);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tbelih t = mapResultSetToTbelih(rs);
                t.setDetails(getDetailsByBeliH(t.getIdBeliH()));
                list.add(t);
            }
        }

        return list;
    }

    private Tbelih mapResultSetToTbelih(ResultSet rs) throws SQLException {
        Tbelih h = new Tbelih();
        h.setIdBeliH(rs.getInt("IDBeliH"));
        h.setKode(rs.getString("Kode"));
        h.setIdSupplier(rs.getInt("IDSupplier"));
        h.setTanggal(rs.getDate("Tanggal"));
        h.setJenisBayar(rs.getString("JenisBayar"));
        h.setJatuhTempo(rs.getDate("JatuhTempo"));
        h.setSubTotal(rs.getDouble("SubTotal"));
        h.setDiskon(rs.getDouble("Diskon"));
        h.setPpn(rs.getDouble("Ppn"));
        h.setTotal(rs.getDouble("Total"));
        h.setNominal(rs.getDouble("Nominal"));
        h.setStatus(rs.getString("Status"));
        h.setInsertUser(rs.getString("InsertUser"));
        h.setUpdateUser(rs.getString("UpdateUser"));
        return h;
    }

    private List<Tbelid> getDetailsByBeliH(int idBeliH) throws SQLException {
        List<Tbelid> list = new ArrayList<>();
        String sql = "SELECT * FROM tbelid WHERE IDBeliH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBeliH);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tbelid d = new Tbelid();
                d.setIdBeliD(rs.getInt("IDBeliD"));
                d.setIdBeliH(rs.getInt("IDBeliH"));
                d.setIdItemD(rs.getInt("IDItemD"));
                d.setQty(rs.getDouble("Qty"));
                d.setHarga(rs.getDouble("Harga"));
                d.setDiskon(rs.getDouble("Diskon"));
                d.setTotal(rs.getDouble("Total"));
                list.add(d);
            }
        }
        return list;
    }

public Tbelih getByIdSimple(int idBeliH) throws SQLException {
    String sql = "SELECT * FROM tbelih WHERE IDBeliH = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idBeliH);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToTbelih(rs); // mapping ke Tbelih
        }
    }
    return null;
}

}
