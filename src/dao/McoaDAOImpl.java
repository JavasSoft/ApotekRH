package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Mcoa;

public class McoaDAOImpl implements McoaDAO {

    private Connection connection;

    // Constructor untuk inisialisasi koneksi
    public McoaDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Mcoa mcoa) throws Exception {
        String query = "INSERT INTO mcoa (kode_akun, nama_akun, saldo_normal, tipe_akun_id, jenis_trans, arus_kas, keterangan, data_aktif, subledger, parent_kode_akun) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, mcoa.getKodeAkun());
            stmt.setString(2, mcoa.getNamaAkun());
            stmt.setString(3, mcoa.getSaldoNormal());
            stmt.setInt(4, mcoa.getTipeAkunId());
            stmt.setString(5, mcoa.getJenisTrans());
            stmt.setString(6, mcoa.getArusKas());
            stmt.setString(7, mcoa.getKeterangan());
            stmt.setBoolean(8, mcoa.isDataAktif());
            stmt.setBoolean(9, mcoa.isSubledger());
            stmt.setString(10, mcoa.getParentKodeAkun());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Mcoa> getAll() throws Exception {
        String query = "SELECT * FROM mcoa";
        List<Mcoa> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Mcoa mcoa = new Mcoa(
                        rs.getString("kode_akun"),
                        rs.getString("nama_akun"),
                        rs.getString("saldo_normal"),
                        rs.getInt("tipe_akun_id"),
                        rs.getString("jenis_trans"),
                        rs.getString("arus_kas"),
                        rs.getString("keterangan"),
                        rs.getBoolean("data_aktif"),
                        rs.getBoolean("subledger"),
                        rs.getString("parent_kode_akun")
                );
                list.add(mcoa);
            }
        }
        return list;
    }

    @Override
    public Mcoa getByKodeAkun(String kodeAkun) throws Exception {
        String query = "SELECT * FROM mcoa WHERE kode_akun = ?";
        Mcoa mcoa = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, kodeAkun);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mcoa = new Mcoa(
                            rs.getString("kode_akun"),
                            rs.getString("nama_akun"),
                            rs.getString("saldo_normal"),
                            rs.getInt("tipe_akun_id"),
                            rs.getString("jenis_trans"),
                            rs.getString("arus_kas"),
                            rs.getString("keterangan"),
                            rs.getBoolean("data_aktif"),
                            rs.getBoolean("subledger"),
                            rs.getString("parent_kode_akun")
                    );
                }
            }
        }
        return mcoa;
    }

    @Override
    public void update(Mcoa mcoa) throws Exception {
        String query = "UPDATE mcoa SET nama_akun = ?, saldo_normal = ?, tipe_akun_id = ?, jenis_trans = ?, arus_kas = ?, keterangan = ?, data_aktif = ?, subledger = ?, parent_kode_akun = ? WHERE kode_akun = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, mcoa.getNamaAkun());
            stmt.setString(2, mcoa.getSaldoNormal());
            stmt.setInt(3, mcoa.getTipeAkunId());
            stmt.setString(4, mcoa.getJenisTrans());
            stmt.setString(5, mcoa.getArusKas());
            stmt.setString(6, mcoa.getKeterangan());
            stmt.setBoolean(7, mcoa.isDataAktif());
            stmt.setBoolean(8, mcoa.isSubledger());
            stmt.setString(9, mcoa.getParentKodeAkun());
            stmt.setString(10, mcoa.getKodeAkun());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String kodeAkun) throws Exception {
        String query = "DELETE FROM mcoa WHERE kode_akun = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, kodeAkun);
            stmt.executeUpdate();
        }
    }
}
