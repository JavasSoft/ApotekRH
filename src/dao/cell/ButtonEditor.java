package dao.cell;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    private JTable table;
    private Runnable onRowRemovedCallback;

    public ButtonEditor(JTable jTable2) {
        this.table = jTable2;
        button = new JButton();
        button.setIcon(new ImageIcon(getClass().getResource("/icon/x.png"))); // Menggunakan ikon
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
                    
                    // Panggil callback jika ada
                    if (onRowRemovedCallback != null) {
                        onRowRemovedCallback.run();
                    }
                } else {
                    System.err.println("Invalid row index for removal: " + row);
                }

                fireEditingStopped();
            }
        });
    }

    private void updateRowNumbers() {
        DefaultTableModel data = (DefaultTableModel) table.getModel();
        int rowCount = data.getRowCount();

        // Asumsi kolom pertama adalah nomor baris
        for (int row = 0; row < rowCount; row++) {
            data.setValueAt(row + 1, row, 0); // Update nomor baris
        }
    }

    @Override
    public Object getCellEditorValue() {
        return null; // Tidak ada nilai yang disimpan
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button; // Kembalikan tombol untuk diedit
    }

    public void setOnRowRemovedCallback(Runnable callback) {
        this.onRowRemovedCallback = callback; // Set callback
    }
}
