package net.vpc.scholar.hadrumaths.plot.table;

import net.vpc.scholar.hadrumaths.plot.ValuesPlotTableModel;
import net.vpc.scholar.hadrumaths.plot.PlotComponentPanel;
import net.vpc.scholar.hadrumaths.plot.PlotModelProvider;
import net.vpc.scholar.hadrumaths.plot.swings.JTableHelper;

import javax.swing.*;
import java.awt.*;

public class TablePlotComponentPanel extends JPanel implements PlotComponentPanel {
    private PlotModelProvider modelProvider;
    private JTableHelper jTableHelper;

    public TablePlotComponentPanel(PlotModelProvider modelProvider) {
        super(new BorderLayout());
        this.modelProvider = modelProvider;
        jTableHelper = JTableHelper.prepareIndexedTable(new ValuesPlotTableModel(modelProvider));
        add(jTableHelper.getPane());
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
