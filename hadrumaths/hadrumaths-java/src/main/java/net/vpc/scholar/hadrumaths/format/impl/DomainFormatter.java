/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
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
    public String format(Domain o, FormatParam... format) {
        FormatParamArray formatArray=new FormatParamArray(format);
        String xf=formatArray.getParam(FormatFactory.X).getName();
        String yf=formatArray.getParam(FormatFactory.Y).getName();
        String zf=formatArray.getParam(FormatFactory.Z).getName();
        DomainFormat d=(DomainFormat) formatArray.getParam(FormatFactory.GATE_DOMAIN);
        DoubleFormat df = (DoubleFormat) formatArray.getParam(DoubleFormat.class, false);
        ProductFormat pp = (ProductFormat)formatArray.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp()==null?" ":(" "+pp.getOp()+" ");
        switch(d.getType()){
            case GATE:{
                if(o.equals(Domain.FULL(o.getDimension()))){
                    return "";
                }else {
                    switch (o.getDimension()){
                        case 1:{
                            String x = format(o, Axis.X, df, xf, yf, zf, format);
                            return "domain("+ (x.isEmpty()?"FULL":x) +")";
                        }
                        case 2:{
                            String x=format(o,Axis.X,df,xf,yf,zf,format);
                            String y=format(o,Axis.Y,df,xf,yf,zf,format);
                            return "domain("+ ((x.isEmpty()?"FULL":x)+","+(y.isEmpty()?"FULL":y) +")");
                        }
                        case 3:{
                            String x=format(o,Axis.X,df,xf,yf,zf,format);
                            String y=format(o,Axis.Y,df,xf,yf,zf,format);
                            String z=format(o,Axis.Z,df,xf,yf,zf,format);
                            return "domain("+ ((x.isEmpty()?"FULL":x)+","+(y.isEmpty()?"FULL":y)+","+(z.isEmpty()?"FULL":z) +")");
                        }
                        default:{
                            throw new IllegalArgumentException("Unsupported");
                        }
                    }
                }
            }
            case NONE:{
                return "";
            }
        }
        return null;
    }

    private static String format(Domain o,Axis axis,DoubleFormat df, String x,String y,String z,FormatParam... format){
        switch (axis){
            case X:{
                if(o.isUnconstrainedX()){
                    return "";
                }
                return ("" + FormatFactory.format(o.xmin(),format) + "->" + FormatFactory.format(o.xmax(),format) + "")
                ;
            }
            case Y:{
                if(o.isUnconstrainedY()){
                    return "";
                }
                return ("" + FormatFactory.format(o.ymin(),format) + "->" + FormatFactory.format(o.ymax(),format) + "")
                ;
            }
            case Z:{
                if(o.isUnconstrainedZ()){
                    return "";
                }
                return ("" + FormatFactory.format(o.zmin(),format) + "->" + FormatFactory.format(o.zmax(),format) + "")
                ;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }
    
}
