package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.BytesSizeFormatter;
import net.vpc.common.util.DoubleFormatter;
import net.vpc.common.util.MetricFormatter;
import net.vpc.scholar.hadrumaths.util.StringUtils;

public class DoubleFormatterFactory {
    public static DoubleFormatter create(String format){
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
                return new FrequencyFormatter(subFormat);
            }
            case "m":
            case "metric": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Maths.Config.getMetricFormatter();
                }
                return new MetricFormatter(subFormat);
            }
            case "b":
            case "mem":
            case "memory": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Maths.Config.getMemorySizeFormatter();
                }
                return new BytesSizeFormatter(subFormat);
            }
            case "%":
            case "percent": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Maths.percentFormat();
                }
                return new DecimalDoubleFormatter(subFormat);
            }
            case "d":
            case "double": {
                if (StringUtils.isEmpty(subFormat)) {
                    return Maths.Config.getDoubleFormat();
                }
                return new DecimalDoubleFormatter(subFormat);
            }
        }
        if (StringUtils.isEmpty(subFormat)) {
            return Maths.Config.getDoubleFormat();
        }
        return new DecimalDoubleFormatter(subFormat);
    }
}
