//package net.thevpc.scholar.hadrumaths;
//
//import net.thevpc.common.strings.StringUtils;
//
//public class DoubleFormatterFactory {
//    public static DoubleFormat create(String format) {
//        if (NBlankable.isBlank(format)) {
//            format = "";
//        }
//        String[] a = format.split(" ");
//        String type = "";
//        String subFormat = "";
//        if (a.length == 0) {
//            //
//        } else {
//            type = a[0];
//            subFormat = format.substring(type.length());
//        }
//        switch (NStringUtils.trim(format).toLowerCase()) {
//            case "hz":
//            case "freq":
//            case "frequency": {
//                if (NBlankable.isBlank(subFormat)) {
//                    return Maths.Config.getFrequencyFormatter();
//                }
//                return new FrequencyFormat(subFormat);
//            }
//            case "m":
//            case "metric":
//            case "dimension":
//                {
//                if (NBlankable.isBlank(subFormat)) {
//                    return Maths.Config.getMetricFormatter();
//                }
//                return new MetricFormat(subFormat);
//            }
//            case "b":
//            case "mem":
//            case "memory": {
//                if (NBlankable.isBlank(subFormat)) {
//                    return Maths.Config.getMemorySizeFormatter();
//                }
//                return new BytesSizeFormat(subFormat);
//            }
//            case "%":
//            case "percent": {
//                if (NBlankable.isBlank(subFormat)) {
//                    return Maths.percentFormat();
//                }
//                return new DecimalDoubleFormat(subFormat);
//            }
//            case "d":
//            case "double": {
//                if (NBlankable.isBlank(subFormat)) {
//                    return Maths.Config.getDoubleFormat();
//                }
//                return new DecimalDoubleFormat(subFormat);
//            }
//        }
//        if (NBlankable.isBlank(subFormat)) {
//            return Maths.Config.getDoubleFormat();
//        }
//        return new DecimalDoubleFormat(subFormat);
//    }
//}
