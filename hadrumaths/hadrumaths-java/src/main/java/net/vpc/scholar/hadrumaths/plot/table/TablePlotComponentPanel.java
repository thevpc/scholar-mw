package net.vpc.scholar.hadrumaths.plot.table;

import net.vpc.common.swings.JTableHelper;
import net.vpc.common.swings.SwingUtilities3;
import net.vpc.scholar.hadrumaths.plot.ValuesPlotTableModel;
import net.vpc.scholar.hadrumaths.plot.PlotComponentPanel;
import net.vpc.scholar.hadrumaths.plot.PlotModelProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TablePlotComponentPanel extends JPanel implements PlotComponentPanel {
    private PlotModelProvider modelProvider;
    private JTableHelper jTableHelper;

    public TablePlotComponentPanel(PlotModelProvider modelProvider) {
        super(new BorderLayout());
        this.modelProvider = modelProvider;
        jTableHelper = SwingUtilities3.createIndexedTable(new ValuesPlotTableModel(modelProvider));
        add(jTableHelper.getPane());
        jTableHelper.getPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.isPopupTrigger() || SwingUtilities3.isShowPopupEvent(e)){
                    JPopupMenu popup = jTableHelper.getTable().getComponentPopupMenu();
                    if(popup==null){
                        return;
                    }
                    SwingUtilities3.showPopup(jTableHelper.getPane(),e.getPoint(),popup);
                }
            }
        });
    }

    @Override
    public JComponent toComponent() {
        return this;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        JPopupMenu componentPopupMenu = jTableHelper.getTable().getComponentPopupMenu();
        if (componentPopupMenu == null) {
            componentPopupMenu = new JPopupMenu();
            jTableHelper.getTable().setComponentPopupMenu(componentPopupMenu);
        }
        return componentPopupMenu;
    }
}
