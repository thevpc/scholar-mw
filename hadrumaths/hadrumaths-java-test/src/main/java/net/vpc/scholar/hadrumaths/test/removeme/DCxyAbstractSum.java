//package net.vpc.scholar.math.functions.cfxy;
//
//import java.util.ArrayList;
//import net.vpc.scholar.math.IDCxy;
//
//import net.vpc.scholar.math.functions.DomainXY;
//
///**
// * Created by IntelliJ IDEA. User: vpc Date: 24 juil. 2005 Time: 10:09:46 To
// * change this template use File | Settings | File Templates.
// */
//@Deprecated
//public abstract class DCxyAbstractSum extends DD2DC implements Cloneable {
//
//    private static final long serialVersionUID = -1010101010101001006L;
//    protected IDCxy[] segments;
//
//    protected DCxyAbstractSum(DomainXY domain) {
//        super(domain);
//    }
//
//    public int getSegmentCount() {
//        return segments.length;
//    }
//
//    public IDCxy getSegmentAt(int i) {
//        return segments[i];
//    }
//
//    public IDCxy[] getSegments() {
//        return segments;
//    }
//
//    public IDCxy[] getSegments(DomainXY domain) {
//        ArrayList<IDCxy> list = new ArrayList<IDCxy>(segments.length);
//        for (int i = 0; i < segments.length; i++) {
//            IDCxy segment = segments[i];
//            if (!segment.getDomain().intersect(domain).isEmpty()) {
//                list.add(segment);
//            }
//        }
//        return (IDCxy[]) list.toArray(new IDCxy[list.size()]);
//    }
//
//    @Override
//    public IDCxy clone() {
//        DCxyAbstractSum x = (DCxyAbstractSum) super.clone();
//        x.segments = new IDCxy[segments.length];
//        for (int i = 0; i < segments.length; i++) {
//            x.segments[i] = (IDCxy)segments[i].clone();
//        }
//        return x;
//    }
//}
