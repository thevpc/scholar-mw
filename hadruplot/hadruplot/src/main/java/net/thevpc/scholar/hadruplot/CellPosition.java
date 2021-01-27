package net.thevpc.scholar.hadruplot;

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
        String r=row==0?"X":row==1?"Y":row==0?"Z":String.valueOf(row);
        if(column==0){
            return r;
        }
        String c=column==0?"X":column==1?"Y":column==0?"Z":String.valueOf(column);
        return r+"-"+c;
    }
}
