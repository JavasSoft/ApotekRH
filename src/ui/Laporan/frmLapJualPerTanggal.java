/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.Laporan;
import ui.Master.*;
import dao.Koneksi;
import dao.TjualhDAO;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Tjualh;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author Admin
 */
public class frmLapJualPerTanggal extends javax.swing.JFrame {
   private Connection conn;
       private Statement stat;
    /**
     * Creates new form ParentTrans
     */
    public frmLapJualPerTanggal() {
        initComponents();
        initializeDatabase();
     //   FormShow();
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
 private String formatAngka(double value) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(value);
    }

    private double parseAngka(String value) {
        return Double.parseDouble(value.replace(",", "").replace(".", ""));
    }


private void tampilkanKeTabelLaporan(List<Tjualh> list) {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); // bersihkan tabel

    int no = 1;
    // 1. Deklarasi variabel untuk Grand Total
    double grandTotal = 0.0; 

    for (Tjualh h : list) {
        String namaCustomer = (h.getCustomer() != null && h.getCustomer().getNama() != null)
                ? h.getCustomer().getNama() : "-";

        String namaDokter = (h.getDokter() != null && h.getDokter().getNama() != null)
                ? h.getDokter().getNama() : "-";

        model.addRow(new Object[]{
            no++, 					// nomor urut
            h.getKode(),
            h.getTanggal(),
            namaCustomer,
            namaDokter,
            formatAngka(h.getSubTotal()),
            formatAngka(h.getDiskon()),
            formatAngka(h.getPpn()),
            formatAngka(h.getTotal())
        });
        
        // 2. Akumulasi total
        grandTotal += h.getTotal(); 
    }
    
    // 3. Tampilkan Grand Total ke txtgrandtotal
    // Asumsi: txtgrandtotal adalah komponen JTextField/JLabel, dan Anda ingin memformat angkanya.
    
txtGrandTotal.setText(formatAngka(grandTotal)); 
}

private void initTableLaporan() {
    DefaultTableModel model = new DefaultTableModel(
        new Object[]{"No", "Kode", "Tanggal", "Customer", "Dokter", "Subtotal", "Diskon", "PPN", "Total"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // semua kolom tidak bisa diedit
        }
    };

    jTable1.setModel(model);

    // Hilangkan grid dan spacing
    jTable1.setShowGrid(false);
    jTable1.setIntercellSpacing(new java.awt.Dimension(0, 0));
    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    // Set lebar kolom
    jTable1.getColumn("No").setPreferredWidth(40);
    jTable1.getColumn("Kode").setPreferredWidth(120);
    jTable1.getColumn("Tanggal").setPreferredWidth(100);
    jTable1.getColumn("Customer").setPreferredWidth(200);
    jTable1.getColumn("Dokter").setPreferredWidth(200);
    jTable1.getColumn("Subtotal").setPreferredWidth(120);
    jTable1.getColumn("Diskon").setPreferredWidth(120);
    jTable1.getColumn("PPN").setPreferredWidth(120);
    jTable1.getColumn("Total").setPreferredWidth(120);

    // Buat striped rows
    jTable1.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                setBackground(new java.awt.Color(135, 206, 250)); // biru muda
                setForeground(java.awt.Color.BLACK);
            } else {
                setBackground(row % 2 == 0
                        ? java.awt.Color.WHITE
                        : new java.awt.Color(240, 255, 240)); // hijau muda lembut
                setForeground(java.awt.Color.BLACK);
            }

            return this;
        }
    });

    // Header warna hijau tua
    jTable1.getTableHeader().setBackground(new java.awt.Color(0, 102, 0));
    jTable1.getTableHeader().setForeground(java.awt.Color.WHITE);
    jTable1.getTableHeader().setFont(jTable1.getTableHeader().getFont().deriveFont(java.awt.Font.BOLD));

    // Kolom angka rata kanan
    DefaultTableCellRenderer right = new DefaultTableCellRenderer();
    right.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
    
    DefaultTableCellRenderer center = new DefaultTableCellRenderer();
center.setHorizontalAlignment(javax.swing.JLabel.CENTER);

    jTable1.getColumn("No").setCellRenderer(center);
    jTable1.getColumn("Subtotal").setCellRenderer(right);
    jTable1.getColumn("Diskon").setCellRenderer(right);
    jTable1.getColumn("PPN").setCellRenderer(right);
    jTable1.getColumn("Total").setCellRenderer(right);
}




    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jdtglAwal = new com.toedter.calendar.JDateChooser();
        jdtglAkhir = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnCari = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        btnPrint = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cmbAktif = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtGrandTotal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(0, 255, 204));
        jPanel1.setToolTipText("");

        jPanel2.setBackground(new java.awt.Color(0, 102, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("s/d");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Tanggal :");

        btnCari.setText("cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        jCheckBox2.setBackground(new java.awt.Color(0, 255, 204));
        jCheckBox2.setSelected(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdtglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(11, 11, 11)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdtglAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCari)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jdtglAwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdtglAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 255, 204));

        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

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
                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit)
                .addGap(29, 29, 29))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnPrint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cmbAktif))
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
        jScrollPane1.setViewportView(jTable1);

        txtGrandTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Total :");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtGrandTotal)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
         try {
        // Ambil tanggal dari JDateChooser
        java.sql.Date tglAwal = new java.sql.Date(jdtglAwal.getDate().getTime());
        java.sql.Date tglAkhir = new java.sql.Date(jdtglAkhir.getDate().getTime());

        TjualhDAO dao = new TjualhDAO(conn);
        List<Tjualh> list = dao.getByDateRange(tglAwal, tglAkhir);

        // Menampilkan ke tabel
        tampilkanKeTabelLaporan(list);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal filter: " + e.getMessage());
    }
         
    }//GEN-LAST:event_btnCariActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        initTableLaporan();
      // Set format tanggal
jdtglAwal.setDateFormatString("dd/MM/yyyy");
jdtglAkhir.setDateFormatString("dd/MM/yyyy");

// Ambil tanggal sekarang
Date today = new Date();
Calendar cal = Calendar.getInstance();
cal.setTime(today);

// Set tanggal awal ke 1 bulan ini
cal.set(Calendar.DAY_OF_MONTH, 1);
jdtglAwal.setDate(cal.getTime());

// Set tanggal akhir ke hari terakhir bulan ini
cal.setTime(today); // reset ke hari ini
int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
cal.set(Calendar.DAY_OF_MONTH, lastDay);
jdtglAkhir.setDate(cal.getTime());

    }//GEN-LAST:event_formWindowOpened

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
try {
    java.sql.Date tglAwal = new java.sql.Date(jdtglAwal.getDate().getTime());
    java.sql.Date tglAkhir = new java.sql.Date(jdtglAkhir.getDate().getTime());
    


    HashMap<String, Object> param = new HashMap<>();
    param.put("tglAwal", tglAwal);
    param.put("tglAkhir", tglAkhir);
    
    ResultSet rs = stat.executeQuery(
    "SELECT * FROM tjualh WHERE Tanggal BETWEEN '" + tglAwal + "' AND '" + tglAkhir + "'"
);
int count = 0;
while(rs.next()) count++;
System.out.println("Jumlah record: " + count);

    // Path file jasper (pastikan path ini sesuai lokasi jasper kamu)
    String reportPath = "src/ui/Laporan/Jual.jasper";

    JasperPrint jp = JasperFillManager.fillReport(reportPath, param, conn);
    

    // Membuat JasperViewer dan menampilkannya
    JasperViewer viewer = new JasperViewer(jp, false);
    viewer.setVisible(true);  // menampilkan viewer

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Gagal cetak: " + e.getMessage());
}


    }//GEN-LAST:event_btnPrintActionPerformed

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
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnPrint;
    private javax.swing.JCheckBox cmbAktif;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private com.toedter.calendar.JDateChooser jdtglAkhir;
    private com.toedter.calendar.JDateChooser jdtglAwal;
    private javax.swing.JTextField txtGrandTotal;
    // End of variables declaration//GEN-END:variables
}
