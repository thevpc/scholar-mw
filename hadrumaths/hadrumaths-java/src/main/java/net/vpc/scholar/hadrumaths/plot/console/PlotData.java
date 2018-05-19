package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamTarget;
import net.vpc.scholar.hadrumaths.plot.console.xlabels.XLabel;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 10 janv. 2007 14:53:09
 */
public class PlotData implements Serializable, Cloneable {

    private String title = "Unknown";
    private ConsoleAxisList axisList = new ConsoleAxisList();
    private ConsoleAwareObject structure;
    private ConsoleAwareObject structure2;
    private ArrayList<ParamSet> params = new ArrayList<ParamSet>();

    public PlotData(String windowTitle, ConsoleAwareObject structure, ConsoleAwareObject modele, ConsoleAxisList axisList, ParamSet... parameters) {
        setStructure(structure);
        setStructure2(modele);
        setAxisList(axisList);
        setParams(parameters);
        setWindowTitle(windowTitle);
    }

    public PlotData() {
    }

    public PlotData(PlotData other) {
        if (other != null) {
            this.title = other.title;
            this.axisList.addAll(other.axisList);
            this.structure = other.structure;
            this.structure2 = other.structure2;
            this.params.addAll(other.params);
        }
    }


    public PlotData setWindowTitle(Object anyObj) {
        this.title =
                anyObj == null ? null :
                        (anyObj instanceof CharSequence) ? ((CharSequence) anyObj).toString() :
                                (anyObj instanceof Class) ? ((Class) anyObj).getSimpleName() :
                                        anyObj.getClass().getSimpleName();
        return this;
    }

    public PlotData setAxisList(ConsoleAxisList axisList) {
        this.axisList = axisList;
        return this;
    }

    public PlotData setStructure(ConsoleAwareObject structure) {
        this.structure = structure;
        if (this.structure != null) {
            this.structure.setTarget(ParamTarget.REFERENCE);
        }
        return this;
    }

    public PlotData setStructure2(ConsoleAwareObject structure2) {
        this.structure2 = structure2;
        if (this.structure2 != null) {
            this.structure2.setTarget(ParamTarget.MODELED);
        }
        return this;
    }

    public PlotData setParams(ArrayList<ParamSet> params) {
        this.params = params;
        return this;
    }

    public ConsoleAxisList getAxisList() {
        return axisList;
    }

    public ConsoleAwareObject getStructure() {
        return structure;
    }

    public ConsoleAwareObject getStructure2() {
        return structure2;
    }

    public ParamSet[] getParams() {
        return params.toArray(new ParamSet[params.size()]);
    }

    public String getWindowTitle() {
        return title;
    }

    public PlotData addParams(ParamSet... params) {
        if (params.length > 0) {
            for (ParamSet param : params) {
                if (param != null) {
                    param.setIndex(this.params.size());
                    this.params.add(param);
                }
            }
        }
        return this;
    }

    public void replaceParams(ParamSet... params) {
        for (ParamSet newParam : params) {
            if (newParam != null) {
                for (int j = 0; j < this.params.size(); j++) {
                    ParamSet oldParam = this.params.get(j);
                    if (oldParam.getClass().equals(oldParam.getClass())) {
                        this.params.set(j, newParam);
                    }
                }
            }
        }
        setParams(getParams());
    }

    public void setParams(ParamSet... params) {
        this.params = new ArrayList<ParamSet>(params.length + 1);
        int index = 0;
        for (ParamSet param : params) {
            if (param != null) {
                param.setIndex(index++);
                this.params.add(param);
            }
        }
        Collections.sort(this.params);
    }

    public PlotData clone() {
        try {
            PlotData pd = (PlotData) super.clone();
            pd.axisList = pd.axisList == null ? null : pd.axisList.clone();
            pd.params = (pd.params == null) ? null : (ArrayList<ParamSet>) pd.params.clone();
            if (pd.structure2 != null) {
                pd.structure2 = pd.structure2.clone();
            }
            if (pd.structure != null) {
                pd.structure = pd.structure.clone();
            }
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    ParamSet param = params.get(i);
                    if (param != null) {
                        pd.params.set(i, param.clone());
                    }
                }
            }
            return pd;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


    public PlotData addAxis(ConsoleAxisList list) {
        getAxisList().addAll(list);
        return this;
    }

    public PlotData addAxis() {
        getAxisList().addAxis();
        return this;
    }

    public PlotData addAxis(ConsoleAxis axis) {
        getAxisList().addAxis(axis);
        return this;
    }

    public void resetAxis() {
        getAxisList().resetAxis();
    }

    public PlotData setX(ParamSet xAxis) {
        getAxisList().setX(xAxis);
        return this;
    }

    public PlotData setXLabel(XLabel xAxisLabel) {
        getAxisList().setXLabel(xAxisLabel);
        return this;
    }

    public void resetY() {
        getAxisList().resetY();
    }

    public void setY(PlotAxis... yAxises) {
        getAxisList().setY(yAxises);
    }

    public PlotData addY(PlotAxis... yAxises) {
        getAxisList().addY(yAxises);
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
        return getAxisList().getAxis();
    }

}
