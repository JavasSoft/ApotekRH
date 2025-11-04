package dao.cell;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonRenderer implements TableCellRenderer {

    private JButton button;

    public ButtonRenderer() {
        // Inisialisasi JButton
        button = new JButton();
        button.setOpaque(true); // Pastikan tombol ditampilkan dengan benar
        button.setBorderPainted(false); // Menghilangkan border
        button.setContentAreaFilled(false); // Menghilangkan warna latar belakang
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Pastikan ikon dimuat dengan benar
        ImageIcon icon = new ImageIcon(getClass().getResource("/icon/x.png"));
        if (icon.getIconWidth() == -1) {
            System.err.println("Icon tidak ditemukan!");
            button.setIcon(null); // Setel ikon ke null jika tidak ditemukan
        } else {
            button.setIcon(icon);
        }

        // Setel warna latar belakang dan teks untuk sel yang dipilih
        if (isSelected) {
            button.setBackground(table.getSelectionBackground());
            button.setForeground(table.getSelectionForeground());
        } else {
            button.setBackground(table.getBackground());
            button.setForeground(table.getForeground());
        }

        // Kembalikan tombol
        return button;
    }
}
