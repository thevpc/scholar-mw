/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.testfunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.HintAxisType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;

/**
 *
 * @author vpc
 */
public class ListTestFunctions extends TestFunctionsBase implements Cloneable{

    private List<Object> list = new ArrayList<Object>();

    public ListTestFunctions() {

    }

//    public ListTestFunctions(List<VDCxy> list) {
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

    public ListTestFunctions add(net.vpc.scholar.hadruwaves.mom.TestFunctions f) {
        if (f != null) {
            list.add(f);
        }
        return this;
    }

    public ListTestFunctions add(TVector<Expr> f) {
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

    public TList<Expr> toList(){
        TList<Expr> found=Maths.exprList();
        for (Object expr : list) {
            found.appendAll(linearizeFunctions(expr));
        }
        return found;
    }

    public DoubleToVector[] toArray(ComputationMonitor monitor){
        return Maths.invokeMonitoredAction(monitor, "Gp Detection", new MonitoredAction<DoubleToVector[]>() {
            @Override
            public DoubleToVector[] process(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                List<DoubleToVector> found=new ArrayList<DoubleToVector>();
                for (int i = 0; i < list.size(); i++) {
                    Object expr = list.get(i);
                    found.addAll(linearizeFunctions(expr));
                    monitor.setProgress(1.0*i/list.size(),"Gp Detection");
                }
                return found.toArray(new DoubleToVector[found.size()]);            }
        });
    }

    private List<DoubleToVector> linearizeFunctions(Object i){
        if(i instanceof Expr){
            if(i instanceof DoubleToVector){
                return Arrays.asList((DoubleToVector) i);
            }else if(i instanceof DoubleToComplex){
                return Arrays.asList(Maths.vector((DoubleToComplex) i));
            }else if(i instanceof DoubleToDouble) {
                return Arrays.asList(Maths.vector(((DoubleToDouble) i)));
            }else{
                throw new IllegalArgumentException("Unsupported Expr "+i);
            }
        }else if(i instanceof TVector && Expr.class.isAssignableFrom(((TVector) i).getComponentType())){
            List<DoubleToVector> found=new ArrayList<DoubleToVector>();
            for (Expr expr : ((TVector<Expr>) i)) {
                found.addAll(linearizeFunctions(expr));
            }
            return found;
        }else if(i instanceof net.vpc.scholar.hadruwaves.mom.TestFunctions){
            return Arrays.asList(((TestFunctions)i).arr());
        }else if(i instanceof List){
            List<DoubleToVector> found=new ArrayList<DoubleToVector>();
            for (Object expr : (List)i) {
                found.addAll(linearizeFunctions(expr));
            }
            return found;
        }else if(i instanceof Object[]){
            List<DoubleToVector> found=new ArrayList<DoubleToVector>();
            for (Object expr : (Object[])i) {
                found.addAll(linearizeFunctions(expr));
            }
            return found;
        }else{
            throw new IllegalArgumentException("Unsupported Expr "+i);
        }
    }

    @Override
    protected DoubleToVector[] gpImpl(ComputationMonitor monitor) {
        List<DoubleToVector> all = (List) toList().toJList();
        return all.toArray(new DoubleToVector[all.size()]);
    }

    @Override
    public TestFunctionsBase clone() {
        ListTestFunctions cloned =(ListTestFunctions) super.clone();
        cloned.list=new ArrayList<Object>();
        for (Object vdCxy : list) {
            cloned.list.add(vdCxy);
        }
        return cloned;
    }

    public Dumper getDumpStringHelper() {
        Dumper h = super.getDumpStringHelper();
        h.add("functions",list);
        return h;
    }

    public List<net.vpc.scholar.hadruwaves.mom.TestFunctions> getSubTestFunctions(){
        List<net.vpc.scholar.hadruwaves.mom.TestFunctions> found=new ArrayList<net.vpc.scholar.hadruwaves.mom.TestFunctions>();
        for (Object o : list) {
            if(o instanceof net.vpc.scholar.hadruwaves.mom.TestFunctions){
                found.add((net.vpc.scholar.hadruwaves.mom.TestFunctions) o);
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
    public void setAxisType(HintAxisType axisType) {
        super.setAxisType(axisType);
        for (TestFunctions testFunctions : getSubTestFunctions()) {
            testFunctions.setAxisType(axisType);
        }
    }

    protected DoubleToVector[] rebuildCachedFunctions(ComputationMonitor monitor) {
        for (TestFunctions testFunctions : getSubTestFunctions()) {
            testFunctions.setStructure(getStructure());
            testFunctions.setAxisType(getAxisType());
            testFunctions.setAxisType(getAxisType());
        }
        return super.rebuildCachedFunctions(monitor);
    }

    public ListTestFunctions addAll(ListTestFunctions other){
        list.addAll(other.list);
        return this;
    }
    public ListTestFunctions addAll(TVector<Expr> other){
        list.addAll(other.toJList());
        return this;
    }
    public ListTestFunctions addAll(List<Expr> other){
        list.addAll(other);
        return this;
    }
    public ListTestFunctions addAll(Expr[] other){
        list.addAll(Arrays.asList(other));
        return this;
    }
}
