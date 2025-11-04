/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.util.List;
import model.McoaTipe;

public interface McoaTipeDAO {
    // Method untuk menambah data
    void insert(McoaTipe mcoaTipe) throws Exception;

    // Method untuk mengambil semua data
    List<McoaTipe> getAll() throws Exception;

    // Method untuk mengambil data berdasarkan id
    McoaTipe getById(int id) throws Exception;

    // Method untuk update data
    void update(McoaTipe mcoaTipe) throws Exception;

    // Method untuk menghapus data
    void delete(int id) throws Exception;
}
