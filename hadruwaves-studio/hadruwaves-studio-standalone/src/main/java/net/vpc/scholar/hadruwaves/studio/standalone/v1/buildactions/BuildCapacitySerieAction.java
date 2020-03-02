package net.vpc.scholar.hadruwaves.studio.standalone.v1.buildactions;

import net.vpc.common.log.Log;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public class BuildCapacitySerieAction extends RunAction {
    private MomStrHelper jxy;
    int progress_percent_build = 0;
    int progress_percent_build_max;

    private double[] freqs;
    private int index1;
    private int index2;
    private String iteratorName;

    public BuildCapacitySerieAction(MomStrHelper jxy, String iteratorName, double[] freqs, int index1, int index2) {
        this.jxy = jxy;
        this.freqs=freqs;
        this.index1=index1;
        this.index2=index2;
        this.iteratorName=iteratorName;
        progress_percent_build_max=freqs.length;
    }


    @Override
    public double getProgress() {
        return (((double) progress_percent_build) / ((double) progress_percent_build_max));
    }

    public Complex go0() {
        return (Complex) go();
    }

    public Object run() {
        // compute
        Complex[] complexes = new Complex[freqs.length];
        int pente = 0;
        for (int i = 0; i < complexes.length; i++) {
            jxy.getStructureConfig().updateExpression(iteratorName,String.valueOf(freqs[i]));
            //jxy.getStructureConfig().setFrequenceExpression(String.valueOf(freqs[i] / jxy.getStructureConfig().getFrequenceUnit()));
            jxy.recompile();
            complexes[i] = jxy.evalCapacity(ProgressMonitors.none()).get(index1,index2);
            if (i > 0) {
                int pente2 = (int) Math.signum(complexes[i].absdbl()- complexes[i - 1].absdbl());
                if (i > 1) {
                    if (pente2 < pente) {
                        Log.trace("Possible resonant frequence " + freqs[i - 1]);
                    }
                }
                pente = pente2;
            }
            progress_percent_build++;
        }
        return complexes;
    }

}
