package net.vpc.scholar.hadrumaths.util.swingext;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorChooserEditor extends AbstractCellEditor implements TableCellEditor,TableCellRenderer {

    private JButton delegate = new JButton();

    Paint savedColor;

    public ColorChooserEditor() {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Color c=Color.BLACK;
                if(savedColor instanceof Color){
                    c=(Color) savedColor;
                }
                Color color = JColorChooser.showDialog(delegate, "Color Chooser", c);
                if(color==null){
                    color=c;
                }
                ColorChooserEditor.this.changeColor(color);
            }
        };
        delegate.addActionListener(actionListener);
    }

    public Object getCellEditorValue() {
        return savedColor;
    }

    private void changeColor(Color color) {
        if (color != null) {
            savedColor = color;
            delegate.setBackground(color);
        }else{
            savedColor = Color.WHITE;
            delegate.setBackground(Color.WHITE);
        }
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                 int row, int column) {
        changeColor((Color) value);
        return delegate;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(isSelected){
            changeColor(Color.LIGHT_GRAY);
        }else{
            changeColor((Color) value);
        }
        return delegate;
    }
}

