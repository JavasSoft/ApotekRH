package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Koneksi {
    // Variabel untuk menyimpan koneksi yang telah dibuat
    private static Connection koneksi;

    // Method untuk mendapatkan koneksi ke database
    public static Connection getConnection() {
        // Jika koneksi belum dibuat, buat koneksi baru
        if (koneksi == null) {
            try {
                // Membaca konfigurasi dari file dbconfig.properties
                Properties props = new Properties();
                FileInputStream in = new FileInputStream("src/dbconfig.properties");
                props.load(in);
                in.close();

                // Mendapatkan properti dari file konfigurasi
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                // Membuat koneksi menggunakan DriverManager
                koneksi = DriverManager.getConnection(url, user, password);
//                System.out.println("Koneksi berhasil!");
            } catch (IOException | SQLException e) {
                // Tangani kesalahan jika koneksi gagal
                System.err.println("Koneksi gagal: " + e.getMessage());
                System.exit(0);
            }
        }
        // Mengembalikan objek koneksi
        return koneksi;
    }

    // Method untuk menutup koneksi
    public static void closeConnection() {
        if (koneksi != null) {
            try {
                koneksi.close();
                System.out.println("Koneksi ditutup.");
            } catch (SQLException e) {
                System.err.println("Tidak dapat menutup koneksi: " + e.getMessage());
            } finally {
                koneksi = null;
            }
        }
    }
}