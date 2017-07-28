package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;

import java.io.Serializable;

/**
 * Created by vpc on 6/6/14.
 */
public interface DoubleToVector extends Expr, Serializable, DoubleToMatrix, Cloneable {
    Expr getComponent(Axis a);

    int getComponentSize();

    Expr getX() ;

    Expr getY() ;

    Expr getZ() ;
}
