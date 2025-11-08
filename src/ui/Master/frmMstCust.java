/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.Master;
import java.sql.Statement;
import dao.CustomerDAO;
import dao.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Customer;
import model.User;
/**
 *
 * @author Admin
 */
public class frmMstCust extends javax.swing.JFrame {
    
    private CustomerDAO customerDAO;
    private Connection conn;
    private Statement stat;
    private ResultSet rs;
    private String sql;
    private User user;
   // private List<Dokter> dokterList;
    private List<Customer> customerList = new ArrayList<>();

    private int currentRecordIndex;
    private int totalInputs;

    /**
     * Creates new form ParentTrans
     */
    
    public frmMstCust() {
        initComponents();
        initializeDatabase();
        customerDAO = new CustomerDAO(conn);
        jToolBar1.setFloatable(false);
        jToolBar2.setFloatable(false);
        btnALL();
        awal();
        FrmProjec();
        FormShow();
    }
private void IDotomatis() {
    try {
        // Ambil kode terakhir dari tabel mdokter berdasarkan prefix "DR"
        String lastCode = customerDAO.getLastKode("CS"); // kirim prefix ke DAO
        String newCode;

        if (lastCode != null && lastCode.startsWith("CS")) {
            // Ambil angka di belakang "DR"
            int number = Integer.parseInt(lastCode.substring(2));
            number++; // tambah 1
            newCode = String.format("CS%03d", number); // hasil: DR001, DR002, dst
        } else {
            // Jika belum ada data sama sekali
            newCode = "CS001";
        }

        jtKode.setText(newCode);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
    }
}


    private void FormShow(){
         loadDataFromDatabase();
         loadCurrentCustomer(); 
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
        customerList = customerDAO.getAllCustomer(); // Ambil semua periode
        totalInputs = customerDAO.countCustomer();

        if (totalInputs > 0) {
            currentRecordIndex = totalInputs - 1; // Set ke indeks terakhir
            loadCurrentCustomer(); // Muat periode terakhir
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
    txtEmail.setText("");
    txtAlamat.setText("");
    txtTelephone.setText("");
    txtKota.setText("");
    txtBank.setText("");
    txtNomorRekening.setText("");
    txtNamaRekening.setText("");
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
// ============ CRUD DOKTER ==================
// ===========================================
private void simpan() {
    String action = jLabel1.getText(); // Mendapatkan teks dari JLabel

    if (action.equals("[Tambah]")) {
        saveCustomer();
    } else if (action.equals("[Ubah]")) {
        updateCustomer();
    } else {
        JOptionPane.showMessageDialog(null, "Aksi tidak dikenali: " + action);
    }

    awal(); // Reset form / state awal
}

// ===========================================
// ============ SIMPAN DOKTER =================
// ===========================================
private void saveCustomer() {
    IDotomatis();
    String kode = jtKode.getText();
    String nama = txtNama.getText();
    String email = txtEmail.getText();
    String alamat = txtAlamat.getText();
    String telephone = txtTelephone.getText();
    String kota = txtKota.getText();
    String bank = txtBank.getText();
    String nomorRekening = txtNomorRekening.getText();
    String namaRekening = txtNamaRekening.getText();
    int aktif = cmbAktif.isSelected() ? 1 : 0; // <-- Diganti ke 1 atau 0

    // Validasi input
    if (kode.isEmpty() || nama.isEmpty() 
        //|| email.isEmpty() || alamat.isEmpty() ||
       // telephone.isEmpty() || kota.isEmpty() || bank.isEmpty() || 
        //nomorRekening.isEmpty() || namaRekening.isEmpty()
                ) {
        JOptionPane.showMessageDialog(this, "Data tidak lengkap. Harap lengkapi semua field.");
        //return;
    }

    Customer customer = new Customer();
    customer.setKode(kode);
    customer.setNama(nama);
    customer.setEmail(email);
    customer.setAlamat(alamat);
    customer.setTelephone(telephone);
    customer.setKota(kota);
    customer.setBank(bank);
    customer.setNomorRekening(nomorRekening);
    customer.setNamaRekening(namaRekening);
    customer.setAktif(aktif);

    if (customerDAO.insertCustomer(customer)) {
        JOptionPane.showMessageDialog(this, "Data Customer berhasil disimpan.");
    } else {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan data Customer.");
    }
}

// ===========================================
// ============ UPDATE DOKTER =================
// ===========================================
private void updateCustomer() {
    int id = Integer.parseInt(txtIDCustomer.getText());
    String kode = jtKode.getText();
    String nama = txtNama.getText();
    String email = txtEmail.getText();
    String alamat = txtAlamat.getText();
    String telephone = txtTelephone.getText();
    String kota = txtKota.getText();
    String bank = txtBank.getText();
    String nomorRekening = txtNomorRekening.getText();
    String namaRekening = txtNamaRekening.getText();
    int aktif = cmbAktif.isSelected() ? 1 : 0; // <-- Diganti ke 1 atau 0

    if (kode.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Kode Customer harus diisi untuk melakukan update.");
        return;
    }

    Customer customer = new Customer();
    customer.setKode(kode);
    customer.setNama(nama);
    customer.setEmail(email);
    customer.setAlamat(alamat);
    customer.setTelephone(telephone);
    customer.setKota(kota);
    customer.setBank(bank);
    customer.setNomorRekening(nomorRekening);
    customer.setNamaRekening(namaRekening);
    customer.setAktif(aktif);

      if (customerDAO.updateCustomer(id, customer)){
        JOptionPane.showMessageDialog(this, "Data Customer berhasil diperbarui.");
    } else {
        JOptionPane.showMessageDialog(this, "Gagal memperbarui data Custpmer.");
    }
}
private void loadDataFromDatabase() {
    customerList = customerDAO.getAllCustomer();
    if (customerList == null) {
        customerList = new ArrayList<>();
    }

    if (customerList.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Data customer belum ada di database.");
    } else {
        currentRecordIndex = 0;  // <<< ini penting
    }
}

private void loadCurrentCustomer() {
    if (currentRecordIndex >= 0 && currentRecordIndex < customerList.size()) {
        Customer customer = customerList.get(currentRecordIndex);
        
        // Mengatur IDPeriode sebagai string
        txtIDCustomer.setText(String.valueOf(customer.getIDCustomer())); 
        
        // Tampilkan nilai-nilai ini di komponen UI
        jtKode.setText(customer.getKode());
        txtNama.setText(customer.getNama());
        txtEmail.setText(customer.getEmail());
     txtAlamat.setText(customer.getAlamat());
      txtTelephone.setText(customer.getTelephone());
    txtKota.setText(customer.getKota());
    txtBank.setText(customer.getBank());
    txtNomorRekening.setText(customer.getNomorRekening());
     txtNamaRekening.setText(customer.getNamaRekening());
      cmbAktif.setSelected(customer.getAktif() == 1); // <-- Diganti ke 1 atau 0
        
        
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
    loadCurrentCustomer();
}
    private void data_terakhir() {
    currentRecordIndex = totalInputs - 1; // Data terakhir
    loadCurrentCustomer();
}
    private void next() {
    if (currentRecordIndex < totalInputs - 1) { // Pastikan tidak melebihi jumlah total input
        currentRecordIndex++;
        loadCurrentCustomer();
    } else {
        JOptionPane.showMessageDialog(null, "Anda sudah berada pada record terakhir.");
    }
}
    private void previous() {
    if (currentRecordIndex > 0) { // Pastikan tidak kurang dari record pertama
        currentRecordIndex--;
        loadCurrentCustomer();
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

        txtIDCustomer = new javax.swing.JTextField();
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
        txtNama = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        txtEmail = new javax.swing.JTextField();
        lblKode3 = new javax.swing.JLabel();
        lblKode4 = new javax.swing.JLabel();
        txtAlamat = new javax.swing.JTextField();
        lblKode5 = new javax.swing.JLabel();
        txtTelephone = new javax.swing.JTextField();
        lblKode6 = new javax.swing.JLabel();
        txtKota = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        lblKode7 = new javax.swing.JLabel();
        txtBank = new javax.swing.JTextField();
        lblKode8 = new javax.swing.JLabel();
        txtNomorRekening = new javax.swing.JTextField();
        lblKode9 = new javax.swing.JLabel();
        txtNamaRekening = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        txtIDCustomer.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Master Dokter");
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

        txtNama.setMinimumSize(new java.awt.Dimension(33, 22));

        jTabbedPane1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                jTabbedPane1AncestorRemoved(evt);
            }
        });

        txtEmail.setMaximumSize(new java.awt.Dimension(64, 23));
        txtEmail.setMinimumSize(new java.awt.Dimension(64, 23));
        txtEmail.setPreferredSize(new java.awt.Dimension(64, 23));
        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailFocusLost(evt);
            }
        });

        lblKode3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode3.setText("Email :");
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
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKode3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Detail", jPanel6);

        lblKode7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode7.setText("Bank :");
        lblKode7.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode7.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode7.setPreferredSize(new java.awt.Dimension(33, 22));

        txtBank.setMaximumSize(new java.awt.Dimension(64, 23));
        txtBank.setMinimumSize(new java.awt.Dimension(64, 23));
        txtBank.setPreferredSize(new java.awt.Dimension(64, 23));
        txtBank.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBankFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBankFocusLost(evt);
            }
        });

        lblKode8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode8.setText("No.Rek :");
        lblKode8.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode8.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode8.setPreferredSize(new java.awt.Dimension(33, 22));

        txtNomorRekening.setMaximumSize(new java.awt.Dimension(64, 23));
        txtNomorRekening.setMinimumSize(new java.awt.Dimension(64, 23));
        txtNomorRekening.setPreferredSize(new java.awt.Dimension(64, 23));
        txtNomorRekening.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNomorRekeningFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomorRekeningFocusLost(evt);
            }
        });

        lblKode9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode9.setText("Nama");
        lblKode9.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode9.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode9.setPreferredSize(new java.awt.Dimension(33, 22));

        txtNamaRekening.setMaximumSize(new java.awt.Dimension(64, 23));
        txtNamaRekening.setMinimumSize(new java.awt.Dimension(64, 23));
        txtNamaRekening.setPreferredSize(new java.awt.Dimension(64, 23));
        txtNamaRekening.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNamaRekeningFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNamaRekeningFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblKode7, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBank, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblKode8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNomorRekening, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblKode9, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNamaRekening, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKode7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKode8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomorRekening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKode9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaRekening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(120, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Rekening", jPanel7);

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
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(jTabbedPane1)
                .addGap(18, 18, 18)
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
                .addGap(0, 0, 0))
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

    private void txtEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailFocusGained

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailFocusLost

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

    private void jTabbedPane1AncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTabbedPane1AncestorRemoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1AncestorRemoved

    private void txtBankFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBankFocusGained

    private void txtBankFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBankFocusLost

    private void txtNomorRekeningFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomorRekeningFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomorRekeningFocusGained

    private void txtNomorRekeningFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomorRekeningFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomorRekeningFocusLost

    private void txtNamaRekeningFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNamaRekeningFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaRekeningFocusGained

    private void txtNamaRekeningFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNamaRekeningFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaRekeningFocusLost

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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JTextField jtKode;
    private javax.swing.JLabel lblKode3;
    private javax.swing.JLabel lblKode4;
    private javax.swing.JLabel lblKode5;
    private javax.swing.JLabel lblKode6;
    private javax.swing.JLabel lblKode7;
    private javax.swing.JLabel lblKode8;
    private javax.swing.JLabel lblKode9;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtBank;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtIDCustomer;
    private javax.swing.JTextField txtKota;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNamaRekening;
    private javax.swing.JTextField txtNomorRekening;
    private javax.swing.JTextField txtTelephone;
    // End of variables declaration//GEN-END:variables
}
