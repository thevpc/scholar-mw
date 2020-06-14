package net.vpc.scholar.hadruplot.console;

import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.xlabels.XLabel;
import net.vpc.scholar.hadruplot.console.params.ParamTarget;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by vpc on 2/6/17.
 */
public class PlotDataBuilder implements Serializable, Cloneable {
    private PlotConsole console;
    private String title = "Unknown";
    private ConsoleAxisList axisList = new ConsoleAxisList();
    private ConsoleAwareObject structure;
    private ConsoleAwareObject structure2;
    private ArrayList<ParamSet> params = new ArrayList<ParamSet>();

    public PlotDataBuilder(PlotConsole console) {
        this.console = console;
    }

    public PlotConsole plot() {
        console.run(
                new PlotData()
                        .setWindowTitle(title)
                        .addAxis(axisList)
                        .setStructure(structure)
                        .setStructure2(structure2)
                        .setParams(params)
        );
        return console;
    }

    public PlotDataBuilder setWindowTitle(Object anyObj) {
        this.title =
                anyObj == null ? null :
                        (anyObj instanceof CharSequence) ? ((CharSequence) anyObj).toString() :
                                (anyObj instanceof Class) ? ((Class) anyObj).getSimpleName() :
                                        anyObj.getClass().getSimpleName();
        return this;
    }

    public PlotDataBuilder setAxisList(ConsoleAxisList axisList) {
        this.axisList = axisList;
        return this;
    }

    public PlotDataBuilder setStructure(ConsoleAwareObject structure) {
        this.structure = structure;
        if (this.structure != null) {
            this.structure.setTarget(ParamTarget.REFERENCE);
        }
        return this;
    }

    public PlotDataBuilder setStructure2(ConsoleAwareObject structure2) {
        this.structure2 = structure2;
        if (this.structure2 != null) {
            this.structure2.setTarget(ParamTarget.MODELED);
        }
        return this;
    }

    public PlotDataBuilder setParams(ArrayList<ParamSet> params) {
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
        return params.toArray(new ParamSet[0]);
    }

    public String getWindowTitle() {
        return title;
    }

    public PlotDataBuilder addParams(ParamSet... params) {
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

    public PlotDataBuilder replaceParams(ParamSet... params) {
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
        return setParams(getParams());
    }

    public PlotDataBuilder setParams(ParamSet... params) {
        this.params = new ArrayList<ParamSet>(params.length + 1);
        int index = 0;
        for (ParamSet param : params) {
            if (param != null) {
                param.setIndex(index++);
                this.params.add(param);
            }
        }
        Collections.sort(this.params);
        return this;
    }

    public PlotDataBuilder clone() {
        try {
            PlotDataBuilder pd = (PlotDataBuilder) super.clone();
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


    public PlotDataBuilder addAxis(ConsoleAxisList list) {
        getAxisList().addAll(list);
        return this;
    }

    public PlotDataBuilder addAxis() {
        getAxisList().addAxis();
        return this;
    }

    public PlotDataBuilder addAxis(ConsoleAxis axis) {
        getAxisList().addAxis(axis);
        return this;
    }

    public void resetAxis() {
        getAxisList().resetAxis();
    }

    public PlotDataBuilder setX(ParamSet xAxis) {
        getAxisList().setX(xAxis);
        return this;
    }

    public PlotDataBuilder setXLabel(XLabel xAxisLabel) {
        getAxisList().setXLabel(xAxisLabel);
        return this;
    }

    public void resetY() {
        getAxisList().resetY();
    }

    public void setY(PlotAxis... yAxises) {
        getAxisList().setY(yAxises);
    }

    public PlotDataBuilder addY(PlotAxis... yAxises) {
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
