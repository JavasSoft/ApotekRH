/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.Master;
import java.sql.Statement;
import dao.ItemDAO;
import dao.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.*;
import model.User;
import ui.Master.BrowseAll.BrowseItem;
/**
 *
 * @author Admin
 */
public class frmMstItem extends javax.swing.JFrame {
    
    private ItemDAO itemDAO;
    private Connection conn;
    private Statement stat;
    private ResultSet rs;
    private String sql;
    private User user;
   // private LisList;
      private List<Item> itemsList = new ArrayList<>();
    private int currentRecordIndex;
    private int totalInputs;

    /**
     * Creates new form ParentTrans
     */
    
    public frmMstItem() {
        initComponents();
        initializeDatabase();
        itemDAO = new ItemDAO(conn);
        jToolBar1.setFloatable(false);
        jToolBar2.setFloatable(false);
        btnALL();
        awal();
        FrmProjec();
        FormShow();
    }
private void IDotomatis() {
    try {
        // Ambil kode terakhir dari tabel berdasarkan prefix "DR"
        String lastCode = itemDAO.getLastKode("OBT"); // kirim prefix ke DAO
        String newCode;

        if (lastCode != null && lastCode.startsWith("OBT")) {
            // Ambil angka di belakang "DR"
            int number = Integer.parseInt(lastCode.substring(3));
            number++; // tambah 1
            newCode = String.format("OBT%04d", number); // hasil: DR001, DR002, dst
        } else {
            // Jika belum ada data sama sekali
            newCode = "OBT001";
        }

        jtKode.setText(newCode);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
    }
}


    private void FormShow(){
         loadDataFromDatabase();
         loadCurrentItem(); 
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
    
    private void btnALL() {
        btnSimpan.addActionListener(evt -> simpan());
         btnExit.addActionListener(evr -> dispose());
         btnTambah.addActionListener(evt -> tambah());
         btnUbah.addActionListener(evt -> ubah());
         btnAwal.addActionListener(evt -> data_awal());
         btnPrevious.addActionListener(evt -> previous());
         btnNext.addActionListener(evt -> next());
         btnAkhir.addActionListener(evt -> data_terakhir());
        
    }
    
    private void FrmProjec(){
    this.setLocationRelativeTo(null);
    this.setResizable(false);
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
    private void awal(){
        jLabel1.setText("[Browse]");
        btnSimpan.setEnabled(false);
        btnCancel.setEnabled(false);
        btnExit.setEnabled(false);
        btnTambah.setEnabled(true);
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
        navaktif();
        //  customerDAO = new CustomerDAO(conn);
      itemsList = itemDAO.getAllItems(); // Ambil semua periode
       totalInputs = itemDAO.countItems();

        if (totalInputs > 0) {
            currentRecordIndex = totalInputs - 1; // Set ke indeks terakhir
            loadCurrentItem(); // Muat periode terakhir
            updateRecordLabel(); // Perbarui label dengan informasi record
        }
        navaktif();
        data_terakhir();
    }
    
    private void tambah(){
        jLabel1.setText("[Tambah]");
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(true);
        Kosong();
        navnonaktif();  
    }
    private void Kosong(){
        
    txtNama.requestFocus();
            jtKode.setText("");
    txtNama.setText("");
   
   cmbAktif.setSelected(true);
    }
    
    private void ubah(){
        jLabel1.setText("[Ubah]");
        btnTambah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(true);
        navaktif();
    }
    
    // ===========================================
// ============ CRU ==================
// ===========================================
private void simpan() {
    String action = jLabel1.getText(); // Mendapatkan teks dari JLabel

    if (action.equals("[Tambah]")) {
        saveItem();
    } else if (action.equals("[Ubah]")) {
        updateItem();
    } else {
        JOptionPane.showMessageDialog(null, "Aksi tidak dikenali: " + action);
    }

    awal(); // Reset form / state awal
}

// ===========================================
// ============ SIMPA =================
// ===========================================
private void saveItem() {
    try {
        IDotomatis();
        String kode = jtKode.getText().trim();
        String nama = txtNama.getText().trim();
        String kategori = cmbKategori.getSelectedItem().toString();
        double stok = Double.parseDouble(txtStok.getText().trim());
        double hargaBeli = Double.parseDouble(txtHargaBeli.getText().trim());
        String satuanBesar = cmbSatuanBesar.getSelectedItem().toString();
        String satuanKecil = cmbSatuanKecil.getSelectedItem().toString();
        double hargaJual = Double.parseDouble(txtHargaJual.getText().trim());
        double labaPersen = Double.parseDouble(txtLabaPersen.getText().trim());
        int konversi = Integer.parseInt(txtKonversi.getText().trim());
        int aktif = cmbAktif.isSelected() ? 1 : 0;

        // Validasi input
        if (kode.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode dan Nama wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Buat objek Item (master)
        Item item = new Item();
        item.setKode(kode);
        item.setNama(nama);
        item.setKategori(kategori);
        item.setStok(stok);
        item.setHargaBeli(hargaBeli);
        item.setAktif(aktif);

        // Buat list detail satuan
        List<ItemDetail> listDetail = new ArrayList<>();

        // Buat objek detail
        ItemDetail detail = new ItemDetail();
        detail.setSatuanBesar(satuanBesar);
        detail.setJumlah(1);
        detail.setSatuan(satuanKecil);
        detail.setKonversi(konversi);
        detail.setHargaJual(hargaJual);
        detail.setLabaPersen(labaPersen);

        // Tambahkan detail ke list
        listDetail.add(detail);

        // Masukkan list detail ke dalam Item
        item.setDetails(listDetail);

        // Simpan ke database lewat DAO
        ItemDAO dao = new ItemDAO(conn);
        boolean success = dao.insertItem(item);

        if (success) {
            JOptionPane.showMessageDialog(this, "Data item berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadDataFromDatabase();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data item.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}



// ===========================================
// ============ UPDAT =================
// ===========================================
private void updateItem() {
    try {
        int IDItem = Integer.parseInt(txtIDItem.getText().trim());
        String kode = jtKode.getText().trim();
        String nama = txtNama.getText().trim();
        String kategori = cmbKategori.getSelectedItem().toString();
        double stok = Double.parseDouble(txtStok.getText().trim());
        String satuanBesar = cmbSatuanBesar.getSelectedItem().toString();
        double hargaBeli = Double.parseDouble(txtHargaBeli.getText().trim());
        String satuanKecil = cmbSatuanKecil.getSelectedItem().toString();
        double hargaJual = Double.parseDouble(txtHargaJual.getText().trim());
        double labaPersen = Double.parseDouble(txtLabaPersen.getText().trim());
        int konversi = Integer.parseInt(txtKonversi.getText().trim());
        int aktif = cmbAktif.isSelected() ? 1 : 0;

        if (kode.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode dan Nama wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Siapkan objek Item & Detail ---
        Item item = new Item();
        item.setIDItem(IDItem); // 🟢 tambahkan baris ini
        item.setKode(kode);
        item.setNama(nama);
        item.setKategori(kategori);
        item.setStok(stok);
        item.setHargaBeli(hargaBeli);
        item.setAktif(aktif);

        ItemDetail detail = new ItemDetail();
        detail.setIDItem(IDItem);
        detail.setSatuanBesar(satuanBesar);
        detail.setSatuan(satuanKecil);
        detail.setKonversi(konversi);
        detail.setHargaJual(hargaJual);
        detail.setLabaPersen(labaPersen);

        // --- Simpan ke database lewat DAO ---
        ItemDAO dao = new ItemDAO(conn);
        boolean success = dao.updateItemWithDetail(item, detail);

        if (success) {
            JOptionPane.showMessageDialog(this, "Data item berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data item.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Pastikan semua angka diisi dengan benar!", "Input Error", JOptionPane.WARNING_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void loadDataFromDatabase() {
    itemsList = itemDAO.getAllItems();
    if (itemsList == null) {
        itemsList = new ArrayList<>();
    }

    if (itemsList.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Data Item belum ada di database.");
    } else {
        currentRecordIndex = 0;  // <<< ini penting
    }
}

public void setItemData(int idItem, String kode, String nama, String kategori, double stok, double hargaBeli, double hargaJual, boolean aktif, String satuanKecil, String satuanBesar, double labaPersen,  double konversi) {

    // === Set data master ===
    txtIDItem.setText(String.valueOf(idItem));
    jtKode.setText(kode);
    txtNama.setText(nama);
    cmbKategori.setSelectedItem(kategori);
    txtStok.setText(String.valueOf(stok));
    txtHargaBeli.setText(String.valueOf(hargaBeli));
    cmbAktif.setSelected(aktif);

    // === Set data detail ===
    cmbSatuanKecil.setSelectedItem(satuanKecil);
    cmbSatuanBesar.setSelectedItem(satuanBesar);
    txtHargaJual.setText(String.valueOf(hargaJual));
    txtLabaPersen.setText(String.valueOf(labaPersen));
    txtKonversi.setText(String.valueOf(konversi));

    // === Update state internal (opsional) ===
    for (int i = 0; i < itemsList.size(); i++) {
        if (itemsList.get(i).getIDItem() == idItem) {
            currentRecordIndex = i;
            updateRecordLabel();
            break;
        }
    }
}



private void loadCurrentItem() {
    if (currentRecordIndex >= 0 && currentRecordIndex < itemsList.size()) {
        Item item = itemsList.get(currentRecordIndex);

        // ==== Tampilkan data master ====
        txtIDItem.setText(String.valueOf(item.getIDItem()));
        jtKode.setText(item.getKode());
        txtNama.setText(item.getNama());
        cmbKategori.setSelectedItem(item.getKategori());
        txtStok.setText(String.valueOf(item.getStok()));
        txtHargaBeli.setText(String.valueOf(item.getHargaBeli()));
        cmbAktif.setSelected(item.getAktif() == 1);

        // ==== Ambil detail pertama (jika ada) ====
        if (item.getDetails() != null && !item.getDetails().isEmpty()) {
            ItemDetail detail = item.getDetails().get(0);

            cmbSatuanKecil.setSelectedItem(detail.getSatuan());
            cmbSatuanBesar.setSelectedItem(detail.getSatuanBesar());
            txtHargaJual.setText(String.valueOf(detail.getHargaJual()));
            txtLabaPersen.setText(String.valueOf(detail.getLabaPersen()));
            txtKonversi.setText(String.valueOf(detail.getKonversi()));
        } else {
            // Kosongkan field detail jika tidak ada data
            cmbSatuanKecil.setSelectedIndex(-1);
            txtHargaJual.setText("");
            txtLabaPersen.setText("");
            txtKonversi.setText("");
        }

        // ==== Update label posisi record (opsional) ====
        updateRecordLabel();

    } else {
        JOptionPane.showMessageDialog(null, "Indeks catatan tidak valid.");
    }
}

    private void updateRecordLabel() {
    recordLabel.setText("Record: " + (currentRecordIndex + 1) + " dari " + totalInputs);
}
    private void data_awal() {
    currentRecordIndex = 0; // Data pertama
    loadCurrentItem();
}
    private void data_terakhir() {
    currentRecordIndex = totalInputs - 1; // Data terakhir
    loadCurrentItem();
}
    private void next() {
    if (currentRecordIndex < totalInputs - 1) { // Pastikan tidak melebihi jumlah total input
        currentRecordIndex++;
        loadCurrentItem();
    } else {
        JOptionPane.showMessageDialog(null, "Anda sudah berada pada record terakhir.");
    }
}
    private void previous() {
    if (currentRecordIndex > 0) { // Pastikan tidak kurang dari record pertama
        currentRecordIndex--;
        loadCurrentItem();
    } else {
        JOptionPane.showMessageDialog(null, "Anda sudah berada pada record pertama.");
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtIDItem = new javax.swing.JTextField();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtKode = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        cmbSatuanKecil = new javax.swing.JComboBox<>();
        txtKonversi = new javax.swing.JTextField();
        txtHargaJual = new javax.swing.JTextField();
        txtLabaPersen = new javax.swing.JTextField();
        txtStok = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtHargaBeli = new javax.swing.JTextField();
        cmbKategori = new javax.swing.JComboBox<>();
        cmbSatuanBesar = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        txtIDItem.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Master Obat");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(0, 255, 204));

        jPanel2.setBackground(new java.awt.Color(0, 102, 0));

        jPanel3.setBackground(new java.awt.Color(204, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setMinimumSize(new java.awt.Dimension(100, 25));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 25));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Mode Aktif ");

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

        jToolBar2.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar2.setRollover(true);

        btnAwal.setText("<<");
        btnAwal.setFocusable(false);
        btnAwal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAwal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(btnAwal);

        btnPrevious.setText("<");
        btnPrevious.setFocusable(false);
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(btnPrevious);

        btnNext.setText(">");
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(btnNext);

        btnAkhir.setText(">>");
        btnAkhir.setFocusable(false);
        btnAkhir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAkhir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
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

        jToolBar1.setBackground(new java.awt.Color(204, 255, 204));
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
        jToolBar1.add(btnHapus);

        recordLabel.setBackground(new java.awt.Color(0, 0, 0));
        recordLabel.setForeground(new java.awt.Color(0, 102, 0));
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

        jPanel4.setBackground(new java.awt.Color(0, 255, 204));

        btnSimpan.setText("Simpan");

        btnExit.setText("Exit Ctrl + X");

        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(92, 23));

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

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Kode :");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Nama :");

        jtKode.setMinimumSize(new java.awt.Dimension(33, 22));

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTabbedPane1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                jTabbedPane1AncestorRemoved(evt);
            }
        });

        cmbSatuanKecil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Box", "Dus", "Pack", "Vial", "Ampul", "Pouch", "Tubes", "Botol", "Kaleng", "Strip", "Pouch", "" }));

        txtKonversi.setMinimumSize(new java.awt.Dimension(33, 22));

        txtHargaJual.setBorder(javax.swing.BorderFactory.createTitledBorder("Harga Jual"));
        txtHargaJual.setMinimumSize(new java.awt.Dimension(33, 22));

        txtLabaPersen.setBorder(javax.swing.BorderFactory.createTitledBorder("%"));
        txtLabaPersen.setMinimumSize(new java.awt.Dimension(33, 22));
        txtLabaPersen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLabaPersenKeyReleased(evt);
            }
        });

        txtStok.setBorder(javax.swing.BorderFactory.createTitledBorder("Stok"));
        txtStok.setMinimumSize(new java.awt.Dimension(33, 22));
        txtStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStokActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtKonversi, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbSatuanKecil, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLabaPersen, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(240, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtLabaPersen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbSatuanKecil, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtKonversi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Detail", jPanel6);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Satuan :");

        txtNama.setMinimumSize(new java.awt.Dimension(33, 22));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Harga Beli :");

        txtHargaBeli.setMinimumSize(new java.awt.Dimension(33, 22));

        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Obat Bebas", "Obat Prekursor", "Obat Keras", "Alat Kesehatan", "Vitamin" }));

        cmbSatuanBesar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Box", "Dus", "Pack", "Vial", "Ampul", "Pouch", "Tubes", "Botol", "Kaleng", "Strip", "Pouch", " " }));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Kategori :");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtKode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbSatuanBesar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtHargaBeli, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(cmbKategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSatuanBesar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

    private void jTabbedPane1AncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTabbedPane1AncestorRemoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1AncestorRemoved

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
               BrowseItem dialog = new BrowseItem(this, true, conn);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtLabaPersenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLabaPersenKeyReleased
        // TODO add your handling code here:
        try {
            double hargaBeli = Double.parseDouble(txtHargaBeli.getText());
            double konversi = Double.parseDouble(txtKonversi.getText());
            double labaPersen = Double.parseDouble(txtLabaPersen.getText());

            double hargaPerPcs = hargaBeli / konversi;
            double hargaJual = hargaPerPcs * (1 + (labaPersen / 100));

            txtHargaJual.setText(String.format("%.0f", hargaJual)); // tanpa desimal
        } catch (NumberFormatException e) {
            txtHargaJual.setText("0");
        }
    }//GEN-LAST:event_txtLabaPersenKeyReleased

    private void txtStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStokActionPerformed

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
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JCheckBox cmbAktif;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JComboBox<String> cmbSatuanBesar;
    private javax.swing.JComboBox<String> cmbSatuanKecil;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField jtKode;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JTextField txtHargaBeli;
    private javax.swing.JTextField txtHargaJual;
    private javax.swing.JTextField txtIDItem;
    private javax.swing.JTextField txtKonversi;
    private javax.swing.JTextField txtLabaPersen;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtStok;
    // End of variables declaration//GEN-END:variables

    private ItemDAO ItemDAO(Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
