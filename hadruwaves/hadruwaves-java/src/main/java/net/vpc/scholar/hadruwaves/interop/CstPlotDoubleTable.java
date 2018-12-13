package net.vpc.scholar.hadruwaves.interop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CstPlotDoubleTable {
    private CstPlotDoubleColumn[] columns;

    public CstPlotDoubleTable(CstPlotDoubleColumn[] columns) {
        this.columns = columns;
    }

    public CstPlotDoubleColumn[] getColumns() {
        return columns;
    }

    public String[] getTitles() {
        String[] titles=new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            titles[i]=columns[i].getTitle();
        }
        return titles;
    }

    public CstPlotDoubleColumn getColumn(int i) {
        return columns[i];
    }

    public CstPlotDoubleRow getRow(int i) {
        String[] names = new String[columns.length];
        double[] values = new double[columns.length];
        CstPlotDoubleColumn cstPlotDoubleColumn = columns[i];
        for (int j = 0; j < columns.length; j++) {
            names[i] = cstPlotDoubleColumn.getTitle();
            values[i] = cstPlotDoubleColumn.getValues()[j];

        }
        return new CstPlotDoubleRow(names, values);
    }

    public double get(int row, int col) {
        return getColumn(col).getValues()[row];
    }

    public int getColumnsCount() {
        return columns.length;
    }

    public int getRowsCount() {
        return columns.length == 0 ? 0 : columns[0].length();
    }
}
