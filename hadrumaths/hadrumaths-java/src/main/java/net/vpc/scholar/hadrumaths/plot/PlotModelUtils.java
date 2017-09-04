package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Complex;

public class PlotModelUtils {


    public static String resolveYTitle(String[] ytitles, int i) {
        String t=null;
        if(ytitles!=null  && i<ytitles.length){
            t=ytitles[i];
        }
        if(t==null){
            t="Y";
        }
        return t;
    }

    public static String resolveYTitle(ValuesPlotModel model, int i) {
        String t=null;
        if(model.getYtitles()!=null  && i<model.getYtitles().length){
            t=model.getYtitles()[i];
        }
        if(t==null){
            t=model.getZtitle();
        }
        if(t==null){
            t="Y";
        }
        return t;
    }

    public static double[] mul(double[] all, double v){
        double[] a=new double[all.length];
        for (int i = 0; i < a.length; i++) {
            a[i]=all[i]*v;
        }
        return a;
    }

    public static Complex[] mul(Complex[] all, double v){
        Complex[] a=new Complex[all.length];
        for (int i = 0; i < a.length; i++) {
            a[i]=all[i].mul(v);
        }
        return a;
    }
}
