/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;

/**
 * @author vpc
 */
public interface DoubleDomainExpr extends Expr {


    default boolean contains(double x) {
        return getDomain().contains(x);
    }

    default boolean contains(double x, double y) {
        return getDomain().contains(x, y);
    }

    default boolean contains(double x, double y, double z) {
        return getDomain().contains(x, y, z);
    }


}
