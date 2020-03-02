package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;
import net.vpc.scholar.hadruwaves.project.UnitTypes;
import net.vpc.scholar.hadruwaves.project.HWSolution;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class HWSProjectUnitsTool extends JPanel {
    private HadruwavesStudioV2 application;
    private UnitsTableModel dataModel;

    public HWSProjectUnitsTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        application.proc().solution().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                solutionUpdated();
            }
        });
        DefaultCellEditor dimUnitValues = new DefaultCellEditor(new JComboBox(UnitTypes.Dimension.values()));
        DefaultCellEditor freqUnitValues = new DefaultCellEditor(new JComboBox(UnitTypes.Frequency.values()));
        JTable comp = new JTable() {
            //  Determine editor to be used by row
            public TableCellEditor getCellEditor(int row, int column) {
                int modelColumn = convertColumnIndexToModel(column);
                if (modelColumn == 1) {
                    if (row == 0) {
                        return dimUnitValues;
                    }
                    if (row == 1) {
                        return freqUnitValues;
                    }
                }
                return super.getCellEditor(row, column);
            }
        };
        dataModel = new UnitsTableModel(application);
        comp.setModel(dataModel);
        add(comp);
    }

    private void solutionUpdated() {
        dataModel.fireTableRowsUpdated(0, 1);
    }

    private static class UnitsTableModel extends AbstractTableModel {
        private final HadruwavesStudioV2 application;

        public UnitsTableModel(HadruwavesStudioV2 application) {
            this.application = application;
        }

        @Override
        public int getRowCount() {
            if (application.proc().solution().get() == null) {
                return 0;
            }
            return 2;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            HWSolution hwSolution = application.proc().solution().get();
            HWProject p = hwSolution==null?null:hwSolution.activeProject().get();
            switch (rowIndex) {
                case 0: {
                    switch (columnIndex) {
                        case 0: {
                            return "Dimension";
                        }
                        case 1: {
                            if (p == null) {
                                return null;
                            }
                            return p.dimensionUnit().get();
                        }
                    }
                }
                case 1: {
                    switch (columnIndex) {
                        case 0: {
                            return "Frequency";
                        }
                        case 1: {
                            if (p == null) {
                                return null;
                            }
                            return p.frequencyUnit().get();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1 && application.proc().solution().get() != null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            HWSolution hwSolution = application.proc().solution().get();
            HWProject p = hwSolution==null?null:hwSolution.activeProject().get();
            switch (rowIndex) {
                case 0: {
                    switch (columnIndex) {
                        case 1: {
                            if (p == null) {
                                return;
                            }
                            p.dimensionUnit().set((UnitTypes.Dimension) aValue);
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (columnIndex) {
                        case 1: {
                            if (p == null) {
                                return;
                            }
                            p.frequencyUnit().set((UnitTypes.Frequency) aValue);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
}
