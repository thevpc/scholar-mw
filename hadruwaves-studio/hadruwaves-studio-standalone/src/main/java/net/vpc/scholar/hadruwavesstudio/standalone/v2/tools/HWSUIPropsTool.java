package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools;

import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class HWSUIPropsTool extends AbstractToolWindowPanel {

    private JTextField filter=new JTextField();
    private JTable table;

    public HWSUIPropsTool(HadruwavesStudio studio) {
        super(studio);
        table = new JTable();
        JPanel p=new JPanel(new BorderLayout());
        p.add(filter,BorderLayout.NORTH);
        p.add(new JScrollPane(table),BorderLayout.CENTER);
        setContent(p);
        filter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onLookChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onLookChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onLookChanged();
            }
        });
    }

    public void onLookChanged() {
        DefaultTableModel tm = new DefaultTableModel();
        tm.addColumn("Key");
        tm.addColumn("Value");
        java.util.List<Object[]> list = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
            Object key = entry.getKey();
            if (key instanceof Color || entry.getValue() instanceof Color) {
                if(filter.getText().length()>0){
                    if(key instanceof String){
                        if(!key.toString().toLowerCase().contains(filter.getText().toLowerCase())){
                            continue;
                        }
                    }else{
                        continue;
                    }
                }
                list.add(new Object[]{key, entry.getValue()});
            }
        }
        list.sort(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                String s1 = String.valueOf(((Object[]) o1)[0]);
                String s2 = String.valueOf(((Object[]) o2)[0]);
                return s1.compareTo(s2);
            }
        });
        for (Object[] objects : list) {
            tm.addRow(objects);
        }
        table.setModel(tm);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Color) {
                    setBackground((Color) value);
                } else {
                    setForeground(Color.black);
                    setBackground(Color.white);
                }
                return c;
            }

        });

    }

}
