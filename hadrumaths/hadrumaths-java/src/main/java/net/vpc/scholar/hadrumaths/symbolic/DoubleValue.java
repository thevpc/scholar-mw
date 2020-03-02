package net.vpc.scholar.hadrumaths.symbolic;

/**
 * Should be a constant of double value in a defined domain.
 * Domain and DefaultDoubleValue are both implementations of this interface
 */
public interface DoubleValue extends NumberExpr {
    double toDouble();
}
