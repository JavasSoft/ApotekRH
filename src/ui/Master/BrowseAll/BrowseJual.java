/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.Master.BrowseAll;

import dao.TjualhDAO;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import ui.Master.frmMstItem;
import ui.Transaksi.frmReturPenjualan;
import ui.Transaksi.frmTransPenjualanTunai;
//import ui.Transaksi.frmTransPo;

/**
 *
 * @author Admin
 */
public class BrowseJual extends javax.swing.JDialog {
    private TjualhDAO tjualhDAO;
    private DefaultTableModel tableModel;
    private List<Tjualh> tjualList;
    private List<Tjualh> currentJualList = new ArrayList<>(); // data yang sedang ditampilkan
    private int displayLimit = 10;
//private List<Item> currentItemList = new ArrayList<>(); // Data di halaman aktif
//private int displayLimit = 10;   // Banyak data per halaman
private int currentPage = 1;     // Halaman aktif
private int totalPages = 1;      // Total halaman


    /**
     * Creates new form BrowseBarangDialog
     */
    private Integer filterIdCust = null;

    public BrowseJual(java.awt.Frame parent, boolean modal, Connection conn, Integer idCust) {
        super(parent, modal);
        initComponents();
        this.tjualhDAO = new TjualhDAO(conn);
        this.filterIdCust = idCust;   // simpan IDCust jika ada

        setupTable();
        setupTableClickListener();
        setLocationRelativeTo(null);

    loadData();
}


  private void setupTable() {
    String[] columnNames = {
        "No", "IDJualH", "No Faktur", "Tanggal", "Jenis Bayar", "Total", "Nominal",
        "Status"
    };

    tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Semua kolom tidak bisa diedit langsung
        }
    };
    jTable1.setModel(tableModel);

    // Lebar kolom penting
    int[] preferredWidths = {30, 10, 200, 80, 50, 100, 80, 60}; 
    for (int i = 0; i < preferredWidths.length; i++) {
        jTable1.getColumnModel().getColumn(i).setPreferredWidth(preferredWidths[i]);
    }

    // Sembunyikan kolom IDItem (kolom 1)
    jTable1.getColumnModel().getColumn(1).setMinWidth(0);
    jTable1.getColumnModel().getColumn(1).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(1).setWidth(0);

    // Rata tengah kolom nomor
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
    rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
    
    jTable1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    jTable1.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    jTable1.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
    jTable1.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
}


private void setupTableClickListener() {
    jTable1.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Deteksi klik 2 kali (double click)
            if (e.getClickCount() == 2 && jTable1.getSelectedRow() != -1) {
                selectRowAndClose(); // Panggil fungsi saat double-click
            }
        }
    });
}


private void loadData() {
    try {
        if (filterIdCust == null) {
            tjualList = tjualhDAO.getAll();                 // Semua transaksi
        } else {
            tjualList = tjualhDAO.getByCustomer(filterIdCust);  // Filter berdasarkan customer
        }
    } catch (SQLException ex) {
    JOptionPane.showMessageDialog(this, 
        "Gagal load data: " + ex.getMessage(),
        "Error", JOptionPane.ERROR_MESSAGE);
}

    

    updateTable(tjualList);
    applyDisplayLimit();
}

private void applyDisplayLimit() {

    // Hitung total halaman
    totalPages = (int) Math.ceil((double) tjualList.size() / displayLimit);

    // Pastikan halaman aktif tidak melebihi total halaman
    if (currentPage > totalPages) {
        currentPage = totalPages;
    }

    updatePageData();
}

private void updatePageData() {
    int startIndex = (currentPage - 1) * displayLimit;
    int endIndex = Math.min(startIndex + displayLimit, tjualList.size());

    currentJualList = tjualList.subList(startIndex, endIndex);

    // Update tampilan tabel
    updateTable(currentJualList);

    // Update label halaman
    lblPage.setText(currentPage + "/" + totalPages);
}

private void updateTable(List<Tjualh> tjualList) {
    tableModel.setRowCount(0); // Hapus semua isi tabel dulu
    int noUrut = 1;

    for (Tjualh tjualh : tjualList) {
        // Ambil detail pertama (karena 1 item bisa punya banyak detail)
        Tjuald detail = null;
        if (tjualh.getDetails() != null && !tjualh.getDetails().isEmpty()) {
            detail = tjualh.getDetails().get(0);
        }

        Object[] row = new Object[]{
            noUrut++,                        // No urut
            tjualh.getIdJualH(),                // IDItem
            tjualh.getKode(),                  // Kode
            tjualh.getTanggal(),                  // Nama
            tjualh.getJenisBayar(),              // Kategori
            tjualh.getSubTotal(),
            tjualh.getNominal(),
            tjualh.getStatus()// Harga Beli
        };

        tableModel.addRow(row);
    }
}

private void selectRowAndClose() {
    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow >= 0) {
        try {
            int idJualH = Integer.parseInt(jTable1.getValueAt(selectedRow, 1).toString());

            if (getParent() instanceof frmTransPenjualanTunai) {
                frmTransPenjualanTunai parentForm =
                        (frmTransPenjualanTunai) getParent();
                parentForm.setSelectedJual(idJualH);
            } 
            else if (getParent() instanceof frmReturPenjualan) {
                frmReturPenjualan parentForm =
                        (frmReturPenjualan) getParent();
                parentForm.setSelectedJual(idJualH);
            }

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal membaca data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this,
            "Silakan pilih data transaksi terlebih dahulu.",
            "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
}

    private void searchBarang() {
        String keyword = jTextField1.getText().trim();
        tjualList = tjualhDAO.searchJualByKode(keyword);
        updateTable(tjualList);
    }

    public JTable getTable() {
        return jTable1;
    }
    
    private void tampilkanKeTabelLaporan(List<Tjualh> listBaru) {
    this.tjualList = listBaru;  // update list utama
    this.currentPage = 1;       // reset ke halaman pertama
    applyDisplayLimit();        // update pagination
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        lblPage = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jdtglAwal = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jdtglAkhir = new com.toedter.calendar.JDateChooser();
        btnCari = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Browse Kategori");
        setMinimumSize(new java.awt.Dimension(582, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

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
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-add-15.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblPage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPage.setText("jLabel1");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-back-25.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-next-page-25.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Tanggal :");

        jCheckBox2.setBackground(new java.awt.Color(0, 255, 204));
        jCheckBox2.setSelected(true);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("s/d");

        btnCari.setText("cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdtglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addGap(11, 11, 11)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdtglAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPage, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(207, 207, 207))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jdtglAwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jdtglAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(86, 86, 86))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        searchBarang();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        if (currentPage < totalPages) {
        currentPage++;
        updatePageData();
    }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
         if (currentPage > 1) {
        currentPage--;
        updatePageData();
    }
    }//GEN-LAST:event_jLabel1MouseClicked

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
 try {
        if (jdtglAwal.getDate() == null || jdtglAkhir.getDate() == null) {
            JOptionPane.showMessageDialog(this, 
                "Isi tanggal awal dan tanggal akhir terlebih dahulu.",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.sql.Date tglAwal = new java.sql.Date(jdtglAwal.getDate().getTime());
        java.sql.Date tglAkhir = new java.sql.Date(jdtglAkhir.getDate().getTime());

        List<Tjualh> list = tjualhDAO.getByDateRange(tglAwal, tglAkhir);

        tampilkanKeTabelLaporan(list);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal filter: " + e.getMessage());
    }

    }//GEN-LAST:event_btnCariActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    Date today = new Date();
    jdtglAwal.setDate(today);
    jdtglAkhir.setDate(today);        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private com.toedter.calendar.JDateChooser jdtglAkhir;
    private com.toedter.calendar.JDateChooser jdtglAwal;
    private javax.swing.JLabel lblPage;
    // End of variables declaration//GEN-END:variables
}
