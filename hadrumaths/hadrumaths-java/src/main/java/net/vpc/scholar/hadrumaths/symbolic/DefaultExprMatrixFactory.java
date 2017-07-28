package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.CellIteratorType;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public class DefaultExprMatrixFactory extends AbstractExprMatrixFactory{
    public static final ExprMatrixFactory INSTANCE=new DefaultExprMatrixFactory();


    @Override
    public ExprMatrix newCachedMatrix(final int rows, final int columns, final CellIteratorType it, final ExprCellIterator item) {
        ExprMatrixStore store = new ExprMatrixStore() {
            Expr[][] elements = new Expr[rows][columns];

            @Override
            public int getColumnsDimension() {
                return rows;
            }

            @Override
            public int getRows() {
                return columns;
            }

            @Override
            public Expr get(int row, int col) {

                Expr val = elements[row][col];
                if(val==null) {
                    switch (it){
                        case FULL:{
                            val=item.get(row,col);
                            break;
                        }
                        case SYMETRIC:{
                            if(col>=row) {
                                val = get(row, col);
                            }else{
                                val = item.get(col,row);
                            }
                            break;
                        }
                        case HERMITIAN:{
                            if(col>row) {
                                val = new Conj(get(row, col));
                            }else{
                                val = item.get(col,row);
                            }
                            break;
                        }
                        case DIAGONAL:{
                            if(col==row){
                                val = item.get(col,row);
                            }else{
                                val=Complex.ZERO;
                            }
                            break;
                        }
                        default:{
                            throw new IllegalArgumentException("Unsupported");
                        }
                    }
                    elements[row][col]=val;
                }
                return val;
            }

            @Override
            public void set(Expr exp, int row, int col) {
                elements[row][col]=exp;
            }
        };
        return new DefaultExprMatrix(store);

    }



    @Override
    public ExprMatrix newPreloadedMatrix(final int rows, final int columns, CellIteratorType it,ExprCellIterator item) {


        Expr[][] elements = new Expr[rows][columns];
        switch (it) {
            case FULL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        elements[i][j]=item.get(i, j);
                    }
                }
                break;
            }
            case DIAGONAL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        elements[i][j]=Complex.ZERO;
                    }
                    elements[i][i]=(item.get(i, i));
                    for (int j = i + 1; j < columns; j++) {
                        elements[i][j]=Complex.ZERO;
                    }
                }
                break;
            }
            case SYMETRIC: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        elements[i][j]=elements[j][i];
                    }
                    for (int j = i; j < columns; j++) {
                        elements[i][j]=item.get(i, j);
                    }
                }
                break;
            }
            case HERMITIAN: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < i; j++) {
                        elements[i][j]=new Conj(elements[j][i]);
                    }
                    for (int j = i; j < columns; j++) {
                        elements[i][j]=item.get(i, j);
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + it);
            }
        }
        ExprMatrixStore store = new ExprMatrixStore() {
            Expr[][] elements = new Expr[rows][columns];

            @Override
            public int getColumnsDimension() {
                return rows;
            }

            @Override
            public int getRows() {
                return columns;
            }

            @Override
            public Expr get(int row, int col) {
                return elements[row][col];
            }

            @Override
            public void set(Expr exp, int row, int col) {
                elements[row][col]=exp;
            }
        };
        return new DefaultExprMatrix(store);

    }

    @Override
    public ExprMatrix newUnmodifiableMatrix(final int rows, final int columns, final ExprCellIterator item) {
        ExprMatrixStore store = new ExprMatrixStore() {
            @Override
            public int getColumnsDimension() {
                return rows;
            }

            @Override
            public int getRows() {
                return columns;
            }

            @Override
            public Expr get(int row, int col) {
                return item.get(row,col);
            }

            @Override
            public void set(Expr exp, int row, int col) {
                throw new IllegalArgumentException("Unmodifiable");
            }
        };
        return new DefaultExprMatrix(store);
    }
}
