//package net.thevpc.scholar.tmwlib.mom.project.utils;
//
//import net.thevpc.scholar.math.functions.DomainXY;
//import net.thevpc.scholar.math.functions.dfxy.DFunctionXY;
//import net.thevpc.scholar.tmwlib.mom.project.common.Area;
//
//public class SimpleArea extends Area {
//    DFunctionXY[] functions;
//
//    public SimpleArea(DomainXY domain, DFunctionXY[] functions) {
//        this(null, domain, functions);
//    }
//
//    public SimpleArea(String name, DomainXY domain, DFunctionXY[] functions) {
//        super(null, name == null ? "Area" : name,
//                String.valueOf(domain.xmin),
//                String.valueOf(domain.ymin),
//                String.valueOf(domain.xmax - domain.xmin),
//                String.valueOf(domain.ymax - domain.ymin));
//        this.functions = functions;
////        setStructureContext(new StructureContext());
//        recompile();
//    }
//
//    @Override
//    public int getFunctionMax() {
//        return functions.length;
//    }
//
//}
