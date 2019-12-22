package net.vpc.scholar.hadrumaths;

import net.vpc.common.strings.StringUtils;
import net.vpc.common.util.*;

public class DoubleFormatterFactory {
    public static DoubleFormat create(String format){
        if (StringUtils.isBlank(format)) {
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
                if (StringUtils.isBlank(subFormat)) {
                    return MathsBase.Config.getFrequencyFormatter();
                }
                return new FrequencyFormat(subFormat);
            }
            case "m":
            case "metric": {
                if (StringUtils.isBlank(subFormat)) {
                    return MathsBase.Config.getMetricFormatter();
                }
                return new MetricFormat(subFormat);
            }
            case "b":
            case "mem":
            case "memory": {
                if (StringUtils.isBlank(subFormat)) {
                    return MathsBase.Config.getMemorySizeFormatter();
                }
                return new BytesSizeFormat(subFormat);
            }
            case "%":
            case "percent": {
                if (StringUtils.isBlank(subFormat)) {
                    return MathsBase.percentFormat();
                }
                return new DecimalDoubleFormat(subFormat);
            }
            case "d":
            case "double": {
                if (StringUtils.isBlank(subFormat)) {
                    return MathsBase.Config.getDoubleFormat();
                }
                return new DecimalDoubleFormat(subFormat);
            }
        }
        if (StringUtils.isBlank(subFormat)) {
            return MathsBase.Config.getDoubleFormat();
        }
        return new DecimalDoubleFormat(subFormat);
    }
}
