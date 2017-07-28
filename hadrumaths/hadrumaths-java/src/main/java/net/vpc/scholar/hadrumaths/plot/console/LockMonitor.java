package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.util.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 janv. 2007 12:13:07
 */
public class LockMonitor extends JPanel implements ActionListener {
    Box list;
    JButton unlockButton;
    private PlotConsole plotConsole;
    private Thread thread = null;
    private JTable table;
    private LocksTableModel lockModel;

    public LockMonitor(PlotConsole plotConsole) {
        this.plotConsole = plotConsole;
        list = Box.createVerticalBox();
        this.setLayout(new BorderLayout());

        this.add(list, BorderLayout.NORTH);
        lockModel = new LocksTableModel();
        AppLockManager.getInstance().addListener(lockModel);
        JTableHelper jTableHelper = JTableHelper.prepareIndexedTable(lockModel);
        jTableHelper.getPane().setPreferredSize(new Dimension(200, 50));

        table = jTableHelper.getTable();
        this.add(jTableHelper.getPane(), BorderLayout.CENTER);

        JToolBar south = new JToolBar(JToolBar.HORIZONTAL);
        south.setFloatable(false);
        this.add(south, BorderLayout.SOUTH);
        unlockButton = new JButton("-");
        unlockButton.setIcon(new ImageIcon(getClass().getResource("LessTasks.gif")));

        south.add(Box.createVerticalGlue());
        south.add(Box.createHorizontalGlue());
        south.add(unlockButton);
        unlockButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == unlockButton) {
            //
            AppLock a = lockModel.getAppLock(table.getSelectionModel().getLeadSelectionIndex());
            if(a!=null){
                try {
                    a.forceRelease();
                }catch (Exception ex){

                }
            }
        }
    }


    private static class LocksTableModel extends AbstractTableModel implements AppLockListener {
        final List<AppLock> files = new ArrayList<>();


        public AppLock getAppLock(int index) {
            synchronized (files) {
                if (index >= 0 && index < files.size()) {
                    return files.get(index);
                }
            }
            return null;
        }
        public void add(AppLock lock) {
            synchronized (files) {
                for (int i = files.size() - 1; i == 0; i--) {
                    AppLock file = files.get(i);
                    if (file.equals(lock)) {
                        return;
                    }
                }
                int a = files.size();
                files.add(lock);
                fireTableRowsInserted(a, a);
            }
        }

        public void remove(AppLock lock) {
            synchronized (files) {
                int p = files.indexOf(lock);
                if (p >= 0) {
                    files.remove(p);
                    fireTableRowsDeleted(p, p);
                }
            }
        }

        @Override
        public void onLockAcquired(AppLockEvent event) {
            add(event.getSource());
        }

        @Override
        public void onLockDetected(AppLockEvent event) {
            add(event.getSource());
        }

        @Override
        public void onLockReleased(AppLockEvent event) {
            remove(event.getSource());
        }

        @Override
        public int getRowCount() {
            synchronized (files) {
                return files.size();
            }
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            synchronized (files) {
                if(rowIndex<=0 || rowIndex>=files.size()){
                    return null;
                }
                AppLock appLock = files.get(rowIndex);
                if (columnIndex == 0) {
                    return appLock.getName();
                }
                if (columnIndex == 1) {
                    return appLock.toString();
                }
                return appLock.toString();
            }
        }

        @Override
        public String getColumnName(int column) {
            if(column==0){
                return "Lock Name";
            }
            if(column==1){
                return "Lock Desc";
            }
            return super.getColumnName(column);
        }
    }
}
