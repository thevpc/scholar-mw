package net.vpc.scholar.hadruplot.console;

import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.xlabels.XLabel;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 25 oct. 2006 02:32:48
 */
public class ConsoleAxis implements Serializable, Cloneable {
    private ParamSet xAxis;
    private XLabel xAxisLabel;
    private ArrayList<PlotAxis> yAxises = new ArrayList<PlotAxis>();

    public ConsoleAxis() {

    }

    public ConsoleAxis(ParamSet xAxis, PlotAxis... yAxises) {
        setX(xAxis);
        setY(yAxises);
    }

    public void setX(ParamSet xAxis) {
        this.xAxis = xAxis;
    }

    public void setXLabel(XLabel xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public void resetY() {
        this.yAxises = new ArrayList<PlotAxis>();
    }

    public void setY(PlotAxis... yAxises) {
        this.yAxises = yAxises == null ? new ArrayList<PlotAxis>() : new ArrayList<PlotAxis>(Arrays.asList(yAxises));
    }

    public void addY(PlotAxis... yAxises) {
        if (yAxises.length > 0) {
            this.yAxises.addAll(Arrays.asList(yAxises));
        }
    }

    public ParamSet getX() {
        return xAxis;
    }


    public XLabel getXLabel() {
        return xAxisLabel;
    }

    public ArrayList<PlotAxis> getY() {
        return yAxises;
    }

    public ConsoleAxis clone() {
        try {
            ConsoleAxis list = (ConsoleAxis) super.clone();
            if (list.xAxis != null) {
                list.xAxis = list.xAxis.clone();
            }
            for (int i = 0; i < list.yAxises.size(); i++) {
                PlotAxis param = list.yAxises.get(i);
                if (param != null) {
                    list.yAxises.set(i, param.clone());
                }
            }
            return list;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
