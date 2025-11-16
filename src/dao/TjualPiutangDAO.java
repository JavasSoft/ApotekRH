package dao;

import java.sql.*;
import model.TjualPiutang;

public class TjualPiutangDAO {
    private Connection conn;

    public TjualPiutangDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(TjualPiutang p) throws Exception {
        String sql = "INSERT INTO tjualpiutang (IDJualH, NoFaktur, Tanggal, JatuhTempo, SisaPiutang) "
                   + "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, p.getIdJualH());
        ps.setString(2, p.getNoFaktur());
        ps.setDate(3, p.getTanggal());
        ps.setDate(4, p.getJatuhTempo());
        ps.setDouble(5, p.getSisaPiutang());

        ps.executeUpdate();
    }
}
