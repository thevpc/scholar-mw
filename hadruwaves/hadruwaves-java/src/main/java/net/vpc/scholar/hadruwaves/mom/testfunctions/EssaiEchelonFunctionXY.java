package net.vpc.scholar.hadruwaves.mom.testfunctions;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.Domain;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 14 juil. 2005
 * Time: 10:17:53
 * To change this template use File | Settings | File Templates.
 */
public class EssaiEchelonFunctionXY extends DFunction2XY implements Cloneable{
    public EssaiEchelonFunctionXY(Domain domain, double bandx, double bandWidth) {
        super(domain, bandx, bandWidth);
        setFunctions(createTest(bandx,
                bandx + bandWidth,
                domain.ymin(),
                domain.ymax()),
                createTest(domain.xmax() - (bandx - domain.xmin()) - bandWidth,
                        domain.xmax() - (bandx - domain.xmin()),
                        domain.ymin(),
                        domain.ymax()));
        band1 = segments[0];
        band2 = segments[1];
    }

    private DoubleToDouble createTest(double minx, double maxx, double miny, double maxy) {
        return expr(Math.sqrt(1 / (maxx - minx)), Domain.forBounds(minx,
                maxx, miny,
                maxy));
    }

    public void recompile(){
       setFunctions(band1,band2); 
    }
}
