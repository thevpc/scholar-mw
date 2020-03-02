package net.vpc.scholar.hadruwaves.util;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.Complex;

import java.util.Objects;

public final class ImpedanceValue implements Impedance {
    private static final long serialVersionUID = 1L;

    private final Complex value;

    public ImpedanceValue(Complex value) {
        this.value = value;
    }

    public ImpedanceValue parallel(Impedance b) {
        return parallel(b.impedanceValue());
    }

    public ImpedanceValue serial(Impedance b) {
        return serial(b.impedanceValue());
    }

    private ImpedanceValue serial(Complex b) {
        return new ImpedanceValue(value.add(b));
    }

    private ImpedanceValue parallel(Complex b) {
        return new ImpedanceValue((value.inv().add(b.inv())).inv());
    }

    public AdmittanceValue admittance() {
        return new AdmittanceValue(value.inv());
    }

    @Override
    public ImpedanceValue impedance() {
        return this;
    }

    @Override
    public Complex impedanceValue() {
        return value;
    }

    @Override
    public Complex admittanceValue() {
        return value.inv();
    }

    public Complex get() {
        return value;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function("impedance", context.elem(value)).build();
    }

//    public String dump() {
//        Dumper h = new Dumper(this, Dumper.Type.SIMPLE);
//        h.add("value", value);
//        return h.toString();
//    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImpedanceValue that = (ImpedanceValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+value.hashCode();
    }
}
