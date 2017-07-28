package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.CubeCellIteratorType;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public class DefaultExprCubeFactory extends AbstractExprCubeFactory {
    public static final ExprCubeFactory INSTANCE = new DefaultExprCubeFactory();

    @Override
    public ExprCube newPreloadedCube(int rows, int columns, int height, ExprCubeCellIterator item) {
        return newPreloadedCube(rows,columns,height,CubeCellIteratorType.FULL, item);
    }

    @Override
    public ExprCube newPreloadedCube(final int rows, final int columns, final int height,CubeCellIteratorType it, ExprCubeCellIterator item) {


        Expr[][] elements = new Expr[rows][columns];
        switch (it) {
            case FULL: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        for (int k = 0; k < height; k++) {
                            elements[i][j] = item.get(i, j,k);
                        }
                    }
                }
                break;
            }

            default: {
                throw new IllegalArgumentException("Unsupported " + it);
            }
        }
        ExprCubeStore store = new ExprCubeStore() {
            Expr[][][] elements = new Expr[rows][columns][height];

            @Override
            public int getColumns() {
                return rows;
            }

            @Override
            public int getRows() {
                return columns;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public Expr get(int row, int col,int h) {
                return elements[row][col][h];
            }

            @Override
            public void set(Expr exp, int row, int col,int h) {
                elements[row][col][h] = exp;
            }
        };
        return new DefaultExprCube(store);

    }

    @Override
    public ExprCube newCachedCube(final int rows, final int columns, final int height, final ExprCubeCellIterator item) {
        ExprCubeStore store = new ExprCubeStore() {
            Expr[][][] elements = new Expr[rows][columns][height];

            @Override
            public int getColumns() {
                return rows;
            }

            @Override
            public int getRows() {
                return columns;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public Expr get(int row, int col,int h) {
                Expr v = elements[row][col][h];
                if(v==null){
                    v=item.get(row,col,h);
                    elements[row][col][h]=v;
                }
                return v;
            }

            @Override
            public void set(Expr exp, int row, int col,int h) {
                elements[row][col][h] = exp;
            }
        };
        return new DefaultExprCube(store);
    }

    @Override
    public ExprCube newUnmodifiableCube(final int rows, final int columns, final int height, final ExprCubeCellIterator item) {
        ExprCubeStore store = new ExprCubeStore() {
            @Override
            public int getColumns() {
                return rows;
            }

            @Override
            public int getRows() {
                return columns;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public Expr get(int row, int col,int h) {
                return item.get(row, col,h);
            }

            @Override
            public void set(Expr exp, int row, int col,int h) {
                throw new IllegalArgumentException("Unmodifiable");
            }
        };
        return new DefaultExprCube(store);
    }
}
