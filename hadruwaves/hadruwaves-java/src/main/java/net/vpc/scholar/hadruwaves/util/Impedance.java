package net.vpc.scholar.hadruwaves.util;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

import java.io.Serializable;

public interface Impedance extends Serializable, Dumpable {

    Impedance parallel(Impedance b);

    Impedance serial(Impedance b);

    ImpedanceValue impedance();

    AdmittanceValue admittance();

    Complex impedanceValue();

    Complex admittanceValue();
}
