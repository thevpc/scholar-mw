/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot;

/**
 * @author vpc
 */
public abstract class AbstractComponentPlotWindowManager extends AbstractPlotWindowManager {

    public AbstractComponentPlotWindowManager() {
    }

    public AbstractComponentPlotWindowManager(String globalTitle) {
        super(globalTitle);
    }

    public abstract PlotContainer getRootContainer();

    public PlotContainer getContainer(String[] path) {
        if (path.length == 0) {
            return getRootContainer();
        }
        return findOrCreateContainer(path, 0, getRootContainer());
    }

    private PlotContainer findOrCreateContainer(String[] path, int index, PlotContainer parent) {
        String name = path[index];
        int childIndex = parent.indexOfPlotComponent(name);
        PlotContainer p = null;
        if (childIndex < 0) {
            p = parent.add(name);
        } else {
            PlotComponent child = parent.getPlotComponent(name);
            if (child instanceof PlotContainer) {
                p = (PlotContainer) child;
            } else {
                p = parent.add(childIndex, name);
            }
        }
        if (index == path.length - 1) {
            return p;
        }
        return findOrCreateContainer(path, index + 1, p);
//
//        if (HadrumathsStringUtils.isInt(name)) {
//
//
//            if (i < 0) {
//                throw new IllegalArgumentException("Invalid index " + i);
//            } else if (i < c) {
//            } else {
//                PlotContainer p = parent.add(String.valueOf(i));
//                if (index == path.length - 1) {
//                    return p;
//                }
//                return findOrCreateContainer(path,index+1,p);
//            }
//        } else {
//            throw new IllegalArgumentException("Not yet supported non numeric path");
//        }
    }

    public void removePlotComponentImpl(PlotComponent component) {
        getRootContainer().remove(component);
    }

    public void addPlotComponentImpl(PlotComponent component, String[] path) {
        getContainer(path).add(component);
    }


}
