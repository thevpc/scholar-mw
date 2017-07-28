package net.vpc.scholar.hadrumaths.util.config;

import net.vpc.scholar.hadrumaths.util.Configuration;
import java.util.Date;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:47
 * To change this template use File | Settings | File Templates.
 */
public class DateConverter extends ConfigConverter {
    private Configuration configuration;

    public DateConverter(Configuration configuration) {
        super(Date.class, "date");
        this.configuration = configuration;
    }

    public String objectToString(Object o) {
        return configuration.getOptions().getDateFormat().format((Date) o);
    }

    public Object stringToObject(String s) throws ParseException {
        return configuration.getOptions().getDateFormat().parse(s);
    }
}
