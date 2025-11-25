/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.testfunctions;

import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.Vector;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.mom.HintAxisType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;

import java.util.*;
import net.thevpc.scholar.hadrumaths.symbolic.ExprDefaults;

/**
 * @author vpc
 */
public class ListTestFunctions extends TestFunctionsBase implements Cloneable {

    private List<Object> list = new ArrayList<Object>();

    public ListTestFunctions() {

    }

//    public ListTestFunctions(PList<VDCxy> list) {
//        if (list != null) {
//            for (VDCxy x : list) {
//                if (x != null) {
//                    this.list.add(x);
//                }
//            }
//        }
//    }
    public ListTestFunctions add(Expr f) {
        if (f != null) {
            list.add(f);
        }
        return this;
    }

    public ListTestFunctions add(net.thevpc.scholar.hadruwaves.mom.TestFunctions f) {
        if (f != null) {
            list.add(f);
        }
        return this;
    }

    public ListTestFunctions add(Vector<Expr> f) {
        if (f != null) {
            list.add(f);
        }
        return this;
    }

    public ListTestFunctions add(List<Expr> f) {
        if (f != null) {
            list.add(f);
        }
        return this;
    }

    public ListTestFunctions add(Expr[] f) {
        if (f != null) {
            list.add(f);
        }
        return this;
    }

    public DoubleToVector[] toArray(ProgressMonitor monitor) {
        return Maths.invokeMonitoredAction(monitor, "Gp Detection", new MonitoredAction<DoubleToVector[]>() {
            @Override
            public DoubleToVector[] process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                List<DoubleToVector> found = new ArrayList<DoubleToVector>();
                for (int i = 0; i < list.size(); i++) {
                    Object expr = list.get(i);
                    found.addAll(linearizeFunctions(expr));
                    monitor.setProgress(1.0 * i / list.size(), "Gp Detection");
                }
                return found.toArray(new DoubleToVector[0]);
            }
        });
    }

    private List<DoubleToVector> linearizeFunctions(Object i) {
        if (i instanceof Expr) {
            Expr ie = (Expr) i;
            switch (ie.getType()) {
                case DOUBLE_DOUBLE: {
                    DoubleToVector v = Maths.vector(ie.toDD()).toDV();
                    v = (DoubleToVector) ExprDefaults.copyProperties(ie, v);
                    return Arrays.asList(v);
                }

                case DOUBLE_COMPLEX: {
                    DoubleToVector v = Maths.vector(ie.toDC()).toDV();
                    v = (DoubleToVector) ExprDefaults.copyProperties(ie, v);
                    return Arrays.asList(v);
                }

                case DOUBLE_CVECTOR: {
                    DoubleToVector v = ie.toDV();
                    v = (DoubleToVector) ExprDefaults.copyProperties(ie, v);
                    return Arrays.asList(v);
                }

            }
            throw new IllegalArgumentException("Unsupported Expr " + i);
        } else if (i instanceof Vector && Maths.$EXPR.isAssignableFrom(((Vector) i).getComponentType())) {
            List<DoubleToVector> found = new ArrayList<DoubleToVector>();
            for (Expr expr : ((Vector<Expr>) i)) {
                found.addAll(linearizeFunctions(expr));
            }
            return found;
        } else if (i instanceof net.thevpc.scholar.hadruwaves.mom.TestFunctions) {
            return Arrays.asList(((TestFunctions) i).arr());
        } else if (i instanceof List) {
            List<DoubleToVector> found = new ArrayList<DoubleToVector>();
            for (Object expr : (List) i) {
                found.addAll(linearizeFunctions(expr));
            }
            return found;
        } else if (i instanceof Object[]) {
            List<DoubleToVector> found = new ArrayList<DoubleToVector>();
            for (Object expr : (Object[]) i) {
                found.addAll(linearizeFunctions(expr));
            }
            return found;
        } else {
            throw new IllegalArgumentException("Unsupported Expr " + i);
        }
    }

    public List<net.thevpc.scholar.hadruwaves.mom.TestFunctions> getSubTestFunctions() {
        List<net.thevpc.scholar.hadruwaves.mom.TestFunctions> found = new ArrayList<net.thevpc.scholar.hadruwaves.mom.TestFunctions>();
        for (Object o : list) {
            if (o instanceof net.thevpc.scholar.hadruwaves.mom.TestFunctions) {
                found.add((net.thevpc.scholar.hadruwaves.mom.TestFunctions) o);
            }
        }
        return found;
    }

    @Override
    public void setStructure(MomStructure structure) {
        super.setStructure(structure);
        for (TestFunctions testFunctions : getSubTestFunctions()) {
            testFunctions.setStructure(structure);
        }
    }

    @Override
    public void invalidateCache() {
        super.invalidateCache();
        for (TestFunctions testFunctions : getSubTestFunctions()) {
            testFunctions.invalidateCache();
        }
    }

    @Override
    protected DoubleToVector[] gpImpl(ProgressMonitor monitor) {
        List<DoubleToVector> all = (List) toList().toList();
        return all.toArray(new DoubleToVector[all.size()]);
    }

    protected DoubleToVector[] rebuildCachedFunctions(ProgressMonitor monitor) {
        for (TestFunctions testFunctions : getSubTestFunctions()) {
            testFunctions.setStructure(getStructure());
            testFunctions.setAxisType(getAxisType());
            testFunctions.setAxisType(getAxisType());
        }
        return super.rebuildCachedFunctions(monitor);
    }

    @Override
    public TestFunctionsBase clone() {
        ListTestFunctions cloned = (ListTestFunctions) super.clone();
        cloned.list = new ArrayList<Object>();
        for (Object vdCxy : list) {
            cloned.list.add(vdCxy);
        }
        return cloned;
    }

    @Override
    public void setAxisType(HintAxisType axisType) {
        super.setAxisType(axisType);
        for (TestFunctions testFunctions : getSubTestFunctions()) {
            testFunctions.setAxisType(axisType);
        }
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder h = super.toElement().toObject().get().builder();
        h.add("functions", NElementHelper.elem(list));
        return h.build();
    }

    public Vector<Expr> toList() {
        Vector<Expr> found = Maths.evector();
        for (Object expr : list) {
            found.appendAll(linearizeFunctions(expr));
        }
        return found;
    }

    public ListTestFunctions addAll(ListTestFunctions other) {
        list.addAll(other.list);
        return this;
    }

    public ListTestFunctions addAll(Vector<Expr> other) {
        list.addAll(other.toList());
        return this;
    }

    public ListTestFunctions addAll(List<Expr> other) {
        list.addAll(other);
        return this;
    }

    public ListTestFunctions addAll(Expr[] other) {
        list.addAll(Arrays.asList(other));
        return this;
    }

    @Override
    public Geometry[] getGeometries() {
        Set<Geometry> ss = new HashSet<>();
        List<DoubleToVector> found = new ArrayList<DoubleToVector>();
        for (Object expr : list) {
            for (DoubleToVector linearizeFunction : linearizeFunctions(expr)) {
                ss.add(linearizeFunction.getDomain().toGeometry());
            }
        }
        return ss.toArray(new Geometry[0]);
    }
}
