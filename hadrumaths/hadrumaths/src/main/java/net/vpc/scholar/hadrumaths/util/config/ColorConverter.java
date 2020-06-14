package net.vpc.scholar.hadrumaths.util.config;

import java.awt.*;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:58
 * To change this template use File | Settings | File Templates.
 */
public class ColorConverter extends ConfigConverter {
    public ColorConverter() {
        super(Color.class, "color");
    }

    public String objectToString(Object o) {
        return Configuration.getStringFromColor((Color) o);
    }

    public Object stringToObject(String s) throws ParseException {
        return Configuration.getColorFromString(s);
    }
}
