/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.ComplexIObjectFormatParam;

/**
 * @author vpc
 */
public class ComplexObjectFormat extends AbstractObjectFormat<Complex> {
    public ComplexObjectFormat() {
    }

    @Override
    public void format(Complex o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        double real = o.getReal();
        double imag = o.getImag();
        format(real, imag, context, format);
//        if (Double.isNaN(real) && Double.isNaN(imag)) {
//            sb.append("NaN");//FormatFactory.format(imag,format);
//            return;
//        }
//        if (Double.isNaN(real)) {
//            String s = "NaN";
//            if (imag == 0) {
//                sb.append(s);
//                return;
//            }
//            if (imag < 0) {
//                sb.append(s);
//                FormatFactory.format(sb, imag, format);
//                sb.append("i");
//                return;
//            }
//            sb.append(s);
//            sb.append("+");
//            FormatFactory.format(sb, imag, format);
//            sb.append("i");
//            return;
//        }
//        if (Double.isNaN(imag)) {
//            if (real == 0) {
//                sb.append("NaN*i");
//                return;
//            }
//            FormatFactory.format(sb, real, ObjectFormatParamSet.EMPTY);
//            sb.append("NaN*i");
//            return;
//        }
////        String imag_string = FormatFactory.format(imag,format);
////        String real_string = FormatFactory.format(real,format);
//        String i_string = "i";
//        if (imag == 0) {
//            FormatFactory.format(sb, real, format);
//            return;
//        } else if (real == 0) {
//            if (imag == 1) {
//                sb.append(i_string);
//            } else {
//                if (imag == -1) {
//                    sb.append("-").append(i_string);
//                } else {
//                    FormatFactory.format(sb,imag,format);
//                    sb.append(i_string);
//                }
//            }
//            return;
//        } else {
//            FormatFactory.format(sb, real, format);
//            if (imag == 1) {
//                sb.append("+").append(i_string);
//            } else {
//                if (imag == -1) {
//                    sb.append("-").append(i_string);
//                } else {
//                    if (imag > 0) {
//                        sb.append("+");
//                        FormatFactory.format(sb,imag,format);
//                        sb.append(i_string);
//                    } else {
//                        FormatFactory.format(sb,imag,format);
//                        sb.append(i_string);
//                    }
//                }
//            }
//            return;
//        }

    }

    private void format(double real, double imag, ObjectFormatContext context, ObjectFormatParamSet format) {
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        ObjectFormatParamSet subParams = format.remove(FormatFactory.REQUIRED_PARS);
        if (imag == 0) {
            if (real >= 0) {
                par = false;
            }
            if (par) {
                context.append("(");
            }
            realToString(real, context, subParams);
            if (par) {
                context.append(")");
            }

        } else if (real == 0) {
            if (imag >= 0) {
                par = false;
            }
            if (par) {
                context.append("(");
            }
            imagToString(imag, context, subParams);
            if (par) {
                context.append(")");
            }
        } else {
            if (imag < 0) {
                if (par) {
                    context.append("(");
                }
                realToString(real, context, subParams);
                context.append("-");
                imagToString(-imag, context, subParams);
                if (par) {
                    context.append(")");
                }
            } else {
                if (par) {
                    context.append("(");
                }
                realToString(real, context, subParams);
                context.append("+");
                imagToString(imag, context, subParams);
                if (par) {
                    context.append(")");
                }
            }
        }
    }

    protected void realToString(double d, ObjectFormatContext context, ObjectFormatParamSet format) {
        context.format( d, format);
    }

    protected void imagToString(double d,ObjectFormatContext context, ObjectFormatParamSet format) {
        ComplexIObjectFormatParam i = format.getParam(ComplexIObjectFormatParam.class, FormatFactory.I_HAT);
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            context.format(d, format);
            context.append("*").append(i.getName());
        } else if (d == 1) {
            context.append(i.getName());
        } else if (d == -1) {
            context.append("-").append(i.getName());
        } else {
            context.format( d, format);
            context.append(i.getName());
        }
    }
}
