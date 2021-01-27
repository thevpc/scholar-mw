package net.thevpc.scholar.hadrumaths.util.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:19:54
 * To change this template use File | Settings | File Templates.
 */
public class IntConverter extends ConfigConverter {
    public IntConverter() {
        super(Integer.class, "int");
    }

    public String objectToString(Object o) {
        return String.valueOf(o);
    }

    public Object stringToObject(String s) throws ParseException {
        return new Integer(s);
    }
}
