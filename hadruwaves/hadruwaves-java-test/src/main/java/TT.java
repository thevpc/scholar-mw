import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

public class TT {
    public static void main(String[] args) {
        Plot.xformat("freq")
                .ysamples(new double[]{1E9,2E9,3E9,4E9,5E9,6E9,7E9})
                .asCurve().plot(
                ArrayUtils.transpose(new double[]{1E9,5E9,6E9,2E9,7E9,10E9,3E9})
        );
    }
}
