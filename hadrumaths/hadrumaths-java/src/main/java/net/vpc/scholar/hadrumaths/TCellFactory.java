package net.vpc.scholar.hadrumaths;

/**
* Created by vpc on 4/28/14.
*/
public interface TCellFactory<T> {

    T item(int row, int column);
}
