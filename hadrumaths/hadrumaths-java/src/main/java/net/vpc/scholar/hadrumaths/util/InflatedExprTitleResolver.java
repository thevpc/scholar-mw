package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.Expr;

import java.util.Map;

public interface InflatedExprTitleResolver {
    String resolveTitle(Map<String,Object> values,int index);
}
