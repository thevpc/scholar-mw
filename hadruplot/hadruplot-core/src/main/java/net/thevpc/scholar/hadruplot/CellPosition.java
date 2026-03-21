package net.thevpc.scholar.hadruplot;

import java.util.Objects;

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

    @Override
    public String toString() {
        String r=row==0?"X":row==1?"Y":row==2?"Z":String.valueOf(row);
        if(column==0){
            return r;
        }
        String c=column==0?"X":column==1?"Y":column==2?"Z":String.valueOf(column);
        return r+"-"+c;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CellPosition that = (CellPosition) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
