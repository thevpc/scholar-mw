package net.thevpc.scholar.hadruwaves.util;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.HSerializable;

public interface Impedance extends HSerializable {

    Impedance parallel(Impedance b);

    Impedance serial(Impedance b);

    ImpedanceValue impedance();

    AdmittanceValue admittance();

    Complex impedanceValue();

    Complex admittanceValue();
}
