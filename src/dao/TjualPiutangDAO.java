package dao;

import java.sql.*;
import model.TjualPiutang;

public class TjualPiutangDAO {
    private Connection conn;

    public TjualPiutangDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(TjualPiutang p) throws Exception {
        String sql = "INSERT INTO tjualpiutang (IDJualH, IDCust, NoFaktur, Tanggal, JatuhTempo, SisaPiutang) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, p.getIdJualH());
        ps.setInt(2, p.getIdCust());
        ps.setString(3, p.getNoFaktur());
        ps.setDate(4, p.getTanggal());
        ps.setDate(5, p.getJatuhTempo());
        ps.setDouble(6, p.getSisaPiutang());

        ps.executeUpdate();
    }
}
