package net.thevpc.scholar.hadrumaths.meshalgo.rect;


import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.elem.NUpletElement;
import net.thevpc.nuts.log.NLog;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.util.function.Function;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 mai 2007 19:50:52
 */
public final class GridPrecision implements Cloneable, HSerializable {
    public static final GridPrecision LEAST_PRECISION = new GridPrecision(0, 0, 0, 0);
    private static final long serialVersionUID = 1L;
    private int xmax = 1;
    private int ymax = 1;
    private int xmin = 1;
    private int ymin = 1;

    public static NOptional<GridPrecision> parse(NElement value, Function<NElement,NElement> evaluator) {
        Function<NElement,NElement> evaluator2=e->{
            if(evaluator!=null){
                return evaluator.apply(e);
            }
            return e;
        };

        if (value.isAnyStringOrName()) {
            switch (NNameFormat.LOWER_KEBAB_CASE.format(value.asStringValue().orElse(""))) {
                case "least": {
                    return NOptional.of(GridPrecision.LEAST_PRECISION);
                }
            }
            return NOptional.of(GridPrecision.LEAST_PRECISION);
        }
        if (value.isNamedUplet()) {
            NUpletElement u = value.asUplet().get();
            switch (NNameFormat.LOWER_KEBAB_CASE.format(u.name().orElse(""))) {
                case "x": {
                    if (u.children().size() == 1) {
                        NOptional<Integer> v = u.children().get(0).asIntValue();
                        if (v.isPresent()) {
                            return NOptional.of(GridPrecision.ofX(v.get()));
                        }
                    }
                    if (u.children().size() == 2) {
                        NOptional<Integer> v1 = u.children().get(0).asIntValue();
                        NOptional<Integer> v2 = u.children().get(1).asIntValue();
                        if (v1.isPresent() && v2.isPresent()) {
                            return NOptional.of(GridPrecision.ofX(v1.get(), v2.get()));
                        }
                    }
                    NLog.ofScoped(GridPrecision.class).log(NMsg.ofC("invalid GridPrecision.ofX(...) :  %s", value).asError());
                    return NOptional.<GridPrecision>ofError(NMsg.ofC("invalid GridPrecision.ofX(...) :  %s", value).asError()).withDefault(() -> GridPrecision.LEAST_PRECISION);
                }
                case "y": {
                    if (u.children().size() == 1) {
                        NOptional<Integer> v = u.children().get(0).asIntValue();
                        if (v.isPresent()) {
                            return NOptional.of(GridPrecision.ofY(v.get()));
                        }
                    }
                    if (u.children().size() == 2) {
                        NOptional<Integer> v1 = u.children().get(0).asIntValue();
                        NOptional<Integer> v2 = u.children().get(1).asIntValue();
                        if (v1.isPresent() && v2.isPresent()) {
                            return NOptional.of(GridPrecision.ofY(v1.get(), v2.get()));
                        }
                    }
                    NLog.ofScoped(GridPrecision.class).log(NMsg.ofC("invalid GridPrecision.ofY(...) :  %s", value).asError());
                    return NOptional.<GridPrecision>ofError(NMsg.ofC("invalid GridPrecision.ofY(...) :  %s", value).asError()).withDefault(() -> GridPrecision.LEAST_PRECISION);
                }
                case "xy": {
                    if (u.children().size() == 1) {
                        NOptional<Integer> v = u.children().get(0).asIntValue();
                        if (v.isPresent()) {
                            return NOptional.of(GridPrecision.ofXY(v.get()));
                        }
                    }
                    if (u.children().size() == 2) {
                        NOptional<Integer> v1 = u.children().get(0).asIntValue();
                        NOptional<Integer> v2 = u.children().get(1).asIntValue();
                        if (v1.isPresent() && v2.isPresent()) {
                            return NOptional.of(GridPrecision.ofXY(v1.get(), v2.get()));
                        }
                    }
                    if (u.children().size() == 4) {
                        NOptional<Integer> v1 = u.children().get(0).asIntValue();
                        NOptional<Integer> v2 = u.children().get(1).asIntValue();
                        NOptional<Integer> v3 = u.children().get(2).asIntValue();
                        NOptional<Integer> v4 = u.children().get(3).asIntValue();
                        if (
                                v1.isPresent() && v2.isPresent()
                                        && v3.isPresent() && v4.isPresent()
                        ) {
                            return NOptional.of(GridPrecision.ofXY(v1.get(), v2.get(), v3.get(), v4.get()));
                        }
                    }
                    NLog.ofScoped(GridPrecision.class).log(NMsg.ofC("invalid GridPrecision.ofXY(...) :  %s", value).asError());
                    return NOptional.<GridPrecision>ofError(NMsg.ofC("invalid GridPrecision.ofXY(...) :  %s", value).asError()).withDefault(() -> GridPrecision.LEAST_PRECISION);
                }
            }
        }
        NLog.ofScoped(GridPrecision.class).log(NMsg.ofC("invalid GridPrecision(...) :  %s", value).asError());
        return NOptional.<GridPrecision>ofError(NMsg.ofC("invalid GridPrecision(...) :  %s", value).asError()).withDefault(() -> GridPrecision.LEAST_PRECISION);
    }

    public GridPrecision(int val) {
        this(val, val, -1, -1);
    }

    private GridPrecision(int xmin, int xmax, int ymin, int ymax) {
        this.xmin = (xmin);
        this.xmax = (xmax);
        this.ymin = (ymin);
        this.ymax = (ymax);
    }

    public GridPrecision(int xmin, int xmax) {
        this(xmin, xmax, -1, -1);
    }

    public static GridPrecision ofX(int min, int max) {
        return new GridPrecision(min, max, 0, 0);
    }

    public static GridPrecision ofX(int value) {
        return new GridPrecision(value, value, 0, 0);
    }

    public static GridPrecision ofY(int min, int max) {
        return new GridPrecision(0, 0, min, max);
    }

    public static GridPrecision ofY(int value) {
        return new GridPrecision(0, 0, value, value);
    }

    public static GridPrecision ofXY(int min, int max) {
        return new GridPrecision(min, max, min, max);
    }

    public static GridPrecision ofXY(int value) {
        return new GridPrecision(value, value, value, value);
    }

    public static GridPrecision ofXY(int xmin, int xmax, int ymin, int ymax) {
        return new GridPrecision(xmin, xmax, ymin, ymax);
    }

    public int getXmax() {
        return xmax;
    }

    public int getYmax() {
        return ymax;
    }

    public int getXmin() {
        return xmin;
    }

    public int getYmin() {
        return ymin;
    }

    @Override
    public NElement toElement() {
        return NElement.ofObjectBuilder(getClass().getSimpleName())
                .add("xmin", NElementHelper.elem(xmin))
                .add("xmax", NElementHelper.elem(xmax))
                .add("ymin", NElementHelper.elem(ymin))
                .add("ymax", NElementHelper.elem(ymax))
                .build();
    }
//    public String dump() {
//        Dumper h = new Dumper(getClass().getSimpleName(), Dumper.Type.SIMPLE);
//        h.add("xmin", xmin);
//        h.add("xmax", xmax);
//        h.add("ymin", ymin);
//        h.add("ymax", ymax);
//        return h.toString();
//    }

    public String toString() {
        return dump();
    }
}
