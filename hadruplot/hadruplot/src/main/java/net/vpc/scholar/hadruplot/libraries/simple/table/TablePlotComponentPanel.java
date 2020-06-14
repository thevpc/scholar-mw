package net.vpc.scholar.hadruplot.libraries.simple.table;

import net.vpc.common.swings.JTableHelper;
import net.vpc.common.swings.SwingUtilities3;
import net.vpc.scholar.hadruplot.PlotComponentPanel;
import net.vpc.scholar.hadruplot.extension.PlotModelProvider;
import net.vpc.scholar.hadruplot.util.PlotUtils;

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
        return PlotUtils.getOrCreateComponentPopupMenu(jTableHelper.getTable());
    }
}
