package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.locks.components;

import net.vpc.common.swings.JTableHelper;
import net.vpc.common.swings.SwingUtilities3;
import net.vpc.scholar.hadrumaths.concurrent.AppLockManager;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWUtils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 janv. 2007 12:13:07
 */
public class HWLockManagerComponent extends JPanel implements ActionListener {
    //    private JButton unlockButton;
    private final Thread thread = null;
    private final JTable table;
    private final LocksTableModel lockModel;
    private Box list;
    private HadruwavesStudio studio;

    public HWLockManagerComponent(HadruwavesStudio studio) {
        this.studio = studio;
        list = Box.createVerticalBox();
        this.setLayout(new BorderLayout());

        this.add(list, BorderLayout.NORTH);
        lockModel = new LocksTableModel();
        AppLockManager.getInstance().addListener(lockModel);
        JTableHelper jTableHelper = SwingUtilities3.createIndexedTable(lockModel);
        jTableHelper.getPane().setPreferredSize(new Dimension(200, 50));

        table = jTableHelper.getTable();


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int r = table.rowAtPoint(e.getPoint());
                    int c = table.columnAtPoint(e.getPoint());
                    if (c == 4 && r >= 0) {
                        int i = table.getSelectionModel().getLeadSelectionIndex();
                        if (i == r) {
                            AppLockInfo a = lockModel.getAppLock(i);
                            if (a != null) {
                                if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog((Component) studio.app().mainWindow().get().component(),
                                        "Removing a lock may produce unexpected results. Are you sure?",
                                        "Attention",
                                        JOptionPane.OK_CANCEL_OPTION
                                )) {
                                    try {
                                        a.lock.forceRelease();
                                    } catch (Exception ex) {
                                        //
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        this.add(jTableHelper.getPane(), BorderLayout.CENTER);

//        JToolBar south = new JToolBar(JToolBar.HORIZONTAL);
//        south.setFloatable(false);
//        this.add(south, BorderLayout.SOUTH);
//        unlockButton = new JButton("");
//
//        south.add(Box.createVerticalGlue());
//        south.add(Box.createHorizontalGlue());
//        south.add(unlockButton);
//        unlockButton.addActionListener(this);
        HWUtils.onLookChanged(studio, () -> onLookChanged());
        onLookChanged();
    }

    public void onLookChanged() {
//        unlockButton.setIcon(studio.app().iconSet().icon("Remove").get());
        TableCellRenderer tableCellRenderer = new LocksDefaultTableCellRenderer(studio.app());
        table.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer);
        TableCellRenderer tableCellRenderer2 = new LockDefaultTableCellRenderer();
        table.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer2);
        TableCellRenderer tableCellRendererAction = new ActionLocksDefaultTableCellRenderer(studio.app());
        table.getColumnModel().getColumn(4).setCellRenderer(tableCellRendererAction);
    }


    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
//        if (source == unlockButton) {
//            //
//            AppLockInfo a = lockModel.getAppLock(table.getSelectionModel().getLeadSelectionIndex());
//            if (a != null) {
//                try {
//                    a.lock.forceRelease();
//                } catch (Exception ex) {
//
//                }
//            }
//        }
    }

    public String getTitle() {
        return "Locks Monitor";
    }

}
