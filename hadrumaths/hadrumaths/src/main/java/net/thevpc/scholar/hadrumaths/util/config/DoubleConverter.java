package net.thevpc.scholar.hadrumaths.util.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:30
 * To change this template use File | Settings | File Templates.
 */
public class DoubleConverter extends ConfigConverter {
    public DoubleConverter() {
        super(Double.class, "double");
    }

    public String objectToString(Object o) {
        return String.valueOf(o);
    }

    public Object stringToObject(String s) throws ParseException {
        return new Double(s);
    }
}
