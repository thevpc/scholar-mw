/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.Plus;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

import java.util.List;

/**
 * @author vpc
 */
public class PlusFormatter implements Formatter<Plus> {

    @Override
    public String format(Plus o, FormatParam... format) {
        List<Expr> segments = o.getSubExpressions();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segments.size(); i++) {
            Expr e = segments.get(i);
            if (i > 0) {
                sb.append(" + ");
            }
            sb.append(FormatFactory.formatArg(e, format));
        }
        return sb.toString();
    }


}
