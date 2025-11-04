
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.Master;
import dao.*;
import model.*;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import ui.Master.BrowseAll.*;
/**
 *
 * @author Admin
 */
public class frmBarang extends javax.swing.JFrame {
    private BarangDAO barangDAO;
    private KategoriDAO kategoriDAO;
    private Connection conn;
    private Statement stat;
    private ResultSet rs;
    private String sql;
    private User user;
    private List<Barang> barangList;
    private List<Kategori> KategoriList;
    private int currentRecordIndex;
    private int totalInputs;

    /**
     * Creates new form ParentTrans
     */
    public frmBarang() {
        initComponents();
        initializeDatabase();
        btnALL();
        barangDAO = new BarangDAO(conn);
        kategoriDAO = new KategoriDAO(conn);
        jToolBar1.setFloatable(false);
        jToolBar2.setFloatable(false);
        setResizable(false); 
        setLocationRelativeTo(null);
        jPanel5.requestFocusInWindow(); 
        awal();
    }
    private void btnALL() {
         btnKategori.addActionListener(evt -> openBrowseDialog());
         btnExit.addActionListener(evr -> dispose());
         btnBrowse.addActionListener(evr -> openBrowseBarang());
    }
    private void initializeDatabase() {
        try {
            conn = Koneksi.getConnection();
            if (conn != null) {
                 stat = conn.createStatement();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to establish connection to the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error initializing database connection: " + e.getMessage());
        }
    }
    private void navaktif(){
     btnAwal.setEnabled(true);
     btnPrevious.setEnabled(true);
     btnNext.setEnabled(true);
     btnAkhir.setEnabled(true);
}
    private void navnonaktif(){
     btnAwal.setEnabled(false);
     btnPrevious.setEnabled(false);
     btnNext.setEnabled(false);
     btnAkhir.setEnabled(false);
    }
    private void IDotomatis() {
    try {
        BarangDAO barangDAO = new BarangDAO(conn); // Assuming conn is initialized
        
        String nama = jtNama.getText().trim(); 
        String duaHurufNama;
        String[] words = nama.split("\\s+"); 
        if (words.length == 1) {
            duaHurufNama = words[0].substring(0, 1).toUpperCase() + "0"; 
        } else {
            duaHurufNama = words[0].substring(0, 1).toUpperCase() + 
                           words[1].substring(0, 1).toUpperCase(); 
        }

        // Get the last code based on the prefix
        String lastCode = barangDAO.getLastKode(duaHurufNama);
        
        // Generate the new code
        int nomorUrut = 1; 
        if (lastCode != null && lastCode.startsWith(duaHurufNama)) {
            String nomorStr = lastCode.substring(2); // Extract number after the prefix
            nomorUrut = Integer.parseInt(nomorStr) + 1; 
        }
        
        // Format the number to four digits
        String nomorUrutFormatted = String.format("%04d", nomorUrut);
        
        // Create the complete code
        String newKode = duaHurufNama + nomorUrutFormatted;
        
        // Check if the new code already exists
        if (barangDAO.isKodeExists(newKode)) {
            // Handle the case where the generated code already exists
            JOptionPane.showMessageDialog(null, "Kode " + newKode + " sudah ada, silakan coba lagi.");
            return;
        }

        // Set the generated code to the jtKode field
        jtKode.setText(newKode);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error generating code: " + e.getMessage());
    }
}
    private void awal(){
        jLabel1.setText("[Browse]");
        btnSimpan.setEnabled(false);
        btnCancel.setEnabled(false);
        btnExit.setEnabled(true);
        btnTambah.setEnabled(true);
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
        jtKode.setEnabled(true);
        jtNama.setEnabled(false);
        jtSatuan.setEnabled(false);
        jtBeli.setEnabled(false);
        jtJual.setEnabled(false);
        jAreaKeteranagan.setEnabled(false);
        btnKategori.setEnabled(false);
        jtKategori.setEnabled(false);
        jtKategoriNama.setEnabled(false);
                //kategoriDAO = new KategoriDAO(conn);
        barangList =barangDAO.getAllBarang(); // Ambil semua periode
        totalInputs = barangDAO.countBarang();
//        navaktif();
        if (totalInputs > 0) {
        currentRecordIndex = totalInputs - 1; // Set ke indeks terakhir
        loadCurrentBarang(); // Muat periode terakhir
        updateRecordLabel(); // Perbarui label dengan informasi record
    }
        navaktif();
        data_terakhir();
    }
    private void simpan() {
    IDotomatis();
    String kode = jtKode.getText();
    String nama = jtNama.getText();
    int idKategori = Integer.parseInt(jTID.getText());
    String satuan = jtSatuan.getText();
    double beli = Double.parseDouble(jtBeli.getText());
    double jual = Double.parseDouble(jtJual.getText());
    String keterangan = jAreaKeteranagan.getText();
    String isAktif = cmbAktif.isSelected()? "Ya" : "Tidak";  // Set as needed

    // Buat objek Kategori
    Barang barang = new Barang();
    barang.setKode(kode);
    barang.setNama(nama);
    barang.setIDKategori(idKategori);
    barang.setSatuan(satuan);
    barang.setBeli(beli);
    barang.setJual(jual);
    barang.setKeterangan(keterangan);
    barang.setIsAktif(isAktif);
    
    BarangDAO barangDAO = new BarangDAO(conn); // Asumsikan conn adalah koneksi database Anda
    boolean isSuccess = barangDAO.insertBarang(barang);

    if (isSuccess) {
        JOptionPane.showMessageDialog(null, "Data berhasil disimpan ke database.");
        // Reload data atau update UI sesuai kebutuhan
    } else {
        JOptionPane.showMessageDialog(null, "Gagal menyimpan data ke database.");
    }
}
    private void ubahData() {
    // Get values from input fields
    int id = Integer.parseInt(jTIDBranag.getText()); // Assuming jTID contains the IDBarang
    String kode = jtKode.getText();
    String nama = jtNama.getText();
    int idKategori = Integer.parseInt(jTID.getText());
    String satuan = jtSatuan.getText();
    double beli = Double.parseDouble(jtBeli.getText());
    double jual = Double.parseDouble(jtJual.getText());
    String keterangan = jAreaKeteranagan.getText();
    String isAktif = "Ya"; // Set as needed

    // Create a Barang object with updated values
    Barang barang = new Barang();
    barang.setKode(kode);
    barang.setNama(nama);
    barang.setIDKategori(idKategori);
    barang.setSatuan(satuan);
    barang.setBeli(beli);
    barang.setJual(jual);
    barang.setKeterangan(keterangan);
    barang.setIsAktif(isAktif);

        // Update in database
        if (barangDAO.updateBarang(id, barang)) {
            JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
            //  clearFields(); // Clear input fields after updating
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data!");
        }
}
    private void tambah(){
        jLabel1.setText("[Tambah]");
        clearFields();
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(false);
        jtKode.setEnabled(true);
        jtNama.setEnabled(true);
        jTID.setText("");
        jtSatuan.setEnabled(true);
        jtBeli.setEnabled(true);
        jtJual.setEnabled(true);
        jAreaKeteranagan.setEnabled(true);
        jtKategori.setEnabled(true);
        jtKategoriNama.setEnabled(true);
        cmbAktif.setSelected(true);
        btnKategori.setEnabled(true);
        navnonaktif();
        
    }
    private void hapus(int idBarang) {
    BarangDAO barangDAO = new BarangDAO(conn); // Asumsikan conn adalah koneksi database Anda
    boolean isSuccess = barangDAO.deleteBarang(idBarang);

    if (isSuccess) {
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus dari database.");
        // Reload data atau update UI sesuai kebutuhan
        awal();
    } else {
        JOptionPane.showMessageDialog(null, "Gagal menghapus data dari database.");
    }
}
    private void clearFields() {
    jTIDBranag.setText("");
    jtKode.setText("");
    jtNama.setText("");
    jTID.setText("");
    jtSatuan.setText("");
    jtBeli.setText("");
    jtJual.setText("");
    jAreaKeteranagan.setText("");
    jtKategori.setText("");
    jtKategoriNama.setText("");
}
    private void ubah(){
        jLabel1.setText("[Ubah]");
        btnTambah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(false);
        jtSatuan.setEnabled(true);
        jtKode.setEnabled(true);
        jtNama.setEnabled(true);
        jtSatuan.setEnabled(true);
        jtBeli.setEnabled(true);
        jtJual.setEnabled(true);
        jAreaKeteranagan.setEnabled(true);
        jtKategori.setEnabled(true);
        jtKategoriNama.setEnabled(true);
        btnKategori.setEnabled(true);
        navaktif();
    }
    private void openBrowseDialog() {
        BrowseKategoriDialog dialog = new BrowseKategoriDialog(this, true, conn);
        dialog.setVisible(true);
    }
    private void openBrowseBarang() {
        BrowseBarangDialog dialog = new BrowseBarangDialog(this, true, conn);
        dialog.setVisible(true);
    }
    public void setKategoriData(String id, String kode, String nama) {
    // Set the values to your components
    jTID.setText(id);
    jtKategori.setText(kode);
    jtKategoriNama.setText(nama);
}
    public void setBarangData(int id, String kode, String nama, int idKategori,String KodeK, String NamaK, String satuan, double beli, double jual, String keterangan, boolean isAktif) {
    // Set data ke komponen di frmBarang
    jTIDBranag.setText(String.valueOf(id)); // Mengubah ID ke String
    jtKode.setText(kode); // Menggunakan kode yang benar
    jtNama.setText(nama); // Menggunakan nama yang benar
    jtSatuan.setText(satuan); // Menggunakan satuan yang benar
    jTID.setText(String.valueOf(idKategori)); // Mengubah ID Kategori ke String
    jtKategori.setText(KodeK);
    jtKategoriNama.setText(NamaK);
    jtBeli.setText(String.valueOf(beli)); // Mengubah harga beli ke String
    jtJual.setText(String.valueOf(jual)); // Mengubah harga jual ke String
    jAreaKeteranagan.setText(keterangan); // Menggunakan keterangan yang benar
    btnKategori.setText(isAktif ? "Aktif" : "Tidak Aktif"); // Mengubah teks tombol berdasarkan status aktif
}
    //[navdata]]
    private void loadCurrentBarang() {
    if (currentRecordIndex >= 0 && currentRecordIndex < barangList.size()) {
        Barang barang = barangList.get(currentRecordIndex);
        
        // Mengatur IDKategori sebagai string
        jTIDBranag.setText(String.valueOf(barang.getIDBarang())); 
        
        // Tampilkan nilai-nilai ini di komponen UI
        jtKode.setText(barang.getKode());
        jtNama.setText(barang.getNama()); // Pastikan Anda memiliki field nama
        jTID.setText(String.valueOf(barang.getIDKategori()));
        jtSatuan.setText(barang.getSatuan());
        jtBeli.setText(String.valueOf(barang.getBeli())); // Assuming jtBeli is a JTextField
        jtJual.setText(String.valueOf(barang.getJual())); // Assuming jtJual is a JTextField

        jAreaKeteranagan.setText(barang.getKeterangan());
        cmbAktif.setSelected("Ya".equalsIgnoreCase(barang.getIsAktif()));
        Kategori kategori = kategoriDAO.getKategoriById(barang.getIDKategori());
        
        if (kategori != null) {
            // Tampilkan nama dan kode kategori
            jtKategoriNama.setText(kategori.getNama()); // Pastikan Anda memiliki field untuk nama kategori
            jtKategori.setText(kategori.getKode()); // Pastikan Anda memiliki field untuk kode kategori
        } else {
            jtKategoriNama.setText(""); // Kosongkan jika tidak ditemukan
            jtKategori.setText("");
        }
        // Update the record label (assuming you have a method for this)
        updateRecordLabel();
    } else {
        JOptionPane.showMessageDialog(null, "Indeks catatan tidak valid.");
    }
}
    private void updateRecordLabel() {
    recordLabel.setText("Record: " + (currentRecordIndex + 1) + " dari " + totalInputs);
}
    public void setCurrentRecordIndex(int index) {
    this.currentRecordIndex = index; // Update current index
    loadCurrentBarang(); // Muat data barang sesuai dengan index yang baru
}

    private void next() {
    if (currentRecordIndex < totalInputs - 1) { // Pastikan tidak melebihi jumlah total input
        currentRecordIndex++;
        loadCurrentBarang();
    } else {
        JOptionPane.showMessageDialog(null, "Anda sudah berada pada record terakhir.");
    }
}
    private void previous() {
    if (currentRecordIndex > 0) { // Pastikan tidak kurang dari record pertama
        currentRecordIndex--;
        loadCurrentBarang();
    } else {
        JOptionPane.showMessageDialog(null, "Anda sudah berada pada record pertama.");
    }
}
    private void data_awal() {
    currentRecordIndex = 0; // Data pertama
    loadCurrentBarang();
}
    private void data_terakhir() {
    currentRecordIndex = totalInputs - 1; // Data terakhir
    loadCurrentBarang();
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTIDBranag = new javax.swing.JTextField();
        jTID = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        btnAwal = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnAkhir = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        recordLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cmbAktif = new javax.swing.JCheckBox();
        jtKode = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        lblKode = new javax.swing.JLabel();
        jtNama = new javax.swing.JTextField();
        lblKode1 = new javax.swing.JLabel();
        lblkategori = new javax.swing.JLabel();
        jtKategori = new javax.swing.JTextField();
        btnKategori = new javax.swing.JButton();
        jtKategoriNama = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jtSatuan = new javax.swing.JTextField();
        lblkategori1 = new javax.swing.JLabel();
        lblkategori2 = new javax.swing.JLabel();
        jtBeli = new javax.swing.JTextField();
        jtJual = new javax.swing.JTextField();
        lblkategori3 = new javax.swing.JLabel();
        lblkategori4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jAreaKeteranagan = new javax.swing.JEditorPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        jTIDBranag.setText("jTextField1");

        jTID.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Master Barang");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 102, 255));

        jPanel3.setBackground(new java.awt.Color(94, 169, 245));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setMinimumSize(new java.awt.Dimension(100, 25));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 25));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Mode Aktif ");

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("[Browse]");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel4.getAccessibleContext().setAccessibleName("Mode Aktif :");

        jToolBar2.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar2.setRollover(true);

        btnAwal.setText("<<");
        btnAwal.setFocusable(false);
        btnAwal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAwal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAwalActionPerformed(evt);
            }
        });
        jToolBar2.add(btnAwal);

        btnPrevious.setText("<");
        btnPrevious.setFocusable(false);
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });
        jToolBar2.add(btnPrevious);

        btnNext.setText(">");
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        jToolBar2.add(btnNext);

        btnAkhir.setText(">>");
        btnAkhir.setFocusable(false);
        btnAkhir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAkhir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAkhir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAkhirActionPerformed(evt);
            }
        });
        jToolBar2.add(btnAkhir);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jToolBar1.setBackground(new java.awt.Color(94, 169, 245));
        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(100, 25));
        jToolBar1.setMinimumSize(new java.awt.Dimension(100, 25));

        btnTambah.setText("Tambah");
        btnTambah.setFocusable(false);
        btnTambah.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTambah.setMaximumSize(new java.awt.Dimension(60, 30));
        btnTambah.setMinimumSize(new java.awt.Dimension(60, 30));
        btnTambah.setPreferredSize(new java.awt.Dimension(60, 30));
        btnTambah.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        jToolBar1.add(btnTambah);

        btnUbah.setText("Ubah");
        btnUbah.setFocusable(false);
        btnUbah.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUbah.setMaximumSize(new java.awt.Dimension(53, 30));
        btnUbah.setMinimumSize(new java.awt.Dimension(53, 30));
        btnUbah.setPreferredSize(new java.awt.Dimension(53, 30));
        btnUbah.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        jToolBar1.add(btnUbah);

        btnHapus.setText("Hapus");
        btnHapus.setFocusable(false);
        btnHapus.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHapus.setMaximumSize(new java.awt.Dimension(53, 30));
        btnHapus.setMinimumSize(new java.awt.Dimension(53, 30));
        btnHapus.setPreferredSize(new java.awt.Dimension(53, 30));
        btnHapus.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        jToolBar1.add(btnHapus);

        recordLabel.setBackground(new java.awt.Color(0, 0, 0));
        recordLabel.setForeground(new java.awt.Color(0, 51, 255));
        recordLabel.setText("1 of 9999");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(recordLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(recordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jPanel4.setBackground(new java.awt.Color(204, 255, 255));

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnExit.setText("Exit Ctrl + X");

        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(92, 23));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        cmbAktif.setText("Data Aktif");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(cmbAktif)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit)
                .addGap(29, 29, 29))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cmbAktif))
        );

        jtKode.setMaximumSize(new java.awt.Dimension(64, 22));
        jtKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtKodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtKodeFocusLost(evt);
            }
        });

        btnBrowse.setText("jButton11");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        lblKode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode.setText("Kode :");
        lblKode.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode.setPreferredSize(new java.awt.Dimension(33, 22));

        jtNama.setMaximumSize(new java.awt.Dimension(64, 23));
        jtNama.setMinimumSize(new java.awt.Dimension(64, 23));
        jtNama.setPreferredSize(new java.awt.Dimension(64, 23));
        jtNama.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtNamaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtNamaFocusLost(evt);
            }
        });

        lblKode1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode1.setText("Nama Barang :");
        lblKode1.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode1.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode1.setPreferredSize(new java.awt.Dimension(33, 22));

        lblkategori.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblkategori.setText("Kategori :");
        lblkategori.setMaximumSize(new java.awt.Dimension(33, 22));
        lblkategori.setMinimumSize(new java.awt.Dimension(33, 22));
        lblkategori.setPreferredSize(new java.awt.Dimension(33, 22));

        jtKategori.setMaximumSize(new java.awt.Dimension(64, 23));
        jtKategori.setMinimumSize(new java.awt.Dimension(64, 23));
        jtKategori.setPreferredSize(new java.awt.Dimension(64, 23));
        jtKategori.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtKategoriFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtKategoriFocusLost(evt);
            }
        });

        btnKategori.setText("jButton12");
        btnKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKategoriActionPerformed(evt);
            }
        });

        jtKategoriNama.setMaximumSize(new java.awt.Dimension(64, 23));
        jtKategoriNama.setMinimumSize(new java.awt.Dimension(64, 23));
        jtKategoriNama.setPreferredSize(new java.awt.Dimension(64, 23));
        jtKategoriNama.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtKategoriNamaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtKategoriNamaFocusLost(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jtSatuan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtSatuan.setMaximumSize(new java.awt.Dimension(64, 22));
        jtSatuan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtSatuanFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtSatuanFocusLost(evt);
            }
        });

        lblkategori1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblkategori1.setText("Satuan :");
        lblkategori1.setMaximumSize(new java.awt.Dimension(33, 22));
        lblkategori1.setMinimumSize(new java.awt.Dimension(33, 22));
        lblkategori1.setPreferredSize(new java.awt.Dimension(33, 22));

        lblkategori2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblkategori2.setText("Harga Beli :");
        lblkategori2.setMaximumSize(new java.awt.Dimension(33, 22));
        lblkategori2.setMinimumSize(new java.awt.Dimension(33, 22));
        lblkategori2.setPreferredSize(new java.awt.Dimension(33, 22));

        jtBeli.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtBeli.setMaximumSize(new java.awt.Dimension(64, 22));
        jtBeli.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtBeliFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtBeliFocusLost(evt);
            }
        });

        jtJual.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtJual.setMaximumSize(new java.awt.Dimension(64, 22));
        jtJual.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtJualFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtJualFocusLost(evt);
            }
        });

        lblkategori3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblkategori3.setText("Harga Jual :");
        lblkategori3.setMaximumSize(new java.awt.Dimension(33, 22));
        lblkategori3.setMinimumSize(new java.awt.Dimension(33, 22));
        lblkategori3.setPreferredSize(new java.awt.Dimension(33, 22));

        lblkategori4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblkategori4.setText("Keterangan :");
        lblkategori4.setMaximumSize(new java.awt.Dimension(33, 22));
        lblkategori4.setMinimumSize(new java.awt.Dimension(33, 22));
        lblkategori4.setPreferredSize(new java.awt.Dimension(33, 22));

        jScrollPane1.setViewportView(jAreaKeteranagan);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lblkategori4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(lblkategori1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addComponent(lblkategori3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jtJual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addComponent(lblkategori2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jtBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblkategori1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtBeli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblkategori2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtJual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblkategori3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblkategori4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Detail", jPanel6);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblKode, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblkategori, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblKode1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jtKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(btnKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtKategoriNama, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKode1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtKategori, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtKategoriNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblkategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addGap(10, 10, 10)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jMenu1.setText("System");

        jMenuItem1.setText("Close Ctrl + X");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        tambah();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        // TODO add your handling code here:
        ubah();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void jtKodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtKodeFocusGained
        // TODO add your handling code here:
        if (jtKode.getText().equals("Kode")) {
            jtKode.setText("");
            jtKode.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_jtKodeFocusGained

    private void jtKodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtKodeFocusLost
        // TODO add your handling code here:
        if (jtKode.getText().equals("")) {
            jtKode.setText("Kode");
            jtKode.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_jtKodeFocusLost

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnBrowseActionPerformed

    private void jtNamaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtNamaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtNamaFocusGained

    private void jtNamaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtNamaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtNamaFocusLost

    private void jtKategoriFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtKategoriFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtKategoriFocusGained

    private void jtKategoriFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtKategoriFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtKategoriFocusLost

    private void btnKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKategoriActionPerformed
        // TODO add your handling code here:
    
    }//GEN-LAST:event_btnKategoriActionPerformed

    private void jtKategoriNamaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtKategoriNamaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtKategoriNamaFocusGained

    private void jtKategoriNamaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtKategoriNamaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtKategoriNamaFocusLost

    private void jtSatuanFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtSatuanFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtSatuanFocusGained

    private void jtSatuanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtSatuanFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtSatuanFocusLost

    private void jtBeliFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtBeliFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBeliFocusGained

    private void jtBeliFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtBeliFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBeliFocusLost

    private void jtJualFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtJualFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtJualFocusGained

    private void jtJualFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtJualFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtJualFocusLost

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
            String action = jLabel1.getText(); // Mendapatkan teks dari JLabel
         if (action.equals("[Tambah]")) {
        simpan(); // Jalankan metode simpan jika teks adalah "Tambah"
        } else if (action.equals("[Ubah]")) {
             ubahData(); // Jalankan metode ubah jika teks adalah "Ubah"
       } else {
        JOptionPane.showMessageDialog(null, "Aksi tidak dikenali: " + action);
    }
         awal();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnAwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAwalActionPerformed
        // TODO add your handling code here:
        data_awal();
    }//GEN-LAST:event_btnAwalActionPerformed

    private void btnAkhirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAkhirActionPerformed
        // TODO add your handling code here:
        data_terakhir();
    }//GEN-LAST:event_btnAkhirActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        previous();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
         int idBarang = Integer.parseInt(jTIDBranag.getText()); // Ambil ID dari komponen UI
    hapus(idBarang); // Panggil metode hapus dengan ID barang
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        awal();
    }//GEN-LAST:event_btnCancelActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ParentTrans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ParentTrans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ParentTrans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ParentTrans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ParentTrans().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAkhir;
    private javax.swing.JButton btnAwal;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKategori;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JCheckBox cmbAktif;
    private javax.swing.JEditorPane jAreaKeteranagan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTID;
    private javax.swing.JTextField jTIDBranag;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField jtBeli;
    private javax.swing.JTextField jtJual;
    private javax.swing.JTextField jtKategori;
    private javax.swing.JTextField jtKategoriNama;
    private javax.swing.JTextField jtKode;
    private javax.swing.JTextField jtNama;
    private javax.swing.JTextField jtSatuan;
    private javax.swing.JLabel lblKode;
    private javax.swing.JLabel lblKode1;
    private javax.swing.JLabel lblkategori;
    private javax.swing.JLabel lblkategori1;
    private javax.swing.JLabel lblkategori2;
    private javax.swing.JLabel lblkategori3;
    private javax.swing.JLabel lblkategori4;
    private javax.swing.JLabel recordLabel;
    // End of variables declaration//GEN-END:variables
}
