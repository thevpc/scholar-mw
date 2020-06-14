package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 8/24/14.
 */
public interface ExprRef extends Expr {
    Expr getReference();

}
