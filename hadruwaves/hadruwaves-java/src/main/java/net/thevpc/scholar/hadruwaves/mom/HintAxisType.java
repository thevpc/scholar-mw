package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.AxisXY;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
* @creationtime 9 janv. 2007 16:23:20
*/
public enum HintAxisType {

    /**
     * ignore Y, 1D one axis functions following X
     */
    X_ONLY,
    /**
     *ignore X, 1D one axis functions following Y
     */
    Y_ONLY,
    /**
     *2D functions following X and Y
     * @Deprecated use GpTestFunctions.setFunctionsComparator(new GpXThenYComparator())
     */
//    @Deprecated
    XY,
    /**
     * 2D functions [X,Y]  for Fn
     * 2D functions [X,0] and [0,Y] for Gp, gp ordered according to index (GpX1,GpY1,GpX2,GpY2, ...)
     * @Deprecated use GpTestFunctions.setFunctionsComparator(new GpAlternateXYComparator())
     */
//    @Deprecated
//    XY_UNCOUPLED_GROUPED,
    /**
     * defaults!!!!
     * 2D functions [X,Y]  for Fn
     * 2D functions [X,0] and [0,Y] for Gp, gp ordered according to Axis (GpX1,GpX2,GpX3,...,GpY1,GpY2,GpY3, ...)
     */
//    XY_UNCOUPLED_UNGROUPED
    XY_SEPARATED;
    public AxisXY toAxisXY(){
        switch (this){
            case X_ONLY:return AxisXY.X;
            case Y_ONLY:return AxisXY.Y;
        }
        return AxisXY.XY;
    }
}
