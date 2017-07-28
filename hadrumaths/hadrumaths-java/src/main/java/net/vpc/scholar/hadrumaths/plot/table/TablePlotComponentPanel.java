package net.vpc.scholar.hadrumaths.plot.table;

import net.vpc.scholar.hadrumaths.ValuesPlotTableModel;
import net.vpc.scholar.hadrumaths.plot.PlotComponentPanel;
import net.vpc.scholar.hadrumaths.plot.PlotModelProvider;
import net.vpc.scholar.hadrumaths.util.swingext.SimpleRowHeaderRenderer;

import javax.swing.*;
import java.awt.*;

public class TablePlotComponentPanel extends JPanel implements PlotComponentPanel {
    private PlotModelProvider modelProvider;
    private JScrollPane pane;
    private JTable table;

    public TablePlotComponentPanel(PlotModelProvider modelProvider) {
        super(new BorderLayout());
        this.modelProvider = modelProvider;
        table = new JTable(new ValuesPlotTableModel(modelProvider));
        pane = new JScrollPane(table);
        SimpleRowHeaderRenderer r = new SimpleRowHeaderRenderer(table);
        r.install();
        add(pane);
    }

    @Override
    public JComponent toComponent() {
        return this;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        JPopupMenu componentPopupMenu = table.getComponentPopupMenu();
        if(componentPopupMenu==null){
            componentPopupMenu=new JPopupMenu();
            table.setComponentPopupMenu(componentPopupMenu);
        }
        return componentPopupMenu;
    }
}
