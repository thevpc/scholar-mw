package net.vpc.scholar.hadrumaths.symbolic.old;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 22:06:55
 */
public class SymMatrix extends SymAbstractExpression {
    private SymExpression[][] value;
    private int rows;
    private int columns;
    public static final SymUtils.BoxFormat MATRIX_FORMAT = new SymUtils.BoxFormat().setLeft("| ").setRight(" |").setColumnsSeparator(" ");


    public SymMatrix(SymExpression[][] value) {
        this.value = value;
        rows = value.length;
        columns = rows == 0 ? 0 : value[0].length;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public SymExpression[][] getValue() {
        return value;
    }

    public SymExpression diff(String varName) {
        SymExpression[][] d = new SymExpression[rows][columns];
        for (int i = 0; i < d.length; i++) {
            SymExpression[] symExpressions = d[i];
            for (int j = 0; j < symExpressions.length; j++) {
                symExpressions[j] = value[i][j].diff(varName);
            }
        }
        return new SymMatrix(d);
    }

    public SymExpression eval(SymContext context) {
        SymExpression[][] d = new SymExpression[rows][columns];
        for (int i = 0; i < d.length; i++) {
            SymExpression[] symExpressions = d[i];
            for (int j = 0; j < symExpressions.length; j++) {
                symExpressions[j] = value[i][j].eval(context);
            }
        }
        if (d.length == 1 && d[0].length == 0) {
            return d[0][0];
        }
        return new SymMatrix(d);
    }

    public String toString(SymStringContext context) {
        String[][] s = new String[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                s[i][j] = value[i][j].toString(context);
            }
        }
        return SymUtils.format(MATRIX_FORMAT, s);
    }
}
