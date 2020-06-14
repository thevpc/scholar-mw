package net.vpc.scholar.hadrumaths.util.config;

import java.text.ParseException;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:19:32
 * To change this template use File | Settings | File Templates.
 */
public class LocaleConverter extends ConfigConverter {
    public LocaleConverter() {
        super(Locale.class, "locale");
    }

    public String objectToString(Object o) {
        return String.valueOf(o);
    }

    public Object stringToObject(String s) throws ParseException {
        return getLocaleFromString(s);
    }

    public Locale getLocaleFromString(String locale) {
        StringTokenizer st = new StringTokenizer(locale, "_ ,;:/");
        int tcount = st.countTokens();
        String a1 = "";
        String a2 = "";
        String a3 = "";
        if (tcount < 1 || tcount > 3)
            throw new RuntimeException("bad locale : " + locale);
        if (st.hasMoreTokens()) {
            a1 = st.nextToken();
            if (st.hasMoreTokens()) {
                a2 = st.nextToken();
                if (st.hasMoreTokens()) {
                    a2 = st.nextToken();
                    if (st.hasMoreTokens())
                        throw new RuntimeException("bad locale : " + locale);
                }
            }
        } else {
            throw new RuntimeException("bad locale : " + locale);
        }
        return new Locale(a1, a2, a3);
    }

}
