/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.Master;
import dao.Koneksi;
import dao.PeriodeDAO;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import model.*;
/**
 *
 * @author Admin
 */
public class frmPeriode extends javax.swing.JFrame {
    private PeriodeDAO periodeDAO;
    private Connection conn;
    private Statement stat;
    private ResultSet rs;
    private String sql;
    private User user;
    private List<Periode> periodeList;
    private int currentRecordIndex;
    private int totalInputs;

    /**
     * Creates new form ParentTrans
     */
    public frmPeriode() {
        initComponents();
        periodeDAO = new PeriodeDAO(conn);
        jToolBar1.setFloatable(false);
        jToolBar2.setFloatable(false);
        setLocationRelativeTo(null);
        setResizable(false); 
        initializeDatabase();
        initialize();
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
    // Enable navigation buttons
    private void navAktif() {
        btnAwal.setEnabled(true);
        btnPrevious.setEnabled(true);
        btnNext.setEnabled(true);
        btnAkhir.setEnabled(true);
    }

    // Disable navigation buttons
    private void navNonAktif() {
        btnAwal.setEnabled(false);
        btnPrevious.setEnabled(false);
        btnNext.setEnabled(false);
        btnAkhir.setEnabled(false);
    }

    // Initialize form to the default state
    private void initialize() {
        jLabel1.setText("[Browse]");
        btnSimpan.setEnabled(false);
        btnCancel.setEnabled(false);
        btnExit.setEnabled(false);
        jtKode.setEnabled(false);
        JTanggalAwal.setEnabled(false);
        JTanggalAkhir.setEnabled(false);
        jComboBox1.setEnabled(false);
        jTextPane1.setEnabled(false);
        cmbAktif.setEnabled(false);
        btnBrowse.setEnabled(true);
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
        btnTambah.setEnabled(true);
        periodeDAO = new PeriodeDAO(conn);
        periodeList = periodeDAO.getAllPeriode(); // Ambil semua periode
        totalInputs = periodeDAO.countPeriode();

    if (totalInputs > 0) {
        currentRecordIndex = totalInputs - 1; // Set ke indeks terakhir
        loadCurrentPeriode(); // Muat periode terakhir
        updateRecordLabel(); // Perbarui label dengan informasi record
    }

        navAktif();
    }

    // Set the form to "Tambah" mode
    private void tambah() {
        jLabel1.setText("[Tambah]");
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(true);
        jtKode.setEnabled(true);
        btnBrowse.setEnabled(false);
        JTanggalAwal.setEnabled(true);
        kosong();
        JTanggalAkhir.setEnabled(true);
        jComboBox1.setEnabled(true);
        jTextPane1.setEnabled(true);
        cmbAktif.setEnabled(true);
        cmbAktif.setSelected(true);
        navNonAktif();
    }
    private void kosong(){
        jtKode.setText("");
        String lastTanggalAkhir = periodeDAO.getLastTanggalAkhir();
        if (lastTanggalAkhir != null) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date endDate = dateFormat.parse(lastTanggalAkhir);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            
            // Set tanggal awal dan akhir untuk bulan berikutnya
            calendar.add(Calendar.MONTH, 1); // Pindah ke bulan berikutnya
            calendar.set(Calendar.DAY_OF_MONTH, 1); // Tanggal awal 1 bulan berikutnya
            Date newStartDate = calendar.getTime(); // Tanggal awal

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // Tanggal akhir bulan
            Date newEndDate = calendar.getTime(); // Tanggal akhir
            
            // Set ke JDateChooser
            JTanggalAwal.setDate(newStartDate);
            JTanggalAkhir.setDate(newEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Kesalahan dalam parsing tanggal: " + e.getMessage());
        }
    }
        jComboBox1.setSelectedItem("Read Write");
        jTextPane1.setText("");
    }
    // Set the form to "Ubah" mode
    private void ubah() {
        jLabel1.setText("[Ubah]");
        btnTambah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);
        btnCancel.setEnabled(true);
        btnExit.setEnabled(true);
        cmbAktif.setEnabled(true);
         JTanggalAkhir.setEnabled(true);
         JTanggalAwal.setEnabled(true);
        jComboBox1.setEnabled(true);
        jTextPane1.setEnabled(true);
        navAktif();
    }
    
private void loadCurrentPeriode() {
    if (currentRecordIndex >= 0 && currentRecordIndex < periodeList.size()) {
        Periode periode = periodeList.get(currentRecordIndex);
        
        // Mengatur IDPeriode sebagai string
        jTIDPeriode.setText(String.valueOf(periode.getIdPeriode())); 
        
        // Tampilkan nilai-nilai ini di komponen UI
        jtKode.setText(periode.getKode());
        
        // Pastikan tanggal diatur dengan benar (gunakan java.util.Date)
        try {
            JTanggalAwal.setDate(java.sql.Date.valueOf(periode.getTanggalAwal()));
            JTanggalAkhir.setDate(java.sql.Date.valueOf(periode.getTanggalAkhir()));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Tanggal tidak valid: " + e.getMessage());
        }
        
        jTextPane1.setText(periode.getKeterangan());
        jComboBox1.setSelectedItem(periode.getStatus());
        cmbAktif.setSelected(periode.isAktif());
        
        updateRecordLabel();
    } else {
        JOptionPane.showMessageDialog(null, "Indeks catatan tidak valid.");
    }
}

private void updateRecordLabel() {
    recordLabel.setText("Record: " + (currentRecordIndex + 1) + " of " + totalInputs);
}
private void next() {
    if (currentRecordIndex < totalInputs - 1) { // Pastikan tidak melebihi jumlah total input
        currentRecordIndex++;
        loadCurrentPeriode();
    } else {
        JOptionPane.showMessageDialog(null, "Anda sudah berada pada record terakhir.");
    }
}
private void previous() {
    if (currentRecordIndex > 0) { // Pastikan tidak kurang dari record pertama
        currentRecordIndex--;
        loadCurrentPeriode();
    } else {
        JOptionPane.showMessageDialog(null, "Anda sudah berada pada record pertama.");
    }
}
private void data_awal() {
    currentRecordIndex = 0; // Data pertama
    loadCurrentPeriode();
}
private void data_terakhir() {
    currentRecordIndex = totalInputs - 1; // Data terakhir
    loadCurrentPeriode();
}
private void ubahdata() {
    try {
        int IDPeriode = Integer.parseInt(jTIDPeriode.getText());
        String kode = jtKode.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Pastikan tanggal tidak null
        if (JTanggalAwal.getDate() == null || JTanggalAkhir.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Tanggal awal dan tanggal akhir harus diisi.");
            return;
        }

        String tanggalAwalStr = dateFormat.format(JTanggalAwal.getDate());
        String tanggalAkhirStr = dateFormat.format(JTanggalAkhir.getDate());
        String keterangan = jTextPane1.getText();
        String status = jComboBox1.getSelectedItem().toString();
        boolean aktif = cmbAktif.isSelected();

        // Buat objek Periode
        Periode periode = new Periode(kode, tanggalAwalStr, tanggalAkhirStr, keterangan, status, aktif, IDPeriode); // Sertakan IDPeriode

        PeriodeDAO periodeDAO = new PeriodeDAO(conn); // Pastikan conn adalah koneksi database
        boolean isSuccess = periodeDAO.updatePeriode(IDPeriode, periode);

        if (isSuccess) {
            JOptionPane.showMessageDialog(null, "Data berhasil diubah.");
            // data_terakhir(); // Reload the data after update
        } else {
            JOptionPane.showMessageDialog(null, "Gagal mengubah data.");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "ID Periode tidak valid: " + e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
    }
}

private void simpan() {
    // Pastikan IDotomatis() diimplementasikan dengan benar
    IDotomatis(); // Mengatur kode dengan benar

    String kode = jtKode.getText();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Pastikan tanggal tidak null
    if (JTanggalAwal.getDate() == null || JTanggalAkhir.getDate() == null) {
        JOptionPane.showMessageDialog(null, "Tanggal awal dan tanggal akhir harus diisi.");
        return;
    }

    String tanggalAwalStr = dateFormat.format(JTanggalAwal.getDate());
    String tanggalAkhirStr = dateFormat.format(JTanggalAkhir.getDate());
    String keterangan = jTextPane1.getText();
    String status = jComboBox1.getSelectedItem().toString();
    boolean aktif = cmbAktif.isSelected();

    // Buat objek Periode
    Periode periode = new Periode(kode, tanggalAwalStr, tanggalAkhirStr, keterangan, status, aktif, 0); // Misal ID 0 untuk baru
    PeriodeDAO periodeDAO = new PeriodeDAO(conn); // Asumsikan conn adalah koneksi database Anda
    boolean isSuccess = periodeDAO.insertPeriode(periode); // Kirim objek Periode

    if (isSuccess) {
        JOptionPane.showMessageDialog(null, "Data berhasil disimpan ke database.");
        // setupAccessControls();
    } else {
        JOptionPane.showMessageDialog(null, "Gagal menyimpan data ke database.");
    }
}

private void IDotomatis() {
    try {
        // Dapatkan kode terakhir dari PeriodeDAO
        String lastCode = periodeDAO.getLastKode(); // Anda perlu membuat metode ini di PeriodeDAO
        
        // Mendapatkan bulan dan tahun dari JDateChooser
        Date date = JTanggalAwal.getDate();
        SimpleDateFormat dateFormatOutput = new SimpleDateFormat("MMyyyy");
        String bulanTahun = dateFormatOutput.format(date);
        
        if (lastCode != null && lastCode.startsWith("P" + bulanTahun)) {
            JOptionPane.showMessageDialog(null, "Kode untuk bulan dan tahun ini sudah ada.");
            return;
        }
        
        jtKode.setText("P" + bulanTahun);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
}


private void showBrowsePeriodeDialog() {
    // Create the dialog with 'this' as the parent and modal set to true
    BrowsePeriodeDialog dialog = new BrowsePeriodeDialog(this, true, periodeDAO);
    
    // Make the dialog visible and wait for user interaction
    dialog.setVisible(true);

    // Get the JTable from the dialog
    JTable table = dialog.getTableBarang();
    
    // Check if a row is selected
    int selectedRow = table.getSelectedRow();
    if (selectedRow != -1) {
        // Extract the values from the selected row and handle them based on their type
        
        // Assuming ID Periode is an Integer in the 0th column
        Object idPeriodeValue = table.getValueAt(selectedRow, 0);
        String idPeriode = idPeriodeValue != null ? idPeriodeValue.toString() : ""; // Convert Integer to String if not null
        
        // Assuming Kode is a String in the 1st column
        Object kodeValue = table.getValueAt(selectedRow, 1);
        String kode = kodeValue != null ? kodeValue.toString() : ""; // Handle null
        
        // Assuming Tanggal Awal and Tanggal Akhir are Date objects in the 2nd and 3rd columns
        Object tanggalAwalValue = table.getValueAt(selectedRow, 2);
        Object tanggalAkhirValue = table.getValueAt(selectedRow, 3);
        
        Date tanggalAwal = null;
        Date tanggalAkhir = null;
        try {
            if (tanggalAwalValue instanceof String) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                tanggalAwal = dateFormat.parse((String) tanggalAwalValue);
            } else if (tanggalAwalValue instanceof Date) {
                tanggalAwal = (Date) tanggalAwalValue;
            }
            
            if (tanggalAkhirValue instanceof String) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                tanggalAkhir = dateFormat.parse((String) tanggalAkhirValue);
            } else if (tanggalAkhirValue instanceof Date) {
                tanggalAkhir = (Date) tanggalAkhirValue;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Error parsing dates: " + e.getMessage());
            e.printStackTrace();
        }

        // Assuming Keterangan is a String in the 4th column
        Object keteranganValue = table.getValueAt(selectedRow, 4);
        String keterangan = keteranganValue != null ? keteranganValue.toString() : ""; // Handle null

        // Assuming Status is a String in the 5th column
        Object statusValue = table.getValueAt(selectedRow, 5);
        String status = statusValue != null ? statusValue.toString() : "";

        // Assuming Aktif is a Boolean in the 6th column (or could be a String like "1"/"0")
        Object aktifValue = table.getValueAt(selectedRow, 6);
        boolean aktif = false;
        if (aktifValue instanceof Boolean) {
            aktif = (Boolean) aktifValue;
        } else if (aktifValue instanceof String) {
            aktif = "Ya".equals(aktifValue); // Convert "1" to true, assuming "0" is false
        }

        // Set values in the form
        jTIDPeriode.setText(idPeriode);
        jtKode.setText(kode);
        JTanggalAwal.setDate(tanggalAwal);
        JTanggalAkhir.setDate(tanggalAkhir);
        jTextPane1.setText(keterangan);
        
        // Set status in the jComboBox1
        jComboBox1.setSelectedItem(status); // Set the status in the dropdown

        // Set checkbox aktif
        cmbAktif.setSelected(aktif); // Set checkbox based on active status

        // Dispose of the dialog after selection
        dialog.dispose();
    } else {
        // No row was selected
        JOptionPane.showMessageDialog(this, "Tidak ada periode yang dipilih.");
        dialog.dispose();
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

        jTIDPeriode = new javax.swing.JTextField();
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
        JTanggalAwal = new com.toedter.calendar.JDateChooser();
        JTanggalAkhir = new com.toedter.calendar.JDateChooser();
        lblKode = new javax.swing.JLabel();
        jtKode = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        lblKode1 = new javax.swing.JLabel();
        lblKode2 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        lblStatus1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        jTIDPeriode.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Master Periode");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 102, 255));

        jPanel3.setBackground(new java.awt.Color(94, 169, 245));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setMinimumSize(new java.awt.Dimension(100, 25));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 25));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Mode Aktif ");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("[Browse]");
        jLabel1.setToolTipText("");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                .addContainerGap())
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

        cmbAktif.setBackground(new java.awt.Color(204, 255, 255));
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

        JTanggalAwal.setBackground(new java.awt.Color(255, 255, 255));
        JTanggalAwal.setDateFormatString("dd/MM/yyyy");
        JTanggalAwal.setMaxSelectableDate(new java.util.Date(253370743280000L));
        JTanggalAwal.setMinSelectableDate(new java.util.Date(-62135791120000L));
        JTanggalAwal.setMinimumSize(new java.awt.Dimension(85, 22));
        JTanggalAwal.setPreferredSize(new java.awt.Dimension(85, 22));

        JTanggalAkhir.setDateFormatString("dd/MM/yyyy");

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
        btnBrowse.setMaximumSize(new java.awt.Dimension(81, 22));
        btnBrowse.setMinimumSize(new java.awt.Dimension(81, 22));
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        lblKode1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode1.setText("Tanggal :");
        lblKode1.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode1.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode1.setPreferredSize(new java.awt.Dimension(33, 22));

        lblKode2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKode2.setText("s/d");
        lblKode2.setMaximumSize(new java.awt.Dimension(33, 22));
        lblKode2.setMinimumSize(new java.awt.Dimension(33, 22));
        lblKode2.setPreferredSize(new java.awt.Dimension(33, 22));

        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setText("Status :");
        lblStatus.setMaximumSize(new java.awt.Dimension(33, 22));
        lblStatus.setMinimumSize(new java.awt.Dimension(33, 22));
        lblStatus.setPreferredSize(new java.awt.Dimension(33, 22));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Read Write", "Read Only" }));

        lblStatus1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus1.setText("Status :");
        lblStatus1.setMaximumSize(new java.awt.Dimension(33, 22));
        lblStatus1.setMinimumSize(new java.awt.Dimension(33, 22));
        lblStatus1.setPreferredSize(new java.awt.Dimension(33, 22));

        jScrollPane1.setViewportView(jTextPane1);

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
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblKode1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JTanggalAwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblKode, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblKode2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JTanggalAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(68, 68, 68))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKode, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(jtKode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblKode1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(lblKode2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(JTanggalAkhir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JTanggalAwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 69, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        showBrowsePeriodeDialog();
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        String action = jLabel1.getText(); // Mendapatkan teks dari JLabel
         if (action.equals("[Tambah]")) {
        simpan(); // Jalankan metode simpan jika teks adalah "Tambah"
        } else if (action.equals("[Ubah]")) {
             ubahdata(); // Jalankan metode ubah jika teks adalah "Ubah"
       } else {
        JOptionPane.showMessageDialog(null, "Aksi tidak dikenali: " + action);
    }
         initialize();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        previous();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnAwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAwalActionPerformed
        // TODO add your handling code here:
        data_awal();
    }//GEN-LAST:event_btnAwalActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnAkhirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAkhirActionPerformed
        // TODO add your handling code here:
        data_terakhir();
    }//GEN-LAST:event_btnAkhirActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser JTanggalAkhir;
    private com.toedter.calendar.JDateChooser JTanggalAwal;
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
    private javax.swing.JComboBox<String> jComboBox1;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTIDPeriode;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField jtKode;
    private javax.swing.JLabel lblKode;
    private javax.swing.JLabel lblKode1;
    private javax.swing.JLabel lblKode2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel recordLabel;
    // End of variables declaration//GEN-END:variables
}
