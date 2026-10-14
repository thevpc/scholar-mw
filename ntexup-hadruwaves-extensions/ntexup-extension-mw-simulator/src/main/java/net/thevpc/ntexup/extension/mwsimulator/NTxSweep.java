package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.document.elem2d.NTxDouble2;
import net.thevpc.ntexup.api.document.elem2d.NTxInt2;
import net.thevpc.ntexup.api.eval.NTxValue;
import net.thevpc.ntexup.api.util.NTxNumberUtils;
import net.thevpc.ntexup.api.util.NTxUtils;
import net.thevpc.nuts.elem.*;
import net.thevpc.nuts.util.NArrays;
import net.thevpc.nuts.util.NOptional;

import java.util.Arrays;
import net.thevpc.nuts.util.NToStringBuilder;

public class NTxSweep implements NToElement {
    public Number rangeFrom;
    public Number rangeTo;
    public Number step;
    public Integer count;

    public NTxSweep() {
        rangeFrom = 0;
        rangeTo = 1;
        count = 1;
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder sweep = NElement.ofObjectBuilder().name("sweep");
        if (rangeFrom != null) {
            sweep.set("from", NElement.ofNumber(rangeFrom));
        }
        if (rangeTo != null) {
            sweep.set("to", NElement.ofNumber(rangeTo));
        }
        if (step != null) {
            sweep.set("step", NElement.ofNumber(step));
        }
        if (count != null) {
            sweep.set("count", NElement.ofNumber(count));
        }
        return sweep.build();
    }

    @Override
    public String toString() {
        NToStringBuilder sweep=new NToStringBuilder("sweep");
        if (rangeFrom != null) {
            sweep.add("from", rangeFrom);
        }
        if (rangeTo != null) {
            sweep.add("to", rangeTo);
        }
        if (step != null) {
            sweep.add("step", step);
        }
        if (count != null) {
            sweep.add("count", count);
        }
        return sweep.build();
    }
    
    

    public double[] doubleValues() {
        if (this.rangeFrom != null && this.rangeTo != null) {
            if (this.step != null) {
                return NArrays.range(this.rangeFrom.doubleValue(), this.rangeTo.doubleValue(), this.step.doubleValue());
            } else if (this.count != null) {
                return NArrays.linear(this.rangeFrom.doubleValue(), this.rangeTo.doubleValue(), this.count.intValue());
            } else {
                return NArrays.linear(this.rangeFrom.doubleValue(), this.rangeTo.doubleValue(), 2);
            }
        }
        return null;
    }

    public Number[] numberValues() {
        if (this.rangeFrom != null && this.rangeTo != null) {
            if (this.step == null && this.count == null) {
                this.step = 2;
            }
            if (this.step != null) {
                if (this.rangeFrom instanceof Double || this.rangeTo instanceof Double) {
                    return Arrays.stream(NArrays.range(this.rangeFrom.doubleValue(), this.rangeTo.doubleValue(), this.step.doubleValue()))
                            .boxed().toArray(Number[]::new);
                }
                return Arrays.stream(NArrays.range(this.rangeFrom.intValue(), this.rangeTo.intValue(), this.step.intValue()))
                        .boxed().toArray(Number[]::new);
            } else {
                if (this.rangeFrom instanceof Double || this.rangeTo instanceof Double) {
                    return Arrays.stream(NArrays.linear(this.rangeFrom.doubleValue(), this.rangeTo.doubleValue(), this.count.intValue()))
                            .boxed().toArray(Number[]::new);
                }
                return Arrays.stream(NArrays.linear(this.rangeFrom.intValue(), this.rangeTo.intValue(), this.count.intValue()))
                        .boxed().toArray(Number[]::new);
            }
        }
        return null;
    }

    public static NOptional<NTxSweep> parse(NElement e) {
        if (e == null || e.isNull()) {
            return NOptional.ofNamedEmpty("sweep");
        }
        if (e.isListContainer()) {
            NTxSweep s = new NTxSweep();
            for (NElement child : e.asListContainer().get().children()) {
                if (child.isNamedPair()) {
                    NPairElement p = child.asPair().get();
                    switch (NTxUtils.uid(p.key())) {
                        case "range": {
                            NOptional<NTxInt2> d = NTxValue.of(p.value()).asInt2();
                            if (!d.isPresent()) {
                                NOptional<NTxDouble2> d2 = NTxValue.of(p.value()).asDouble2();
                                if (!d2.isPresent()) {
                                    return NOptional.ofNamedError("sweep");
                                }
                                s.rangeFrom = d2.get().getX();
                                s.rangeTo = d2.get().getY();
                            } else {
                                s.rangeFrom = d.get().getX();
                                s.rangeTo = d.get().getY();
                            }
                            break;
                        }
                        case "from": {
                            Number n = asNumber(p.value());
                            if (n != null) {
                                s.rangeFrom = n;
                            }
                            break;
                        }
                        case "to": {
                            Number n = asNumber(p.value());
                            if (n != null) {
                                s.rangeTo = n;
                            }
                            break;
                        }
                        case "steps": {
                            Number n = asNumber(p.value());
                            if (n != null) {
                                s.step = n;
                            }
                            break;
                        }
                        case "times":
                        case "count": {
                            NOptional<Integer> d = NTxValue.of(p.value()).asInt();
                            if (d.isPresent()) {
                                s.count = d.get();
                            }
                            break;
                        }
                    }
                }
            }
            return NOptional.of(s);
        }
        return NOptional.ofNamedError("sweep");
    }

    private static Number asNumber(NElement e) {
        return e.asNumber()
                .flatMap(x -> NTxNumberUtils.toSIUnit(x).get().asNumberValue())
                .orNull();
    }
}
