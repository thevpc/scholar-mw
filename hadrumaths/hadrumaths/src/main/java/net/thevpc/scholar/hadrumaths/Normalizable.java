package net.thevpc.scholar.hadrumaths;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public interface Normalizable {
    double getDistance(Normalizable other);

    double norm();
}
