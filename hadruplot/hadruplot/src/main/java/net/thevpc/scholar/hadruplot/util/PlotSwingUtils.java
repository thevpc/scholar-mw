package net.thevpc.scholar.hadruplot.util;

import net.thevpc.common.swing.GridBagLayout2;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ItemListener;

public class PlotSwingUtils {

    private static void preparePopupMenu(final JPopupMenu componentPopupMenu) {
        componentPopupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                onRefreshComponentTree(componentPopupMenu);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
    }

    public static void setOnRefreshComponent(Component c,OnRefreshComponent runnable) {
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            jc.putClientProperty("OnRefresh",runnable);
        }
    }

    public static void onRefreshComponentTree(Component c) {
        if (c instanceof JComponent) {
            final JComponent jc = (JComponent) c;
            final OnRefreshComponent r = (OnRefreshComponent) jc.getClientProperty("OnRefresh");
            if (r != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        r.onRefreshComponent(jc);
                    }
                });
            }
            for (Component component : jc.getComponents()) {
                onRefreshComponentTree(component);
            }
        }
    }

    public static JPanel createVerticalBox(){
        JPanel f0 = new JPanel();
        f0.setLayout(new BoxLayout(f0, BoxLayout.Y_AXIS));
        return f0;
    }

    public static JPanel createVerticalComponents(String title,Component[] components){
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < components.length; i++) {
            sb.append("[^A"+i+"+<]\n");
        }
        JPanel p=new JPanel(new GridBagLayout2(sb.toString()));

        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            p.add(component, "A" + i);
        }
        return createTitledPanel(title,p);
    }

    public static JPanel createTitledPanel(String title,Component component){
        JPanel f = new JPanel(new BorderLayout());
        f.setBorder(BorderFactory.createTitledBorder(title));
        JScrollPane comp = new JScrollPane(component);
        comp.setBorder(null);
        f.add(comp);
        return f;
    }

    public static JRadioButton createRadioButton(String name, boolean selected, String keyName, Object value, ButtonGroup group, ItemListener itemListener){
        JRadioButton r = new JRadioButton(name);
        r.putClientProperty(keyName, value);
        r.setSelected(selected);
        if(group!=null){
            group.add(r);
        }
        if(itemListener!=null){
            r.addItemListener(itemListener);
        }
        return r;
    }
}
