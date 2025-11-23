/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.Transaksi;
import dao.Koneksi;
import dao.TStokDAO;
import ui.Master.*;
import dao.cell.ButtonEditor;
import dao.cell.ButtonRenderer;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ui.Master.BrowseAll.BrowseCustomer;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.Stok;
import model.User;
import ui.Master.BrowseAll.BrowseItem;

/**
 *
 * @author Admin
 */
public class frmTransStok extends javax.swing.JFrame {
    private Connection conn;
     private Statement stat;
    private ResultSet rs;
    private String sql;
    private Integer selectedCustomerId;
      DefaultTableModel model;
    JTable tblDetail;
    private List<Stok> list;
    private int currentRecordIndex = 0;
    private int totalInputs = 0;
    private TStokDAO tStokDAO;
 
    /**
     * Creates new form ParentTrans
     */
    public frmTransStok() {
        initComponents();
    initializeDatabase();   // ← WAJIB DULU BIAR conn TIDAK NULL
    tStokDAO = new TStokDAO(conn); 
    
            initTable();
            setupSatuanColumn();
            jToolBar1.setFloatable(false);
            jToolBar2.setFloatable(false);
            btnSimpan.addActionListener(evt -> simpan());
            FormShow();
            btnALL();
             awal();
    }
    
    private void FormShow() {
    loadDataFromDatabase();
    loadCurrentItem();
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
        btnSimpan.setEnabled(true);
        btnBrows.setEnabled(true);
        btnCancel.setEnabled(false);
        btnExit.setEnabled(false);
        navaktif();
        FormShow();
    }
    private void tambah(){
        clearInputFields();
        IDotomatis();
        jLabel1.setText("[Tambah]");
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnBrows.setEnabled(false);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(true);
        navnonaktif();
    ((DefaultTableModel) jTable1.getModel()).setRowCount(0);
        dtpTanggal.setDate(new java.util.Date()); // set tanggal hari ini
        dtpTanggal.setDateFormatString("dd/MM/yyyy"); // ubah format tampilan 
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
    

    
    private void initTable() {
    DefaultTableModel model = new DefaultTableModel(
        new Object[]{"X", "ID", "Kode", "Nama", "Satuan", "Stok", "Harga Jual", "Harga Beli", "SatuanList", "IDItem"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0 || column == 4 || column == 5 || column == 6 || column == 7;
        }
        
    };
    jTable1.setModel(model);
    
     DefaultTableCellRenderer paddingRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (c instanceof JLabel) {
                // padding kiri/kanan/atas/bawah
                ((JLabel) c).setBorder(
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                );
            }

            return c;
        }
    };

    // Terapkan padding ke semua kolom kecuali tombol X
    for (int i = 1; i < jTable1.getColumnCount(); i++) {
        jTable1.getColumnModel().getColumn(i).setCellRenderer(paddingRenderer);
    }

    // === SUPAYA ROW LEBIH TINGGI ===
    jTable1.setRowHeight(28);

    // === SET AUTO RESIZE (BIKIN LEBAR RAPI) ===
    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

     //sembunyikan kolom index 8
    jTable1.getColumnModel().getColumn(8).setMinWidth(0);
    jTable1.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(8).setWidth(0);
    
    jTable1.getColumnModel().getColumn(9).setMinWidth(0);
    jTable1.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(9).setWidth(0);


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
    jTable1.getColumn("Kode").setPreferredWidth(200);
    jTable1.getColumn("Nama").setPreferredWidth(400);
    jTable1.getColumn("Satuan").setPreferredWidth(190);
    jTable1.getColumn("Stok").setPreferredWidth(50);
    jTable1.getColumn("Harga Beli").setPreferredWidth(162);
    jTable1.getColumn("Harga Jual").setPreferredWidth(162);



    // Warna baris bergantian (striped rows)
   // Warna baris bergantian (striped rows)
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
    
    javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        jTable1.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
}
    
    private void clearInputFields() {
   // txtNoFaktur.setText("");
    txtIDStok.setText("");
    txtKode.setText("");
}
    
    private void simpan() {
        String action = jLabel1.getText();
    if (action.equals("[Tambah]")) {
        simpanDataStok();
    } else if (action.equals("[Ubah]")) {
        updateTrans();
    } else {
        JOptionPane.showMessageDialog(null, "Aksi tidak dikenali: " + action);
    }
  // clearInputFields();
   tambah(); // Reset form / state awal
}
    
    private void updateTrans() {
    try {
        Connection conn = Koneksi.getConnection();
        TStokDAO dao = new TStokDAO(conn);

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        java.util.List<Stok> list = new java.util.ArrayList<>();

        String kodeForm = txtKode.getText().trim();
        java.util.Date tanggalForm = dtpTanggal.getDate();

        for (int i = 0; i < model.getRowCount(); i++) {
            Stok t = new Stok();

            // Ambil IDStok dari kolom tersembunyi di tabel
            t.setId(Integer.parseInt(model.getValueAt(i, 1).toString())); // IDStok
           // t.setIdItem(Integer.parseInt(model.getValueAt(i, 2).toString())); 
            t.setIdItem(Integer.parseInt(model.getValueAt(i, 9).toString()));// IDItem
            t.setKode(txtKode.getText().trim());
            t.setTanggal(tanggalForm);
            t.setNama(model.getValueAt(i, 3).toString());
            t.setSatuan(model.getValueAt(i, 4).toString());
            t.setStok(parseAngka(model.getValueAt(i, 5).toString()));
            t.setHargaJual(parseAngka(model.getValueAt(i, 6).toString()));
            t.setHargaBeli(parseAngka(model.getValueAt(i, 7).toString()));
            t.setAktif(cmbAktif.isSelected());

            list.add(t);
        }

        dao.updateBatchWithJurnal(list, "Admin");

        JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal update: " + e.getMessage());
    }
}


    
    private void IDotomatis() {
    try {
        LocalDate today = LocalDate.now();

        String year  = String.format("%02d", today.getYear() % 100); 
        String month = String.format("%02d", today.getMonthValue());

        String prefix = "ST" + year + month; // contoh: ST2511

        TStokDAO dao = new TStokDAO(conn);

        String lastCode = dao.getLastKode(prefix);
        String newCode;

        if (lastCode != null && lastCode.startsWith(prefix)) {
            int number = Integer.parseInt(lastCode.substring(prefix.length()));
            number++;
            newCode = prefix + String.format("%05d", number); 
        } else {
            newCode = prefix + "00001";
        }

        txtKode.setText(newCode);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, 
            "Terjadi kesalahan generate kode: " + e.getMessage());
    }
}

   
    public void setItemData(
    int idItem,
    String kode,
    String nama,
    String satuanKecil,
    String satuanBesar,
    double hargaJual,
    double hargaBeli
) {
    
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

    // Opsional: Cek duplikasi, seperti yang disarankan sebelumnya
    for (int i = 0; i < model.getRowCount(); i++) {
        String existingKode = model.getValueAt(i, 2).toString();
        if (existingKode.equals(kode)) {
//            JOptionPane.showMessageDialog(this, "Item dengan kode " + kode + " sudah ada di daftar.");
            return;
        }
    }
    
    java.util.List<String> satuanList = new java.util.ArrayList<>();
    
    // Tambahkan satuan kecil
    if (satuanKecil != null && !satuanKecil.isEmpty())
        satuanList.add(satuanKecil);

    // Tambahkan satuan besar, HANYA jika berbeda dengan satuan kecil
    if (satuanBesar != null && !satuanBesar.isEmpty() && 
        !satuanBesar.equalsIgnoreCase(satuanKecil))
        satuanList.add(satuanBesar);

    // Pastikan list tidak kosong
    if (satuanList.isEmpty()) {
        satuanList.add("N/A");
    }

    // 2. Masukkan ke tabel
    model.addRow(new Object[]{
    "X",
        
    idItem,
    kode,
    nama,
    satuanKecil,  // tampil di kolom Satuan
    0,
    hargaJual,
    hargaBeli,  
    satuanList   // Index 8 (SatuanList)// simpan list satuan (besar + kecil)
    });
 // Atur dropdown satuan
}

    private void setupSatuanColumn() {
    TableColumn col = jTable1.getColumnModel().getColumn(4); // Kolom Satuan (Index 4)

    // Gunakan DefaultCellEditor dengan JComboBox kosong di awal
    col.setCellEditor(new DefaultCellEditor(new JComboBox<String>()) {
        
        // JComboBox ini dibuat SEKALI saat CellEditor diinisialisasi
        private JComboBox<String> combo = (JComboBox<String>) getComponent(); 

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
            
            // 1. Ambil list satuan dari kolom tersembunyi (Index 8)
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) table.getValueAt(row, 8); 
            
            // 2. Kosongkan item yang lama
            combo.removeAllItems();

            // 3. Isi ComboBox dengan item yang relevan untuk baris ini
            if (list != null) {
                for (String s : list) {
                    combo.addItem(s);
                }
            }

            // 4. Set nilai yang sedang diedit (nilai saat ini di tabel)
            combo.setSelectedItem(value);

            // 5. Kembalikan komponen editor yang sama
            return combo;
        }

        // ✅ TAMBAH: PENTING UNTUK MEMASTIKAN NILAI TERSIMPAN
        @Override
        public Object getCellEditorValue() {
            // Nilai yang dipilih dari combo box DIKEMBALIKAN dan DISIMPAN ke model tabel
            return combo.getSelectedItem(); 
        }
    });
}
    
   private void simpanDataStok() {
    try {
        Connection conn = Koneksi.getConnection();
        TStokDAO dao = new TStokDAO(conn);

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        java.util.List<Stok> list = new java.util.ArrayList<>();

        String kodeForm = txtKode.getText().trim();
        java.util.Date tanggalForm = dtpTanggal.getDate();

        for (int i = 0; i < model.getRowCount(); i++) {
            Stok t = new Stok();
            t.setKode(kodeForm);
            t.setTanggal(tanggalForm);
            t.setIdItem(Integer.parseInt(model.getValueAt(i, 1).toString()));
            t.setNama(model.getValueAt(i, 3).toString());
            t.setSatuan(model.getValueAt(i, 4).toString());
            t.setStok(Double.parseDouble(model.getValueAt(i, 5).toString()));
            t.setHargaJual(Double.parseDouble(model.getValueAt(i, 6).toString()));
            t.setHargaBeli(Double.parseDouble(model.getValueAt(i, 7).toString()));
            t.setAktif(cmbAktif.isSelected());
            list.add(t);
        }

        // insert ke tstok & tjurnalitem
        dao.insertBatchWithJurnal(list, "Admin"); // bisa ganti "System" dengan user login

        JOptionPane.showMessageDialog(this, "Data stok dan jurnal berhasil disimpan!");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal simpan: " + e.getMessage());
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
   
   private void loadDataFromDatabase() {
    try {
        list = tStokDAO.getAll(); 
        totalInputs = list.size();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Gagal mengambil data stok: " + e.getMessage());
        return;
    }

    if (list == null || list.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Data stok belum ada di database.");
        return;
    }

    currentRecordIndex = 0;
    loadCurrentItem();
}

   private void loadCurrentItem() {
    if (currentRecordIndex < 0 || currentRecordIndex >= list.size()) return;

    Stok s = list.get(currentRecordIndex);

    txtKode.setText(s.getKode());
    dtpTanggal.setDate(s.getTanggal());

    // filter data dengan kode yang sama
    List<Stok> sameKodeList = list.stream()
        .filter(item -> item.getKode().equals(s.getKode()))
        .toList();

    tampilkanKeTabel(sameKodeList);

    updateRecordLabel();
}

   
   private void tampilkanKeTabel(List<Stok> data) {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    for (Stok s : data) {
        model.addRow(new Object[]{
            "X",
            s.getId(),
            s.getKodeItem(),
            s.getNama(),
            s.getSatuan(),
            formatAngka(s.getStok()),
            formatAngka(s.getHargaJual()),
            formatAngka(s.getHargaBeli()),
            s.getSatuan(),
            s.getIdItem()
        });
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



    




//    public void setCustomerData(int idCustomer, String kodeCustomer, String namaCustomer) {
//    // kalau kamu ingin menyimpan idDokter untuk keperluan simpan ke database,
//    // buat variabel global di kelas, misalnya:
//    this.selectedCustomerId = idCustomer;
//    jtKodeCust.setText(kodeCustomer);
//    // hanya tampilkan nama di text field
//    txtCustomer.setText(namaCustomer);
//}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtIDStok = new javax.swing.JTextField();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lblKode = new javax.swing.JLabel();
        jtKode = new javax.swing.JTextField();
        dtpTanggal = new com.toedter.calendar.JDateChooser();
        lblKode3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cmbAktif = new javax.swing.JCheckBox();
        btnBrowsItem = new javax.swing.JButton();
        lblKode1 = new javax.swing.JLabel();
        txtKode = new javax.swing.JTextField();
        btnBrows = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        txtIDStok.setText("jTextField3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1265, 537));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(0, 255, 204));
        jPanel1.setToolTipText("");

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

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        lblKode.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode.setText("Kode Barang :");
        lblKode.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode.setPreferredSize(new java.awt.Dimension(33, 22));

        jtKode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jtKode.setMaximumSize(new java.awt.Dimension(64, 26));
        jtKode.setMinimumSize(new java.awt.Dimension(64, 26));
        jtKode.setPreferredSize(new java.awt.Dimension(64, 26));
        jtKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtKodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtKodeFocusLost(evt);
            }
        });

        dtpTanggal.setDateFormatString("dd/MM/yyyy");
        dtpTanggal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dtpTanggal.setMinSelectableDate(new java.util.Date(-62135791098000L));
        dtpTanggal.setPreferredSize(new java.awt.Dimension(85, 26));

        lblKode3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKode3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode3.setText("Tanggal :");
        lblKode3.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode3.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode3.setPreferredSize(new java.awt.Dimension(33, 22));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(0, 255, 204));

        btnSimpan.setText("Simpan");

        btnExit.setText("Exit Ctrl + X");

        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(92, 23));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        cmbAktif.setBackground(new java.awt.Color(0, 255, 204));
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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 17, Short.MAX_VALUE))
        );

        btnBrowsItem.setText("jButton1");
        btnBrowsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowsItemActionPerformed(evt);
            }
        });

        lblKode1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKode1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode1.setText("Kode :");
        lblKode1.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode1.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode1.setPreferredSize(new java.awt.Dimension(33, 22));

        txtKode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtKode.setMaximumSize(new java.awt.Dimension(64, 26));
        txtKode.setMinimumSize(new java.awt.Dimension(64, 26));
        txtKode.setPreferredSize(new java.awt.Dimension(64, 26));
        txtKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKodeFocusLost(evt);
            }
        });

        btnBrows.setText("jButton1");
        btnBrows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(lblKode, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrowsItem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblKode1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrows, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(lblKode3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtpTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 566, Short.MAX_VALUE)))
                .addGap(31, 31, 31))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtpTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblKode3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(62, 62, 62))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBrows, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblKode1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBrowsItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblKode, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
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
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        initializeDatabase();
        setLocationRelativeTo(null);
    }//GEN-LAST:event_formWindowOpened

    private void btnBrowsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowsItemActionPerformed
        // TODO add your handling code here:
        BrowseItem dialog = new BrowseItem(this, true, conn);
        dialog.setVisible(true);
    }//GEN-LAST:event_btnBrowsItemActionPerformed

    private void txtKodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKodeFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeFocusGained

    private void txtKodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKodeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeFocusLost

    private void btnBrowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrowsActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        awal();//DO add your handling code here:      // TODO add your handling code here:
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
    private javax.swing.JButton btnBrows;
    private javax.swing.JButton btnBrowsItem;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JCheckBox cmbAktif;
    private com.toedter.calendar.JDateChooser dtpTanggal;
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
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField jtKode;
    private javax.swing.JLabel lblKode;
    private javax.swing.JLabel lblKode1;
    private javax.swing.JLabel lblKode3;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JTextField txtIDStok;
    private javax.swing.JTextField txtKode;
    // End of variables declaration//GEN-END:variables
}
