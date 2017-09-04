package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ValuesPlotXComplexModelFace {
    private String title;
    private double[][] x;
    private Complex[][] y;
    private String[] ytitles;

    public ValuesPlotXComplexModelFace(ValuesPlotModel model, PlotConfig plotConfig) {
        this.title=model.getTitle();

        double[][] xAxis = model.getX();
        Complex[][] yAxis = model.getZ();

        List<double[]> xAxisList = new ArrayList<>();
        List<Complex[]> yAxisList = new ArrayList<>();
        List<String> yTitleList = new ArrayList<>();
        if(plotConfig==null){
            plotConfig = (PlotConfig) model.getProperty("config", null);
            plotConfig=PlotConfig.copy(plotConfig).validate(model.getZ().length);
        }
        if(yAxis==null){
            //do nothing
        }else{
            double defaultXMultiplier=plotConfig.getDefaultXMultiplier(1);
            if(xAxis==null){
                for (int i = 0; i < yAxis.length; i++) {
                    if(model.getYVisible(i)){
                        double xmultiplier=plotConfig.getXMultiplierAt(i,1)*defaultXMultiplier;
                        double ymultiplier=plotConfig.getYMultiplierAt(i,1);
                        xAxisList.add(PlotModelUtils.mul(Maths.dsteps(1,yAxis[i].length-1),xmultiplier));
                        yAxisList.add(PlotModelUtils.mul(yAxis[i],ymultiplier));
                        yTitleList.add(PlotModelUtils.resolveYTitle(model, i));
                    }
                }
            }else {
                for (int i = 0; i < yAxis.length; i++) {
                    if(model.getYVisible(i)) {
                        double xmultiplier=plotConfig.getXMultiplierAt(i,1)*defaultXMultiplier;
                        double ymultiplier=plotConfig.getYMultiplierAt(i,1);
                        yAxisList.add(PlotModelUtils.mul(yAxis[i],ymultiplier));
                        yTitleList.add(PlotModelUtils.resolveYTitle(model, i));
                        if (i >= xAxis.length || xAxis[i] == null || xAxis[i].length == 0 || xAxis[i].length<yAxis[i].length) {
                            if (xAxisList.isEmpty() || yAxis[i].length != xAxisList.get(xAxisList.size() - 1).length) {
                                xAxisList.add(PlotModelUtils.mul(Maths.dsteps(1, yAxis[i].length - 1),xmultiplier));
                            } else {
                                xAxisList.add(xAxisList.get(xAxisList.size() - 1));
                            }
                        }else{
                            xAxisList.add(PlotModelUtils.mul(xAxis[i],xmultiplier));
                        }
                    }
                }
            }
        }
        x = xAxisList.toArray(new double[xAxisList.size()][]);
        y = yAxisList.toArray(new Complex[yAxisList.size()][]);

        //rename titles with the same name
        HashSet<String> visited=new HashSet<>();
        for (int i = 0; i < yTitleList.size(); i++) {
            String key=yTitleList.get(i);
            int keyIndex = 1;
            while (true) {
                String kk = keyIndex==1 ? key : (key+" "+keyIndex);
                if (!visited.contains(kk)) {
                    visited.add(kk);
                    key = kk;
                    break;
                }
                visited.add(kk);
                keyIndex++;
            }
            yTitleList.set(i,key);
        }
        ytitles =yTitleList.toArray(new String[yTitleList.size()]);
    }

    //    public ValuesPlotXDoubleModelFace(double[] x, double[] y, double[][] z, String title) {
//        this.x = x;
//        this.y = y;
//        this.z = z;
//        this.title = title;
//    }


    public double[] getX(int series) {
        return x[series];
    }

    public String getYTitle(int series){
        return ytitles[series];
    }
    public Complex[] getY(int series) {
        return y[series];
    }

    public int size() {
        return y.length;
    }

    public int getSeriesLength() {
        int len=0;
        for (Complex[] doubles : y) {
            if(doubles.length>len){
                len=doubles.length;
            }
        }
        return len;
    }

    public String getTitle() {
        return title;
    }
}
