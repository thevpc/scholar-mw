package net.vpc.scholar.hadrumaths.internal.hl;

import net.vpc.hadralang.stdlib.DoubleRange;
import net.vpc.hadralang.stdlib.Range;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DomainExpr;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;

public final class HLMaths {
    private HLMaths() {
    }

    public static Domain II(DoubleRange u) {
        return Domain.ofBounds(u.start(), u.end());
    }

    public static Domain II(DoubleRange ux, DoubleRange uy) {
        return Domain.ofBounds(ux.start(), ux.end(), uy.start(), uy.endValue());
    }

    public static Domain II(DoubleRange ux, DoubleRange uy, DoubleRange uz) {
        return Domain.ofBounds(ux.start(), ux.end(), uy.start(), uy.end(), uz.start(), uz.end());
    }

    public static Expr II(Range<Expr> u) {
        if (u.start().getDomain().isUnbounded() && u.end().getDomain().isUnbounded()) {
            if (u.start().isNarrow(ExprType.DOUBLE_NBR) && u.end().isNarrow(ExprType.DOUBLE_NBR)) {
                return Domain.ofBounds(u.start().toDouble(), u.end().toDouble());
            }
        }
        return DomainExpr.ofBounds(u.start(), u.end());
    }

    public static Expr II(Range<Expr> ux, Range<Expr> uy) {
        if (ux.start().isNarrow(ExprType.DOUBLE_NBR) && ux.end().isNarrow(ExprType.DOUBLE_NBR) && uy.start().isNarrow(ExprType.DOUBLE_NBR) && uy.end().isNarrow(ExprType.DOUBLE_NBR)) {
            return Domain.ofBounds(ux.start().toDouble(), ux.end().toDouble(), uy.start().toDouble(), uy.end().toDouble());
        }
        return DomainExpr.ofBounds(ux.start(), ux.end(), uy.start(), uy.end());
    }

    public static Expr II(Range<Expr> ux, Range<Expr> uy, Range<Expr> uz) {
        if (ux.start().isNarrow(ExprType.DOUBLE_NBR) && ux.end().isNarrow(ExprType.DOUBLE_NBR) && uy.start().isNarrow(ExprType.DOUBLE_NBR) && uy.end().isNarrow(ExprType.DOUBLE_NBR) && uz.start().isNarrow(ExprType.DOUBLE_NBR) && uz.end().isNarrow(ExprType.DOUBLE_NBR)) {
            return Domain.ofBounds(ux.start().toDouble(), ux.end().toDouble(), uy.start().toDouble(), uy.end().toDouble(), uz.start().toDouble(), uz.end().toDouble());
        }
        return DomainExpr.ofBounds(ux.start(), ux.end(), uy.start(), uy.end(), uz.start(), uz.end());
    }
}
