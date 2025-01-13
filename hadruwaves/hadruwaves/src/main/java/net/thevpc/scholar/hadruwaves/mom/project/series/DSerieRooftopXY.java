package net.thevpc.scholar.hadruwaves.mom.project.series;

//package net.thevpc.scholar.tmwlib.mom.planar.series;
//
//import net.thevpc.scholar.math.functions.DomainXY;
//import net.thevpc.scholar.math.functions.dfxy.DFunctionXY;
//import net.thevpc.scholar.tmwlib.mom.planar.essai.AreaRooftop2D;
//
///**
// * Created by IntelliJ IDEA.
// * User: vpc
// * Date: 11 août 2005
// * Time: 14:49:16
// * To change this template use File | Settings | File Templates.
// */
//public class DSerieRooftopXY extends DSerieDXY implements Cloneable {
//    private AreaRooftop2D rooftop2D;
//    public DSerieRooftopXY(DomainXY domain) {
//        super(domain);
//        compile();
//    }
//
//    public void setMaxXIndex(int maxXIndex) {
//        super.setMaxXIndex(maxXIndex);
//        compile();
//    }
//
//    public void setMaxYIndex(int maxYIndex) {
//        super.setMaxYIndex(maxYIndex);
//        compile();
//    }
//
//    private void compile(){
//        rooftop2D = new AreaRooftop2D("NO_NAME", (getMaxXIndex()+1)/getDomain().width * (getMaxYIndex()+1)/getDomain().height, getMaxXIndex(), getMaxYIndex()+1, true, getDomain().xmin, getDomain().ymin, getDomain().width, getDomain().height);
//        rooftop2D.recompile();
//    }
//
//    public DFunctionXY getFunction(int n) {
//        return rooftop2D.getFunction(n).fx;
//    }
//
//    public int getMinIndex() {
//        return 0;
//    }
//
//    public int getMaxIndex() {
//        return rooftop2D.getGridX() * rooftop2D.getGridY();
//    }
//}
