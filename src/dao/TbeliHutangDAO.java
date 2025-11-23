package dao;

import java.sql.*;
import model.TbeliHutang;

public class TbeliHutangDAO {
    private Connection conn;

    public TbeliHutangDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(TbeliHutang h) throws Exception {
        String sql = "INSERT INTO tbelihutang (IDBeliH, IDSupplier, NoFaktur, Tanggal, JatuhTempo, SisaHutang) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, h.getIdBeliH());
            ps.setInt(2, h.getIdSupplier());
            ps.setString(3, h.getNoFaktur());
            ps.setDate(4, h.getTanggal());
            ps.setDate(5, h.getJatuhTempo());
            ps.setDouble(6, h.getSisaHutang());

            ps.executeUpdate();
        }
    }
}
