package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Koneksi {
    private static Connection koneksi;

    public static Connection getConnection() {
        if (koneksi == null) {
            try {
                Properties props = new Properties();
                FileInputStream in = new FileInputStream("src/dbconfig.properties");
                props.load(in);
                in.close();

                String host = props.getProperty("db.host");
                String port = props.getProperty("db.port");
                String dbName = props.getProperty("db.name");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                // Buat URL JDBC dengan port
                String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName +
                             "?useSSL=false&serverTimezone=UTC";

                koneksi = DriverManager.getConnection(url, user, password);

            } catch (IOException | SQLException e) {
                System.err.println("Koneksi gagal: " + e.getMessage());
                System.exit(0);
            }
        }
        return koneksi;
    }

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
