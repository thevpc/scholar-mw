package net.thevpc.scholar.hadruplot.extension;

import net.thevpc.scholar.hadruplot.PlotDoubleComplex;
import net.thevpc.scholar.hadruplot.PlotDoubleConverter;

public interface PlotNumbers {
    double toDouble(Object o);

    PlotDoubleComplex toDoubleComplex(Object o);

    Object plus(Object o, Object b);

    Object mul(Object o, double b);

    int compare(Object s, Object c);

    double relativeError(Object aa, Object bb);

    PlotDoubleConverter resolveDoubleConverter(Object d);
}
