/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.Transaksi;

import dao.ItemDAO;
import dao.Koneksi;
import dao.cell.ButtonEditor;
import dao.cell.ButtonRenderer;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Item;
import model.User;
import ui.Master.BrowseAll.BrowseItem;
import dao.TbelihDAO;
import dao.TbelidDAO;
import dao.TjurnalitemDAO;
import model.Tbelih;
import model.Tbelid;
import model.Tjurnalitem;
import ui.Master.BrowseAll.BrowseSupplier;

/**
 * @author Admin
 */
public class frmTransPembelianBayar extends javax.swing.JFrame {
    private Connection conn;
    private ItemDAO itemDAO;
    private Statement stat;
    private ResultSet rs;
    private String sql;
    private User user;

    private List<Item> itemsList = new ArrayList<>();
    private String selectedSatuanKecil;
    private double selectedHarga;
    private String selectedSatuanBesar;
    private double selectedKonversi;
    private double setSubtotal = 0;
    private int selectedSupplierId = 0;
    DefaultTableModel model;
    JTable tblDetail;

    public frmTransPembelianBayar() {
        initComponents();
        FormCreate();
        btnALL();
        jToolBar1.setFloatable(false);
        jToolBar2.setFloatable(false);
        awal();
        FrmProjec();
    }

    private void FrmProjec(){
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        txtHarga.setText(formatAngka(0));
        lblSubtotal.setText(formatAngka(0));
        txtNama.setText("< Nama Item >");
    }

    private void FormCreate(){
        initializeDatabase();
        initTable();
        setupDiscEvents();
        setupJenisListener(); 
        cmbSatuan.addActionListener(e -> updateHargaBerdasarkanSatuan());
        txtJumlah.addActionListener(e -> addItemFromInput());
        txtDiscPersen.addActionListener(e -> addItemFromInput());
        txtDiscTotal.addActionListener(e -> addItemFromInput());
    }
    
    private void addItemFromInput() {
        try {
            if (txtIDItem.getText().isEmpty() || txtNama.getText().isEmpty() ||
                txtJumlah.getText().isEmpty() || txtHarga.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
                return;
            }

            if (cmbSatuan.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Satuan belum dipilih!");
                return;
            }

            int idItem = Integer.parseInt(txtIDItem.getText());
            String kode = txtKodeItem.getText();
            String nama = txtNama.getText();
            String satuan = cmbSatuan.getSelectedItem().toString();
            int qty = Integer.parseInt(txtJumlah.getText());
            double harga = parseAngka(txtHarga.getText());

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Qty harus lebih dari 0");
                return;
            }

            double discTotal = txtDiscTotal.getText().isEmpty() ? 0 : parseAngka(txtDiscTotal.getText());
            double total = qty * harga;
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            for (int i = 0; i < jTable1.getRowCount(); i++) {
                int existingId = Integer.parseInt(jTable1.getValueAt(i, 1).toString());
                if (existingId == idItem && jTable1.getValueAt(i, 4).toString().equals(satuan)) {
                    int existingQty = Integer.parseInt(jTable1.getValueAt(i, 5).toString().replace(",", ""));
                    int newQty = existingQty + qty;
                    double newTotal = newQty * harga;
                    jTable1.setValueAt(formatAngka(newQty), i, 5);
                    jTable1.setValueAt(formatAngka(newTotal), i, 7);
                    updateSubtotal();
                    return;
                }
            }

            model.addRow(new Object[]{
                "X",
                idItem,
                kode,
                nama,
                satuan,
                formatAngka(qty),
                formatAngka(harga),
                formatAngka(discTotal),
                formatAngka(total)
            });

            txtJumlah.setText("");
            clearInputFields();
            updateSubtotal();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid! Pastikan angka di Qty dan Harga.");
        }
    }
    
    public void updateSubtotal() {
        double subtotal = 0;
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            subtotal += parseAngka(model.getValueAt(i, 8).toString());
        }
        lblSubtotal.setText(formatAngka(subtotal));
    }
    
    private void clearInputFields() {
    txtIDItem.setText("");
    txtKodeItem.setText("");
    txtSupplier.setText("");
     txtNama.setText("< Nama Item >");
    txtJumlah.setText("0");
    txtHarga.setText(formatAngka(0));
    cmbSatuan.removeAllItems();
    txtDiscPersen.setText("");
    txtDiscTotal.setText("");
    }
    
    private void updateHargaBerdasarkanSatuan() {
        try {
            String satuanDipilih = cmbSatuan.getSelectedItem().toString();
            double hargaAkhir;
            if (satuanDipilih.equalsIgnoreCase(selectedSatuanBesar)) {
                hargaAkhir = selectedHarga * selectedKonversi;
            } else {
                hargaAkhir = selectedHarga;
            }
            txtHarga.setText(formatAngka(hargaAkhir));
        } catch (Exception e) {
            txtHarga.setText("0");
        }
    }

    private void btnALL() {
        btnSimpan.addActionListener(evt -> simpan());
        btnExit.addActionListener(evr -> dispose());
        btnTambah.addActionListener(evt -> tambah());
        btnUbah.addActionListener(evt -> ubah());
    }

    private void initializeDatabase() {
        try {
            conn = Koneksi.getConnection();
            if (conn != null) stat = conn.createStatement();
            else JOptionPane.showMessageDialog(this, "Koneksi database gagal!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error koneksi DB: " + e.getMessage());
        }
    }

    private void setupJenisListener() {
        cmbJenis.addActionListener(e -> {
            String jenis = cmbJenis.getSelectedItem().toString();
            if (jenis.equalsIgnoreCase("Biasa")) {
                txtSupplier.setEnabled(false);
                txtSupplier.setText("");
            } else if (jenis.equalsIgnoreCase("Konsinyasi")) {
                txtSupplier.setEnabled(true);
            }
        });
    }

    // === TABEL DETAIL ===
    private void initTable() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"X", "ID", "Kode", "Nama", "Satuan", "Qty", "Harga", "Diskon", "Total"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        jTable1.setModel(model);
        jTable1.setShowGrid(false);
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        jTable1.getColumn("X").setCellRenderer(new ButtonRenderer());
        jTable1.getColumn("X").setCellEditor(new ButtonEditor(jTable1));
        jTable1.getColumn("ID").setMinWidth(0);
        jTable1.getColumn("ID").setMaxWidth(0);
        jTable1.getColumn("ID").setWidth(0);

        jTable1.getColumn("X").setPreferredWidth(30);
        jTable1.getColumn("Kode").setPreferredWidth(100);
        jTable1.getColumn("Nama").setPreferredWidth(400);
        jTable1.getColumn("Satuan").setPreferredWidth(50);
        jTable1.getColumn("Qty").setPreferredWidth(50);
        jTable1.getColumn("Harga").setPreferredWidth(100);
        jTable1.getColumn("Total").setPreferredWidth(120);

        javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        for (int i = 5; i <= 8; i++) jTable1.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
    }

    private void setupDiscEvents() {
        txtDiscPersen.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateDiscTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateDiscTotal(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateDiscTotal(); }
        });
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
        navaktif();
    }
    private void tambah(){
        jLabel1.setText("[Tambah]");
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(true);
        navnonaktif();
        
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

    private void updateDiscTotal() {
        try {
            double persen = txtDiscPersen.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscPersen.getText());
            double harga = parseAngka(txtHarga.getText());
            int qty = txtJumlah.getText().isEmpty() ? 0 : Integer.parseInt(txtJumlah.getText());
            double total = harga * qty;
            double discTotal = (persen / 100) * total;
            txtDiscTotal.setText(formatAngka(discTotal));
        } catch (Exception e) {
            txtDiscTotal.setText(formatAngka(0));
        }
    }

    private String formatAngka(double value) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(value);
    }

    private double parseAngka(String value) {
        return Double.parseDouble(value.replace(",", "").replace(".", ""));
    }

    // === GENERATE NO FAKTUR BELI ===
    private String generateNoFaktur() {
        String prefix = "PB";
        String datePart = new java.text.SimpleDateFormat("yyMMdd").format(new java.util.Date());
        String uniquePart = String.format("%03d", (int)(Math.random() * 999));
        return prefix + datePart + uniquePart;
    }

    // === SIMPAN TRANSAKSI PEMBELIAN ===
    private void simpanTransaksi() {
        try {
            conn.setAutoCommit(false);

            String noFaktur = generateNoFaktur();
            java.sql.Date tanggal = new java.sql.Date(System.currentTimeMillis());
            String jenisBayar = "Tunai";

            // --- HEADER ---
            Tbelih belih = new Tbelih();
            belih.setKode(noFaktur);
            belih.setTanggal(tanggal);
            belih.setJenisBayar(jenisBayar);
            belih.setIdSupplier(selectedSupplierId);
            belih.setSubTotal(parseAngka(lblSubtotal.getText()));
            belih.setDiskon(0);
            belih.setPpn(0);
            belih.setTotal(parseAngka(lblSubtotal.getText()));
            belih.setStatus("Open");
            belih.setInsertUser(user != null ? user.getUsername() : "admin");

            TbelihDAO belihDAO = new TbelihDAO(conn);
            belihDAO.insert(belih);

            // --- DAPATKAN ID HEADER TERAKHIR ---
            int idBeliH = 0;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID() AS id")) {
                if (rs.next()) idBeliH = rs.getInt("id");
            }

            // --- DETAIL ---
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            TbelidDAO belidDAO = new TbelidDAO(conn);
            TjurnalitemDAO jurnalDAO = new TjurnalitemDAO(conn);

            for (int i = 0; i < model.getRowCount(); i++) {
                int idItem = Integer.parseInt(model.getValueAt(i, 1).toString());
                double qty = parseAngka(model.getValueAt(i, 5).toString());
                double harga = parseAngka(model.getValueAt(i, 6).toString());
                double total = parseAngka(model.getValueAt(i, 8).toString());

                Tbelid belid = new Tbelid();
                belid.setIdBeliH(idBeliH);
                belid.setIdItemD(idItem);
                belid.setQty(qty);
                belid.setHarga(harga);
                belid.setTotal(total);
                belid.setQtyBase(qty);

                belidDAO.insert(belid);

                // --- JURNAL STOK MASUK ---
                Tjurnalitem ji = new Tjurnalitem();
                ji.setTanggal(tanggal);
                ji.setIdItem(idItem);
                ji.setKodeTrans(noFaktur);
                ji.setJenisTrans("Beli");
                ji.setQtyMasuk(qty);
                ji.setQtyKeluar(0);
                ji.setSatuan(model.getValueAt(i, 4).toString());
                ji.setKeterangan("Pembelian barang " + noFaktur);
                ji.setInsertUser(user != null ? user.getUsername() : "admin");

                jurnalDAO.insert(ji);
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Transaksi pembelian berhasil disimpan!\nNo Faktur: " + noFaktur);
            clearInputFields();
            ((DefaultTableModel) jTable1.getModel()).setRowCount(0);
            lblSubtotal.setText("0");
        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage());
        }
    }
    
    public void setItemData(int idItem, String kode, String nama, String satuanKecil,
            String satuanBesar, double hargaJual, double konversi) {
        txtIDItem.setText(String.valueOf(idItem));
        txtKodeItem.setText(kode);
        txtNama.setText(nama);
        txtHarga.setText(formatAngka(hargaJual));
        selectedHarga = hargaJual;
        this.selectedSatuanKecil = satuanKecil;
        this.selectedSatuanBesar = satuanBesar;
        this.selectedKonversi = konversi;

        cmbSatuan.removeAllItems();
        if (satuanKecil != null && !satuanKecil.isEmpty()) cmbSatuan.addItem(satuanKecil);
        if (satuanBesar != null && !satuanBesar.isEmpty() && !satuanBesar.equalsIgnoreCase(satuanKecil)) {
            cmbSatuan.addItem(satuanBesar);
        }
        cmbSatuan.setSelectedItem(satuanKecil);
    }
    
    private void simpan() {
        System.out.println("DEBUG: simpan() terpanggil");
        String action = jLabel1.getText();
        System.out.println("DEBUG: Label aksi = " + action);

        if (action.equals("[Tambah]")) simpanTransaksi();
        else if (action.equals("[Ubah]")) updateTrans();
        else JOptionPane.showMessageDialog(null, "Aksi tidak dikenali: " + action);

        awal();
    }

    private void updateTrans() {
    }
    
    public void setSupplierData(int idSupplier, String namaSupplier) {
    // kalau kamu ingin menyimpan idDokter untuk keperluan simpan ke database,
    // buat variabel global di kelas, misalnya:
    this.selectedSupplierId = idSupplier;

    // hanya tampilkan nama di text field
    txtSupplier.setText(namaSupplier);
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
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cmbAktif = new javax.swing.JCheckBox();
        txtNoFaktur = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        txtKodeItem = new javax.swing.JTextField();
        txtJumlah = new javax.swing.JTextField();
        cmbSatuan = new javax.swing.JComboBox<>();
        txtNama = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JLabel();
        txtDiscTotal = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtDiscPersen = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        dtpTanggal = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtSupplier = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        cmbJenis = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        txtIDItem.setText("jTextField3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transaksi Penjualan Tunai");
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

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setForeground(new java.awt.Color(0, 102, 0));
        jLabel5.setText("1 of 9999");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
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
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(18, 18, 18)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExit)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cmbAktif))
        );

        txtNoFaktur.setMinimumSize(new java.awt.Dimension(33, 22));

        txtKodeItem.setMinimumSize(new java.awt.Dimension(33, 22));
        txtKodeItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKodeItemMouseClicked(evt);
            }
        });

        txtJumlah.setMinimumSize(new java.awt.Dimension(33, 22));
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJumlahKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJumlahKeyReleased(evt);
            }
        });

        cmbSatuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Box", "Dus", "Pack", "Vial", "Ampul", "Pouch", "Tubes", "Botol", "Kaleng", "Strip", "Pouch", " " }));

        txtNama.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        txtNama.setText("< Nama Item >");

        lblSubtotal.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubtotal.setText("Rp. 1.234.567");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Kode Barang :");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Jumlah");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Satuan");

        txtHarga.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        txtHarga.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtHarga.setText("1.234.567");

        txtDiscTotal.setMinimumSize(new java.awt.Dimension(33, 22));
        txtDiscTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiscTotalKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscTotalKeyReleased(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Diskon");

        txtDiscPersen.setMaximumSize(new java.awt.Dimension(33, 22));
        txtDiscPersen.setMinimumSize(new java.awt.Dimension(33, 22));
        txtDiscPersen.setName(""); // NOI18N
        txtDiscPersen.setOpaque(true);
        txtDiscPersen.setPreferredSize(new java.awt.Dimension(33, 22));
        txtDiscPersen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiscPersenKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscPersenKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("%");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKodeItem, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addGap(22, 22, 22)
                        .addComponent(cmbSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDiscPersen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDiscTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDiscPersen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtKodeItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtDiscTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode", "Nama Item", "Jumlah", "Satuan", "Diskon"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 27, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Tanggal :");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("No.Faktur :");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Supplier :");

        txtSupplier.setMinimumSize(new java.awt.Dimension(33, 22));
        txtSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSupplierMouseClicked(evt);
            }
        });

        jTextField5.setMinimumSize(new java.awt.Dimension(33, 22));

        cmbJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Biasa", "Resep" }));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Jenis :");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(dtpTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNoFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(dtpTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbJenis)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        tambah();
        
        // === AUTO GENERATE NO FAKTUR ===
    String noFaktur = generateNoFaktur();
    txtNoFaktur.setText(noFaktur);
    
    // === SET TANGGAL HARI INI ===
    dtpTanggal.setDate(new java.util.Date());
    
    // === ATUR FIELD DOKTER BERDASARKAN JENIS PENJUALAN ===
    Object selectedJenis = cmbJenis.getSelectedItem();
    if (selectedJenis != null) {
        String jenis = selectedJenis.toString();
        if (jenis.equalsIgnoreCase("Biasa")) {
            txtSupplier.setEnabled(false);
            txtSupplier.setText(""); // Kosongkan jika tidak perlu
        } else if (jenis.equalsIgnoreCase("Resep")) {
            txtSupplier.setEnabled(true);
        }
    }
    clearInputFields();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        // TODO add your handling code here:
        ubah();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void txtKodeItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKodeItemMouseClicked
        // TODO add your handling code here:
         if (evt.getClickCount() == 2) {
        // Membuka form lain
        BrowseItem dialog = new BrowseItem(this, true, conn);
        dialog.setVisible(true);
        
        // Opsional: menutup form saat ini
        // this.dispose();
    }
    }//GEN-LAST:event_txtKodeItemMouseClicked

    private void txtJumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyPressed
        // TODO add your handling code here:
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addItemFromInput();
        }
    }//GEN-LAST:event_txtJumlahKeyPressed

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJumlahKeyReleased

    private void txtDiscTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscTotalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscTotalKeyPressed

    private void txtDiscTotalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscTotalKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscTotalKeyReleased

    private void txtDiscPersenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscPersenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscPersenKeyPressed

    private void txtDiscPersenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscPersenKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscPersenKeyReleased

    private void txtSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSupplierMouseClicked
         if (evt.getClickCount() == 2) {
        // Membuka form lain
        BrowseSupplier dialog = new BrowseSupplier(this, true, conn);
        dialog.setVisible(true);   // TODO add your handling code here:
    }           // TODO add your handling code here:
    }//GEN-LAST:event_txtSupplierMouseClicked

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
    private javax.swing.JComboBox<String> cmbJenis;
    private javax.swing.JComboBox<String> cmbSatuan;
    private com.toedter.calendar.JDateChooser dtpTanggal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JTextField txtDiscPersen;
    private javax.swing.JTextField txtDiscTotal;
    private javax.swing.JLabel txtHarga;
    private javax.swing.JTextField txtIDItem;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKodeItem;
    private javax.swing.JLabel txtNama;
    private javax.swing.JTextField txtNoFaktur;
    private javax.swing.JTextField txtSupplier;
    // End of variables declaration//GEN-END:variables
}
