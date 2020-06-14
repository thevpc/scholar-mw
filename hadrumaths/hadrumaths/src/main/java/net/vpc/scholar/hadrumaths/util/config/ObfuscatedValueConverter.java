package net.vpc.scholar.hadrumaths.util.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:12
 * To change this template use File | Settings | File Templates.
 */
public class ObfuscatedValueConverter extends ConfigConverter {
    public ObfuscatedValueConverter() {
        super(ObfuscatedValue.class, "obfuscated");
    }

    public String objectToString(Object o) {
        return ((ObfuscatedValue) o).val;
    }

    public Object stringToObject(String s) throws ParseException {
        return new ObfuscatedValue(s);
    }
}
