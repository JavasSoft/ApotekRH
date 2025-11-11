/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.Master.BrowseAll;

import dao.ItemDAO;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import ui.Master.frmMstItem;
import ui.Transaksi.frmTransPenjualanTunai;
//import ui.Transaksi.frmTransPo;

/**
 *
 * @author Admin
 */
public class BrowseItem extends javax.swing.JDialog {
    private ItemDAO itemDAO;
    private DefaultTableModel tableModel;
    private List<Item> itemList;
    private List<Item> currentItemList = new ArrayList<>(); // data yang sedang ditampilkan
    private int displayLimit = 10;
//private List<Item> currentItemList = new ArrayList<>(); // Data di halaman aktif
//private int displayLimit = 10;   // Banyak data per halaman
private int currentPage = 1;     // Halaman aktif
private int totalPages = 1;      // Total halaman


    /**
     * Creates new form BrowseBarangDialog
     */
    public BrowseItem(java.awt.Frame parent, boolean modal, Connection conn) {
        super(parent, modal);
        initComponents();
        this.itemDAO = new ItemDAO(conn);
        setupTable();
        loadData();
        setupTableClickListener();
        setupLimitListener();
        setLocationRelativeTo(null);
        setupTableClickListener();
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

  private void setupTable() {
    String[] columnNames = {
        "No", "IDItem", "Kode", "Nama", "Kategori",
        "Harga Beli", "Aktif",
        "Satuan Kecil", "Satuan Besar", "Harga Jual",
        "Laba (%)", "Konversi"
    };

    tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Semua kolom tidak bisa diedit langsung
        }
    };
    jTable1.setModel(tableModel);

    // Lebar kolom penting
    int[] preferredWidths = {30, 0, 80, 200, 100, 80, 60, 100, 100, 100, 70, 70};
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
    jTable1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
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
        itemList = itemDAO.getAllItems();
        updateTable(itemList);
        applyDisplayLimit();
    }
    
 private void applyDisplayLimit() {
    // Ambil limit dari combo box
    displayLimit = Integer.parseInt(cmbLimit.getSelectedItem().toString());

    // Potong list sesuai limit
    if (itemList.size() > displayLimit) {
        currentItemList = itemList.subList(0, displayLimit);
    } else {
        currentItemList = itemList;
    }

    updateTable(currentItemList); // refresh tampilan tabel
}
 
 private void setupLimitListener() {
    cmbLimit.addActionListener(e -> applyDisplayLimit());
}



private void updateTable(List<Item> itemList) {
    tableModel.setRowCount(0); // Hapus semua isi tabel dulu
    int noUrut = 1;

    for (Item item : itemList) {
        // Ambil detail pertama (karena 1 item bisa punya banyak detail)
        ItemDetail detail = null;
        if (item.getDetails() != null && !item.getDetails().isEmpty()) {
            detail = item.getDetails().get(0);
        }

        Object[] row = new Object[]{
            noUrut++,                        // No urut
            item.getIDItem(),                // IDItem
            item.getKode(),                  // Kode
            item.getNama(),                  // Nama
            item.getKategori(),              // Kategori
            item.getHargaBeli(),             // Harga Beli
            (item.getAktif() == 1) ? "Aktif" : "Tidak Aktif",  // Status aktif
            (detail != null) ? detail.getSatuan() : "",         // Satuan kecil
            (detail != null) ? detail.getSatuanBesar() : "",    // Satuan besar
            (detail != null) ? detail.getHargaJual() : 0,       // Harga jual
            (detail != null) ? detail.getLabaPersen() : 0,      // Laba %
            (detail != null) ? detail.getKonversi() : 0         // Konversi
        };

        tableModel.addRow(row);
    }
}

private void selectRowAndClose() {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow >= 0) {
        try {
            // Ambil semua data sesuai urutan kolom
            int selectedID = Integer.parseInt(jTable1.getValueAt(selectedRow, 1).toString()); // IDItem
            String selectedKode = jTable1.getValueAt(selectedRow, 2).toString();
            String selectedNama = jTable1.getValueAt(selectedRow, 3).toString();
            String selectedKategori = jTable1.getValueAt(selectedRow, 4).toString();
            double selectedHargaBeli = Double.parseDouble(jTable1.getValueAt(selectedRow, 5).toString());
            boolean isAktif = "Aktif".equalsIgnoreCase(jTable1.getValueAt(selectedRow, 6).toString());

            String satuanKecil = jTable1.getValueAt(selectedRow, 7).toString();
            String satuanBesar = jTable1.getValueAt(selectedRow, 8).toString();
            double hargaJual = Double.parseDouble(jTable1.getValueAt(selectedRow, 9).toString());
            double labaPersen = Double.parseDouble(jTable1.getValueAt(selectedRow, 10).toString());
            double konversi = Double.parseDouble(jTable1.getValueAt(selectedRow, 11).toString());

            // Kirim ke form utama
            if (getParent() instanceof frmMstItem) {
                frmMstItem parentForm = (frmMstItem) getParent();
                parentForm.setItemData(
                    selectedID, selectedKode, selectedNama, selectedKategori,
                    selectedHargaBeli, isAktif,
                    satuanKecil, satuanBesar, hargaJual, labaPersen, konversi
                );
            } 
            else if (getParent() instanceof frmTransPenjualanTunai) {
                frmTransPenjualanTunai parentForm = (frmTransPenjualanTunai) getParent();
                parentForm.setItemData(
                    selectedID, selectedKode, selectedNama, selectedKategori,
                    selectedHargaBeli, isAktif,
                    satuanKecil, satuanBesar, hargaJual, labaPersen, konversi
                );
            }

            dispose(); // Tutup dialog setelah memilih

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan membaca data item: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this,
                "Silakan pilih item terlebih dahulu.",
                "Tidak ada pilihan", JOptionPane.WARNING_MESSAGE);
    }
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

    private void searchBarang() {
        String keyword = jTextField1.getText().trim();
        itemList = itemDAO.searchBarangByName(keyword);
        updateTable(itemList);
    }

    public JTable getTable() {
        return jTable1;
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
        cmbLimit = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Browse Kategori");
        setMinimumSize(new java.awt.Dimension(464, 245));

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

        cmbLimit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10", "50", "100" }));

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cmbLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(258, 258, 258))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(80, 80, 80))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        searchBarang();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbLimit;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
