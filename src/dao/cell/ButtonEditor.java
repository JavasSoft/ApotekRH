package dao.cell;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import ui.Transaksi.frmTransPenjualanTunai;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JButton button;
    private final JTable table;
    private Runnable onRowRemovedCallback;

    public ButtonEditor(JTable jTable2) {
        this.table = jTable2;
        button = new JButton();
        button.setIcon(new ImageIcon(getClass().getResource("/icon/x.png"))); // ikon hapus
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                DefaultTableModel data = (DefaultTableModel) table.getModel();

                if (row >= 0 && row < data.getRowCount()) {
                    System.out.println("Attempting to remove row: " + row);
                    data.removeRow(row);
                    updateRowNumbers();

                    // callback internal (kalau ada)
                    if (onRowRemovedCallback != null) {
                        onRowRemovedCallback.run();
                    }

                    // update subtotal di form parent
                    SwingUtilities.invokeLater(() -> {
                        frmTransPenjualanTunai parentFrame =
                                (frmTransPenjualanTunai) SwingUtilities.getWindowAncestor(table);
                        if (parentFrame != null) {
                            parentFrame.updateSubtotal();
                        }
                    });
                } else {
                    System.err.println("Invalid row index for removal: " + row);
                }

                fireEditingStopped();
            }
        });
    }

    private void updateRowNumbers() {
        DefaultTableModel data = (DefaultTableModel) table.getModel();
        for (int i = 0; i < data.getRowCount(); i++) {
            data.setValueAt(i + 1, i, 0); // kolom pertama nomor urut
        }
    }

    @Override
    public Object getCellEditorValue() {
        return null; // tidak perlu nilai khusus
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }

    public void setOnRowRemovedCallback(Runnable callback) {
        this.onRowRemovedCallback = callback;
    }
}
