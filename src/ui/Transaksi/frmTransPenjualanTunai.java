/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.Transaksi;
import dao.ItemDAO;
import dao.Koneksi;
import dao.TjualPiutangDAO;
import dao.cell.ButtonEditor;
import dao.cell.ButtonRenderer;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Item;
import model.ItemDetail;
import model.User;
import ui.Master.BrowseAll.BrowseItem;
import ui.Master.BrowseAll.BrowseDokter;
import ui.Master.BrowseAll.BrowsePelangganDialog;
import ui.Master.BrowseAll.BrowseCustomer;
import dao.TjualhDAO;
import dao.TjualdDAO;
import dao.TjurnalitemDAO;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import model.ItemFull;
import model.Session;
import model.TjualPiutang;
import model.Tjualh;
import model.Tjuald;
import model.Tjurnalitem;
import ui.Master.BrowseAll.BrowseCustomer;

/**
 *
 * @author Admin
 */
public class frmTransPenjualanTunai extends javax.swing.JFrame {
    private Connection conn;
    private ItemDAO itemDAO;
    private Statement stat;
    private ResultSet rs;
    private String sql;
    private User user;
   // private LisList;
    private TjualdDAO tjualdDAO;
    private Tjuald tjuald;
    private Tjualh  tjualh;
    private TjualhDAO tjualhDAO;
    private List<Item> itemsList = new ArrayList<>();
    private String selectedSatuanKecil;
    private double selectedHarga;
    private String selectedSatuanBesar;
    private Integer selectedDokterId;
    private Integer selectedCustomerId;
    private List<Tjualh> list = new ArrayList<>();
    private int totalInputs;
    private int currentRecordIndex;

    private double selectedKonversi;
   private double setSubtotal = 0;
    DefaultTableModel model;
    JTable tblDetail;

    /**
     * Creates new form ParentTrans
     */
    public frmTransPenjualanTunai() {
        initComponents();
        FormCreate();
        this.tjualhDAO = new TjualhDAO(conn);
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
           //TjualhDAO dao = new TjualhDAO(conn);
           cmbSatuan.addActionListener(e -> updateHargaBerdasarkanSatuan());
           txtJumlah.addActionListener(e -> addItemFromInput());
            txtDiscPersen.addActionListener(e -> {
                addItemFromInput();
                updateSubtotal();
                    });
            txtDiscTotal.addActionListener(e -> {
                addItemFromInput();
                updateSubtotal();
                    });
            cmbPPN.addActionListener(e -> updateSubtotal());
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
        list = tjualhDAO.getAll(); 
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
     
     private void setupJenisListener() {
    cmbJenis.addActionListener(e -> {
        String jenis = cmbJenis.getSelectedItem().toString();
        if (jenis.equalsIgnoreCase("Biasa")) {
            txtDokter.setEnabled(false);
            txtDokter.setText("");
        } else if (jenis.equalsIgnoreCase("Resep")) {
            txtDokter.setEnabled(true);
        }
    });
}
     
     private void updateJatuhTempoVisibility() {
    // Berlaku hanya saat mode tambah
    if (!jLabel1.getText().equals("[Tambah]")) return;

    String jenis = cmbJenisTras.getSelectedItem().toString();

    if (jenis.equalsIgnoreCase("Tunai")) {
        dtpJatuhTempo.setVisible(false);
        lblJatuhTempo.setVisible(false); // kalau ada labelnya
        dtpJatuhTempo.setDate(null);
        lblCustomer.setVisible(false);
        txtCustomer.setVisible(false);
        
    } else if (jenis.equalsIgnoreCase("Kredit")) {
        dtpJatuhTempo.setVisible(true);
        lblJatuhTempo.setVisible(true);
                // Customer dimunculkan
        lblCustomer.setVisible(true);
        txtCustomer.setVisible(true);

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


private void initTable() {
    DefaultTableModel model = new DefaultTableModel(
        new Object[]{"X", "ID","Kode", "Nama", "Satuan", "Qty", "Harga", "Diskon", "Total"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0; // hanya kolom "X"
        }
    };
    jTable1.setModel(model);

    // Hilangkan garis grid
    jTable1.setShowGrid(false);
    jTable1.setIntercellSpacing(new java.awt.Dimension(0, 0));

    // Matikan auto resize supaya kolom tidak menyesuaikan lebar table
    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    // Renderer & Editor untuk tombol "X"
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



    // Warna baris bergantian (striped rows)
   // Warna baris bergantian (striped rows)
jTable1.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
    @Override
    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            setBackground(new java.awt.Color(135, 206, 250)); // biru muda saat dipilih
            setForeground(java.awt.Color.BLACK);
        } else {
            if (row % 2 == 0) {
                setBackground(java.awt.Color.WHITE); // baris genap putih
            } else {
                setBackground(new java.awt.Color(204, 255, 204)); // baris ganjil hijau muda
            }
            setForeground(java.awt.Color.BLACK);
        }
        return this;
    }
});


    // Optional: header warna
    jTable1.getTableHeader().setBackground(new java.awt.Color(0, 102, 0)); // hijau tua
    jTable1.getTableHeader().setForeground(java.awt.Color.WHITE); // teks putih
    jTable1.getTableHeader().setFont(jTable1.getTableHeader().getFont().deriveFont(java.awt.Font.BOLD)); // opsional: teks tebal

  // Kolom angka rata kanan
    javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
    rightRenderer.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        jTable1.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        jTable1.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        jTable1.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
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


private String formatAngka(double value) {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator('.');
    symbols.setDecimalSeparator(',');

    DecimalFormat df = new DecimalFormat("#,##0", symbols);
    return df.format(value);
}

private double parseAngka(String value) {
    if (value == null || value.trim().isEmpty()) return 0;

    // hilangkan spasi
    value = value.trim();

    // Hilangkan pemisah ribuan (.)
    value = value.replace(".", "");

    // Ganti koma menjadi titik untuk parsing desimal
    value = value.replace(",", ".");

    try {
        return Double.parseDouble(value);
    } catch (Exception e) {
        return 0;
    }
}


    private void navaktif(){
     btnAwal.setEnabled(true);
     btnPrevious.setEnabled(true);
     btnNext.setEnabled(true);
     btnAkhir.setEnabled(true);
     btnHapus.setEnabled(true);
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
        IDotomatis();
        jLabel1.setText("[Tambah]");
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        updateBayarVisibility();
        btnCancel.setEnabled(true);
        btnExit.setEnabled(true);
        navnonaktif();
            clearInputFields();
    ((DefaultTableModel) jTable1.getModel()).setRowCount(0);
        dtpTanggal.setDate(new java.util.Date()); // set tanggal hari ini
        dtpTanggal.setDateFormatString("dd/MM/yyyy"); // ubah format tampilan 
        updateJatuhTempoVisibility();
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
    
  public void setItemData(int idItem, String kode, String nama, String satuanKecil,
            String satuanBesar, double hargaJual, double konversi) {
        txtIDJual.setText(String.valueOf(idItem));
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
private void clearInputFields() {
    txtIDJual.setText("");
    txtKodeItem.setText("");
     txtNama.setText("< Nama Item >");
    txtJumlah.setText("0");
    txtHarga.setText(formatAngka(0));
    cmbSatuan.removeAllItems();
    txtDiscPersen.setText("");
    txtDiscTotal.setText("");
}

    private void clearInputSelectedFields() {
        selectedDokterId = null;
        txtDokter.setText("");
        selectedCustomerId = null;
        txtCustomer.setText("");
        dtpJatuhTempo.setDate(null);
    }

    // === TAMBAH ITEM KE TABLE ===
    private void addItemFromInput() {
        try {
            if (txtIDJual.getText().isEmpty() || txtNama.getText().isEmpty() ||
                txtJumlah.getText().isEmpty() || txtHarga.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
                return;
            }

            if (cmbSatuan.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Satuan belum dipilih!");
                return;
            }

            int idItem = Integer.parseInt(txtIDJual.getText());
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

    // === UPDATE HARGA BERDASARKAN SATUAN ===
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

    // === UPDATE SUBTOTAL ===
    public void updateSubtotal() {
double subtotal = 0;
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

    // Hitung subtotal dari tabel
    for (int i = 0; i < model.getRowCount(); i++) {
        subtotal += parseAngka(model.getValueAt(i, 8).toString());
    }

    double totalSetelahPPN = subtotal;

    // Jika checkbox PPN dicentang → tambahkan 11%
    if (cmbPPN.isSelected()) {
        totalSetelahPPN = subtotal + (subtotal * 0.11);
    }

    // Tampilkan
    lblSubtotal.setText(formatAngka(totalSetelahPPN));
    jtTotal.setText(lblSubtotal.getText()); 
    }
    
private void simpanTransaksi() {
    try {
        // 🔹 Buat koneksi
        conn.setAutoCommit(false); // mulai transaksi

        // 🔹 Generate kode faktur
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

    // Lanjutkan proses penyimpanan transaksi sesuai jenis pembayaran


        // --- INSERT HEADER ---
        Tjualh jualh = new Tjualh();
        jualh.setKode(noFaktur);
        jualh.setTanggal(tanggal);
        jualh.setJenisBayar(jenisBayar);
        jualh.setJatuhTempo(tanggal);
        
        if (selectedDokterId == null || selectedDokterId == 0) {
        jualh.setIdDokter(null);
        } else {
            jualh.setIdDokter(selectedDokterId);
        }
        
        if (selectedCustomerId == null || selectedCustomerId == 0) {
        jualh.setIdCust(null);
        } else {
            jualh.setIdCust(selectedCustomerId);
        }
        
        jualh.setSubTotal(parseAngka(lblSubtotal.getText()));
        jualh.setDiskon(parseAngka(txtDiscTotal.getText()));
        jualh.setPpn(ppnValue);
        jualh.setTotal(parseAngka(lblSubtotal.getText()));
        jualh.setNominal(parseAngka(txtBayar.getText()));
        jualh.setStatus(status);
        jualh.setInsertUser(user != null ? user.getUsername() : "admin");

        TjualhDAO jualhDAO = new TjualhDAO(conn);
        jualhDAO.insert(jualh);

        // 🔹 Dapatkan ID header terakhir
        int idJualH = 0;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID() AS id")) {
            if (rs.next()) idJualH = rs.getInt("id");
        }
        
        int idCust = 0;
        String sqlGetCust = "SELECT IDCust FROM tjualh WHERE idJualH = ?";

        try (PreparedStatement pst = conn.prepareStatement(sqlGetCust)) {
            pst.setInt(1, idJualH);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    idCust = rs.getInt("IDCust");
                }
            }
        }
        
        
        // === INSERT PIUTANG JIKA KREDIT ===
        if (jenisBayar.equalsIgnoreCase("Kredit")) {

        TjualPiutangDAO pdao = new TjualPiutangDAO(conn);
        TjualPiutang p = new TjualPiutang();

        java.sql.Date dueDate = new java.sql.Date(dtpJatuhTempo.getDate().getTime());

        p.setIdJualH(idJualH);
        p.setIdCust(idCust);
        p.setNoFaktur(noFaktur);
        p.setTanggal(tanggal);
        p.setJatuhTempo(dueDate);
        p.setSisaPiutang(parseAngka(lblSubtotal.getText())); // piutang awal = total

        pdao.insert(p);

        System.out.println("DEBUG: Piutang berhasil disimpan untuk transaksi kredit.");
        }


        // --- INSERT DETAIL ---
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        TjualdDAO jualdDAO = new TjualdDAO(conn);
        TjurnalitemDAO jurnalDAO = new TjurnalitemDAO(conn);

        for (int i = 0; i < model.getRowCount(); i++) {
            int idItem = Integer.parseInt(model.getValueAt(i, 1).toString());
            double qty = parseAngka(model.getValueAt(i, 5).toString());
            double harga = parseAngka(model.getValueAt(i, 6).toString());
            double diskon = parseAngka(model.getValueAt(i, 7).toString());
            double total = parseAngka(model.getValueAt(i, 8).toString());

            Tjuald juald = new Tjuald();
            juald.setIdJualH(idJualH);
            juald.setIdItemD(idItem);
            juald.setQty(qty);
            juald.setHarga(harga);
            juald.setTotal(total);
            juald.setDiskon(diskon);
            juald.setQtyBase(qty);

            jualdDAO.insert(juald);

            // --- INSERT KE JURNAL STOK ---
            Tjurnalitem ji = new Tjurnalitem();
            ji.setTanggal(tanggal);
            ji.setIdItem(idItem);
            ji.setKodeTrans(noFaktur);
            ji.setJenisTrans("Jual");
            ji.setQtyMasuk(0);
            ji.setQtyKeluar(qty);
            ji.setSatuan(model.getValueAt(i, 4).toString());
            ji.setKeterangan("Penjualan tunai " + noFaktur);
            ji.setInsertUser(user != null ? user.getUsername() : "admin");

            jurnalDAO.insert(ji);
        }

        conn.commit();
        JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!\nNo Faktur: " + noFaktur);
        clearInputFields();
        clearInputSelectedFields();
        ((DefaultTableModel) jTable1.getModel()).setRowCount(0);
        lblSubtotal.setText("0");

    } catch (Exception e) {
        try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage());
    }
}
//simpan data
private void simpan() {
    String action = jLabel1.getText();
     if (cmbJenis.getSelectedItem().toString().equalsIgnoreCase("Resep")) {
        if (txtDokter.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "Dokter harus diisi jika jenis = Resep!",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtDokter.requestFocus();
            return; // HENTIKAN proses simpan
        }
    }
    if (action.equals("[Tambah]")) {
        simpanTransaksi();
    } else if (action.equals("[Ubah]")) {
        updateTrans();
    } else {
        JOptionPane.showMessageDialog(null, "Aksi tidak dikenali: " + action);
    }

   tambah(); // Reset form / state awal
}

private void updateTrans(){
}
private void IDotomatis() {
    try {
        // Buat prefix dinamis berdasarkan tahun & bulan
        LocalDate today = LocalDate.now();
        String year  = String.format("%02d", today.getYear() % 100); // 25
        String month = String.format("%02d", today.getMonthValue()); // 11

        String prefix = "PJ" + year + month; // contoh: PJ2511
            TjualhDAO dao = new TjualhDAO(conn);
        // Ambil kode terakhir berdasarkan prefix baru
        String lastCode = dao.getLastKode(prefix);
        String newCode;

        if (lastCode != null && lastCode.startsWith(prefix)) {
            // Ambil angka urut setelah prefix
            int number = Integer.parseInt(lastCode.substring(prefix.length()));
            number++; // increment
            newCode = prefix + String.format("%05d", number); // hasil: PJ251100001
        } else {
            // Jika belum ada data untuk bulan tersebut
            newCode = prefix + "00001";
        }

        txtNoFaktur.setText(newCode);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
    }
}

private void loadCurrentItem() {
    if (currentRecordIndex >= 0 && currentRecordIndex < list.size()) {

        Tjualh tjualh = list.get(currentRecordIndex);

        // HEADER
        txtIDJual.setText(String.valueOf(tjualh.getIdJualH()));
        txtNoFaktur.setText(tjualh.getKode());
        dtpTanggal.setDate(tjualh.getTanggal());
        dtpJatuhTempo.setDate(tjualh.getJatuhTempo());
        txtDokter.setText(String.valueOf(tjualh.getIdDokter()));
        

        lblSubtotal.setText(formatAngka(tjualh.getSubTotal()));
        txtDiscTotal.setText(formatAngka(tjualh.getDiskon()));
        txtHarga.setText(formatAngka(tjualh.getTotal()));

        // DETAIL → isi ke JTable
        tampilkanDetailKeTabel(tjualh.getDetails());

        updateRecordLabel();
    } else {
        JOptionPane.showMessageDialog(null, "Indeks catatan tidak valid.");
    }
}

private void tampilkanDetailKeTabel(List<Tjuald> details) {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    for (Tjuald d : details) {
        try {
            ItemFull item = itemDAO.getFullItem(d.getIdItemD());

            String kode = item != null ? item.getKode() : "";
            String nama = item != null ? item.getNama() : "";
            String satuan = item != null ? item.getSatuanBesar() : "";

            model.addRow(new Object[]{
                null, 
                null,// Kolom 0 = tombol X
                kode,            // Kolom 1 = Kode
                nama,            // Kolom 2 = Nama
                satuan,          // Kolom 3 = Satuan
                formatAngka(d.getQty()),      // Kolom 4 = Qty
                formatAngka(d.getHarga()),
                formatAngka(d.getDiskon()),// Kolom 5 = Harga
                //d.getDiskon() != null ? d.getDiskon() : 0,
                formatAngka(d.getTotal())     // Kolom 7 = Total
            });


        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Gagal memuat item detail: " + e.getMessage());
        }
    }
}

public void setDokterData(int idDokter, String namaDokter) {
    // kalau kamu ingin menyimpan idDokter untuk keperluan simpan ke database,
    // buat variabel global di kelas, misalnya:
    this.selectedDokterId = idDokter;

    // hanya tampilkan nama di text field
    txtDokter.setText(namaDokter);
}

public void setCustomerData(int idCustomer, String namaCustomer) {
    // kalau kamu ingin menyimpan idDokter untuk keperluan simpan ke database,
    // buat variabel global di kelas, misalnya:
    this.selectedCustomerId = idCustomer;

    // hanya tampilkan nama di text field
    txtCustomer.setText(namaCustomer);
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



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtIDJual = new javax.swing.JTextField();
        buttonGroup1 = new javax.swing.ButtonGroup();
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
        cmbJenisTras = new javax.swing.JComboBox<>();
        lblJatuhTempo = new javax.swing.JLabel();
        dtpJatuhTempo = new com.toedter.calendar.JDateChooser();
        txtCustomer = new javax.swing.JTextField();
        lblCustomer = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cmbAktif = new javax.swing.JCheckBox();
        btnBayar = new javax.swing.JButton();
        cmbPPN = new javax.swing.JCheckBox();
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
        txtDokter = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        cmbJenis = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        txtIDJual.setText("jTextField3");

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

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Tunai");

        buttonGroup1.add(jRadioButton2);
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

        cmbJenisTras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tunai", "Kredit" }));

        lblJatuhTempo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblJatuhTempo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblJatuhTempo.setText("Jatuh Tempo :");

        txtCustomer.setMinimumSize(new java.awt.Dimension(33, 22));
        txtCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustomerMouseClicked(evt);
            }
        });

        lblCustomer.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCustomer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCustomer.setText("Customer :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(recordLabel)
                .addGap(53, 53, 53)
                .addComponent(cmbJenisTras, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblJatuhTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtpJatuhTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(recordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblJatuhTempo)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(dtpJatuhTempo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbJenisTras)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblCustomer))
                                .addGap(1, 1, 1)))
                        .addContainerGap())))
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

        btnBayar.setText("Bayar");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });

        cmbPPN.setText("PPN 11%");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(cmbAktif)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbPPN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit)
                .addGap(24, 24, 24))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cmbAktif)
                .addComponent(cmbPPN))
            .addGroup(jPanel4Layout.createSequentialGroup()
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

        lblSubtotal.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
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
        txtDiscPersen.setPreferredSize(new java.awt.Dimension(32, 22));
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDiscPersen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDiscTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHarga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtDiscTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtDiscPersen, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtKodeItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jLabel11.setText("Dokter :");

        txtDokter.setMinimumSize(new java.awt.Dimension(33, 22));
        txtDokter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDokterMouseClicked(evt);
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
                .addComponent(txtDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(dtpTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbJenis)
                    .addComponent(txtDokter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNoFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
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
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 508, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
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

    private void txtDokterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDokterMouseClicked
             // TODO add your handling code here:
         if (evt.getClickCount() == 2) {
        // Membuka form lain
        BrowseDokter dialog = new BrowseDokter(this, true, conn);
        dialog.setVisible(true);}   // TODO add your handling code here:
    }//GEN-LAST:event_txtDokterMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        awal();//DO add your handling code here:
        clearInputFields();
        
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerMouseClicked
        if (evt.getClickCount() == 2) {
        // Membuka form lain
            BrowseCustomer dialog = new BrowseCustomer(this, true, conn);
        dialog.setVisible(true);}     // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerMouseClicked

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

    private void txtBayarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBayarKeyTyped

    private void jdBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jdBayarKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jdBayarKeyReleased

    private void txtBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyReleased
        prosesBayar();        // TODO add your handling code here:
    }//GEN-LAST:event_txtBayarKeyReleased

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
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
    simpanTransaksi();

                    // TODO add your handling code here:
    }//GEN-LAST:event_btnSimpanActionPerformed

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
    private javax.swing.ButtonGroup buttonGroup1;
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
    private javax.swing.JLabel lblCustomer;
    private javax.swing.JLabel lblJatuhTempo;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtDiscPersen;
    private javax.swing.JTextField txtDiscTotal;
    private javax.swing.JTextField txtDokter;
    private javax.swing.JLabel txtHarga;
    private javax.swing.JTextField txtIDJual;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtKodeItem;
    private javax.swing.JLabel txtNama;
    private javax.swing.JTextField txtNoFaktur;
    // End of variables declaration//GEN-END:variables
}
