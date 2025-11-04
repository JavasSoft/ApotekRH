/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.Master.BrowseAll;

import dao.BarangDAO;
import dao.KategoriDAO;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import ui.Master.frmBarang;
import ui.Master.frmKategori;
import ui.Transaksi.frmTransPo;

/**
 *
 * @author Admin
 */
public class BrowseBarangDialog extends javax.swing.JDialog {
    private BarangDAO barangDAO;
    private KategoriDAO kategoriDAO;
    private DefaultTableModel tableModel;
    private List<Barang> barangList;

    /**
     * Creates new form BrowseBarangDialog
     */
    public BrowseBarangDialog(java.awt.Frame parent, boolean modal, Connection conn) {
        super(parent, modal);
        initComponents();
        this.barangDAO = new BarangDAO(conn);
        this.kategoriDAO = new KategoriDAO(conn); // Inisialisasi KategoriDAO
        setupTable();
        loadData();
        setLocationRelativeTo(null);
        setupTableClickListener();
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

   private void setupTable() {
    String[] columnNames = {"NO", "ID", "Kode", "Nama", "ID Kategori", "Kode Kategori", 
                             "Nama Kategori", "Satuan", "Harga Beli", "Harga Jual", "Keterangan", "Is Aktif"};
    tableModel = new DefaultTableModel(columnNames, 0);
    jTable1.setModel(tableModel);

    // Kolom yang akan diatur khusus
    int[] columnsWithPreferredWidth = {0, 2, 3}; // No, Kode, Nama
    int[] columnWidths = {30, 60, 300};

    // Atur kolom-kolom dengan preferensi lebar khusus
    for (int i = 0; i < columnsWithPreferredWidth.length; i++) {
        jTable1.getColumnModel().getColumn(columnsWithPreferredWidth[i]).setPreferredWidth(columnWidths[i]);
    }

    // Atur kolom yang harus disembunyikan (MinWidth, MaxWidth, Width = 0)
    int[] columnsToHide = {1, 4, 5, 6, 7, 8, 9, 10, 11};
    for (int col : columnsToHide) {
        jTable1.getColumnModel().getColumn(col).setMinWidth(0);
        jTable1.getColumnModel().getColumn(col).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(col).setWidth(0);
    }

    // Set alignment untuk kolom "NO" agar nomor urut berada di tengah
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    jTable1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    
    
    // Opsional: Isi nomor urut secara otomatis (jika ingin nomor urut ditampilkan di kolom pertama)
//    jTable1.getModel().addTableModelListener(e -> {
//        for (int row = 0; row < jTable1.getRowCount(); row++) {
//            jTable1.setValueAt(row + 1, row, 0); // Mengisi nomor urut pada kolom pertama
//        }
//    });
}

    private void setupTableClickListener() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 1) { // Deteksi klik
                    selectRowAndClose();
                }
            }
        });
    }

    private void loadData() {
        barangList = barangDAO.getAllBarang();
        updateTable(barangList);
    }

private void updateTable(List<Barang> barangList) {
    tableModel.setRowCount(0); // Reset table model
    int noUrut = 1;
    for (Barang barang : barangList) {
        // Ambil kategori berdasarkan ID kategori
        Kategori kategori = kategoriDAO.getKategoriById(barang.getIDKategori());
        String kodeKategori = (kategori != null) ? kategori.getKode() : "Tidak Ditemukan"; // Kode kategori
        String namaKategori = (kategori != null) ? kategori.getNama() : "Tidak Ditemukan"; // Nama kategori

        Object[] row = {
            noUrut++, // Nomor urut
            barang.getIDBarang(),
            barang.getKode(),
            barang.getNama(),
            barang.getIDKategori(), // ID Kategori
            kodeKategori, // Kode Kategori
            namaKategori, // Nama Kategori
            barang.getSatuan(),
            barang.getBeli(),
            barang.getJual(),
            barang.getKeterangan(),
            barang.getIsAktif()
        };
        tableModel.addRow(row);
    }
}
private void selectRowAndClose() {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow >= 0) {
        int selectedID = (int) jTable1.getValueAt(selectedRow, 1); // ID
        String selectedKode = jTable1.getValueAt(selectedRow, 2).toString(); // Kode
        String selectedNama = jTable1.getValueAt(selectedRow, 3).toString(); // Nama
        int selectedIDKategori = (int) jTable1.getValueAt(selectedRow, 4); // Kategori
        String selectedKodeK = jTable1.getValueAt(selectedRow, 5).toString(); // Kode Kategori
        String selectedNamaK = jTable1.getValueAt(selectedRow, 6).toString(); // Nama Kategori
        String selectedSatuan = jTable1.getValueAt(selectedRow, 7).toString(); // Satuan
        double selectedBeli = (double) jTable1.getValueAt(selectedRow, 8); // Harga Beli
        double selectedJual = (double) jTable1.getValueAt(selectedRow, 9); // Harga Jual
        String selectedKeterangan = jTable1.getValueAt(selectedRow, 10).toString(); // Keterangan
        boolean isAktif = "Ya".equalsIgnoreCase(jTable1.getValueAt(selectedRow, 11).toString()); // Is Aktif

        // Set nilai ke komponen di form utama
        if (getParent() instanceof frmBarang) {
            frmBarang parentForm = (frmBarang) getParent();
            parentForm.setBarangData(selectedID, selectedKode, 
                    selectedNama, selectedIDKategori, selectedKodeK, 
                    selectedNamaK, selectedSatuan, selectedBeli, 
                    selectedJual, selectedKeterangan, isAktif);
            
            // Temukan index barang dalam barangList
            int index = -1;
            for (int i = 0; i < barangList.size(); i++) {
                if (barangList.get(i).getIDBarang() == selectedID) {
                    index = i;
                    break;
                }
            }
            
            if (index != -1) {
                // Kirim index ke frmBarang
                parentForm.setCurrentRecordIndex(index);
            }
        } else if  (getParent() instanceof frmTransPo) {
            frmTransPo parentForm = (frmTransPo) getParent();
            parentForm.setBarangData(selectedID, selectedKode, 
                    selectedNama, selectedIDKategori, selectedKodeK, 
                    selectedNamaK, selectedSatuan, selectedBeli, 
                    selectedJual, selectedKeterangan, isAktif);
        }
        dispose(); // Tutup dialog setelah memilih
    } else {
        JOptionPane.showMessageDialog(this, "Silakan pilih barang.", "Tidak ada pilihan", JOptionPane.WARNING_MESSAGE);
    }
}

    private void searchBarang() {
        String keyword = jTextField1.getText().trim();
        barangList = barangDAO.searchBarangByName(keyword);
        updateTable(barangList);
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
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
