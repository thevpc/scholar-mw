package net.vpc.scholar.hadruwaves.mom.testfunctions;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.HintAxisType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;
import net.vpc.scholar.hadruwaves.mom.str.TestFunctionsComparator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 10:11:39
 */
public abstract class TestFunctionsBase implements Serializable, Dumpable, net.vpc.scholar.hadruwaves.mom.TestFunctions {
    /**
     * les fonctions d'cache_essai
     */
    private DoubleToVector[] cachedFunctions;
    private Domain cachedDomain;
    private HintAxisType axisType;

//    private boolean autoshrink = false;
//    private double autoshrinkEpsilon = 1E-10;
    //    private boolean needRebuild = true;

    private transient MomStructure structure;
    private TestFunctionsComparator functionsComparator;
    private PropertyChangeSupport pcs;
    private Boolean complex = null;

    protected TestFunctionsBase() {
        pcs = new PropertyChangeSupport(this);
    }

    @Override
    public MomStructure getStructure() {
        return structure;
    }

    @Override
    public void setStructure(MomStructure structure) {
        this.structure = structure;
        if (this.structure != null) {
            setAxisType(structure.getHintsManager().getHintAxisType());
        }
        invalidateCache();
    }

    @Override
    public void invalidateCache() {

        cachedFunctions = null;
        cachedDomain = null;
    }

    @Override
    public DoubleToVector gp(int p) {
        return arr()[p];
    }

    @Override
    public DoubleToVector get(int p) {
        return arr()[p];
    }

    @Override
    public TList<Expr> list() {
        return Maths.exprList(arr());
    }

    @Override
    public DoubleToVector apply(int index) {
        return gp(index);
    }

    @Override
    public DoubleToVector[] arr() {
        return arr(null, null);
    }

    @Override
    public DoubleToVector[] arr(ComputationMonitor monitor, ObjectCache objectCache) {
        if (cachedFunctions == null) {
            if (objectCache != null) {
                try {
                    cachedFunctions = (DoubleToVector[]) objectCache.load("test-functions", null);
                } catch (Exception ex) {
                    //ignore
                }
                if (cachedFunctions != null) {
                    if (cachedFunctions.length == 0) {
                        throw new IllegalArgumentException("No Test Functions defined.");
                    }
                    reEvalComplexField();
                    return cachedFunctions;
                }
            }

            cachedFunctions = rebuildCachedFunctions(monitor);
            reEvalComplexField();
            if (objectCache != null) {
                try {
                    objectCache.store("test-functions", cachedFunctions, monitor);
                } catch (Exception ex) {
                    //ignore
                }
            }
        }
        if (cachedFunctions.length == 0) {
            throw new IllegalArgumentException("No Test Functions defined.");
        }
        return cachedFunctions;
    }

    @Override
    public Domain getDomain() {
        if (cachedDomain == null) {
            Domain d = Domain.NaNX;
            for (DoubleToVector t : arr()) {
                d = d.expand(t.getDomain());
            }
            cachedDomain = d.toDomain(getDomain().getDimension());
        }
        return cachedDomain;
    }

    protected abstract DoubleToVector[] gpImpl(ComputationMonitor monitor);

    protected DoubleToVector[] rebuildCachedFunctions(ComputationMonitor monitor) {
//        if(structure==null){
//            throw new IllegalArgumentException("Unable to evaluate Test Functions since 'structure' is null");
//        }
        EnhancedComputationMonitor emon = ComputationMonitorFactory.enhance(monitor);
        DoubleToVector[] gpImpl = gpImpl(emon);
        DoubleToVector[] validGpImpl = null;
        HintAxisType fnAxis = getAxisType();
        if (fnAxis == null) {
            fnAxis = HintAxisType.XY;
        }
        switch (fnAxis) {
            case XY_SEPARATED: {
                ArrayList<DoubleToVector> goodX = new ArrayList<DoubleToVector>();
                ArrayList<DoubleToVector> goodY = new ArrayList<DoubleToVector>();
                int xi = 1;
                int yi = 1;
                for (DoubleToVector cFunctionXY2D : gpImpl) {
                    Map<String, Object> properties0 = cFunctionXY2D.hasProperties() ? cFunctionXY2D.getProperties() : null;
                    if (!cFunctionXY2D.getComponent(Axis.X).isZero()) {
                        HashMap<String, Object> properties = new HashMap<String, Object>();
                        if (properties0 != null) {
                            properties.putAll(properties0);
                        }
                        properties.put("Axis", "X");
                        properties.put("AxisIndex", xi);
                        xi++;
                        DoubleToVector d = Maths.vector(cFunctionXY2D.getComponent(Axis.X), FunctionFactory.CZEROXY);
                        d = (DoubleToVector) d.setTitle("[X] " + cFunctionXY2D.getTitle());
                        d = (DoubleToVector) d.setProperties(properties);
                        goodX.add(d);
                    }
                    if (!cFunctionXY2D.getComponent(Axis.Y).isZero()) {
                        HashMap<String, Object> properties = new HashMap<String, Object>();
                        if (properties0 != null) {
                            properties.putAll(properties0);
                        }
                        properties.put("Axis", "Y");
                        properties.put("AxisIndex", yi);
                        yi++;
                        DoubleToVector o = Maths.vector(FunctionFactory.CZEROXY, cFunctionXY2D.getComponent(Axis.Y));
                        o = (DoubleToVector) o.setTitle("[Y] " + cFunctionXY2D.getTitle());
                        o = (DoubleToVector) o.setProperties(properties);
                        goodY.add(o);
                    }
                }
                goodX.addAll(goodY);
                int index = 1;
                for (int i = 0; i < goodX.size(); i++) {
                    DoubleToVector cFunctionXY2D = goodX.get(i);
                    goodX.set(i, (DoubleToVector) cFunctionXY2D.setProperty("Index", index++));
                }
                validGpImpl = goodX.toArray(new DoubleToVector[goodX.size()]);
                break;
            }
//            case XY_UNCOUPLED_GROUPED: {
//                ArrayList<IVDCxy> goodXY = new ArrayList<IVDCxy>();
//                int xi = 1;
//                int yi = 1;
//                for (IVDCxy cFunctionXY2D : gpImpl) {
//                    Map<String, Object> properties0 = cFunctionXY2D.getProperties();
//                    if (!cFunctionXY2D.get(Axis.X).isZero()) {
//                        LinkedHashMap<String, Object> properties = new LinkedHashMap<String, Object>();
//                        if (properties0 != null) {
//                            properties.putAll(properties0);
//                        }
//                        properties.put("Axis", "X");
//                        properties.put("Index", xi);
//                        properties.put("AxisIndex", xi);
//                        IVDCxy d = Maths.vector(cFunctionXY2D.get(Axis.X), FunctionFactory.CZEROXY);
//                        d.setTitle("[X] " + cFunctionXY2D.getName());
//                        d.getProperties().putAll(properties);
//                        goodXY.add(d);
//                        xi++;
//                    }
//                    if (!cFunctionXY2D.get(Axis.Y).isZero()) {
//                        LinkedHashMap<String, Object> properties = new LinkedHashMap<String, Object>();
//                        if (properties0 != null) {
//                            properties.putAll(properties0);
//                        }
//                        properties.put("Axis", "Y");
//                        properties.put("Index", yi);
//                        properties.put("AxisIndex", yi);
//                        IVDCxy o = Maths.vector(FunctionFactory.CZEROXY, cFunctionXY2D.get(Axis.Y));
//                        o.setTitle("[Y] " + cFunctionXY2D.getName());
//                        o.getProperties().putAll(properties);
//                        goodXY.add(o);
//                        yi++;
//                    }
//                }
//                int index = 1;
//                for (IVDCxy cFunctionXY2D : goodXY) {
//                    cFunctionXY2D.getProperties().put("Index", index++);
//                }
//                validGpImpl = goodXY.toArray(new IVDCxy[goodXY.size()]);
//                break;
//            }
            case XY: {
                validGpImpl = gpImpl;
                for (int i = 0; i < validGpImpl.length; i++) {
                    DoubleToVector cFunctionXY2D = validGpImpl[i];
                    Map<String, Object> properties = cFunctionXY2D.getProperties();
                    properties.put("Axis", "XY");
                    properties.put("Index", i);
                    properties.put("AxisIndex", i + 1);
                    //cFunctionXY2D.setProperties(properties);
                }
                break;
            }
            case X_ONLY: {
                ArrayList<DoubleToVector> newFcts = new ArrayList<DoubleToVector>();
                int index = 1;
                for (DoubleToVector cFunctionXY2D : gpImpl) {
                    if (!cFunctionXY2D.getComponent(Axis.X).isZero()) {
                        cFunctionXY2D = Maths.vector(cFunctionXY2D.getComponent(Axis.X), Maths.DCZERO);
                        Map<String, Object> properties = cFunctionXY2D.getProperties();
                        properties.put("Axis", "X");
                        properties.put("Index", index);
                        properties.put("AxisIndex", index);
//                        cFunctionXY2D.setProperties(properties);
                        newFcts.add(cFunctionXY2D);
                        index++;
                    }
                }
                validGpImpl = newFcts.toArray(new DoubleToVector[newFcts.size()]);
                break;
            }
            case Y_ONLY: {
                ArrayList<DoubleToVector> newFcts = new ArrayList<DoubleToVector>();
                int index = 1;
                for (DoubleToVector cFunctionXY2D : gpImpl) {
                    if (!cFunctionXY2D.getComponent(Axis.Y).isZero()) {
                        cFunctionXY2D = Maths.vector(Maths.DDZERO, cFunctionXY2D.getComponent(Axis.Y));
                        Map<String, Object> properties = cFunctionXY2D.getProperties();
//                        if (properties == null) {
//                            properties = new LinkedHashMap<String, Object>();
//                        }
                        properties.put("Axis", "Y");
                        properties.put("Index", newFcts.size() + 1);
                        properties.put("AxisIndex", index);
//                        cFunctionXY2D.setProperties(properties);
                        newFcts.add(cFunctionXY2D);
                        index++;
                    }
                }
                validGpImpl = newFcts.toArray(new DoubleToVector[newFcts.size()]);
                break;
            }
        }
        TestFunctionsComparator comp = getFunctionsComparator();
        if (comp != null) {
            Arrays.sort(validGpImpl, comp);
        }
        for (DoubleToVector cFunctionVector2D : validGpImpl) {
            Map<String, Object> properties = cFunctionVector2D.getProperties();
            properties.put("Domain", cFunctionVector2D.getComponent(Axis.X).getDomain().expand(cFunctionVector2D.getComponent(Axis.Y).getDomain()));
            properties.put("DomainX", cFunctionVector2D.getComponent(Axis.X).getDomain());
            properties.put("DomainY", cFunctionVector2D.getComponent(Axis.Y).getDomain());
        }
        return validGpImpl;
//            for (int i = 0; i < cachedFunctions.length; i++) {
//                cachedFunctions[i]=cachedFunctions[i].simplify();
//
//            }
    }

    private void reEvalComplexField() {
        if (cachedFunctions == null) {
            throw new IllegalArgumentException("Unable to reeval Complex Field");
        }
        complex = true;
        for (DoubleToVector cFunctionVector2D : cachedFunctions) {
            if (!cFunctionVector2D.getComponent(Axis.X).toDC().getImag().isZero() || !cFunctionVector2D.getComponent(Axis.Y).toDC().getImag().isZero()) {
                complex = false;
                break;
            }
        }
    }


    @Override
    public TestFunctions clone() {
        try {
            return (TestFunctionsBase) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public int count() {
        return arr().length;
    }

    @Override
    public int length() {
        return count();
    }

    @Override
    public int size() {
        return count();
    }

    @Override
    public boolean isComplex() {
        if (complex == null) {
            throw new IllegalArgumentException("I dont know yet");
        }
        return complex;
    }

    @Override
    public HintAxisType getAxisType() {
        return axisType;
    }

    @Override
    public void setAxisType(HintAxisType axisType) {
        HintAxisType old=this.axisType;
        this.axisType = axisType;
        firePropertyChange("axisType",old,axisType);
    }

    public Dumper getDumpStringHelper() {
        Dumper h = new Dumper(getClass().getSimpleName());
        h.add("axisType", axisType);
        if (functionsComparator != null) {
            h.add("functionsComparator", functionsComparator);
        }
        return h;
    }

    @Override
    public final String dump() {
        return getDumpStringHelper().toString();
    }

    @Override
    public TestFunctionsComparator getFunctionsComparator() {
        return functionsComparator;
    }

    @Override
    public void setFunctionsComparator(TestFunctionsComparator functionsComparator) {
        TestFunctionsComparator old=this.functionsComparator;
        this.functionsComparator = functionsComparator;
        firePropertyChange("functionsComparator",old,functionsComparator);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        invalidateCache();
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    @Override
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }


    @Override
    public synchronized TList<Expr> toList() {
        return list();
    }


    //    public FnBaseFunctions getFnBaseFunctions(){
//        return getStructure().getBaseFunctions();
//    }
}
