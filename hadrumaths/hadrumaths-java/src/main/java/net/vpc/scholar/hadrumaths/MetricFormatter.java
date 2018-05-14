package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.StringUtils;

import java.text.DecimalFormat;

/**
 * Created by vpc on 3/20/17.
 */
public class MetricFormatter implements DoubleFormatter {

    boolean leadingZeros = false;
    boolean intermediateZeros = true;
    private boolean fixedLength = false;
    private boolean decimal = false;
    private int high = 6;
    private int low = -2;
    private int fractionDigits = 3;
    private int integerDigits = 3;
    private DecimalFormat decimalFormat = null;
    private int[] pows = {-12, -9, -6, -3, -2, -1, 0, 1, 2, 3, 6, 9, 12};

    public MetricFormatter(boolean leadingZeros, boolean intermediateZeros, boolean fixedLength, int high, int low, boolean decimal) {
        this.leadingZeros = leadingZeros;
        this.intermediateZeros = intermediateZeros;
        this.fixedLength = fixedLength;
        this.high = high;
        this.low = low;
        this.decimal = decimal;
    }

    public MetricFormatter() {
        this("M-3 M3 I2 D2");
    }

    public MetricFormatter(String format) {
        leadingZeros = false;
        intermediateZeros = false;
        int low = 0;
        int high = 0;
        if (format != null) {
            boolean startInterval = true;
            char[] charArray = format.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = Character.toUpperCase(charArray[i]);
                switch (c) {
                    case ' ': {
                        //ignore
                        break;
                    }
                    case 'M': {
                        int cc = 0;
                        if (i + 1 < charArray.length) {
                            i++;
                            boolean nn = false;
                            if (charArray[i] == '-') {
                                nn = true;
                                i++;
                            }
                            int r = 0;
                            if (i < charArray.length && Character.isDigit(charArray[i])) {
                                r = r * 10 + (charArray[i] - '0');
                                while (i + 1 < charArray.length && Character.isDigit(charArray[i+1])) {
                                    i++;
                                    r = r * 10 + (charArray[i] - '0');
                                }
                            } else {
                                throw new IllegalArgumentException("Invalid");
                            }
                            cc = nn ? -r : r;
                        } else {
                            throw new IllegalArgumentException("Invalid");
                        }
                        boolean validPow=false;
                        for (int pow : pows) {
                            if (pow == cc) {
                                validPow = true;
                                break;
                            }
                        }
                        if(!validPow){
                            throw new IllegalArgumentException("Invalid Dimension Power");
                        }
                        if (startInterval) {
                            startInterval = false;
                            low = cc;
                        } else {
                            high = cc;
                        }
                        break;
                    }
                    case 'D': {
                        decimal = true;
                        if (i + 1 < charArray.length && Character.isDigit(charArray[i + 1])) {
                            i++;
                            fractionDigits = charArray[i] - '0';
                        }
                        break;
                    }
                    case 'F': {
                        fixedLength = true;
                        break;
                    }
                    case 'I': {
                        if (i + 1 < charArray.length && Character.isDigit(charArray[i + 1])) {
                            i++;
                            integerDigits = charArray[i] - '0';
                        }
                        break;
                    }
                    case '0': {
                        if (i == 0) {
                            leadingZeros = true;
                        } else {
                            intermediateZeros = true;
                        }
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported " + c);
                    }
                }
            }
        }
        if (low == '\0') {
            low = 'H';
        }
        if (high == '\0') {
            high = 'T';
        }
        this.low = (low);
        this.high = (high);
        if (this.high < this.low) {
            int t = this.low;
            this.low = this.high;
            this.high = t;
        }
        //force decimal
        decimal=true;
        if (fixedLength) {
            decimalFormat = new DecimalFormat("0." + StringUtils.fillString('0', integerDigits));
        } else {
            decimalFormat = new DecimalFormat("0.0");
        }
    }

//    public static void main(String[] args) {
//        MetricFormatter frt = new MetricFormatter();
////        System.out.println(frt.format(0));
//        double f = 10E-6;
//        for (int i = 0; i < 10; i++) {
//            String s = frt.format(f);
//            System.out.println(s + " <== " + f);
//            f = f * 10;
//        }
//    }


    private String formatLeftPow(double number, int pow) {
        return formatLeft(number * 1.0 / Maths.pow(10, pow));
    }

    private String formatLeft(double number) {
        String s = (decimal) ?
                formatLeft(number, integerDigits + 1 + fractionDigits)
                : formatLeft(number, integerDigits);
//        System.out.println("formatLeft " + s + " <= " + number);
        return s;
    }

    private String formatLeft(Object number, int size) {
        if (!decimal) {
            if (fixedLength) {
                return StringUtils.formatLeft(number, size);
            } else {
                return String.valueOf(number);
            }
        } else {
            String s = decimalFormat.format(number);
            if (fixedLength) {
                return StringUtils.formatLeft(s, size);
            } else {
                return s;
            }
        }
    }

    @Override
    public String formatDouble(double value) {
        return format(value);
    }


    private String strUnit(int pow) {
        switch (pow) {
            case -12:
                return "pm";
            case -9:
                return "nm";
            case -6:
                return "um";
            case -3:
                return "mm";
            case -2:
                return "cm";
            case -1:
                return "dm";
            case 0:
                return "m";
            case 1:
                return "dam";
            case 2:
                return "hm";
            case 3:
                return "km";
            case 6:
                return "Mm";
            case 9:
                return "Gm";
            case 12:
                return "Tm";
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public String format(double value) {
        StringBuilder sb = new StringBuilder();
        boolean neg = value < 0;
        int sign = neg ? -1 : 1;
        double v = value < 0 ? -value : value;
        double r = v;
        if (decimal) {
            if (v == 0) {
                sb.append(formatLeft(0));
                sb.append(strUnit(low));
            } else {
                if (v < Maths.pow(10, low)) {
                    sb.append(formatLeftPow(v * sign, low));
                    sb.append(strUnit(low));
                } else if (v >= Maths.pow(10, high)) {
                    sb.append(formatLeftPow(v * sign, high)).append(strUnit(high));
                } else {
                    boolean ok=false;
                    for (int i = pows.length - 1; i >= 0; i--) {
                        double b = Maths.pow(10, pows[i]);
                        if (v >= b) {
                            sb.append(formatLeftPow(v * sign ,pows[i])).append(strUnit(pows[i]));
                            ok=true;
                            break;
                        }
                    }
                    if(!ok){
                        sb.append(formatLeftPow(v*sign,pows[0])).append(strUnit(pows[0]));
                    }
                }
            }
            return sb.toString();
        } else {
            throw new IllegalArgumentException("Not supported yet");
//            boolean empty = true;
//            if (low <= Maths.TERA) {
//                if (high >= Maths.TERA) {
//                    r = v / Maths.TERA;
//                    if (r > 0 || (!empty && intermediateZeros)) {
//                        if (sb.length() > 0) {
//                            sb.append(" ");
//                        }
//                        sb.append(formatLeft(r)).append("THz");
//                        v = v % Maths.TERA;
//                        empty = false;
//                    }
//                }
//                if (low <= Maths.GIGA) {
//                    if (high >= Maths.GIGA) {
//                        r = v / Maths.GIGA;
//                    }
//                    if ((leadingZeros && empty) || r > 0 || (!empty && intermediateZeros)) {
//                        if (sb.length() > 0) {
//                            sb.append(" ");
//                        }
//                        sb.append(formatLeft(r)).append("GHz");
//                        v = v % Maths.GIGA;
//                        empty = false;
//                    }
//                    if (low <= Maths.MEGA) {
//                        if (high >= Maths.MEGA) {
//                            r = v / Maths.MEGA;
//                            if ((leadingZeros && empty) || r > 0 || (!empty && intermediateZeros)) {
//                                if (sb.length() > 0) {
//                                    sb.append(" ");
//                                }
//                                sb.append(formatLeft(r)).append("MHz");
//                                v = v % Maths.MEGA;
//                                empty = false;
//                            }
//                        }
//                        if (low <= Maths.KILO) {
//                            if (high >= Maths.KILO) {
//                                r = v / Maths.KILO;
//                                if ((leadingZeros && empty) || r > 0 || (!empty && intermediateZeros)) {
//                                    if (sb.length() > 0) {
//                                        sb.append(" ");
//                                    }
//                                    sb.append(formatLeft(r)).append("KHz");
//                                    v = v % Maths.KILO;
//                                    empty = false;
//                                }
//                            }
//                            if (low <= 1) {
//                                if ((leadingZeros && empty) || v > 0 || sb.length() == 0 || (!empty && intermediateZeros)) {
//                                    if (sb.length() > 0) {
//                                        sb.append(" ");
//                                    }
//                                    sb.append(formatLeft(v)).append("Hz");
//                                    empty = false;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
//        if (sb.length() == 0) {
//            if (neg) {
//                sb.insert(0, "-");
//            }
//            sb.append(formatLeft(0)).append(strUnit(low));
//        } else {
//            if (neg) {
//                sb.insert(0, "-");
//            }
//        }
//        return sb.toString();
    }
}
