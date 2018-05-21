package net.vpc.scholar.hadruwaves.studio.standalone.editors;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.vpc.scholar.hadruwaves.studio.standalone.TMWLabApplication;
import net.vpc.scholar.hadrumaths.io.HFile;
import net.vpc.scholar.hadruwaves.mom.MomCache;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 11 mars 2005
 * Time: 23:12:42
 * To change this template use File | Settings | File Templates.
 */
public class BuildCacheList extends JTable{
    public BuildCacheList(MomCache[] buildCaches) {
        super(new BuildCacheListModel(buildCaches));
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==2){
                   int r=BuildCacheList.this.getSelectedRow();
                    if(r>=0){
                        BuildCacheListModel m=(BuildCacheListModel) getModel();
                        HFile folder=m.buildCaches[r].getFolder();
                        try {
                            TMWLabApplication.openInShell(new File(folder.getNativeLocalFile()),null);
                            return;
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}

class BuildCacheListModel implements TableModel{
    MomCache[] buildCaches;
    private String[] cols=new String[]{};
    public BuildCacheListModel(MomCache[] buildCaches) {
        this.buildCaches=buildCaches;
    }

    public int getRowCount() {
        return buildCaches.length;
    }

    public int getColumnCount() {
        return cols.length;
    }

    public String getColumnName(int columnIndex) {
        return cols[columnIndex].toString();
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        MomCache c=buildCaches[rowIndex];
        return c.parseCacheValue(cols[columnIndex]);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    public void addTableModelListener(TableModelListener l) {

    }

    public void removeTableModelListener(TableModelListener l) {

    }
}

