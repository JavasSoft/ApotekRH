/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
package dao.cell;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class BrowseButtonRenderer implements TableCellRenderer {
    private JButton button;

    public BrowseButtonRenderer() {
        button = new JButton("Browse");
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        button.setText("Browse");
        if (isSelected) {
            button.setBackground(table.getSelectionBackground());
            button.setForeground(table.getSelectionForeground());
        } else {
            button.setBackground(table.getBackground());
            button.setForeground(table.getForeground());
        }
        return button;
    }
}

