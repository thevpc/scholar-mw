package net.thevpc.scholar.hadruwaves.project.configuration;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePMap;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.common.strings.StringConverter;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.plot.d3.BoundDomain;
import net.thevpc.scholar.hadrumaths.symbolic.Param;
import net.thevpc.scholar.hadrumaths.units.ConvParamUnit;
import net.thevpc.scholar.hadrumaths.units.MulParamUnit;
import net.thevpc.scholar.hadrumaths.units.ParamUnit;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadruwaves.project.EvalExprResult;
import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterValue;
import net.thevpc.scholar.hadruwaves.project.scene.Point3DTemplate;
import net.thevpc.scholar.hadruwaves.solvers.HWSolverTemplate;
import net.thevpc.scholar.hadruwaves.util.ProjectFormatter;

import java.util.*;

public class HWConfigurationRun extends AbstractHWConfigurationElement {

    /**
     * name to expression map
     */
    private WritablePMap<String, String> parameters = Props.of("parameters").mapOf(String.class, String.class);
    private WritablePValue<HWSolverTemplate> solver = Props.of("solver").valueOf(HWSolverTemplate.class, null);
    private Map<String, Object> cachedExpressions = new HashMap<>();
    private Domain cachedDomain = null;

    public HWConfigurationRun() {
    }

    public HWConfigurationRun(String name) {
        name().set(name);
    }

    public WritablePMap<String, String> parameters() {
        return parameters;
    }

    public WritablePValue<HWSolverTemplate> solver() {
        return solver;
    }

    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj("configuration")
                .add("name", name().get())
                .add("description", description().get())
                .add("parameters", context.elem(parameters.toMap()))
                .build();
    }

    public String getExpression(String param) {
        return parameters().get(param);
    }

    public Domain getDomain() {
        if (cachedDomain != null) {
            return cachedDomain;
        }
        if (project.get() == null) {
            return cachedDomain = Domain.FULLXYZ;
        }
        Domain d1 = project.get().scene().get().domain() == null ? null : project.get().scene().get().domain().eval(this);
        BoundDomain bdomain = new BoundDomain();
        for (HWProjectComponent cmp : project.get().scene().get().findDeepComponents(x -> x.enabled().eval(this))) {
            cmp.updateBoundDomain(this, bdomain);
        }
        Domain rd = bdomain.toDomain(0);
        if (d1 != null) {
            rd = rd.intersect(d1);
        }
        return cachedDomain = rd;
    }

    public Integer evalInteger(String expression, Integer def) {
        Long ll = evalLong(expression, def == null ? null : def.longValue());
        if (ll == null) {
            return null;
        }
        return ll.intValue();
    }

    public Long evalLong(String expression, Long def) {
        return ((Long) evalResult(expression, UnitType.Integer, null, def)
                .getValueOrError());
    }

    public Double evalDouble(String expression, Double def) {
        return (Double) evalResult(expression, UnitType.Double, null, def)
                .getValueOrError();
    }

    public Complex evalComplex(String expression, Complex def) {
        return (Complex) evalResult(expression, UnitType.Complex, null, def)
                .getValueOrError();
    }

    public Expr evalExpr(String expression, Expr def) {
        return (Expr) evalResult(expression, UnitType.Expression, null, def)
                .getValueOrError();
    }

    public Boolean evalBoolean(String expression, Boolean defaultValue) {
        return (Boolean) evalResult(expression, UnitType.Boolean, null, defaultValue).getValueOrError();
    }

    public String evalString(String expression) {
        return (String) evalResult(expression, UnitType.String, null, null).getValueOrError();
    }

    public Object evalAny(String expression) {
        if (expression == null) {
            return null;
        }
        // TODO this is a workaround untill infinity and NaN are supported by expression parser!
        switch (expression.toLowerCase().trim()) {
            case "infinity":
                return Maths.expr(Double.POSITIVE_INFINITY);
            case "+infinity":
                return Maths.expr(Double.POSITIVE_INFINITY);
            case "NaN":
                return Maths.expr(Double.NaN);
        }
        Expr e = Maths.parseExpression(expression);
        if (e == null) {
            return null;
        }
        ParamValues pv = new DefaultParamValues();
        for (Param param : e.getParams()) {
            Object c = cachedExpressions.get(param.getName());
            if (c != null) {
                if (c.getClass().equals(Object.class)) {
                    throw new IllegalArgumentException("Unable to resolve " + param.getName() + ". Recursion detected.");
                }
            } else {
                c = evalParam(param.getName());
            }
            if (c instanceof Double) {
                pv.set(param, (Double) c);
            } else if (c instanceof Complex) {
                pv.set(param, (Complex) c);
            } else if (c instanceof Expr) {
                pv.set(param, (Expr) c);
            } else {
                throw new IllegalArgumentException("Unexpected type " + c.getClass().getName() + " for " + param.getName());
            }
        }
        return e.setParams(pv).simplify();
    }

    public Object evalParam(String parameterName) {
        return evalParamResult(parameterName).getValueOrError();
    }

    public String formatDimension(String expr, ProjectFormatter.Mode mode) {
        try {
            double d = evalDouble(expr, 0.0);
            return ProjectFormatter.formatDimension(project.get(), d, mode);
        } catch (Exception ex) {
            return expr;
        }
    }

    public String formatFrequency(String expr, ProjectFormatter.Mode mode) {
        try {
            double d = evalDouble(expr, 0.0);
            return ProjectFormatter.formatFrequency(project.get(), d, mode);
        } catch (Exception ex) {
            return expr;
        }
    }

    public String formatPoint(Point3DTemplate p, ProjectFormatter.Mode mode) {
        return "("
                + formatDimension(p.x().get(), mode)
                + "," + formatDimension(p.y().get(), mode)
                + "," + formatDimension(p.z().get(), mode)
                + ")";
    }

    public void reset() {
        cachedDomain = null;
        cachedExpressions.clear();
    }

    public EvalExprResult evalParamResult(String paramName) {
        HWParameterValue p2 = project.get().parameters().getParameneter(paramName);
        if (p2 == null) {
            if ("infinity".equals(paramName) || "Infinity".equals(paramName) || "\u221E".equals(paramName)) {
                return EvalExprResult.Success(paramName, "\u221E", Double.POSITIVE_INFINITY);
            } else if ("NaN".equalsIgnoreCase(paramName)) {
                return EvalExprResult.Success(paramName, "NaN", Double.NEGATIVE_INFINITY);
            } else if ("pi".equalsIgnoreCase(paramName) || (CharactersTable.PI.equals(paramName)) || (CharactersTable.pi.equals(paramName))){
                return EvalExprResult.Success(paramName, CharactersTable.pi, Maths.PI);
            } else if ("eps0".equalsIgnoreCase(paramName) || ((CharactersTable.epsilon+"0").equals(paramName))  || ((CharactersTable.epsilon+"_0").equals(paramName)) ) {
                return EvalExprResult.Success(paramName, (CharactersTable.epsilon+"_0"), Maths.EPS0);
            } else if ("u0".equalsIgnoreCase(paramName) || ((CharactersTable.mu+"0").equals(paramName))  || ((CharactersTable.mu+"_0").equals(paramName))) {
                return EvalExprResult.Success(paramName, (CharactersTable.mu+"_0"), Maths.U0);
            } else if ("z0".equalsIgnoreCase(paramName)) {
                return EvalExprResult.Success(paramName, "Z0", Maths.Z0);
            } else if ("C".equals(paramName)) {
                return EvalExprResult.Success(paramName, "C", Maths.C);
            } else if ("Qe".equals(paramName)) {
                return EvalExprResult.Success(paramName, "Qe", Maths.Qe);
            } else if ("xmin".equals(paramName) || "x_min".equals(paramName)) {
                return EvalExprResult.Success(paramName, paramName, getDomain().xmin());
            } else if ("xmax".equals(paramName) || "x_max".equals(paramName)) {
                return EvalExprResult.Success(paramName, paramName, getDomain().xmax());
            } else if ("ymin".equals(paramName) || "y_min".equals(paramName)) {
                return EvalExprResult.Success(paramName, paramName, getDomain().ymin());
            } else if ("ymax".equals(paramName) || "y_max".equals(paramName)) {
                return EvalExprResult.Success(paramName, paramName, getDomain().ymax());
            } else if ("zmin".equals(paramName) || "z_min".equals(paramName)) {
                return EvalExprResult.Success(paramName, paramName, getDomain().zmin());
            } else if ("zmax".equals(paramName) || "z_max".equals(paramName)) {
                return EvalExprResult.Success(paramName, paramName, getDomain().zmax());
            }
            return EvalExprResult.Error(paramName, "Unable to resolve Param " + paramName + ".");
        }
        boolean hasUnit = p2.type().get() != null && p2.type().get().unitType() != null;
        Object c = cachedExpressions.get(paramName);
        if (c != null) {
            if (c.getClass().equals(Object.class)) {
                return EvalExprResult.Error(paramName, "Unable to resolve " + paramName + ". Recursion detected.");
            }
            if (c instanceof EvalExprResult) {
                return (EvalExprResult) c;
            }
            return EvalExprResult.Error(paramName, "Unexpected Error");
        } else {
            cachedExpressions.put(paramName, new Object());
            EvalExprResult r = null;
            try {
                String expression = parameters().get(paramName);
                UnitType ttype = p2.type().get();
                ParamUnit tunit = p2.unit().get();
                if (tunit == null) {
                    if (project() != null) {
                        tunit = project().get().units().defaultUnitValue(ttype);
                    }
                    if (tunit == null) {
                        tunit = ttype.defaultValue();
                    }
                }
                r = evalResult(expression, ttype, tunit, null);
                if (hasUnit && !r.isError() && !r.hasUnit()) {
                    r = EvalExprResult.Success(r.expression, r.formattedString, r.value, r.unitAwareValue, true);
                }
            } finally {
                cachedExpressions.remove(paramName);
            }
            if (r != null) {
                cachedExpressions.put(paramName, r);
            }
            return r;
        }
    }

    public EvalExprResult evalResult(String expr, UnitType type, ParamUnit uu, Object defaultValue) {
        if (expr == null || expr.isEmpty()) {
            if (defaultValue != null) {
                return EvalExprResult.Success(expr, defaultValue == null ? null : String.valueOf(defaultValue), defaultValue, defaultValue, false);
            }
            expr = "";
        }
        if (type == null) {
            return EvalExprResult.Error(expr, "Missing Type");
        }
        if (uu == null) {
            uu = project == null ? null : project().get().units().defaultUnitValue(type);
            if (uu == null) {
                uu = type.defaultValue();
            }
        }
        if (type == UnitType.String) {
            try {
                String v = StringUtils.replaceDollarPlaceHolders(expr, new StringConverter() {
                    @Override
                    public String convert(String param) {
                        EvalExprResult r = evalParamResult(param);
                        if (r.isError()) {
                            throw new IllegalArgumentException(r.getErrorMessage());
                        }
                        return r.getFormattedString();
                    }
                });
                return EvalExprResult.Success(expr, v, v, v, false);
            } catch (Exception ex) {
                return EvalExprResult.Error(expr, ex);
            }
        }
        if (type == UnitType.Boolean) {
            try {
                if ("true".equals(expr) || "yes".equals(expr)) {
                    return EvalExprResult.Success(expr, "true", true, false, false);
                }
                if ("false".equals(expr) || "no".equals(expr)) {
                    return EvalExprResult.Success(expr, "false", false, false, false);
                }
                EvalExprResult r = evalResult(expr, UnitType.Double, null, defaultValueAsDouble(defaultValue));
                if (r.isError()) {
                    return r;
                }
                boolean b = ((Number) r.getValue()).doubleValue() != 0;
                return EvalExprResult.Success(expr, String.valueOf(b), b, b, false);
            } catch (Exception ex) {
                return EvalExprResult.Error(expr, ex);
            }
        }
        if (type == UnitType.Temperature) {
            try {
                EvalExprResult r = evalResult(expr, UnitType.Double, null, defaultValueAsDouble(defaultValue));
                if (r.isError()) {
                    return r;
                }
                ConvParamUnit SI = (ConvParamUnit) type.defaultValue();
                double v0 = ((Number) r.getValue()).doubleValue();
//                double v_SI = (r.hasUnit()) ? v0 : ((TemperatureUnit) uu).to((TemperatureUnit)UnitType.Temperature.defaultValue(), v0);
//                double v_usr = TemperatureUnit.Celsius.to((TemperatureUnit) uu, v_SI);
//                return EvalExprResult.Success(expr, ProjectFormatter.decimalFormat(project).format(v_usr), v_SI, v_usr, true);
                ConvParamUnit userUnit = (ConvParamUnit) uu;
                double v_SI = (r.hasUnit()) ? v0 : userUnit.toSI(v0);
                double v_usr = SI.to(v_SI, userUnit);
                return EvalExprResult.Success(expr, ProjectFormatter.decimalFormat(project.get()).format(v_usr), v_SI, v_usr, true);

            } catch (Exception ex) {
                return EvalExprResult.Error(expr, ex);
            }
        }
        if (type instanceof UnitType.MulUnitType) {
            try {
                EvalExprResult r = evalResult(expr, UnitType.Double, null, defaultValueAsDouble(defaultValue));
                if (r.isError()) {
                    return r;
                }
                double v0 = ((Number) r.getValue()).doubleValue();
                double m = ((MulParamUnit) uu).multiplier();
                double v1 = (r.hasUnit()) ? v0 : v0 * m;
                return EvalExprResult.Success(expr, ProjectFormatter.decimalFormat(project.get()).format(v1 / m), v1, v1 / m, true);
            } catch (Exception ex) {
                return EvalExprResult.Error(expr, ex);
            }
        }
        if (type == UnitType.Double || type == UnitType.Integer || type == UnitType.Complex || type == UnitType.Expression) {
            Expr eexpr = null;
            boolean hasUnit = false;
            // TODO this is a workaround untill infinity and NaN are supported by expression parser!
            if (expr.isEmpty()) {
                eexpr = Maths.expr(defaultValueAsDouble(defaultValue));
            } else if (expr.toLowerCase().trim().equals("infinity")) {
                eexpr = Maths.expr(Double.POSITIVE_INFINITY);
            } else if (expr.toLowerCase().trim().equals("+infinity")) {
                eexpr = Maths.expr(Double.POSITIVE_INFINITY);
            } else if (expr.toLowerCase().trim().equals("-infinity")) {
                eexpr = Maths.expr(Double.NEGATIVE_INFINITY);
            } else if (expr.toLowerCase().trim().equals("nan")) {
                eexpr = Maths.expr(Double.NaN);
            } else {
                eexpr = Maths.parseExpression(expr);
            }
            if (eexpr == null) {
                eexpr = Maths.expr(0);
            }
            ParamValues pv = new DefaultParamValues();
            for (Param param : eexpr.getParams()) {
                EvalExprResult r = null;
                boolean noSetParam = false;
                r = evalParamResult(param.getName());
                if (type == UnitType.Expression) {
                    if(r.isError() && r.getErrorMessage().contains("Unable to resolve Param ")){
                        //this an extra param
                        noSetParam = true;
                    }
                }
                if (noSetParam) {
                    //dont set this param!
                } else {
                    if (r.isError()) {
                        return r;
                    }
                    hasUnit |= r.hasUnit();
                    if (r.value instanceof Complex) {
                        pv.set(param, (Complex) r.value);
                    } else if (r.value instanceof Expr) {
                        pv.set(param, (Expr) r.value);
                    } else if (r.value instanceof Number) {
                        pv.set(param, ((Number) r.value).doubleValue());
                    } else {
                        return EvalExprResult.Error(expr, new IllegalArgumentException("Unexpected type " + (r.value == null ? "null" : (r.value.getClass().getName()))
                                + " for " + param.getName()));
                    }
                }
            }
            Expr eexpr2 = eexpr.setParams(pv).simplify();
            if (type == UnitType.Complex) {
                try {
                    Complex c = eexpr2.toComplex();
                    return EvalExprResult.Success(expr, c.toString(), c, c, hasUnit);
                } catch (Exception ex) {
                    return EvalExprResult.Error(expr, ex);
                }
            }
            if (type == UnitType.Double) {
                try {
                    double c = eexpr2.toDouble();
                    return EvalExprResult.Success(expr, ProjectFormatter.decimalFormat(project.get()).format(c), c, c, hasUnit);
                } catch (Exception ex) {
                    return EvalExprResult.Error(expr, ex);
                }
            }
            if (type == UnitType.Integer) {
                try {
                    //TODO fix me:how to support large integers ? double is not precise.
                    long c = (long) eexpr2.toDouble();
                    return EvalExprResult.Success(expr, String.valueOf(c), c, c, hasUnit);
                } catch (Exception ex) {
                    return EvalExprResult.Error(expr, ex);
                }
            }
            if (type == UnitType.Expression) {
                try {
                    return EvalExprResult.Success(expr, String.valueOf(eexpr2), eexpr2, eexpr2, hasUnit);
                } catch (Exception ex) {
                    return EvalExprResult.Error(expr, ex);
                }
            }
        }
        if (type instanceof UnitType.EnumUnitType) {
            if (expr.trim().isEmpty()) {
                return EvalExprResult.Success(expr, "", null, null, false);
            }
            UnitType.EnumUnitType et = (UnitType.EnumUnitType) type;
            for (Object enumConstant : et.getEnumType().getEnumConstants()) {
                if (String.valueOf(enumConstant).equals(expr)) {
                    return EvalExprResult.Success(expr, String.valueOf(enumConstant), enumConstant, enumConstant, false);
                }
            }
            return EvalExprResult.Error(expr, new IllegalArgumentException("Invalid value " + expr + " for " + type.name()));
        }
        throw new IllegalArgumentException("Unsupported");
    }

    private double defaultValueAsDouble(Object defaultValue) throws IllegalArgumentException {
        double defDbl = 0;
        if (defaultValue == null) {
            return 0;
        }
        if (defaultValue instanceof Boolean) {
            defDbl = ((Boolean) defaultValue) ? 1 : 0;
        } else if (defaultValue instanceof Number) {
            defDbl = ((Number) defaultValue).doubleValue();
        } else if (defaultValue instanceof String) {
            defDbl = Boolean.valueOf(String.valueOf(defaultValue)) ? 1 : 0;
        } else {
            throw new IllegalArgumentException("Unexpected");
        }
        return defDbl;
    }

    public String discriminatorStringSuffix() {
        String s = discriminatorString();
        if (s.isEmpty()) {
            return s;
        }
        return "-" + s;
    }

    public String discriminatorString() {
        List<HWParameterValue> all = new ArrayList<>(project().get().parameters().toMap().values());
        all.sort(Comparator.comparing(a -> StringUtils.trim(a.name().get())));
        StringBuilder sb = new StringBuilder();
        for (HWParameterValue v : all) {
            String n = v.name().get();
            if (v.discriminator().get()) {
                EvalExprResult r = evalParamResult(n);
                if (sb.length() > 0) {
                    sb.append("_");
                }
                sb.append(n).append("=");
                if (r.isError()) {
                    sb.append("err");
                } else {
                    sb.append(r.getFormattedString());
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.valueOf(project.get()) + "::" + String.valueOf(name());
    }
}
