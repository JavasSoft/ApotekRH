/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;
import ui.Master.*;
import ui.Transaksi.*;
import dao.Koneksi;
import dao.LoginDAO;
import model.User;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.security.MessageDigest;
import java.sql.Statement;

/**
 *
 * @author Admin
 */
public class frmLogin extends javax.swing.JFrame {
private javax.swing.JPanel jMenuPanel; // Add this declaration
private LoginDAO loginDAO;
    private Connection conn;
    private Statement stat;
    private ResultSet rs;
    private String sql;
    private User user;
    /**
     * Creates new form ATIS
     */
    public frmLogin() {
        initComponents();
        initializeDatabase();
        loginDAO = new LoginDAO(conn);
        jMenuBar1.setVisible(false);
        jToolBar1.setFloatable(false);
        MeSu();
         awal();
         setExtendedState(JFrame.MAXIMIZED_BOTH);
         
    }
       private void changeFormTitle(String title) {
        setTitle(title);  // Mengubah title form berdasarkan menu yang dipilih
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
private void MeSu(){
     jMenuPanel = new javax.swing.JPanel();
    jMenuPanel.setLayout(new java.awt.BorderLayout()); // Set layout for the menu panel
    jMenuPanel.add(jMenuBar1, java.awt.BorderLayout.NORTH); // Add the menu bar to this panel

    // Update jPanel2 layout to include jMenuPanel
    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jMenuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Add menu panel here
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGap(59, 59, 59)
            .addComponent(PanelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(65, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createSequentialGroup()
        .addComponent(jMenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) // Add menu panel here
        .addGap(27, 27, 27)
        .addComponent(PanelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(26, Short.MAX_VALUE));
}

private void awal(){
    PtxtUsernameFocusLost();
    PtxtPasswordFocusLost();
    jButton1.requestFocusInWindow();   
    jLabel6.setVisible(false);
}
    private void PtxtUsernameFocusGained() {                                        
        // TODO add your handling code here:
     if (txtUsername.getText().equals("Username")) {
        txtUsername.setText("");
        txtUsername.setForeground(Color.BLACK);
    }
    }                                       

    private void PtxtUsernameFocusLost() {                                      
        // TODO add your handling code here:
                    if (txtUsername.getText().equals("")) {
        txtUsername.setText("Username");
        txtUsername.setForeground(Color.GRAY);
    }
    }                                     

    private void PtxtPasswordFocusGained() {                                        
        // TODO add your handling code here:
//         txtPassword.setSelected(false);
    txtPassword.setEchoChar('*');
    String password = String.valueOf(txtPassword.getPassword());
    
    if(password.toLowerCase().equals("password"))
    {
        txtPassword.setText("");
        txtPassword.setForeground(Color.black);
    }
    }                                       

    private void PtxtPasswordFocusLost() {                                      
        // TODO add your handling code here:
         String password = String.valueOf(txtPassword.getPassword());
    
    
    if(password.toLowerCase().equals("password") || password.toLowerCase().equals("") )
    {
        txtPassword.setText("Password");
        txtPassword.setEchoChar((char)0);
        txtPassword.setForeground(new Color(153, 153, 153));
    }
    }                                     
//////////////////////////////////////////////////////////////////////////////////////////////
    private void LoginAction(){
        String username = txtUsername.getText();
    String password = String.valueOf(txtPassword.getPassword());
        // Validasi input
    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Username dan password tidak boleh kosong.");
        return;
    }
    try {
        User user = loginDAO.validate(username, password);
        if (user != null) {
            // Successful login logic here
            // Optionally show a success message
            // JOptionPane.showMessageDialog(this, "Login successful!");
            
            // Proceed to the next screen or functionality
            PanelLogin.setVisible(false);
            jMenuBar1.setVisible(true);
            jLabel6.setText(user.getUsername() + " - " + user.getEmail());
            jLabel6.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error during login: " + e.getMessage());
        e.printStackTrace();
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

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        btnMstPeriode = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jmMstKategori = new javax.swing.JMenuItem();
        MstBarang = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jmMstCus = new javax.swing.JMenuItem();
        jmMstSupp = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuPO = new javax.swing.JMenuItem();
        jMenuBeli = new javax.swing.JMenuItem();
        jMenuBeliBayar = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuPenjualanTunai = new javax.swing.JMenuItem();
        jMenuPenjualanKredit = new javax.swing.JMenuItem();
        jMenuJualBayar = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        PanelLogin = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        txtUsername = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();

        jMenu1.setText("Master");

        btnMstPeriode.setText("[1001] Master Periode");
        btnMstPeriode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMstPeriodeActionPerformed(evt);
            }
        });
        jMenu1.add(btnMstPeriode);
        jMenu1.add(jSeparator3);

        jmMstKategori.setText("[1002] Master Kategori");
        jmMstKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmMstKategoriActionPerformed(evt);
            }
        });
        jMenu1.add(jmMstKategori);

        MstBarang.setText("[1003] Master Barang");
        MstBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MstBarangActionPerformed(evt);
            }
        });
        jMenu1.add(MstBarang);
        jMenu1.add(jSeparator2);

        jmMstCus.setText("[1004] Master Pelanggan");
        jmMstCus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmMstCusActionPerformed(evt);
            }
        });
        jMenu1.add(jmMstCus);

        jmMstSupp.setText("[1005] Master Supplier");
        jmMstSupp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmMstSuppActionPerformed(evt);
            }
        });
        jMenu1.add(jmMstSupp);

        jMenuItem5.setText("Coa");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Transaksi");

        jMenuPO.setText("[2001] Transaksi PO");
        jMenuPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPOActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuPO);

        jMenuBeli.setText("Transaksi Beli");
        jMenuBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuBeliActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuBeli);

        jMenuBeliBayar.setText("Beli Bayar");
        jMenu2.add(jMenuBeliBayar);
        jMenu2.add(jSeparator1);

        jMenuPenjualanTunai.setText("Transaksi Penjualan Tunai");
        jMenuPenjualanTunai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPenjualanTunaiActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuPenjualanTunai);

        jMenuPenjualanKredit.setText("Transaksi Penjualan Kredit");
        jMenu2.add(jMenuPenjualanKredit);

        jMenuJualBayar.setText("Pelunasan");
        jMenu2.add(jMenuJualBayar);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Laporan");

        jMenu5.setText("Stok");

        jMenuItem1.setText("Kartu Stok Per Item");
        jMenu5.add(jMenuItem1);

        jMenuItem3.setText("Laporan Rekap Stok");
        jMenu5.add(jMenuItem3);

        jMenu4.add(jMenu5);

        jMenu6.setText("Purchase Order");

        jMenuItem4.setText("PO per Item");
        jMenu6.add(jMenuItem4);

        jMenuItem6.setText("PO per Supplier");
        jMenu6.add(jMenuItem6);

        jMenu4.add(jMenu6);

        jMenuBar1.add(jMenu4);

        jMenu3.setText("Tambahan");

        jMenuItem2.setText("Konfigurasi");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar1.add(jMenu3);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ATIS (Accounting Trading Inventory Solution) System");

        jPanel6.setBackground(new java.awt.Color(0, 102, 255));

        jLabel5.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Toko Alfaduro");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("jLabel6");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jPanel2AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        PanelLogin.setBackground(new java.awt.Color(204, 255, 255));
        PanelLogin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 255)));

        jButton1.setText("Masuk");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PressEnter(evt);
            }
        });

        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsernameFocusLost(evt);
            }
        });
        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText(" Form Login");
        jLabel1.setToolTipText("");
        jLabel1.setAlignmentY(0.0F);

        jPanel4.setBackground(new java.awt.Color(0, 102, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 141, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 199, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Username");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Password");

        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordFocusLost(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("(c) Hasan");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PanelLoginLayout = new javax.swing.GroupLayout(PanelLogin);
        PanelLogin.setLayout(PanelLoginLayout);
        PanelLoginLayout.setHorizontalGroup(
            PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelLoginLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelLoginLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addComponent(txtPassword))))))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PanelLoginLayout.setVerticalGroup(
            PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelLoginLayout.createSequentialGroup()
                .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelLoginLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(PanelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 238, Short.MAX_VALUE)
                .addComponent(PanelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jToolBar1.setBackground(new java.awt.Color(204, 255, 255));
        jToolBar1.setRollover(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1021, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jPanel2AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2AncestorAdded

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        LoginAction();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void MstBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MstBarangActionPerformed
        // TODO add your handling code here:
            frmBarang fb = new frmBarang();
            fb.setVisible(true);
    }//GEN-LAST:event_MstBarangActionPerformed

    private void txtUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusGained
        // TODO add your handling code here:
        PtxtUsernameFocusGained();
    }//GEN-LAST:event_txtUsernameFocusGained

    private void txtUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusLost
        // TODO add your handling code here:
        PtxtUsernameFocusLost();
    }//GEN-LAST:event_txtUsernameFocusLost

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        // TODO add your handling code here:
        PtxtPasswordFocusGained();
//         txtPassword.setSelected(false);
    }//GEN-LAST:event_txtPasswordFocusGained

    private void txtPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusLost
        // TODO add your handling code here:
       PtxtUsernameFocusLost();
    }//GEN-LAST:event_txtPasswordFocusLost

    private void jMenuPenjualanTunaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPenjualanTunaiActionPerformed
        // TODO add your handling code here:
//                     uTransJual utransjual = new uTransJual();
//            utransjual.setVisible(true);
    }//GEN-LAST:event_jMenuPenjualanTunaiActionPerformed

    private void jMenuPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPOActionPerformed
        frmTransPo TranPO = new frmTransPo();
        TranPO.setVisible(true);
    }//GEN-LAST:event_jMenuPOActionPerformed

    private void jmMstKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmMstKategoriActionPerformed
//         TODO add your handling code here:
        frmKategori mstKategori = new frmKategori();
        mstKategori.setVisible(true);
    }//GEN-LAST:event_jmMstKategoriActionPerformed

    private void jMenuBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuBeliActionPerformed
        // TODO add your handling code here:
//                uTransBeliPo utransbelipo = new uTransBeliPo();
//        utransbelipo.setVisible(true);
    }//GEN-LAST:event_jMenuBeliActionPerformed

    private void btnMstPeriodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMstPeriodeActionPerformed
        // TODO add your handling code here:
              frmPeriode fp = new frmPeriode();
        fp.setVisible(true);
    }//GEN-LAST:event_btnMstPeriodeActionPerformed

    private void PressEnter(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PressEnter
        // TODO add your handling code here:
         if(evt.getKeyCode()==KeyEvent.VK_ENTER){
             LoginAction();
         }
    }//GEN-LAST:event_PressEnter

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode()==KeyEvent.VK_ENTER){
             LoginAction();
         }
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void jmMstSuppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmMstSuppActionPerformed
        // TODO add your handling code here:
        frmSupp fspp = new frmSupp();
        fspp.setVisible(true);
    }//GEN-LAST:event_jmMstSuppActionPerformed

    private void jmMstCusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmMstCusActionPerformed
        // TODO add your handling code here:
                frmCus frmCus = new frmCus();
        frmCus.setVisible(true);
    }//GEN-LAST:event_jmMstCusActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
                // TODO add your handling code here:
        frmCoa frmCoa = new frmCoa();
        frmCoa.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

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
//            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem MstBarang;
    private javax.swing.JPanel PanelLogin;
    private javax.swing.JMenuItem btnMstPeriode;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuBeli;
    private javax.swing.JMenuItem jMenuBeliBayar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuJualBayar;
    private javax.swing.JMenuItem jMenuPO;
    private javax.swing.JMenuItem jMenuPenjualanKredit;
    private javax.swing.JMenuItem jMenuPenjualanTunai;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem jmMstCus;
    private javax.swing.JMenuItem jmMstKategori;
    private javax.swing.JMenuItem jmMstSupp;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
