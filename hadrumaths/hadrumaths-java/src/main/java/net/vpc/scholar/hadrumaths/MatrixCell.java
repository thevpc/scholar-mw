package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 4/28/14.
 */
public interface MatrixCell extends TMatrixCell<Complex> {

    Complex get(int row, int column);
}
