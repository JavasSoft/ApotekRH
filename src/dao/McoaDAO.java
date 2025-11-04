/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.util.List;
import model.Mcoa;

public interface McoaDAO {
    // Method untuk menambah data
    void insert(Mcoa mcoa) throws Exception;

    // Method untuk mengambil semua data
    List<Mcoa> getAll() throws Exception;

    // Method untuk mengambil data berdasarkan kode akun
    Mcoa getByKodeAkun(String kodeAkun) throws Exception;

    // Method untuk update data
    void update(Mcoa mcoa) throws Exception;

    // Method untuk menghapus data
    void delete(String kodeAkun) throws Exception;
}
