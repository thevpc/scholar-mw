package net.vpc.scholar.hadrumaths.plot;

/**
 * Created by vpc on 6/4/14.
 */
public class CellPosition {
    public final int row;
    public final int column;

    public CellPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
