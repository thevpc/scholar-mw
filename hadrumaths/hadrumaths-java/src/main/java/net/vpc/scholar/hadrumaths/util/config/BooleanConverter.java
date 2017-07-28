package net.vpc.scholar.hadrumaths.util.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:42
 * To change this template use File | Settings | File Templates.
 */
public class BooleanConverter extends ConfigConverter {
    public BooleanConverter() {
        super(Boolean.class, "boolean");
    }

    public String objectToString(Object o) {
        return String.valueOf(o);
    }

    public Object stringToObject(String s) throws ParseException {
        return Boolean.valueOf(s);
    }
}
