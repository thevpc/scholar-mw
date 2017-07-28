package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.StringUtils;

/**
 * Created by vpc on 3/20/17.
 */
public class MemorySizeFormatter implements DoubleFormatter{

    boolean leadingZeros = false;
    boolean intermediateZeros = true;
    private boolean fixedLength = false;
    private boolean binaryPrefix = false;
    private long high = Maths.TERA;
    private long low = Maths.BYTE;

    public MemorySizeFormatter(boolean leadingZeros, boolean intermediateZeros, boolean fixedLength, boolean binaryPrefix, long high, long low) {
        this.leadingZeros = leadingZeros;
        this.intermediateZeros = intermediateZeros;
        this.fixedLength = fixedLength;
        this.binaryPrefix = binaryPrefix;
        this.high = high;
        this.low = low;
    }

    public MemorySizeFormatter() {
        this("B0TF");
    }

    public MemorySizeFormatter(String format) {
        leadingZeros=false;
        intermediateZeros=false;
        char low = '\0';
        char high = '\0';
        if (format != null) {
            boolean startInterval = true;
            char[] charArray = format.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = Character.toUpperCase(charArray[i]);
                switch (c) {
                    case 'B':
                    case 'K':
                    case 'M':
                    case 'G':
                    case 'T': {
                        if (startInterval) {
                            startInterval = false;
                            low = c;
                        } else {
                            high = c;
                        }
                        break;
                    }
                    case 'I': {
                        binaryPrefix = true;
                        break;
                    }
                    case 'F': {
                        fixedLength = true;
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
            low = 'B';
        }
        if (high == '\0') {
            high = 'T';
        }
        this.low = eval(low);
        this.high = eval(high);
        if (this.high < this.low) {
            long t=this.low;
            this.low=this.high;
            this.high=t;
        }
    }

    private long eval(char c) {
        switch (c) {
            case 'B': {
                return binaryPrefix ? Maths.BYTE : Maths.BYTE;
            }
            case 'K': {
                return binaryPrefix ? Maths.KiBYTE : Maths.KILO;
            }
            case 'M': {
                return binaryPrefix ? Maths.MiBYTE : Maths.MEGA;
            }
            case 'G': {
                return binaryPrefix ? Maths.GiBYTE : Maths.GIGA;
            }
            case 'T': {
                return binaryPrefix ? Maths.TiBYTE : Maths.TERA;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    private String formatLeft(Object number, int size) {
        if (fixedLength) {
            return StringUtils.formatLeft(number, size);
        } else {
            return String.valueOf(number);
        }
    }

    @Override
    public String formatDouble(double value) {
        return format((long)value);
    }

    public String format(long bytes) {
        StringBuilder sb = new StringBuilder();
        boolean neg = bytes < 0;
        long v = bytes < 0 ? -bytes : bytes;
        long r = v;

        boolean empty = true;
        if (low <= Maths.TERA) {
            if (high >= Maths.TERA) {
                r = v / Maths.TERA;
                if (r > 0 || (!empty && intermediateZeros)) {
                    if (sb.length() > 0) {
                        sb.append(" ");
                    }
                    sb.append(formatLeft(r, 3)).append("T");
                    v = v % Maths.TERA;
                    empty = false;
                }
            }
            if (low <= Maths.GIGA) {
                if (high >= Maths.GIGA) {
                    r = v / Maths.GIGA;
                }
                if ((leadingZeros && empty) || r > 0 || (!empty && intermediateZeros)) {
                    if (sb.length() > 0) {
                        sb.append(" ");
                    }
                    sb.append(formatLeft(r, 3)).append("G");
                    v = v % Maths.GIGA;
                    empty = false;
                }
                if (low <= Maths.MEGA) {
                    if (high >= Maths.MEGA) {
                        r = v / Maths.MEGA;
                        if ((leadingZeros && empty) || r > 0 || (!empty && intermediateZeros)) {
                            if (sb.length() > 0) {
                                sb.append(" ");
                            }
                            sb.append(formatLeft(r, 3)).append("M");
                            v = v % Maths.MEGA;
                            empty = false;
                        }
                    }
                    if (low <= Maths.KILO) {
                        if (high >= Maths.KILO) {
                            r = v / Maths.KILO;
                            if ((leadingZeros && empty) || r > 0 || (!empty && intermediateZeros)) {
                                if (sb.length() > 0) {
                                    sb.append(" ");
                                }
                                sb.append(formatLeft(r, 3)).append("K");
                                v = v % Maths.KILO;
                                empty = false;
                            }
                        }
                        if (low <= 1) {
                            if ((leadingZeros && empty) || v > 0 || sb.length() == 0 || (!empty && intermediateZeros)) {
                                if (sb.length() > 0) {
                                    sb.append(" ");
                                }
                                sb.append(formatLeft(v, 3)).append("B");
                                empty = false;
                            }
                        }
                    }
                }
            }
        }
        if (sb.length() == 0) {
            if (neg) {
                sb.insert(0, "-");
            }
            if (low >= Maths.TERA) {
                sb.append(formatLeft(0, 3)).append("T");
            } else if (low >= Maths.GIGA) {
                sb.append(formatLeft(0, 3)).append("G");
            } else if (low >= Maths.MEGA) {
                sb.append(formatLeft(0, 3)).append("M");
            } else if (low >= Maths.KILO) {
                sb.append(formatLeft(0, 3)).append("K");
                sb.append(formatLeft(0, 3)).append("B");
            }
        } else {
            if (neg) {
                sb.insert(0, "-");
            }
        }
        return sb.toString();
    }
}
