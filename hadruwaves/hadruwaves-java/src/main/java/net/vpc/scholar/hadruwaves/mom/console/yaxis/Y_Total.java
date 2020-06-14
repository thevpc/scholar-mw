package net.vpc.scholar.hadruwaves.mom.console.yaxis;

//package net.vpc.scholar.tmwlib.mom.console.yaxis;
//
//import net.vpc.scholar.math.plot.PlotType;
//import net.vpc.scholar.math.Complex;
//import net.vpc.scholar.math.Chronometer;
//import net.vpc.scholar.tmwlib.mom.str.AbstractStructure2D;
//import net.vpc.scholar.math.plot.plotconsole.xlabels.XLabel;
//import net.vpc.scholar.tmwlib.mom.console.*;
//import net.vpc.scholar.math.plot.plotconsole.paramsets.ParamSet;
//
///**
// * @author : vpc
// * @creationtime 5 févr. 2006 15:07:36
// */
//public abstract class Y_Total extends PlotAxis implements Cloneable{
//    private int factor = 1;
//    protected double progress = 0;
//    public String plotTitle = null;
//
//    protected Y_Total(String name, Type type) {
//        super(name, type);
//    }
//
//    protected Y_Total(String name, Type type, PlotType graphix) {
//        super(name, type, graphix);
//    }
//
//    protected abstract Complex computeComplexArg(AbstractStructure2D structure, ParamSet x);
//
//    public ConsoleAction createConsoleAction(ComputeTitle serieTitle, AbstractStructure2D direct, AbstractStructure2D modele, ConsoleAxis axis, PlotConsole newPlot, WindowPath preferredPath) {
//        PlotMatrix yvalues;
//        Chronometer chronometer = new Chronometer();
//        chronometer.start();
////        x1values = getX(direct, modele, x_axis);
////        x2values = getY(direct, modele, x_axis);
//        yvalues = compute(direct, modele, axis, newPlot);
//        chronometer.stop();
//        String st = this.toString();
//        if (st.length() > 0 && serieTitle.toString().length() > 0) {
//            st += " : ";
//        }
//        st += serieTitle;
//        switch (getType()) {
//            case DIRECT: {
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " : ";
//                }
//                st += "Fn" + direct.modeFunctionsCount();
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " / ";
//                }
//                st += "Gp" + direct.getGpTestFunctions().count();
//                break;
//            }
//            case MODEL: {
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " : ";
//                }
//                st += "Fn" + modele.modeFunctionsCount();
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " / ";
//                }
//                st += "Gp" + modele.getGpTestFunctions().count();
//                break;
//            }
//            case ERREUR_RELATIVE: {
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " : dir ";
//                }
//                st += "Fn" + direct.modeFunctionsCount();
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " / ";
//                }
//                st += "Gp" + direct.getGpTestFunctions().count();
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " : mod ";
//                }
//                st += "Fn" + modele.modeFunctionsCount();
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " / ";
//                }
//                st += "Gp" + modele.getGpTestFunctions().count();
//                break;
//            }
//            case ERREUR: {
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " : dir ";
//                }
//                st += "Fn" + direct.modeFunctionsCount();
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " / ";
//                }
//                st += "Gp" + direct.getGpTestFunctions().count();
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " : mod ";
//                }
//                st += "Fn" + modele.modeFunctionsCount();
//                if (st.length() > 0 && serieTitle.toString().length() > 0) {
//                    st += " / ";
//                }
//                st += "Gp" + modele.getGpTestFunctions().count();
//                break;
//            }
//        }
//
////        String xaxisName = axis.getXLabel() != null ? (axis.getXLabel().getName()) : (axis.getX() != null ? axis.getX().getName() : "-");
////        String plotTitle = getPlotTitle() == null ? (this.toString() + "/" + xaxisName) : getPlotTitle();
//        return new ConsoleActionPlot(yvalues, st, getInfiniteValue(), getPlotType(), getName(),preferredPath);
//
//    }
//
//    public PlotMatrix compute(AbstractStructure2D direct, AbstractStructure2D modele, ConsoleAxis axis, PlotConsole newPlot) {
//        PlotMatrix ret = null;
//        switch (getType()) {
//            case DIRECT: {
//                factor = 1;
//                progress = 0;
//                ret = computeMatrix(direct, axis, newPlot);
//                break;
//            }
//            case MODEL: {
//                factor = 1;
//                progress = 0;
//                ret = modele == null ? null : computeMatrix(modele, axis, newPlot);
//                break;
//            }
//            case ERREUR_RELATIVE: {
//                factor = 2;
//                progress = 0;
//                PlotMatrix d0 = computeMatrix(direct, axis, newPlot);
//                PlotMatrix m0 = computeMatrix(modele, axis, newPlot);
//                Complex[][] d = d0.getMatrix();
//                Complex[][] m = m0.getMatrix();
//                Complex[][] c = new Complex[d.length][];
//                for (int i = 0; i < c.length; i++) {
//                    c[i] = new Complex[d[i].length];
//                    for (int j = 0; j < c[i].length; j++) {
//                        c[i][j] = (d[i][j].substract(m[i][j])).divide(d[i][j]).multiply(100);
//                    }
//                }
//                ret = new PlotMatrix(c, d0.getColumnsDimension(), d0.getRowsDimension());
//                break;
//            }
//            case ERREUR: {
//                factor = 2;
//                progress = 0;
//                PlotMatrix d0 = computeMatrix(direct, axis, newPlot);
//                PlotMatrix m0 = computeMatrix(modele, axis, newPlot);
//                Complex[][] d = d0.getMatrix();
//                Complex[][] m = m0.getMatrix();
//                Complex[][] c = new Complex[d.length][];
//                for (int i = 0; i < c.length; i++) {
//                    c[i] = new Complex[d[i].length];
//                    for (int j = 0; j < c[i].length; j++) {
//                        c[i][j] = (d[i][j].substract(m[i][j]));
//                    }
//                }
//                ret = new PlotMatrix(c, d0.getColumnsDimension(), d0.getRowsDimension());
//                break;
//            }
//        }
//        if (!Double.isNaN(getMultiplier()) && ret != null) {
//            ret.multiply(getMultiplier());
//        }
//        return ret;
//    }
//
//    protected PlotMatrix computeMatrix(AbstractStructure2D structure, ConsoleAxis axis, PlotConsole newPlot) {
//        ParamSet x = axis.getX();
//        XLabel xlabel = axis.getXLabel();
//        Complex[] z = new Complex[x.getSize()];
//        double[] xs = new double[x.getSize()];
//        int index = 0;
//        x.reset();
//        while (x.hasNext()) {
//            x.next();
//            x.setParameter(structure);
//            z[index] = computeComplexArg(structure, x);
//            if (xlabel == null) {
//                Object v = x.getValue();
//                xs[index] = (v instanceof Number) ? ((Number) v).doubleValue() : index;
//            } else {
//                Number v = xlabel.getValue(index, x, structure);
//                xs[index] = v.doubleValue();
//            }
//            progress += (100.0) / z.length;
//            index++;
//        }
//        return new PlotMatrix(new Complex[][]{z}, xs, new double[]{1});
//    }
//
//
//    public String getPlotTitle() {
//        return plotTitle;
//    }
//
//    public Y_Total setPlotTitle(String plotTitle) {
//        this.plotTitle = plotTitle;
//        return this;
//    }
//
//
//    public double getProgressValue() {
//        return progress / factor;
//    }
//}
