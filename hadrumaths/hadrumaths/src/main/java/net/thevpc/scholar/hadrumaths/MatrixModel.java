package net.thevpc.scholar.hadrumaths;

/**
 * Created by vpc on 4/28/14.
 */
public interface MatrixModel<T> extends MatrixCell<T> {

    int getColumnCount();

    int getRowCount();
}
