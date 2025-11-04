/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.Master;
import dao.CustomerDAO;
import dao.Koneksi;
import dao.SupplierDAO;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import model.Customer;
import model.Supplier;
import model.User;
import ui.Master.BrowseAll.BrowseSupplierDialog;
/**
 *
 * @author Admin
 */
public class frmSupp extends javax.swing.JFrame {
    private SupplierDAO supplierDAO;
    private Connection conn;
    private Statement stat;
    private ResultSet rs;
    private String sql;
    private User user;
    private List<Supplier> supplierList;
    private int currentRecordIndex;
    private int totalInputs;

// Dalam kelas frmCus


    /**
     * Creates new form ParentTrans
     */
    public frmSupp() {
        initComponents();
        initializeDatabase();
        supplierDAO = new SupplierDAO(conn);
        jToolBar1.setFloatable(false);
        jToolBar2.setFloatable(false);
        setResizable(false); 
        setLocationRelativeTo(null);
        jPanel5.requestFocusInWindow();
        btnALL();
        awal();
      
    }
   private void btnALL() {
       btnBrowse.addActionListener(evt -> openBrowseDialog());
        btnSimpan.addActionListener(evt -> simpan());
         btnExit.addActionListener(evr -> dispose());
         btnTambah.addActionListener(evt -> tambah());
         btnUbah.addActionListener(evt -> ubah());
         btnAwal.addActionListener(evt -> data_awal());
         btnPrevious.addActionListener(evt -> previous());
         btnNext.addActionListener(evt -> next());
         btnAkhir.addActionListener(evt -> data_terakhir());
//         btnHapus.addActionListener(evt -> hapusdata());
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
    private void awal(){
        jLabel1.setText("[Browse]");
        btnSimpan.setEnabled(false);
        btnCancel.setEnabled(false);
        btnExit.setEnabled(true);
        jtKode.setEnabled(true);
        txtNama.setEditable(false);
        jtBadanNama.setEditable(false);
        txtTempo.setEditable(false);
        txtAlamat.setEditable(false);
        txtTelephone.setEditable(false);
        txtKota.setEditable(false);
        cmbAktif.setEnabled(false);
          supplierDAO = new SupplierDAO(conn);
        supplierList = supplierDAO.getAllSuppliers(); // Ambil semua periode
        totalInputs = supplierDAO.countSuppliers();

    if (totalInputs > 0) {
        currentRecordIndex = totalInputs - 1; // Set ke indeks terakhir
        loadCurrentSupplier(); // Muat periode terakhir
        updateRecordLabel(); // Perbarui label dengan informasi record
    }
        navaktif();
        data_terakhir();
    }
    private void tambah(){
        jLabel1.setText("[Tambah]");
        clear();
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(false);
        jtKode.setEnabled(true);
        txtNama.setEditable(true);
        jtBadanNama.setEditable(true);
        txtTempo.setEditable(true);
        txtAlamat.setEditable(true);
        txtTelephone.setEditable(true);
        txtKota.setEditable(true);
        cmbAktif.setEnabled(true);
        cmbAktif.setSelected(true);
        navnonaktif();
        
    }
    private void clear(){
        jtKode.setText("");
        txtNama.setText("");
        jtBadanNama.setText("");
        txtTempo.setText("");
        txtAlamat.setText("");
        txtTelephone.setText("");
        txtKota.setText("");
    }
    private void ubah(){
        jLabel1.setText("[Ubah]");
        btnTambah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(true);
         jtKode.setEnabled(true);
        txtNama.setEditable(true);
        jtBadanNama.setEditable(true);
        txtTempo.setEditable(true);
        txtAlamat.setEditable(true);
        txtTelephone.setEditable(true);
        txtKota.setEditable(true);
        cmbAktif.setEnabled(true);
        navaktif();
    }
    private void saveSupplier() {
    IDotomatis();
    String kode = jtKode.getText();
    String namaUsaha = jtBadanNama.getText();
    int tempo = Integer.parseInt(txtTempo.getText());
    String nama = txtNama.getText();
    String alamat = txtAlamat.getText();
    String telephone = txtTelephone.getText();
    String kota = txtKota.getText();
    String isAktif = cmbAktif.isSelected() ? "Ya" : "Tidak";

    if (kode.isEmpty() || namaUsaha.isEmpty() ) {
        JOptionPane.showMessageDialog(this, "Data tidak lengkap. Harap lengkapi semua field.");
        return;
    }

    Supplier supplier = new Supplier();
    supplier.setKode(kode);
    supplier.setNamaUsaha(namaUsaha);
    supplier.setTempo(tempo);
    supplier.setNama(nama);
    supplier.setAlamat(alamat);
    supplier.setTelephone(telephone);
    supplier.setKota(kota);
    supplier.setIsAktif(isAktif);

    if (supplierDAO.insertSupplier(supplier)) {
        JOptionPane.showMessageDialog(this, "Data Customer berhasil disimpan.");
    } else {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan data Customer.");
    }
    } 
    private void updateSupplier() {
    // Ambil ID Supplier yang ingin diubah
    int id = Integer.parseInt(txtId.getText());  // Pastikan Anda memiliki komponen untuk menampilkan ID

    String kode = jtKode.getText();
    String namaUsaha = jtBadanNama.getText();
    int tempo = Integer.parseInt(txtTempo.getText());
    String nama = txtNama.getText();
    String alamat = txtAlamat.getText();
    String telephone = txtTelephone.getText();
    String kota = txtKota.getText();
    String isAktif = cmbAktif.isSelected() ? "Ya" : "Tidak";

    // Validasi input
    if (kode.isEmpty() || namaUsaha.isEmpty() || nama.isEmpty() || alamat.isEmpty() || telephone.isEmpty() || kota.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Data tidak lengkap. Harap lengkapi semua field.");
        return;
    }

    // Membuat objek Supplier dengan data yang sudah diperbarui
    Supplier supplier = new Supplier();
    supplier.setIdSup(id); // Pastikan Anda memiliki metode untuk mengatur ID
    supplier.setKode(kode);
    supplier.setNamaUsaha(namaUsaha);
    supplier.setTempo(tempo);
    supplier.setNama(nama);
    supplier.setAlamat(alamat);
    supplier.setTelephone(telephone);
    supplier.setKota(kota);
    supplier.setIsAktif(isAktif);

    // Mengubah data ke database menggunakan SupplierDAO
    if (supplierDAO.updateSupplier(id, supplier)) {
        JOptionPane.showMessageDialog(this, "Data Supplier berhasil diubah.");
    } else {
        JOptionPane.showMessageDialog(this, "Gagal mengubah data Supplier.");
    }
}
    private void simpan(){
            String action = jLabel1.getText(); // Mendapatkan teks dari JLabel
         if (action.equals("[Tambah]")) {
        saveSupplier(); // Jalankan metode simpan jika teks adalah "Tambah"
        } else if (action.equals("[Ubah]")) {
             updateSupplier(); // Jalankan metode ubah jika teks adalah "Ubah"
       } else {
        JOptionPane.showMessageDialog(null, "Aksi tidak dikenali: " + action);
    }
         awal();
} 
    private void IDotomatis() {
    try {
        
        // Mendapatkan dua huruf pertama dari field nama
        String nama = jtBadanNama.getText().trim();
        String duaHurufNama = nama.substring(0, Math.min(nama.length(), 2)).toUpperCase(); // Ambil dua huruf pertama
        
        // Mendapatkan kode terakhir berdasarkan dua huruf nama
        String lastCode = supplierDAO.getLastKode(duaHurufNama);
        
        // Mendapatkan nomor urut terakhir
        int nomorUrut = 0;
        if (lastCode != null && lastCode.startsWith(duaHurufNama)) {
            // Jika sudah ada kode dengan dua huruf nama yang sama
            String nomorStr = lastCode.substring(2); // Ambil bagian nomor urut setelah dua huruf nama
            nomorUrut = Integer.parseInt(nomorStr) + 1; // Increment nomor urut
        } else {
            // Jika belum ada kode untuk dua huruf nama tersebut
            nomorUrut = 1;
        }
        
        // Format nomor urut menjadi empat digit dengan leading zeros
        String nomorUrutFormatted = String.format("%04d", nomorUrut);
        
        // Buat kode lengkap
        String kodeLengkap = duaHurufNama + nomorUrutFormatted;
        
        jtKode.setText(kodeLengkap);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
}
    private void loadCurrentSupplier() {
    if (currentRecordIndex >= 0 && currentRecordIndex < supplierList.size()) {
        Supplier supplier = supplierList.get(currentRecordIndex);
        
        // Mengatur IDKategori sebagai string
        txtId.setText(String.valueOf(supplier.getIdSup())); 
        
        // Tampilkan nilai-nilai ini di komponen UI
        jtKode.setText(supplier.getKode());
        jtBadanNama.setText(supplier.getNamaUsaha()); // Pastikan Anda memiliki field nama
        txtTempo.setText(String.valueOf(supplier.getTempo()));
        txtNama.setText(supplier.getNama());
        txtAlamat.setText(supplier.getAlamat());
        txtTelephone.setText(supplier.getTelephone());
        txtKota.setText(supplier.getKota());
        cmbAktif.setSelected("Ya".equalsIgnoreCase(supplier.getIsAktif()));
        
        updateRecordLabel();
    } else {
        JOptionPane.showMessageDialog(null, "Indeks catatan tidak valid.");
    }
}
    private void updateRecordLabel() {
    recordLabel.setText("Record: " + (currentRecordIndex + 1) + " dari " + totalInputs);
}
    private void next() {
    if (currentRecordIndex < totalInputs - 1) { // Pastikan tidak melebihi jumlah total input
        currentRecordIndex++;
        loadCurrentSupplier();
    } else {
        JOptionPane.showMessageDialog(null, "Anda sudah berada pada record terakhir.");
    }
}  
    public void setCurrentRecordIndex(int index) {
    this.currentRecordIndex = index; // Update current index
    loadCurrentSupplier(); // Muat data barang sesuai dengan index yang baru
}
    private void previous() {
    if (currentRecordIndex > 0) { // Pastikan tidak kurang dari record pertama
        currentRecordIndex--;
        loadCurrentSupplier();
    } else {
        JOptionPane.showMessageDialog(null, "Anda sudah berada pada record pertama.");
    }
}
    private void data_awal() {
    currentRecordIndex = 0; // Data pertama
    loadCurrentSupplier();
}
    private void data_terakhir() {
    currentRecordIndex = totalInputs - 1; // Data terakhir
    loadCurrentSupplier();
}
    public void setSupplierData(String ID, String kode, String namaUsaha, String tempo, String nama, String alamat, String telephone, String kota, boolean isAktif) {
        // Set values in the form fields
        txtId.setText(ID);
        jtKode.setText(kode);
        jtBadanNama.setText(namaUsaha);
        txtTempo.setText(tempo);
        txtNama.setText(nama);
        txtAlamat.setText(alamat);
        txtTelephone.setText(telephone);
        txtKota.setText(kota);
        cmbAktif.setSelected(isAktif); // Check the checkbox if the supplier is active
    }
    private void openBrowseDialog(){
         BrowseSupplierDialog dialog = new BrowseSupplierDialog(this, true, conn);
        dialog.setVisible(true);
    }
    // Other form methods and initialization code

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtId = new javax.swing.JTextField();
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
        lblKode = new javax.swing.JLabel();
        jtKode = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        lblKode1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        txtNama = new javax.swing.JTextField();
        lblKode3 = new javax.swing.JLabel();
        lblKode4 = new javax.swing.JLabel();
        txtAlamat = new javax.swing.JTextField();
        lblKode5 = new javax.swing.JLabel();
        txtTelephone = new javax.swing.JTextField();
        lblKode6 = new javax.swing.JLabel();
        txtKota = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        lblKode2 = new javax.swing.JLabel();
        txtTempo = new javax.swing.JTextField();
        jtBadanNama = new javax.swing.JTextField();
        lblhari = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        txtId.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Master Supplier");
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
        jToolBar1.add(btnTambah);

        btnUbah.setText("Ubah");
        btnUbah.setFocusable(false);
        btnUbah.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUbah.setMaximumSize(new java.awt.Dimension(53, 30));
        btnUbah.setMinimumSize(new java.awt.Dimension(53, 30));
        btnUbah.setPreferredSize(new java.awt.Dimension(53, 30));
        btnUbah.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
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

        btnExit.setText("Exit Ctrl + X");

        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(92, 23));

        cmbAktif.setText("Data Aktif");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
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

        lblKode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode.setText("Kode :");
        lblKode.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode.setPreferredSize(new java.awt.Dimension(33, 22));

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

        lblKode1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode1.setText("Nama :");
        lblKode1.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode1.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode1.setPreferredSize(new java.awt.Dimension(33, 22));

        jPanel6.setBackground(new java.awt.Color(204, 255, 255));

        txtNama.setMaximumSize(new java.awt.Dimension(64, 23));
        txtNama.setMinimumSize(new java.awt.Dimension(64, 23));
        txtNama.setPreferredSize(new java.awt.Dimension(64, 23));
        txtNama.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNamaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNamaFocusLost(evt);
            }
        });

        lblKode3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode3.setText("Nama :");
        lblKode3.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode3.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode3.setPreferredSize(new java.awt.Dimension(33, 22));

        lblKode4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode4.setText("Alamat :");
        lblKode4.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode4.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode4.setPreferredSize(new java.awt.Dimension(33, 22));

        txtAlamat.setMaximumSize(new java.awt.Dimension(64, 23));
        txtAlamat.setMinimumSize(new java.awt.Dimension(64, 23));
        txtAlamat.setPreferredSize(new java.awt.Dimension(64, 23));
        txtAlamat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAlamatFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAlamatFocusLost(evt);
            }
        });

        lblKode5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode5.setText("Telephone :");
        lblKode5.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode5.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode5.setPreferredSize(new java.awt.Dimension(33, 22));

        txtTelephone.setMaximumSize(new java.awt.Dimension(64, 23));
        txtTelephone.setMinimumSize(new java.awt.Dimension(64, 23));
        txtTelephone.setPreferredSize(new java.awt.Dimension(64, 23));
        txtTelephone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelephoneFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTelephoneFocusLost(evt);
            }
        });

        lblKode6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode6.setText("Kota :");
        lblKode6.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode6.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode6.setPreferredSize(new java.awt.Dimension(33, 22));

        txtKota.setMaximumSize(new java.awt.Dimension(64, 23));
        txtKota.setMinimumSize(new java.awt.Dimension(64, 23));
        txtKota.setPreferredSize(new java.awt.Dimension(64, 23));
        txtKota.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKotaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKotaFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lblKode3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lblKode4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                            .addComponent(lblKode5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtTelephone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                            .addComponent(lblKode6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtKota, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(210, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKode3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKode4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKode5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKode6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Detail", jPanel6);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 623, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 162, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Rekening", jPanel7);

        lblKode2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode2.setText("Jatuh Tempo :");
        lblKode2.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode2.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode2.setPreferredSize(new java.awt.Dimension(33, 22));

        txtTempo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTempo.setMaximumSize(new java.awt.Dimension(64, 23));
        txtTempo.setMinimumSize(new java.awt.Dimension(64, 23));
        txtTempo.setPreferredSize(new java.awt.Dimension(64, 23));
        txtTempo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTempoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTempoFocusLost(evt);
            }
        });

        lblhari.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblhari.setText("Hari");
        lblhari.setMaximumSize(new java.awt.Dimension(33, 22));
        lblhari.setMinimumSize(new java.awt.Dimension(33, 22));
        lblhari.setPreferredSize(new java.awt.Dimension(33, 22));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblKode, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblKode2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblKode1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtBadanNama, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblhari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblKode1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtBadanNama, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKode2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblhari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void txtTempoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTempoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTempoFocusGained

    private void txtTempoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTempoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTempoFocusLost

    private void txtNamaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNamaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaFocusGained

    private void txtNamaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNamaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaFocusLost

    private void txtAlamatFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAlamatFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAlamatFocusGained

    private void txtAlamatFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAlamatFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAlamatFocusLost

    private void txtTelephoneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelephoneFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelephoneFocusGained

    private void txtTelephoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelephoneFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelephoneFocusLost

    private void txtKotaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKotaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKotaFocusGained

    private void txtKotaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKotaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKotaFocusLost

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
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JCheckBox cmbAktif;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField jtBadanNama;
    private javax.swing.JTextField jtKode;
    private javax.swing.JLabel lblKode;
    private javax.swing.JLabel lblKode1;
    private javax.swing.JLabel lblKode2;
    private javax.swing.JLabel lblKode3;
    private javax.swing.JLabel lblKode4;
    private javax.swing.JLabel lblKode5;
    private javax.swing.JLabel lblKode6;
    private javax.swing.JLabel lblhari;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtKota;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtTelephone;
    private javax.swing.JTextField txtTempo;
    // End of variables declaration//GEN-END:variables
}
