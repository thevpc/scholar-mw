package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.locks.components;

import net.thevpc.common.swing.SwingUtilities3;
import net.thevpc.scholar.hadrumaths.concurrent.AppLock;
import net.thevpc.scholar.hadrumaths.concurrent.AppLockEvent;
import net.thevpc.scholar.hadrumaths.concurrent.AppLockListener;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class LocksTableModel extends AbstractTableModel implements AppLockListener {
    final List<AppLockInfo> files = new ArrayList<>();


    public AppLockInfo getAppLock(int index) {
        synchronized (files) {
            if (index >= 0 && index < files.size()) {
                return files.get(index);
            }
        }
        return null;
    }

    @Override
    public void onLockAcquired(AppLockEvent event) {
        add(event.getSource());
    }

    public void add(AppLock lock) {
        SwingUtilities3.invokeLater(new Runnable() {
            @Override
            public void run() {
                synchronized (files) {
                    for (int i = files.size() - 1; i >= 0; i--) {
                        AppLockInfo appLockInfo = files.get(i);
                        AppLock file = appLockInfo.lock;
                        if (file.equals(lock)) {
                            appLockInfo.hits++;
                            if (appLockInfo.hits > 1) {
                                UIManager.getLookAndFeel().provideErrorFeedback(null);
                            }
                            appLockInfo.time = new Date();
                            fireTableRowsUpdated(i, i);
                            return;
                        }
                    }
                    int a = files.size();
                    AppLockInfo info = new AppLockInfo();
                    info.lock = lock;
                    info.hits = 1;
                    info.time = new Date();
                    files.add(info);
                    fireTableRowsInserted(a, a);
                }
            }
        });
    }

    @Override
    public void onLockDetected(AppLockEvent event) {
        add(event.getSource());
    }

    @Override
    public void onLockReleased(AppLockEvent event) {
        remove(event.getSource());
    }

    public void remove(AppLock lock) {
        SwingUtilities3.invokeLater(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    @Override
    public int getRowCount() {
        synchronized (files) {
            return files.size();
        }
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        synchronized (files) {
            if (rowIndex < 0 || rowIndex >= files.size()) {
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
            if (columnIndex == 4) {
                return null;
            }
            return appLock.toString();
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "Lock Name";
        }
        if (column == 1) {
            return "Lock Desc";
        }
        if (column == 2) {
            return "Hits";
        }
        if (column == 3) {
            return "Last Hit";
        }
        if (column == 4) {
            return "";
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
        if (columnIndex == 4) {
            return Object.class;
        }
        return super.getColumnClass(columnIndex);
    }
}
