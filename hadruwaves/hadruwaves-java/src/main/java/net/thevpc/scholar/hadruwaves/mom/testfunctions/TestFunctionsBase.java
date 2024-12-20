package net.thevpc.scholar.hadruwaves.mom.testfunctions;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.thevpc.scholar.hadrumaths.util.PlatformUtils;
import net.thevpc.scholar.hadruwaves.mom.HintAxisType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;
import net.thevpc.scholar.hadruwaves.mom.str.TestFunctionsComparator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 10:11:39
 */
public abstract class TestFunctionsBase implements net.thevpc.scholar.hadruwaves.mom.TestFunctions {
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
    public Vector<Expr> list() {
        return Maths.evector(arr());
    }

    @Override
    public DoubleToVector apply(int index) {
        return gp(index);
    }

    @Override
    public DoubleToVector[] arr(ProgressMonitor monitor) {
        return arr(monitor, null);
    }

    @Override
    public DoubleToVector[] arr() {
        return arr(null, null);
    }

    @Override
    public DoubleToVector[] arr(ProgressMonitor monitor, ObjectCache objectCache) {
        try {
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
                        if (monitor != null) {
                            monitor.terminate("Test functions rebuilt");
                        }
                        return cachedFunctions;
                    }
                }
                ProgressMonitor[] mons = ProgressMonitors.nonnull(monitor).split(8, 2);

                cachedFunctions = rebuildCachedFunctions(mons[0]);
                reEvalComplexField();
                if (objectCache != null) {
                    try {
                        objectCache.store("test-functions", cachedFunctions, mons[1]);
                    } catch (Exception ex) {
                        //ignore
                    }
                } else {
                    mons[1].terminate("Test functions reloaded");
                }
            }
            if (cachedFunctions.length == 0) {
                throw new IllegalArgumentException("No Test Functions defined.");
            }
            return cachedFunctions;
        }finally {
            if(monitor!=null && monitor.isTerminated()){
                monitor.terminate("Test functions rebuilt");
            }
        }
    }

    @Override
    public Domain getDomain() {
        if (cachedDomain == null) {
            cachedDomain= ExprDefaults.expandDomainForExpressions(arr());
        }
        return cachedDomain;
    }

    protected abstract DoubleToVector[] gpImpl(ProgressMonitor monitor);

    protected DoubleToVector[] rebuildCachedFunctions(ProgressMonitor monitor) {
//        if(structure==null){
//            throw new IllegalArgumentException("Unable to evaluate Test Functions since 'structure' is null");
//        }
        ProgressMonitor emon = ProgressMonitors.nonnull(monitor);
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
                        Map<String, Object> properties = PlatformUtils.merge(new HashMap(), cFunctionXY2D.getProperties());
                        if (properties0 != null) {
                            properties.putAll(properties0);
                        }
                        properties.put("Axis", "X");
                        properties.put("AxisIndex", xi);
                        xi++;
                        Expr d = Maths.vector(cFunctionXY2D.getComponent(Axis.X), Maths.CZEROXY);
                        d = d.setTitle("[X] " + cFunctionXY2D.getTitle()).toDV();
                        d = d.setProperties(properties).toDV();
                        goodX.add(d.toDV());
                    }
                    if (!cFunctionXY2D.getComponent(Axis.Y).isZero()) {
                        Map<String, Object> properties = PlatformUtils.merge(new HashMap(), cFunctionXY2D.getProperties());
                        if (properties0 != null) {
                            properties.putAll(properties0);
                        }
                        properties.put("Axis", "Y");
                        properties.put("AxisIndex", yi);
                        yi++;
                        Expr o = Maths.vector(Maths.CZEROXY, cFunctionXY2D.getComponent(Axis.Y));
                        o = o.setTitle("[Y] " + cFunctionXY2D.getTitle()).toDV();
                        o = o.setProperties(properties).toDV();
                        goodY.add(o.toDV());
                    }
                }
                goodX.addAll(goodY);
                int index = 1;
                for (int i = 0; i < goodX.size(); i++) {
                    DoubleToVector cFunctionXY2D = goodX.get(i);
                    goodX.set(i, cFunctionXY2D.setProperty("Index", index++).toDV());
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
                    Map<String, Object> properties = PlatformUtils.<String, Object>merge(new HashMap<String, Object>(), cFunctionXY2D.getProperties());
                    properties.put("Axis", "XY");
                    properties.put("Index", i);
                    properties.put("AxisIndex", i + 1);
                    validGpImpl[i] = cFunctionXY2D.setProperties(properties).toDV();
                }
                break;
            }
            case X_ONLY: {
                ArrayList<DoubleToVector> newFcts = new ArrayList<DoubleToVector>();
                int index = 1;
                for (DoubleToVector cFunctionXY2D : gpImpl) {
                    if (!cFunctionXY2D.getComponent(Axis.X).isZero()) {
                        Expr cFunctionXY2D2 = Maths.vector(cFunctionXY2D.getComponent(Axis.X), Maths.DCZERO);
                        Map<String, Object> properties = PlatformUtils.<String,Object>merge(new HashMap<String, Object>(), cFunctionXY2D2.getProperties());
                        properties.put("Axis", "X");
                        properties.put("Index", index);
                        properties.put("AxisIndex", index);
                        newFcts.add(cFunctionXY2D2.setProperties(properties).toDV());
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
                        Expr cFunctionXY2D2 = Maths.vector(Maths.ZERO, cFunctionXY2D.getComponent(Axis.Y));
                        Map<String, Object> properties = PlatformUtils.<String,Object>merge(new HashMap<>(), cFunctionXY2D2.getProperties());

//                        if (properties == null) {
//                            properties = new LinkedHashMap<String, Object>();
//                        }
                        properties.put("Axis", "Y");
                        properties.put("Index", newFcts.size() + 1);
                        properties.put("AxisIndex", index);
                        cFunctionXY2D2 = cFunctionXY2D2.setProperties(properties).toDV();
                        newFcts.add(cFunctionXY2D2.toDV());
                        index++;
                    }
                }
                validGpImpl = newFcts.toArray(new DoubleToVector[0]);
                break;
            }
        }
        TestFunctionsComparator comp = getFunctionsComparator();
        if (comp != null) {
            Arrays.sort(validGpImpl, comp);
        }
        for (int i = 0; i < validGpImpl.length; i++) {
            DoubleToVector cFunctionVector2D = validGpImpl[i];
            Map<String, Object> properties = new HashMap<>();
            properties.put("Domain", cFunctionVector2D.getComponent(Axis.X).getDomain().expand(cFunctionVector2D.getComponent(Axis.Y).getDomain()));
            properties.put("DomainX", cFunctionVector2D.getComponent(Axis.X).getDomain());
            properties.put("DomainY", cFunctionVector2D.getComponent(Axis.Y).getDomain());
            cFunctionVector2D = cFunctionVector2D.setMergedProperties(properties).toDV();
            validGpImpl[i] = cFunctionVector2D;
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
        complex = false;
        for (DoubleToVector cFunctionVector2D : cachedFunctions) {
            if (!cFunctionVector2D.getComponent(Axis.X).toDC().getImagDD().isZero() || !cFunctionVector2D.getComponent(Axis.Y).toDC().getImagDD().isZero()) {
                complex = true;
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
        HintAxisType old = this.axisType;
        this.axisType = axisType;
        firePropertyChange("axisType", old, axisType);
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = Tson.ofObj(getClass().getSimpleName());
        h.add("axisType", context.elem(axisType));
        if (functionsComparator != null) {
            h.add("functionsComparator", context.elem(functionsComparator));
        }
        return h.build();
    }

    @Override
    public TestFunctionsComparator getFunctionsComparator() {
        return functionsComparator;
    }

    @Override
    public void setFunctionsComparator(TestFunctionsComparator functionsComparator) {
        TestFunctionsComparator old = this.functionsComparator;
        this.functionsComparator = functionsComparator;
        firePropertyChange("functionsComparator", old, functionsComparator);
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
    public synchronized Vector<Expr> toList() {
        return list();
    }


    //    public FnBaseFunctions getFnBaseFunctions(){
//        return getStructure().getBaseFunctions();
//    }
}
