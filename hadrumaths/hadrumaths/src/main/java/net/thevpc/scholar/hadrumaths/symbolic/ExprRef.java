package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 8/24/14.
 */
public interface ExprRef extends Expr {
    Expr getReference();

}
