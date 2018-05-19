package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.xlabels.XLabel;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 10 janv. 2007 14:48:17
 */
public class ConsoleAxisList implements Iterable<ConsoleAxis>, Serializable, Cloneable {
    private ArrayList<ConsoleAxis> consoleAxises = new ArrayList<ConsoleAxis>();

    public ConsoleAxisList() {
    }

    public ConsoleAxisList(ParamSet xAxis) {
        setX(xAxis);
    }

    public Iterator<ConsoleAxis> iterator() {
        return consoleAxises.iterator();
    }

    public void addAll(ConsoleAxisList list) {
        consoleAxises.addAll(list.consoleAxises);
    }

    public void addAxis() {
        addAxis(new ConsoleAxis());
    }

    public ConsoleAxisList addAxis(ConsoleAxis axis) {
        consoleAxises.add(axis);
        return this;
    }

    public void resetAxis() {
        consoleAxises.clear();
    }

    public ConsoleAxisList setX(ParamSet xAxis) {
        getAxis().setX(xAxis);
        return this;
    }

    public void setXLabel(XLabel xAxisLabel) {
        getAxis().setXLabel(xAxisLabel);
    }

    public void resetY() {
        getAxis().resetY();
    }

    public void setY(PlotAxis... yAxises) {
        getAxis().setY(yAxises);
    }

    public ConsoleAxisList addY(PlotAxis... yAxises) {
        getAxis().addY(yAxises);
        return this;
    }

    //    public ParamSet getX() {
//        return getAxis().getX();
//    }
//
//
//    public XLabel getXLabel() {
//        return xAxisLabel;
//    }
//
//    public ArrayList<PlotAxis> getY() {
//        return yAxises;
//    }
    public ConsoleAxis getAxis() {
        if (consoleAxises.size() == 0) {
            consoleAxises.add(new ConsoleAxis());
        }
        return consoleAxises.get(consoleAxises.size() - 1);
    }

    public ConsoleAxisList clone() {
        try {
            ConsoleAxisList list = (ConsoleAxisList) super.clone();
            for (int i = 0; i < list.consoleAxises.size(); i++) {
                ConsoleAxis param = list.consoleAxises.get(i);
                if (param != null) {
                    list.consoleAxises.set(i, param.clone());
                }
            }
            return list;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int size() {
        return consoleAxises.size();
    }

}
