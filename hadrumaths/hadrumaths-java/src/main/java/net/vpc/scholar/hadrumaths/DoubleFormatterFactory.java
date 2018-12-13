package net.vpc.scholar.hadrumaths;

import net.vpc.common.strings.StringUtils;
import net.vpc.common.util.BytesSizeFormat;
import net.vpc.common.util.DoubleFormat;
import net.vpc.common.util.FrequencyFormat;
import net.vpc.common.util.MetricFormat;

public class DoubleFormatterFactory {
    public static DoubleFormat create(String format){
        if (StringUtils.isEmpty(format)) {
            format = "";
        }
        String[] a = format.split(" ");
        String type = "";
        String subFormat = "";
        if (a.length == 0) {
            //
        } else {
            type = a[0];
            subFormat = format.substring(type.length());
        }
        switch (StringUtils.trim(format).toLowerCase()) {
            case "hz":
            case "freq":
            case "frequency": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Maths.Config.getFrequencyFormatter();
                }
                return new FrequencyFormat(subFormat);
            }
            case "m":
            case "metric": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Maths.Config.getMetricFormatter();
                }
                return new MetricFormat(subFormat);
            }
            case "b":
            case "mem":
            case "memory": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Maths.Config.getMemorySizeFormatter();
                }
                return new BytesSizeFormat(subFormat);
            }
            case "%":
            case "percent": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Maths.percentFormat();
                }
                return new DecimalDoubleFormat(subFormat);
            }
            case "d":
            case "double": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Maths.Config.getDoubleFormat();
                }
                return new DecimalDoubleFormat(subFormat);
            }
        }
        if (StringUtils.isEmpty(subFormat)) {
            return Maths.Config.getDoubleFormat();
        }
        return new DecimalDoubleFormat(subFormat);
    }
}
