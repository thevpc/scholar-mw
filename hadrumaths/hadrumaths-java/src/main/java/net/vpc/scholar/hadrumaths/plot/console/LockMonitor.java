package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.common.swings.JInternalFrameHelper;
import net.vpc.common.swings.JTableHelper;
import net.vpc.common.swings.SwingUtilities3;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.concurrent.AppLock;
import net.vpc.scholar.hadrumaths.concurrent.AppLockEvent;
import net.vpc.scholar.hadrumaths.concurrent.AppLockListener;
import net.vpc.scholar.hadrumaths.concurrent.AppLockManager;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
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
        JTableHelper jTableHelper = SwingUtilities3.createIndexedTable(lockModel);
        jTableHelper.getPane().setPreferredSize(new Dimension(200, 50));

        table = jTableHelper.getTable();

        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {


            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                if (value instanceof Date) {
                    value = Maths.UNIVERSAL_DATE_TIME_FORMAT.format(value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
            }
        };

        table.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer);

        TableCellRenderer tableCellRenderer2 = new DefaultTableCellRenderer() {

            private Color getDefaultBG(JTable table,
                                       Object value, boolean isSelected, boolean hasFocus,
                                       int row, int column) {
                Color bg = null;

                JTable.DropLocation dropLocation = table.getDropLocation();
                if (dropLocation != null
                        && !dropLocation.isInsertRow()
                        && !dropLocation.isInsertColumn()
                        && dropLocation.getRow() == row
                        && dropLocation.getColumn() == column) {

                    bg = DefaultLookup.getColor(this, ui, "Table.dropCellBackground");

                    isSelected = true;
                }

                if (isSelected) {
                    bg = (bg == null ? table.getSelectionBackground() : bg);
                } else {
                    Color unselectedBackground = getUnselectedBackground();
                    Color background = unselectedBackground != null
                            ? unselectedBackground
                            : table.getBackground();
                    if (background == null || background instanceof javax.swing.plaf.UIResource) {
                        Color alternateColor = DefaultLookup.getColor(this, ui, "Table.alternateRowColor");
                        if (alternateColor != null && row % 2 != 0) {
                            background = alternateColor;
                        }
                    }
                    if (background != null) {
                        bg = (background);
                    }
                }


                if (hasFocus) {
                    if (!isSelected && table.isCellEditable(row, column)) {
                        Color col;
                        col = DefaultLookup.getColor(this, ui, "Table.focusCellBackground");
                        if (col != null) {
                            bg = (col);
                        }
                    }
                }
                return bg;
            }

            private Color getUnselectedBackground() {
                try {
                    Field unselectedBackgroundField = getClass().getSuperclass().getDeclaredField("unselectedBackground");
                    unselectedBackgroundField.setAccessible(true);
                    return (Color) unselectedBackgroundField.get(this);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Unexpected");
                }
            }

            private Color getUnselectedForeground() {
                try {
                    Field unselectedForegroundField = getClass().getSuperclass().getDeclaredField("unselectedForeground");
                    unselectedForegroundField.setAccessible(true);
                    return (Color) unselectedForegroundField.get(this);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Unexpected");
                }
            }

            private Color getDefaultFG(JTable table,
                                       Object value, boolean isSelected, boolean hasFocus,
                                       int row, int column) {
                Color fg = null;

                JTable.DropLocation dropLocation = table.getDropLocation();
                if (dropLocation != null
                        && !dropLocation.isInsertRow()
                        && !dropLocation.isInsertColumn()
                        && dropLocation.getRow() == row
                        && dropLocation.getColumn() == column) {

                    fg = DefaultLookup.getColor(this, ui, "Table.dropCellForeground");

                    isSelected = true;
                }

                if (isSelected) {
                    fg = (fg == null ? table.getSelectionForeground()
                            : fg);
                } else {
                    Color unselectedForeground = getUnselectedForeground();
                    fg = (unselectedForeground != null
                            ? unselectedForeground
                            : table.getForeground());
                }


                if (hasFocus) {
                    if (!isSelected && table.isCellEditable(row, column)) {
                        Color col;
                        col = DefaultLookup.getColor(this, ui, "Table.focusCellForeground");
                        if (col != null) {
                            fg = (col);
                        }
                    }
                } else {
                }
                return fg;
            }

            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Color bg = getDefaultBG(table, value, isSelected, hasFocus, row, column);
                Color fg = getDefaultFG(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Number && ((Number) value).intValue() > 1) {
                    if (isSelected) {
                        bg = (new Color(230, 135, 230));
//                        fg = (Color.YELLOW);
                    } else {
                        bg = (new Color(230, 135, 121));
//                        fg = (Color.BLACK);
                    }
                }
                super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
                setHorizontalTextPosition(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(bg);
                setForeground(fg);
                return this;
            }
        };

        table.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer2);


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

    public JInternalFrameHelper getFrame() {
        return new JInternalFrameHelper(frame);
    }

    public void setFrame(JInternalFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == unlockButton) {
            //
            AppLockInfo a = lockModel.getAppLock(table.getSelectionModel().getLeadSelectionIndex());
            if (a != null) {
                try {
                    a.lock.forceRelease();
                } catch (Exception ex) {

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
