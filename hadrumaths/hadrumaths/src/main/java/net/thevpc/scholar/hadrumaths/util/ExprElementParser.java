package net.thevpc.scholar.hadrumaths.util;

import net.thevpc.nuts.elem.NBinaryOperatorElement;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NUpletElement;
import net.thevpc.nuts.math.NDoubleComplex;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.DoubleExpr;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;

public class ExprElementParser {
    public static NOptional<Expr> parse(NElement value) {
        if (value == null || value.isNull()) {
            return NOptional.ofNamedEmpty("expr");
        }
        switch (value.type()) {
            case INT:
            case DOUBLE:
            case FLOAT:
            case BYTE:
            case LONG:
            case SHORT:
            case BIG_INT:
            case BIG_DECIMAL: {
                return NOptional.of(DoubleExpr.of(value.asDoubleValue().get()));
            }
            case FLOAT_COMPLEX:
            case DOUBLE_COMPLEX:
            case BIG_COMPLEX: {
                NDoubleComplex d = value.asDoubleComplexValue().get();
                return NOptional.of(Complex.of(
                        d.real(),
                        d.imag()
                ));
            }
            case NAME: {
                switch (value.asStringValue().get()) {
                    case "X":
                    case "x":
                        return NOptional.of(Maths.X);
                    case "Y":
                    case "y":
                        return NOptional.of(Maths.X);
                    case "Z":
                    case "z":
                        return NOptional.of(Maths.X);
                }
                return NOptional.of(Maths.param(value.asStringValue().get()));
            }
            case NAMED_UPLET: {
                NUpletElement u = value.asUplet().get();
                int paramsSize = u.params().size();
                if (paramsSize == 1) {
                    NElement p = u.params().get(0);
                    switch (u.name().get().toLowerCase()) {
                        case "cos": {
                            return parse(p).map(Expr::cos);
                        }
                        case "sin": {
                            return parse(p).map(Expr::sin);
                        }
                        case "acosh": {
                            return parse(p).map(Expr::acosh);
                        }
                        case "cosh": {
                            return parse(p).map(Expr::cosh);
                        }
                        case "asinh": {
                            return parse(p).map(Expr::asinh);
                        }
                        case "sinh": {
                            return parse(p).map(Expr::sinh);
                        }
                        case "db": {
                            return parse(p).map(Expr::db);
                        }
                        case "db2": {
                            return parse(p).map(Expr::db2);
                        }
                        case "inv": {
                            return parse(p).map(Expr::inv);
                        }
                        case "acotan": {
                            return parse(p).map(Expr::acotan);
                        }
                        case "atan": {
                            return parse(p).map(Expr::atan);
                        }
                        case "tan": {
                            return parse(p).map(Expr::tan);
                        }
                        case "cotan": {
                            return parse(p).map(Expr::cotan);
                        }
                        case "abs": {
                            return parse(p).map(Expr::abs);
                        }
                        case "conj": {
                            return parse(p).map(Expr::conj);
                        }
                        case "imag": {
                            return parse(p).map(Expr::imag);
                        }
                        case "real": {
                            return parse(p).map(Expr::real);
                        }
                        case "norm": {
                            return parse(p).map(x -> DoubleExpr.of(Maths.norm(x)));
                        }
                        case "log": {
                            return parse(p).map(Expr::log);
                        }
                        case "log10": {
                            return parse(p).map(Expr::log10);
                        }
                        case "narrow": {
                            return parse(p).map(Expr::narrow);
                        }
                        case "sqr": {
                            return parse(p).map(Expr::sqr);
                        }
                        case "sqrt": {
                            return parse(p).map(Expr::sqrt);
                        }
                        case "complex": {
                            return parse(p).map(Expr::toComplex);
                        }
                    }
                }
                break;
            }
            case FLAT_EXPR: {
                NElement expr2 = value.asFlatExpression().get().reshape();
                return parse(expr2);
            }
            case BINARY_OPERATOR: {
                NBinaryOperatorElement bo = value.asBinaryOperator().get();
                NOptional<Expr> o1 = parse(bo.firstOperand());
                NOptional<Expr> o2 = parse(bo.secondOperand());
                if (o1.isPresent() && o2.isPresent()) {
                    switch (bo.operatorSymbol().lexeme()) {
                        case "+": {
                            return NOptional.of(o1.get().plus(o2.get()));
                        }
                        case "-": {
                            return NOptional.of(o1.get().sub(o2.get()));
                        }
                        case "*": {
                            return NOptional.of(o1.get().mul(o2.get()));
                        }
                        case "/": {
                            return NOptional.of(o1.get().div(o2.get()));
                        }
                        case "^": {
                            return NOptional.of(o1.get().pow(o2.get()));
                        }
                        case "**": {
                            return NOptional.of(Maths.scalarProduct(o1.get(), o2.get()));
                        }
                        case "!=": {
                            return NOptional.of(Maths.ne(o1.get(), o2.get()));
                        }
                        case "==": {
                            return NOptional.of(Maths.eq(o1.get(), o2.get()));
                        }
                        case "<": {
                            return NOptional.of(Maths.lt(o1.get(), o2.get()));
                        }
                        case "<=": {
                            return NOptional.of(Maths.lte(o1.get(), o2.get()));
                        }
                        case ">": {
                            return NOptional.of(Maths.gt(o1.get(), o2.get()));
                        }
                        case ">=": {
                            return NOptional.of(Maths.gte(o1.get(), o2.get()));
                        }
                    }
                }
                break;
            }
        }
        return NOptional.ofNamedError("expr");
    }
}
