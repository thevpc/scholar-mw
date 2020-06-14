package net.vpc.scholar.hadrumaths.util.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:19:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class ConfigConverter {
    Class acceptedClass;
    String acceptedType;

    ConfigConverter(Class acceptedClass, String acceptedType) {
        this.acceptedClass = acceptedClass;
        this.acceptedType = acceptedType;
    }

    public String getAcceptedType() {
        return acceptedType;
    }

    public Class getAcceptedClass() {
        return acceptedClass;
    }

    public abstract String objectToString(Object o);

    public abstract Object stringToObject(String o) throws ParseException;
}
