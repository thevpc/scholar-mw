package net.thevpc.scholar.hadrumaths.util.config;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:51
 * To change this template use File | Settings | File Templates.
 */
public class TimeConverter extends ConfigConverter {
    private final DecimalFormat hms = new DecimalFormat("00");
    private final DecimalFormat S = new DecimalFormat("000");

    public TimeConverter() {
        super(Time.class, "time");
    }

    public String objectToString(Object o) {
        if (o == null) {
            return "";
        }
        Date d = (Date) o;
        int[] parts = getDateTimeParts(d);
        if (parts[6] == 0) {
            if (parts[5] == 0) {
                return hms.format(parts[3]) + ":" + hms.format(parts[4]);
            } else {
                return hms.format(parts[3]) + ":" + hms.format(parts[4]) + ":" + hms.format(parts[5]);
            }
        } else {
            return hms.format(parts[3]) + ":" + hms.format(parts[4]) + ":" + hms.format(parts[5]) + ":" + S.format(parts[6]);
        }
    }

    private static int[] getDateTimeParts(Date date) {
        int[] parts = new int[7];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        parts[0] = calendar.get(Calendar.YEAR);
        parts[1] = calendar.get(Calendar.MONTH) + 1;
        parts[2] = calendar.get(Calendar.DAY_OF_MONTH);
        parts[3] = calendar.get(Calendar.HOUR_OF_DAY);
        parts[4] = calendar.get(Calendar.MINUTE);
        parts[5] = calendar.get(Calendar.SECOND);
        parts[6] = calendar.get(Calendar.MILLISECOND);
        return parts;
    }


    public Object stringToObject(String s) throws ParseException {
        if (s == null || s.length() == 0) {
            return null;
        }
        int hour;
        int minute;
        int second;
        int milliseconds;
        int firstColon;
        int secondColon;
        int thirdColon;

        firstColon = s.indexOf(':');
        secondColon = s.indexOf(':', firstColon + 1);
        thirdColon = s.indexOf(':', secondColon + 1);
        if ((firstColon > 0) && (secondColon > 0) && (thirdColon > 0) && (thirdColon < s.length() - 1)) {
            hour = Integer.parseInt(s.substring(0, firstColon));
            minute = Integer.parseInt(s.substring(firstColon + 1, secondColon));
            second = Integer.parseInt(s.substring(secondColon + 1, thirdColon));
            milliseconds = Integer.parseInt(s.substring(thirdColon + 1));

        } else if ((firstColon > 0) && (secondColon > 0) && (secondColon < s.length() - 1)) {
            hour = Integer.parseInt(s.substring(0, firstColon));
            minute = Integer.parseInt(s.substring(firstColon + 1, secondColon));
            second = Integer.parseInt(s.substring(secondColon + 1));
            milliseconds = 0;

        } else if ((firstColon > 0) && (firstColon < s.length() - 1)) {
            hour = Integer.parseInt(s.substring(0, firstColon));
            minute = Integer.parseInt(s.substring(firstColon + 1));
            second = 0;
            milliseconds = 0;
        } else {
            throw new IllegalArgumentException();
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1900);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, milliseconds);
        return new Time(c.getTime().getTime());
    }
}
