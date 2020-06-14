package net.vpc.scholar.hadruwaves.util;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.Complex;

import java.util.Objects;

public final class AdmittanceValue implements Impedance {
    private static final long serialVersionUID = 1L;
    private final Complex value;

    public AdmittanceValue(Complex value) {
        this.value = value;
    }

    @Override
    public AdmittanceValue parallel(Impedance b){
        return parallel(b.admittanceValue());
    }

    @Override
    public AdmittanceValue serial(Impedance b){
        return serial(b.admittanceValue());
    }

    private AdmittanceValue parallel(Complex b){
        return new AdmittanceValue(value.plus(b));
    }

    private AdmittanceValue serial(Complex b){
        return new AdmittanceValue((value.inv().plus(b.inv())).inv());
    }

    @Override
    public ImpedanceValue impedance(){
        return new ImpedanceValue(value.inv());
    }

    @Override
    public AdmittanceValue admittance(){
        return this;
    }

    public Complex get() {
        return value;
    }

    @Override
    public Complex impedanceValue() {
        return value.inv();
    }

    @Override
    public Complex admittanceValue() {
        return value;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function("admittance", context.elem(value)).build();
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
        AdmittanceValue that = (AdmittanceValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+value.hashCode();
    }
}
