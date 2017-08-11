package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.util.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
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
    private JInternalFrame frame;

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

    public JInternalFrame getFrame() {
        return frame;
    }

    public void setFrame(JInternalFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == unlockButton) {
            //
            AppLockInfo a = lockModel.getAppLock(table.getSelectionModel().getLeadSelectionIndex());
            if(a!=null){
                try {
                    a.lock.forceRelease();
                }catch (Exception ex){

                }
            }
        }
    }


    private static class AppLockInfo {
        AppLock lock;
        long hits;
        Date time;
    }
    private static class LocksTableModel extends AbstractTableModel implements AppLockListener {
        final List<AppLockInfo> files = new ArrayList<>();


        public AppLockInfo getAppLock(int index) {
            synchronized (files) {
                if (index >= 0 && index < files.size()) {
                    return files.get(index);
                }
            }
            return null;
        }
        public void add(AppLock lock) {
            synchronized (files) {
                for (int i = files.size() - 1; i >= 0; i--) {
                    AppLockInfo appLockInfo = files.get(i);
                    AppLock file = appLockInfo.lock;
                    if (file.equals(lock)) {
                        appLockInfo.hits++;
                        appLockInfo.time=new Date();
                        fireTableRowsUpdated(i, i);
                        return;
                    }
                }
                int a = files.size();
                AppLockInfo info=new AppLockInfo();
                info.lock=lock;
                info.hits=1;
                info.time=new Date();
                files.add(info);
                fireTableRowsInserted(a, a);
            }
        }

        public void remove(AppLock lock) {
            synchronized (files) {
                for (int i = files.size() - 1; i >= 0; i--) {
                    AppLockInfo appLockInfo = files.get(i);
                    AppLock file = appLockInfo.lock;
                    if (file.equals(lock)) {
                        files.remove(i);
                        fireTableRowsDeleted(i, i);
                        return;
                    }
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
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            synchronized (files) {
                if(rowIndex<0 || rowIndex>=files.size()){
                    return null;
                }
                AppLockInfo appLock = files.get(rowIndex);
                if (columnIndex == 0) {
                    return appLock.lock.getName();
                }
                if (columnIndex == 1) {
                    return appLock.lock.toString();
                }
                if (columnIndex == 2) {
                    return appLock.hits;
                }
                if (columnIndex == 3) {
                    return appLock.time;
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
            if(column==2){
                return "Hits";
            }
            if(column==3){
                return "Last Hit";
            }
            return super.getColumnName(column);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return String.class;
            }
            if (columnIndex == 1) {
                return String.class;
            }
            if (columnIndex == 2) {
                return Integer.class;
            }
            if (columnIndex == 3) {
                return Date.class;
            }
            return super.getColumnClass(columnIndex);
        }
    }
}
