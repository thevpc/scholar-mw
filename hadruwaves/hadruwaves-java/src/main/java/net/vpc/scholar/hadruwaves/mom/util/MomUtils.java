/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.util;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadruwaves.Boundary;
import net.vpc.scholar.hadruwaves.mom.str.*;
import net.vpc.scholar.hadruwaves.mom.str.TestFunctionsComparator;

/**
 *
 * @author vpc
 */
public class MomUtils {

    public static final TestFunctionsComparator GP_COMP_DOMAIN = new GpDomainComparator();
    public static final TestFunctionsComparator GP_COMP_XYXY = new GpAlternateXYComparator();
    public static final TestFunctionsComparator GP_COMP_XXYY = new GpXThenYComparator();

    /**
     * En fait isElectricFieldMaximumForWall donne le meme resultat que
     * isCurrentMaximumForWall sauf qu'il ne faut pas oublier qu'ils ne sont pas
     * definis sur le meme domaine!!!
     *
     * @param currentAxis
     * @param wall
     * @param wallAxis
     * @return
     */
    public static boolean isElectricFieldMaximumForWall(Axis currentAxis, Boundary wall, Axis wallAxis) {
        return isCurrentMaximumForWall(currentAxis, wall, wallAxis);
    }

    public static boolean isCurrentMaximumForWall(Axis currentAxis, Boundary wall, Axis wallAxis) {
        switch (currentAxis) {
            case X: {
                switch (wallAxis) {
                    case X: {
                        switch (wall) {
                            case ELECTRIC: {
                                return false;
                            }
                            case MAGNETIC: {
                                return true;
                            }
                            case PERIODIC: {
                                return true;
                            }
                        }
                        break;
                    }
                    case Y: {
                        switch (wall) {
                            case ELECTRIC: {
                                return true;
                            }
                            case MAGNETIC: {
                                return false;
                            }
                            case PERIODIC: {
                                return false;
                            }
                        }
                    }
                }
                break;
            }
            case Y: {
                switch (wallAxis) {
                    case X: {
                        switch (wall) {
                            case ELECTRIC: {
                                return true;
                            }
                            case MAGNETIC: {
                                return false;
                            }
                            case PERIODIC: {
                                return false;
                            }
                        }
                        break;
                    }
                    case Y: {
                        switch (wall) {
                            case ELECTRIC: {
                                return false;
                            }
                            case MAGNETIC: {
                                return true;
                            }
                            case PERIODIC: {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        throw new UnsupportedOperationException("Physics.isCurrentMaximumForWall(...)");
    }

    public static String toValidEnumString(Enum e) {
        return e == null ? null : e.toString().toLowerCase();
    }

    public static String toValidEnumString(String s, Class<? extends Enum> e) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        Enum[] values;
        try {
            values = (Enum[]) e.getDeclaredMethod("values").invoke(null);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Exxror", ex);
        }
        for (int i = 0; i < values.length; i++) {
            String s2 = toValidEnumString(values[i]);
            if (s2.equalsIgnoreCase(s)) {
                return s2;
            }
        }
        return s;
    }

    public static Enum getEnum(String s, Class<? extends Enum> e) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        Enum[] values;
        try {
            values = (Enum[]) e.getDeclaredMethod("values").invoke(null);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Exxror", ex);
        }
        for (int i = 0; i < values.length; i++) {
            String s2 = toValidEnumString(values[i]);
            if (s2.equalsIgnoreCase(s)) {
                return values[i];
            }
        }
        return null;
    }
}
