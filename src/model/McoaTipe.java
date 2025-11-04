/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class McoaTipe {
    private int id;
    private String namaTipe;

    // Constructor
    public McoaTipe(int id, String namaTipe) {
        this.id = id;
        this.namaTipe = namaTipe;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaTipe() {
        return namaTipe;
    }

    public void setNamaTipe(String namaTipe) {
        this.namaTipe = namaTipe;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "McoaTipe{" +
                "id=" + id +
                ", namaTipe='" + namaTipe + '\'' +
                '}';
    }
}
