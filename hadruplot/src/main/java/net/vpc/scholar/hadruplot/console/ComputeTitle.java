package net.vpc.scholar.hadruplot.console;

import net.vpc.scholar.hadruplot.console.params.ParamSet;

import java.io.Serializable;
import java.util.Formatter;

public class ComputeTitle implements Serializable {
    StringBuilder sb = new StringBuilder();
    private boolean forStatic;
    Formatter formatter = new Formatter(sb);

    public ComputeTitle(boolean forStatic) {
        this.forStatic = forStatic;
    }

    public void addAlways(String name, Object value) {

        if (sb.length() > 0) {
            sb.append(";");
        }
        sb.append(name).append("=").append(value);
    }

    public void add(ParamSet param) {
        if (forStatic) {
            if (param.getSize() == 1) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(param.getTitle());
            }
        } else {
            if (param.getSize() > 1) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(param.getTitle());
            }
        }
    }

    public void add(String name, double[] values) {
        if (forStatic) {
            if (values.length == 1) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(name).append("=").append(values[0]);
            }
        }
    }

    public void add(String name, int[] values) {
        if (forStatic) {
            if (values.length == 1) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(name).append("=").append(values[0]);
            }
        }
    }

    public void addFormatted(String format, double[] values, int index, Object... otherValues) {
        if (!forStatic) {
            if (values.length > 1) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                Object[] all = new Object[otherValues.length + 1];
                all[0] = values[index];
                System.arraycopy(otherValues, 0, all, 1, otherValues.length);
                formatter.format(format, all);
            }
        }
    }

    public void addFormatted(String format, int[] values, int index, Object... otherValues) {
        if (!forStatic) {
            if (values.length > 1) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                Object[] all = new Object[otherValues.length + 1];
                all[0] = values[index];
                System.arraycopy(otherValues, 0, all, 1, otherValues.length);
                formatter.format(format, all);
            }
        }
    }

    public void add(String name, double[] values, int index) {
        if (!forStatic) {
            if (values.length > 1) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(name).append("=").append(values[index]);
            }
        }
    }

    public void add(String name, int[] values, int index) {
        if (!forStatic) {
            if (values.length > 1) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(name).append("=").append(values[index]);
            }
        }
    }

    public String toString() {

        return sb.length() == 0 ? "" : "(" + sb.toString() + ")";
    }
}
