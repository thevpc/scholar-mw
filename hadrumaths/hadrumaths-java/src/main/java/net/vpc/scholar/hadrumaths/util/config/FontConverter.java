package net.vpc.scholar.hadrumaths.util.config;

import net.vpc.scholar.hadrumaths.util.Configuration;

import java.awt.*;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:55
 * To change this template use File | Settings | File Templates.
 */
public class FontConverter extends ConfigConverter {
    public FontConverter() {
        super(Font.class, "font");
    }

    public String objectToString(Object o) {
        return Configuration.getStringFromFont((Font) o);
    }

    public Object stringToObject(String s) throws ParseException {
        return Configuration.getFontFromString(s);
    }
}
