/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.*;
import net.vpc.scholar.hadrumaths.Domain;

/**
 *
 * @author vpc
 */
public class DomainFormatter implements Formatter<Domain>{
    public DomainFormatter() {
    }

    @Override
    public String format(Domain o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();

    }

    private static void format(StringBuilder sb,Domain o,Axis axis,DoubleFormat df, String x,String y,String z,FormatParamSet format,String emptyValue){
        switch (axis){
            case X:{
                if(!Double.isNaN(o.xmin()) && !Double.isNaN(o.xmax()) && o.isUnconstrainedX()){
                    sb.append(emptyValue);
                    return ;
                }
                FormatFactory.format(sb,o.xmin(),format);
                sb.append("->");
                FormatFactory.format(sb,o.xmax(),format);
                return;
            }
            case Y:{
                if(!Double.isNaN(o.ymin()) && !Double.isNaN(o.ymax()) && o.isUnconstrainedY()){
                    sb.append(emptyValue);
                    return ;
                }
                FormatFactory.format(sb,o.ymin(),format);
                sb.append("->");
                FormatFactory.format(sb,o.ymax(),format);
                return;
            }
            case Z:{
                if(!Double.isNaN(o.zmin()) && !Double.isNaN(o.zmax()) && o.isUnconstrainedZ()){
                    sb.append(emptyValue);
                    return ;
                }
                FormatFactory.format(sb,o.zmin(),format);
                sb.append("->");
                FormatFactory.format(sb,o.zmax(),format);
                return;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public void format(StringBuilder sb, Domain o, FormatParamSet format) {
        String xf=format.getParam(FormatFactory.X).getName();
        String yf=format.getParam(FormatFactory.Y).getName();
        String zf=format.getParam(FormatFactory.Z).getName();
        DomainFormat d= format.getParam(FormatFactory.NON_FULL_GATE_DOMAIN);
        DoubleFormat df = format.getParam(DoubleFormat.class, false);
        ProductFormat pp = format.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp()==null?" ":(" "+pp.getOp()+" ");
        switch(d.getType()){
            case GATE:{
                if(!o.isNaN() && o.isFull() && d.isIgnoreFull()){
                    return;
                }else {
                    switch (o.getDimension()){
                        case 1:{
                            sb.append("domain(");
                            format(sb,o, Axis.X, df, xf, yf, zf, format,"FULL");
                            sb.append(")");
                            return ;
                        }
                        case 2:{
                            sb.append("domain(");
                            format(sb,o, Axis.X, df, xf, yf, zf, format,"FULL");
                            sb.append(", ");
                            format(sb,o, Axis.Y, df, xf, yf, zf, format,"FULL");
                            sb.append(")");
                            return;
                        }
                        case 3:{
                            sb.append("domain(");
                            format(sb,o, Axis.X, df, xf, yf, zf, format,"FULL");
                            sb.append(", ");
                            format(sb,o, Axis.Y, df, xf, yf, zf, format,"FULL");
                            sb.append(", ");
                            format(sb,o, Axis.Z, df, xf, yf, zf, format,"FULL");
                            sb.append(")");
                            return ;
                        }
                        default:{
                            throw new IllegalArgumentException("Unsupported");
                        }
                    }
                }
            }
            case NONE:{
                return ;
            }
        }
        return;
    }
}
