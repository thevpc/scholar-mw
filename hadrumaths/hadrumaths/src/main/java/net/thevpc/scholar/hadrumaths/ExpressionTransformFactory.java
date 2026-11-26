package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.reflect.NClassMap;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.thevpc.scholar.hadrumaths.symbolic.NumberExpr;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.AbstractDoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.XX;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.YY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.ZZ;
import net.thevpc.scholar.hadrumaths.transform.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author vpc
 */
public class ExpressionTransformFactory extends AbstractFactory {

    private static final NClassMap<Object, Map<Class, ExpressionTransformer>> expressionToExpressionTransformers
            = NClassMap.of(Expr.class, (Class) Map.class);

    public static void setExpressionTransformer(Class<? extends Expr> expressionClass, Class<? extends ExpressionTransform> transformerClass, ExpressionTransformer expressionTransformer) {
        if (expressionTransformer == null) {
            getExpressionTransformerMap(expressionClass).remove(transformerClass);
        } else {
            getExpressionTransformerMap(expressionClass).put(transformerClass, expressionTransformer);
        }
    }

    private static Map<Class, ExpressionTransformer> getExpressionTransformerMap(Class<? extends Expr> expressionClass) {
        Map<Class, ExpressionTransformer> t = expressionToExpressionTransformers.getExact(expressionClass);
        if (t == null) {
            t = new HashMap<Class, ExpressionTransformer>();
            expressionToExpressionTransformers.put(expressionClass, t);
        }
        return t;
    }

    /**
     * Applies <code>transform</code> to <code>e</code>:
     * <ol>
     *   <li>a structure pass, which uses the registered per-node transformers to rebuild the
     *       tree (they recurse with {@link #transformStructure});</li>
     *   <li>one payload application on the result ({@link #applyPayload}).</li>
     * </ol>
     * The structure pass never applies the payload (so a translation is not applied once per
     * tree level), and leaves without a registered transformer are left unchanged.
     */
    public static <T extends Expr> T transform(Expr e, ExpressionTransform transform) {
        if (e == null) {
            return null;
        }
        if (transform == null) {
            return (T) e;
        }
        Expr structured = transformStructure(e, transform);
        return (T) applyPayload(structured, transform);
    }

    /** Structure-only pass: registered transformers rebuild the node, no payload is applied. */
    public static Expr transformStructure(Expr e, ExpressionTransform transform) {
        ExpressionTransformer t = findExpressionTransformer(e.getClass(), transform.getClass());
        if (t == null) {
            return e;
        }
        return t.transform(e, transform);
    }

    private static ExpressionTransformer findExpressionTransformer(Class<? extends Expr> expressionClass, Class<? extends ExpressionTransform> transformerClass) {
        for (Map<Class, ExpressionTransformer> t2 : expressionToExpressionTransformers.findMatches(expressionClass)) {
            ExpressionTransformer found = t2.get(transformerClass);
            if (found == null) {
                found = t2.get(ExpressionTransform.class);
            }
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public static ExpressionTransformer getExpressionTransformer(Class<? extends Expr> expressionClass, Class<? extends ExpressionTransform> transformerClass) {
        ExpressionTransformer t = findExpressionTransformer(expressionClass, transformerClass);
        if (t == null) {
            throw new NoSuchElementException("Missing " + transformerClass + " for " + expressionClass);
        }
        return t;
    }

    /**
     * Applies the payload of <code>transform</code> once, on the (already rebuilt) expression.
     * <ul>
     *   <li>domain restriction and scalar multiplication are applied at the root;</li>
     *   <li>translation / symmetry / opposite are geometric and are applied leaf by leaf
     *       (each shape leaf knows how to move its own domain and coefficients).</li>
     * </ul>
     */
    public static Expr applyPayload(Expr e, ExpressionTransform transform) {
        if (e == null || transform == null) {
            return e;
        }
        if (transform instanceof DomainXYMultiplierTransform) {
            Domain d = ((DomainXYMultiplierTransform) transform).getValue();
            return d == null ? e : e.mul(d);
        }
        if (transform instanceof DoubleMultiplierTransform) {
            return e.mul(((DoubleMultiplierTransform) transform).getValue());
        }
        if (transform instanceof TranslateTransform) {
            TranslateTransform t = (TranslateTransform) transform;
            if (t.getValueX() == 0 && t.getValueY() == 0) {
                return e;
            }
            return translate(e, t.getValueX(), t.getValueY());
        }
        if (transform instanceof SymmetryTransform) {
            SymmetryTransform t = (SymmetryTransform) transform;
            Axis axis = t.getAxis();
            Domain target = t.getDomain();
            double center;
            if (target != null) {
                center = axis == Axis.X ? target.xmiddle() : target.ymiddle();
            } else {
                Domain d = e.getDomain();
                center = axis == Axis.X ? d.xmiddle() : d.ymiddle();
            }
            if (Double.isNaN(center) || Double.isInfinite(center)) {
                center = 0;
            }
            return mirror(e, axis, center);
        }
        if (transform instanceof OppositeTransform) {
            return mirror(e, ((OppositeTransform) transform).getValue(), 0.0);
        }
        // plain ExpressionTransform: structure only
        return e;
    }

    /**
     * Returns f(x - dx, y - dy). Shape leaves move their own domain and coefficients;
     * coordinate leaves are substituted; constants are unchanged; composites are rebuilt.
     */
    public static Expr translate(Expr e, double dx, double dy) {
        if (e instanceof XX) {
            return dx == 0 ? e : Maths.X.sub(dx);
        }
        if (e instanceof YY) {
            return dy == 0 ? e : Maths.Y.sub(dy);
        }
        if (e instanceof ZZ) {
            return e;
        }
        if (e instanceof AbstractDoubleToDouble) {
            try {
                return ((AbstractDoubleToDouble) e).translate(dx, dy);
            } catch (IllegalArgumentException ignored) {
                // falls through to the generic handling below
            }
        }
        List<Expr> children = e.getChildren();
        if (children.isEmpty()) {
            // a constant (or an opaque leaf) does not change under a translation
            return e;
        }
        Expr[] updated = new Expr[children.size()];
        boolean changed = false;
        for (int i = 0; i < updated.length; i++) {
            Expr si = children.get(i);
            updated[i] = translate(si, dx, dy);
            changed |= updated[i] != si;
        }
        if (!changed) {
            return e;
        }
        return ExprDefaults.copyProperties(e, e.newInstance(updated));
    }

    /**
     * Returns f(reflected about <code>axis</code> at <code>center</code>).
     * Leaves that are invariant on the axis only move their domain; shape leaves use their
     * own mirror (and therefore their own coefficient signs); composites are rebuilt.
     */
    public static Expr mirror(Expr e, Axis axis, double center) {
        if (e instanceof XX) {
            return axis == Axis.X ? Maths.expr(2 * center).sub(Maths.X) : e;
        }
        if (e instanceof YY) {
            return axis == Axis.Y ? Maths.expr(2 * center).sub(Maths.Y) : e;
        }
        if (e instanceof ZZ) {
            return e;
        }
        if (e instanceof NumberExpr) {
            // constants and parameters: the value does not depend on the coordinates,
            // so a reflection only moves the support
            return relocate(e, axis, center);
        }
        if (e instanceof AbstractDoubleToDouble) {
            try {
                if (axis == Axis.X) {
                    return ((AbstractDoubleToDouble) e).getSymmetricX(center);
                } else {
                    return ((AbstractDoubleToDouble) e).getSymmetricY(center);
                }
            } catch (IllegalArgumentException ignored) {
                // fall through
            }
        }
        List<Expr> children = e.getChildren();
        if (children.isEmpty()) {
            throw new UnsupportedOperationException("Cannot mirror " + e.getClass().getSimpleName()
                    + " about " + axis + "=" + center);
        }
        Expr[] updated = new Expr[children.size()];
        for (int i = 0; i < updated.length; i++) {
            updated[i] = mirror(children.get(i), axis, center);
        }
        return ExprDefaults.copyProperties(e, e.newInstance(updated));
    }

    /**
     * Moves the support of a coordinate-invariant leaf (constant / parameter) by reflecting
     * its domain about <code>center</code>; the value itself is unchanged.
     */
    private static Expr relocate(Expr e, Axis axis, double center) {
        Domain d = e.getDomain();
        if (axis == Axis.X ? d.isUnboundedX() : d.isUnboundedY()) {
            return e;
        }
        Domain nd;
        switch (d.getDimension()) {
            case 1: {
                nd = Domain.ofBounds(2 * center - d.xmax(), 2 * center - d.xmin());
                break;
            }
            case 2: {
                nd = axis == Axis.X
                        ? Domain.ofBounds(2 * center - d.xmax(), 2 * center - d.xmin(), d.ymin(), d.ymax())
                        : Domain.ofBounds(d.xmin(), d.xmax(), 2 * center - d.ymax(), 2 * center - d.ymin());
                break;
            }
            default: {
                nd = axis == Axis.X
                        ? Domain.ofBounds(2 * center - d.xmax(), 2 * center - d.xmin(), d.ymin(), d.ymax(), d.zmin(), d.zmax())
                        : Domain.ofBounds(d.xmin(), d.xmax(), 2 * center - d.ymax(), 2 * center - d.ymin(), d.zmin(), d.zmax());
                break;
            }
        }
        return Maths.expr(e.toComplex(), nd);
    }

    public static DoubleMultiplierTransform doubleMul(double d) {
        return new DoubleMultiplierTransform(d);
    }

    public static DomainXYMultiplierTransform domainMul(Domain d) {
        return new DomainXYMultiplierTransform(d);
    }

    public static OppositeTransform opposite(Axis axis) {
        return new OppositeTransform(axis);
    }

    public static SymmetryTransform symmetric(Axis axis, Domain domainXY) {
        return new SymmetryTransform(axis, domainXY);
    }

    public static TranslateTransform translate(double valueX, double valueY) {
        return new TranslateTransform(valueX, valueY);
    }
}
