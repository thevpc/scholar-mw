/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.util;

import java.text.DecimalFormat;
import net.vpc.scholar.hadrumaths.geom.Dimension;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.Point3D;
import net.vpc.scholar.hadrumaths.units.FrequencyUnit;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadrumaths.units.LengthUnit;
import net.vpc.scholar.hadruwaves.project.ParamUnits;

/**
 *
 * @author vpc
 */
public class ProjectFormatter {

    public static enum Mode {
        LONG,
        SHORT_SUFFIXED,
        SHORT_NOSUFFIX,
    }

    public static String formatPoint3D(HWProject p, Point3D v, Mode unitSuffix) {
        return "("
                + formatDimension(p, v.getX(), unitSuffix)
                + "," + formatDimension(p, v.getY(), unitSuffix)
                + "," + formatDimension(p, v.getZ(), unitSuffix)
                + ")";
    }

    public static String formatPoint(HWProject p, Point v, Mode unitSuffix) {
        return "("
                + formatDimension(p, v.x, unitSuffix)
                + "," + formatDimension(p, v.y, unitSuffix)
                + "," + formatDimension(p, v.z, unitSuffix)
                + ")";
    }

    public static String formatDimension(HWProject p, Dimension v, Mode unitSuffix) {
        return "("
                + formatDimension(p, v.x, unitSuffix)
                + "," + formatDimension(p, v.y, unitSuffix)
                + "," + formatDimension(p, v.z, unitSuffix)
                + ")";
    }

    public static DecimalFormat decimalFormat(HWProject p) {
        return new DecimalFormat("#0.000");
    }

    public static String formatDimension(HWProject p, double v, Mode unitSuffix) {
        LengthUnit a = p == null ? null : p.units().lengthUnit().get();
        if (a == null) {
            a = LengthUnit.m;
        }
        switch (unitSuffix) {
            case LONG: {
                return String.valueOf(v / a.multiplier());
            }
            case SHORT_NOSUFFIX: {
                return decimalFormat(p).format(v / a.multiplier());
            }
            case SHORT_SUFFIXED: {
                return decimalFormat(p).format(v / a.multiplier()) + a.name();
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public static String formatFrequency(HWProject p, double v, Mode unitSuffix) {
        FrequencyUnit a = p == null ? null : p.units().frequencyUnit().get();
        if (a == null) {
            a = FrequencyUnit.Hz;
        }
        switch (unitSuffix) {
            case LONG: {
                return String.valueOf(v / a.multiplier());
            }
            case SHORT_NOSUFFIX: {
                return decimalFormat(p).format(v / a.multiplier());
            }
            case SHORT_SUFFIXED: {
                return decimalFormat(p).format(v / a.multiplier()) + a.name();
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }
}
