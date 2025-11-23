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
import model.TbeliHutang;
import dao.TbeliHutangDAO;
import dao.TjualPiutangDAO;
import dao.TjualhDAO;
import java.awt.Color;
import java.awt.Component;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import javax.swing.table.DefaultTableCellRenderer;
import model.ItemFull;
import model.Tbelih;
import model.Tbelid;
import model.TjualPiutang;
import model.Tjurnalitem;
import ui.Master.BrowseAll.BrowseSupplier;
//import ui.Master.BrowseAll.BrowseBeli;

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
    
    private TbelidDAO tbelidDAO;
    private Tbelid tbelid;
    private Tbelih tbelih;
    private TbelihDAO tbelihDAO;

    private List<Item> itemsList = new ArrayList<>();
    private String selectedSatuanKecil;
    private double selectedHarga;
    private String selectedSatuanBesar;
    private double selectedKonversi;
    private double setSubtotal = 0;
    private Integer selectedSupplierId;
    private List<Tbelih> list = new ArrayList<>();
    private int totalInputs;
    private int currentRecordIndex;
    DefaultTableModel model;
    JTable tblDetail;

    public frmTransPembelianBayar() {
        initComponents();
        //initializeDatabase();
        FormCreate();
        this.tbelihDAO = new TbelihDAO(conn);
        itemDAO = new ItemDAO(conn);
        btnALL();
        jToolBar1.setFloatable(false);
        jToolBar2.setFloatable(false);
        tambah();
        FrmProjec();
    }

    private void FrmProjec(){
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        txtHarga.setText(formatAngka(0));
        lblSubtotal.setText(formatAngka(0));
        txtNama.setText("< Nama Item >");
    }
    
    private void FormShow(){
         loadDataFromDatabase();
         loadCurrentItem(); 
    }

    private void FormCreate(){
        initializeDatabase();
        initTable();
        setupDiscEvents();
        setupJenisListener(); 
        cmbSatuan.addActionListener(e -> updateHargaBerdasarkanSatuan());
        txtJumlah.addActionListener(e -> addItemFromInput());
        txtDiscPersen.addActionListener(e ->{ addItemFromInput();  
                             updateSubtotal();
                    });
        txtDiscTotal.addActionListener(e -> {addItemFromInput(); updateSubtotal();
                    });
         cmbJenisTras.addActionListener(e -> {
                updateJatuhTempoVisibility();
                updateBayarVisibility();
                    });
    }
    
     private void btnALL() {
//        btnSimpan.addActionListener(evt -> simpan());
         btnExit.addActionListener(evr -> dispose());
         btnTambah.addActionListener(evt -> tambah());
         btnUbah.addActionListener(evt -> ubah());
         btnAwal.addActionListener(evt -> data_awal());
         btnPrevious.addActionListener(evt -> previous());
         btnNext.addActionListener(evt -> next());
         btnAkhir.addActionListener(evt -> data_terakhir());
        
    }
     
     private void loadDataFromDatabase() {
    try {
        list = tbelihDAO.getAll(); 
        totalInputs = list.size();
// ← butuh try-catch
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Gagal mengambil data penjualan: " + e.getMessage());
        return;
    }

    if (list == null) {
        list = new ArrayList<>();
    }

    if (list.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Data Item belum ada di database.");
    } else {
        currentRecordIndex = 0;
        loadCurrentItem();
    }
}
     
     
    
    private void addItemFromInput() {
        try {
            if (txtIDBeli.getText().isEmpty() || txtNama.getText().isEmpty() ||
                txtJumlah.getText().isEmpty() || txtHarga.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
                return;
            }

            if (cmbSatuan.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Satuan belum dipilih!");
                return;
            }

            int idItem = Integer.parseInt(txtIDBeli.getText());
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
            double total = qty * harga - discTotal;
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
        jtTotal.setText(lblSubtotal.getText()); 
    }
    
    private void clearInputFields() {
    txtIDBeli.setText("");
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
    
    private void updateJatuhTempoVisibility() {
    // Berlaku hanya saat mode tambah
    if (!jLabel1.getText().equals("[Tambah]")) return;

    String jenis = cmbJenisTras.getSelectedItem().toString();

    if (jenis.equalsIgnoreCase("Tunai")) {
        dtpJatuhTempo.setVisible(false);
        lblJatuhTempo.setVisible(false); // label jatuh tempo
        dtpJatuhTempo.setDate(null);

    } else if (jenis.equalsIgnoreCase("Kredit")) {
        dtpJatuhTempo.setVisible(true);
        lblJatuhTempo.setVisible(true);

        // set default +7 hari dari tanggal transaksi
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(dtpTanggal.getDate());
        cal.add(java.util.Calendar.DAY_OF_MONTH, 7);
        dtpJatuhTempo.setDate(cal.getTime());
    }
}
    
    private void updateBayarVisibility() {
        String jenis = cmbJenisTras.getSelectedItem().toString();

        if (jenis.equalsIgnoreCase("Tunai")) {
            btnBayar.setEnabled(true);
            txtBayar.setText("");
            txtKembalian.setText("0");
            btnSimpan.setEnabled(false);

        } else { // Kredit
            btnBayar.setEnabled(false);
            btnSimpan.setEnabled(true);
        }
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
        
        
        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        setOpaque(true); // PENTING

        if (isSelected) {
            c.setBackground(new Color(135, 206, 250));
            c.setForeground(Color.BLACK);
        } else {
            if (row % 2 == 0) c.setBackground(Color.WHITE);
            else c.setBackground(new Color(204, 255, 204));
            c.setForeground(Color.BLACK);
        }
        return c;
    }
});
    }
    
    private void setupDiscEvents() {
        txtDiscPersen.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateDiscTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateDiscTotal(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateDiscTotal(); }
        });
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
        FormShow();
    }
    private void tambah(){
        generateNoFaktur();
        jLabel1.setText("[Tambah]");
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(true);
        updateBayarVisibility();
        navnonaktif();
        ((DefaultTableModel) jTable1.getModel()).setRowCount(0);
        dtpTanggal.setDate(new java.util.Date()); // set tanggal hari ini
        dtpTanggal.setDateFormatString("dd/MM/yyyy"); // ubah format tampilan 
        updateJatuhTempoVisibility();
        clearInputFields();
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
    
    private void clearInputSelectedFields() {
        selectedSupplierId = null;
        txtSupplier.setText("");
        dtpJatuhTempo.setDate(null);
    }
    
    

    private String formatAngka(double value) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(value);
    }

    private double parseAngka(String value) {
        return Double.parseDouble(value.replace(",", "").replace(".", ""));
    }

    // === GENERATE NO FAKTUR BELI ===
    private void generateNoFaktur() {
    try {
        // Prefix berdasarkan tahun dan bulan
        LocalDate today = LocalDate.now();
        String year  = String.format("%02d", today.getYear() % 100); // contoh: 25
        String month = String.format("%02d", today.getMonthValue()); // contoh: 11

        String prefix = "PB" + year + month; // contoh: PB2511

        // DAO khusus header pembelian
        TbelihDAO dao = new TbelihDAO(conn);

        // Ambil nomor terakhir dari database
        String lastCode = dao.getLastKode(prefix);
        String newCode;

        if (lastCode != null && lastCode.startsWith(prefix)) {

            // Ambil angka urut setelah prefix, misal PB251100015 → 00015
            int number = Integer.parseInt(lastCode.substring(prefix.length()));

            number++; // increment nomor urut

            newCode = prefix + String.format("%05d", number); 
            // contoh hasil: PB251100016

        } else {
            // Jika belum ada transaksi bulan ini
            newCode = prefix + "00001"; // PB251100001
        }

        txtNoFaktur.setText(newCode);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error generate nomor faktur: " + e.getMessage());
        e.printStackTrace();
    }
}


    // === SIMPAN TRANSAKSI PEMBELIAN ===
    private void simpanTransaksi() {
        try {
            conn.setAutoCommit(false);
            
            
            String noFaktur = txtNoFaktur.getText();
            java.sql.Date tanggal = new java.sql.Date(System.currentTimeMillis());
            String jenisBayar = cmbJenisTras.getSelectedItem().toString(); 
             String status = "";
                if ("Tunai".equals(jenisBayar)) {
                    status = "Lunas";  
                } else {
                    status = "Open";
                }
                
                 double ppnValue = cmbPPN.isSelected() ? 0.11 : 0.0;

            // --- HEADER ---
            Tbelih belih = new Tbelih();
            belih.setKode(noFaktur);
            belih.setTanggal(tanggal);
            belih.setJenisBayar(jenisBayar);
            belih.setJatuhTempo(tanggal);
            if (selectedSupplierId == null || selectedSupplierId == 0) {
                belih.setIdSupplier(null);
                } else {
                    belih.setIdSupplier(selectedSupplierId);
                }
            
            belih.setSubTotal(parseAngka(lblSubtotal.getText()));
            belih.setDiskon(0);
            belih.setPpn(ppnValue);
            belih.setTotal(parseAngka(lblSubtotal.getText()));
            belih.setNominal(parseAngka(txtBayar.getText()));
            belih.setStatus(status);
            belih.setInsertUser(user != null ? user.getUsername() : "admin");

            TbelihDAO belihDAO = new TbelihDAO(conn);
            belihDAO.insert(belih);

            // --- DAPATKAN ID HEADER TERAKHIR ---
            int idBeliH = 0;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID() AS id")) {
                if (rs.next()) idBeliH = rs.getInt("id");
            }
            
            int idSupplier = 0;
            String sqlGetSupp = "SELECT IDSupplier FROM tbelih WHERE idBeliH = ?";
            
            try (PreparedStatement pst = conn.prepareStatement(sqlGetSupp)) {
            pst.setInt(1, idBeliH);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    idSupplier = rs.getInt("IDCust");
                }
            }
        }
            
            // === INSERT HUTANG JIKA KREDIT ===
        if (jenisBayar.equalsIgnoreCase("Kredit")) {

        TbeliHutangDAO pdao = new TbeliHutangDAO(conn);
        TbeliHutang p = new TbeliHutang();
        
        java.sql.Date dueDate = new java.sql.Date(dtpJatuhTempo.getDate().getTime());
        
        
        p.setIdBeliH(idBeliH);
        p.setIdSupplier(idSupplier);
        p.setNoFaktur(noFaktur);
        p.setTanggal(tanggal);
        p.setJatuhTempo(dueDate);
        p.setSisaHutang(parseAngka(lblSubtotal.getText())); // piutang awal = total

        pdao.insert(p);

        System.out.println("DEBUG: Hutang berhasil disimpan untuk transaksi kredit.");
        }

            // --- DETAIL ---
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            TbelidDAO belidDAO = new TbelidDAO(conn);
            TjurnalitemDAO jurnalDAO = new TjurnalitemDAO(conn);

            for (int i = 0; i < model.getRowCount(); i++) {
                int idItem = Integer.parseInt(model.getValueAt(i, 1).toString());
                double qty = parseAngka(model.getValueAt(i, 5).toString());
                double harga = parseAngka(model.getValueAt(i, 6).toString());
                double diskon = parseAngka(model.getValueAt(i, 7).toString());
                double total = parseAngka(model.getValueAt(i, 8).toString());

                Tbelid belid = new Tbelid();
                belid.setIdBeliH(idBeliH);
                belid.setIdItemD(idItem);
                belid.setQty(qty);
                belid.setHarga(harga);
                belid.setTotal(total);
                belid.setDiskon(diskon);
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
            printNotaKecil();
            clearInputFields();
            clearInputSelectedFields();
            ((DefaultTableModel) jTable1.getModel()).setRowCount(0);
            lblSubtotal.setText("0");
            generateNoFaktur();
        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage());
        }
    }
    
    public void setItemData(int idItem, String kode, String nama, String satuanKecil,
            String satuanBesar, double hargaJual, double konversi) {
        txtIDBeli.setText(String.valueOf(idItem));
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
        String action = jLabel1.getText();

        if (action.equals("[Tambah]")) { simpanTransaksi();
        } else if (action.equals("[Ubah]")) { updateTrans();
        } else  { JOptionPane.showMessageDialog(null, "Aksi tidak dikenali: " + action);}

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
    
    private void loadCurrentItem() {
    if (currentRecordIndex >= 0 && currentRecordIndex < list.size()) {

        Tbelih tjbelih = list.get(currentRecordIndex); // ganti Tjualh → Tbelih

        // HEADER
        txtIDBeli.setText(String.valueOf(tjbelih.getIdBeliH())); // ganti ID field
        txtNoFaktur.setText(tjbelih.getKode());
        dtpTanggal.setDate(tjbelih.getTanggal());
        dtpJatuhTempo.setDate(tjbelih.getJatuhTempo());
        txtSupplier.setText(String.valueOf(tjbelih.getIdSupplier())); // ganti txtCustomer → txtSupplier

        lblSubtotal.setText(formatAngka(tjbelih.getSubTotal()));
        txtDiscTotal.setText(formatAngka(tjbelih.getDiskon()));
        txtHarga.setText(formatAngka(tjbelih.getTotal()));

        // DETAIL → isi ke JTable
        tampilkanDetailKeTabel(tjbelih.getDetails());

        updateRecordLabel();
    } else {
        JOptionPane.showMessageDialog(null, "Indeks catatan tidak valid.");
    }
}
    
    private void tampilkanDetailKeTabel(List<Tbelid> details) { 
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    for (Tbelid d : details) {
        try {
            ItemFull item = itemDAO.getFullItem(d.getIdItemD());

            String kode = item != null ? item.getKode() : "";
            String nama = item != null ? item.getNama() : "";
            String satuan = item != null ? item.getSatuanBesar() : "";

            model.addRow(new Object[]{
                "X",                        // 0 Tombol hapus
                d.getIdItemD(),             // 1 ID item
                kode,                       // 2 Kode item
                nama,                       // 3 Nama item
                satuan,                     // 4 Satuan
                formatAngka(d.getQty()),    // 5 Qty
                formatAngka(d.getHarga()),  // 6 Harga
                formatAngka(d.getDiskon()), // 7 Diskon
                formatAngka(d.getTotal())   // 8 Total
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Gagal memuat item detail: " + e.getMessage());
        }
    }
}
    
    private int selectedBeliH;

public void setSelectedBeli(int idBeliH) {
    try {
        this.selectedBeliH = idBeliH;

        // Ambil data header lengkap beserta detailnya
        Tbelih tbelih = tbelihDAO.getById(idBeliH);

        if (tbelih == null) {
            JOptionPane.showMessageDialog(this,
                "Data transaksi tidak ditemukan.");
            return;
        }

        // Tampilkan HEADER ke form
        txtIDBeli.setText(String.valueOf(tbelih.getIdBeliH()));
        txtNoFaktur.setText(tbelih.getKode());
        dtpTanggal.setDate(tbelih.getTanggal());
        dtpJatuhTempo.setDate(tbelih.getJatuhTempo());
        cmbJenisTras.setSelectedItem(tbelih.getJenisBayar());
        txtSupplier.setText(String.valueOf(tbelih.getIdSupplier()));

        lblSubtotal.setText(formatAngka(tbelih.getSubTotal()));
        txtDiscTotal.setText(formatAngka(tbelih.getDiskon()));
        txtHarga.setText(formatAngka(tbelih.getTotal()));

        // Tampilkan DETAIL ke JTable
        tampilkanDetailKeTabel(tbelih.getDetails());

    } catch (Exception ex) {
        ex.printStackTrace();
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
    
    private boolean kembaliNegatif = false;
    
    private void prosesBayar() {
    String input = txtBayar.getText();

    String angka = input.replaceAll("\\D", "");
    if (angka.isEmpty()) {
        txtKembalian.setText("0");
        kembaliNegatif = false;
        return;
    }

    // Format ribuan
    StringBuilder sb = new StringBuilder(angka);
    for (int i = sb.length() - 3; i > 0; i -= 3) {
        sb.insert(i, ".");
    }

    txtBayar.setText(sb.toString());
    txtBayar.setCaretPosition(sb.length());

    // Hitung kembalian
    double nominal = Double.parseDouble(angka);
    double subtotal = Double.parseDouble(lblSubtotal.getText().replace(".", ""));
    double kembali = nominal - subtotal;

    // Flag: apakah negatif?
    kembaliNegatif = (kembali < 0);

    txtKembalian.setText(formatRibuan(kembali));
}

// Format ribuan untuk angka double
private String formatRibuan(double nilai) {
    long n = (long) nilai;
    String angka = String.valueOf(Math.abs(n));

    StringBuilder sb = new StringBuilder(angka);
    for (int i = sb.length() - 3; i > 0; i -= 3) {
        sb.insert(i, ".");
    }

    return (n < 0 ? "-" : "") + sb.toString();
}
private void printNotaKecil() {
    try {
        String toko = "APOTEK RH";
        String alamat = "Jl. Kalijudan 15 – Surabaya";
        String tanggal = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        String kasir = System.getProperty("user.name");

        // Ambil total, bayar, kembali dari form
        String diskon = txtDiscTotal.getText(); 
        String subtotal = lblSubtotal.getText();
        String total = jtTotal.getText();
        String bayar = txtBayar.getText();
        String kembali = txtKembalian.getText();
                        System.out.println("=== DEBUG TABEL ===");
        System.out.println("Row count: " + jTable1.getRowCount());

        for (int i = 0; i < jTable1.getRowCount(); i++) {
            System.out.println("Nama: " + jTable1.getValueAt(i, 3));
            System.out.println("Qty : " + jTable1.getValueAt(i, 5));
            System.out.println("Total: " + jTable1.getValueAt(i, 8));
        }
        System.out.println("====================");


        // Builder untuk isi nota
        StringBuilder sb = new StringBuilder();

        sb.append(centerText(toko, 32)).append("\n");
        sb.append(centerText(alamat, 32)).append("\n");
        sb.append("--------------------------------\n");
        sb.append("Tanggal : ").append(tanggal).append("\n");
        sb.append("Kasir   : ").append(kasir).append("\n");
        sb.append("--------------------------------\n");
        sb.append(String.format("%-14s %4s %10s\n", "Item", "Qty", "Total"));

        // Loop isi JTable
        for (int i = 0; i < jTable1.getRowCount(); i++) {

            String nama = jTable1.getValueAt(i, 3).toString();
            String qty  = jTable1.getValueAt(i, 5).toString();
            String harga = jTable1.getValueAt(i, 6).toString();
            String subtotalItem = jTable1.getValueAt(i, 8).toString();

            // Baris nama
            sb.append(potong(nama, 32)).append("\n");

            // Baris "harga x qty     subtotal"
            String line2 = String.format("%s x %s", harga, qty);

            sb.append(String.format("%-20s %12s\n",
                    potong(line2, 20),
                    subtotalItem
            ));
        }


           sb.append("--------------------------------\n");

           sb.append(String.format("%-20s %10s\n", "Diskon",  "Rp " + diskon));
           sb.append(String.format("%-20s %10s\n", "Grand",   "Rp " + total));
           sb.append(String.format("%-20s %10s\n", "Tunai",   "Rp " + bayar));
           sb.append(String.format("%-20s %10s\n", "Kembali", "Rp " + kembali));

           sb.append("--------------------------------\n\n");

        sb.append(centerText("Terima Kasih", 32)).append("\n");
        sb.append("\n\n");

        // Print
        printRaw(sb.toString());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal print: " + e.getMessage());
    }
}
private String centerText(String text, int width) {
    int padSize = (width - text.length()) / 2;
    if (padSize < 0) padSize = 0;
    return " ".repeat(padSize) + text;
}

private String potong(String text, int max) {
    return text.length() > max ? text.substring(0, max) : text;
}

private void printRaw(String data) throws Exception {
    javax.print.PrintService service = javax.print.PrintServiceLookup.lookupDefaultPrintService();
    if (service == null) {
        JOptionPane.showMessageDialog(this, "Tidak ada default printer!");
        return;
    }

    javax.print.DocPrintJob job = service.createPrintJob();
    byte[] bytes = data.getBytes("GB18030"); // aman untuk ESC/POS

    javax.print.Doc doc = new javax.print.SimpleDoc(
            bytes,
            javax.print.DocFlavor.BYTE_ARRAY.AUTOSENSE,
            null
    );

    job.print(doc, null);
}






    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtIDBeli = new javax.swing.JTextField();
        jdBayar = new javax.swing.JDialog();
        Pebayaran = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jtTotal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtKembalian = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
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
        dtpJatuhTempo = new com.toedter.calendar.JDateChooser();
        lblJatuhTempo = new javax.swing.JLabel();
        cmbJenisTras = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cmbAktif = new javax.swing.JCheckBox();
        cmbPPN = new javax.swing.JCheckBox();
        btnBayar = new javax.swing.JButton();
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

        txtIDBeli.setText("jTextField3");

        jdBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jdBayarKeyReleased(evt);
            }
        });

        Pebayaran.setBackground(new java.awt.Color(255, 255, 255));
        Pebayaran.setMinimumSize(new java.awt.Dimension(415, 282));

        jPanel8.setBackground(new java.awt.Color(0, 255, 204));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Pembayaran");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Total :");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Pembayaran :");

        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBayarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBayarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBayarKeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Kembali :");

        jRadioButton1.setText("Tunai");

        jRadioButton2.setText("Transfer");

        javax.swing.GroupLayout PebayaranLayout = new javax.swing.GroupLayout(Pebayaran);
        Pebayaran.setLayout(PebayaranLayout);
        PebayaranLayout.setHorizontalGroup(
            PebayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PebayaranLayout.createSequentialGroup()
                .addGroup(PebayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PebayaranLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PebayaranLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PebayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PebayaranLayout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PebayaranLayout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PebayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PebayaranLayout.createSequentialGroup()
                                        .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        PebayaranLayout.setVerticalGroup(
            PebayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PebayaranLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PebayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtTotal)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(PebayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PebayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtBayar)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PebayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtKembalian)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 68, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jdBayarLayout = new javax.swing.GroupLayout(jdBayar.getContentPane());
        jdBayar.getContentPane().setLayout(jdBayarLayout);
        jdBayarLayout.setHorizontalGroup(
            jdBayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
            .addGroup(jdBayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jdBayarLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Pebayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jdBayarLayout.setVerticalGroup(
            jdBayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(jdBayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jdBayarLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Pebayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

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

        recordLabel.setBackground(new java.awt.Color(0, 0, 0));
        recordLabel.setForeground(new java.awt.Color(0, 102, 0));
        recordLabel.setText("1 of 9999");

        lblJatuhTempo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblJatuhTempo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblJatuhTempo.setText("Jatuh Tempo :");

        cmbJenisTras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tunai", "Kredit" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(recordLabel)
                .addGap(34, 34, 34)
                .addComponent(cmbJenisTras, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblJatuhTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(dtpJatuhTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dtpJatuhTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(recordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbJenisTras, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblJatuhTempo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(0, 255, 204));

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

        cmbPPN.setText("PPN 11%");

        btnBayar.setText("Bayar");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(cmbAktif)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbPPN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExit)
                .addGap(12, 12, 12))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cmbAktif)
                .addComponent(cmbPPN))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBayar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
                        .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                            .addComponent(txtDiscPersen, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
//        tambah();
//        
//        // === AUTO GENERATE NO FAKTUR ===
//    String noFaktur = generateNoFaktur();
//    txtNoFaktur.setText(noFaktur);
//    
//    // === SET TANGGAL HARI INI ===
//    dtpTanggal.setDate(new java.util.Date());
//    
//    // === ATUR FIELD DOKTER BERDASARKAN JENIS PENJUALAN ===
//    Object selectedJenis = cmbJenis.getSelectedItem();
//    if (selectedJenis != null) {
//        String jenis = selectedJenis.toString();
//        if (jenis.equalsIgnoreCase("Biasa")) {
//            txtSupplier.setEnabled(false);
//            txtSupplier.setText(""); // Kosongkan jika tidak perlu
//        } else if (jenis.equalsIgnoreCase("Resep")) {
//            txtSupplier.setEnabled(true);
//        }
//    }
//    clearInputFields();
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

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
       awal();//DO add your handling code here:
        clearInputFields(); // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            // ❗ Jika kembalian minus → TOLAK enter
            if (kembaliNegatif) {
                JOptionPane.showMessageDialog(
                    this,
                    "Pembayaran kurang! Silakan masukkan nominal yang cukup.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                evt.consume();      // blok enter
                txtBayar.requestFocus();
                return;
            }

            // ❗ Jika kembalian cukup → lanjut seperti biasa
            simpanTransaksi();
            jdBayar.dispose();
        }           // TODO add your handling code here:
    }//GEN-LAST:event_txtBayarKeyPressed

    private void txtBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyReleased
        prosesBayar();        // TODO add your handling code here:
    }//GEN-LAST:event_txtBayarKeyReleased

    private void txtBayarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBayarKeyTyped

    private void jdBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jdBayarKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jdBayarKeyReleased

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    int rowCount = model.getRowCount();

    // Cek subtotal
    double subtotal = parseAngka(lblSubtotal.getText());

    if (rowCount == 0 || subtotal == 0) {
        JOptionPane.showMessageDialog(this, 
                "Data transaksi masih kosong!\nSilakan masukkan item terlebih dahulu.",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
        return; // Stop proses, jangan buka dialog
    }
    simpanTransaksi();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed

        // Cek jumlah baris di tabel
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int rowCount = model.getRowCount();

        // Cek subtotal
        double subtotal = parseAngka(lblSubtotal.getText());

        if (rowCount == 0 || subtotal == 0) {
            JOptionPane.showMessageDialog(this,
                "Data transaksi masih kosong!\nSilakan masukkan item terlebih dahulu.",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return; // Stop proses, jangan buka dialog
        }

        // --- Jika lolos validasi, tampilkan JDialog ---
        jdBayar.pack();
        jdBayar.setLocationRelativeTo(null);
        jdBayar.setResizable(false);
        jdBayar.setVisible(true);  // TODO add your handling code here:
    }//GEN-LAST:event_btnBayarActionPerformed

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
    private javax.swing.JPanel Pebayaran;
    private javax.swing.JButton btnAkhir;
    private javax.swing.JButton btnAwal;
    private javax.swing.JButton btnBayar;
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
    private javax.swing.JComboBox<String> cmbJenisTras;
    private javax.swing.JCheckBox cmbPPN;
    private javax.swing.JComboBox<String> cmbSatuan;
    private com.toedter.calendar.JDateChooser dtpJatuhTempo;
    private com.toedter.calendar.JDateChooser dtpTanggal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JDialog jdBayar;
    private javax.swing.JTextField jtTotal;
    private javax.swing.JLabel lblJatuhTempo;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtDiscPersen;
    private javax.swing.JTextField txtDiscTotal;
    private javax.swing.JLabel txtHarga;
    private javax.swing.JTextField txtIDBeli;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtKodeItem;
    private javax.swing.JLabel txtNama;
    private javax.swing.JTextField txtNoFaktur;
    private javax.swing.JTextField txtSupplier;
    // End of variables declaration//GEN-END:variables
}
