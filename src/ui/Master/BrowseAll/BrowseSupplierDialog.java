/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.Master.BrowseAll;
import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import ui.Master.frmBarang;
import ui.Master.frmKategori;
import ui.Master.frmSupp;

/**
 *
 * @author Admin
 */
public class BrowseSupplierDialog extends javax.swing.JDialog {
    private SupplierDAO supplierDAO;
    private DefaultTableModel tableModel;
    private List<Supplier> supplierList;

    /**
     * Creates new form BrowseKategoriDialog
     */
    public BrowseSupplierDialog(java.awt.Frame parent, boolean modal, Connection conn) {
        super(parent, modal);
        initComponents();
        this.supplierDAO = new SupplierDAO(conn);
        setupTable();
        loadData();
        setLocationRelativeTo(null);
         setupTableClickListener();
         jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }
private void setupTable() {
    // Define column names
    String[] columnNames = {"NO", "ID", "Kode", "Nama", "Tempo", "Nama Usaha", 
                            "Alamat", "Telephone", "Kota", "Is Aktif"};
    tableModel = new DefaultTableModel(columnNames, 0); // Initialize table model with column names
    jTable1.setModel(tableModel);

    // Kolom yang akan diatur khusus dengan lebar yang berbeda
    int[] columnsWithPreferredWidth = {0, 2, 3, 7, 8}; // Kolom: No, Kode, Nama, Telephone, Kota
    int[] columnWidths = {30, 60, 250, 150, 100}; // Lebar kolom: No, Kode, Nama, Telephone, Kota

    // Ensure the lengths of these arrays match
    if (columnsWithPreferredWidth.length == columnWidths.length) {
        for (int i = 0; i < columnsWithPreferredWidth.length; i++) {
            jTable1.getColumnModel().getColumn(columnsWithPreferredWidth[i]).setPreferredWidth(columnWidths[i]);
        }
    } else {
        System.out.println("Error: columnWidths and columnsWithPreferredWidth arrays do not match.");
    }

    // Atur kolom yang harus disembunyikan (MinWidth, MaxWidth, Width = 0)
    int[] columnsToHide = {1, 4, 5, 6, 9}; // Kolom: ID, Tempo, Nama Usaha, Alamat, Is Aktif
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
                if (e.getClickCount() >= 1) { // Detect double click
                    selectRowAndClose();
                }
            }
        });
    }

    private void loadData() {
        supplierList = supplierDAO.getAllSuppliers();
        updateTable(supplierList);
    }

    private void updateTable(List<Supplier> supplierList) {
        tableModel.setRowCount(0); // Reset table model
        int num = 1;
        for (Supplier supplier : supplierList) {
            Object[] row = {
                num++,
                supplier.getIdSup(),
                supplier.getKode(),
                supplier.getNamaUsaha(),
                supplier.getTempo(),
                supplier.getNama(),
                supplier.getAlamat(),
                supplier.getTelephone(),
                supplier.getKota(),
                supplier.getIsAktif(),
            };
            tableModel.addRow(row);
        }
    }

private void selectRowAndClose() {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow >= 0) {
        String selectedID = jTable1.getValueAt(selectedRow, 1).toString(); // ID
        String selectedKode = jTable1.getValueAt(selectedRow, 2).toString(); // Kode
        String selectedNamaUsaha = jTable1.getValueAt(selectedRow, 3).toString(); // Nama Usaha
        String selectedTempo = jTable1.getValueAt(selectedRow, 4).toString(); // Tempo
        String selectedNama = jTable1.getValueAt(selectedRow, 5).toString(); // Nama
        String selectedAlamat = jTable1.getValueAt(selectedRow, 6).toString(); // Alamat
        String selectedTelephone = jTable1.getValueAt(selectedRow, 7).toString(); // Telephone
        String selectedKota = jTable1.getValueAt(selectedRow, 8).toString(); // Kota
        boolean isAktif = "Ya".equalsIgnoreCase(jTable1.getValueAt(selectedRow, 9).toString()); // Is Aktif

        // Set values to the components in the parent form (example)
        if (getParent() instanceof frmSupp) {
            frmSupp parentForm = (frmSupp) getParent();
            parentForm.setSupplierData(selectedID, selectedKode, 
                selectedNamaUsaha, selectedTempo, selectedNama, 
                selectedAlamat, selectedTelephone, selectedKota, 
                isAktif);
    
    // Temukan index barang dalam barangList
    int index = -1;
    for (int i = 0; i < supplierList.size(); i++) {
    int selectedIdAsInt = Integer.parseInt(selectedID); // Convert String to int
    if (supplierList.get(i).getIdSup() == selectedIdAsInt) {
        index = i;
        break;
    }
    }
    
    if (index != -1) {
        // Kirim index ke frmBarang
        parentForm.setCurrentRecordIndex(index);
    }
}

        // Close the dialog
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Please select a supplier.", "No selection", JOptionPane.WARNING_MESSAGE);
    }
}

// Search suppliers by name
    private void searchSupplier() {
    String keyword = jTextField1.getText().trim();
    supplierList = supplierDAO.searchSupplierByName(keyword); // Adjust for SupplierDAO
    updateTable(supplierList); // Update table with the search results
    }

// Getter for table (optional, if needed)
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
        searchSupplier();
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
