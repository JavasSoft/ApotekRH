package dao;

import model.Periode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class PeriodeDAO {
    private Connection conn;

    public PeriodeDAO(Connection conn) {
        this.conn = conn; // Pastikan koneksi yang diterima digunakan
    }

    public boolean insertPeriode(Periode periode) {
        String sqlInsert = "INSERT INTO mperiode (Kode, TanggalAwal, TanggalAkhir, Keterangan, Status, Aktif) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setString(1, periode.getKode());
            pstmt.setString(2, periode.getTanggalAwal());
            pstmt.setString(3, periode.getTanggalAkhir());
            pstmt.setString(4, periode.getKeterangan());
            pstmt.setString(5, periode.getStatus());
            pstmt.setString(6, periode.isAktif() ? "Ya" : "Tidak");
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePeriode(int idPeriode, Periode periode) {
        String sqlUpdate = "UPDATE mperiode SET Kode=?, TanggalAwal=?, TanggalAkhir=?, Keterangan=?, Status=?, Aktif=? WHERE IDPeriode=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, periode.getKode());
            pstmt.setString(2, periode.getTanggalAwal());
            pstmt.setString(3, periode.getTanggalAkhir());
            pstmt.setString(4, periode.getKeterangan());
            pstmt.setString(5, periode.getStatus());
            pstmt.setString(6, periode.isAktif() ? "Ya" : "Tidak");
            pstmt.setInt(7, idPeriode);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            return false;
        }
    }

    public List<Periode> getAllPeriode() {
        List<Periode> periodeList = new ArrayList<>();
        String sqlSelect = "SELECT * FROM mperiode";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Periode periode = new Periode(
                    rs.getString("Kode"),
                    rs.getString("TanggalAwal"),
                    rs.getString("TanggalAkhir"),
                    rs.getString("Keterangan"),
                    rs.getString("Status"),
                    rs.getString("Aktif").equals("Ya"),
                    rs.getInt("IDPeriode") // Menambahkan IDPeriode
                );
                periodeList.add(periode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return periodeList;
    }

    public int countPeriode() {
        int totalRecords = 0;
        String sqlCount = "SELECT COUNT(*) AS total FROM mperiode";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sqlCount);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return totalRecords;
    }
    
    public Periode getPeriodeByIndex(int index) {
        String sql = "SELECT * FROM mperiode ORDER BY IDPeriode ASC LIMIT ?, 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, index);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Periode(
                        rs.getString("Kode"),
                        rs.getString("TanggalAwal"),
                        rs.getString("TanggalAkhir"),
                        rs.getString("Keterangan"),
                        rs.getString("Status"),
                        rs.getString("Aktif").equals("Ya"),
                        rs.getInt("IDPeriode") // Menambahkan IDPeriode
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLastKode() {
        String lastKode = null;
        String sql = "SELECT MAX(Kode) AS max_kode FROM mperiode";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                lastKode = rs.getString("max_kode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return lastKode;
    }

    public String getLastTanggalAkhir() {
        String lastTanggalAkhir = null;
        String sql = "SELECT TanggalAkhir FROM mperiode ORDER BY TanggalAkhir DESC LIMIT 1";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                lastTanggalAkhir = rs.getString("TanggalAkhir");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return lastTanggalAkhir;
    }
}
