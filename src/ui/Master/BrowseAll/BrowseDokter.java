/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.Master.BrowseAll;

import dao.*;
import model.Dokter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import ui.Master.frmMstDokter1;
import ui.Master.frmMstItem;
import ui.Transaksi.frmTransPenjualanTunai;
//import ui.Transaksi.frmTransPo;

/**
 *
 * @author Admin
 */
public class BrowseDokter extends javax.swing.JDialog {
    private DokterDAO dokterDAO;
    private DefaultTableModel tableModel;
    private List<Dokter> dokterList;
       private List<Dokter> currentItemList = new ArrayList<>();
   // private List<Item> currentItemList = new ArrayList<>(); // data yang sedang ditampilkan
    private int displayLimit = 10;
//private List<Item> currentItemList = new ArrayList<>(); // Data di halaman aktif
//private int displayLimit = 10;   // Banyak data per halaman
private int currentPage = 1;     // Halaman aktif
private int totalPages = 1;      // Total halaman


    /**
     * Creates new form BrowseBarangDialog
     */
    public BrowseDokter(java.awt.Frame parent, boolean modal, Connection conn) {
        super(parent, modal);
        initComponents();
        this.dokterDAO = new DokterDAO(conn);
        setupTable();
        loadData();
        setupTableClickListener();
        setLocationRelativeTo(null);
        setupTableClickListener();
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

 private void setupTable() {
        String[] columnNames = {
            "No", "IDDokter", "Kode", "Nama", "Email", "Alamat", 
            "Telephone", "Kota", "Bank", "No. Rekening", "Nama Rekening", "Aktif"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tidak bisa edit langsung
            }
        };
        jTable1.setModel(tableModel);

        int[] widths = {30, 0, 80, 150, 150, 200, 100, 100, 100, 120, 120, 60};
        for (int i = 0; i < widths.length; i++) {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Sembunyikan kolom IDDokter
        jTable1.getColumnModel().getColumn(1).setMinWidth(0);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(1).setWidth(0);

        // Rata tengah kolom nomor
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    }

//   private void setupTableClickListener() {
//    jTable1.addMouseListener(new MouseAdapter() {
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            // Deteksi klik 2 kali (double click)
//            if (e.getClickCount() == 2 && jTable1.getSelectedRow() != -1) {
//                selectRowAndClose(); // Panggil fungsi saat double-click
//            }
//        }
//    });
//}


    private void loadData() {
        dokterList = dokterDAO.getAllDokter();
        updateTable(dokterList);
        applyDisplayLimit();
    }
    
private void applyDisplayLimit() {

    // Hitung total halaman
    totalPages = (int) Math.ceil((double) dokterList.size() / displayLimit);

    // Pastikan halaman aktif tidak melebihi total halaman
    if (currentPage > totalPages) {
        currentPage = totalPages;
    }

    updatePageData();
}

private void updatePageData() {
    int startIndex = (currentPage - 1) * displayLimit;
    int endIndex = Math.min(startIndex + displayLimit, dokterList.size());

    currentItemList = dokterList.subList(startIndex, endIndex);

    // Update tampilan tabel
//    updateTable(currentItemList);

    // Update label halaman
    lblPage.setText(currentPage + "/" + totalPages);
}

  private void updateTable(List<Dokter> list) {
        tableModel.setRowCount(0);
        int no = 1;
        for (Dokter d : list) {
            tableModel.addRow(new Object[]{
                no++,
                d.getIDDokter(),
                d.getKode(),
                d.getNama(),
                d.getEmail(),
                d.getAlamat(),
                d.getTelephone(),
                d.getKota(),
                d.getBank(),
                d.getNomorRekening(),
                d.getNamaRekening(),
                (d.getAktif() == 1) ? "Aktif" : "Tidak Aktif"
            });
        }
    }


    /** Event double-click di tabel **/
    private void setupTableClickListener() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && jTable1.getSelectedRow() != -1) {
                    selectRowAndClose();
                }
            }
        });
    }

    /** Ambil data dari tabel saat double-click **/
    private void selectRowAndClose() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int id = Integer.parseInt(jTable1.getValueAt(selectedRow, 1).toString());
                String kode = jTable1.getValueAt(selectedRow, 2).toString();
                String nama = jTable1.getValueAt(selectedRow, 3).toString();
                String email = jTable1.getValueAt(selectedRow, 4).toString();
                String alamat = jTable1.getValueAt(selectedRow, 5).toString();
                String telp = jTable1.getValueAt(selectedRow, 6).toString();
                String kota = jTable1.getValueAt(selectedRow, 7).toString();
                String bank = jTable1.getValueAt(selectedRow, 8).toString();
                String norek = jTable1.getValueAt(selectedRow, 9).toString();
                String namarek = jTable1.getValueAt(selectedRow, 10).toString();
                boolean aktif = "Aktif".equalsIgnoreCase(jTable1.getValueAt(selectedRow, 11).toString());

                if (getParent() instanceof frmMstDokter1) {
                frmMstDokter1 parentForm = (frmMstDokter1) getParent();
                parentForm.setDokterData(
                    id, kode, nama, email,
                    alamat, telp,
                    kota, bank, norek, namarek, aktif
                );
            } 
                 else if (getParent() instanceof frmTransPenjualanTunai) {
                frmTransPenjualanTunai parentForm = (frmTransPenjualanTunai) getParent();
                // kirim hanya data yang dibutuhkan oleh transaksi
                parentForm.setDokterData(id, nama);
            }
                
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Terjadi kesalahan: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih dokter terlebih dahulu.",
                    "Tidak ada pilihan", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Pencarian dokter **/
    private void searchDokter() {
        String keyword = jTextField1.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
        } else {
            dokterList = dokterDAO.searchDokterByName(keyword);
            updateTable(dokterList);
        }
    }

    public JTable getTable() {
        return jTable1;
    }






//private void selectRowAndClose() {
//    int selectedRow = jTable1.getSelectedRow();
//    if (selectedRow >= 0) {
//        int selectedID = (int) jTable1.getValueAt(selectedRow, 1); // ID
//        String selectedKode = jTable1.getValueAt(selectedRow, 2).toString(); // Kode
//        String selectedNama = jTable1.getValueAt(selectedRow, 3).toString(); // Nama
//        int selectedIDKategori = (int) jTable1.getValueAt(selectedRow, 4); // Kategori
//        String selectedKodeK = jTable1.getValueAt(selectedRow, 5).toString(); // Kode Kategori
//        String selectedNamaK = jTable1.getValueAt(selectedRow, 6).toString(); // Nama Kategori
//        String selectedSatuan = jTable1.getValueAt(selectedRow, 7).toString(); // Satuan
//        double selectedBeli = (double) jTable1.getValueAt(selectedRow, 8); // Harga Beli
//        double selectedJual = (double) jTable1.getValueAt(selectedRow, 9); // Harga Jual
//        String selectedKeterangan = jTable1.getValueAt(selectedRow, 10).toString(); // Keterangan
//        boolean isAktif = "Ya".equalsIgnoreCase(jTable1.getValueAt(selectedRow, 11).toString()); // Is Aktif
//
//        // Set nilai ke komponen di form utama
//        if (getParent() instanceof frmMstItem) {
//            frmMstItem parentForm = (frmMstItem) getParent();
//            parentForm.setItemData(selectedID, selectedKode, 
//                    selectedNama, selectedIDKategori, selectedKodeK, 
//                    selectedNamaK, selectedSatuan, selectedBeli, 
//                    selectedJual, selectedKeterangan, isAktif);
//            
//            // Temukan index barang dalam barangList
//            int index = -1;
//            for (int i = 0; i < barangList.size(); i++) {
//                if (barangList.get(i).getIDBarang() == selectedID) {
//                    index = i;
//                    break;
//                }
//            }
//            
//            if (index != -1) {
//                // Kirim index ke frmBarang
//                parentForm.setCurrentRecordIndex(index);
//            }
//        } else if  (getParent() instanceof frmTransPo) {
//            frmTransPo parentForm = (frmTransPo) getParent();
//            parentForm.setBarangData(selectedID, selectedKode, 
//                    selectedNama, selectedIDKategori, selectedKodeK, 
//                    selectedNamaK, selectedSatuan, selectedBeli, 
//                    selectedJual, selectedKeterangan, isAktif);
//        }
//        dispose(); // Tutup dialog setelah memilih
//    } else {
//        JOptionPane.showMessageDialog(this, "Silakan pilih barang.", "Tidak ada pilihan", JOptionPane.WARNING_MESSAGE);
//    }
//}

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Browse Kategori");
        setMinimumSize(new java.awt.Dimension(582, 300));

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
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
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        searchDokter();
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

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblPage;
    // End of variables declaration//GEN-END:variables
}
