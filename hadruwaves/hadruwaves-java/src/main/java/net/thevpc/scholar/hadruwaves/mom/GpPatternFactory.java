package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.AbstractFactory;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.EchelonPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.PolyhedronPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.RWGPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.RooftopPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.SinxCosxPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.SisiCocoPattern;

/**
 * @author : vpc
 * @creationtime 3 févr. 2006 12:14:25
 */
public final class GpPatternFactory extends AbstractFactory{
    public final static GpPattern POLYEDRE = new PolyhedronPattern(true, true);
    public final static GpPattern ECHELON = new EchelonPattern();
    public final static GpPattern RWG10 = new RWGPattern(10);
    public final static GpPattern RWG20 = new RWGPattern(20);
    public final static GpPattern RWG50 = new RWGPattern(50);
    public final static GpPattern RWG = new RWGPattern();
    public final static GpPattern RWG100 = new RWGPattern(100);
    public final static GpPattern RWG200 = new RWGPattern(200);
    public final static GpPattern RWG600 = new RWGPattern(600);

    public final static GpPattern ROOFTOP = new RooftopPattern(false, false);
    public final static GpPattern ROOFTOP_ATTACHXY = new RooftopPattern(true, true);
    public final static GpPattern ROOFTOP_ATTACHX = new RooftopPattern(true, false);
    public final static GpPattern ROOFTOP_ATTACHY = new RooftopPattern(false, true);

//    public final static GpPattern ARCHE_SINUS = new ArcheSinusPattern(false, false);
//    public final static GpPattern ARCHE_SINUS_ATTACHXY = new ArcheSinusPattern(true, true);
//    public final static GpPattern ARCHE_SINUS_ATTACHX = new ArcheSinusPattern(true, false);
//    public final static GpPattern ARCHE_SINUS_ATTACHY = new ArcheSinusPattern(false, true);
//
//    public final static GpPattern ARCHE_SINUS2 = new ArcheSinus2Pattern(false, false);
//    public final static GpPattern ARCHE_SINUS2_ATTACHXY = new ArcheSinus2Pattern(true, true);
//    public final static GpPattern ARCHE_SINUS2_ATTACHX = new ArcheSinus2Pattern(true, false);
//    public final static GpPattern ARCHE_SINUS2_ATTACHY = new ArcheSinus2Pattern(false, true);

    public static GpPattern SISI_COCO(int complexity) {
        return new SisiCocoPattern(complexity);
    }

    private GpPatternFactory() {

    }

    //    public final static GpPattern ROOFTOP2=new GpPattern() {
//        public int getCount(int complexity) {
//            return 2;
//        }
//
//        public CFunctionXY2D createFunction(int index, DomainXY domain2, DomainXY globalDomain) {
//            return
//                    new CFunctionXY2D(
//                            new CFunctionXY(FunctionFactory.rooftopX(1,index==0,domain2)),
//                            new CFunctionXY(FunctionFactory.rooftopX(1,index==0,domain2))
//                    );
//        }
//    };

//    public final static GpPattern SINUS=new GpPattern() {
//        public int getCount(int complexity) {
//            return complexity;
//        }
//
//        public CFunctionXY2D createFunction(int index, DomainXY localDomain, DomainXY globalDomain) {
//            return  new CFunctionXY2D(
//                    new CFunctionXY(Physics.fnMagnetic(index, localDomain)),
//                    new CFunctionXY(Physics.fnMagnetic(index, localDomain))
//            );
//        }
//    };
//
//    public final static GpPattern SINUS_PIECE=new GpPattern() {
//        public int getCount(int complexity) {
//            return 1;
//        }
//
//        public CFunctionXY2D createFunction(int index, DomainXY domain2, DomainXY globalDomain) {
//            return new CFunctionXY2D(
//                    new CFunctionXY(FunctionFactory.sinPiece(domain2)),
//                    new CFunctionXY(FunctionFactory.sinPiece(domain2))
//            );
//        }
//    };


    public static GpPattern SINX_COSX(int complexity) {
        return new SinxCosxPattern(complexity);
    }


}
