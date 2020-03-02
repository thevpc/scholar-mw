package net.vpc.scholar.hadruwaves.util;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.HSerializable;

public interface Impedance extends HSerializable {

    Impedance parallel(Impedance b);

    Impedance serial(Impedance b);

    ImpedanceValue impedance();

    AdmittanceValue admittance();

    Complex impedanceValue();

    Complex admittanceValue();
}
